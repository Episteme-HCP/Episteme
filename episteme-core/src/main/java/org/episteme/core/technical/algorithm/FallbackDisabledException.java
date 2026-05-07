package org.episteme.core.technical.algorithm;

/**
 * Exception thrown when a fallback is attempted but globally disabled (e.g., during benchmarks).
 */
public class FallbackDisabledException extends RuntimeException {
    public FallbackDisabledException(String message) {
        super(message);
    }
}
