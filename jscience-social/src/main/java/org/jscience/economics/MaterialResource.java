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
package org.jscience.economics;

import org.jscience.geography.Place;
import org.jscience.mathematics.numbers.real.Real;

import java.util.UUID;

/**
 * A class representing a modern (material) resource. * @author Silvere
 * Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * 
 */
public class MaterialResource<Q extends org.jscience.measure.Quantity<Q>> extends Resource<Q> {

    // Using UUID string for identification instead of legacy Identification class
    private final String identification;

    // Value of the single unit of this resource? Or the total pile?
    // Usually price * quantity. Let's keep it simple: "Value" is the estimated
    // monetary worth.
    private Money value;

    public MaterialResource(String name, String description, Real quantity, Organization producer, Place place,
            Money value) {
        super(name, description, quantity, producer, place);
        this.identification = UUID.randomUUID().toString();
        this.value = value;
    }

    public String getIdentification() {
        return identification;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MaterialResource))
            return false;
        MaterialResource<?> that = (MaterialResource<?>) o;
        return identification.equals(that.identification);
    }

    @Override
    public int hashCode() {
        return identification.hashCode();
    }
}
