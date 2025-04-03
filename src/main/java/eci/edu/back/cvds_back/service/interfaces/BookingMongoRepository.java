package eci.edu.back.cvds_back.service.interfaces;

import eci.edu.back.cvds_back.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Booking entities in a MongoDB database.
 * This interface extends the MongoRepository interface, providing CRUD operations
 * and additional query methods for the Booking entity.
 *
 * @see org.springframework.data.mongodb.repository.MongoRepository
 * @param <Booking> The type of the entity to handle.
 * @param <String> The type of the entity's ID.
 */
@Repository
public interface BookingMongoRepository extends MongoRepository<Booking, String> {
}
