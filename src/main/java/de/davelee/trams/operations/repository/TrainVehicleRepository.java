package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.TrainVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * This class enables as part of Spring Data access to the train vehicle objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface TrainVehicleRepository extends MongoRepository<TrainVehicleModel, String> {

    List<TrainVehicleModel> findByCompanyStartsWithAndFleetNumberStartsWith ( final String company, final String fleetNumber);

    List<TrainVehicleModel> findByFleetNumberStartsWith (final String fleetNumber);
}
