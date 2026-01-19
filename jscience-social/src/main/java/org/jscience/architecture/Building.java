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

import org.jscience.history.time.UncertainDate;
import org.jscience.geography.Place;
import java.util.Objects;

/**
 * Represents a building or architectural structure.
 */
public class Building {

    public enum Style {
        CLASSICAL, GOTHIC, RENAISSANCE, BAROQUE, NEOCLASSICAL,
        ART_NOUVEAU, ART_DECO, MODERNIST, POSTMODERN, CONTEMPORARY,
        BRUTALIST, DECONSTRUCTIVIST, HIGH_TECH, BYZANTINE, ROMANESQUE,
        ISLAMIC, VERNACULAR
    }

    public enum Type {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, RELIGIOUS, EDUCATIONAL,
        GOVERNMENT, CULTURAL, HEALTHCARE, RECREATIONAL, INFRASTRUCTURE,
        MILITARY, MIXED_USE
    }

    private final String name;
    private final Style style;
    private final Type type;
    private final UncertainDate buildDate;
    private final String architect;
    private final Place location;
    private final double heightMeters;

    public Building(String name, Style style, Type type, UncertainDate buildDate,
            String architect, Place location, double heightMeters) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.style = style;
        this.type = type;
        this.buildDate = buildDate;
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

    public UncertainDate getBuildDate() {
        return buildDate;
    }

    public String getArchitect() {
        return architect;
    }

    public Place getLocation() {
        return location;
    }

    public double getHeightMeters() {
        return heightMeters;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) by %s at %s - %.1fm",
                name, style, buildDate, architect, (location != null ? location.getName() : "Unknown"), heightMeters);
    }
}


