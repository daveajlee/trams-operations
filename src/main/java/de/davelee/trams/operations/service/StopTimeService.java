package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.repository.StopTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a service for managing stop times in Trams Operations.
 * @author Dave Lee
 */
@Service
public class StopTimeService {

    @Autowired
    private StopTimeRepository stopTimeRepository;

    /**
     * Return the next 3 departures for this stop within the next 2 hours.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve departures from.
     * @param startingTime a <code>String</code> containing the time to start retrieving departures from which may be null if current time should be used.
     * @return a <code>List</code> of <code>StopTimeModel</code> objects which may be null if no departures were found or there
     * are no departures in next 2 hours.
     */
    public List<StopTimeModel> getDepartures (final String stopName, final String startingTime ) {
        return getTimes(stopName, startingTime, "Departure", Comparator.comparing(StopTimeModel::getDepartureTime));
    }

    /**
     * Return the next 3 arrivals for this stop within the next 2 hours.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve arrivals for.
     * @param startingTime a <code>String</code> containing the time to start retrieving arrivals from which may be null if current time should be used.
     * @return a <code>List</code> of <code>StopTimeModel</code> objects which may be null if the stop arrivals were not found or there
     * are no arrivals in next 2 hours.
     */
    public List<StopTimeModel> getArrivals (final String stopName, final String startingTime ) {
        return getTimes(stopName, startingTime, "Arrival", Comparator.comparing(StopTimeModel::getArrivalTime));
    }

    /**
     * Return the next 3 stop time models (either departures or arrivals) for this stop within the next 2 hours.
     * @param stopName a <code>String</code> containing the name of the stop to retrieve stop times for.
     * @param startingTime a <code>String</code> containing the time to start retrieving stop times from which may be null if current time should be used.
     * @param type a <code>String</code> which can be either Departure to return the departures or Arrival to return the arrivals.
     * @param comparator a <code>Comparator</code> of <code>StopTimeModel</code> which defines how the departures or arrivals will be sorted.
     * @return a <code>List</code> of <code>StopTimeModel</code> objects which may be null if the stop time models were not found or there
     *       are no stop time models in next 2 hours.
     */
    public List<StopTimeModel> getTimes ( final String stopName, final String startingTime, final String type, final Comparator<StopTimeModel> comparator ) {
        //Initial time to starting time or current time if no starting time was supplied.
        final LocalTime time = startingTime != null ? convertToLocalTime(startingTime) : LocalTime.now();

        //Special processing if between 22 and 24 - otherwise normal processing.
        if ( time.isAfter(LocalTime.of(21,59))) {
            //First of all get stop times between now and midnight.
            List<StopTimeModel> stopTimeModels = stopTimeRepository.findByStopName(stopName).stream()
                    //Filter stop times which do not run on this day.
                    .filter(stopTimeModel -> stopTimeModel.getOperatingDays().contains(LocalDate.now().getDayOfWeek()))
                    //Filter so that stop times in the past are not shown.
                    .filter(stopTimeModel -> !stopTimeModel.getTime(type).isBefore(time))
                    //Filter remove stop times which lie much further in the future
                    .filter(stopTimeModel -> !stopTimeModel.getTime(type).isAfter(LocalTime.of(23,59)))
                    //Sort the stop times by time.
                    .sorted(comparator)
                    //Only show next 3 stop times.
                    .limit(3)
                    .collect(Collectors.toList());
            //If we already have 3 stop times then no need to look further.
            if ( stopTimeModels.size() == 3 ) {
                return stopTimeModels;
            }
            //Otherwise add the remaining stop times from the next day.
            stopTimeModels.addAll(stopTimeRepository.findByStopName(stopName).stream()
                    //Filter remove stop times which lie much further in the future
                    .filter(stopTimeModel -> !stopTimeModel.getTime(type).isAfter(time.plusHours(2)))
                    //Sort the stop times by time.
                    .sorted(comparator)
                    //Only show next 3 stop times.
                    .limit(3-stopTimeModels.size())
                    .collect(Collectors.toList()));
            return stopTimeModels;
        }
        //Normal processing
        List<StopTimeModel> filteredStopTimeModels = stopTimeRepository.findByStopName(stopName).stream()
                //Filter stop times which do not run on this day.
                .filter(stopTimeModel -> stopTimeModel.getOperatingDays().contains(LocalDate.now().getDayOfWeek()))
                //Filter so that stop times in the past are not shown.
                .filter(stopTimeModel -> !stopTimeModel.getTime(type).isBefore(time))
                //Filter remove stop times which lie much further in the future
                .filter(stopTimeModel -> !stopTimeModel.getTime(type).isAfter(time.plusHours(2)))
                //Sort the stop times by time.
                .sorted(comparator)
                .collect(Collectors.toList());

        //Remove any duplicates.
        for (int i = 0; i < filteredStopTimeModels.size()-1; i++ ) {
            for (int j = (i+1); j < filteredStopTimeModels.size(); j++ ) {
                if ( filteredStopTimeModels.get(i).getDestination().contentEquals(filteredStopTimeModels.get(j).getDestination()) &&
                        filteredStopTimeModels.get(i).getTime(type).compareTo(filteredStopTimeModels.get(j).getTime(type)) == 0) {
                    filteredStopTimeModels.remove(i);
                }
            }
        }

        //Only show next 3 stop times and remove.
        return filteredStopTimeModels.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * This helper method contains a String in the format HH:mm to a <code>LocalTime</code> object.
     * @param time a <code>String</code> in the format HH:mm
     * @return a <code>LocalTime</code> object which contains the time.
     */
    private LocalTime convertToLocalTime ( final String time ) {
        String[] timeHoursMinArray = time.split(":");
        return LocalTime.of(Integer.parseInt(timeHoursMinArray[0]), Integer.parseInt(timeHoursMinArray[1]));
    }

}
