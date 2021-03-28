package de.davelee.trams.operations.repository;

import de.davelee.trams.operations.model.StopTimeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This class enables as part of Spring Data access to the stop time objects stored in the Mongo DB.
 * @author Dave Lee
 */
public interface StopTimeRepository extends MongoRepository<StopTimeModel, String> {

    /**
     * Find all departures and/or arrivals for a particular stop name.
     * @param stopName a <code>String</code> containing the name of the stop to find departures and/or arrivals for.
     * @return a <code>List</code> of <code>StopTimeModel</code> objects containing the deparatures and/or arrivals matching the stop name.
     */
    List<StopTimeModel> findByStopName(@Param("stopName") final String stopName );

}
