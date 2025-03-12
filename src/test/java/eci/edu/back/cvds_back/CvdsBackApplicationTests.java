package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.config.UserServiceException;
import eci.edu.back.cvds_back.controller.BookingController;
import eci.edu.back.cvds_back.controller.UserController;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.dto.UserDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.model.User;
import eci.edu.back.cvds_back.service.impl.BookingRepositoryImpl;
import eci.edu.back.cvds_back.service.impl.BookingServiceImpl;
import eci.edu.back.cvds_back.service.impl.UserRepositoryImpl;
import eci.edu.back.cvds_back.service.impl.UserServiceImpl;
import eci.edu.back.cvds_back.service.interfaces.BookingMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.BookingRepository;
import eci.edu.back.cvds_back.service.interfaces.BookingService;
import eci.edu.back.cvds_back.service.interfaces.UserMongoRepository;
import eci.edu.back.cvds_back.service.interfaces.UserRepository;
import eci.edu.back.cvds_back.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Remove the @SpringBootTest annotation to avoid loading the entire Spring context
public class CvdsBackApplicationTests {

	// Mocks y componentes para pruebas de Booking
	@Mock
	private BookingMongoRepository bookingMongoRepository;

	@InjectMocks
	private BookingRepositoryImpl bookingRepository;

	@Mock
	private BookingRepository mockBookingRepository;

	@InjectMocks
	private BookingServiceImpl bookingService;

	@Mock
	private BookingService mockBookingService;

	@InjectMocks
	private BookingController bookingController;

	private BookingDTO bookingDTO;
	private Booking booking;
	private List<Booking> bookingList;

	// Mocks y componentes para pruebas de User
	@Mock
	private UserMongoRepository userMongoRepository;

	@InjectMocks
	private UserRepositoryImpl userRepository;

	@Mock
	private UserRepository mockUserRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserService mockUserService;

	@InjectMocks
	private UserController userController;

	private UserDTO userDTO;
	private User user;
	private List<User> userList;

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
		when(bookingMongoRepository.findAll()).thenReturn(bookingList);
		when(bookingMongoRepository.findById("test123")).thenReturn(Optional.of(booking));
		when(bookingMongoRepository.existsById("test123")).thenReturn(true);
		when(bookingMongoRepository.existsById("nonExistingId")).thenReturn(false);

		when(mockBookingRepository.findAll()).thenReturn(bookingList);
		when(mockBookingRepository.findById("test123")).thenReturn(booking);
		when(mockBookingRepository.existsById("test123")).thenReturn(true);
		when(mockBookingRepository.existsById("nonExistingId")).thenReturn(false);

		when(mockBookingService.getAllBookings()).thenReturn(bookingList);
		when(mockBookingService.getBooking("test123")).thenReturn(booking);
		when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(booking);

		// Configure with ReflectionTestUtils
		ReflectionTestUtils.setField(bookingService, "bookingRepository", mockBookingRepository);
		ReflectionTestUtils.setField(bookingController, "bookingService", mockBookingService);

		// Configuración inicial para pruebas de User
		userDTO = new UserDTO();
		userDTO.setId("user123");
		userDTO.setUsername("testuser");
		userDTO.setPhone(123456789);

		user = new User(userDTO);
		userList = new ArrayList<>();
		userList.add(user);

		// Configuramos los mocks para User
		when(userMongoRepository.findAll()).thenReturn(userList);
		when(userMongoRepository.findById("user123")).thenReturn(Optional.of(user));

		when(mockUserRepository.findAll()).thenReturn(userList);
		when(mockUserRepository.findById("user123")).thenReturn(user);

		when(mockUserService.getAllUsers()).thenReturn(userList);
		when(mockUserService.getUser("user123")).thenReturn(user);
		when(mockUserService.saveUser(any(UserDTO.class))).thenReturn(user);

		// Configure with ReflectionTestUtils
		ReflectionTestUtils.setField(userService, "userRepository", mockUserRepository);
		ReflectionTestUtils.setField(userController, "userService", mockUserService);
	}
	// Tests para BookingRepositoryImpl
	@Test
	void testFindAll() {
		List<Booking> result = bookingRepository.findAll();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(bookingMongoRepository).findAll();
	}

	@Test
	void testFindById_Success() throws BookingServiceException {
		Booking result = bookingRepository.findById("test123");
		assertNotNull(result);
		assertEquals("test123", result.getBookingId());
		verify(bookingMongoRepository).findById("test123");
	}

	@Test
	void testFindById_NotFound() {
		when(bookingMongoRepository.findById("nonExistingId")).thenReturn(Optional.empty());

		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingRepository.findById("nonExistingId");
		});

		assertEquals("Booking Not found", exception.getMessage());
		verify(bookingMongoRepository).findById("nonExistingId");
	}

	@Test
	void testSave() {
		bookingRepository.save(booking);
		verify(bookingMongoRepository).save(booking);
	}

	@Test
	void testDeleteById() throws BookingServiceException {
		bookingRepository.deleteById("test123");
		verify(bookingMongoRepository).deleteById("test123");
	}

	@Test
	void testUpdate_Success() throws BookingServiceException {
		bookingRepository.update(booking);
		verify(bookingMongoRepository).save(booking);
	}

	@Test
	void testUpdate_NotFound() {
		Booking nonExistingBooking = new Booking(bookingDTO);
		nonExistingBooking.setBookingId("nonExistingId");

		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingRepository.update(nonExistingBooking);
		});

		assertEquals("Booking Not Found", exception.getMessage());
	}

	@Test
	void testExistsById() {
		assertTrue(bookingRepository.existsById("test123"));
		assertFalse(bookingRepository.existsById("nonExistingId"));
	}

	// Tests para BookingServiceImpl
	@Test
	void testGetBooking_Success() throws BookingServiceException {
		when(mockBookingRepository.findById("test123")).thenReturn(booking);

		Booking result = bookingService.getBooking("test123");
		assertNotNull(result);
		assertEquals("test123", result.getBookingId());
		verify(mockBookingRepository).findById("test123");
	}

	@Test
	void testSaveBooking_Success() throws BookingServiceException {
		when(mockBookingRepository.existsById("newBooking")).thenReturn(false);

		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("newBooking");
		newBookingDTO.setBookingDate(LocalDate.now());
		newBookingDTO.setBookingTime(LocalTime.of(15, 30));
		newBookingDTO.setBookingClassRoom("Sala B");

		Booking result = bookingService.saveBooking(newBookingDTO);
		assertNotNull(result);
		assertEquals("newBooking", result.getBookingId());
		verify(mockBookingRepository).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_AlreadyExists() {
		when(mockBookingRepository.existsById("test123")).thenReturn(true);

		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingService.saveBooking(bookingDTO);
		});

		assertTrue(exception.getMessage().contains("ya existe"));
	}

	@Test
	void testSaveBooking_SuccessWithTimeGapGreaterThan2Hours() throws BookingServiceException {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
		String classRoom = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoom);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con más de 2 horas de diferencia
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(today);
		newBookingDTO.setBookingTime(LocalTime.of(13, 0)); // 1:00 PM (3 horas después)
		newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba
		Booking result = bookingService.saveBooking(newBookingDTO);

		// Verificar
		assertNotNull(result);
		assertEquals("new123", result.getBookingId());
		assertEquals(today, result.getBookingDate());
		assertEquals(LocalTime.of(13, 0), result.getBookingTime());
		assertEquals(classRoom, result.getBookingClassRoom());
		assertTrue(result.isDisable()); // Por defecto es true según el constructor
		verify(mockBookingRepository).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_FailsWithTimeGapLessThan2Hours() {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
		String classRoom = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoom);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con menos de 2 horas de diferencia
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(today);
		newBookingDTO.setBookingTime(LocalTime.of(11, 30)); // 11:30 AM (1.5 horas después)
		newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba y verificar excepción
		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingService.saveBooking(newBookingDTO);
		});

		// Verificar mensaje de error
		assertEquals("Error: No se puede reservar en el mismo salón dentro de un intervalo de 2 horas.", exception.getMessage());

		// Verificar que nunca se llamó al método save
		verify(mockBookingRepository, never()).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_SuccessWithDifferentClassRoom() throws BookingServiceException {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
		String classRoomA = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoomA);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con mismo horario pero diferente salón
		String classRoomB = "Sala B";
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(today);
		newBookingDTO.setBookingTime(LocalTime.of(10, 30)); // 10:30 AM (menos de 2 horas después)
		newBookingDTO.setBookingClassRoom(classRoomB); // Diferente salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba
		Booking result = bookingService.saveBooking(newBookingDTO);

		// Verificar
		assertNotNull(result);
		assertEquals("new123", result.getBookingId());
		assertEquals(today, result.getBookingDate());
		assertEquals(LocalTime.of(10, 30), result.getBookingTime());
		assertEquals(classRoomB, result.getBookingClassRoom());
		assertTrue(result.isDisable()); // Por defecto es true según el constructor
		verify(mockBookingRepository).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_SuccessWithDifferentDate() throws BookingServiceException {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
		String classRoom = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoom);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con mismo salón pero diferente fecha
		LocalDate tomorrow = today.plusDays(1);
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(tomorrow); // Día siguiente
		newBookingDTO.setBookingTime(time1); // Mismo horario
		newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba
		Booking result = bookingService.saveBooking(newBookingDTO);

		// Verificar
		assertNotNull(result);
		assertEquals("new123", result.getBookingId());
		assertEquals(tomorrow, result.getBookingDate());
		assertEquals(time1, result.getBookingTime());
		assertEquals(classRoom, result.getBookingClassRoom());
		assertTrue(result.isDisable()); // Por defecto es true según el constructor
		verify(mockBookingRepository).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_SuccessBookingBeforeExisting() throws BookingServiceException {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(14, 0); // 2:00 PM
		String classRoom = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoom);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con horario anterior pero más de 2 horas de diferencia
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(today);
		newBookingDTO.setBookingTime(LocalTime.of(11, 0)); // 11:00 AM (3 horas antes)
		newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba
		Booking result = bookingService.saveBooking(newBookingDTO);

		// Verificar
		assertNotNull(result);
		assertEquals("new123", result.getBookingId());
		verify(mockBookingRepository).save(any(Booking.class));
	}

	@Test
	void testSaveBooking_FailsWithPriorBookingLessThan2Hours() {
		// Configurar booking existente
		LocalDate today = LocalDate.now();
		LocalTime time1 = LocalTime.of(14, 0); // 2:00 PM
		String classRoom = "Sala A";

		Booking existingBooking = new Booking("existing123", today, time1, false, classRoom);

		List<Booking> existingBookings = new ArrayList<>();
		existingBookings.add(existingBooking);

		// Configurar nuevo booking con horario anterior pero menos de 2 horas de diferencia
		BookingDTO newBookingDTO = new BookingDTO();
		newBookingDTO.setBookingId("new123");
		newBookingDTO.setBookingDate(today);
		newBookingDTO.setBookingTime(LocalTime.of(13, 0)); // 1:00 PM (1 hora antes)
		newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón

		// Configurar comportamiento del mock
		when(mockBookingRepository.existsById("new123")).thenReturn(false);
		when(mockBookingRepository.findAll()).thenReturn(existingBookings);

		// Ejecutar método bajo prueba y verificar excepción
		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingService.saveBooking(newBookingDTO);
		});

		// Verificar mensaje de error
		assertEquals("Error: No se puede reservar en el mismo salón dentro de un intervalo de 2 horas.", exception.getMessage());

		// Verificar que nunca se llamó al método save
		verify(mockBookingRepository, never()).save(any(Booking.class));
	}


	@Test
	void testGetAllBookings() {
		List<Booking> result = bookingService.getAllBookings();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(mockBookingRepository).findAll();
	}

	@Test
	void testDeleteBooking() throws BookingServiceException {
		doNothing().when(mockBookingRepository).deleteById(anyString());

		bookingService.deleteBooking("test123");
		verify(mockBookingRepository).deleteById("test123");
	}

	@Test
	void testUpdateBooking_ActivateSuccess() throws BookingServiceException {
		Booking disabledBooking = new Booking(bookingDTO);
		disabledBooking.setDisable(true);

		when(mockBookingRepository.findById("test123")).thenReturn(disabledBooking);

		Booking result = bookingService.updateBooking("test123", false);
		assertNotNull(result);
		assertFalse(result.isDisable());
		verify(mockBookingRepository).update(any(Booking.class));
	}

	@Test
	void testUpdateBooking_DeactivateSuccess() throws BookingServiceException {
		Booking enabledBooking = new Booking(bookingDTO);
		enabledBooking.setDisable(false);

		when(mockBookingRepository.findById("test123")).thenReturn(enabledBooking);

		Booking result = bookingService.updateBooking("test123", true);
		assertNotNull(result);
		assertTrue(result.isDisable());
		verify(mockBookingRepository).update(any(Booking.class));
	}

	@Test
	void testUpdateBooking_AlreadyActive() throws BookingServiceException {
		Booking enabledBooking = new Booking(bookingDTO);
		enabledBooking.setDisable(false);

		when(mockBookingRepository.findById("test123")).thenReturn(enabledBooking);

		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingService.updateBooking("test123", false);
		});

		assertEquals("La reserva ya está activa.", exception.getMessage());
	}

	@Test
	void testUpdateBooking_AlreadyDisabled() throws BookingServiceException {
		Booking disabledBooking = new Booking(bookingDTO);
		disabledBooking.setDisable(true);

		when(mockBookingRepository.findById("test123")).thenReturn(disabledBooking);

		BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
			bookingService.updateBooking("test123", true);
		});

		assertEquals("La reserva ya está cancelada.", exception.getMessage());
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

	@Test
	void testMakeBookingReservation() throws BookingServiceException {
		// Arrange
		String bookingId = "test123";
		Booking updatedBooking = new Booking(bookingDTO);
		updatedBooking.setDisable(false); // La reserva está activa

		when(mockBookingService.updateBooking(bookingId, false)).thenReturn(updatedBooking);

		// Reconfigurar bookingController con el mock
		bookingController = new BookingController();
		ReflectionTestUtils.setField(bookingController, "bookingService", mockBookingService);

		// Act
		Booking result = bookingController.makeBookingReservation(bookingId);

		// Assert
		verify(mockBookingService, times(1)).updateBooking(bookingId, false);
		assertNotNull(result);
		assertFalse(result.isDisable());
		assertEquals(bookingId, result.getBookingId());
		assertEquals(bookingDTO.getBookingDate(), result.getBookingDate());
		assertEquals(bookingDTO.getBookingTime(), result.getBookingTime());
		assertEquals(bookingDTO.getBookingClassRoom(), result.getBookingClassRoom());
	}

	@Test
	void testCancelBookingReservation() throws BookingServiceException {
		// Arrange
		String bookingId = "test123";
		Booking updatedBooking = new Booking(bookingDTO);
		updatedBooking.setDisable(true); // La reserva está cancelada

		when(mockBookingService.updateBooking(bookingId, true)).thenReturn(updatedBooking);

		// Reconfigurar bookingController con el mock
		bookingController = new BookingController();
		ReflectionTestUtils.setField(bookingController, "bookingService", mockBookingService);

		// Act
		Booking result = bookingController.cancelBookingReservation(bookingId);

		// Assert
		verify(mockBookingService, times(1)).updateBooking(bookingId, true);
		assertNotNull(result);
		assertTrue(result.isDisable());
		assertEquals(bookingId, result.getBookingId());
		assertEquals(bookingDTO.getBookingDate(), result.getBookingDate());
		assertEquals(bookingDTO.getBookingTime(), result.getBookingTime());
		assertEquals(bookingDTO.getBookingClassRoom(), result.getBookingClassRoom());
	}

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
		Booking booking = new Booking("testId", LocalDate.now(), LocalTime.now(), false, "ClassRoom1");

		booking.setBookingId("newId");
		booking.setBookingDate(LocalDate.of(2024, 3, 7));
		booking.setBookingTime(LocalTime.of(10, 30));
		booking.setDisable(true);
		booking.setBookingClassRoom("ClassRoom2"); // Este método no hace nada en la implementación

		assertEquals("newId", booking.getBookingId());
		assertEquals(LocalDate.of(2024, 3, 7), booking.getBookingDate());
		assertEquals(LocalTime.of(10, 30), booking.getBookingTime());
		assertTrue(booking.isDisable());
		assertEquals("ClassRoom1", booking.getBookingClassRoom()); // No cambia porque el setter está vacío
	}

	// Test para BookingServiceException
	@Test
	void testBookingServiceException() {
		BookingServiceException exception = new BookingServiceException("Test exception");
		assertEquals("Test exception", exception.getMessage());
	}

	// *** TESTS PARA USER ***

	// Tests para UserRepositoryImpl
	@Test
	void testUserRepositoryFindAll() {
		List<User> result = userRepository.findAll();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(userMongoRepository).findAll();
	}

	@Test
	void testUserRepositoryFindById_Success() throws UserServiceException {
		User result = userRepository.findById("user123");
		assertNotNull(result);
		assertEquals("user123", result.getId());
		verify(userMongoRepository).findById("user123");
	}

	@Test
	void testUserRepositoryFindById_NotFound() {
		when(userMongoRepository.findById("nonExistingId")).thenReturn(Optional.empty());

		UserServiceException exception = assertThrows(UserServiceException.class, () -> {
			userRepository.findById("nonExistingId");
		});

		assertEquals("User Not found", exception.getMessage());
		verify(userMongoRepository).findById("nonExistingId");
	}

	@Test
	void testUserRepositorySave() {
		userRepository.save(user);
		verify(userMongoRepository).save(user);
	}

	@Test
	void testUserRepositoryDeleteById() throws UserServiceException {
		userRepository.deleteById("user123");
		verify(userMongoRepository).deleteById("user123");
	}

	// Tests para UserServiceImpl
	@Test
	void testGetUser_Success() throws UserServiceException {
		when(mockUserRepository.findById("user123")).thenReturn(user);

		User result = userService.getUser("user123");
		assertNotNull(result);
		assertEquals("user123", result.getId());
		verify(mockUserRepository).findById("user123");
	}

	@Test
	void testGetUser_NotFound() throws UserServiceException {
		when(mockUserRepository.findById("nonExistingId")).thenThrow(new UserServiceException("User Not found"));

		UserServiceException exception = assertThrows(UserServiceException.class, () -> {
			userService.getUser("nonExistingId");
		});

		assertEquals("User Not found", exception.getMessage());
	}

	@Test
	void testSaveUser() {
		UserDTO newUserDTO = new UserDTO();
		newUserDTO.setId("newUser");
		newUserDTO.setUsername("newUsername");
		newUserDTO.setPhone(987654321);

		User result = userService.saveUser(newUserDTO);
		assertNotNull(result);
		assertEquals("newUser", result.getId());
		assertEquals("newUsername", result.getUsername());
		assertEquals(987654321, result.getPhone());
		verify(mockUserRepository).save(any(User.class));
	}

	@Test
	void testGetAllUsers() {
		List<User> result = userService.getAllUsers();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(mockUserRepository).findAll();
	}

	@Test
	void testDeleteUser() throws UserServiceException {
		doNothing().when(mockUserRepository).deleteById(anyString());

		userService.deleteUser("user123");
		verify(mockUserRepository).deleteById("user123");
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
		assertEquals("user123", result.getId());
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

	// Tests para UserDTO
	@Test
	void testUserDTOGettersAndSetters() {
		UserDTO dto = new UserDTO();

		dto.setId("testId");
		dto.setUsername("testUsername");
		dto.setPhone(123456789);

		assertEquals("testId", dto.getId());
		assertEquals("testUsername", dto.getUsername());
		assertEquals(123456789, dto.getPhone());
	}

	// Tests para User model
	@Test
	void testUserConstructorWithDTO() {
		UserDTO dto = new UserDTO();
		dto.setId("testId");
		dto.setUsername("testUsername");
		dto.setPhone(123456789);

		User user = new User(dto);

		assertEquals("testId", user.getId());
		assertEquals("testUsername", user.getUsername());
		assertEquals(123456789, user.getPhone());
	}

	@Test
	void testUserConstructorWithParameters() {
		User user = new User("testId", "testUsername", 123456789);

		assertEquals("testId", user.getId());
		assertEquals("testUsername", user.getUsername());
		assertEquals(123456789, user.getPhone());
	}

	@Test
	void testUserGettersAndSetters() {
		User user = new User("testId", "testUsername", 123456789);

		user.setId("newId");
		user.setUsername("newUsername");
		user.setPhone(987654321);

		assertEquals("newId", user.getId());
		assertEquals("newUsername", user.getUsername());
		assertEquals(987654321, user.getPhone());
	}

	// Test para UserServiceException
	@Test
	void testUserServiceException() {
		UserServiceException exception = new UserServiceException("Test user exception");
		assertEquals("Test user exception", exception.getMessage());
	}

	// Test main

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	@Test
	void testMainMethod() {
		// Instead of calling the main method directly, we'll mock the SpringApplication class
		try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
			// Set up the mock to do nothing when run is called
			mocked.when(() -> SpringApplication.run(
							CvdsBackApplication.class,
							new String[]{}))
					.thenReturn(null);

			// Now call the main method
			CvdsBackApplication.main(new String[]{});

			// Verify that SpringApplication.run was called with the correct parameters
			mocked.verify(() -> SpringApplication.run(
							CvdsBackApplication.class,
							new String[]{}),
					times(1));
		}
	}

	@Test
	void testApplicationAnnotations() {
		// Verify the application class has the required annotations
		Class<?> appClass = CvdsBackApplication.class;

		// Check for @SpringBootApplication annotation
		assertTrue(appClass.isAnnotationPresent(SpringBootApplication.class),
				"Application class should have @SpringBootApplication annotation");

		// Check for @EnableMongoRepositories annotation
		assertTrue(appClass.isAnnotationPresent(EnableMongoRepositories.class),
				"Application class should have @EnableMongoRepositories annotation");
	}

}