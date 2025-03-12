package eci.edu.back.cvds_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CvdsBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CvdsBackApplication.class, args);
	}

}
