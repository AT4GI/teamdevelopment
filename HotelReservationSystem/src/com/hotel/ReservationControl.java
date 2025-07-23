package com.hotel;

import java.util.Date;
import java.sql.SQLException;

public class ReservationControl {
    private ReservationDao reservationDao = new ReservationDao();

    public String reserveRoom(Date stayingDate) {
        String reservationNumber = String.valueOf(System.currentTimeMillis());
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(reservationNumber);
        reservation.setStayingDate(stayingDate);
        reservation.setStatus("create");
        try {
            reservationDao.save(reservation);
            System.out.println("予約が完了しました。");
            return reservationNumber;
        } catch (SQLException e) {
            System.err.println("予約に失敗しました: " + e.getMessage());
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
            reservationDao.delete(reservationNumber);
            System.out.println("予約番号 " + reservationNumber + " のキャンセルが完了しました。");
        } catch (SQLException e) {
            System.err.println("データベースエラーにより予約キャンセルに失敗しました。");
            e.printStackTrace();
        }
    }
}