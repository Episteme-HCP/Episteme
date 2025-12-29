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

package org.jscience.geography;

import org.jscience.politics.Country;
import org.jscience.util.identity.Identifiable;

/**
 * Represents a city or settlement.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class City extends Place implements Identifiable<String> {

    private final String id; // Typically name or code
    private final Country exactCountry; // Renamed to avoid name clash with Place.getCountry()
    private final String zipCode;
    private final int estimatedPopulation; // Renamed to avoid name clash with Place.getPopulation()

    public City(String name, Country country, String zipCode, int population) {
        super(name, Type.CITY); // Fixed constructor call
        this.id = name;
        this.exactCountry = country;
        this.zipCode = zipCode;
        this.estimatedPopulation = population;

        // Also set the string representation in the parent Place
        if (country != null) {
            super.setCountry(country.getName());
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public Country getExactCountry() {
        return exactCountry;
    }

    public String getZipCode() {
        return zipCode;
    }

    // Override generic getPopulation if necessary, or just use
    // getEstimatedPopulation
    public int getEstimatedPopulation() {
        return estimatedPopulation;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", getName(), exactCountry != null ? exactCountry.getName() : "Unknown Country");
    }
}
