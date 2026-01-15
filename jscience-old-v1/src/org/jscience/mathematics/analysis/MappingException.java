package org.jscience.mathematics.analysis;

/**
 * This class represents exceptions thrown by mappings
 */

//may be we should extend Exception
public class MappingException extends RuntimeException {
/**
     * Simple constructor. Build an exception with a default message
     */
    public MappingException() {
        super("Mapping exception.");
    }

/**
     * Simple constructor. Build an exception with the specified message
     *
     * @param message exception message
     */
    public MappingException(String message) {
        super(message);
    }

/**
     * Simple constructor. Build an exception from a cause
     *
     * @param cause cause of this exception
     */
    public MappingException(Throwable cause) {
        super(cause);
    }

/**
     * Simple constructor. Build an exception from a message and a cause
     *
     * @param message exception message
     * @param cause   cause of this exception
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
