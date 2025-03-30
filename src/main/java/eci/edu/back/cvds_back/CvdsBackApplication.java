package eci.edu.back.cvds_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * The main entry point for the CvdsBackApplication Spring Boot application.
 * 
 * This class is annotated with:
 * - {@link SpringBootApplication}: Indicates that this is a Spring Boot application.
 * - {@link EnableMongoRepositories}: Enables the creation of Spring Data MongoDB repositories.
 * 
 * The {@code main} method initializes and runs the application using 
 * {@link SpringApplication#run(Class, String...)}.
 */
@SpringBootApplication
@EnableMongoRepositories
public class CvdsBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CvdsBackApplication.class, args);
	}

}