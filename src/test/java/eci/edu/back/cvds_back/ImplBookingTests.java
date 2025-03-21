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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,2);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,3);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoomA,4);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,5);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,1);

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

        Booking existingBooking = new Booking("existing123", today, time1, false, classRoom,2);

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
                1
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
        bookingsToDelete.add(new Booking("booking1", LocalDate.now(), LocalTime.of(10, 0), false, "A101", 1));
        bookingsToDelete.add(new Booking("errorBooking", LocalDate.now(), LocalTime.of(14, 0), false, "B201", 2));

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

        existingBookings.add(new Booking("lab1", LocalDate.now(), LocalTime.of(9, 0), false, "A101", 1));
        existingBookings.add(new Booking("lab5", LocalDate.now(), LocalTime.of(11, 0), false, "B201", 2));
        existingBookings.add(new Booking("test123", LocalDate.now(), LocalTime.of(13, 0), false, "C301", 3));
        existingBookings.add(new Booking("labXYZ", LocalDate.now(), LocalTime.of(15, 0), false, "D401", 4));

        when(mockBookingService.getAllBookings()).thenReturn(existingBookings);

        // Configurar la reserva que se devolverá cuando se llame a saveBooking
        Booking mockSavedBooking = new Booking(
                "lab6",
                LocalDate.now(),
                LocalTime.of(10, 0),
                true,
                "A101",
                1
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


}
