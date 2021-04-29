package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.BusVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * This class enables as part of Spring Data access to the bus vehicle objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface BusVehicleRepository extends MongoRepository<BusVehicleModel, String> {

    List<BusVehicleModel> findByCompanyStartsWithAndFleetNumberStartsWith (final String company, final String fleetNumber);

}
