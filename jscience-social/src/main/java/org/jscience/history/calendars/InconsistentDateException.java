package org.jscience.history.calendars;

import java.time.DateTimeException;

/**
 * Thrown when a date string is determined to be inconsistent or invalid 
 * during parsing or construction.
 */
public class InconsistentDateException extends DateTimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InconsistentDateException object with the specified detail message.
     *
     * @param message the detail message.
     */
    public InconsistentDateException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InconsistentDateException object with the specified detail message and cause.
     * 
     * @param message the detail message.
     * @param cause the cause.
     */
    public InconsistentDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
