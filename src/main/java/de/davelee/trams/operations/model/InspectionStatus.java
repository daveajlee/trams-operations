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
    INSPECTED {
        /**
         * Return the current status of an inspection e.g. Inspected as a String.
         * @return a <code>String</code> containing the current status of this vehicle with regards to inspection.
         */
        @Override
        public String getInspectionNotice() {
            return "Inspected";
        }
    },

    /**
     * Vehicle is due for inspection urgently
     */
    INSPECTION_DUE {
        /**
         * Return the current status of an inspection e.g. Inspection Due! as a String.
         * @return a <code>String</code> containing the current status of this vehicle with regards to inspection.
         */
        @Override
        public String getInspectionNotice() {
            return "Inspection Due!";
        }
    };

    /**
     * Return the current status of an inspection as a String.
     * @return a <code>String</code> containing the current status of this vehicle with regards to inspection.
     */
    public abstract String getInspectionNotice( );

}
