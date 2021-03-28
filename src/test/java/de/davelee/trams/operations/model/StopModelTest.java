package de.davelee.trams.operations.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopModel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopModelTest {

    /**
     * Ensure that a StopModel class can be correctly instantiated.
     */
    @Test
    public void testBuilderGetterSetterToString ( ) {
        //Test builder
        StopModel stopModel = StopModel.builder()
                .id("123")
                .name("Greenfield")
                .latitude(50.03)
                .longitude(123.04)
                .build();
        //Verify the builder functionality through getter methods
        assertEquals("123", stopModel.getId());
        assertEquals("Greenfield", stopModel.getName());
        assertEquals(50.03, stopModel.getLatitude());
        assertEquals(123.04, stopModel.getLongitude());
        //Verify the toString method
        assertEquals("StopModel(id=123, name=Greenfield, latitude=50.03, longitude=123.04)", stopModel.toString());
        //Now use the setter methods
        stopModel.setId("1234");
        stopModel.setName("Greenerfield");
        stopModel.setLatitude(52.03);
        stopModel.setLongitude(133.04);
        //And verify again through the toString methods
        assertEquals("StopModel(id=1234, name=Greenerfield, latitude=52.03, longitude=133.04)", stopModel.toString());
    }

}
