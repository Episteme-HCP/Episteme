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

package org.jscience.social.architecture.traffic;

import org.jscience.social.history.time.TimePoint;
import java.util.UUID;
import org.jscience.natural.earth.coordinates.GeodeticCoordinate;
import org.jscience.social.economics.money.Money;
import org.jscience.social.economics.resources.Machine;
import org.jscience.natural.earth.Place;
import org.jscience.natural.earth.PlaceType;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents a generic vehicle entity within a macro or microscopic traffic system. 
 * It tracks physical state including geographical position, current velocity, 
 * and network location (current road).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Vehicle extends Machine {

    private static final long serialVersionUID = 2L;

    @Attribute
    private GeodeticCoordinate geodeticPosition;
    @Attribute
    private Real speed; // Speed in meters per second (m/s)
    @Attribute
    private Road currentRoad;

    /**
     * Initializes a new vehicle with a unique identity.
     * 
     * @param name descriptive label for the vehicle (e.g., "Car-A1")
     */
    public Vehicle(String name) {
        super(name, "", Quantities.create(1, Units.ONE), null, null, TimePoint.now(), 
              new UUIDIdentification(UUID.randomUUID().toString()), Money.usd(0));
        this.speed = Real.ZERO;
    }

    @Override
    public Place getPosition() {
        return super.getPosition();
    }

    public GeodeticCoordinate getGeodeticPosition() {
        return geodeticPosition;
    }

    public void setPosition(GeodeticCoordinate position) {
        this.geodeticPosition = position;
        Place p = getPosition();
        if (p == null) {
            p = new Place(getName(), position, PlaceType.OTHER);
            super.setPosition(p);
        } else {
            p.setCenter(position);
        }
    }

    @Override
    public String getActionName(int i) {
        return "Move";
    }

    @Override
    public void act(int i, Object[] objects) {
        if (i == 0 && objects.length > 0 && objects[0] instanceof Number) {
            move(((Number) objects[0]).doubleValue());
        }
    }

    /** @return current velocity in meters per second */
    public Real getSpeed() {
        return speed;
    }

    /** @param speed new velocity as a Real value in m/s */
    public void setSpeed(Real speed) {
        this.speed = (speed != null) ? speed : Real.ZERO;
    }

    /** @param speed new velocity as a double in m/s */
    public void setSpeed(double speed) {
        this.speed = Real.of(speed);
    }

    /** @return the road segment the vehicle is currently traversing */
    public Road getCurrentRoad() {
        return currentRoad;
    }

    /**
     * Assigns the vehicle to a specific road and initializes its position 
     * to the start of the road segment.
     * 
     * @param road the road to enter
     */
    public void enterRoad(Road road) {
        this.currentRoad = road;
        if (road != null && !road.getPoints().isEmpty()) {
            this.setPosition(road.getStart());
        }
    }

    /**
     * Simulates the spatial progress of the vehicle along its current road 
     * over a time interval.
     * 
     * @param seconds time delta in seconds
     */
    public void move(double seconds) {
        if (currentRoad != null && speed.doubleValue() > 0 && seconds > 0) {
            // Integration logic for path progression to be implemented 
        }
    }
}

