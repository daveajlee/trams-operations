package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides a service for managing routes in Trams Operations.
 * @author Dave Lee
 */
@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    /**
     * Return all routes currently stored in the database.
     * @return a <code>List</code> of <code>RouteModel</code> objects which may be null if there are no routes in the database.
     */
    public List<RouteModel> getRoutes ( ) {
        return routeRepository.findAll();
    }


}
