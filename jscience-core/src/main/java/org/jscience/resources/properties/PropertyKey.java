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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources.properties;

import java.util.Objects;

/**
 * A type-safe key for accessing properties in a {@link PropertySet}.
 * 
 * @param <T> The type of the property value.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PropertyKey<T> {

    private final String name;
    private final Class<T> type;

    /**
     * Creates a new property key.
     * 
     * @param name The unique name of the property (e.g., "density").
     * @param type The class of the property value (e.g., Real.class).
     */
    public PropertyKey(String name, Class<T> type) {
        this.name = Objects.requireNonNull(name, "Property name cannot be null");
        this.type = Objects.requireNonNull(type, "Property type cannot be null");
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PropertyKey<?> that = (PropertyKey<?>) o;
        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + "(" + type.getSimpleName() + ")";
    }

    /**
     * Factory method for creating keys.
     */
    public static <T> PropertyKey<T> of(String name, Class<T> type) {
        return new PropertyKey<>(name, type);
    }
}


