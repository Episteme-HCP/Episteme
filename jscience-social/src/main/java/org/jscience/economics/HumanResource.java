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

package org.jscience.economics;


import org.jscience.history.time.TimeCoordinate;
import org.jscience.history.time.TimePoint;
import java.util.Objects;
import org.jscience.economics.money.Money;
import org.jscience.earth.Place;
import org.jscience.measure.Quantity;

/**
 * Models human labor and expertise as an economic resource.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HumanResource extends Resource implements Property {

    private static final long serialVersionUID = 1L;

    private Money value;

    /**
     * Standard human resource constructor.
     * 
     * @param value market price or wage for this labor quantity
     */
    public HumanResource(String name, String description, Quantity<?> amount,
            Community producer, Money value) {
        super(name, description, amount, producer, producer.getPosition(), TimePoint.now());
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    public HumanResource(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Money value) {
        super(name, description, amount, producer, productionPlace, productionDate);
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    /** Returns the market valuation (price/wage) of this human resource. */
    public Money getValue() { return value; }

    public void setValue(Money value) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof HumanResource that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
