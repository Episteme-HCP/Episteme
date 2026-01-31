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

package org.jscience.social.psychology;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.Named;

/**
 * Represents a quantified psychological personality trait or behavioral 
 * tendency (e.g., the 'Big Five' traits: Openness, Conscientiousness, 
 * Extraversion, Agreeableness, and Neuroticism). 
 * 
 * <p>Trait values are normalized on a continuous scale from 0.0 to 1.0.</p>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Trait implements Named, Serializable {

    private static final long serialVersionUID = 2L;

    private final String name;
    private final String description;
    private Real value;

    /**
     * Initializes a personality trait with a specific value.
     * 
     * @param name common name of the trait
     * @param value normalized magnitude from 0.0 to 1.0
     * @throws IllegalArgumentException if value is out of range
     */
    public Trait(String name, Real value) {
        this(name, null, value);
    }

    /**
     * Initializes a personality trait with a description and value.
     * 
     * @param name common name of the trait
     * @param description qualitative summary of what the trait measures
     * @param value normalized magnitude from 0.0 to 1.0
     * @throws IllegalArgumentException if value is out of range or null
     */
    public Trait(String name, String description, Real value) {
        this.name = Objects.requireNonNull(name, "Trait name cannot be null");
        this.description = description;
        this.value = Objects.requireNonNull(value, "Trait value cannot be null");
        
        if (value.doubleValue() < 0.0 || value.doubleValue() > 1.0) {
            throw new IllegalArgumentException("Trait value must be a normalized Real between 0.0 and 1.0");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    /** @return textual description of the trait, or null if not provided */
    public String getDescription() {
        return description;
    }

    /** @return normalized intensity value (0.0 to 1.0) */
    public Real getValue() {
        return value;
    }

    /**
     * Updates the intensity of the trait.
     * @param value new normalized value
     * @throws IllegalArgumentException if value is out of range
     */
    public void setValue(Real value) {
        if (value == null || value.doubleValue() < 0.0 || value.doubleValue() > 1.0) {
            throw new IllegalArgumentException("Trait value must be a normalized Real between 0.0 and 1.0");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "%s: %.2f", name, value.doubleValue());
    }
}

