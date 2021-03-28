package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the RouteModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class RouteModelTest {

    /**
     * Ensure that a RouteModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        RouteModel routeModel = RouteModel.builder()
                .agency("Mustermann Bus GmbH")
                .id("123")
                .routeNumber("405")
                .build();
        //Verify the builder functionality through getter methods
        assertEquals("Mustermann Bus GmbH", routeModel.getAgency());
        assertEquals("123", routeModel.getId());
        assertEquals("405", routeModel.getRouteNumber());
        //Verify the toString method
        assertEquals("RouteModel(id=123, routeNumber=405, agency=Mustermann Bus GmbH)", routeModel.toString());
        //Now use the setter methods
        routeModel.setAgency("Max Mustermann Buses");
        routeModel.setId("1234");
        routeModel.setRouteNumber("405A");
        //And verify again through the toString methods
        assertEquals("RouteModel(id=1234, routeNumber=405A, agency=Max Mustermann Buses)", routeModel.toString());
    }

}
