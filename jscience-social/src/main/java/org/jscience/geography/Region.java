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
package org.jscience.geography;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents geographic/geopolitical region.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Region {

    public enum Type {
        CONTINENT, SUBCONTINENT, COUNTRY, STATE, PROVINCE,
        COUNTY, CITY, DISTRICT, TIMEZONE, CLIMATE_ZONE
    }

    private final String name;
    private Type type;
    private String parentRegion;
    private Real areaSqKm;
    private long population;
    private Coordinate center;
    private String capital;
    // New Data Loader Fields
    private String code; // ISO Code
    private org.jscience.economics.Money gdp;

    public Region(String name, Type type) {
        this.name = name;
        this.type = type;
        this.areaSqKm = Real.ZERO;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getParentRegion() {
        return parentRegion;
    }

    public Real getAreaSqKm() {
        return areaSqKm;
    }

    public long getPopulation() {
        return population;
    }

    public Coordinate getCenter() {
        return center;
    }

    public String getCapital() {
        return capital;
    }

    public String getCode() {
        return code;
    }

    public org.jscience.economics.Money getGdp() {
        return gdp;
    }

    // Setters
    public void setType(Type type) {
        this.type = type;
    }

    public void setParentRegion(String parent) {
        this.parentRegion = parent;
    }

    public void setAreaSqKm(Real area) {
        this.areaSqKm = area;
    }

    public void setAreaSqKm(double area) {
        this.areaSqKm = Real.of(area);
    }

    public void setPopulation(long pop) {
        this.population = pop;
    }

    public void setCenter(Coordinate center) {
        this.center = center;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setGdp(org.jscience.economics.Money gdp) {
        this.gdp = gdp;
    }

    /** Population density (people per sq km) */
    public Real getPopulationDensity() {
        return areaSqKm.isZero() ? Real.ZERO : Real.of(population).divide(areaSqKm);
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.0f km², pop %d", name, type, areaSqKm.doubleValue(), population);
    }

    // Factory methods for major regions
    public static Region europe() {
        Region r = new Region("Europe", Type.CONTINENT);
        r.setAreaSqKm(10_180_000);
        r.setPopulation(750_000_000);
        return r;
    }

    public static Region california() {
        Region r = new Region("California", Type.STATE);
        r.setParentRegion("United States");
        r.setAreaSqKm(423_970);
        r.setPopulation(39_000_000);
        r.setCapital("Sacramento");
        return r;
    }
}