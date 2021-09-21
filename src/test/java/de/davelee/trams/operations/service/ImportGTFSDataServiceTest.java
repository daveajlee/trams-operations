package de.davelee.trams.operations.service;

import de.davelee.trams.operations.exception.StorageException;
import de.davelee.trams.operations.repository.RouteRepository;
import de.davelee.trams.operations.repository.StopRepository;
import de.davelee.trams.operations.repository.StopTimeRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onebusaway.csv_entities.exceptions.MissingRequiredEntityException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the ImportGTFSDataService class and ensures that the import work successfully. Mocks are used
 * for the database layer.
 * @author Dave Lee
 */
@SpringBootTest
public class ImportGTFSDataServiceTest {

    @InjectMocks
    private ImportGTFSDataService importGTFSDataService;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @Mock
    private StopTimeRepository stopTimeRepository;

    /**
     * Verify that it is possible to import the sample feed.
     */
    @Test
    public void testGTFSDataService ( ) {
        Mockito.when(routeRepository.findAll()).thenReturn(Lists.emptyList());
        Mockito.when(stopRepository.findAll()).thenReturn(Lists.emptyList());
        File file = new File("src/test/resources/sample-feed-1");
        assertTrue(importGTFSDataService.readGTFSFile(file.getAbsolutePath(), Lists.newArrayList()));
        assertTrue(importGTFSDataService.readGTFSFile(file.getAbsolutePath(), List.of("10", "20")));
        File zipFile = new File("src/test/resources/sample-feed-1.zip");
        Assertions.assertThrows(MissingRequiredEntityException.class, () -> importGTFSDataService.readGTFSFile(zipFile.getAbsolutePath(), Lists.newArrayList()));
        assertFalse(importGTFSDataService.readGTFSFile("no-feed", Lists.newArrayList()));
    }
}
