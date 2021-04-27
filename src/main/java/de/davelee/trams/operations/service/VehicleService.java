package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.BusVehicleModel;
import de.davelee.trams.operations.model.TrainVehicleModel;
import de.davelee.trams.operations.model.TramVehicleModel;
import de.davelee.trams.operations.repository.BusVehicleRepository;
import de.davelee.trams.operations.repository.TrainVehicleRepository;
import de.davelee.trams.operations.repository.TramVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides a service for managing vehicles in Trams Operations.
 * @author Dave Lee
 */
@Service
public class VehicleService {

    @Autowired
    private BusVehicleRepository busVehicleRepository;

    @Autowired
    private TrainVehicleRepository trainVehicleRepository;

    @Autowired
    private TramVehicleRepository tramVehicleRepository;

    /**
     * Add the supplied bus to the database.
     * @param busVehicleModel a <code>BusVehicleModel</code> object containing the information about the bus to be added.
     * @return a <code>boolean</code> which is true iff the bus was added successfully.
     */
    public boolean addBus (final BusVehicleModel busVehicleModel ) {
        return busVehicleRepository.insert(busVehicleModel) != null;
    }

    /**
     * Add the supplied tram to the database.
     * @param tramVehicleModel a <code>TramVehicleModel</code> object containing the information about the tram to be added.
     * @return a <code>boolean</code> which is true iff the tram was added successfully.
     */
    public boolean addTram ( final TramVehicleModel tramVehicleModel ) {
        return tramVehicleRepository.insert(tramVehicleModel) != null;
    }

    /**
     * Add the supplied train to the database.
     * @param trainVehicleModel a <code>TrainVehicleModel</code> object containing the information about the train to be added.
     * @return a <code>boolean</code> which is true iff the train was added successfully.
     */
    public boolean addTrain (final TrainVehicleModel trainVehicleModel ) {
        return trainVehicleRepository.insert(trainVehicleModel) != null;
    }

}
