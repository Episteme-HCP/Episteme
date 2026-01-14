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

package org.jscience.architecture;

/**
 * Represents a building or architectural structure.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Building {

    public enum Style {
        CLASSICAL, GOTHIC, RENAISSANCE, BAROQUE, NEOCLASSICAL,
        ART_NOUVEAU, ART_DECO, MODERNIST, POSTMODERN, CONTEMPORARY,
        BRUTALIST, DECONSTRUCTIVIST, HIGH_TECH
    }

    public enum Type {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, RELIGIOUS, EDUCATIONAL,
        GOVERNMENT, CULTURAL, HEALTHCARE, RECREATIONAL, INFRASTRUCTURE
    }

    private final String name;
    private final Style style;
    private final Type type;
    private final int yearBuilt;
    private final String architect;
    private final String location;
    private final double heightMeters;

    public Building(String name, Style style, Type type, int yearBuilt,
            String architect, String location, double heightMeters) {
        this.name = name;
        this.style = style;
        this.type = type;
        this.yearBuilt = yearBuilt;
        this.architect = architect;
        this.location = location;
        this.heightMeters = heightMeters;
    }

    public String getName() {
        return name;
    }

    public Style getStyle() {
        return style;
    }

    public Type getType() {
        return type;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public String getArchitect() {
        return architect;
    }

    public String getLocation() {
        return location;
    }

    public double getHeightMeters() {
        return heightMeters;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d) by %s - %.0fm",
                name, style, yearBuilt, architect, heightMeters);
    }

    // Famous buildings
    public static final Building EIFFEL_TOWER = new Building("Eiffel Tower", Style.ART_NOUVEAU,
            Type.CULTURAL, 1889, "Gustave Eiffel", "Paris, France", 330);
    public static final Building EMPIRE_STATE = new Building("Empire State Building", Style.ART_DECO,
            Type.COMMERCIAL, 1931, "Shreve, Lamb & Harmon", "New York, USA", 443);
    public static final Building SAGRADA_FAMILIA = new Building("Sagrada Familia", Style.ART_NOUVEAU,
            Type.RELIGIOUS, 1882, "Antoni GaudÃƒÂ­", "Barcelona, Spain", 172);
}


