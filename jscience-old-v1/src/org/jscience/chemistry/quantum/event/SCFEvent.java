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

/*
 * SCFEvent.java
 *
 * Created on August 9, 2004, 10:19 PM
 */
package org.jscience.chemistry.quantum.event;

/**
 * Represents an event in sn SCF cycle
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class SCFEvent extends java.util.EventObject {
    /** Informative message about SCF, like intermediate energy etc. */
    public static final int INFO_EVENT = 1;

    /** SCF Convergence event */
    public static final int CONVERGED_EVENT = 2;

    /** Failed SCF convergence event */
    public static final int FAILED_CONVERGENCE_EVENT = 2;

    /** Holds value of property currentIteration. */
    private int currentIteration;

    /** Holds value of property currentEnergy. */
    private double currentEnergy;

    /** Holds value of property type. */
    private int type;

/**
     * Creates a new instance of SCFEvent
     *
     * @param source DOCUMENT ME!
     */
    public SCFEvent(Object source) {
        super(source);

        currentEnergy = 0.0;
        currentIteration = 0;

        type = INFO_EVENT;
    }

    /**
     * Getter for property currentIteration.
     *
     * @return Value of property currentIteration.
     */
    public int getCurrentIteration() {
        return this.currentIteration;
    }

    /**
     * Setter for property currentIteration.
     *
     * @param currentIteration New value of property currentIteration.
     */
    public void setCurrentIteration(int currentIteration) {
        this.currentIteration = currentIteration;
    }

    /**
     * Getter for property currentEnergy.
     *
     * @return Value of property currentEnergy.
     */
    public double getCurrentEnergy() {
        return this.currentEnergy;
    }

    /**
     * Setter for property currentEnergy.
     *
     * @param currentEnergy New value of property currentEnergy.
     */
    public void setCurrentEnergy(double currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }

    /**
     * Setter for property type.
     *
     * @param type New value of property type.
     */
    public void setType(int type) {
        this.type = type;
    }
} // end of class SCFEvent
