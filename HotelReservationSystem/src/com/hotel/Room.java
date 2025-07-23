package com.hotel;

import java.util.Date;

public class Room {
    private String roomNumber;
    private String roomType;
    private int price;
    private Date stayingDate;

    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public Date getStayingDate() { return stayingDate; }
    public void setStayingDate(Date stayingDate) { this.stayingDate = stayingDate; }
}