package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DtoTests {

    // Tests para BookingDTO
    @Test
    void testBookingDTOGettersAndSetters() {
        BookingDTO dto = new BookingDTO();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        dto.setBookingId("testId");
        dto.setBookingDate(date);
        dto.setBookingTime(time);
        dto.setDisable(true);
        dto.setBookingClassRoom("ClassRoom1");

        assertEquals("testId", dto.getBookingId());
        assertEquals(date, dto.getBookingDate());
        assertEquals(time, dto.getBookingTime());
        assertTrue(dto.getDisable());
        assertEquals("ClassRoom1", dto.getBookingClassRoom());
    }

    // Tests para UserDTO
    @Test
    void testUserDTOGettersAndSetters() {
        UserDTO dto = new UserDTO();

        dto.setUserId("testId");
        dto.setEmail("testUsername");
        dto.setPassword("Juanito123");
        dto.setPasswordConfirmation("Juanito123");

        assertEquals("testId", dto.getUserId());
        assertEquals("testUsername", dto.getEmail());
        assertEquals("Juanito123", dto.getPassword());
        assertEquals("Juanito123", dto.getPasswordConfirmation());
    }
}