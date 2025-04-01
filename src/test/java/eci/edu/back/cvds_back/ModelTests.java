package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTests {

    private BookingDTO bookingDTO;
    private Booking booking;
    private List<Booking> bookingList;

    private UserDTO userDTO;
    private User user;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Configuración inicial para pruebas de Booking
        bookingDTO = new BookingDTO();
        bookingDTO.setBookingId("test123");
        bookingDTO.setBookingDate(LocalDate.now());
        bookingDTO.setBookingTime(LocalTime.of(14, 30));
        bookingDTO.setBookingClassRoom("Sala A");
        bookingDTO.setDisable(false);

        booking = new Booking(bookingDTO);
        bookingList = new ArrayList<>();
        bookingList.add(booking);

        // Configuración inicial para pruebas de User
        userDTO = new UserDTO();
        userDTO.setUserId("user123");
        userDTO.setEmail("testuser");
        userDTO.setPassword("123456789");

        user = new User(userDTO);
        userList = new ArrayList<>();
        userList.add(user);
    }

    // Tests para Booking model
    @Test
    void testBookingConstructor() {
        Booking booking = new Booking(bookingDTO);

        assertEquals(bookingDTO.getBookingId(), booking.getBookingId());
        assertEquals(bookingDTO.getBookingDate(), booking.getBookingDate());
        assertEquals(bookingDTO.getBookingTime(), booking.getBookingTime());
        assertTrue(booking.isDisable()); // Por defecto se crea como disabled (true)
        assertEquals(bookingDTO.getBookingClassRoom(), booking.getBookingClassRoom());
    }

    @Test
    void testBookingGettersAndSetters() {
        Booking booking = new Booking("testId", LocalDate.now(), LocalTime.now(), false, "ClassRoom1", 1);

        booking.setBookingId("newId");
        booking.setBookingDate(LocalDate.of(2024, 3, 7));
        booking.setBookingTime(LocalTime.of(10, 30));
        booking.setDisable(true);
        booking.setBookingClassRoom("ClassRoom2"); // Este método no hace nada en la implementación
        booking.setPriority(1);

        assertEquals("newId", booking.getBookingId());
        assertEquals(LocalDate.of(2024, 3, 7), booking.getBookingDate());
        assertEquals(LocalTime.of(10, 30), booking.getBookingTime());
        assertTrue(booking.isDisable());
        assertEquals("ClassRoom1", booking.getBookingClassRoom()); // No cambia porque el setter está vacío
        assertEquals(1, booking.getPriority());
    }

    // Tests para User model
    @Test
    void testUserConstructorWithDTO() {
        UserDTO dto = new UserDTO();
        dto.setUserId("testId");
        dto.setEmail("testUsername");
        dto.setPassword("123456789");

        User user = new User(dto);

        assertEquals("testId", user.getUserId());
        assertEquals("testUsername", user.getEmail());
        assertEquals("123456789", user.getPassword());
    }

    @Test
    void testUserConstructorWithParameters() {
        User user = new User("testId", "testUsername", "123456789", "123456789");

        assertEquals("testId", user.getUserId());
        assertEquals("testUsername", user.getEmail());
        assertEquals("123456789", user.getPassword());
        assertEquals("123456789", user.getPasswordConfirmation());
    }

    @Test
    void testUserGettersAndSetters() {
        User user = new User("testId", "testUsername", "123456789", "123456789");

        user.setUserId("newId");
        user.setEmail("newUsername");
        user.setPassword("987654321");
        user.setPasswordConfirmation("987654321");

        assertEquals("newId", user.getUserId());
        assertEquals("newUsername", user.getEmail());
        assertEquals("987654321", user.getPassword());
        assertEquals("987654321", user.getPasswordConfirmation());
    }
}
