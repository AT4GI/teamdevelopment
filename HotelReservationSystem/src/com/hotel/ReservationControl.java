// ファイルの中身をこれで完全に置き換えてください
package com.hotel;

import java.util.Date;
import java.sql.SQLException;

public class ReservationControl {
    private ReservationDao reservationDao = new ReservationDao();
    private AvailableQtyDao availableQtyDao = new AvailableQtyDao();

    public String reserveRoom(Date checkin, Date checkout, String roomType) {
        try {
            // 1. 空室があるか確認
            if (!availableQtyDao.hasVacancy(checkin, checkout, roomType)) {
                System.out.println("申し訳ありません。ご指定の期間・部屋タイプに空室がありません。");
                return null;
            }

            // 2. 予約を作成
            String reservationNumber = String.valueOf(System.currentTimeMillis());
            Reservation reservation = new Reservation();
            reservation.setReservationNumber(reservationNumber);
            reservation.setCheckinDate(checkin);
            reservation.setCheckoutDate(checkout);
            reservation.setRoomType(roomType);
            reservation.setStatus("create");
            reservationDao.save(reservation);

            // 3. 空室数を減らす
            availableQtyDao.updateVacancy(checkin, checkout, roomType, -1);

            System.out.println("予約が完了しました。");
            return reservationNumber;
        } catch (SQLException e) {
            System.err.println("予約処理中にデータベースエラーが発生しました。");
            e.printStackTrace();
            return null;
        }
    }

    public void cancelReservation(String reservationNumber) {
        try {
            Reservation reservation = reservationDao.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                System.out.println("エラー: その予約番号は存在しません。");
                return;
            }
            if ("consume".equals(reservation.getStatus())) {
                System.out.println("エラー: チェックイン済みの予約はキャンセルできません。");
                return;
            }
            
            // 1. 予約を削除
            reservationDao.delete(reservationNumber);

            // 2. 空室数を元に戻す
            availableQtyDao.updateVacancy(reservation.getCheckinDate(), reservation.getCheckoutDate(), reservation.getRoomType(), 1);

            System.out.println("予約番号 " + reservationNumber + " のキャンセルが完了しました。");
        } catch (SQLException e) {
            System.err.println("データベースエラーにより予約キャンセルに失敗しました。");
            e.printStackTrace();
        }
    }
}