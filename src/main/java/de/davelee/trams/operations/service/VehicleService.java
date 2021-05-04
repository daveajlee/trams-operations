package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.*;
import de.davelee.trams.operations.repository.BusVehicleRepository;
import de.davelee.trams.operations.repository.TrainVehicleRepository;
import de.davelee.trams.operations.repository.TramVehicleRepository;
import de.davelee.trams.operations.response.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Value("${bus.inspection.period}")
    private int busInspectionPeriodInYears;

    @Value("${train.inspection.period}")
    private int trainInspectionPeriodInYears;

    @Value("${tram.inspection.period}")
    private int tramInspectionPeriodInYears;


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
        processTrainModels(trainVehicleRepository.findAll(), vehicleResponseList);
        //Process buses
        processBusModels(busVehicleRepository.findAll(), vehicleResponseList);
        //Process trams
        processTramModels(tramVehicleRepository.findAll(), vehicleResponseList);
        //Return the vehicle list
        return vehicleResponseList;
    }

    /**
     * Retrieve all vehicles starting with the supplied company name and fleet number from the database for all types.
     * @param company a <code>String</code> with the name of the company to search for.
     * @param fleetNumber a <code>String</code> with the fleet number to search for.
     * @return a <code>List</code> of <code>VehicleResponse</code> objects in a format suitable to be returned via API.
     */
    public List<VehicleResponse> retrieveVehiclesByCompanyAndFleetNumber ( final String company, final String fleetNumber) {
        //List to store all vehicles
        List<VehicleResponse> vehicleResponseList = new ArrayList<>();
        //Process trains
        processTrainModels(trainVehicleRepository
                .findByCompanyStartsWithAndFleetNumberStartsWith(company, fleetNumber), vehicleResponseList);
        //Process buses
        processBusModels(busVehicleRepository
                .findByCompanyStartsWithAndFleetNumberStartsWith(company, fleetNumber), vehicleResponseList);
        //Process trams
        processTramModels(tramVehicleRepository
                .findByCompanyStartsWithAndFleetNumberStartsWith(company, fleetNumber), vehicleResponseList);
        //Return the vehicle list
        return vehicleResponseList;
    }

    /**
     * This is a private helper method to process the supplied list of <code>TrainVehicleModel</code> objects and convert
     * them into <code>VehicleResponse</code> objects.
     * @param trainVehicleModelList a <code>List</code> of <code>TrainVehicleModel</code> objects to convert
     * @param vehicleResponseList a <code>List</code> of <code>VehicleResponse</code> which may be empty to store converted results.
     */
    private void processTrainModels ( final List<TrainVehicleModel> trainVehicleModelList, final List<VehicleResponse> vehicleResponseList) {
        for ( TrainVehicleModel trainVehicleModel : trainVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(trainVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.TRAIN.getTypeName());
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Power Mode", trainVehicleModel.getPowerMode().toString()));
            processInspectionDate(vehicleResponse, trainVehicleModel.getInspectionDate(), trainInspectionPeriodInYears );
            vehicleResponseList.add(vehicleResponse);
        }
    }

    /**
     * This is a private helper method to process the supplied list of <code>BusVehicleModel</code> objects and convert
     * them into <code>VehicleResponse</code> objects.
     * @param busVehicleModelList a <code>List</code> of <code>BusVehicleModel</code> objects to convert
     * @param vehicleResponseList a <code>List</code> of <code>VehicleResponse</code> which may be empty to store converted results.
     */
    private void processBusModels ( final List<BusVehicleModel> busVehicleModelList, final List<VehicleResponse> vehicleResponseList) {
        for ( BusVehicleModel busVehicleModel : busVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(busVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.BUS.getTypeName());
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Registration Number", busVehicleModel.getRegistrationNumber()));
            processInspectionDate(vehicleResponse, busVehicleModel.getInspectionDate(), busInspectionPeriodInYears );
            vehicleResponseList.add(vehicleResponse);
        }
    }

    /**
     * This is a private helper method to calculate the inspection status of a vehicle and how many days until the next
     * inspection is due based on the last inspection date.
     * @param vehicleResponse a <code>VehicleResponse</code> object to write the results of the calculations in.
     * @param inspectionDate a <code>LocalDate</code> containing the date of the last inspection range
     * @param inspectionPeriod a <code>int</code> containing the number of years within which an inspection must take place
     */
    private void processInspectionDate ( final VehicleResponse vehicleResponse, final LocalDate inspectionDate,
                                         final int inspectionPeriod ) {
        final LocalDate inspectionStartRange = LocalDate.now().minusYears(inspectionPeriod);
        if ( inspectionDate.isAfter(LocalDate.now().minusYears(inspectionPeriod)) ) {
            vehicleResponse.setInspectionStatus(InspectionStatus.INSPECTED.getInspectionNotice());
            vehicleResponse.setNextInspectionDueInDays(ChronoUnit.DAYS.between(LocalDate.now(),
                    inspectionDate.plusYears(inspectionPeriod)));
        } else {
            vehicleResponse.setInspectionStatus(InspectionStatus.INSPECTION_DUE.getInspectionNotice());
            vehicleResponse.setNextInspectionDueInDays(0);
        }
    }

    /**
     * This is a private helper method to process the supplied list of <code>TramVehicleModel</code> objects and convert
     * them into <code>VehicleResponse</code> objects.
     * @param tramVehicleModelList a <code>List</code> of <code>TramVehicleModel</code> objects to convert
     * @param vehicleResponseList a <code>List</code> of <code>VehicleResponse</code> which may be empty to store converted results.
     */
    private void processTramModels ( final List<TramVehicleModel> tramVehicleModelList, final List<VehicleResponse> vehicleResponseList ) {
        for ( TramVehicleModel tramVehicleModel : tramVehicleModelList ) {
            VehicleResponse vehicleResponse = convertToStandardVehicleResponse(tramVehicleModel);
            vehicleResponse.setVehicleType(VehicleType.TRAM.getTypeName());
            vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Bidirectional", Boolean.toString(tramVehicleModel.isBidirectional())));
            processInspectionDate(vehicleResponse, tramVehicleModel.getInspectionDate(), tramInspectionPeriodInYears );
            vehicleResponseList.add(vehicleResponse);
        }
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
