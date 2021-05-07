package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.TramVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * This class enables as part of Spring Data access to the tram vehicle objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface TramVehicleRepository extends MongoRepository<TramVehicleModel, String> {

    List<TramVehicleModel> findByCompanyStartsWithAndFleetNumberStartsWith (final String company, final String fleetNumber);

    List<TramVehicleModel> findByFleetNumberStartsWith (final String fleetNumber);
}
