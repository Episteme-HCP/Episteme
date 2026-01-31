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

package org.jscience.social.geography;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.natural.earth.Place;
import org.jscience.natural.earth.PlaceType;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Area;
import org.jscience.social.economics.money.Money;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * A region is an area, typically part of a country or the world, having definable characteristics but not always fixed boundaries.
 * Examples: "Middle East", "Silicon Valley", "Provence".
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Region extends Place {

    private static final long serialVersionUID = 1L;

    public enum SubType {
        CONTINENT, COUNTRY, STATE, PROVINCE, COUNTY, CITY, DISTRICT, OTHER
    }

    @Attribute
    private final SubType subType;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Region parent;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Region> subRegions = new HashSet<>();

    @Attribute
    private Quantity<Area> area;

    @Attribute
    private long population;

    @Attribute
    private String capital; // Name of the capital city/place

    @Attribute
    private String isoCode; // ISO 3166 code if applicable

    @Attribute
    private Quantity<Money> gdp;

    public Region(String name, SubType subType) {
        super(name, PlaceType.REGION);
        this.subType = Objects.requireNonNull(subType);
        this.area = Quantities.create(0.0, Units.SQUARE_METER);
    }

    public SubType getSubType() {
        return subType;
    }

    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
        if (parent != null) {
            parent.subRegions.add(this);
        }
    }

    public Set<Region> getSubRegions() {
        return Collections.unmodifiableSet(subRegions);
    }

    public Quantity<Area> getArea() {
        return area;
    }

    public void setArea(Quantity<Area> area) {
        this.area = area;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public Quantity<Money> getGDP() {
        return gdp;
    }

    public void setGDP(Quantity<Money> gdp) {
        this.gdp = gdp;
    }

    @Override
    public String toString() {
        return getName() + " (" + subType + ")";
    }
}

