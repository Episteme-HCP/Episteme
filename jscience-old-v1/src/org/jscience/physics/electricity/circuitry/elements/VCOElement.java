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

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class VCOElement extends ChipElement {
    /**
     * DOCUMENT ME!
     */
    public final double cResistance = 1e6;

    /**
     * DOCUMENT ME!
     */
    public double cCurrent;

    /**
     * DOCUMENT ME!
     */
    public int cDir;

    /**
     * Creates a new VCOElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public VCOElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new VCOElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public VCOElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "VCO";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 2;
        sizeY = 4;
        pins = new Pin[6];
        pins[0] = new Pin(0, SIDE_W, "Vi");
        pins[1] = new Pin(3, SIDE_W, "Vo");
        pins[1].output = true;
        pins[2] = new Pin(0, SIDE_E, "C");
        pins[3] = new Pin(1, SIDE_E, "C");
        pins[4] = new Pin(2, SIDE_E, "R1");
        pins[4].output = true;
        pins[5] = new Pin(3, SIDE_E, "R2");
        pins[5].output = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nonLinear() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        // output pin
        circuitFrame.stampVoltageSource(0, nodes[1], pins[1].voltSource);
        // attach Vi to R1 pin so its current is proportional to Vi
        circuitFrame.stampVoltageSource(nodes[0], nodes[4], pins[4].voltSource, 0);
        // attach 5V to R2 pin so we get a current going
        circuitFrame.stampVoltageSource(0, nodes[5], pins[5].voltSource, 5);
        // put resistor across cap pins to give current somewhere to go
        // in case cap is not connected
        circuitFrame.stampResistor(nodes[2], nodes[3], cResistance);
        circuitFrame.stampNonLinear(nodes[2]);
        circuitFrame.stampNonLinear(nodes[3]);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        double vc = volts[3] - volts[2];
        double vo = volts[1];
        int dir = (vo < 2.5) ? 1 : (-1);

        // switch direction of current through cap as we oscillate
        if ((vo < 2.5) && (vc > 4.5)) {
            vo = 5;
            dir = -1;
        }

        if ((vo > 2.5) && (vc < .5)) {
            vo = 0;
            dir = 1;
        }

        // generate output voltage
        circuitFrame.updateVoltageSource(0, nodes[1], pins[1].voltSource, vo);

        // now we set the current through the cap to be equal to the
        // current through R1 and R2, so we can measure the voltage
        // across the cap
        int cur1 = circuitFrame.nodeList.size() + pins[4].voltSource;
        int cur2 = circuitFrame.nodeList.size() + pins[5].voltSource;
        circuitFrame.stampMatrix(nodes[2], cur1, dir);
        circuitFrame.stampMatrix(nodes[2], cur2, dir);
        circuitFrame.stampMatrix(nodes[3], cur1, -dir);
        circuitFrame.stampMatrix(nodes[3], cur2, -dir);
        cDir = dir;
    }

    // can't do this in calculateCurrent() because it's called before
    /**
     * DOCUMENT ME!
     */
    public void computeCurrent() {
        double c = (cDir * (pins[4].current + pins[5].current)) +
            ((volts[3] - volts[2]) / cResistance);
        pins[2].current = -c;
        pins[3].current = c;
        pins[0].current = -pins[4].current;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        computeCurrent();
        drawChip(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 6;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 158;
    }
}
