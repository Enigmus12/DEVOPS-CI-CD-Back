package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.controller.BookingController;
import eci.edu.back.cvds_back.controller.BookingGeneratorController;
import eci.edu.back.cvds_back.dto.BookingDTO;
import eci.edu.back.cvds_back.model.Booking;
import eci.edu.back.cvds_back.service.impl.*;
import eci.edu.back.cvds_back.service.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ImplBookingTests {

    // Mocks para BookingRepository tests
    @Mock
    private BookingMongoRepository bookingMongoRepository;

    @InjectMocks
    private BookingRepositoryImpl bookingRepository;

    // Mocks para BookingService tests
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

    // Mocks para BookingGenerator tests
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

        // Setup para Booking tests
        bookingDTO = new BookingDTO();
        bookingDTO.setBookingId("test123");
        bookingDTO.setBookingDate(LocalDate.now());
        bookingDTO.setBookingTime(LocalTime.of(14, 30));
        bookingDTO.setBookingClassRoom("Sala A");
        bookingDTO.setDisable(false);

        booking = new Booking(bookingDTO);
        bookingList = new ArrayList<>();
        bookingList.add(booking);

        // Configuracion mocks para BookingRepository tests
        when(bookingMongoRepository.findAll()).thenReturn(bookingList);
        when(bookingMongoRepository.findById("test123")).thenReturn(Optional.of(booking));
        when(bookingMongoRepository.existsById("test123")).thenReturn(true);
        when(bookingMongoRepository.existsById("nonExistingId")).thenReturn(false);

        // Configuracion mocks para BookingService tests
        when(mockBookingRepository.findAll()).thenReturn(bookingList);
        when(mockBookingRepository.findById("test123")).thenReturn(booking);
        when(mockBookingRepository.existsById("test123")).thenReturn(true);
        when(mockBookingRepository.existsById("nonExistingId")).thenReturn(false);

        when(mockBookingService.getAllBookings()).thenReturn(bookingList);
        when(mockBookingService.getBooking("test123")).thenReturn(booking);
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(booking);

        // Inject mocks usando ReflectionTestUtils
        ReflectionTestUtils.setField(bookingService, "bookingRepository", mockBookingRepository);
        ReflectionTestUtils.setField(bookingController, "bookingService", mockBookingService);

        // Configuracion mocks para BookingGenerator tests
        when(mockBookingGeneratorService.generateRandomBookings(anyInt(), anyInt()))
                .thenReturn(bookingList);
        when(mockBookingGeneratorService.generateExactBookings(anyInt()))
                .thenReturn(bookingList);
        when(mockBookingGeneratorService.clearAllBookings())
                .thenReturn(1);

        // Inject mocks para BookingGenerator
        ReflectionTestUtils.setField(bookingGeneratorController, "bookingGeneratorService", mockBookingGeneratorService);
        ReflectionTestUtils.setField(bookingGeneratorService, "bookingService", mockBookingService);
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
        newBookingDTO.setPriority(2);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,2, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con más de 2 horas de diferencia
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(today);
        newBookingDTO.setBookingTime(LocalTime.of(13, 0)); // 1:00 PM (3 horas después)
        newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón
        newBookingDTO.setPriority(2);

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
        assertEquals(2,result.getPriority());
        verify(mockBookingRepository).save(any(Booking.class));
    }

    @Test
    void testSaveBooking_FailsWithTimeGapLessThan2Hours() {
        // Configurar booking existente
        LocalDate today = LocalDate.now();
        LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
        String classRoom = "Sala A";

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,3, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con menos de 2 horas de diferencia
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(today);
        newBookingDTO.setBookingTime(LocalTime.of(11, 30)); // 11:30 AM (1.5 horas después)
        newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón
        newBookingDTO.setPriority(3);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoomA,4, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con mismo horario pero diferente salón
        String classRoomB = "Sala B";
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(today);
        newBookingDTO.setBookingTime(LocalTime.of(10, 30)); // 10:30 AM (menos de 2 horas después)
        newBookingDTO.setBookingClassRoom(classRoomB); // Diferente salón
        newBookingDTO.setPriority(4);

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
        assertEquals(4,result.getPriority());
        verify(mockBookingRepository).save(any(Booking.class));
    }

    @Test
    void testSaveBooking_SuccessWithDifferentDate() throws BookingServiceException {
        // Configurar booking existente
        LocalDate today = LocalDate.now();
        LocalTime time1 = LocalTime.of(10, 0); // 10:00 AM
        String classRoom = "Sala A";

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,5, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con mismo salón pero diferente fecha
        LocalDate tomorrow = today.plusDays(1);
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(tomorrow); // Día siguiente
        newBookingDTO.setBookingTime(time1); // Mismo horario
        newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón
        newBookingDTO.setPriority(5);

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
        assertEquals(5,result.getPriority());
        verify(mockBookingRepository).save(any(Booking.class));
    }

    @Test
    void testSaveBooking_SuccessBookingBeforeExisting() throws BookingServiceException {
        // Configurar booking existente
        LocalDate today = LocalDate.now();
        LocalTime time1 = LocalTime.of(14, 0); // 2:00 PM
        String classRoom = "Sala A";

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,1, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con horario anterior pero más de 2 horas de diferencia
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(today);
        newBookingDTO.setBookingTime(LocalTime.of(11, 0)); // 11:00 AM (3 horas antes)
        newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón
        newBookingDTO.setPriority(1);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,2, null);

        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(existingBooking);

        // Configurar nuevo booking con horario anterior pero menos de 2 horas de diferencia
        BookingDTO newBookingDTO = new BookingDTO();
        newBookingDTO.setBookingId("new123");
        newBookingDTO.setBookingDate(today);
        newBookingDTO.setBookingTime(LocalTime.of(13, 0)); // 1:00 PM (1 hora antes)
        newBookingDTO.setBookingClassRoom(classRoom); // Mismo salón
        newBookingDTO.setPriority(2);

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
    void testSaveBookingWithInvalidPriority() throws BookingServiceException {
        // Crear un BookingDTO con valores de prioridad inválidos

        // Prueba con prioridad por debajo del mínimo (menor que 1)
        BookingDTO lowPriorityDTO = new BookingDTO();
        lowPriorityDTO.setBookingId("testPriority1");
        lowPriorityDTO.setBookingDate(LocalDate.now());
        lowPriorityDTO.setBookingTime(LocalTime.of(10, 0));
        lowPriorityDTO.setBookingClassRoom("Sala B");
        lowPriorityDTO.setPriority(0); // Prioridad inválida (menor que 1)

        // Verificar que al llamar a saveBooking con prioridad menor que 1 se lanza una excepción
        BookingServiceException lowPriorityException = assertThrows(BookingServiceException.class, () -> {
            bookingService.saveBooking(lowPriorityDTO);
        });
        assertEquals("Error: La prioridad debe estar entre 1 y 5.", lowPriorityException.getMessage());

        // Prueba con prioridad por encima del máximo (mayor que 5)
        BookingDTO highPriorityDTO = new BookingDTO();
        highPriorityDTO.setBookingId("testPriority2");
        highPriorityDTO.setBookingDate(LocalDate.now());
        highPriorityDTO.setBookingTime(LocalTime.of(10, 0));
        highPriorityDTO.setBookingClassRoom("Sala B");
        highPriorityDTO.setPriority(6); // Prioridad inválida (mayor que 5)

        // Verificar que al llamar a saveBooking con prioridad mayor que 5 se lanza una excepción
        BookingServiceException highPriorityException = assertThrows(BookingServiceException.class, () -> {
            bookingService.saveBooking(highPriorityDTO);
        });
        assertEquals("Error: La prioridad debe estar entre 1 y 5.", highPriorityException.getMessage());

        // Verificar que nunca se llamó a save debido a que la validación falló
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

    // Tests for BookingGeneratorServiceImpl
    @Test
    void testGenerateExactBookings() throws BookingServiceException {
        // Crear una lista vacía de reservas existentes
        when(mockBookingService.getAllBookings()).thenReturn(new ArrayList<>());

        // Configurar la reserva que se devolverá cuando se llame a saveBooking
        Booking mockSavedBooking = new Booking(
                "lab1",
                LocalDate.now(),
                LocalTime.of(10, 0),
                true,
                "A101",
                1, null
        );
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(mockSavedBooking);

        // Inyectar el servicio simulado en nuestra implementación del servicio
        ReflectionTestUtils.setField(bookingGeneratorService, "bookingService", mockBookingService);

        // Llamar al método en prueba
        List<Booking> result = bookingGeneratorService.generateExactBookings(1);

        // Verificar los resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockBookingService, atLeastOnce()).getAllBookings();
        verify(mockBookingService, atLeastOnce()).saveBooking(any(BookingDTO.class));
    }

    @Test
    void testClearAllBookings() {
        // Simular que el servicio devuelve un recuento específico cuando se llama a clearAllBookings
        when(mockBookingGeneratorService.clearAllBookings()).thenReturn(5);

        // Llamar al método en prueba
        ResponseEntity<Map<String, Object>> response = bookingGeneratorController.clearAllBookings();

        // Verificar la respuesta
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Successfully cleared 5 bookings", body.get("message"));
        assertEquals(5, body.get("totalRemoved"));

        // Verificar que se llamó al método del servicio
        verify(mockBookingGeneratorService, times(1)).clearAllBookings();
    }

    @Test
    void testClearAllBookingsWithException() throws BookingServiceException {
        // Crear una lista de reservas con una que causará una excepción
        List<Booking> bookingsToDelete = new ArrayList<>();
        bookingsToDelete.add(new Booking("booking1", LocalDate.now(), LocalTime.of(10, 0), false, "A101", 1, null));
        bookingsToDelete.add(new Booking("errorBooking", LocalDate.now(), LocalTime.of(14, 0), false, "B201", 2, null));

        when(mockBookingService.getAllBookings()).thenReturn(bookingsToDelete);
        doNothing().when(mockBookingService).deleteBooking("booking1");
        doThrow(new BookingServiceException("Error eliminando la reserva")).when(mockBookingService).deleteBooking("errorBooking");

        // Inyectar el servicio simulado en nuestra implementación del servicio
        ReflectionTestUtils.setField(bookingGeneratorService, "bookingService", mockBookingService);

        // Llamar al método en prueba
        int result = bookingGeneratorService.clearAllBookings();

        // Verificar los resultados
        assertEquals(2, result); // Aún debería devolver el total de reservas
        verify(mockBookingService).getAllBookings();
        verify(mockBookingService, times(2)).deleteBooking(anyString());
    }

    @Test
    void testInitializeLabCounter() throws BookingServiceException {
        // Configurar reservas con identificadores de laboratorio
        List<Booking> existingBookings = new ArrayList<>();

        existingBookings.add(new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, null));
        existingBookings.add(new Booking("lab5", LocalDate.now(), LocalTime.of(11, 0), false, "B201", 2, null));
        existingBookings.add(new Booking("test123", LocalDate.now(), LocalTime.of(13, 0), false, "C301", 3, null));
        existingBookings.add(new Booking("labXYZ", LocalDate.now(), LocalTime.of(15, 0), false, "D401", 4, null));

        when(mockBookingService.getAllBookings()).thenReturn(existingBookings);

        // Configurar la reserva que se devolverá cuando se llame a saveBooking
        Booking mockSavedBooking = new Booking(
                "lab6",
                LocalDate.now(),
                LocalTime.of(10, 0),
                true,
                "A101",
                1, null
        );
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(mockSavedBooking);

        // Inyectar el servicio simulado en nuestra implementación del servicio
        ReflectionTestUtils.setField(bookingGeneratorService, "bookingService", mockBookingService);

        // Generar una reserva para activar initializeLabCounter
        bookingGeneratorService.generateExactBookings(1);

        // Verificar que el contador de laboratorios se inicializó correctamente capturando el BookingDTO
        ArgumentCaptor<BookingDTO> bookingCaptor = ArgumentCaptor.forClass(BookingDTO.class);
        verify(mockBookingService).saveBooking(bookingCaptor.capture());

        // El contador de laboratorios debería comenzar en 6 (después de lab5)
        BookingDTO capturedBooking = bookingCaptor.getValue();
        assertTrue(capturedBooking.getBookingId().startsWith("lab6"));
    }
    @Test
    void testGetBooking() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, null);
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        Booking result = bookingService.getBooking("test123");

        assertNotNull(result);
        assertEquals("test123", result.getBookingId());
        verify(mockBookingRepository, times(1)).findById("test123");
    }

    @Test
    void testMakeReservation() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), true, "A101", 1, null);
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        Booking result = bookingService.makeReservation("test123", "user1");

        assertNotNull(result);
        assertFalse(result.isDisable());
        assertEquals("user1", result.getReservedBy());
        verify(mockBookingRepository, times(1)).update(booking);
    }
    @Test
    void testMakeReservationAlreadyActive() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
            bookingService.makeReservation("test123", "user2");
        });

        assertEquals("La reserva ya está activa.", exception.getMessage());
        verify(mockBookingRepository, never()).update(booking);
    }
    @Test
    void testCancelReservationWithNullReservedBy() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, null);
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);
    
       
        Booking result = bookingService.cancelReservation("test123", "user2");
    
        assertNotNull(result);
        assertTrue(result.isDisable());
        verify(mockBookingRepository, times(1)).update(booking);
    }
    @Test
    void testCancelReservation() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        Booking result = bookingService.cancelReservation("test123", "user1");

        assertNotNull(result);
        assertTrue(result.isDisable());
        verify(mockBookingRepository, times(1)).update(booking);
    }
    @Test
    void testCancelAlreadyCanceledReservation() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), true, "A101", 1, "user1");
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
            bookingService.cancelReservation("test123", "user1");
        });

        assertEquals("La reserva ya está cancelada.", exception.getMessage());
        verify(mockBookingRepository, never()).update(booking);
    }


    @Test
    void testCancelReservationByDifferentUser() throws BookingServiceException {
        Booking booking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        booking.setBookingId("test123");
        when(mockBookingRepository.findById("test123")).thenReturn(booking);

        BookingServiceException exception = assertThrows(BookingServiceException.class, () -> {
            bookingService.cancelReservation("test123", "user2");
        });

        assertEquals("Solo el usuario que realizó la reserva puede cancelarla.", exception.getMessage());
        verify(mockBookingRepository, never()).update(booking);
    }

    @Test
    void testGetBookingsByReservedBy() {
        Booking booking1 = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        Booking booking2 = new Booking("lab2", LocalDate.now(), LocalTime.of(10, 0), false, "A102", 1, "user1");
        Booking booking3 = new Booking("lab3", LocalDate.now(), LocalTime.of(11, 0), false, "A103", 1, "user2");
        
        when(mockBookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2, booking3));

        List<Booking> result = bookingService.getBookingsByReservedBy("user1");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
        verify(mockBookingRepository, times(1)).findAll();
    }
    @Test
    void testGetBookingsByReservedByWithNullValues() {
        Booking booking1 = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        Booking booking2 = new Booking("lab2", LocalDate.now(), LocalTime.of(10, 0), false, "A102", 1, null); // Sin usuario
        Booking booking3 = new Booking("lab3", LocalDate.now(), LocalTime.of(11, 0), false, "A103", 1, "user2");
    
        when(mockBookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2, booking3));
    
        List<Booking> result = bookingService.getBookingsByReservedBy("user1");
    
        assertNotNull(result);
        assertEquals(1, result.size()); // Solo booking1 debe estar en la lista
        assertTrue(result.contains(booking1));
        assertFalse(result.contains(booking2)); // No tiene usuario
        assertFalse(result.contains(booking3)); // Otro usuario
        verify(mockBookingRepository, times(1)).findAll();
    }
    
    @Test
    void testGenerateRandomBookings() {
        List<Booking> bookings = bookingGeneratorService.generateRandomBookings(3, 5);
        assertNotNull(bookings);
        assertTrue(bookings.size() >= 3 && bookings.size() <= 5, "Generated bookings should be within range");
    }

    @Test
    void testGenerateBookings() {
        List<Booking> bookings = bookingGeneratorService.generateExactBookings(5);
        assertNotNull(bookings);
        assertEquals(5, bookings.size(), "Should generate exactly 5 bookings");
    }
    @Test
    void testGenerateBookings_NoAvailableSlots() {
        // Mock para devolver que no hay slots disponibles
        BookingGeneratorServiceImpl spyService = spy(bookingGeneratorService);
        doReturn(null).when(spyService).getRandomAvailableBooking(any(Map.class));

        List<Booking> result = spyService.generateExactBookings(5);

        assertEquals(0, result.size()); // No debe generar ninguna reserva
    }

    @Test
    void testGenerateBookings_BookingServiceException() throws BookingServiceException {
        // Mock para generar una reserva disponible
        Booking mockBooking = new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1, "user1");
        BookingGeneratorServiceImpl spyService = spy(bookingGeneratorService);
        doReturn(mockBooking).when(spyService).getRandomAvailableBooking(any(Map.class));

        // Mock para lanzar una excepción al guardar la reserva
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenThrow(new BookingServiceException("Error al guardar"));

        List<Booking> result = spyService.generateExactBookings(3);

        assertEquals(0, result.size()); // No se deben agregar reservas por la excepción
    }
    @Test
    void testGetRandomAvailableBooking_NoAvailableSlots() {
        Map<String, Map<LocalDate, Set<Integer>>> bookedSlots = new HashMap<>();
        LocalDate today = LocalDate.now();
    
        // Llenamos todas las aulas con todas las horas ocupadas en los próximos 30 días
        for (String classroom : bookingGeneratorService.classrooms) {
            bookedSlots.put(classroom, new HashMap<>());
            for (int day = 0; day < 30; day++) {
                LocalDate date = today.plusDays(day);
                bookedSlots.get(classroom).put(date, new HashSet<>());
    
                // Marcar todas las horas como reservadas
                for (int hour : bookingGeneratorService.validHours) {
                    bookedSlots.get(classroom).get(date).add(hour);
                }
            }
        }
    
        // Llamar al método cuando no hay espacios disponibles
        Booking result = bookingGeneratorService.getRandomAvailableBooking(bookedSlots);
    
        // Verificar que el resultado es null
        assertNull(result, "El método debe devolver null cuando no hay espacios disponibles");
    }

    @Test
    void testGenerateBookings_AddNewDateToBookedSlots() throws BookingServiceException {
        BookingGeneratorServiceImpl spyService = spy(bookingGeneratorService);
    
        // Simular bookedSlots con un aula pero sin reservas en cierta fecha
        Map<String, Map<LocalDate, Set<Integer>>> bookedSlots = new HashMap<>();
        bookedSlots.put("A101", new HashMap<>()); // Aula con mapa vacío (sin fechas)
    
        // Mock para devolver una reserva en una nueva fecha
        Booking mockBooking = new Booking("lab1", LocalDate.now().plusDays(2), LocalTime.of(9, 0), false, "A101", 1, "user1");
        doReturn(mockBooking).when(spyService).getRandomAvailableBooking(any(Map.class));
    
        // Simular que la reserva se guarda correctamente
        when(mockBookingService.saveBooking(any(BookingDTO.class))).thenReturn(mockBooking);
    
        List<Booking> result = spyService.generateExactBookings(1);
    
        assertEquals(1, result.size(), "Debe haberse generado una reserva");
    }

    @Test
    void testIsSlotBooked_NoReservationsForClassroom() {
        Map<String, Map<LocalDate, Set<Integer>>> bookedSlots = new HashMap<>();
        
        // No se agrega "A101" a bookedSlots
        boolean result = bookingGeneratorService.isSlotBooked(bookedSlots, "A101", LocalDate.now(), 9);
    
        assertFalse(result, "Si el aula no tiene reservas, debe devolver false");
    }
}
    



