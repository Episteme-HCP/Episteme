/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.economics.Market;
import org.jscience.sociology.Person;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a named geographic place.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Place implements org.jscience.geography.Locatable {

    public enum Type {
        CITY, TOWN, VILLAGE, NEIGHBORHOOD, LANDMARK,
        PARK, MOUNTAIN, RIVER, LAKE, OCEAN, ISLAND,
        BUILDING, MONUMENT, AIRPORT, STATION,
        COUNTRY, REGION, STATE, CONTINENT
    }

    private final String name;
    private final Type type;
    private Coordinate location;
    private String description;
    private String region;
    private String country;

    // V1 Features
    private final List<Person> inhabitants;
    private final List<Market> markets;

    public Place(String name, Type type) {
        this.name = name;
        this.type = type;
        this.inhabitants = new ArrayList<>();
        this.markets = new ArrayList<>();
    }

    public Place(String name, Type type, Coordinate location) {
        this(name, type);
        this.location = location;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Place getLocation() {
        return this;
    }

    public Coordinate getCoordinate() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public int getPopulation() {
        return inhabitants.size();
    }

    public List<Person> getInhabitants() {
        return Collections.unmodifiableList(inhabitants);
    }

    public void addInhabitant(Person p) {
        if (!inhabitants.contains(p)) {
            inhabitants.add(p);
        }
    }

    public void removeInhabitant(Person p) {
        inhabitants.remove(p);
    }

    public List<Market> getMarkets() {
        return Collections.unmodifiableList(markets);
    }

    public void addMarket(Market m) {
        markets.add(m);
    }

    // Setters
    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Calculates distance to another place.
     * 
     * @return distance as Quantity<Length>, or null if location unknown
     */
    public Quantity<Length> distanceTo(Place other) {
        if (location != null && other.location != null) {
            return location.distanceTo(other.location);
        }
        return null;
    }

    /**
     * Calculates distance to another place in meters.
     * 
     * @return distance in meters, or -1 if location unknown
     */
    public double distanceToMeters(Place other) {
        Quantity<Length> dist = distanceTo(other);
        if (dist != null) {
            return dist.to(Units.METER).getValue().doubleValue();
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, type);
    }

    // Notable places
    public static Place eiffelTower() {
        Place p = new Place("Eiffel Tower", Type.MONUMENT,
                new Coordinate(48.8584, 2.2945));
        p.setCountry("France");
        p.setDescription("Iron lattice tower on the Champ de Mars");
        return p;
    }

    public static Place mountEverest() {
        Place p = new Place("Mount Everest", Type.MOUNTAIN,
                new Coordinate(27.9881, 86.9250, 8848.86));
        p.setCountry("Nepal/China");
        p.setDescription("Highest mountain on Earth");
        return p;
    }
}
