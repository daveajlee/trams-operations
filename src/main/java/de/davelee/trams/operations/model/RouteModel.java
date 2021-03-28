package de.davelee.trams.operations.model;

import lombok.*;

/**
 * This class represents a route. A route can contain an id, a route number and an agency who runs the route on a regular basis.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@ToString
public class RouteModel {

    /**
     * The id of the route.
     */
    private String id;

    /**
     * The number of the route which can contain either alphabetical and alphanumeric characters.
     */
    private String routeNumber;

    /**
     * The agency or company who runs the route - currently only one company can run a particular route.
     */
    private String agency;

}
