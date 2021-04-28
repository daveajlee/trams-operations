package de.davelee.trams.operations.response;

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
                .vehicleType(VehicleType.BUS)
                .additionalTypeInformationMap(Collections.singletonMap("Registration Number", "XXX2 BBB"))
                .build();
        assertEquals("Green with red text", vehicleResponse.getLivery());
        assertEquals("213", vehicleResponse.getFleetNumber());
        assertEquals("1/1", vehicleResponse.getAllocatedTour());
        assertEquals(VehicleType.BUS, vehicleResponse.getVehicleType());
        assertEquals("XXX2 BBB", vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
        vehicleResponse.setLivery("Blue with orange text");
        vehicleResponse.setFleetNumber("1213");
        vehicleResponse.setAllocatedTour("1/2");
        vehicleResponse.setVehicleType(VehicleType.TRAM);
        vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Bidirectional", "true"));
        assertEquals("VehicleResponse(fleetNumber=1213, vehicleType=TRAM, livery=Blue with orange text, allocatedTour=1/2, additionalTypeInformationMap={Bidirectional=true})", vehicleResponse.toString());
    }

}
