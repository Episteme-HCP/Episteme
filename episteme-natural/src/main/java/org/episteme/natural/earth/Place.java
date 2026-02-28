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

package org.episteme.natural.earth;

import org.episteme.natural.earth.coordinates.EarthCoordinate;
import org.episteme.natural.earth.coordinates.GeodeticCoordinate;
import org.episteme.core.util.Temporal;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Length;
import org.episteme.core.util.Positioned;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.mathematics.geometry.boundaries.Boundary;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class for all geographical features and named locations.
 * Uses geodetic coordinates for absolute positioning on Earth.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 * Modernized to use extensible PlaceType and PlacePrecision.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Place implements ComprehensiveIdentification, Positioned<EarthCoordinate>, Temporal<Temporal<?>> {

    private static final long serialVersionUID = 6L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private GeodeticCoordinate center;

    @Attribute
    private PlaceType type;

    @Attribute
    private String historicalName;

    @Attribute
    private PlacePrecision precision = PlacePrecision.UNKNOWN;

    @Attribute
    private Quantity<Length> uncertaintyRadius;

    @Attribute
    private String region;

    @Attribute
    private final List<Boundary<EarthCoordinate>> boundaries = new ArrayList<>();

    private final java.util.Set<org.episteme.natural.biology.Individual> inhabitants = new java.util.HashSet<>();

    @Attribute
    private Temporal<?> existenceTime;

    public Place(String name) {
        this(name, PlaceType.UNKNOWN);
    }

    public Place(String name, PlaceType type) {
        this.id = new UUIDIdentification(UUID.randomUUID().toString());
        setName(Objects.requireNonNull(name));
        this.type = type != null ? type : PlaceType.UNKNOWN;
        this.uncertaintyRadius = Quantities.create(0.0, Units.METER);
    }

    public Place(String name, GeodeticCoordinate center, PlaceType type) {
        this(name, type);
        this.center = center;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public GeodeticCoordinate getCenter() {
        return center;
    }

    public void setCenter(GeodeticCoordinate center) {
        this.center = center;
    }

    @Override
    public EarthCoordinate getPosition() {
        return center;
    }

    public String getHistoricalName() {
        return historicalName;
    }

    public void setHistoricalName(String historicalName) {
        this.historicalName = historicalName;
    }

    public PlacePrecision getPrecision() {
        return precision;
    }

    public void setPrecision(PlacePrecision precision) {
        this.precision = precision;
    }

    public Quantity<Length> getUncertaintyRadius() {
        return uncertaintyRadius;
    }

    public void setUncertaintyRadius(Quantity<Length> radius) {
        this.uncertaintyRadius = radius;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Returns the boundaries of this place.
     * Enclaves and exclaves should be modeled using CompositeBoundary2D if supported.
     */
    public List<Boundary<EarthCoordinate>> getBoundaries() {
        return Collections.unmodifiableList(boundaries);
    }

    public void addBoundary(Boundary<EarthCoordinate> boundary) {
        if (boundary != null) {
            boundaries.add(boundary);
        }
    }
    
    public void clearBoundaries() {
        boundaries.clear();
    }

    @Override
    public Temporal<?> getWhen() {
        return existenceTime;
    }

    public void setExistenceTime(Temporal<?> existenceTime) {
        this.existenceTime = existenceTime;
    }

    public void addInhabitant(org.episteme.natural.biology.Individual individual) {
        if (individual != null) inhabitants.add(individual);
    }

    public void removeInhabitant(org.episteme.natural.biology.Individual individual) {
        inhabitants.remove(individual);
    }

    public java.util.Set<org.episteme.natural.biology.Individual> getInhabitants() {
        return java.util.Collections.unmodifiableSet(inhabitants);
    }

    public Quantity<Length> distanceTo(Place other) {
        if (center == null || other.center == null) return null;
        return center.distanceTo(other.center);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place place)) return false;
        return Objects.equals(id, place.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return center != null ? String.format("%s (%s) [%s]", getName(), type, center) : String.format("%s (%s)", getName(), type);
    }

    // Factory methods
    public static Place of(String name, double lat, double lon, PlaceType type) {
        return new Place(name, new GeodeticCoordinate(lat, lon), type);
    }

    public static Place greenwich() {
        return of("Greenwich Observatory", 51.4772, 0.0, PlaceType.BUILDING);
    }
}


