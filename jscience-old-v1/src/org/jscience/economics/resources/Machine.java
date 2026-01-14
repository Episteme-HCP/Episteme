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

package org.jscience.economics.resources;

import org.jscience.economics.Community;
import org.jscience.economics.money.Money;

import org.jscience.geography.Place;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * A class representing Machine.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an object with moving parts that usually makes some awful noise (motors)
//uses some energy source and emits some pollution whether heat, gaz just to mention a few
//produces some work, understand a repeated behavior
//also modify Objects as any tool
//actual wiring and energy supply is left to users
public abstract class Machine extends Tool {
    /** DOCUMENT ME! */
    public final static String SOLAR = "Solar";

    /** DOCUMENT ME! */
    public final static String VAPOR = "Vapor"; //nuclear, geothermic

    /** DOCUMENT ME! */
    public final static String WIND = "Wind";

    /** DOCUMENT ME! */
    public final static String GAZ = "Gaz";

    /** DOCUMENT ME! */
    public final static String WOOD = "Wood";

    /** DOCUMENT ME! */
    public final static String COIL = "Coil";

    /** DOCUMENT ME! */
    public final static String FUEL = "Fuel";

    /** DOCUMENT ME! */
    public final static String ELECTRICITY = "Electricity"; //battery

    /** DOCUMENT ME! */
    public final static String WATERFALL = "Waterfall"; //dams

    /** DOCUMENT ME! */
    public final static String MUSCULAR = "Muscular";

    /** DOCUMENT ME! */
    private boolean active; //is on or off, defaults to false

    //some machine have more than on and off state but all machines have at least two states
    /** DOCUMENT ME! */
    private Set energySources; //the listed recognized energy sources for this machine

/**
     * Creates a new Machine object.
     *
     * @param name            DOCUMENT ME!
     * @param description     DOCUMENT ME!
     * @param amount          DOCUMENT ME!
     * @param producer        DOCUMENT ME!
     * @param productionPlace DOCUMENT ME!
     * @param productionDate  DOCUMENT ME!
     * @param identification  DOCUMENT ME!
     * @param value           DOCUMENT ME!
     */
    public Machine(String name, String description, Amount amount,
        Community producer, Place productionPlace, Date productionDate,
        Identification identification, Amount<Money> value) {
        super(name, description, amount, producer, productionPlace,
            productionDate, identification, value);
        this.active = false;
        this.energySources = new HashSet();
    }

    //a broken machine may not produce its behavior even if activated if it is boken
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOn() {
        return active;
    }

    //stops or launches the active behavior (if the machine is not broken), changes the status
    /**
     * DOCUMENT ME!
     */
    public void switchStatus() {
        active = !active;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getEnergySources() {
        return energySources;
    }

    //should be a set of strings
    /**
     * DOCUMENT ME!
     *
     * @param energySources DOCUMENT ME!
     */
    public void setEnergySources(Set energySources) {
        this.energySources = energySources;
    }
}
