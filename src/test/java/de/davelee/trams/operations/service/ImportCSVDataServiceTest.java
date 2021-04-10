package de.davelee.trams.operations.service;

import de.davelee.trams.operations.repository.RouteRepository;
import de.davelee.trams.operations.repository.StopRepository;
import de.davelee.trams.operations.repository.StopTimeRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the ImportCSVDataService class and ensures that the import work successfully. Mocks are used
 * for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class ImportCSVDataServiceTest {

    @InjectMocks
    private ImportCSVDataService importCSVDataService;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @Mock
    private StopTimeRepository stopTimeRepository;

    /**
     * Verify that it is possible to import the sample directory.
     */
    @Test
    public void testCSVDataService ( ) {
        Mockito.when(routeRepository.findAll()).thenReturn(Lists.emptyList());
        Mockito.when(stopRepository.findAll()).thenReturn(Lists.emptyList());
        File file = new File("src/test/resources/my-network-landuff");
        assertTrue(importCSVDataService.readCSVFile(file.getAbsolutePath(), "2021-01-01", "2021-12-31"));
        assertFalse(importCSVDataService.readCSVFile("no-feed", "2021-01-01", "2021-12-31"));
    }

}
