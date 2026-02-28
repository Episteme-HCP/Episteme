/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.archeology;

import org.episteme.core.mathematics.geometry.boundaries.Boundary;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.episteme.natural.earth.Place;
import org.episteme.natural.earth.PlaceType;
import org.episteme.natural.earth.coordinates.EarthCoordinate;
import org.episteme.social.history.time.TimeCoordinate;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents an archaeological field site.
 * A Site is a physical location in which evidence of past activity is preserved and excavation occurs.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Site extends Place {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate excavationStartDate;

    @Attribute
    private String description;

    @Attribute
    private Boundary<EarthCoordinate> limits;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Item> discoveredItems = new HashSet<>();

    public Site(String name, TimeCoordinate excavationStartDate, Boundary<EarthCoordinate> limits) {
        super(name, PlaceType.LANDMARK);
        this.limits = limits;
        this.excavationStartDate = Objects.requireNonNull(excavationStartDate, "Start date cannot be null");
    }

    public TimeCoordinate getExcavationStartDate() {
        return excavationStartDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boundary<EarthCoordinate> getLimits() {
        return limits;
    }

    public void setLimits(Boundary<EarthCoordinate> limits) {
        this.limits = limits;
    }

    public Set<Item> getDiscoveredItems() {
        return Collections.unmodifiableSet(discoveredItems);
    }

    public void addItem(Item item) {
        if (item != null) discoveredItems.add(item);
    }

    public void removeItem(Item item) {
        discoveredItems.remove(item);
    }

    @Override
    public String toString() {
        return "Archeological Site: " + getName() + " (" + discoveredItems.size() + " items found)";
    }

    /**
     * Inner class representing a discovered item.
     * Can be moved to its own file later if it grows complex.
     */
    @Persistent
    public static class Item {
        @Attribute
        private String id;
        @Attribute
        private String type;
        @Attribute
        private String material;

        public Item(String type, String material) {
            this.id = UUID.randomUUID().toString();
            this.type = type;
            this.material = material;
        }
        
        // Getters omitted for brevity in this simple struct
        public String getType() { return type; }
    }
}

