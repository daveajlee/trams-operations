package de.davelee.trams.operations.response;

import de.davelee.trams.operations.model.InspectionStatus;
import de.davelee.trams.operations.model.VehicleType;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the VehicleResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class VehicleResponseTest {

    /**
     * Ensure that a VehicleResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        VehicleResponse vehicleResponse = VehicleResponse.builder()
                .livery("Green with red text")
                .fleetNumber("213")
                .allocatedTour("1/1")
                .vehicleType("Bus")
                .additionalTypeInformationMap(Collections.singletonMap("Registration Number", "XXX2 BBB"))
                .inspectionStatus("Inspected")
                .nextInspectionDueInDays(100)
                .build();
        assertEquals("Green with red text", vehicleResponse.getLivery());
        assertEquals("213", vehicleResponse.getFleetNumber());
        assertEquals("1/1", vehicleResponse.getAllocatedTour());
        assertEquals("Bus", vehicleResponse.getVehicleType());
        assertEquals("XXX2 BBB", vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
        assertEquals("Inspected", vehicleResponse.getInspectionStatus());
        assertEquals(100, vehicleResponse.getNextInspectionDueInDays());
        vehicleResponse.setLivery("Blue with orange text");
        vehicleResponse.setFleetNumber("1213");
        vehicleResponse.setAllocatedTour("1/2");
        vehicleResponse.setVehicleType("Tram");
        vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Bidirectional", "true"));
        vehicleResponse.setInspectionStatus("Inspection Due!");
        vehicleResponse.setNextInspectionDueInDays(0);
        assertEquals("VehicleResponse(fleetNumber=1213, vehicleType=Tram, livery=Blue with orange text, allocatedTour=1/2, inspectionStatus=Inspection Due!, nextInspectionDueInDays=0, additionalTypeInformationMap={Bidirectional=true})", vehicleResponse.toString());
    }

}
