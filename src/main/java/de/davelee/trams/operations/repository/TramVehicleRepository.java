package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.TramVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This class enables as part of Spring Data access to the tram vehicle objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface TramVehicleRepository extends MongoRepository<TramVehicleModel, String> {
}
