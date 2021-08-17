package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the TrainVehicleModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class TrainVehicleModelTest {

    /**
     * Ensure that a TrainVehicleModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        TrainVehicleModel vehicleModel = TrainVehicleModel.builder()
                .powerMode(TrainPowerMode.DIESEL)
                .modelName("Train 2000 Di")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.DELIVERED)
                .fleetNumber("213")
                .company("Lee Buses")
                .build();
        //Verify the builder functionality through getter methods
        assertEquals(TrainPowerMode.DIESEL, vehicleModel.getPowerMode());
        assertEquals("Train 2000 Di", vehicleModel.getModelName());
        assertEquals(LocalDate.of(2021,3,25), vehicleModel.getDeliveryDate());
        assertEquals(LocalDate.of(2021,4,25), vehicleModel.getInspectionDate());
        assertEquals("Green with black slide", vehicleModel.getLivery());
        assertEquals(50, vehicleModel.getSeatingCapacity());
        assertEquals(80, vehicleModel.getStandingCapacity());
        assertEquals(VehicleStatus.DELIVERED, vehicleModel.getVehicleStatus());
        assertEquals("213", vehicleModel.getFleetNumber());
        assertEquals("Lee Buses", vehicleModel.getCompany());
        //Verify the toString method
        assertEquals("TrainVehicleModel(powerMode=Diesel)", vehicleModel.toString());
        assertEquals("VehicleModel(fleetNumber=213, company=Lee Buses, deliveryDate=2021-03-25, inspectionDate=2021-04-25, seatingCapacity=50, standingCapacity=80, modelName=Train 2000 Di, livery=Green with black slide, vehicleStatus=DELIVERED, allocatedTour=)", vehicleModel.toDetailedString());
        //Now use the setter methods
        vehicleModel.setPowerMode(TrainPowerMode.ELECTRIC);
        vehicleModel.setModelName("Train 2000 El");
        vehicleModel.setDeliveryDate(LocalDate.of(2021,3,31));
        vehicleModel.setInspectionDate(LocalDate.of(2021,4,5));
        vehicleModel.setLivery("Red with plus logo");
        vehicleModel.setSeatingCapacity(54);
        vehicleModel.setStandingCapacity(82);
        vehicleModel.setVehicleStatus(VehicleStatus.PURCHASED);
        vehicleModel.setFleetNumber("214");
        vehicleModel.setCompany("Lee Buses 2");
        //And verify again through the toString methods
        assertEquals("TrainVehicleModel(powerMode=Electric)", vehicleModel.toString());
        assertEquals("VehicleModel(fleetNumber=214, company=Lee Buses 2, deliveryDate=2021-03-31, inspectionDate=2021-04-05, seatingCapacity=54, standingCapacity=82, modelName=Train 2000 El, livery=Red with plus logo, vehicleStatus=PURCHASED, allocatedTour=)", vehicleModel.toDetailedString());
    }

}
