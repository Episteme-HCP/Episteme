package org.episteme.core.technical.algorithm;

/**
 * Base exception for errors occurring during algorithm selection or execution.
 */
public class AlgorithmException extends RuntimeException {
    public AlgorithmException(String message) {
        super(message);
    }

    public AlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
