package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.model.StopModel;
import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.repository.RouteRepository;
import de.davelee.trams.operations.repository.StopRepository;
import de.davelee.trams.operations.repository.StopTimeRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileSystems;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class provides a service for importing CSV files which match the following specification:
 * @author Dave Lee
 */
@Service
public class ImportCSVDataService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private StopTimeRepository stopTimeRepository;

    private int stopTimeCounter;

    /**
     * Read a csv folder and extract all departures from the supplied file and store them in the mongo db.
     * @param directory a <code>String</code> containing the directory to load the data from.
     * @param validFromDate a <code>String</code> containing the valid from date in format yyyy-MM-dd
     * @param validToDate a <code>String</code> containing the valid to date in format yyyy-MM-dd
     * @return a <code>boolean</code> which is true iff the file could be read successfully.
     */
    public boolean readCSVFile(final String directory, final String validFromDate, final String validToDate) {
        //Set counter for stopTime objects to ensure a valid identifier.
        stopTimeCounter = 0;

        //Check that the directory exists - otherwise return false.
        if ( !directory.startsWith("/") && ImportCSVDataService.class.getClassLoader().getResource(directory) == null ) {
            return false;
        }

        //Get all csv files in the directory.
        final File directoryPath = directory.startsWith("/") ? new File(directory) :
                                        new File(ImportGTFSDataService.class.getClassLoader().getResource(directory).getFile());
        File[] directoryFiles = directoryPath.listFiles((dir, name) -> name.endsWith(".csv"));

        //If there are no csv files in the directory then return false.
        if ( directoryFiles == null || directoryFiles.length == 0 ) {
            return false;
        }

        //Determine operator name based on name of zip file.
        String[] directorySplitPath = directory.split(FileSystems.getDefault().getSeparator());
        String operatorName = directorySplitPath[directorySplitPath.length-1].replace("-", " ");
        operatorName = WordUtils.capitalizeFully(operatorName);

        //Loop through all of the files and begin to process them in helper methods.
        for ( File csvFile : directoryFiles ) {
            if (!loadCSVFile(csvFile.getAbsolutePath(), operatorName, validFromDate, validToDate)) {
                return false;
            }
        }

        //If all csv files could be processed successfully, then return true.
        return true;

    }

    /**
     * This method attempts to load the supplied csv file, read all data and upload it to the database. If it is
     * successful and data goes into the database, then it returns true. Otherwise it returns false.
     * @param csvFilePath a <code>String</code> containing the path to the csv file to load.
     * @param operatorName a <code>String</code> containing the name of the operator.
     * @param validFromDate a <code>String</code> containing the valid from date in format yyyy-MM-dd
     * @param validToDate a <code>String</code> containing the valid to date in format yyyy-MM-dd
     * @return a <code>boolean</code> which is true iff the csv file could be read and processed successfully.
     */
    private boolean loadCSVFile ( final String csvFilePath, final String operatorName, final String validFromDate,
                                  final String validToDate ) {
        try {
            Reader reader = new FileReader(csvFilePath);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withTrim());
            String destination = ""; ArrayList<List<DayOfWeek>> operatingDays = new ArrayList<>();
            ArrayList<String> routeNumberList = new ArrayList<>();
            for(CSVRecord record : csvParser.getRecords()) {
                if ( record.get(0).startsWith("Route:") ) {
                    for ( int i = 1; i < record.size(); i++ ) {
                        if ( record.get(i).isEmpty() ) continue; //Do not add empty data.
                        importRoute(record.get(i), operatorName);
                        routeNumberList.add(record.get(i));
                    }
                }
                else if ( record.get(0).contains(" <> ")) {
                    destination = record.get(0).split(" <> ")[1];
                }
                else if ( record.get(0).contains("Days of Operation")) {
                    for ( int i = 1; i < record.size(); i++ ) {
                        operatingDays.add(getOperatingDays(record.get(i)));
                    }
                }
                else if ( record.get(0).isEmpty() || record.get(0).startsWith("Circulation:")) {
                    continue;
                } else {
                    if (!hasStopAlreadyBeenImported(record.get(0))) {
                        importStop(record.get(0));
                    }
                    for ( int i = 1; i < record.size(); i++ ) {
                        if ( record.get(i).isEmpty() ) continue;
                        StopTimeModel stopTimeModel = StopTimeModel.builder()
                            .id(stopTimeCounter)
                            .departureTime(LocalTime.parse(record.get(i), DateTimeFormatter.ofPattern("HH:mm")))
                            .arrivalTime(LocalTime.parse(record.get(i), DateTimeFormatter.ofPattern("HH:mm")))
                            .stopName(record.get(0))
                            .destination(destination)
                            .routeNumber(routeNumberList.get(i-1))
                            .validFromDate(LocalDate.parse(validFromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .validToDate(LocalDate.parse(validToDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .operatingDays(operatingDays.get(i-1))
                            .journeyNumber("" + i)
                            .build();
                        stopTimeRepository.insert(stopTimeModel);
                        stopTimeCounter++;
                    }
                }
            }
            return true;
        } catch ( IOException exception ) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * This is a private helper method which determines if the specified route already exists in the database.
     * @param routeNumber a <code>String</code> object which contains the route number that should be checked.
     * @return a <code>boolean</code> which is true iff the route number has already been imported to the database.
     */
    private boolean hasRouteAlreadyBeenImported ( final String routeNumber ) {
        for ( RouteModel existingRouteModel : routeRepository.findAll() ) {
            if ( existingRouteModel.getRouteNumber().contentEquals(routeNumber) ) {
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
     * @param routeNumber a <code>String</code> object with the route numnber that should be imported.
     * @param operatorName a <code>String</code> object which contains the name of the operator of this route.
     */
    private void importRoute (final String routeNumber, final String operatorName ) {
        if ( !hasRouteAlreadyBeenImported(routeNumber) ) {
            RouteModel routeModel = RouteModel.builder()
                    .routeNumber(routeNumber)
                    .id(UUID.randomUUID().toString())
                    .agency(operatorName)
                    .build();
            routeRepository.insert( routeModel );
        }
    }

    /**
     * This is a private helper method to import the supplied stop to the database.
     * @param stopName a <code>String</code> object containing the name of the stop to add.
     */
    private void importStop ( final String stopName ) {
        StopModel stopModel = StopModel.builder()
                .id(UUID.randomUUID().toString())
                .name(stopName)
                .build();
        stopRepository.insert(stopModel);
    }

    /**
     * This helper method converts the operating days of a service into a list of operating days.
     * @param operatingDaysStr a <code>String</code> object containing the comma-separated list of operating days.
     * @return a <code>List</code> of <code>OperatingDays</code> containing the operating days of this service.
     */
    private List<DayOfWeek> getOperatingDays (final String operatingDaysStr ) {
        //Create empty list
        List<DayOfWeek> operatingDays = new ArrayList<>();
        //Split comma separated list
        String[] operatingDaysCommaList = operatingDaysStr.split(",");
        //Go through comma separated list
        for ( String operatingDay : operatingDaysCommaList ) {
            //Weekdays
            if (operatingDay.contentEquals("WD")) {
                operatingDays.add(DayOfWeek.MONDAY);
                operatingDays.add(DayOfWeek.TUESDAY);
                operatingDays.add(DayOfWeek.WEDNESDAY);
                operatingDays.add(DayOfWeek.THURSDAY);
                operatingDays.add(DayOfWeek.FRIDAY);
            }
            //Saturdays
            if (operatingDay.contentEquals("SA")) {
                operatingDays.add(DayOfWeek.SATURDAY);
            }
            //Sundays
            if (operatingDay.contentEquals("S")) {
                operatingDays.add(DayOfWeek.SUNDAY);
            }
        }
        //Return complete list of operating days.
        return operatingDays;
    }



}
