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

package org.jscience.social.economics.resources;

import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.economics.Community;
import org.jscience.social.economics.PotentialResource;
import org.jscience.social.economics.Resource;
import org.jscience.social.economics.money.Money;
import org.jscience.natural.earth.Place;
import org.jscience.core.measure.Quantity;
import org.jscience.core.util.identity.Identification;

/**
 * Represents a tool used to build or repair other objects.
 * Tools are distinct from final consumption goods (like food) but similar to machines.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Tool extends PhysicalObject {

    private static final long serialVersionUID = 1L;

    private String purpose;
    private PotentialResource[] targets;
    private int acts;

    /**
     * Initializes a new tool.
     */
    public Tool(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
        this.purpose = null;
        this.targets = new PotentialResource[0];
        this.acts = 0;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public PotentialResource[] getTargets() {
        return targets;
    }

    public void setTargets(Resource[] targets) {
        this.targets = targets; // Note: PotentialResource is likely superclass
    }

    public int getNumActions() {
        return acts;
    }

    public void setNumActions(int acts) {
        this.acts = acts;
    }

    /**
     * Returns the human-readable name for a specific action index.
     */
    public abstract String getActionName(int i);

    /**
     * Executes the specific action on a set of target objects.
     */
    public abstract void act(int i, Object[] objects);
}

