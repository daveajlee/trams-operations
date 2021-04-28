package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.*;
import de.davelee.trams.operations.repository.BusVehicleRepository;
import de.davelee.trams.operations.repository.TrainVehicleRepository;
import de.davelee.trams.operations.repository.TramVehicleRepository;
import de.davelee.trams.operations.response.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * Retrieve all of the vehicles currently stored in the database for all types.
     * @return a <code>List</code> of <code>VehicleResponse</code> objects in a format suitable to be returned via API.
     */
    public List<VehicleResponse> retrieveAllVehicles ( ) {
        //List to store all vehicles
        List<VehicleResponse> vehicleResponseList = new ArrayList<>();
        //Process trains
        List<TrainVehicleModel> trainVehicleModelList = trainVehicleRepository.findAll();
        for ( TrainVehicleModel trainVehicleModel : trainVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(trainVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.TRAIN);
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Power Mode", trainVehicleModel.getPowerMode().toString()));
            vehicleResponseList.add(vehicleResponse);
        }
        //Process buses
        List<BusVehicleModel> busVehicleModelList = busVehicleRepository.findAll();
        for ( BusVehicleModel busVehicleModel : busVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(busVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.BUS);
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Registration Number", busVehicleModel.getRegistrationNumber()));
            vehicleResponseList.add(vehicleResponse);
        }
        //Process trams
        List<TramVehicleModel> tramVehicleModelList = tramVehicleRepository.findAll();
        for ( TramVehicleModel tramVehicleModel : tramVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(tramVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.TRAM);
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Bidirectional", Boolean.toString(tramVehicleModel.isBidirectional())));
            vehicleResponseList.add(vehicleResponse);
        }
        //Return the vehicle list
        return vehicleResponseList;
    }

    /**
     * This is a private helper method which converts selected attributes from the database to a suitable format for the
     * response for the Rest API.
     * @param vehicleModel a <code>VehicleModel</code> object with the data from the database to be converted.
     * @return a <code>VehicleResponse</code> object with data suitable to be included in the response.
     */
    private VehicleResponse convertToStandardVehicleResponse( final VehicleModel vehicleModel ) {
        return VehicleResponse.builder()
                .allocatedTour(vehicleModel.getAllocatedTour())
                .fleetNumber(vehicleModel.getFleetNumber())
                .livery(vehicleModel.getLivery())
                .build();
    }

}
