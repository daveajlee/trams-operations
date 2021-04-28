package de.davelee.trams.operations.controller;

import de.davelee.trams.operations.model.*;
import de.davelee.trams.operations.request.ImportZipRequest;
import de.davelee.trams.operations.response.VehicleResponse;
import de.davelee.trams.operations.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides REST endpoints which can be called by other clients wishing to communicate with the Trams Operations Module.
 * @author Dave Lee
 */
@RestController
@Api(value="/trams-operations")
@RequestMapping(value="/trams-operations")
public class TramsOperationsRestController {

    @Autowired
    private StopTimeService stopTimeService;

    @Autowired
    private ImportGTFSDataService gtfsDataService;

    @Autowired
    private ImportCSVDataService csvDataService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StopService stopService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @Autowired
    private VehicleService vehicleService;

    /**
     * Return the next 3 departures for this stop within the next 2 hours.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve departures from.
     * @param startingTime a <code>String</code> containing the time to start retrieving departures from which may be null if current time should be used.
     * @return a <code>List</code> of <code>StopDeparture</code> objects which may be null if the stop departure was not found or there
     * are no departures in next 2 hours.
     */
    @GetMapping("/departures")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get latest departures", notes="Return the next departures.")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned departures")})
    public List<StopTimeModel> getDepartures (final String stopName, final String startingTime ) {
        return stopTimeService.getDepartures(stopName, startingTime);
    }

    /**
     * Return the next 3 arrivals for this stop within the next 2 hours.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve arrivals for.
     * @param startingTime a <code>String</code> containing the time to start retrieving arrivals from which may be null if current time should be used.
     * @return a <code>List</code> of <code>StopTimeModel</code> objects which may be null if the stop arrivals were not found or there
     * are no arrivals in next 2 hours.
     */
    @GetMapping("/arrivals")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get latest arrivals", notes="Return the next arrivals.")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned arrivals")})
    public List<StopTimeModel> getArrivals (final String stopName, final String startingTime ) {
        return stopTimeService.getArrivals(stopName, startingTime);
    }

    /**
     * Return all departures for this stop on the supplied date.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve departures from.
     * @param date a <code>String</code> containing the date in format YYYY-mm-dd.
     * @return a <code>List</code> of <code>StopDeparture</code> objects which may be null if the stop departure was not found or there
     * are no departures on this date.
     */
    @GetMapping("/departuresByDate")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get all departures for a particualar date and stop", notes="Return all departures" +
            " for this stop and date.")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned departures")})
    public List<StopTimeModel> getDeparturesByDate (final String stopName, final String date ) {
        return stopTimeService.getDeparturesByDate(stopName, date);
    }

    /**
     * Return all routes currently stored in the database.
     * @return a <code>List</code> of <code>RouteModel</code> objects which may be null if there are no routes in the database.
     */
    @GetMapping("/routes")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get routes", notes="Return all routes")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned routes")})
    public List<RouteModel> getRoutes ( ) {
        return routeService.getRoutes();
    }

    /**
     * Return all stops currently stored in the database.
     * @return a <code>List</code> of <code>StopModel</code> objects which may be null if there are no stops in the database.
     */
    @GetMapping("/stops")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get stops", notes="Return all stops")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned stops")})
    public List<StopModel> getStops ( ) {
        return stopService.getStops();
    }

    /**
     * Upload a zip file containing files either fulfilling the GTFS specification or the CSV specification.
     * Optionally a list of routes can be provided which should be imported and may be null if all routes should be imported.
     * Optionally a valid from and valid to date can also be provided (which are only read in the csv import).
     * The GTFS specification is specified here: https://developers.google.com/transit/gtfs
     * @param importZipRequest a <code>ImportZipRequest</code> containing the zip file, list of routes to import
     *                            and valid from and valid to dates.
     * @return a <code>ResponseEntity</code> object which returns the http status of this method if it was successful or not.
     */
    @PostMapping("/uploadDataFile")
    @CrossOrigin
    @ApiOperation(value = "Upload Data file", notes="Upload a GTFS or CSV Zip file to TraMS")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully imported GTFS/CSV Data"), @ApiResponse(code=422,message="Entity could not be processed because zip file was not valid")})
    public ResponseEntity<Void> handleFileUpload(@ModelAttribute final ImportZipRequest importZipRequest) {
        String folderName = fileSystemStorageService.store(importZipRequest.getZipFile());
        List<String> routesToImport =  importZipRequest.getRoutesToImport() != null ?
                                        Arrays.asList(importZipRequest.getRoutesToImport().split(",")) : new ArrayList<>();
        if ( importZipRequest.getFileFormat().contentEquals("General Transit Feed Specification (GTFS)")) {
            if (gtfsDataService.readGTFSFile(folderName, routesToImport)) {
                return ResponseEntity.ok().build();
            }
        } else if ( importZipRequest.getFileFormat().contentEquals("Comma Separated Value (CSV)")) {
            if (csvDataService.readCSVFile(folderName, importZipRequest.getValidFromDate(), importZipRequest.getValidToDate())) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    /**
     * Endpoint to retrieve vehicle fleet information.
     * @return a <code>List</code> of <code>VehicleResponse</code> objects which may be null if there are no vehicles in the database.
     */
    @GetMapping("/vehicles")
    @CrossOrigin
    @ResponseBody
    @ApiOperation(value = "Get vehicles", notes="Return all vehicles in the fleet")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully returned vehicles")})
    public List<VehicleResponse> getVehicles ( ) {
        return vehicleService.retrieveAllVehicles();
    }

    /**
     * Temporary endpoint to add test data which will be removed as soon as data can be added through normal endpoints.
     * @return a <code>ResponseEntity</code> object which returns the http status of this method if it was successful or not.
     */
    @GetMapping("/testdata")
    @ApiOperation(value = "Populate test data", notes="returns nothing")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully added test data")})
    public ResponseEntity<Void> addTestData ( ) {
        //Create test bus
        BusVehicleModel busVehicleModel = BusVehicleModel.builder()
                .registrationNumber("W234DHDF")
                .modelName("BendyBus 2000")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        //Create test train
        TrainVehicleModel trainVehicleModel = TrainVehicleModel.builder()
                .powerMode(TrainPowerMode.DIESEL)
                .modelName("Train 2000 Di")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("2130")
                .company("Lee Trains")
                .build();
        //Create test tram
        TramVehicleModel tramVehicleModel = TramVehicleModel.builder()
                .isBidirectional(true)
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("2310")
                .company("Lee Trams")
                .build();
        //Add all three to databse
        vehicleService.addBus(busVehicleModel);
        vehicleService.addTrain(trainVehicleModel);
        vehicleService.addTram(tramVehicleModel);
        //Return ok.
        return ResponseEntity.ok().build();
    }

}
