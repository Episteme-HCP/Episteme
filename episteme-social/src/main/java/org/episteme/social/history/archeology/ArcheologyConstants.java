/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.archeology;

import org.episteme.social.history.HistoryConstants;
import org.episteme.social.history.HistoricalPeriod;
import org.episteme.core.mathematics.algebra.Interval;
import org.episteme.core.mathematics.algebra.intervals.RealInterval;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Units;


/**
 * Constants useful for archaeology and geological time scales.
 * All intervals are expressed in seconds relative to the Unix Epoch (1970-01-01T00:00:00Z).

 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 * @see <a href="http://en.wikipedia.org/wiki/Geologic_timescale">Geologic Timescale (Wikipedia)</a>
 */
public final class ArcheologyConstants {

    /** Historical period category identifiers. */
    public static final HistoricalPeriod ANTIQUE_PERIOD = HistoricalPeriod.ANCIENT_HISTORY;
    public static final HistoricalPeriod MEDIEVAL_PERIOD = HistoricalPeriod.POST_CLASSICAL;
    public static final HistoricalPeriod MODERN_PERIOD = HistoricalPeriod.LATE_MODERN;
    public static final HistoricalPeriod CONTEMPORARY_PERIOD = HistoricalPeriod.CONTEMPORARY;

    @Deprecated public static final int ANTIQUE = 1;
    @Deprecated public static final int MEDIEVAL = 2;
    @Deprecated public static final int MODERN = 3;
    @Deprecated public static final int CONTEMPORARY = 4;

    private static final double YEAR = HistoryConstants.JULIAN_YEAR.to(Units.SECOND).getValue().doubleValue();


    /** Holocene Epoch interval (~10,000 years ago to present). */
    public static final Interval<Real> HOLOCENE = RealInterval.closed(-1.0e4 * YEAR, 0.0);

    /** Pleistocene Epoch interval (~1.6M years ago to ~10,000 years ago). */
    public static final Interval<Real> PLEISTOCENE = RealInterval.closed(-1.6e6 * YEAR, -1.0e4 * YEAR);

    /** Pliocene Epoch interval (~5M years ago to ~1.6M years ago). */
    public static final Interval<Real> PLIOCENE = RealInterval.closed(-5.0e6 * YEAR, -1.6e6 * YEAR);

    /** Miocene Epoch interval (~23M years ago to ~5M years ago). */
    public static final Interval<Real> MIOCENE = RealInterval.closed(-2.3e7 * YEAR, -5.0e6 * YEAR);

    /** Oligocene Epoch interval (~38M years ago to ~23M years ago). */
    public static final Interval<Real> OLIGOCENE = RealInterval.closed(-3.8e7 * YEAR, -2.3e7 * YEAR);

    /** Eocene Epoch interval (~55M years ago to ~38M years ago). */
    public static final Interval<Real> EOCENE = RealInterval.closed(-5.5e7 * YEAR, -3.8e7 * YEAR);

    /** Paleocene Epoch interval (~64.3M years ago to ~55M years ago). */
    public static final Interval<Real> PALEOCENE = RealInterval.closed(-6.43e7 * YEAR, -5.5e7 * YEAR);

    /** Cretaceous Period interval (~146M years ago to ~64.3M years ago). */
    public static final Interval<Real> CRETACEOUS = RealInterval.closed(-1.46e8 * YEAR, -6.43e7 * YEAR);

    /** Jurassic Period interval (~208M years ago to ~146M years ago). */
    public static final Interval<Real> JURASSIC = RealInterval.closed(-2.08e8 * YEAR, -1.46e8 * YEAR);

    /** Triassic Period interval (~251.1M years ago to ~208M years ago). */
    public static final Interval<Real> TRIASSIC = RealInterval.closed(-2.511e8 * YEAR, -2.08e8 * YEAR);

    /** Permian Period interval (~325M years ago to ~251.1M years ago). */
    public static final Interval<Real> PERMIAN = RealInterval.closed(-3.25e8 * YEAR, -2.511e8 * YEAR);

    /** Carboniferous Period interval (~360M years ago to ~325M years ago). */
    public static final Interval<Real> CARBONIFEROUS = RealInterval.closed(-3.6e8 * YEAR, -3.25e8 * YEAR);

    /** Devonian Period interval (~408.5M years ago to ~360M years ago). */
    public static final Interval<Real> DEVONIAN = RealInterval.closed(-4.085e8 * YEAR, -3.6e8 * YEAR);

    /** Silurian Period interval (~443.5M years ago to ~408.5M years ago). */
    public static final Interval<Real> SILURIAN = RealInterval.closed(-4.435e8 * YEAR, -4.085e8 * YEAR);

    /** Ordovician Period interval (~490M years ago to ~443.5M years ago). */
    public static final Interval<Real> ORDOVICIAN = RealInterval.closed(-4.9e8 * YEAR, -4.435e8 * YEAR);

    /** Cambrian Period interval (~545M years ago to ~490M years ago). */
    public static final Interval<Real> CAMBRIAN = RealInterval.closed(-5.45e8 * YEAR, -4.9e8 * YEAR);

    /** Neoproterozoic Era interval (~900M years ago to ~545M years ago). */
    public static final Interval<Real> NEOPROTEROZOIC = RealInterval.closed(-9.0e8 * YEAR, -5.45e8 * YEAR);

    /** Mesoproterozoic Era interval (~1.6B years ago to ~900M years ago). */
    public static final Interval<Real> MESOPROTEROZOIC = RealInterval.closed(-1.6e9 * YEAR, -9.0e8 * YEAR);

    /** Paleoproterozoic Era interval (~2.5B years ago to ~1.6B years ago). */
    public static final Interval<Real> PALEOPROTEROZOIC = RealInterval.closed(-2.5e9 * YEAR, -1.6e9 * YEAR);

    /** Archean Eon interval (~3.8B years ago to ~2.5B years ago). */
    public static final Interval<Real> ARCHEAN = RealInterval.closed(-3.8e9 * YEAR, -2.5e9 * YEAR);

    /** Hadean Eon interval (~4.1B years ago to ~3.8B years ago). */
    public static final Interval<Real> HADEAN = RealInterval.closed(-4.1e9 * YEAR, -3.8e9 * YEAR);

    private ArcheologyConstants() {
        // Utility class
    }
}

