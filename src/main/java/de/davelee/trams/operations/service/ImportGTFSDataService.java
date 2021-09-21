package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.model.StopModel;
import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.repository.RouteRepository;
import de.davelee.trams.operations.repository.StopTimeRepository;
import de.davelee.trams.operations.repository.StopRepository;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.model.*;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a service for importing GTFS files which match the GTFS specification: https://developers.google.com/transit/gtfs
 * @author Dave Lee
 */
@Service
public class ImportGTFSDataService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private StopTimeRepository stopTimeRepository;

    /**
     * Read a gtfs folder and extract all departures from the supplied file and store them in the mongo db.
     * @param directory a <code>String</code> containing the directory to load the data from.
     * @param routesToImport a <code>List</code> of <code>String</code> containing the list of routes to import. If the list is empty then all routes
     *                       should be imported.
     * @return a <code>boolean</code> which is true iff the file could be read successfully.
     */
    public boolean readGTFSFile(final String directory, final List<String> routesToImport) {
        //Set counter for stopTime objects to ensure a valid identifier.
        int stopTimeCounter = 0;

        //Check that the directory exists - otherwise return false.
        if ( !directory.startsWith("/") && ImportGTFSDataService.class.getClassLoader().getResource(directory) == null ) {
            return false;
        }
        //Create a new reader with directory and a variable to store all data.
        try {
            GtfsReader reader = new GtfsReader();
            reader.setInputLocation(directory.startsWith("/") ?
                    new File(directory) :
                    new File(ImportGTFSDataService.class.getClassLoader().getResource(directory).getFile()));
            GtfsDaoImpl store = new GtfsDaoImpl();
            reader.setEntityStore(store);

            //Run the importer for data from the files.
            reader.run();

            //Import the route information.
            if (!routesToImport.isEmpty()) {
                //Import only the selected routes as long as routesToImport is not empty.
                for (Route route : store.getAllRoutes()) {
                    if (shouldRouteBeImported(route, routesToImport)) {
                        importRoute(route, store.getAgencyForId(route.getAgency().getId()));
                    }
                }
            } else {
                //Otherwise import all routes as routesToImport is empty.
                for (Route route : store.getAllRoutes()) {
                    importRoute(route, store.getAgencyForId(route.getAgency().getId()));
                }
            }

            //Import the stop time information.
            for (StopTime stopTime : store.getAllStopTimes()) {
                if ((!routesToImport.isEmpty() && shouldRouteBeImported(stopTime.getTrip().getRoute(), routesToImport))) {

                    //Do not add duplicate stops to the database.
                    if (!hasStopAlreadyBeenImported(stopTime.getStop().getName())) {
                        importStop(stopTime.getStop());
                    }

                    //Add the StopTime information to the database.
                    List<ServiceCalendar> serviceCalendarList = store.getAllCalendars().stream().filter(sc -> sc.getServiceId().getId().contentEquals(stopTime.getTrip().getServiceId().getId())).collect(Collectors.toList());
                    StopTimeModel stopTimeModel = StopTimeModel.builder()
                            .id(stopTimeCounter)
                            .departureTime(LocalTime.parse(convertTimeToHoursAndMinutes(stopTime.getDepartureTime()), DateTimeFormatter.ofPattern("HH:mm")))
                            .arrivalTime(LocalTime.parse(convertTimeToHoursAndMinutes(stopTime.getArrivalTime()), DateTimeFormatter.ofPattern("HH:mm")))
                            .stopName(stopTime.getStop().getName())
                            .destination(stopTime.getTrip().getTripHeadsign())
                            .routeNumber(stopTime.getTrip().getRoute().getShortName())
                            .journeyNumber(stopTime.getTrip().getId().getId())
                            .validFromDate( serviceCalendarList.size() == 1 ? LocalDate.of(serviceCalendarList.get(0).getStartDate().getYear(), serviceCalendarList.get(0).getStartDate().getMonth(), serviceCalendarList.get(0).getStartDate().getDay()): null )
                            .validToDate( serviceCalendarList.size() == 1 ? LocalDate.of(serviceCalendarList.get(0).getEndDate().getYear(), serviceCalendarList.get(0).getEndDate().getMonth(), serviceCalendarList.get(0).getEndDate().getDay()): null )
                            .operatingDays(serviceCalendarList.size() == 1 ? getOperatingDays(serviceCalendarList.get(0)) : null)
                            .build();
                    stopTimeRepository.insert(stopTimeModel);
                    stopTimeCounter++;
                }
            }
            return true;
        } catch ( IOException ioException ) {
            return false;
        }
    }

    /**
     * This is a private helper method which determines that a route should be imported if it is contained in the list
     * of routes to import.
     * @param route a <code>Route</code> object which contains the route that would be imported.
     * @param routesToImport a <code>List</code> of <code>String</code> objects containing the lists which should be imported.
     * @return a <code>boolean</code> which is true iff the route should be imported.
     */
    private boolean shouldRouteBeImported ( final Route route, final List<String> routesToImport ) {
        for ( String routeNumber : routesToImport ) {
            if (route.getShortName().contentEquals(routeNumber)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a private helper method which determines if the specified route already exists in the database.
     * @param route a <code>Route</code> object which contains the route that should be checked.
     * @return a <code>boolean</code> which is true iff the route has already been imported to the database.
     */
    private boolean hasRouteAlreadyBeenImported ( final Route route ) {
        for ( RouteModel existingRouteModel : routeRepository.findAll() ) {
            if ( existingRouteModel.getRouteNumber().contentEquals(route.getShortName()) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a private helper method which determines if the specified stop already exists in the database.
     * @param stopName a <code>String</code> object which contains the name of the stop that should be checked.
     * @return a <code>boolean</code> which is true iff the stop has already been imported to the database.
     */
    private boolean hasStopAlreadyBeenImported ( final String stopName ) {
        for ( StopModel stopModel : stopRepository.findAll() ) {
            if ( stopModel.getName().contentEquals(stopName) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is a private helper method to import the supplied route to the database.
     * @param route a <code>Route</code> object which should be imported.
     * @param agency a <code>Agency</code> object which contains the name of the operator of this route.
     */
    private void importRoute ( final Route route, final Agency agency ) {
        if ( !hasRouteAlreadyBeenImported(route) ) {
            routeRepository.insert( RouteModel.builder()
                    .routeNumber(route.getShortName())
                    .id(route.getId().getId())
                    .agency(agency.getName())
                    .build());
        }
    }

    /**
     * This is a private helper method to import the supplied stop to the database.
     * @param stop a <code>Stop</code> object which should be imported.
     */
    private void importStop ( final Stop stop ) {
        stopRepository.insert(StopModel.builder()
                .id(stop.getId().getId())
                .latitude(stop.getLat())
                .longitude(stop.getLon())
                .name(stop.getName())
                .build());
    }

    /**
     * Private helper method to convert the time from seconds after midnight into the form HH:mm.
     * @param seconds a <code>int</code> with the number of seconds after midnight.
     * @return a <code>String</code> with the converted time in the format HH:mm.
     */
    private String convertTimeToHoursAndMinutes ( final int seconds ) {
        //Convert seconds to hours (divide by 60 twice).
        int correctHours = seconds / 60 / 60;
        //Adjust hours to take account of hours if greater than 23 then subtract 24.
        int adjustedHours = ( correctHours > 23 ) ? correctHours - 24 : correctHours;
        //Display hours by adding a 0 if below 10.
        String hoursStr = (adjustedHours < 10) ? "0" + adjustedHours : "" + adjustedHours;
        //Calcuate remaining minutes to calculate the minutes.
        int minutes = seconds-(correctHours*60*60);
        //Display minutes by adding a 0 if below 10.
        String minutesStr = ((minutes/60) < 10) ? "0" + minutes/60 : "" + minutes/60;
        //Return in HH:mm format.
        return hoursStr + ":" + minutesStr;
    }

    /**
     * This helper method converts the operating days of a service into a list of operating days.
     * @param serviceCalendar a <code>ServiceCalendar</code> object containing the operating days.
     * @return a <code>List</code> of <code>OperatingDays</code> containing the operating days of this service.
     */
    private List<DayOfWeek> getOperatingDays (final ServiceCalendar serviceCalendar ) {
        //Create empty list
        List<DayOfWeek> operatingDays = new ArrayList<>();
        //Monday
        if ( serviceCalendar.getMonday() == 1 ) {
            operatingDays.add(DayOfWeek.MONDAY);
        }
        //Tuesday
        if ( serviceCalendar.getTuesday() == 1 ) {
            operatingDays.add(DayOfWeek.TUESDAY);
        }
        //Wednesday
        if ( serviceCalendar.getWednesday() == 1 ) {
            operatingDays.add(DayOfWeek.WEDNESDAY);
        }
        //Thursday
        if ( serviceCalendar.getThursday() == 1 ) {
            operatingDays.add(DayOfWeek.THURSDAY);
        }
        //Friday
        if ( serviceCalendar.getFriday() == 1 ) {
            operatingDays.add(DayOfWeek.FRIDAY);
        }
        //Saturday
        if ( serviceCalendar.getSaturday() == 1 ) {
            operatingDays.add(DayOfWeek.SATURDAY);
        }
        //Sunday
        if ( serviceCalendar.getSunday() == 1 ) {
            operatingDays.add(DayOfWeek.SUNDAY);
        }
        //Return complete list of operating days.
        return operatingDays;
    }

}
