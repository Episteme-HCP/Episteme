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
