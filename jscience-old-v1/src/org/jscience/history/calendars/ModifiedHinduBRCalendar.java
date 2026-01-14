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

//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;

import org.jscience.mathematics.algebraic.numbers.ExactRational;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
abstract class ModifiedHinduBRCalendar {
    /** DOCUMENT ME! */
    public static final ExactRational SIDEREALYEAR;

    /** DOCUMENT ME! */
    public static final ExactRational SIDEREALMONTH = (new ExactRational(0x46de57L,
            0xdc4fbeL)).add(new ExactRational(27L));

    /** DOCUMENT ME! */
    public static final ExactRational SYNODICMONTH = (new ExactRational(0x6c269bL,
            0xcbd4feL)).add(new ExactRational(29L));

    /** DOCUMENT ME! */
    public static final ExactRational CREATION;

    /** DOCUMENT ME! */
    public static final ExactRational ANOMYEAR = new ExactRational(0x16f633b4ba0L,
            0x1017df67dL);

    /** DOCUMENT ME! */
    public static final ExactRational ANOMMONTH = new ExactRational(0x5e0d1d84L,
            0x369cbf1L);

    /** DOCUMENT ME! */
    private static final ExactRational TWO1600;

    /** DOCUMENT ME! */
    private static final ExactRational THOUSAND = new ExactRational(1000L);

    /** DOCUMENT ME! */
    private static final ExactRational TWENTYTHOUSAND = new ExactRational(20000L);

    /** DOCUMENT ME! */
    private static final ExactRational MEANMOTION;

    /** DOCUMENT ME! */
    private static final ExactRational FOURTEENTHREESIXTY = new ExactRational(14L,
            360L);

    /** DOCUMENT ME! */
    private static final ExactRational EQTIMEFACTOR;

    /** DOCUMENT ME! */
    private static final ExactRational SUNRISEFACTOR;

    static {
        SIDEREALYEAR = (new ExactRational(0x443a1L, 0x107ac0L)).add(new ExactRational(
                    365L));
        CREATION = SIDEREALYEAR.multiply(new ExactRational(0x74945c40L));
        TWO1600 = new ExactRational(21600L);
        MEANMOTION = ((ExactRational) SIDEREALYEAR.inverse()).multiply(TWO1600);
        EQTIMEFACTOR = SIDEREALYEAR.divide(TWO1600).divide(TWO1600);
        SUNRISEFACTOR = (new ExactRational(0x5e0d1d84L, 0x5e4f0884L)).divide(TWO1600);
    }

/**
     * Creates a new ModifiedHinduBRCalendar object.
     */
    ModifiedHinduBRCalendar() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int signum(double d) {
        if (d > 0.0D) {
            return 1;
        }

        return (d >= 0.0D) ? 0 : (-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int hindSineTable(long l) {
        double d = 3438D * Math.sin(((((double) l * 225D) / 60D) * 3.1415926535897931D) / 180D);
        double d1 = 0.215D * (double) signum(d) * (double) signum(Math.abs(d) -
                1716D);

        return (int) Math.round(d + d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational hindSine(ExactRational bigrational) {
        ExactRational bigrational1 = bigrational.divide(new ExactRational(225L));
        ExactRational bigrational2 = new ExactRational(bigrational1.fractionalPart());

        return bigrational2.multiply(new ExactRational(hindSineTable(
                    bigrational1.ceil().longValue())))
                           .add(ExactRational.ONE.subtract(bigrational2)
                                                 .multiply(new ExactRational(
                    hindSineTable(bigrational1.floor().longValue()))));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational hindArcSine(ExactRational bigrational) {
        if (lt(bigrational, ExactRational.ZERO)) {
            return (ExactRational) hindArcSine((ExactRational) bigrational.negate())
                                       .negate();
        }

        int k = 0;
        int j = 0;

        for (; gt(bigrational, new ExactRational(hindSineTable(k))); k++)
            j++;

        int i = hindSineTable(j - 1);
        ExactRational bigrational1 = bigrational.subtract(new ExactRational(i))
                                                .divide(new ExactRational(hindSineTable(
                        j) - i));
        bigrational1 = (new ExactRational(j)).add(bigrational1);
        bigrational1 = bigrational1.subtract(new ExactRational(1L));
        bigrational1 = bigrational1.multiply(new ExactRational(225L));

        return bigrational1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational hindArcSine(long l) {
        return hindArcSine(new ExactRational(l));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     * @param bigrational1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational meanPosition(ExactRational bigrational,
        ExactRational bigrational1) {
        return (ExactRational) bigrational.divide(bigrational1).fractionalPart()
                                          .multiply(TWO1600);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     * @param bigrational1 DOCUMENT ME!
     * @param bigrational2 DOCUMENT ME!
     * @param bigrational3 DOCUMENT ME!
     * @param bigrational4 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational truePosition(ExactRational bigrational,
        ExactRational bigrational1, ExactRational bigrational2,
        ExactRational bigrational3, ExactRational bigrational4) {
        ExactRational bigrational5 = meanPosition(bigrational, bigrational1);
        ExactRational bigrational6 = CREATION.add(bigrational);
        ExactRational bigrational7 = hindSine(meanPosition(bigrational6,
                    bigrational3));
        ExactRational bigrational8 = bigrational2.divide(new ExactRational(
                    3438L)).multiply(bigrational4).multiply(bigrational7.abs());
        ExactRational bigrational9 = hindArcSine(bigrational2.subtract(
                    bigrational8).multiply(bigrational7));

        return bigrational5.subtract(bigrational9).mod(TWO1600);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational solarLongitude(ExactRational bigrational) {
        return truePosition(bigrational, SIDEREALYEAR, FOURTEENTHREESIXTY,
            ANOMYEAR, new ExactRational(1L, 42L));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int zodiac(ExactRational bigrational) {
        return solarLongitude(bigrational).divide(new ExactRational(1800L))
                   .floor().intValue() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational lunarLongitude(ExactRational bigrational) {
        return truePosition(bigrational, SIDEREALMONTH,
            new ExactRational(32L, 360L), ANOMMONTH, new ExactRational(1L, 42L));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational lunarPhase(ExactRational bigrational) {
        return lunarLongitude(bigrational).subtract(solarLongitude(bigrational))
                   .mod(TWO1600);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int lunarDay(ExactRational bigrational) {
        return lunarPhase(bigrational).divide(new ExactRational(720L)).floor()
                   .intValue() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational newMoon(ExactRational bigrational) {
        ExactRational bigrational1 = bigrational.add(ExactRational.ONE);
        ExactRational bigrational2 = bigrational1.subtract(bigrational1.mod(
                    SYNODICMONTH));
        ExactRational bigrational3 = new ExactRational(2L, 3L);
        ExactRational bigrational4 = bigrational2.subtract(bigrational3);
        ExactRational bigrational5 = bigrational2.add(bigrational3);
        ExactRational bigrational6 = bigrational4.add(bigrational5)
                                                 .divide(new ExactRational(2L));
        ExactRational bigrational7 = new ExactRational(10800L);

        while (!lt(bigrational, bigrational4) &&
                (!lte(bigrational5, bigrational) ||
                (zodiac(bigrational4) != zodiac(bigrational5)))) {
            if (lt(lunarPhase(bigrational6), bigrational7)) {
                bigrational5 = bigrational6;
            } else {
                bigrational4 = bigrational6;
            }

            bigrational6 = bigrational4.add(bigrational5)
                                       .divide(new ExactRational(2L));
        }

        if (gt(bigrational6, bigrational)) {
            return newMoon(new ExactRational(bigrational.floor()
                                                        .subtract(new ExactRational(
                            20))));
        } else {
            return bigrational6;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int calYear(ExactRational bigrational) {
        ExactRational bigrational1 = bigrational.divide(SIDEREALYEAR);
        ExactRational bigrational2 = (ExactRational) bigrational1.fractionalPart()
                                                                 .multiply(TWO1600);
        ExactRational bigrational3 = solarLongitude(bigrational);
        int i = bigrational1.floor().intValue();

        if (gt(bigrational3, TWENTYTHOUSAND) && lt(bigrational2, THOUSAND)) {
            return i - 1;
        }

        if (gt(bigrational2, TWENTYTHOUSAND) && lt(bigrational3, THOUSAND)) {
            return i + 1;
        } else {
            return i;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     * @param bigrational1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational ascDiff(ExactRational bigrational,
        ExactRational bigrational1) {
        ExactRational bigrational2 = (new ExactRational(1397L, 3438L)).multiply(hindSine(
                    tropLongitude(bigrational)));
        ExactRational bigrational3 = hindSine((ExactRational) hindArcSine(
                    bigrational2).negate().add(new ExactRational(5400L)));
        ExactRational bigrational4 = hindSine(bigrational1)
                                         .divide(hindSine(bigrational1.add(
                        new ExactRational(5400L))));
        ExactRational bigrational5 = bigrational2.multiply(bigrational4);

        return hindArcSine(bigrational5.divide(bigrational3)
                                       .multiply(new ExactRational(-3438L)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational tropLongitude(ExactRational bigrational) {
        ExactRational bigrational1 = new ExactRational(bigrational.floor());
        ExactRational bigrational2 = bigrational1.multiply(new ExactRational(
                    0x3b5380L, 0x5e0d1d84L));
        bigrational2 = bigrational2.mod(new ExactRational(6480L));
        bigrational2 = (ExactRational) bigrational2.add(new ExactRational(1620L))
                                                   .negate();
        bigrational2 = bigrational2.add(new ExactRational(3240L));
        bigrational2 = (ExactRational) bigrational2.abs().negate();
        bigrational2 = bigrational2.add(new ExactRational(1620L));

        return solarLongitude(bigrational).subtract(bigrational2).mod(TWO1600);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational solarSiderealDifference(
        ExactRational bigrational) {
        return dailyMotion(bigrational)
                   .multiply(new ExactRational(risingSign(bigrational)))
                   .divide(new ExactRational(1800L));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational dailyMotion(ExactRational bigrational) {
        ExactRational bigrational1 = meanPosition(CREATION.add(bigrational),
                ANOMYEAR);
        ExactRational bigrational2 = hindSine(bigrational1).abs();
        ExactRational bigrational3 = FOURTEENTHREESIXTY.subtract(bigrational2.divide(
                    new ExactRational(0x38a810L)));
        int i = bigrational1.divide(new ExactRational(225L)).floor().intValue();
        int j = hindSineTable(i + 1) - hindSineTable(i);
        ExactRational bigrational4 = ((ExactRational) bigrational3.divide(new ExactRational(
                    225L)).negate()).multiply(new ExactRational(j));

        return MEANMOTION.multiply(bigrational4.add(new ExactRational(1L)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int risingSign(ExactRational bigrational) {
        int[] ai = { 1670, 1795, 1935, 1935, 1795, 1670 };

        return ai[tropLongitude(bigrational).divide(new ExactRational(1800L))
                      .floor().intValue() % 6];
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational equationOfTime(ExactRational bigrational) {
        ExactRational bigrational1 = hindSine(meanPosition(CREATION.add(
                        bigrational), ANOMYEAR));
        ExactRational bigrational2 = bigrational1.abs();
        ExactRational bigrational3 = bigrational2.divide(new ExactRational(
                    0x38a810L)).subtract(FOURTEENTHREESIXTY)
                                                 .multiply(bigrational1);

        return dailyMotion(bigrational).multiply(bigrational3)
                   .multiply(EQTIMEFACTOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @param bigrational DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExactRational sunrise(ExactRational bigrational) {
        ExactRational bigrational1 = bigrational.add(new ExactRational(1L, 4L))
                                                .add(equationOfTime(bigrational));
        bigrational1 = bigrational1.add(SUNRISEFACTOR.multiply(ascDiff(
                        bigrational, new ExactRational(1389L))
                                                                   .add(solarSiderealDifference(
                            bigrational).divide(new ExactRational(4L)))));

        return bigrational1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean gt(ExactRational rational1, ExactRational rational2) {
        return rational2.subtract(rational1).signum() < 0;

        //we could also use compareTo here which may return slightly different results in some situations
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean lte(ExactRational rational1, ExactRational rational2) {
        return !gt(rational1, rational2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean lt(ExactRational rational1, ExactRational rational2) {
        return gt(rational2, rational1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rational1 DOCUMENT ME!
     * @param rational2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean gte(ExactRational rational1, ExactRational rational2) {
        return !lt(rational1, rational2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(hindSineTable(Integer.parseInt(args[0])));
    }
}
