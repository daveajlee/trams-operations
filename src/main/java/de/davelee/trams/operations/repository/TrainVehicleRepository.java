package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.TrainVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This class enables as part of Spring Data access to the train vehicle objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface TrainVehicleRepository extends MongoRepository<TrainVehicleModel, String> {
}
