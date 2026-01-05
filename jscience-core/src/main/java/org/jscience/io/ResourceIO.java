/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.io;

/**
 * Common interface for all resource I/O components in JScience.
 * Replaces the old ResourceLoader.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ResourceIO<T> {

    /**
     * Returns the base path where this resource is located.
     */
    String getResourcePath();

    /**
     * Returns the type of resource.
     */
    Class<T> getResourceType();

    /**
     * Returns true if this uses local file resources.
     */
    default boolean isFileBased() {
        return getResourcePath() != null && !getResourcePath().startsWith("http");
    }

    /**
     * Returns expected resource file names for auditing.
     */
    default String[] getExpectedResourceFiles() {
        return new String[0];
    }

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default String getDescription() {
        return "";
    }

    default String getCategory() {
        return "category.other";
    }

    default boolean isInput() {
        return false;
    }

    default boolean isOutput() {
        return false;
    }
}
