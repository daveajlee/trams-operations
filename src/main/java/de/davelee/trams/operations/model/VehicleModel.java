package de.davelee.trams.operations.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This class represents a vehicle. A vehicle can contain an id, a fleet number and company (which in combination
 * must be unique), a delivery date, an inspection date, a seating capacity, a standing capacity, a model name,
 * a livery and a status.
 * @author Dave Lee
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class VehicleModel {

    /**
     * The id of the vehicle which is a combination of fleet number and company.
     */
    @Id
    private VehicleUniqueIdentifier id;

    /**
     * Private helper class to implement the composite key for fleet number and company.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    static class VehicleUniqueIdentifier implements Serializable {
        /**
         * The fleet number of this vehicle.
         */
        private String fleetNumber;

        /**
         * The company that owns this vehicle.
         */
        private String company;
    }

    /**
     * The date that the vehicle was delivered to its current company.
     */
    private LocalDate deliveryDate;

    /**
     * The date that the vehicle last went through an inspection.
     */
    private LocalDate inspectionDate;

    /**
     * The number of seats that this vehicle has.
     */
    private int seatingCapacity;

    /**
     * The number of persons who are allowed to stand in this vehicle.
     */
    private int standingCapacity;

    /**
     * The name of the model of this vehicle.
     */
    private String modelName;

    /**
     * The livery that this vehicle has.
     */
    private String livery;

    /**
     * The current status of the vehicle.
     */
    private VehicleStatus vehicleStatus;


}
