package de.davelee.trams.operations.controller;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.model.StopModel;
import de.davelee.trams.operations.model.StopTimeModel;
import de.davelee.trams.operations.request.ImportZipRequest;
import de.davelee.trams.operations.service.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * This class tests the TramsOperationsRestController and ensures that the endpoints work successfully. It uses
 * mocks for the service and database layers.
 * @author Dave Lee
 */
@SpringBootTest
public class TramsOperationsRestControllerTest {

    @InjectMocks
    private TramsOperationsRestController controller;

    @Mock
    private RouteService routeService;

    @Mock
    private StopTimeService stopTimeService;

    @Mock
    private ImportGTFSDataService importGTFSDataService;

    @Mock
    private StopService stopService;

    @Mock
    private FileSystemStorageService fileSystemStorageService;

    /**
     * Test the departure endpoint of this controller.
     */
    @Test
    public void testDeparturesEndpoints() {
        Mockito.when(stopTimeService.getDepartures(anyString(), anyString())).thenReturn(Lists.newArrayList(StopTimeModel.builder()
                .arrivalTime(LocalTime.of(22,11))
                .departureTime(LocalTime.of(22,13))
                .destination("Greenfield")
                .id(1)
                .journeyNumber("101")
                .operatingDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .routeNumber("405A")
                .stopName("Lakeside")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build()));
        List<StopTimeModel> stopTimeModelList = controller.getDepartures("Lakeside", "22:00");
        assertEquals(1, stopTimeModelList.size());
        assertEquals("101", stopTimeModelList.get(0).getJourneyNumber());
    }

    /**
     * Test the arrival endpoint of this controller.
     */
    @Test
    public void testArrivalsEndpoints() {
        Mockito.when(stopTimeService.getArrivals(anyString(), anyString())).thenReturn(Lists.newArrayList(StopTimeModel.builder()
                .arrivalTime(LocalTime.of(22,11))
                .departureTime(LocalTime.of(22,13))
                .destination("Greenfield")
                .id(1)
                .journeyNumber("101")
                .operatingDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .routeNumber("405A")
                .stopName("Lakeside")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build()));
        List<StopTimeModel> stopTimeModelList = controller.getArrivals("Lakeside", "22:00");
        assertEquals(1, stopTimeModelList.size());
        assertEquals(LocalTime.of(22,11), stopTimeModelList.get(0).getArrivalTime());
    }

    /**
     * Test the departure date endpoint of this controller.
     */
    @Test
    public void testDeparturesDateEndpoints() {
        Mockito.when(stopTimeService.getDeparturesByDate(anyString(), anyString())).thenReturn(Lists.newArrayList(StopTimeModel.builder()
                .arrivalTime(LocalTime.of(22,11))
                .departureTime(LocalTime.of(22,13))
                .destination("Greenfield")
                .id(1)
                .journeyNumber("101")
                .operatingDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                .routeNumber("405A")
                .stopName("Lakeside")
                .validFromDate(LocalDate.of(2020,12,12))
                .validToDate(LocalDate.of(2021,12,11))
                .build()));
        List<StopTimeModel> stopTimeModelList = controller.getDeparturesByDate("Lakeside", "2021-04-10");
        assertEquals(1, stopTimeModelList.size());
        assertEquals("101", stopTimeModelList.get(0).getJourneyNumber());
    }

    /**
     * Test the file upload endpoint of this controller.
     */
    @Test
    public void testHandleFileUpload() {
        //First of all test the happy case that it works as planned.
        ImportZipRequest importZipRequest = new ImportZipRequest();
        importZipRequest.setZipFile(new MockMultipartFile("test", new byte[8]));
        importZipRequest.setFileFormat("General Transit Feed Specification (GTFS)");
        importZipRequest.setRoutesToImport("1A,2B");
        Mockito.when(fileSystemStorageService.store(importZipRequest.getZipFile())).thenReturn("testFolder");
        Mockito.when(importGTFSDataService.readGTFSFile("testFolder", Lists.newArrayList("1A", "2B"))).thenReturn(true);
        ResponseEntity<Void> uploadResponse = controller.handleFileUpload(importZipRequest);
        assertEquals(HttpStatus.OK, uploadResponse.getStatusCode());
        //Second test the case where file could not be loaded.
        ImportZipRequest importGtfsZipBadRequest = new ImportZipRequest();
        importGtfsZipBadRequest.setZipFile(new MockMultipartFile("test", new byte[8]));
        importGtfsZipBadRequest.setRoutesToImport("3C,4D");
        importGtfsZipBadRequest.setFileFormat("General Transit Feed Specification (GTFS)");
        Mockito.when(fileSystemStorageService.store(importGtfsZipBadRequest.getZipFile())).thenReturn("testBadFolder");
        Mockito.when(importGTFSDataService.readGTFSFile("testFolder", Lists.newArrayList("3C", "3D"))).thenReturn(false);
        ResponseEntity<Void> uploadBadResponse = controller.handleFileUpload(importGtfsZipBadRequest);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, uploadBadResponse.getStatusCode());
    }

    /**
     * Test the routes endpoint of this controller.
     */
    @Test
    public void testRoutesEndpoint() {
        Mockito.when(routeService.getRoutes()).thenReturn(Lists.newArrayList(RouteModel.builder()
                .agency("Mustermann Bus GmbH")
                .id("123")
                .routeNumber("405")
                .build()));
        List<RouteModel> routeModelList = controller.getRoutes();
        assertEquals(1, routeModelList.size());
        assertEquals("123", routeModelList.get(0).getId());
    }

    /**
     * Test the stops endpoint of this controller.
     */
    @Test
    public void testStopsEndpoint() {
        Mockito.when(stopService.getStops()).thenReturn(Lists.newArrayList(StopModel.builder()
                .id("123")
                .name("Greenfield")
                .latitude(50.03)
                .longitude(123.04)
                .build()));
        List<StopModel> stopModelList = controller.getStops();
        assertEquals(1, stopModelList.size());
        assertEquals("Greenfield", stopModelList.get(0).getName());
    }

}
