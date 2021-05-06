package de.davelee.trams.operations.model;

/**
 * This class represents the various methods through which a train can be powered.
 * @author Dave Lee
 */
public enum TrainPowerMode {

    /**
     * The train is powered by Diesel fuel.
     */
    DIESEL {
        @Override
        public String toString() {
            return "Diesel";
        }
    },

    /**
     * The train is powered by electric overhead current.
     */
    ELECTRIC {
        @Override
        public String toString() {
            return "Electric";
        }
    },

    /**
     * The train is powered by steam.
     */
    STEAM {
        @Override
        public String toString() {
            return "Steam";
        }
    },

    /**
     * The train is powered by hydrogen.
     */
    HYDROGEN {
        @Override
        public String toString() {
            return "Hydrogen";
        }
    },

    /**
     * The train is powered by battery.
     */
    BATTERY {
        @Override
        public String toString() {
            return "Battery";
        }
    }
}
