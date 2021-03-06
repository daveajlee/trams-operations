package de.davelee.trams.operations.response;

import de.davelee.trams.operations.model.InspectionStatus;
import de.davelee.trams.operations.model.VehicleType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class VehicleResponse {

    /**
     * The fleet number of this vehicle.
     */
    private String fleetNumber;

    /**
     * The type of this vehicle which is mapped from subclasses as appropriate.
     */
    private String vehicleType;

    /**
     * The livery that this vehicle has.
     */
    private String livery;

    /**
     * The allocated tour for this vehicle.
     */
    private String allocatedTour;

    /**
     * The current status of inspection for this vehicle.
     */
    private String inspectionStatus;

    /**
     * The number of days until the next inspection is due.
     */
    private long nextInspectionDueInDays;

    /**
     * The additional parameters relevant to this vehicle type e.g. registration number for buses are stored as key/value pairs.
     */
    private Map<String, String> additionalTypeInformationMap;

}
