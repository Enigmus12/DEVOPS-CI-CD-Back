package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingMongoRepository extends MongoRepository<Booking, String> {

}
