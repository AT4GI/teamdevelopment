package com.hotel;

import java.sql.SQLException;
import java.util.Date;

public class CheckInControl {
    private ReservationDao reservationDao = new ReservationDao();
    private RoomDao roomDao = new RoomDao();

    public void checkIn(String reservationNumber) {
        try {
            // 1. 予約を確認し、使用済みにする
            Reservation reservation = reservationDao.findByReservationNumber(reservationNumber);
            if (reservation == null) {
                System.out.println("エラー: その予約番号は存在しません。");
                return;
            }
            if ("consume".equals(reservation.getStatus())) {
                System.out.println("エラー: この予約は既に使用済みです。");
                return;
            }
            reservation.setStatus("consume");
            reservationDao.update(reservation);
            
            // 2. 空いている部屋を探して割り当てる
            Room room = roomDao.findEmptyRoom();
            if (room == null) {
                System.out.println("エラー: 満室です。チェックインできません。");
                // (本来は予約を元に戻す処理が必要)
                return;
            }
            room.setStayingDate(reservation.getCheckinDate()); // ★ここを修正しました★
            roomDao.update(room);

            System.out.println("チェックインが完了しました。お部屋番号は " + room.getRoomNumber() + " です。");

        } catch (SQLException e) {
            System.err.println("データベースエラーによりチェックインに失敗しました。");
            e.printStackTrace();
        }
    }
}