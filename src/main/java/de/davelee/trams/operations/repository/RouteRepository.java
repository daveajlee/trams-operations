package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.RouteModel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * This class enables as part of Spring Data access to the route objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface RouteRepository extends MongoRepository<RouteModel, String> {
}
