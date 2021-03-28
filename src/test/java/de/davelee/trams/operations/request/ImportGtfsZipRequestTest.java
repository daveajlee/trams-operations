package de.davelee.trams.operations.request;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the ImportGtfsZipRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class ImportGtfsZipRequestTest {

    /**
     * Ensure that a ImportGtfsZipRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest( ) {
        ImportGtfsZipRequest importGtfsZipRequest = new ImportGtfsZipRequest();
        importGtfsZipRequest.setZipFile(new MockMultipartFile("test", new byte[8]));
        importGtfsZipRequest.setRoutesToImport("1A,2B");
        assertNotNull(importGtfsZipRequest.getZipFile());
        assertEquals("1A,2B", importGtfsZipRequest.getRoutesToImport());
    }

}
