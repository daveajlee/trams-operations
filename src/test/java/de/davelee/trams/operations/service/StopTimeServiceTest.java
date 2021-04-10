package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.repository.StopTimeRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopTimeService class and ensures that it works successfully. Mocks are used for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class StopTimeServiceTest {

    @InjectMocks
    private StopTimeService stopTimeService;

    @Mock
    private StopTimeRepository stopTimeRepository;

    /**
     * Verify that stop times can be retrieved from the database correctly.
     */
    @Test
    public void testService ( ) {
        //Test data
        Mockito.when(stopTimeRepository.findByStopName("Lakeside")).thenReturn(Lists.newArrayList(
                createStopTime(LocalTime.of(16,11), LocalTime.of(16,12), "101", 1),
                createStopTime(LocalTime.of(16,41), LocalTime.of(16,42), "102", 2),
                createStopTime(LocalTime.of(17,21), LocalTime.of(17,22), "103", 3),
                createStopTime(LocalTime.of(20,21), LocalTime.of(20,22), "104", 4),
                createStopTime(LocalTime.of(21,16), LocalTime.of(21,17), "105", 5),
                createStopTime(LocalTime.of(22,11), LocalTime.of(22,12), "106", 6),
                createStopTime(LocalTime.of(23,21), LocalTime.of(23,22), "107", 7),
                createStopTime(LocalTime.of(0,21), LocalTime.of(0,22), "108", 8)
                ));
        //Test case 1: 3 Departures in the next 2 hours before 22:00
        List<StopTimeModel> stopTimeTestList1 = stopTimeService.getDepartures("Lakeside", "16:00");
        assertEquals(3, stopTimeTestList1.size());
        assertEquals(1, stopTimeTestList1.get(0).getId());
        assertEquals(2, stopTimeTestList1.get(1).getId());
        assertEquals(3, stopTimeTestList1.get(2).getId());
        //Test case 2: 1 Departure before 22:00 and 1 after
        List<StopTimeModel> stopTimeTestList2 = stopTimeService.getDepartures("Lakeside", "21:00");
        assertEquals(2, stopTimeTestList2.size());
        assertEquals(5, stopTimeTestList2.get(0).getId());
        assertEquals(6, stopTimeTestList2.get(1).getId());
        //Test case 3: 2 Departures before 22:00 and 0 after
        List<StopTimeModel> stopTimeTestList3 = stopTimeService.getDepartures("Lakeside", "20:05");
        assertEquals(2, stopTimeTestList3.size());
        assertEquals(4, stopTimeTestList3.get(0).getId());
        assertEquals(5, stopTimeTestList3.get(1).getId());
        //Test caae 4: 2 Departures between 23:00 and 01:00 on separate days.
        List<StopTimeModel> stopTimeTestList4 = stopTimeService.getDepartures("Lakeside", "23:00");
        assertEquals(2, stopTimeTestList4.size());
        assertEquals(7, stopTimeTestList4.get(0).getId());
        assertEquals(8, stopTimeTestList4.get(1).getId());
        //Test case 5: 3 Arrivals before 22:00
        List<StopTimeModel> stopTimeTestArrivalList1 = stopTimeService.getArrivals("Lakeside", "16:00");
        assertEquals(3, stopTimeTestArrivalList1.size());
        assertEquals(1, stopTimeTestArrivalList1.get(0).getId());
        assertEquals(2, stopTimeTestArrivalList1.get(1).getId());
        assertEquals(3, stopTimeTestArrivalList1.get(2).getId());
        //Test case 6: 1 Arrivals before 22:00 and 1 after
        List<StopTimeModel> stopTimeTestArrivalList2 = stopTimeService.getArrivals("Lakeside", "21:00");
        assertEquals(2, stopTimeTestArrivalList2.size());
        assertEquals(5, stopTimeTestArrivalList2.get(0).getId());
        assertEquals(6, stopTimeTestArrivalList2.get(1).getId());
        //Test case 7: 2 Arrivals before 22:00 and 0 after
        List<StopTimeModel> stopTimeTestArrivalList3 = stopTimeService.getArrivals("Lakeside", "20:05");
        assertEquals(2, stopTimeTestArrivalList3.size());
        assertEquals(4, stopTimeTestArrivalList3.get(0).getId());
        assertEquals(5, stopTimeTestArrivalList3.get(1).getId());
        //Test caae 8: 2 Arrivals between 23:00 and 01:00 on separate days.
        List<StopTimeModel> stopTimeTestArrivalList4 = stopTimeService.getArrivals("Lakeside", "23:00");
        assertEquals(2, stopTimeTestArrivalList4.size());
        assertEquals(7, stopTimeTestArrivalList4.get(0).getId());
        assertEquals(8, stopTimeTestArrivalList4.get(1).getId());
        //Test case: test all departures for this date.
        List<StopTimeModel> stopTimeDepartureDateList = stopTimeService.getDeparturesByDate("Lakeside", "2021-04-10");
        assertEquals(8, stopTimeDepartureDateList.size());
        assertEquals(8, stopTimeDepartureDateList.get(0).getId());
        assertEquals(7, stopTimeDepartureDateList.get(stopTimeDepartureDateList.size()-1).getId());
    }

    /**
     * Private helper method to create test stop time data.
     * @param arrivalTime a <code>LocalTime</code> object containing the desired arrival time.
     * @param departureTime a <code>LocalTime</code> object containing the desired departure time.
     * @param journeyNumber a <code>String</code> containing the journey name.
     * @param count a <code>int</code> with the id to use.
     * @return a <code>StopTimeModel</code> object which contains all data filled for a test StopTimeModel object.
     */
    private StopTimeModel createStopTime ( final LocalTime arrivalTime, final LocalTime departureTime, final String journeyNumber, final int count ) {
        return StopTimeModel.builder()
                .arrivalTime(arrivalTime)
                .departureTime(departureTime)
                .destination("Greenfield")
                .id(count)
                .journeyNumber(journeyNumber)
                .operatingDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .routeNumber("405A")
                .stopName("Lakeside")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build();
    }
}
