// ファイルの中身をこれで完全に置き換えてください
package com.hotel;

import java.sql.*;
import java.util.Date;

public class ReservationDao {
    public Reservation findByReservationNumber(String reservationNumber) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reservation reservation = null;
        try {
            con = DBUtil.getConnection();
            String sql = "SELECT * FROM RESERVATION WHERE RESERVATIONNUMBER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservationNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                reservation = new Reservation();
                reservation.setReservationNumber(rs.getString("RESERVATIONNUMBER"));
                reservation.setCheckinDate(DateUtil.convertToDate(rs.getString("CHECKIN_DATE")));
                reservation.setCheckoutDate(DateUtil.convertToDate(rs.getString("CHECKOUT_DATE")));
                reservation.setRoomType(rs.getString("ROOM_TYPE"));
                reservation.setStatus(rs.getString("STATUS"));
            }
        } finally {
            DBUtil.close(rs, pstmt, con);
        }
        return reservation;
    }

    public void save(Reservation reservation) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "INSERT INTO RESERVATION (RESERVATIONNUMBER, CHECKIN_DATE, CHECKOUT_DATE, ROOM_TYPE, STATUS) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservation.getReservationNumber());
            pstmt.setString(2, DateUtil.convertToString(reservation.getCheckinDate()));
            pstmt.setString(3, DateUtil.convertToString(reservation.getCheckoutDate()));
            pstmt.setString(4, reservation.getRoomType().toUpperCase());
            pstmt.setString(5, reservation.getStatus());
            pstmt.executeUpdate();
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }
    
    public void update(Reservation reservation) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "UPDATE RESERVATION SET STATUS = ? WHERE RESERVATIONNUMBER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservation.getStatus());
            pstmt.setString(2, reservation.getReservationNumber());
            pstmt.executeUpdate();
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }

    public void delete(String reservationNumber) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "DELETE FROM RESERVATION WHERE RESERVATIONNUMBER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservationNumber);
            pstmt.executeUpdate();
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }
}