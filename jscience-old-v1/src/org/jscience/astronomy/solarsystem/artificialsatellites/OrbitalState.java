/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
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
