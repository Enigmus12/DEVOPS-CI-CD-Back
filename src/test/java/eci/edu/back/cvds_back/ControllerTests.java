package eci.edu.back.cvds_back;
import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.controller.BookingController;
import eci.edu.back.cvds_back.controller.BookingGeneratorController;
import eci.edu.back.cvds_back.controller.UserController;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.impl.BookingGeneratorServiceImpl;
import eci.edu.back.cvds_back.service.interfaces.BookingGeneratorService;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ControllerTests {

    // Mocks para pruebas de Booking
    @Mock
    private BookingService mockBookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDTO bookingDTO;
    private Booking booking;
    private List<Booking> bookingList;

    // Mocks para pruebas de User
    @Mock
    private UserService mockUserService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private User user;
    private List<User> userList;

    // Mocks para pruebas de BookingGenerator
    @Mock
    private BookingGeneratorService mockBookingGeneratorService;

    @InjectMocks
    private BookingGeneratorController bookingGeneratorController;

    @InjectMocks
    private BookingGeneratorServiceImpl bookingGeneratorService;

    @BeforeEach
    void setUp() throws BookingServiceException {
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

        // Configuramos los mocks para Booking
        when(mockBookingService.getAllBookings()).thenReturn(bookingList);
        when(mockBookingService.getBooking("test123")).thenReturn(booking);
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(booking);

        // Configure with ReflectionTestUtils
        ReflectionTestUtils.setField(bookingController, "bookingService", mockBookingService);

        // Configuración para BookingGeneratorService
        when(mockBookingGeneratorService.generateRandomBookings(anyInt(), anyInt()))
                .thenReturn(bookingList);
        when(mockBookingGeneratorService.generateExactBookings(anyInt()))
                .thenReturn(bookingList);
        when(mockBookingGeneratorService.clearAllBookings())
                .thenReturn(1);

        // Configuración inicial para pruebas de User
        userDTO = new UserDTO();
        userDTO.setUserId("user123");
        userDTO.setEmail("testuser");
        userDTO.setPassword("123456789");

        user = new User(userDTO);
        userList = new ArrayList<>();
        userList.add(user);

        // Configuramos los mocks para User
        when(mockUserService.getAllUsers()).thenReturn(userList);
        when(mockUserService.getUser("user123")).thenReturn(user);
        when(mockUserService.saveUser(any(UserDTO.class))).thenReturn(user);

        // Configure with ReflectionTestUtils
        ReflectionTestUtils.setField(userController, "userService", mockUserService);

        // Inyección del mock en el controlador
        ReflectionTestUtils.setField(bookingGeneratorController, "bookingGeneratorService", mockBookingGeneratorService);
        // Set BookingService mock in BookingGeneratorServiceImpl
        ReflectionTestUtils.setField(bookingGeneratorService, "bookingService", mockBookingService);
    }

    // Tests para BookingController
    @Test
    void testBookings() {
        List<Booking> result = bookingController.bookings();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockBookingService).getAllBookings();
    }

    @Test
    void testBookingById() throws BookingServiceException {
        Booking result = bookingController.booking("test123");
        assertNotNull(result);
        assertEquals("test123", result.getBookingId());
        verify(mockBookingService).getBooking("test123");
    }

    @Test
    void testCreateBooking() throws BookingServiceException {
        Booking result = bookingController.booking(bookingDTO);
        assertNotNull(result);
        verify(mockBookingService).saveBooking(bookingDTO);
    }

    @Test
    void testDeleteBookingController() throws BookingServiceException {
        List<Booking> result = bookingController.deleteBooking("test123");
        assertNotNull(result);
        verify(mockBookingService).deleteBooking("test123");
        verify(mockBookingService).getAllBookings();
    }





    // Tests para BookingGeneratorController
    @Test
    void testGenerateBookings() {
        ResponseEntity<Map<String, Object>> response = bookingGeneratorController.generateBookings(10, 20);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Successfully generated 1 bookings", body.get("message"));
        assertEquals(1, body.get("totalGenerated"));

        verify(mockBookingGeneratorService).generateRandomBookings(10, 20);
    }

    @Test
    void testGenerateBookingsWithDefaultValues() {
        ResponseEntity<Map<String, Object>> response = bookingGeneratorController.generateBookings(100, 1000);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(mockBookingGeneratorService).generateRandomBookings(100, 1000);
    }

    @Test
    void testGenerateExactBookingsWithDefaultValue() {
        ResponseEntity<Map<String, Object>> response = bookingGeneratorController.generateExactBookings(100);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(mockBookingGeneratorService).generateExactBookings(100);
    }

    // Tests para UserController
    @Test
    void testUsersController() {
        List<User> result = userController.users();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockUserService).getAllUsers();
    }

    @Test
    void testUserByIdController() throws UserServiceException {
        User result = userController.user("user123");
        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        verify(mockUserService).getUser("user123");
    }

    @Test
    void testCreateUserController() {
        User result = userController.user(userDTO);
        assertNotNull(result);
        verify(mockUserService).saveUser(userDTO);
    }

    @Test
    void testDeleteUserController() throws UserServiceException {
        List<User> result = userController.deleteUser("user123");
        assertNotNull(result);
        verify(mockUserService).deleteUser("user123");
        verify(mockUserService).getAllUsers();
    }
}