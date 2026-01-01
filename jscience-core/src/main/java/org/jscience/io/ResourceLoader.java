/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.io;

/**
 * Common interface for all resource loaders in JScience.
 * <p>
 * This interface ensures a consistent contract for loading domain objects
 * from various sources (JSON files, APIs, databases). All loaders should
 * implement this interface to enable resource auditing and discovery.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface ResourceLoader<T> {

    /**
     * Loads a resource by its identifier.
     *
     * @param resourceId the unique identifier for the resource
     * @return the loaded resource object
     * @throws Exception if loading fails
     */
    T load(String resourceId) throws Exception;

    /**
     * Returns the base path where this loader's resources are located.
     * <p>
     * For file-based loaders, this is typically a classpath resource path
     * like "/org/jscience/chemistry/". For API-based loaders, this may
     * return the API base URL or null.
     * </p>
     *
     * @return the resource path, or null if not applicable
     */
    String getResourcePath();

    /**
     * Returns the type of resource this loader produces.
     *
     * @return the Class object for type T
     */
    Class<T> getResourceType();

    /**
     * Returns true if this loader uses local file resources.
     * <p>
     * Loaders returning true are expected to have backing JSON/XML files
     * that can be audited for existence.
     * </p>
     *
     * @return true if file-based, false if API-based or other
     */
    default boolean isFileBased() {
        return getResourcePath() != null && !getResourcePath().startsWith("http");
    }

    /**
     * Returns the expected resource file name(s) for this loader.
     * <p>
     * Used by ResourceAuditTest to verify resource existence.
     * </p>
     *
     * @return array of expected resource file names, or empty array if not
     *         applicable
     */
    default String[] getExpectedResourceFiles() {
        return new String[0];
    }
}


