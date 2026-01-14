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

package org.jscience.history.archeology;

import org.jscience.astronomy.AstronomyConstants;

import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.analysis.Interval;


/**
 * A class representing the archeology useful constants.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class ArcheologyConstants extends Object {
    /** DOCUMENT ME! */
    public final static int ANTIQUE = 1;

    /** DOCUMENT ME! */
    public final static int MEDIEVAL = 2;

    /** DOCUMENT ME! */
    public final static int MODERN = 3;

    /** DOCUMENT ME! */
    public final static int CONTEMPORARY = 4;

    //see http://en.wikipedia.org/wiki/Geologic_timescale
    //may be we would better define these using intervals
    //currently defines end of era or epoch
    /** DOCUMENT ME! */
    public final static Interval HOLOCENE = new Interval(new Double(
                -1.0e4 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(0));

    /** DOCUMENT ME! */
    public final static Interval PLEISTOCENE = new Interval(new Double(
                -1.6e6 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-1.0e4 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY)); //quaternary begins

    /** DOCUMENT ME! */
    public final static Interval PLIOCENE = new Interval(new Double(
                -5.0e6 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-1.6e6 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval MIOCENE = new Interval(new Double(
                -2.3e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-5.0e6 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval OLIGOCENE = new Interval(new Double(
                -3.8e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-2.3e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval EOCENE = new Interval(new Double(
                -5.5e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-3.8e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval PALEOCENE = new Interval(new Double(
                -6.43e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-5.5e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY)); //tertiary begins

    /** DOCUMENT ME! */
    public final static Interval CRETACEOUS = new Interval(new Double(
                -1.46e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-6.43e7 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval JURASSIC = new Interval(new Double(
                -2.08e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-1.46e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval TRIASSIC = new Interval(new Double(
                -2.511e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-2.08e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY)); //secondary begins

    /** DOCUMENT ME! */
    public final static Interval PERMIAN = new Interval(new Double(
                -3.25e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-2.511e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval CARBONIFEROUS = new Interval(new Double(
                -3.6e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-3.25e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval DEVONIAN = new Interval(new Double(
                -4.085e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-3.6e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval SILURIAN = new Interval(new Double(
                -4.435e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-4.085e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval ORDOVICIAN = new Interval(new Double(
                -4.9e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-4.435e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval CAMBRIAN = new Interval(new Double(
                -5.45e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-4.9e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY)); //primary begins

    /** DOCUMENT ME! */
    public final static Interval NEOPROTEROZOIC = new Interval(new Double(
                -9.0e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-5.45e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval MESOPROTEROZOIC = new Interval(new Double(
                -1.6e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-9.0e8 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval PALEOPROTEROZOIC = new Interval(new Double(
                -2.5e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-1.6e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval ARCHEAN = new Interval(new Double(
                -3.8e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-2.5e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    /** DOCUMENT ME! */
    public final static Interval HADEAN = new Interval(new Double(
                -4.1e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY),
            new Double(-3.8e9 * AstronomyConstants.JULIAN_YEAR * AstronomyConstants.EARTH_DAY));

    //4,100 million- Oldest known rock;
    //4,400 million- Oldest known mineral;
    //4,600- Formation of Earth
}
