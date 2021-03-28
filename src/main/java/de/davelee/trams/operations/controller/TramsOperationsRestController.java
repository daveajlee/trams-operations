package de.davelee.trams.operations.controller;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.model.StopModel;
import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.request.ImportGtfsZipRequest;
import de.davelee.trams.operations.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * This class provides REST endpoints which can be called by other clients wishing to communicate with the Trams Operations Module.
 * @author Dave Lee
 */
@Controller
@Api(value="/trams-operations")
@RequestMapping(value="/trams-operations")
public class TramsOperationsRestController {

    @Autowired
    private StopTimeService stopTimeService;

    @Autowired
    private ImportGTFSDataService gtfsDataService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StopService stopService;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

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
     * Upload a GTFS zip file containing a zip file fulfilling the GTFS specification and optionally a list of routes
     * to import which may be null if all routes should be imported.
     * The GTFS specification is specified here: https://developers.google.com/transit/gtfs
     * @param importGtfsZipRequest a <code>ImportGtfsZipRequest</code> containing the zip file and list of routes to import which may be null.
     * @return a <code>ResponseEntity</code> object which returns the http status of this method if it was successful or not.
     */
    @PostMapping("/uploadGTFSFile")
    @CrossOrigin
    @ApiOperation(value = "Upload GTFS file", notes="Upload a GTFS Zip file to TraMS")
    @ApiResponses(value = {@ApiResponse(code=200,message="Successfully imported GTFS Data"), @ApiResponse(code=422,message="Entity could not be processed because zip file was not valid")})
    public ResponseEntity<Void> handleFileUpload(@ModelAttribute final ImportGtfsZipRequest importGtfsZipRequest) {
        String folderName = fileSystemStorageService.store(importGtfsZipRequest.getZipFile());
        List<String> routesToImport =  importGtfsZipRequest.getRoutesToImport() != null ?
                                        Arrays.asList(importGtfsZipRequest.getRoutesToImport().split(",")) : new ArrayList<>();
        if ( gtfsDataService.readGTFSFile(folderName, routesToImport) ) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    /**
     * This is a temporary method which allows a file to be uploaded through Thymeleaf until the final UI in Angular is available.
     * @param model a <code>Model</code> which can create attributes to be passed to Thymeleaf but is currently not read by Thymeleaf.
     * @return a <code>String</code> which contains the name of the HTML file to be displayed by Thymeleaf.
     */
    @GetMapping("/uploadFile")
    public String uploadFile(final Model model) {
        return "uploadForm";
    }

}
