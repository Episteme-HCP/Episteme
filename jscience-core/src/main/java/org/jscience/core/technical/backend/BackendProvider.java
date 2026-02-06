package org.jscience.core.technical.backend;

/**
 * Placeholder interface for BackendProvider to resolve compilation errors.
 */
public interface BackendProvider<T> {
    T createBackend();
}
