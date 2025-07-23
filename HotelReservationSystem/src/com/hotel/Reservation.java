package com.hotel;
import java.util.Date;

public class Reservation {
    private String reservationNumber;
    private Date stayingDate;
    private String status;

    // Getters and Setters
    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Date getStayingDate() {
        return stayingDate;
    }

    public void setStayingDate(Date stayingDate) {
        this.stayingDate = stayingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation [reservationNumber=" + reservationNumber +
               ", stayingDate=" + DateUtil.convertToString(stayingDate) +
               ", status=" + status + "]";
    }
}