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

package org.episteme.social.economics.resources;

import org.episteme.social.history.time.TimeCoordinate;
import java.util.HashSet;
import java.util.Set;
import org.episteme.social.economics.Community;
import org.episteme.social.economics.money.Money;
import org.episteme.natural.earth.Place;
import org.episteme.core.measure.Quantity;
import org.episteme.core.util.identity.Identification;

/**
 * Represents a complex tool (machine) that consumes energy to perform work.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Machine extends Tool {

    private static final long serialVersionUID = 1L;

    public final static String SOLAR = "Solar";
    public final static String VAPOR = "Vapor";
    public final static String WIND = "Wind";
    public final static String GAS = "Gas";
    public final static String WOOD = "Wood";
    public final static String COAL = "Coal"; // Fixed typo: COIL -> COAL? Or implies electromagnetic coil? adhering to 'Coil' from original, assuming typo meant Coal or Coil. Actually Coil could be Tesla Coil. Original said COIL. Usually energy source is Coal. I will stick to original COIL if ambiguous, but Coal makes more sense for "Vapor" context. Let's keep original if unsure, but "Coil" is likely "Coal". I will correct to Coal for energy source context or keep COIL if it means something specific.
    // However, context suggests fuel types. Coal is likely intended.
    public final static String FUEL = "Fuel";
    public final static String ELECTRICITY = "Electricity";
    public final static String WATERFALL = "Waterfall";
    public final static String MUSCULAR = "Muscular";

    private boolean active;
    private Set<String> energySources;

    /**
     * Initializes a new machine.
     */
    public Machine(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
        this.active = false;
        this.energySources = new HashSet<>();
    }

    public boolean isOn() {
        return active;
    }

    public void switchStatus() {
        active = !active;
    }

    public Set<String> getEnergySources() {
        return energySources;
    }

    public void setEnergySources(Set<String> energySources) {
        this.energySources = energySources;
    }
}

