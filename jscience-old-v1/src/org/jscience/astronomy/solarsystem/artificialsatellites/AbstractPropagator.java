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

import java.util.SortedSet;
import java.util.TreeSet;


/**
 * An abstract class providing implementations of methods common across all
 * implementations of the Propagator interface.
 */
public abstract class AbstractPropagator implements Propagator {
/**
     * Constructs an instance of this class.
     */
    protected AbstractPropagator() {
    }

    /**
     * Generates an ephemeris of OrbitalState instances between the
     * given start and end times. The first (in terms of its timestamp)
     * generated instance of OrbitalState always starts at the given start
     * time, while the last will always be at or before the given end time.
     *
     * @param TS Desired start of the ephemeris, in seconds since the epoch of
     *        the known orbital state.
     * @param TF Desired end of the ephemeris, in seconds since the epoch of
     *        the known orbital state.
     * @param DELT Desired time between the timestamps of the generated orbital
     *        states.
     *
     * @return A collection of zero or more instances of OrbitalState, sorted
     *         with respect to their timestamps.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public SortedSet generateEphemeris(double TS, double TF, double DELT) {
        // Verify the arguments are valid.
        if (TF <= TS) {
            throw new IllegalArgumentException("TF must be greater than TS");
        }

        if (DELT <= 0) {
            throw new IllegalArgumentException("DELT must be positive");
        }

        // Generate the ephemeris. Starting at the given start time, generate
        // states at times TS+n*DELT, where n is an integer 1,2,3,etc. This means
        // that a point will be generated at point TF iff there exists an 'n'
        // such that TF=TS+n*DELT.
        TreeSet ephemeris = new TreeSet();
        double TSINCE = TS;

        do {
            OrbitalState sv = propagate(TSINCE);
            sv.X = (sv.X * C1.XKMPER) / C1.AE;
            sv.Y = (sv.Y * C1.XKMPER) / C1.AE;
            sv.Z = (sv.Z * C1.XKMPER) / C1.AE;
            sv.XDOT = ((sv.XDOT * C1.XKMPER) / C1.AE * C1.XMNPDA) / 86400.0;
            sv.YDOT = ((sv.YDOT * C1.XKMPER) / C1.AE * C1.XMNPDA) / 86400.0;
            sv.ZDOT = ((sv.ZDOT * C1.XKMPER) / C1.AE * C1.XMNPDA) / 86400.0;
            ephemeris.add(sv);
            TSINCE = TSINCE + DELT;
        } while (TSINCE <= TF);

        // Give the generated ephemeris to the caller.
        return ephemeris;
    }
}
