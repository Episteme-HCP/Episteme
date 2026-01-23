/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Originally based on code from Mark E. Shoulson <mark@kli.org>
 * http://web.meson.org/calendars/
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

package org.jscience.history.calendars;

import org.jscience.mathematics.algebraic.numbers.ExactRational;

/**
 * Modified Hindu Solar calendar using arbitrary-precision arithmetic.
 * This version uses ExactRational for precise calculations without
 * floating-point errors.
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class ModifiedHinduSolarBRCalendar extends OldHinduSolarCalendar {

    private static final long serialVersionUID = 1L;
    /** Offset for the solar era (Saka Era). */
    protected static final int SOLARERA = 3179;

    /** Delegate for Hindu astronomical calculations. */
    protected static ModifiedHinduBRCalendar mh;

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param l Rata Die number.
     */
    public ModifiedHinduSolarBRCalendar(long l) {
        super(l);
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ModifiedHinduSolarBRCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     */
    public ModifiedHinduSolarBRCalendar() {
    }

/**
     * Creates a new ModifiedHinduSolarBRCalendar object.
     *
     * @param i the month.
     * @param j the day.
     * @param k the year.
     */
    public ModifiedHinduSolarBRCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    /**
     * Recomputes the Hindu solar date components from the current Rata Die number.
     */
    public synchronized void recomputeFromRD() {
        ExactRational bigrational = new ExactRational(dayCount());
        ExactRational bigrational1 = ModifiedHinduBRCalendar.sunrise(bigrational);
        super.month = ModifiedHinduBRCalendar.zodiac(bigrational1);
        super.year = ModifiedHinduBRCalendar.calYear(bigrational1) - 3179;

        ExactRational bigrational2;

        for (bigrational2 = bigrational.subtract(new ExactRational(3L))
                                       .subtract(new ExactRational(
                        ModifiedHinduBRCalendar.solarLongitude(bigrational1)
                                               .mod(new ExactRational(1800L))
                                               .divide(new ExactRational(60L))
                                               .floor()));
                ModifiedHinduBRCalendar.zodiac(ModifiedHinduBRCalendar.sunrise(
                        bigrational2)) != super.month;
                bigrational2 = bigrational2.add(new ExactRational(1L)))
            ;

        super.day = bigrational.subtract(bigrational2).intValue() + 1;
    }

    /**
     * Checks if this date precedes another Hindu solar date.
     *
     * @param modhindusolarbr the other date.
     * @return true if this date is before the other.
     */
    public boolean precedes(ModifiedHinduSolarBRCalendar modhindusolarbr) {
        return (super.year < ((MonthDayYear) (modhindusolarbr)).year) ||
        ((super.year == ((MonthDayYear) (modhindusolarbr)).year) &&
        ((super.month < ((MonthDayYear) (modhindusolarbr)).month) ||
        ((super.month == ((MonthDayYear) (modhindusolarbr)).month) &&
        (super.day < ((MonthDayYear) (modhindusolarbr)).day))));
    }

    /**
     * Recomputes the Rata Die number from the current Hindu solar date components.
     *
     * @throws InconsistentDateException if the date is invalid.
     */
    public synchronized void recomputeRD() {
        ExactRational bigrational = new ExactRational(super.month - 1, 12L);
        bigrational = bigrational.add(new ExactRational(super.year + 3179))
                                 .multiply(ModifiedHinduBRCalendar.SIDEREALYEAR);

        long l = bigrational.floor().longValue();
        l += ((OldHinduSolarCalendar.EPOCH + (long) super.day) - 9L);

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(l);
        System.err.println("tmp: " + modhindusolarbr);

        if ((((MonthDayYear) (modhindusolarbr)).month == super.month) &&
                (((MonthDayYear) (modhindusolarbr)).year == super.year)) {
            l += (super.day - ((MonthDayYear) (modhindusolarbr)).day - 1);
        }

        for (; modhindusolarbr.precedes(this);
                System.err.println("tmp: " + modhindusolarbr + " => (" +
                    modhindusolarbr.toRD() + ")"))
            modhindusolarbr = new ModifiedHinduSolarBRCalendar(++l);

        super.rd = l;

        if ((((MonthDayYear) (modhindusolarbr)).day != super.day) ||
                (((MonthDayYear) (modhindusolarbr)).month != super.month) ||
                (((MonthDayYear) (modhindusolarbr)).year != super.year)) {
            throw new InconsistentDateException("Illegal Hindu Solar Date");
        } else {
            return;
        }
    }

    /**
     * Returns the era suffix for this calendar.
     *
     * @return the suffix string.
     */
    protected String getSuffix() {
        return " S.E.";
    }

    /**
     * Main method for testing Hindu solar calendar calculations.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int i;
        int j;
        int k;

        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception _ex) {
            i = k = j = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        ModifiedHinduSolarBRCalendar modhindusolarbr = new ModifiedHinduSolarBRCalendar(gregorian);
        System.out.println(gregorian + "(" + modhindusolarbr.toRD() + "): " +
            modhindusolarbr);
        System.out.println("===============");
        modhindusolarbr = new ModifiedHinduSolarBRCalendar(i, j, k);
        System.out.println("HinduCalendar " + i + " " + j + " " + k + ": " +
            modhindusolarbr.toRD());
    }
}
