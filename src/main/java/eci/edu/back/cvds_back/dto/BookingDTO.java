package eci.edu.back.cvds_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

//@Getter
//@Setter
/**
 * The BookingDTO class represents a Data Transfer Object for booking information.
 * It contains details about a booking such as its ID, date, time, classroom, priority, 
 * and whether it is disabled.
 */
public class BookingDTO {
    private String bookingId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private boolean disable;
    private String bookingClassRoom;
    private Integer priority;


    /**
     * Retrieves the disable status of the booking.
     *
     * @return {@code true} if the booking is disabled, {@code false} otherwise.
     */
    public boolean getDisable() {return disable;}

    /**
     * Sets the disable status of the booking.
     *
     * @param disable A boolean value indicating whether the booking should be disabled (true) or enabled (false).
     */
    public void setDisable(boolean disable) {this.disable = disable;}

    /**
     * Retrieves the booking time associated with this booking.
     *
     * @return the booking time as a {@link java.time.LocalTime} object
     */
    public LocalTime getBookingTime() {return bookingTime;}

    /**
     * Sets the booking time for this booking.
     *
     * @param bookingTime the time to set for the booking, represented as a LocalTime object
     */
    public void setBookingTime(LocalTime bookingTime) {this.bookingTime = bookingTime;}

    /**
     * Retrieves the classroom associated with the booking.
     *
     * @return A string representing the classroom for the booking.
     */
    public String getBookingClassRoom() {return bookingClassRoom;}

    /**
     * Sets the classroom associated with the booking.
     *
     * @param bookingClassRoom the name or identifier of the classroom to set
     */
    public void setBookingClassRoom(String bookingClassRoom) {this.bookingClassRoom = bookingClassRoom;}

    /**
     * Retrieves the unique identifier of the booking.
     *
     * @return the booking ID as a String.
     */
    public String getBookingId() {
        return bookingId;
    }


    /**
     * Sets the booking ID for this booking.
     *
     * @param bookingId the unique identifier of the booking to set
     */
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Retrieves the booking date.
     *
     * @return the date of the booking as a {@link LocalDate}.
     */
    public LocalDate getBookingDate() {return bookingDate;}

    /**
     * Sets the booking date for this booking.
     *
     * @param bookingDate the date to set as the booking date
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Retrieves the priority of the booking.
     *
     * @return the priority as an Integer.
     */
    public Integer getPriority() {return priority;}

    /**
     * Sets the priority of the booking.
     *
     * @param priority the priority level to set, represented as an Integer
     */
    public void setPriority(Integer priority) {this.priority = priority;}
}
