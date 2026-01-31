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

package org.jscience.social.politics;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.Named;

/**
 * Represents an electoral district or voting precinct.
 * Constituencies define the geographic area and population size for representative allocation.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Constituency implements Named, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final Place area;
    private int population;
    private int electorateSize; // Number of eligible voters

    /**
     * Creates a new Constituency.
     *
     * @param name       the name of the district
     * @param area       the geographic region
     * @param population total resident population
     * @throws NullPointerException if name or area is null
     */
    public Constituency(String name, Place area, int population) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.area = Objects.requireNonNull(area, "Area cannot be null");
        this.population = population;
        this.electorateSize = (int) (population * 0.7); // Rough estimate default
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the geographic area of the constituency.
     * @return the place/area
     */
    public Place getArea() {
        return area;
    }

    /**
     * Returns the total population count.
     * @return population
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the total population count.
     * @param population new population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Returns the estimated or actual number of eligible voters.
     * @return electorate size
     */
    public int getElectorateSize() {
        return electorateSize;
    }

    /**
     * Updates the number of eligible voters in the constituency.
     * @param size electorate size
     */
    public void setElectorateSize(int size) {
        this.electorateSize = size;
    }

    @Override
    public String toString() {
        return String.format("%s (%d voters)", name, electorateSize);
    }
}

