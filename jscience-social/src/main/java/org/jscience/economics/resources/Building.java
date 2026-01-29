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

package org.jscience.economics.resources;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.economics.Community;
import org.jscience.economics.money.Money;
import org.jscience.earth.Place;
import org.jscience.measure.Quantity;
import org.jscience.util.identity.Identification;
import org.jscience.history.time.TimeCoordinate;

/**
 * Represents a human-made physical structure such as a house, factory, or skyscraper.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Building extends Artifact implements Store {

    private static final long serialVersionUID = 1L;

    private Set<Object> contents;
    private String purpose;

    /**
     * Initializes a new building resource.
     */
    public Building(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
        this.contents = new HashSet<>();
    }

    @Override
    public Set<Object> getContents() {
        return Collections.unmodifiableSet(contents);
    }

    /**
     * Returns the intended purpose of the building.
     * @return the purpose string, or null if not set
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Sets the intended purpose of the building.
     * @param purpose descriptive purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override public void getIn() { /* Implementation hook */ }
    @Override public void getOut() { /* Implementation hook */ }
}
