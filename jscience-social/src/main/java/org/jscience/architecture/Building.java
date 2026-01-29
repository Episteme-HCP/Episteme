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

import org.jscience.history.time.TimePoint;
import java.util.UUID;
import org.jscience.economics.money.Money;
import org.jscience.economics.resources.Artifact;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a building or architectural structure, integrating historical, 
 * geographical, and stylistic dimensions. This model tracks construction dates, 
 * architectural styles, and physical signatures like height.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Building extends Artifact {

    private static final long serialVersionUID = 3L;

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

    @Attribute
    private final Style style;
    @Attribute
    private final Type type;
    @Attribute
    private final String architect;
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
    public Building(String name, Style style, Type type, TimeCoordinate buildDate,
            String architect, Place location, double heightMeters) {
        super(name, "", Quantities.create(1, Units.ONE), null, location, 
              buildDate != null ? buildDate : TimePoint.now(), 
              new UUIDIdentification(UUID.randomUUID().toString()), Money.usd(0));
        this.style = style;
        this.type = type;
        this.architect = architect;
        this.heightMeters = heightMeters;
    }

    @Override
    public Place getPosition() {
        return getProductionPlace();
    }

    public Style getStyle() {
        return style;
    }

    public Type getType() {
        return type;
    }

    public TimeCoordinate getBuildDate() {
        return null; // Should be handled by productionDate in parent
    }

    public String getArchitect() {
        return architect;
    }

    public Place getLocation() {
        return getProductionPlace();
    }

    public double getHeightMeters() {
        return heightMeters;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s) by %s at %s - %.1fm",
                getName(), style, type, architect, (getLocation() != null ? getLocation().getName() : "Unknown"), heightMeters);
    }
}
