package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.BookingDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a booking entity in the system.
 * This class is annotated with @Document to indicate that it is a MongoDB document.
 * It contains details about a booking, such as its ID, date, time, classroom, priority, 
 * and the user who reserved it.
 * 
 * <p>Key Features:</p>
 * <ul>
 *   <li>Stores booking details including date, time, and classroom.</li>
 *   <li>Tracks whether the booking is disabled or available.</li>
 *   <li>Supports priority levels for bookings.</li>
 *   <li>Includes the user ID of the person who reserved the booking.</li>
 * </ul>
 * 
 * <p>Constructors:</p>
 * <ul>
 *   <li>A full constructor for initializing all fields.</li>
 *   <li>A constructor that initializes a booking from a BookingDTO object, 
 *       setting the booking as available by default.</li>
 * </ul>
 * 
 * <p>Getters and Setters:</p>
 * Provides getter and setter methods for all fields, allowing for 
 * encapsulated access and modification of booking details.
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>@Document: Specifies the MongoDB collection name ("bookings").</li>
 *   <li>@Id: Marks the bookingId field as the unique identifier for the document.</li>
 *   <li>@PersistenceCreator: Indicates the constructor used for persistence operations.</li>
 * </ul>
 * 
 * <p>Usage:</p>
 * This class is used to represent and manage booking data within the application, 
 * particularly in scenarios involving database operations and business logic.
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
    private String reservedBy; // Almacena el userId del usuario que realizó la reserva

    /**
     * Constructs a new Booking instance with the specified details.
     *
     * @param bookingId         the unique identifier for the booking
     * @param bookingDate       the date of the booking
     * @param bookingTime       the time of the booking
     * @param disable           indicates whether the booking is disabled
     * @param bookingClassRoom  the classroom associated with the booking
     * @param priority          the priority level of the booking
     * @param reservedBy        the user who reserved the booking
     */
    @PersistenceCreator
    public Booking(String bookingId, LocalDate bookingDate, LocalTime bookingTime, boolean disable, String bookingClassRoom, Integer priority, String reservedBy) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.disable = disable;
        this.bookingClassRoom = bookingClassRoom;
        this.priority = priority;
        this.reservedBy = reservedBy;
    }

    /**
     * Constructs a new Booking object using the provided BookingDTO.
     * 
     * @param bookingDTO The data transfer object containing the booking details.
     *                    - bookingTime: The time of the booking.
     *                    - bookingDate: The date of the booking.
     *                    - bookingId: The unique identifier for the booking.
     *                    - bookingClassRoom: The classroom associated with the booking.
     *                    - priority: The priority level of the booking.
     * 
     * Note:
     * - The booking is initialized as available (disable = true).
     * - The reservedBy field is not assigned during initialization, as the booking
     *   does not have a reservation initially.
     */
    public Booking(BookingDTO bookingDTO) {
        this.bookingTime = bookingDTO.getBookingTime();
        this.bookingDate = bookingDTO.getBookingDate();
        this.bookingId = bookingDTO.getBookingId();
        this.disable = true; // Los bookings se crean inicialmente como disponibles (disable=true)
        this.bookingClassRoom = bookingDTO.getBookingClassRoom();
        this.priority = bookingDTO.getPriority();
        // No asignamos reservedBy aquí, porque inicialmente no tiene reserva
    }

    public boolean isDisable() {return disable;}
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

    public String getReservedBy() {return reservedBy;}
    public void setReservedBy(String reservedBy) {this.reservedBy = reservedBy;}
}