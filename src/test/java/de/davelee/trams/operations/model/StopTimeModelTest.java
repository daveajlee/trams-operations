package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopTimeModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopTimeModelTest {

    /**
     * Ensure that a StopTimeModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        StopTimeModel stopTimeModel = StopTimeModel.builder()
                .arrivalTime(LocalTime.of(19, 46))
                .departureTime(LocalTime.of(19,48))
                .destination("Greenfield")
                .id(1234)
                .journeyNumber("123")
                .operatingDays(Collections.singletonList(DayOfWeek.MONDAY))
                .routeNumber("405A")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build();
        //Verify the builder functionality through getter methods
        assertEquals(LocalTime.of(19,46), stopTimeModel.getArrivalTime());
        assertEquals(LocalTime.of(19,48), stopTimeModel.getDepartureTime());
        assertEquals("Greenfield", stopTimeModel.getDestination());
        assertEquals(1234, stopTimeModel.getId());
        assertEquals("123", stopTimeModel.getJourneyNumber());
        assertEquals(Collections.singletonList(DayOfWeek.MONDAY), stopTimeModel.getOperatingDays());
        assertEquals("405A", stopTimeModel.getRouteNumber());
        assertEquals(LocalDate.of(2020,12,12), stopTimeModel.getValidFromDate());
        assertEquals(LocalDate.of(2021,12,11), stopTimeModel.getValidToDate());
        //Verify the toString method
        assertEquals("StopTimeModel(id=1234, stopName=null, arrivalTime=19:46, departureTime=19:48, destination=Greenfield, routeNumber=405A, validFromDate=2020-12-12, validToDate=2021-12-11, operatingDays=[MONDAY], journeyNumber=123)", stopTimeModel.toString());
        //Now use the setter methods
        stopTimeModel.setArrivalTime(LocalTime.of(20, 46));
        stopTimeModel.setDepartureTime(LocalTime.of(20,48));
        stopTimeModel.setDestination("Lake Way");
        stopTimeModel.setId(12345);
        stopTimeModel.setJourneyNumber("1234");
        stopTimeModel.setOperatingDays(Collections.singletonList(DayOfWeek.SUNDAY));
        stopTimeModel.setRouteNumber("405B");
        stopTimeModel.setValidFromDate(LocalDate.of(2020,11,12));
        stopTimeModel.setValidToDate(LocalDate.of(2021,11,11));
        //And verify again through the toString methods
        assertEquals("StopTimeModel(id=12345, stopName=null, arrivalTime=20:46, departureTime=20:48, destination=Lake Way, routeNumber=405B, validFromDate=2020-11-12, validToDate=2021-11-11, operatingDays=[SUNDAY], journeyNumber=1234)", stopTimeModel.toString());
    }

    /**
     * Test the get time method to ensure that depending on departure or arrival parameter it returns the correct code.
     */
    @Test
    public void testGetTime () {
        //Create Test Date
        StopTimeModel stopTimeModel = StopTimeModel.builder()
                .arrivalTime(LocalTime.of(19, 46))
                .departureTime(LocalTime.of(19,48))
                .destination("Greenfield")
                .id(1234)
                .journeyNumber("123")
                .operatingDays(Collections.singletonList(DayOfWeek.MONDAY))
                .routeNumber("405A")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build();
        // Test retrieval of departure time.
        assertEquals(stopTimeModel.getTime("Departure"), LocalTime.of(19, 48));
        // Test retrieval of arrival time.
        assertEquals(stopTimeModel.getTime("Arrival"), LocalTime.of(19, 46));
    }

}
