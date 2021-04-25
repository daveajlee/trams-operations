package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the TramVehicleModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class TramVehicleModelTest {

    /**
     * Ensure that a TramVehicleModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        TramVehicleModel vehicleModel = TramVehicleModel.builder()
                .isBidirectional(true)
                .modelName("Tram 2000 Bi")
                .deliveryDate(LocalDate.of(2021,3,25))
                .inspectionDate(LocalDate.of(2021,4,25))
                .livery("Green with black slide")
                .seatingCapacity(50)
                .standingCapacity(80)
                .vehicleStatus(VehicleStatus.INSPECTED)
                .id(new VehicleModel.VehicleUniqueIdentifier("213", "Lee Buses"))
                .build();
        //Verify the builder functionality through getter methods
        assertTrue(vehicleModel.isBidirectional());
        assertEquals("Tram 2000 Bi", vehicleModel.getModelName());
        assertEquals(LocalDate.of(2021,3,25), vehicleModel.getDeliveryDate());
        assertEquals(LocalDate.of(2021,4,25), vehicleModel.getInspectionDate());
        assertEquals("Green with black slide", vehicleModel.getLivery());
        assertEquals(50, vehicleModel.getSeatingCapacity());
        assertEquals(80, vehicleModel.getStandingCapacity());
        assertEquals(VehicleStatus.INSPECTED, vehicleModel.getVehicleStatus());
        assertEquals("213", vehicleModel.getId().getFleetNumber());
        assertEquals("Lee Buses", vehicleModel.getId().getCompany());
        //Verify the toString method
        assertEquals("TramVehicleModel(isBidirectional=true)", vehicleModel.toString());
        assertEquals("VehicleModel(id=VehicleModel.VehicleUniqueIdentifier(fleetNumber=213, company=Lee Buses), deliveryDate=2021-03-25, inspectionDate=2021-04-25, seatingCapacity=50, standingCapacity=80, modelName=Tram 2000 Bi, livery=Green with black slide, vehicleStatus=INSPECTED)", vehicleModel.toDetailedString());
        //Now use the setter methods
        vehicleModel.setBidirectional(false);
        vehicleModel.setModelName("Tram 2000");
        vehicleModel.setDeliveryDate(LocalDate.of(2021,3,31));
        vehicleModel.setInspectionDate(LocalDate.of(2021,4,5));
        vehicleModel.setLivery("Red with plus logo");
        vehicleModel.setSeatingCapacity(54);
        vehicleModel.setStandingCapacity(82);
        vehicleModel.setVehicleStatus(VehicleStatus.PURCHASED);
        vehicleModel.setId(new VehicleModel.VehicleUniqueIdentifier("214", "Lee Buses 2"));
        //And verify again through the toString methods
        assertEquals("TramVehicleModel(isBidirectional=false)", vehicleModel.toString());
        assertEquals("VehicleModel(id=VehicleModel.VehicleUniqueIdentifier(fleetNumber=214, company=Lee Buses 2), deliveryDate=2021-03-31, inspectionDate=2021-04-05, seatingCapacity=54, standingCapacity=82, modelName=Tram 2000, livery=Red with plus logo, vehicleStatus=PURCHASED)", vehicleModel.toDetailedString());
    }

}
