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

package org.jscience.geography;

import org.jscience.earth.Place;
import org.jscience.economics.money.Money;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Area;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

import java.util.Objects;

/**
 * Represents a geographical or geopolitical region (Country, State, City, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Region extends Place {

    private static final long serialVersionUID = 1L;

    public enum SubType {
        CONTINENT, SUBCONTINENT, COUNTRY, STATE, PROVINCE,
        COUNTY, CITY, DISTRICT, MUNICIPALITY, VILLAGE,
        TIMEZONE, CLIMATE_ZONE, ECONOMIC_ZONE, OTHER
    }

    @Attribute
    private SubType subType = SubType.OTHER;

    @Attribute
    private String parentRegionId;

    @Attribute
    private Quantity<Area> area;

    @Attribute
    private long population;

    @Attribute
    private String capital;

    @Attribute
    private String isoCode;

    @Attribute
    private Quantity<Money> gdp;

    public Region(String name, SubType subType) {
        super(name, Type.REGION);
        this.subType = Objects.requireNonNull(subType);
        this.area = Quantities.create(0.0, Units.SQUARE_METER);
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
    }

    public String getParentRegionId() {
        return parentRegionId;
    }

    public void setParentRegionId(String parentRegionId) {
        this.parentRegionId = parentRegionId;
    }

    public Quantity<Area> getArea() {
        return area;
    }

    public void setArea(Quantity<Area> area) {
        this.area = area;
    }

    /**
     * Sets the area in square kilometers.
     * @param areaSqKm the area in km^2
     */
    public void setAreaSqKm(double areaSqKm) {
        this.area = Quantities.create(areaSqKm * 1_000_000.0, Units.SQUARE_METER);
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

    public Quantity<Money> getGdp() {
        return gdp;
    }

    public void setGdp(Quantity<Money> gdp) {
        this.gdp = gdp;
    }

    /** Legacy getter for area in sq km. */
    public double getAreaSqKm() {
        return area != null ? area.to(Units.SQUARE_METER).getValue().doubleValue() / 1_000_000.0 : 0.0;
    }

    /** Legacy getter for population. */
    public long getPopulationLong() {
        return population;
    }

    /**
     * Calculates population density (people per sq km).
     */
    public double getPopulationDensity() {
        double areaKm2 = area.to(Units.SQUARE_METER).getValue().doubleValue() / 1_000_000.0;
        return areaKm2 > 0 ? population / areaKm2 : 0;
    }

    @Override
    public String toString() {
        return String.format("%s (%s), Area: %s, Pop: %d", getName(), subType, area, population);
    }
}
