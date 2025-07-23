package com.hotel;
import java.sql.*;
import java.util.Date;


public class ReservationDao {

    // 予約番号で予約情報を取得
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
                reservation.setStayingDate(DateUtil.convertToDate(rs.getString("STAYINGDATE")));
                reservation.setStatus(rs.getString("STATUS"));
            }
        } finally {
            DBUtil.close(rs, pstmt, con);
        }
        return reservation;
    }

    // 予約情報をDBに保存
    public void save(Reservation reservation) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            String sql = "INSERT INTO RESERVATION (RESERVATIONNUMBER, STAYINGDATE, STATUS) VALUES (?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservation.getReservationNumber());
            pstmt.setString(2, DateUtil.convertToString(reservation.getStayingDate()));
            pstmt.setString(3, reservation.getStatus());
            pstmt.executeUpdate();
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }
    
    // 予約情報を更新
    public void update(Reservation reservation) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBUtil.getConnection();
            String sql = "UPDATE RESERVATION SET STAYINGDATE = ?, STATUS = ? WHERE RESERVATIONNUMBER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, DateUtil.convertToString(reservation.getStayingDate()));
            pstmt.setString(2, reservation.getStatus());
            pstmt.setString(3, reservation.getReservationNumber());
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