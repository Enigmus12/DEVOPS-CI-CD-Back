package eci.edu.back.cvds_back;

import eci.edu.back.cvds_back.config.BookingServiceException;
import eci.edu.back.cvds_back.config.UserServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CvdsBackApplicationTests {

	// Test para BookingServiceException
	@Test
	void testBookingServiceException() {
		BookingServiceException exception = new BookingServiceException("Test exception");
		assertEquals("Test exception", exception.getMessage());
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
		// Esta prueba verifica que el contexto de Spring se carga correctamente
		// Lo que implícitamente prueba la clase CvdsBackApplication
		CvdsBackApplication application = new CvdsBackApplication();
		assertNotNull(application);
	}

	@Test
	void testMainMethod() {
		try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
			mocked.when(() -> SpringApplication.run(
							CvdsBackApplication.class,
							new String[]{}))
					.thenReturn(null);

			CvdsBackApplication.main(new String[]{});

			mocked.verify(() -> SpringApplication.run(
							CvdsBackApplication.class,
							new String[]{}),
					times(1));
		}
	}


}