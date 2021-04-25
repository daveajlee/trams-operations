package de.davelee.trams.operations.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * This class represents a train. A train has all attributes of a vehicle and also a power mode.
 * @author Dave Lee
 */
@Getter
@Setter
@ToString
public class TrainVehicleModel extends VehicleModel {

    /**
     * The train has a power mode which defines how the train is powered.
     */
    private TrainPowerMode powerMode;

    /**
     * Workaround for lombok problem of not being able to use builder pattern properly with inheritance. Therefore
     * helper all parameter constructor for the builder pattern.
     * @param id a <code>VehicleUniqueIdentifer</code> object containing fleet number and company
     * @param deliveryDate a <code>LocalDate</code> object containing the delivery date of vehicle
     * @param inspectionDate a <code>LocalDate</code> object containing the inspection date of vehicle
     * @param seatingCapacity a <code>int</code> containing the seating capacity of the vehicle
     * @param standingCapacity a <code>int</code> containing the standing capacity of the vehicle
     * @param modelName a <code>String</code> containing the name of the model of this vehicle
     * @param livery a <code>String</code> containing the livery of this vehicle
     * @param vehicleStatus a <code>VehicleStatus</code> object containing the current status of this vehicle
     * @param powerMode a <code>TrainPowerMode</code> which contains the method of power that the train uses.
     */
    @Builder
    public TrainVehicleModel (final VehicleModel.VehicleUniqueIdentifier id, final LocalDate deliveryDate, final LocalDate inspectionDate,
                             final int seatingCapacity, final int standingCapacity, final String modelName, final String livery,
                             final VehicleStatus vehicleStatus, final TrainPowerMode powerMode ) {
        super(id, deliveryDate, inspectionDate, seatingCapacity, standingCapacity, modelName, livery, vehicleStatus);
        this.powerMode = powerMode;
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
