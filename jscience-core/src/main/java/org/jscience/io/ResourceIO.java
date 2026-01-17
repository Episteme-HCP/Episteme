/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
 * Common interface for all resource I/O components in JScience.
 * Replaces the old ResourceLoader.
 * 
 * Implementations MUST override: getCategory(), getName(), getDescription()
 * with proper I18n support.
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

    /**
     * Returns the display name of this resource handler.
     * MUST be implemented with I18n support.
     * @return the display name
     */
    String getName();

    /**
     * Returns a short description of this resource handler.
     * MUST be implemented with I18n support.
     * @return the description
     */
    String getDescription();

    /**
     * Returns a long description of this resource handler.
     * MUST be implemented with I18n support.
     * @return the long description
     */
    String getLongDescription();

    /**
     * Returns the category for grouping.
     * MUST be implemented with I18n support.
     * @return the category name
     */
    String getCategory();

    /**
     * Returns true if this is an input (reader) resource.
     */
    default boolean isInput() {
        return false;
    }

    /**
     * Returns true if this is an output (writer) resource.
     */
    default boolean isOutput() {
        return false;
    }
}
