package de.davelee.trams.operations.model;

/**
 * This class represents the current statuses of an inspection which can be either INSPECTED meaning that a vehicle has
 * been inspected within the correct time range or INSPECTION_DUE which means that a vehicle is due for inspection urgently.
 * @author Dave Lee
 */
public enum InspectionStatus {

    /**
     * Vehicle has been inspected within the correct time range
     */
    INSPECTED,

    /**
     * Vehicle is due for inspection urgently
     */
    INSPECTION_DUE

}
