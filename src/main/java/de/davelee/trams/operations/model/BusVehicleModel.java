package de.davelee.trams.operations.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * This class represents a bus. A bus has a registration number as well as all other attributes of a vehicle.
 * @author Dave Lee
 */
@Getter
@Setter
@ToString
public class BusVehicleModel extends VehicleModel {

    /**
     * The registration number of this bus.
     */
    private String registrationNumber;

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
     * @param registrationNumber a <code>String</code> containing the registration number of this bus
     */
    @Builder
    public BusVehicleModel (final String fleetNumber, final String company, final LocalDate deliveryDate, final LocalDate inspectionDate,
                            final int seatingCapacity, final int standingCapacity, final String modelName, final String livery,
                            final VehicleStatus vehicleStatus, final String registrationNumber ) {
        super(fleetNumber, company, deliveryDate, inspectionDate, seatingCapacity, standingCapacity, modelName, livery, vehicleStatus, "");
        this.registrationNumber = registrationNumber;
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
