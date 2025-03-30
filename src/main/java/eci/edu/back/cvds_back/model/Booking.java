package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.BookingDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a booking entity in the system.
 * This class is mapped to the "bookings" collection in the database.
 * It contains information about a booking such as its ID, date, time, 
 * classroom, priority, and whether it is disabled.
 */
@Document(collection = "bookings")
public class Booking {
    @Id
    private String bookingId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private boolean disable;
    private String bookingClassRoom;
    private Integer priority;

    /**
     * Constructs a new Booking instance with the specified details.
     *
     * @param bookingId         the unique identifier for the booking
     * @param bookingDate       the date of the booking
     * @param bookingTime       the time of the booking
     * @param disable           indicates whether the booking is disabled
     * @param bookingClassRoom  the classroom associated with the booking
     * @param priority          the priority level of the booking
     */
    @PersistenceCreator
    public Booking(String bookingId, LocalDate bookingDate,LocalTime bookingTime ,boolean disable, String bookingClassRoom, Integer priority) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.disable = disable;
        this.bookingClassRoom = bookingClassRoom;
        this.priority = priority;
    }

    /**
     * Constructs a new Booking object using the provided BookingDTO.
     *
     * @param bookingDTO the data transfer object containing the booking details
     *                   such as booking time, booking date, booking ID, classroom,
     *                   and priority.
     *                   The disable property is set to true by default.
     */
    public Booking(BookingDTO bookingDTO) {
        this.bookingTime = bookingDTO.getBookingTime();
        this.bookingDate = bookingDTO.getBookingDate();
        this.bookingId = bookingDTO.getBookingId();
        this.disable = true;
        this.bookingClassRoom = bookingDTO.getBookingClassRoom();
        this.priority = bookingDTO.getPriority();
    }


    /**
     * Checks if the booking is disabled.
     *
     * @return {@code true} if the booking is disabled, {@code false} otherwise.
     */
    public boolean isDisable() {return disable;}

    /**
     * Sets the disable status of the booking.
     *
     * @param disable a boolean indicating whether the booking should be disabled (true) or enabled (false).
     */
    public void setDisable(boolean disable) {this.disable = disable;}

    /**
     * Retrieves the booking time.
     *
     * @return the time of the booking as a {@link java.time.LocalTime} object.
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
     * @param bookingClassRoom the identifier or name of the classroom to be set
     */
    public void setBookingClassRoom(String bookingClassRoom) {}

    /**
     * Retrieves the unique identifier for the booking.
     *
     * @return the booking ID as a String.
     */
    public String getBookingId() {return bookingId;}

    /**
     * Sets the booking ID for this booking.
     *
     * @param bookingId the unique identifier for the booking
     */
    public void setBookingId(String bookingId) {this.bookingId = bookingId;}

    /**
     * Retrieves the booking date.
     *
     * @return the date of the booking as a {@link java.time.LocalDate}.
     */
    public LocalDate getBookingDate() {return bookingDate;}

    /**
     * Sets the booking date for this booking.
     *
     * @param bookingDate the date to set as the booking date
     */
    public void setBookingDate(LocalDate bookingDate) {this.bookingDate = bookingDate;}

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