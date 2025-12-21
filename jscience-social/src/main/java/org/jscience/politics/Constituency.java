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
package org.jscience.politics;

import org.jscience.geography.Place;

/**
 * Represents an electoral district or constituency. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Constituency {

    private final String name;
    private final Place area;
    private int population;
    private int electorateSize; // Number of eligible voters

    public Constituency(String name, Place area, int population) {
        this.name = name;
        this.area = area;
        this.population = population;
        this.electorateSize = (int) (population * 0.7); // Rough estimate default
    }

    public String getName() {
        return name;
    }

    public Place getArea() {
        return area;
    }

    public int getPopulation() {
        return population;
    }

    public int getElectorateSize() {
        return electorateSize;
    }

    public void setElectorateSize(int size) {
        this.electorateSize = size;
    }

    @Override
    public String toString() {
        return String.format("%s (%d voters)", name, electorateSize);
    }
}
