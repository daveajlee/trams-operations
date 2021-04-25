package de.davelee.trams.operations.model;

/**
 * This class represents the various statuses that a vehicle may have during its lifetime.
 * @author Dave Lee
 */
public enum VehicleStatus {

    /**
     * Vehicle has been purchased but not yet delivered.
     */
    PURCHASED,

    /**
     * Vehicle has been delivered.
     */
    DELIVERED,

    /**
     * Vehicle has been inspected.
     */
    INSPECTED,

    /**
     * Vehicle has been sold.
     */
    SOLD

}
