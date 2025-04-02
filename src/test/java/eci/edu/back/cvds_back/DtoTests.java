package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.dto.AuthenticationResponseDTO;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.User;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    @Test
    void testAuthenticationResponseDTOGettersAndSetters() {
        User user = new User(null, null, null, null);
        user.setUserId("testUser");
        
        AuthenticationResponseDTO dto = new AuthenticationResponseDTO(true, user, "testToken", "Success");

        assertTrue(dto.isAuthenticated());
        assertEquals(user, dto.getUser());
        assertEquals("testToken", dto.getToken());
        assertEquals("Success", dto.getMessage());

        // Modificación y prueba de setters
        User newUser = new User(null, null, null, null);
        newUser.setUserId("newUser");
        
        dto.setAuthenticated(false);
        dto.setUser(newUser);
        dto.setToken("newToken");
        dto.setMessage("Failed");

        assertFalse(dto.isAuthenticated());
        assertEquals(newUser, dto.getUser());
        assertEquals("newToken", dto.getToken());
        assertEquals("Failed", dto.getMessage());
    }
}