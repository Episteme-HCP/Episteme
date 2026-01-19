package org.jscience.data;

import java.util.Map;
import java.util.HashMap;

/**
 * Common interface for all universal data models in JScience.
 * Provides a unified way to access metadata and structure.
 */
public interface UniversalDataModel {

    /**
     * Returns a map of metadata for this model (e.g., creation date, source, units).
     */
    default Map<String, Object> getMetadata() {
        return new HashMap<>();
    }

    /**
     * Returns a unique identifier for the type of data model.
     */
    String getModelType();

    /**
     * Validates the data model for consistency.
     */
    default boolean validate() {
        return true;
    }
}
