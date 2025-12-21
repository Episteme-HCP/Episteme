/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.util;

/**
 * Interface for extensible enumeration pattern.
 * <p>
 * Unlike Java enums which are closed sets, ExtensibleEnum allows
 * user-defined values to be added at runtime. Useful for:
 * <ul>
 * <li>Particle types (new particles can be discovered)</li>
 * <li>Taxonomic ranks (custom classifications)</li>
 * <li>Unit systems (domain-specific units)</li>
 * </ul>
 * </p>
 *
 * @param <T> the implementing type
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see EnumRegistry
 */
public interface ExtensibleEnum<T extends ExtensibleEnum<T>> {

    /**
     * Returns the name of this enum constant.
     */
    String name();

    /**
     * Returns the ordinal of this enum constant.
     */
    int ordinal();

    /**
     * Returns a description of this enum constant.
     */
    default String description() {
        return name();
    }

    /**
     * Checks if this is a built-in (predefined) value or user-added.
     */
    default boolean isBuiltIn() {
        return true;
    }
}
