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

package org.jscience.social.history.calendars;

import org.jscience.core.mathematics.numbers.rationals.Rational;

/**
 * Abstract base class for modified Hindu calendars using arbitrary-precision arithmetic.
 * This version uses Rational (arbitrary precision) for precise calculations without
 * floating-point errors.
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
abstract class ModifiedHinduBRCalendar {
    /** Sidereal year length in days. */
    public static final Rational SIDEREALYEAR;

    /** Sidereal month length in days. */
    public static final Rational SIDEREALMONTH = Rational.of(0x46de57L, 0xdc4fbeL).add(Rational.of(27L));

    /** Synodic month length in days. */
    public static final Rational SYNODICMONTH = Rational.of(0x6c269bL, 0xcbd4feL).add(Rational.of(29L));

    /** Days from creation to epoch. */
    public static final Rational CREATION;

    /** Anomalistic year length in days. */
    public static final Rational ANOMYEAR = Rational.of(0x16f633b4ba0L, 0x1017df67dL);

    /** Anomalistic month length in days. */
    public static final Rational ANOMMONTH = Rational.of(0x5e0d1d84L, 0x369cbf1L);

    /** Number of minutes in a circle (21600). */
    private static final Rational TWO1600;

    /** Constant for 1000. */
    private static final Rational THOUSAND = Rational.of(1000L);

    /** Constant for 20000. */
    private static final Rational TWENTYTHOUSAND = Rational.of(20000L);

    /** Mean motion of the sun. */
    private static final Rational MEANMOTION;

    /** Constant 14/360. */
    private static final Rational FOURTEENTHREESIXTY = Rational.of(14L, 360L);

    /** Factor for equation of time. */
    private static final Rational EQTIMEFACTOR;

    /** Factor for sunrise calculation. */
    private static final Rational SUNRISEFACTOR;

    static {
        SIDEREALYEAR = Rational.of(0x443a1L, 0x107ac0L).add(Rational.of(365L));
        CREATION = SIDEREALYEAR.multiply(Rational.of(0x74945c40L));
        TWO1600 = Rational.of(21600L);
        MEANMOTION = SIDEREALYEAR.inverse().multiply(TWO1600);
        EQTIMEFACTOR = SIDEREALYEAR.divide(TWO1600).divide(TWO1600);
        SUNRISEFACTOR = Rational.of(0x5e0d1d84L, 0x5e4f0884L).divide(TWO1600);
    }

    ModifiedHinduBRCalendar() {
    }

    public static int signum(double d) {
        if (d > 0.0D) return 1;
        return (d >= 0.0D) ? 0 : (-1);
    }

    public static int hindSineTable(long l) {
        double d = 3438D * Math.sin(((((double) l * 225D) / 60D) * 3.1415926535897931D) / 180D);
        double d1 = 0.215D * (double) signum(d) * (double) signum(Math.abs(d) - 1716D);
        return (int) Math.round(d + d1);
    }

    public static Rational hindSine(Rational val) {
        Rational val1 = val.divide(Rational.of(225L));
        Rational val2 = val1.fractionalPart();

        return val2.multiply(Rational.of(hindSineTable(val1.ceil().longValue())))
                           .add(Rational.ONE.subtract(val2)
                                                  .multiply(Rational.of(
                    hindSineTable(val1.floor().longValue()))));
    }

    public static Rational hindArcSine(Rational val) {
        if (lt(val, Rational.ZERO)) {
            return hindArcSine(val.negate()).negate();
        }

        int k = 0;
        int j = 0;

        for (; gt(val, Rational.of(hindSineTable(k))); k++)
            j++;

        int i = hindSineTable(j - 1);
        Rational val1 = val.subtract(Rational.of(i))
                                                .divide(Rational.of(hindSineTable(j) - i));
        val1 = Rational.of(j).add(val1);
        val1 = val1.subtract(Rational.ONE);
        val1 = val1.multiply(Rational.of(225L));

        return val1;
    }

    public static Rational hindArcSine(long l) {
        return hindArcSine(Rational.of(l));
    }

    public static Rational meanPosition(Rational val, Rational val1) {
        return val.divide(val1).fractionalPart().multiply(TWO1600);
    }

    public static Rational truePosition(Rational val, Rational val1, Rational val2,
        Rational val3, Rational val4) {
        Rational val5 = meanPosition(val, val1);
        Rational val6 = CREATION.add(val);
        Rational val7 = hindSine(meanPosition(val6, val3));
        Rational val8 = val2.divide(Rational.of(3438L)).multiply(val4).multiply(val7.abs());
        Rational val9 = hindArcSine(val2.subtract(val8).multiply(val7));

        return val5.subtract(val9).mod(TWO1600);
    }

    public static Rational solarLongitude(Rational val) {
        return truePosition(val, SIDEREALYEAR, FOURTEENTHREESIXTY,
            ANOMYEAR, Rational.of(1L, 42L));
    }

    public static int zodiac(Rational val) {
        return solarLongitude(val).divide(Rational.of(1800L))
                   .floor().intValue() + 1;
    }

    public static Rational lunarLongitude(Rational val) {
        return truePosition(val, SIDEREALMONTH,
            Rational.of(32L, 360L), ANOMMONTH, Rational.of(1L, 42L));
    }

    public static Rational lunarPhase(Rational val) {
        return lunarLongitude(val).subtract(solarLongitude(val)).mod(TWO1600);
    }

    public static int lunarDay(Rational val) {
        return lunarPhase(val).divide(Rational.of(720L)).floor().intValue() + 1;
    }

    public static Rational newMoon(Rational val) {
        Rational val1 = val.add(Rational.ONE);
        Rational val2 = val1.subtract(val1.mod(SYNODICMONTH));
        Rational val3 = Rational.of(2L, 3L);
        Rational val4 = val2.subtract(val3);
        Rational val5 = val2.add(val3);
        Rational val6 = val4.add(val5).divide(Rational.of(2L));
        Rational val7 = Rational.of(10800L);

        while (!lt(val, val4) && (!lte(val5, val) || (zodiac(val4) != zodiac(val5)))) {
            if (lt(lunarPhase(val6), val7)) {
                val5 = val6;
            } else {
                val4 = val6;
            }
            val6 = val4.add(val5).divide(Rational.of(2L));
        }

        if (gt(val6, val)) {
            return newMoon(val.floor().subtract(org.jscience.core.mathematics.numbers.integers.Integer.of(20)).toRational());
        } else {
            return val6;
        }
    }

    public static int calYear(Rational val) {
        Rational val1 = val.divide(SIDEREALYEAR);
        Rational val2 = val1.fractionalPart().multiply(TWO1600);
        Rational val3 = solarLongitude(val);
        int i = val1.floor().intValue();

        if (gt(val3, TWENTYTHOUSAND) && lt(val2, THOUSAND)) {
            return i - 1;
        }

        if (gt(val2, TWENTYTHOUSAND) && lt(val3, THOUSAND)) {
            return i + 1;
        } else {
            return i;
        }
    }

    public static Rational ascDiff(Rational val, Rational val1) {
        Rational val2 = Rational.of(1397L, 3438L).multiply(hindSine(tropLongitude(val)));
        Rational val3 = hindSine(hindArcSine(val2).negate().add(Rational.of(5400L)));
        Rational val4 = hindSine(val1).divide(hindSine(val1.add(Rational.of(5400L))));
        Rational val5 = val2.multiply(val4);

        return hindArcSine(val5.divide(val3).multiply(Rational.of(-3438L)));
    }

    public static Rational tropLongitude(Rational val) {
        Rational val1 = val.floor().toRational();
        Rational val2 = val1.multiply(Rational.of(0x3b5380L, 0x5e0d1d84L));
        val2 = val2.mod(Rational.of(6480L));
        val2 = val2.add(Rational.of(1620L)).negate();
        val2 = val2.add(Rational.of(3240L));
        val2 = val2.abs().negate();
        val2 = val2.add(Rational.of(1620L));

        return solarLongitude(val).subtract(val2).mod(TWO1600);
    }

    public static Rational solarSiderealDifference(Rational val) {
        return dailyMotion(val)
                   .multiply(Rational.of(risingSign(val)))
                   .divide(Rational.of(1800L));
    }

    public static Rational dailyMotion(Rational val) {
        Rational val1 = meanPosition(CREATION.add(val), ANOMYEAR);
        Rational val2 = hindSine(val1).abs();
        Rational val3 = FOURTEENTHREESIXTY.subtract(val2.divide(Rational.of(0x38a810L)));
        int i = val1.divide(Rational.of(225L)).floor().intValue();
        int j = hindSineTable(i + 1) - hindSineTable(i);
        Rational val4 = val3.divide(Rational.of(225L)).negate().multiply(Rational.of(j));

        return MEANMOTION.multiply(val4.add(Rational.ONE));
    }

    public static int risingSign(Rational val) {
        int[] ai = { 1670, 1795, 1935, 1935, 1795, 1670 };
        return ai[tropLongitude(val).divide(Rational.of(1800L)).floor().intValue() % 6];
    }

    public static Rational equationOfTime(Rational val) {
        Rational val1 = hindSine(meanPosition(CREATION.add(val), ANOMYEAR));
        Rational val2 = val1.abs();
        Rational val3 = val2.divide(Rational.of(0x38a810L)).subtract(FOURTEENTHREESIXTY).multiply(val1);
        return dailyMotion(val).multiply(val3).multiply(EQTIMEFACTOR);
    }

    public static Rational sunrise(Rational val) {
        Rational val1 = val.add(Rational.of(1L, 4L)).add(equationOfTime(val));
        val1 = val1.add(SUNRISEFACTOR.multiply(ascDiff(val, Rational.of(1389L))
                                     .add(solarSiderealDifference(val).divide(Rational.of(4L)))));
        return val1;
    }

    private static boolean gt(Rational val1, Rational val2) {
        return val1.compareTo(val2) > 0;
    }

    private static boolean lte(Rational val1, Rational val2) {
        return val1.compareTo(val2) <= 0;
    }

    private static boolean lt(Rational val1, Rational val2) {
        return val1.compareTo(val2) < 0;
    }


    public static void main(String[] args) {
        System.out.println(hindSineTable(Integer.parseInt(args[0])));
    }
}

