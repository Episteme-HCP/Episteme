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
public class AntennaElement extends RailElement {
    /**
     * DOCUMENT ME!
     */
    public double fmphase;

    /**
     * Creates a new AntennaElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public AntennaElement(int xx, int yy) {
        super(xx, yy, WF_DC);
    }

    /**
     * Creates a new AntennaElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public AntennaElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        waveform = WF_DC;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "A " + x + " " + y + " " + x2 + " " + y2 + " " + flags;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampVoltageSource(0, nodes[0], voltSource);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        circuitFrame.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltage() {
        fmphase += (2 * circuitFrame.pi * (2200 + (Math.sin(2 * circuitFrame.pi * circuitFrame.t * 13) * 100)) * circuitFrame.timeStep);

        double fm = 3 * Math.sin(fmphase);

        return (Math.sin(2 * circuitFrame.pi * circuitFrame.t * 3000) * (1.3 + Math.sin(2 * circuitFrame.pi * circuitFrame.t * 12)) * 3) +
        (Math.sin(2 * circuitFrame.pi * circuitFrame.t * 2710) * (1.3 + Math.sin(2 * circuitFrame.pi * circuitFrame.t * 13)) * 3) +
        (Math.sin(2 * circuitFrame.pi * circuitFrame.t * 2433) * (1.3 + Math.sin(2 * circuitFrame.pi * circuitFrame.t * 14)) * 3) +
        fm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'A';
    }
}
