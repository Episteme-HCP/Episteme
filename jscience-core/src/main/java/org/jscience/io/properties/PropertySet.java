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
package org.jscience.io.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A container for properties, accessed via type-safe {@link PropertyKey}s.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PropertySet {

    private final Map<String, Object> properties = new HashMap<>();

    /**
     * Sets a property value.
     * 
     * @param key   The property key.
     * @param value The value to set.
     * @param <T>   The type of the value.
     */
    public <T> void set(PropertyKey<T> key, T value) {
        properties.put(key.getName(), value);
    }

    /**
     * Retrieves a property value.
     * 
     * @param key The property key.
     * @param <T> The type of the value.
     * @return The value, or null if not present.
     */
    public <T> T get(PropertyKey<T> key) {
        Object value = properties.get(key.getName());
        if (value == null) {
            return null;
        }

        // Type conversion logic
        if (!key.getType().isInstance(value)) {
            // Convert Number -> Real
            if (key.getType().equals(org.jscience.mathematics.numbers.real.Real.class) && value instanceof Number) {
                return key.getType().cast(org.jscience.mathematics.numbers.real.Real.of(((Number) value).doubleValue()));
            }
            // Convert String -> Quantity
            if (org.jscience.measure.Quantity.class.isAssignableFrom(key.getType()) && value instanceof String) {
                try {
                    return key.getType().cast(org.jscience.measure.Quantities.parse((String) value));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to parse Quantity from string: " + value, e);
                }
            }
        }

        // Runtime check to ensure type safety
        if (key.getType().isInstance(value)) {
            return key.getType().cast(value);
        } else {
            throw new ClassCastException("Property '" + key.getName() + "' is of type " + value.getClass().getName()
                    + " but expected " + key.getType().getName());
        }
    }

    public <T> Optional<T> getOptional(PropertyKey<T> key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Checks if a property exists.
     */
    public boolean has(PropertyKey<?> key) {
        return properties.containsKey(key.getName());
    }
}