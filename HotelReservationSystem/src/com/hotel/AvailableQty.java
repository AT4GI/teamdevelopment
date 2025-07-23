package com.hotel;

import java.util.Date;

public class AvailableQty {
    private Date stayDate;
    private String roomType;
    private int quantity;

    // Getters and Setters
    public Date getStayDate() { return stayDate; }
    public void setStayDate(Date stayDate) { this.stayDate = stayDate; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}