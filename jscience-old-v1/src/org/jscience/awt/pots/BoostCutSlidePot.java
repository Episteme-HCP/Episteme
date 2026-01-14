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

// Special Slide Pot Wrapper Class
// Written by: Craig A. Lindley
// Last Update: 03/18/99
package org.jscience.awt.pots;


// This class extends Pot to provide a specialized boost/cut pot for
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class BoostCutSlidePot extends SlidePot {
    // Private class data
    /** DOCUMENT ME! */
    private double minDBGain;

    /** DOCUMENT ME! */
    private double potGranularity;

/**
     * Class constructor
     *
     * @param length    length is the length in pixels of the slide pot
     * @param width     width is the width in pixels of the slide pot
     * @param caption   caption is the label to be associated with the pot
     * @param minDBGain minDBGain is the min value of gain in dB
     * @param maxDBGain maxDBGain is the max value of gain in dB NOTE min and
     *                  max values are usually the same only different in sign
     */
    public BoostCutSlidePot(int length, int width, String caption,
        int minDBGain, int maxDBGain) {
        super(length, width, caption, 50);

        this.minDBGain = minDBGain;

        potGranularity = ((double) maxDBGain - minDBGain) / POTRANGE;
    }

    /**
     * Return the value of the gain at the current pot setting
     *
     * @return double gain
     */
    public double getGain() {
        // Get the pots current value 0..100
        int potValue = getValue();

        double db = (potValue * potGranularity) + minDBGain;

        double gain = Math.pow(10, db / 20.0);

        if (gain >= 1.0) {
            return gain;
        } else {
            return -gain;
        }
    }
}
