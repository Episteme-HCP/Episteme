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
