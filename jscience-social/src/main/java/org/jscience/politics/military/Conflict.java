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

package org.jscience.politics.military;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.jscience.earth.Place;
import org.jscience.politics.Country;

/**
 * Represents a military conflict or war.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Conflict {
    private String name;
    private Place location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Country> belligerents = new HashSet<>();

    public Conflict(String name, Place location, LocalDate startDate) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
    }

    public String getName() { return name; }
    public Place getLocation() { return location; }
    public LocalDate getStartDate() { return startDate; }
    public void addBelligerent(Country country) { belligerents.add(country); }
    public Set<Country> getBelligerents() { return belligerents; }
    public boolean isActive() { return endDate == null; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
