package de.davelee.trams.operations.model;

/**
 * This class represents the various methods through which a train can be powered.
 * @author Dave Lee
 */
public enum TrainPowerMode {

    /**
     * The train is powered by Diesel fuel.
     */
    DIESEL,

    /**
     * The train is powered by electric overhead current.
     */
    ELECTRIC,

    /**
     * The train is powered by steam.
     */
    STEAM,

    /**
     * The train is powered by hydrogen.
     */
    HYDROGEN,

    /**
     * The train is powered by battery.
     */
    BATTERY
}
