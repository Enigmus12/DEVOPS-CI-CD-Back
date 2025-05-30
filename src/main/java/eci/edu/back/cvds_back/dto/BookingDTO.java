package eci.edu.back.cvds_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * The BookingDTO class represents a data transfer object for booking information.
 * It contains details about a booking such as its ID, date, time, classroom, 
 * priority, and whether it is disabled.
 */
public class BookingDTO {
    private String bookingId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private boolean disable;
    private String bookingClassRoom;
    private Integer priority;

    public boolean getDisable() {return disable;}
    public void setDisable(boolean disable) {this.disable = disable;}

    public LocalTime getBookingTime() {return bookingTime;}
    public void setBookingTime(LocalTime bookingTime) {this.bookingTime = bookingTime;}

    public String getBookingClassRoom() {return bookingClassRoom;}
    public void setBookingClassRoom(String bookingClassRoom) {this.bookingClassRoom = bookingClassRoom;}

    public String getBookingId() {return bookingId;}
    public void setBookingId(String bookingId) {this.bookingId = bookingId;}

    public LocalDate getBookingDate() {return bookingDate;}
    public void setBookingDate(LocalDate bookingDate) {this.bookingDate = bookingDate;}

    public Integer getPriority() {return priority;}
    public void setPriority(Integer priority) {this.priority = priority;}

}