package de.davelee.trams.operations.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the StorageException class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StorageExceptionTest {

    /**
     * Ensure that a StorageException class can be correctly instantiated.
     */
    @Test
    public void testCreateException() {
        StorageException storageException = new StorageException("Could not initialize storage");
        assertNotNull(storageException);
        assertEquals("Could not initialize storage", storageException.getMessage());
        StorageException storageException1 = new StorageException("Could not initialize storage", new Throwable());
        assertNotNull(storageException1);
        assertEquals("Could not initialize storage", storageException.getMessage());
        assertNotNull(storageException1.getCause());
    }

}
