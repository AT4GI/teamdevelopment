package com.hotel;

import java.sql.SQLException;

public class CheckOutControl {
    private RoomDao roomDao = new RoomDao();

    public void checkOut(String roomNumber) {
        try {
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setStayingDate(null); // 滞在日をNULLにして部屋を空ける
            roomDao.update(room);
            System.out.println("お部屋番号 " + roomNumber + " のチェックアウトが完了しました。");
        } catch (SQLException e) {
            System.err.println("データベースエラーによりチェックアウトに失敗しました。");
            e.printStackTrace();
        }
    }
}