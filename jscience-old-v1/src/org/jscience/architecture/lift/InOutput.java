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

package org.jscience.architecture.lift;

/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a fundamental class that is used as {@link Passenger} to {@link
 * org.jscience.architecture.lift.ca.CA} Inputs (call panels and command
 * panels) and as {@link org.jscience.architecture.lift.ca.CA} to {@link
 * Passenger} Outputs (indicators). It supports both the two-button model (UP
 * and DOWN) and the destination-call model (a button for each floor).
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:51 $
 */
public class InOutput {
    /**
     * DOCUMENT ME!
     */
    private int F = -1;

    /**
     * DOCUMENT ME!
     */
    private int MF;

    /**
     * DOCUMENT ME!
     */
    private boolean[] Signals;

/**
     * Creates a new {@code InOutput} on the {@code Floor}th floor with support
     * up to {@code MaxFloor} floors.
     */
    public InOutput(int Floor, int MaxFloor) {
        F = Floor;
        MF = MaxFloor;
        Signals = new boolean[MF];

        for (int i = 0; i < MF; i++) {
            Signals[i] = false;
        }
    }

    /**
     * Returns the maximum floor of this {@code InOutput}
     *
     * @return DOCUMENT ME!
     */
    public int getMaxFloor() {
        return (MF);
    }

    /**
     * Returns the floor of this {@code InOutput}. In other words, this
     * {@code InOutput} is on the {@code getFloor()}th floor.
     *
     * @return DOCUMENT ME!
     */
    public int getFloor() {
        return (F);
    }

    /**
     * Returns true if and only if the {@code Level}th signal has been
     * activated in the past (via {@link InOutput#setSignal}) and had not been
     * deactiveted since.
     *
     * @param Level DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getSignal(int Level) {
        return (Signals[Level]);
    }

    /**
     * Gets the up attribute of the InOutput object
     *
     * @return The up value
     */
    public boolean getUp() {
        for (int i = F; i < MF; i++) {
            if (Signals[i]) {
                return (true);
            }
        }

        return (false);
    }

    /**
     * Gets the down attribute of the InOutput object
     *
     * @return The down value
     */
    public boolean getDown() {
        for (int i = F; i >= 0; i--) {
            if (Signals[i]) {
                return (true);
            }
        }

        return (false);
    }

    /**
     * Gets the signals attribute of the InOutput object
     *
     * @return The signals value
     */
    public boolean[] getSignals() {
        return (Signals);
    }

    /**
     * Sets the signal attribute of the InOutput object
     *
     * @param Level The new signal value
     * @param Value The new signal value
     */
    public void setSignal(int Level, boolean Value) {
        if (F != Level) {
            Signals[Level] = Value;
        }
    }
}
