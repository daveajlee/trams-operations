package de.davelee.trams.operations.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * This class represents a tram. A tram has all attributes of a vehicle and can optionally be bidirectional.
 * @author Dave Lee
 */
@Getter
@Setter
@ToString
public class TramVehicleModel extends VehicleModel {

    /**
     * The tram can be bidirectional otherwise if it is false or null then the tram can only go in a single direction.
     */
    private boolean isBidirectional;

    /**
     * Workaround for lombok problem of not being able to use builder pattern properly with inheritance. Therefore
     * helper all parameter constructor for the builder pattern.
     * @param fleetNumber a <code>String</code> containing the fleet number of the vehicle
     * @param company a <code>String</code> containing the name of the company that the vehicle belongs to.
     * @param deliveryDate a <code>LocalDate</code> object containing the delivery date of vehicle
     * @param inspectionDate a <code>LocalDate</code> object containing the inspection date of vehicle
     * @param seatingCapacity a <code>int</code> containing the seating capacity of the vehicle
     * @param standingCapacity a <code>int</code> containing the standing capacity of the vehicle
     * @param modelName a <code>String</code> containing the name of the model of this vehicle
     * @param livery a <code>String</code> containing the livery of this vehicle
     * @param vehicleStatus a <code>VehicleStatus</code> object containing the current status of this vehicle
     * @param isBidirectional a <code>boolean</code> which is true iff this tram is bidirectional
     */
    @Builder
    public TramVehicleModel (final String fleetNumber, final String company, final LocalDate deliveryDate, final LocalDate inspectionDate,
                            final int seatingCapacity, final int standingCapacity, final String modelName, final String livery,
                            final VehicleStatus vehicleStatus, final boolean isBidirectional ) {
        super(fleetNumber, company, deliveryDate, inspectionDate, seatingCapacity, standingCapacity, modelName, livery, vehicleStatus, "");
        this.isBidirectional = isBidirectional;
    }

    /**
     * A helper method to return all of the vehicle information which all vehicles contain. Specific information about
     * this bus is available via the normal to string method.
     * @return a <code>String</code> containing all of the standard vehicle information.
     */
    public String toDetailedString ( ) {
        return super.toString();
    }

}
