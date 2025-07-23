package com.hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomDao {

    // 空いている部屋を1つ見つける
    public Room findEmptyRoom() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Room room = null;
        try {
            con = DBUtil.getConnection();
            // STAYINGDATEがNULLまたは空文字の部屋を探す
            String sql = "SELECT * FROM ROOM WHERE STAYINGDATE IS NULL OR STAYINGDATE = '' LIMIT 1";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                room = new Room();
                room.setRoomNumber(rs.getString("ROOMNUMBER"));
            }
        } finally {
            DBUtil.close(rs, pstmt, con);
        }
        return room;
    }

    // 部屋情報を更新する (チェックイン/チェックアウトで利用)
    public void update(Room room) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtil.getConnection();
            String sql = "UPDATE ROOM SET STAYINGDATE = ? WHERE ROOMNUMBER = ?";
            pstmt = con.prepareStatement(sql);
            
            if (room.getStayingDate() != null) {
                pstmt.setString(1, DateUtil.convertToString(room.getStayingDate()));
            } else {
                pstmt.setString(1, null); // チェックアウト時は日付をNULLに
            }
            pstmt.setString(2, room.getRoomNumber());
            pstmt.executeUpdate();
        } finally {
            DBUtil.close(null, pstmt, con);
        }
    }
}