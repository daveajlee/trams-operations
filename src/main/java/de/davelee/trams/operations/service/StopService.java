package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.StopModel;
import de.davelee.trams.operations.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides a service for managing stops in Trams Operations.
 * @author Dave Lee
 */
@Service
public class StopService {

    @Autowired
    private StopRepository stopRepository;

    /**
     * Return all stops currently stored in the database.
     * @return a <code>List</code> of <code>StopModel</code> objects which may be null if there are no stops in the database.
     */
    public List<StopModel> getStops() {
        return stopRepository.findAll();
    }

}
