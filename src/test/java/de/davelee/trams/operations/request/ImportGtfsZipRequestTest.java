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
        ImportZipRequest importZipRequest = new ImportZipRequest();
        importZipRequest.setZipFile(new MockMultipartFile("test", new byte[8]));
        importZipRequest.setRoutesToImport("1A,2B");
        importZipRequest.setValidFromDate("01.01.2021");
        importZipRequest.setValidToDate("31.12.2021");
        assertNotNull(importZipRequest.getZipFile());
        assertEquals("1A,2B", importZipRequest.getRoutesToImport());
        assertEquals("01.01.2021", importZipRequest.getValidFromDate());
        assertEquals("31.12.2021", importZipRequest.getValidToDate());
    }

}
