package de.davelee.trams.operations.exception;

/**
 * This class represents an exception which can occur when uploading files. It provides a more specific exception than a normal <code>RuntimeException</code> object.
 * @author Dave Lee
 */
public class StorageException extends RuntimeException {

    /**
     * Create a new Storage Exception with a short text message with the reason why this exception occurred.
     * @param message a <code>String</code> with the reason why this exception occurred.
     */
    public StorageException(final String message) {
        super(message);
    }

    /**
     * Create a new Storage Exception with a short text message explaining the reason for the exception and the exception itself.
     * @param message a <code>String</code> with the reason why this exception occurred.
     * @param cause a <code>Throwable</code> object containing the exception itself.
     */
    public StorageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
