package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.*;
import de.davelee.trams.operations.repository.BusVehicleRepository;
import de.davelee.trams.operations.repository.TrainVehicleRepository;
import de.davelee.trams.operations.repository.TramVehicleRepository;
import de.davelee.trams.operations.response.VehicleResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the VehicleService class and ensures that it works successfully. Mocks are used for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private BusVehicleRepository busVehicleRepository;

    @Mock
    private TramVehicleRepository tramVehicleRepository;

    @Mock
    private TrainVehicleRepository trainVehicleRepository;

    /**
     * Ensure that a bus can be added successfully to the mock database.
     */
    @Test
    public void testAddBus() {
        //Test data.
        BusVehicleModel vehicleModel = BusVehicleModel.builder()
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
        Mockito.when(busVehicleRepository.insert(vehicleModel)).thenReturn(vehicleModel);
        assertTrue(vehicleService.addBus(vehicleModel));
    }

    /**
     * Ensure that a train can be added successfully to the mock database.
     */
    @Test
    public void testAddTrain() {
        //Test data
        TrainVehicleModel vehicleModel = TrainVehicleModel.builder()
                .powerMode(TrainPowerMode.DIESEL)
                .modelName("Train 2000 Di")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(trainVehicleRepository.insert(vehicleModel)).thenReturn(vehicleModel);
        assertTrue(vehicleService.addTrain(vehicleModel));
    }

    /**
     * Ensure that a tram can be added successfully to the mock database.
     */
    @Test
    public void testAddTram() {
        //Test data
        TramVehicleModel vehicleModel = TramVehicleModel.builder()
                .isBidirectional(true)
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(tramVehicleRepository.insert(vehicleModel)).thenReturn(vehicleModel);
        assertTrue(vehicleService.addTram(vehicleModel));
    }

    /**
     * Ensure that data can be retrieved from the mock database and supplied as a response.
     */
    @Test
    public void testRetrieveAllVehicles() {
        //Test data.
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
        Mockito.when(busVehicleRepository.findAll()).thenReturn(List.of(busVehicleModel));
        TrainVehicleModel trainVehicleModel = TrainVehicleModel.builder()
                .powerMode(TrainPowerMode.DIESEL)
                .modelName("Train 2000 Di")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(trainVehicleRepository.findAll()).thenReturn(List.of(trainVehicleModel));
        TramVehicleModel tramVehicleModel = TramVehicleModel.builder()
                .isBidirectional(true)
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(tramVehicleRepository.findAll()).thenReturn(List.of(tramVehicleModel));
        //Now do actual test.
        List<VehicleResponse> vehicleResponseList = vehicleService.retrieveAllVehicles();
        assertEquals(VehicleType.TRAIN, vehicleResponseList.get(0).getVehicleType());
        assertEquals(VehicleType.BUS, vehicleResponseList.get(1).getVehicleType());
        assertEquals(VehicleType.TRAM, vehicleResponseList.get(2).getVehicleType());
    }

    /**
     * Ensure that data can be retrieved by searching for fleet number and company name
     * from the mock database and supplied as a response.
     */
    @Test
    public void testRetrieveVehiclesByCompanyAndFleetNumber() {
        //Test data.
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
        Mockito.when(busVehicleRepository.findByCompanyStartsWithAndFleetNumberStartsWith("Lee", "21")).thenReturn(List.of(busVehicleModel));
        TrainVehicleModel trainVehicleModel = TrainVehicleModel.builder()
                .powerMode(TrainPowerMode.DIESEL)
                .modelName("Train 2000 Di")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(trainVehicleRepository.findByCompanyStartsWithAndFleetNumberStartsWith("Lee", "21")).thenReturn(List.of(trainVehicleModel));
        TramVehicleModel tramVehicleModel = TramVehicleModel.builder()
                .isBidirectional(true)
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        Mockito.when(tramVehicleRepository.findByCompanyStartsWithAndFleetNumberStartsWith("Lee", "21")).thenReturn(List.of(tramVehicleModel));
        //Now do actual test.
        List<VehicleResponse> vehicleResponseList = vehicleService.retrieveVehiclesByCompanyAndFleetNumber("Lee", "21");
        assertEquals(VehicleType.TRAIN, vehicleResponseList.get(0).getVehicleType());
        assertEquals(VehicleType.BUS, vehicleResponseList.get(1).getVehicleType());
        assertEquals(VehicleType.TRAM, vehicleResponseList.get(2).getVehicleType());
    }

}
