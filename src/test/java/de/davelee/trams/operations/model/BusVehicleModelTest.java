package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the BusVehicleModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class BusVehicleModelTest {

    /**
     * Ensure that a BusVehicleModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        BusVehicleModel vehicleModel = BusVehicleModel.builder()
                .registrationNumber("W234DHDF")
                .modelName("BendyBus 2000")
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
        assertEquals("W234DHDF", vehicleModel.getRegistrationNumber());
        assertEquals("BendyBus 2000", vehicleModel.getModelName());
        assertEquals(LocalDate.of(2021,3,25), vehicleModel.getDeliveryDate());
        assertEquals(LocalDate.of(2021,4,25), vehicleModel.getInspectionDate());
        assertEquals("Green with black slide", vehicleModel.getLivery());
        assertEquals(50, vehicleModel.getSeatingCapacity());
        assertEquals(80, vehicleModel.getStandingCapacity());
        assertEquals(VehicleStatus.DELIVERED, vehicleModel.getVehicleStatus());
        assertEquals("213", vehicleModel.getFleetNumber());
        assertEquals("Lee Buses", vehicleModel.getCompany());
        //Verify the toString method
        assertEquals("BusVehicleModel(registrationNumber=W234DHDF)", vehicleModel.toString());
        assertEquals("VehicleModel(fleetNumber=213, company=Lee Buses, deliveryDate=2021-03-25, inspectionDate=2021-04-25, seatingCapacity=50, standingCapacity=80, modelName=BendyBus 2000, livery=Green with black slide, vehicleStatus=DELIVERED, allocatedTour=)", vehicleModel.toDetailedString());
        //Now use the setter methods
        vehicleModel.setRegistrationNumber("W234DHDG");
        vehicleModel.setModelName("BendyBus 2000 Plus");
        vehicleModel.setDeliveryDate(LocalDate.of(2021,3,31));
        vehicleModel.setInspectionDate(LocalDate.of(2021,4,5));
        vehicleModel.setLivery("Red with plus logo");
        vehicleModel.setSeatingCapacity(54);
        vehicleModel.setStandingCapacity(82);
        vehicleModel.setVehicleStatus(VehicleStatus.PURCHASED);
        vehicleModel.setFleetNumber("214");
        vehicleModel.setCompany("Lee Buses 2");
        //And verify again through the toString methods
        assertEquals("BusVehicleModel(registrationNumber=W234DHDG)", vehicleModel.toString());
        assertEquals("VehicleModel(fleetNumber=214, company=Lee Buses 2, deliveryDate=2021-03-31, inspectionDate=2021-04-05, seatingCapacity=54, standingCapacity=82, modelName=BendyBus 2000 Plus, livery=Red with plus logo, vehicleStatus=PURCHASED, allocatedTour=)", vehicleModel.toDetailedString());
    }

}
