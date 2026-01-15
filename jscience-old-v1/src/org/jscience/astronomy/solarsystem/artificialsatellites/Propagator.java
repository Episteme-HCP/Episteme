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

/**
 * Instances of classes which implement this interface propagate orbital state
 * from an epoch at which the orbital state is known to a desired delta since
 * the epoch.
 */
public interface Propagator {

    /**
     * Propagates orbital state from the epoch at which the orbital state is known
     * to the given desired delta since the epoch.<p>
     * <p/>
     * Implementations must set the time on the generated OrbitalState.
     *
     * @param tsince Time at which the orbital states is desired, in seconds since
     *               the epoch at which the orbital state is known.
     * @return A non-null instance of OrbitalState representing the position and
     *         velocity at timestamp=epoch+tsince.
     */
    public OrbitalState propagate(double tsince);

    /**
     * Generates an ephemeris of OrbitalState instances between the given start
     * and end times. The first (in terms of its timestamp) generated instance
     * of OrbitalState always starts at the given start time, while the last will
     * always be at or before the given end time.
     *
     * @param TS   Desired start of the ephemeris, in seconds since the epoch of
     *             the known orbital state.
     * @param TF   Desired end of the ephemeris, in seconds since the epoch of
     *             the known orbital state.
     * @param DELT Desired time between the timestamps of the generated orbital
     *             states.
     * @return A collection of zero or more instances of OrbitalState, sorted with
     *         respect to their timestamps.
     */
    public SortedSet generateEphemeris(double TS,
                                       double TF,
                                       double DELT);

    /**
     * @return Name of the propagator.
     */
    public String getName();

    /**
     * @return True if the propagator is a deep-space propagator, false if it
     *         isn't (i.e., if it is a near-Earth propagator).
     */
    public boolean isDeep();

}
