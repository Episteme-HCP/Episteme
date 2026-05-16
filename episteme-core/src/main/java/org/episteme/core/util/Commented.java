/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.util;

import java.util.Map;

/**
 * Interface for objects that can have comments and dynamic attributes/traits.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Commented {
    /**
     * Returns the map of traits/attributes for this object.
     * Implementations should provide a mutable map to support default methods.
     * @return the traits map
     */
    Map<String, Object> getTraits();

    /**
     * Sets a custom trait (dynamic property).
     * @param name  trait name
     * @param value trait value
     */
    default void setTrait(String name, Object value) {
        getTraits().put(name, value);
    }

    /**
     * Retrieves a custom trait.
     * @param name trait name
     * @return trait value, or null if not found
     */
    default Object getTrait(String name) {
        return getTraits().get(name);
    }

    /**
     * Retrieves a typed custom trait.
     * @param name trait name
     * @param type expected class of the trait
     * @param <T>  result type
     * @return typed trait value, or null if not found or type mismatch
     */
    default <T> T getTrait(String name, Class<T> type) {
        Object value = getTraits().get(name);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    /**
     * Returns the comments associated with this object.
     * Maps to a "comments" trait.
     * @return the comments
     */
    default String getComments() {
        return (String) getTrait("comments");
    }

    /**
     * Sets the comments for this object.
     * Maps to a "comments" trait.
     * @param comments the comments to set
     */
    default void setComments(String comments) {
        setTrait("comments", comments);
    }
}

