package com.hotel;

import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class AvailableQtyDao {

    public boolean hasVacancy(Date checkin, Date checkout, String roomType) throws SQLException {
        Connection con = null;
        PreparedStatement selectPstmt = null;
        PreparedStatement insertPstmt = null;
        ResultSet rs = null;
        RoomDao roomDao = new RoomDao(); // RoomDaoを呼び出せるようにする

        try {
            con = DBUtil.getConnection();
            String selectSql = "SELECT QTY FROM AVAILABLEQTY WHERE STAY_DATE = ? AND ROOM_TYPE = ?";
            String insertSql = "INSERT INTO AVAILABLEQTY (STAY_DATE, ROOM_TYPE, QTY) VALUES (?, ?, ?)";
            
            selectPstmt = con.prepareStatement(selectSql);
            insertPstmt = con.prepareStatement(insertSql);

            Calendar cal = Calendar.getInstance();
            cal.setTime(checkin);

            while (cal.getTime().before(checkout)) {
                String currentDateStr = DateUtil.convertToString(cal.getTime());
                selectPstmt.setString(1, currentDateStr);
                selectPstmt.setString(2, roomType.toUpperCase());
                rs = selectPstmt.executeQuery();

                if (rs.next()) {
                    // データがある場合：空室が1以上あるか確認
                    if (rs.getInt("QTY") <= 0) {
                        return false; // 満室なのでNG
                    }
                } else {
                    // データがない場合：全室空いているとみなし、新しくレコードを作成する
                    int totalRooms = roomDao.countRoomsByType(roomType);
                    if (totalRooms > 0) {
                        insertPstmt.setString(1, currentDateStr);
                        insertPstmt.setString(2, roomType.toUpperCase());
                        insertPstmt.setInt(3, totalRooms);
                        insertPstmt.executeUpdate();
                    } else {
                        return false; // そもそもその部屋タイプが存在しない
                    }
                }
                cal.add(Calendar.DATE, 1);
            }
            return true; // 全日程で空きがあった
        } finally {
            // PreparedStatementが2つあるので、それぞれ閉じる
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (selectPstmt != null) try { selectPstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (insertPstmt != null) try { insertPstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 空室数を増減させる
    public void updateVacancy(Date checkin, Date checkout, String roomType, int num) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "UPDATE AVAILABLEQTY SET QTY = QTY + ? WHERE STAY_DATE = ? AND ROOM_TYPE = ?";
            pstmt = con.prepareStatement(sql);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(checkin);

            while (cal.getTime().before(checkout)) {
                pstmt.setInt(1, num);
                pstmt.setString(2, DateUtil.convertToString(cal.getTime()));
                pstmt.setString(3, roomType.toUpperCase());
                pstmt.executeUpdate();
                cal.add(Calendar.DATE, 1);
            }
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }
}