package eci.edu.back.cvds_back.model;

import eci.edu.back.cvds_back.dto.BookingDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String bookingId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private boolean disable;
    private String bookingClassRoom;

    @PersistenceCreator
    public Booking(String bookingId, LocalDate bookingDate,LocalTime bookingTime ,boolean disable, String bookingClassRoom) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.disable = disable;
        this.bookingClassRoom = bookingClassRoom;
    }

    public Booking(BookingDTO bookingDTO) {
        this.bookingTime = bookingDTO.getBookingTime();
        this.bookingDate = bookingDTO.getBookingDate();
        this.bookingId = bookingDTO.getBookingId();
        this.disable = true;
        this.bookingClassRoom = bookingDTO.getBookingClassRoom();
    }


    public boolean isDisable() {return disable;}
    public void setDisable(boolean disable) {this.disable = disable;}

    public LocalTime getBookingTime() {return bookingTime;}
    public void setBookingTime(LocalTime bookingTime) {this.bookingTime = bookingTime;}

    public String getBookingClassRoom() {return bookingClassRoom;}
    public void setBookingClassRoom(String bookingClassRoom) {}

    public String getBookingId() {return bookingId;}
    public void setBookingId(String bookingId) {this.bookingId = bookingId;}

    public LocalDate getBookingDate() {return bookingDate;}
    public void setBookingDate(LocalDate bookingDate) {this.bookingDate = bookingDate;}
}
