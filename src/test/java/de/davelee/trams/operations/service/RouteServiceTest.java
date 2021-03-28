package de.davelee.trams.operations.service;

import de.davelee.trams.operations.model.RouteModel;
import de.davelee.trams.operations.repository.RouteRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the RouteService class and ensures that it works successfully. Mocks are used for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private RouteRepository routeRepository;

    /**
     * Verify that routes can be retrieved from the database correctly.
     */
    @Test
    public void testService ( ) {
        Mockito.when(routeRepository.findAll()).thenReturn(Lists.newArrayList(RouteModel.builder()
                .routeNumber("1A")
                .id("1")
                .agency("Mustermann Bus GmbH")
                .build()));
        assertEquals(1, routeService.getRoutes().size());
        assertEquals("1A", routeService.getRoutes().get(0).getRouteNumber());
        assertEquals("1", routeService.getRoutes().get(0).getId());
        assertEquals("Mustermann Bus GmbH", routeService.getRoutes().get(0).getAgency());
    }
}
