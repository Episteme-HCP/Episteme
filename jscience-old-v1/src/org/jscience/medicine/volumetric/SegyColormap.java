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

package org.jscience.medicine.volumetric;


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SegyColormap extends Colormap {
    /** DOCUMENT ME! */
    IntAttr minAlphaAttr;

    /** DOCUMENT ME! */
    IntAttr maxAlphaAttr;

    /** DOCUMENT ME! */
    int minAlpha;

    /** DOCUMENT ME! */
    int maxAlpha;

/**
     * Creates a new SegyColormap object.
     *
     * @param minAlphaAttr DOCUMENT ME!
     * @param maxAlphaAttr DOCUMENT ME!
     */
    SegyColormap(IntAttr minAlphaAttr, IntAttr maxAlphaAttr) {
        this.minAlphaAttr = minAlphaAttr;
        this.maxAlphaAttr = maxAlphaAttr;
        minAlpha = maxAlphaAttr.getValue();
        maxAlpha = maxAlphaAttr.getValue();
        updateMapping();
    }

    /**
     * DOCUMENT ME!
     */
    void updateMapping() {
        // set up mappings (from C code by Prem Domingo)
        for (int i = 0; i < 256; i++) {
            double fraction = i / 256.0;
            double value;
            int redMapping;
            int greenMapping;
            int blueMapping;
            int alphaMapping;
            value = 255.0 * Math.abs(Math.sin((fraction * Math.PI) / 2.0));
            redMapping = (int) value;
            value = 200.0 * Math.abs(Math.sin(fraction * Math.PI));

            if (value > 175.0) {
                value = 175.0;
            }

            greenMapping = (int) value;
            value = 255.0 * Math.abs(Math.cos((fraction * Math.PI) / 2.0));

            if (i == 0) {
                value = 0;
            }

            blueMapping = (int) value;

            if (i == 0) {
                value = 0;
            } else if (i < minAlpha) {
                value = 255 * 0.9;
            } else if (i < maxAlpha) {
                value = 1; // just a touch
            } else {
                value = 255 * 0.9;
            }

            alphaMapping = (int) value;
            colorMapping[i] = ((alphaMapping & 0xFF) << 24) |
                ((redMapping & 0xFF) << 16) | ((greenMapping & 0xFF) << 8) |
                (blueMapping & 0xFF);
        }

        editId++;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int update() {
        int newMinAlpha;
        int newMaxAlpha;
        newMinAlpha = minAlphaAttr.getValue();
        newMaxAlpha = maxAlphaAttr.getValue();

        if ((minAlpha != newMinAlpha) || (maxAlpha != newMaxAlpha)) {
            minAlpha = newMinAlpha;
            maxAlpha = newMaxAlpha;
            updateMapping();
        }

        return editId;
    }
}
