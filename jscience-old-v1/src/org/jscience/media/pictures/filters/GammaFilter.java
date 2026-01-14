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

package org.jscience.media.pictures.filters;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class GammaFilter extends TransferFilter {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5329733893740770899L;

    /** DOCUMENT ME! */
    private float rGamma;

    /** DOCUMENT ME! */
    private float gGamma;

    /** DOCUMENT ME! */
    private float bGamma;

/**
     * Creates a new GammaFilter object.
     */
    public GammaFilter() {
        this(1.0f);
    }

/**
     * Creates a new GammaFilter object.
     *
     * @param gamma DOCUMENT ME!
     */
    public GammaFilter(float gamma) {
        this(gamma, gamma, gamma);
    }

/**
     * Creates a new GammaFilter object.
     *
     * @param rGamma DOCUMENT ME!
     * @param gGamma DOCUMENT ME!
     * @param bGamma DOCUMENT ME!
     */
    public GammaFilter(float rGamma, float gGamma, float bGamma) {
        setGamma(rGamma, gGamma, bGamma);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rGamma DOCUMENT ME!
     * @param gGamma DOCUMENT ME!
     * @param bGamma DOCUMENT ME!
     */
    public void setGamma(float rGamma, float gGamma, float bGamma) {
        this.rGamma = rGamma;
        this.gGamma = gGamma;
        this.bGamma = bGamma;
        initialized = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gamma DOCUMENT ME!
     */
    public void setGamma(float gamma) {
        setGamma(gamma, gamma, gamma);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGamma() {
        return rGamma;
    }

    /**
     * DOCUMENT ME!
     */
    protected void initialize() {
        rTable = makeTable(rGamma);

        if (gGamma == rGamma) {
            gTable = rTable;
        } else {
            gTable = makeTable(gGamma);
        }

        if (bGamma == rGamma) {
            bTable = rTable;
        } else if (bGamma == gGamma) {
            bTable = gTable;
        } else {
            bTable = makeTable(bGamma);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param gamma DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int[] makeTable(float gamma) {
        int[] table = new int[256];

        for (int i = 0; i < 256; i++) {
            table[i] = transferFunction(i);

            int v = (int) ((255.0 * Math.pow(i / 255.0, 1.0 / gamma)) + 0.5);

            if (v > 255) {
                v = 255;
            }

            table[i] = v;
        }

        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Colors/Gamma...";
    }
}
