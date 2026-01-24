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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.jscience.geography.Place;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Named;
import org.jscience.util.Positioned;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a building or architectural structure, integrating historical, 
 * geographical, and stylistic dimensions. This model tracks construction dates, 
 * architectural styles, and physical signatures like height.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
@Persistent
public class Building implements Identified<String>, Named, Positioned<Place>, Temporal<TemporalCoordinate>, Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Major architectural styles and movements.
     */
    public enum Style {
        CLASSICAL, GOTHIC, RENAISSANCE, BAROQUE, NEOCLASSICAL,
        ART_NOUVEAU, ART_DECO, MODERNIST, POSTMODERN, CONTEMPORARY,
        BRUTALIST, DECONSTRUCTIVIST, HIGH_TECH, BYZANTINE, ROMANESQUE,
        ISLAMIC, VERNACULAR
    }

    /**
     * Functional types of buildings.
     */
    public enum Type {
        RESIDENTIAL, COMMERCIAL, INDUSTRIAL, RELIGIOUS, EDUCATIONAL,
        GOVERNMENT, CULTURAL, HEALTHCARE, RECREATIONAL, INFRASTRUCTURE,
        MILITARY, MIXED_USE
    }

    @Id
    private final String id;
    @Attribute
    private final String name;
    @Attribute
    private final Style style;
    @Attribute
    private final Type type;
    @Attribute
    private final TemporalCoordinate buildDate;
    @Attribute
    private final String architect;
    @Attribute
    private final Place location;
    @Attribute
    private final double heightMeters;

    /**
     * Creates a new Building record.
     * 
     * @param name common name of the building
     * @param style architectural style classification
     * @param type functional type classification
     * @param buildDate date of construction/completion
     * @param architect name of the lead architect or firm
     * @param location geographical location
     * @param heightMeters height in meters
     */
    public Building(String name, Style style, Type type, TemporalCoordinate buildDate,
            String architect, Place location, double heightMeters) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.style = style;
        this.type = type;
        this.buildDate = buildDate;
        this.architect = architect;
        this.location = location;
        this.heightMeters = heightMeters;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Place getPosition() {
        return location;
    }

    @Override
    public TemporalCoordinate getWhen() {
        return buildDate;
    }

    public Style getStyle() {
        return style;
    }

    public Type getType() {
        return type;
    }

    public TemporalCoordinate getBuildDate() {
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
