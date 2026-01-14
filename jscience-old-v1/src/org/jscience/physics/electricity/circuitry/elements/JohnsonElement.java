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

// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class JohnsonElement extends ChipElement {
    /**
     * Creates a new JohnsonElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public JohnsonElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new JohnsonElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public JohnsonElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "Johnson counter";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean needsBits() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = (bits > 2) ? bits : 2;
        sizeY = 2;
        pins = new Pin[getPostCount()];
        pins[0] = new Pin(1, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new Pin(sizeX - 1, SIDE_S, "R");
        pins[1].bubble = true;

        int i;

        for (i = 0; i != bits; i++) {
            int ii = i + 2;
            pins[ii] = new Pin(i, SIDE_N, "Q" + i);
            pins[ii].output = pins[ii].state = true;
        }

        allocNodes();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return bits + 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return bits;
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
        int i;

        if (pins[0].value && !lastClock) {
            for (i = 0; i != bits; i++)
                if (pins[i + 2].value) {
                    break;
                }

            if (i < bits) {
                pins[i++ + 2].value = false;
            }

            i %= bits;
            pins[i + 2].value = true;
        }

        if (!pins[1].value) {
            for (i = 1; i != bits; i++)
                pins[i + 2].value = false;

            pins[2].value = true;
        }

        lastClock = pins[0].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 163;
    }
}
