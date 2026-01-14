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

package org.jscience.astronomy.solarsystem.artificialsatellites;

import java.text.DecimalFormat;


/**
 * Represents orbital state of the spacecraft at a point in time.
 */
public final class OrbitalState implements Comparable {
    /**
     * Timestamp of the orbital state, in seconds since the epoch of
     * the known orbital state.
     */
    public double TSINCE = 0.0;

    /** Position of the spacecraft at the point in time. */
    public double X = 0.0;

    /**
     * DOCUMENT ME!
     */
    public double Y = 0.0;

    /**
     * DOCUMENT ME!
     */
    public double Z = 0.0;

    /** Velocity of the spacecraft at the point in time. */
    public double XDOT = 0.0;

    /**
     * DOCUMENT ME!
     */
    public double YDOT = 0.0;

    /**
     * DOCUMENT ME!
     */
    public double ZDOT = 0.0;

/**
     * Constructs an instance of this class.
     */
    public OrbitalState() {
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return the String representation of this instance.
     */
    public String toString() {
        StringBuffer b = new StringBuffer(format(TSINCE));
        b.append(format(X));
        b.append(format(Y));
        b.append(format(Z));
        b.append(format(XDOT));
        b.append(format(YDOT));
        b.append(format(ZDOT));

        return b.toString();
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return the double value formatted for use by toString().
     */
    private static String format(double d) {
        final DecimalFormat f = new DecimalFormat("0.00000000");
        StringBuffer s = new StringBuffer(f.format(d));

        while (s.length() < 17) {
            s = s.insert(0, " ");
        }

        return s.toString();
    }

    /**
     * Compares this instance of the given one.
     *
     * @param obj DOCUMENT ME!
     *
     * @return -1, 0, +1 if this instance is less than, equal, or greater than
     *         the given instance.
     */
    public int compareTo(Object obj) {
        if (!(obj instanceof OrbitalState)) {
            return -1;
        }

        OrbitalState that = (OrbitalState) obj;

        return ((this.TSINCE < that.TSINCE) ? (-1)
                                            : ((this.TSINCE > that.TSINCE) ? 1 : 0));
    }
}
