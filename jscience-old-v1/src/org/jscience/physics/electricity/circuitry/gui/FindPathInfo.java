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
package org.jscience.physics.electricity.circuitry.gui;

import org.jscience.physics.electricity.circuitry.CircuitElement;
import org.jscience.physics.electricity.circuitry.elements.CapacitorElement;
import org.jscience.physics.electricity.circuitry.elements.CurrentElement;
import org.jscience.physics.electricity.circuitry.elements.InductorElement;
import org.jscience.physics.electricity.circuitry.elements.VoltageElement;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class FindPathInfo {
    /**
     * DOCUMENT ME!
     */
    public static final int FPI_INDUCT = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int FPI_VOLTAGE = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int FPI_SHORT = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int FPI_CAP_V = 4;

    /**
     * DOCUMENT ME!
     */
    public boolean[] used;

    /**
     * DOCUMENT ME!
     */
    public int dest;

    /**
     * DOCUMENT ME!
     */
    public CircuitElement firstElement;

    /**
     * DOCUMENT ME!
     */
    public int type;

    public CircuitFrame circuitFrame;

    /**
     * Creates a new FindPathInfo object.
     *
     * @param t DOCUMENT ME!
     * @param e DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    public FindPathInfo(int t, CircuitElement e, int d, CircuitFrame circuitFrame) {
        dest = d;
        type = t;
        firstElement = e;
        used = new boolean[circuitFrame.nodeList.size()];
        this.circuitFrame = circuitFrame;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean findPath(int n1) {
        if (n1 == dest) {
            return true;
        }

        if (used[n1]) {
            //System.out.println("used " + n1);
            return false;
        }

        used[n1] = true;

        int i;

        for (i = 0; i != circuitFrame.elmList.size(); i++) {
            CircuitElement ce = circuitFrame.getElement(i);

            if (ce == firstElement) {
                continue;
            }

            if (type == FPI_INDUCT) {
                if (ce instanceof CurrentElement) {
                    continue;
                }
            }

            if (type == FPI_VOLTAGE) {
                if (!(ce.isWire() || ce instanceof VoltageElement)) {
                    continue;
                }
            }

            if ((type == FPI_SHORT) && !ce.isWire()) {
                continue;
            }

            if (type == FPI_CAP_V) {
                if (!(ce.isWire() || ce instanceof CapacitorElement ||
                        ce instanceof VoltageElement)) {
                    continue;
                }
            }

            if (n1 == 0) {
                // look for posts which have a ground connection;
                // our path can go through ground
                int j;

                for (j = 0; j != ce.getPostCount(); j++)
                    if (ce.hasGroundConnection(j) && findPath(ce.getNode(j))) {
                        used[n1] = false;

                        return true;
                    }
            }

            int j;

            for (j = 0; j != ce.getPostCount(); j++) {
                //System.out.println(ce + " " + ce.getNode(j));
                if (ce.getNode(j) == n1) {
                    break;
                }
            }

            if (j == ce.getPostCount()) {
                continue;
            }

            if (ce.hasGroundConnection(j) && findPath(0)) {
                //System.out.println(ce + " has ground");
                used[n1] = false;

                return true;
            }

            if ((type == FPI_INDUCT) && ce instanceof InductorElement) {
                double c = ce.getCurrent();

                if (j == 0) {
                    c = -c;
                }

                //System.out.println("matching " + c + " to " + firstElement.getCurrent());
                //System.out.println(ce + " " + firstElement);
                if (Math.abs(c - firstElement.getCurrent()) > 1e-10) {
                    continue;
                }
            }

            int k;

            for (k = 0; k != ce.getPostCount(); k++) {
                if (j == k) {
                    continue;
                }

                //System.out.println(ce + " " + ce.getNode(j) + "-" + ce.getNode(k));
                if (ce.getConnection(j, k) && findPath(ce.getNode(k))) {
                    //System.out.println("got findpath " + n1);
                    used[n1] = false;

                    return true;
                }

                //System.out.println("back on findpath " + n1);
            }
        }

        used[n1] = false;

        //System.out.println(n1 + " failed");
        return false;
    }
}
