package com.hotel;

import java.util.Date;

public class Reservation {
    private String reservationNumber;
    private Date checkinDate;
    private Date checkoutDate;
    private String roomType;
    private String status;

    // Getters and Setters
    public String getReservationNumber() { return reservationNumber; }
    public void setReservationNumber(String r) { this.reservationNumber = r; }
    public Date getCheckinDate() { return checkinDate; }
    public void setCheckinDate(Date d) { this.checkinDate = d; }
    public Date getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(Date d) { this.checkoutDate = d; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}