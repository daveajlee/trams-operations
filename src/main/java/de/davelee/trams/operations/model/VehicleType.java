package de.davelee.trams.operations.model;

/**
 * This class represents the type of a vehicle as a simple enum which can be returned as part of the vehicle response.
 * @author Dave Lee
 */
public enum VehicleType {

    BUS {
        /**
         * Return the name of the type as a string e.g. Bus
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Bus";
        }
    },

    TRAIN {
        /**
         * Return the name of the type as a string e.g. Train
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Train";
        }
    },

    TRAM {
        /**
         * Return the name of the type as a string e.g. Tram
         * @return a <code>String</code> containing the name of the type.
         */
        @Override
        public String getTypeName() {
            return "Tram";
        }
    };

    /**
     * Return the name of the type as a string e.g. Bus
     * @return a <code>String</code> containing the name of the type.
     */
    public abstract String getTypeName();

}
