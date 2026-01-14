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

// Potentiometer Wrapper Class
// Written by: Craig A. Lindley
// Last Update: 03/18/99
package org.jscience.awt.pots;


// This class extends Pot to provide for integer values other than 0..100
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntValuedPot extends Pot {
    // Private class data
    /** DOCUMENT ME! */
    private double potGranularity;

    /** DOCUMENT ME! */
    private int minValue;

/**
     * Class constructor
     *
     * @param maxValue maxValue is the maximum value the pot should return at
     *                 max rotation.
     * @param minValue minValue is the minimum value the pot should return at
     *                 min rotation.
     */
    public IntValuedPot(int maxValue, int minValue) {
        this.minValue = minValue;
        potGranularity = ((double) maxValue - minValue) / 100.0;
    }

    /**
     * Get the scaled value of the pot at its current position
     *
     * @return int scaled int value
     */
    public int getIntValue() {
        return (int) ((super.getValue() * potGranularity) + minValue);
    }

    /**
     * Set the current position of the pot to the scaled value
     *
     * @param realValue realValue is the scaled int value to set the pot to
     */
    public void setIntValue(int realValue) {
        super.setValue((int) ((realValue - minValue) / potGranularity));
    }
}
