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

import java.util.Enumeration;

/**
 * Implementation of the French Republican Calendar (modified version).
 * The French Republican Calendar was created during the French Revolution
 * and was in official use from 1793 to 1805.
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Epoch: September 22, 1792 (Autumnal Equinox)</li>
 *   <li>12 months of 30 days each, plus 5-6 complementary days</li>
 *   <li>10-day weeks (dÃ©cades) replacing the 7-day week</li>
 *   <li>Modified leap year rule for long-term accuracy</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ModifiedFrenchCalendar extends MonthDayYear {

    private static final long serialVersionUID = 1L;
    /** The RD number for the French Republican epoch (September 22, 1792). */
    protected static final long EPOCH = (new GregorianCalendar(9, 22, 1792)).toRD();

    /** Month names with accented characters. */
    private static final String[] UNIMONTHS = {
            "Vend\351miaire", "Brumaire", "Frimaire", "Niv\364se", "Pluvi\364se",
            "Vent\364se", "Germinal", "Flor\351al", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    /** Month names with standard ASCII characters. */
    private static final String[] MONTHS = {
            "Vendemiaire", "Brumaire", "Frimaire", "Nivose", "Pluviose",
            "Ventose", "Germinal", "Floreal", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    /** Day names for the 10-day decade. */
    protected static final String[] DAYS = {
            "Primidi", "Duodi", "Tridi", "Quartidi", "Quintidi", "Sextidi",
            "Septidi", "Octidi", "Nonidi", "Decadi"
        };

    /** Names for the complementary sans-culottides days. */
    protected static final String[] JOURS = {
            "de la Vertu", "du Genie", "du Labour", "de la Raison",
            "de la Recompense", "de la Revolution"
        };

/**
     * Creates a new ModifiedFrenchCalendar object.
     */
    public ModifiedFrenchCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new ModifiedFrenchCalendar object.
     *
     * @param l the Rata Die number.
     */
    public ModifiedFrenchCalendar(long l) {
        set(l);
    }

/**
     * Creates a new ModifiedFrenchCalendar object.
     *
     * @param i the month (1-13).
     * @param j the day.
     * @param k the year.
     */
    public ModifiedFrenchCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new ModifiedFrenchCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public ModifiedFrenchCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    /**
     * Checks if the given year is a leap year in the French Republican calendar.
     *
     * @param i the year to check.
     * @return true if it is a leap year.
     */
    public static boolean isLeapYear(int i) {
        int j = AlternateCalendar.mod(i, 400);

        return (AlternateCalendar.mod(i, 4) == 0) && (j != 100) && (j != 200) &&
        (j != 300) && (AlternateCalendar.mod(i, 4000) != 0);
    }

    /**
     * Sets the Rata Die number and recomputes the date.
     *
     * @param l the Rata Die number.
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * Recomputes the Rata Die number from the current month, day, and year.
     */
    protected synchronized void recomputeRD() {
        int i = super.year - 1;
        super.rd = ((((EPOCH - 1L) + (long) (365 * i) +
            AlternateCalendar.floorDiv(i, 4L)) - AlternateCalendar.floorDiv(i, 100L)) +
            AlternateCalendar.floorDiv(i, 400L)) -
            AlternateCalendar.floorDiv(i, 4000L);
        super.rd += ((30 * (super.month - 1)) + super.day);
    }

    /**
     * Recomputes the month, day, and year from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        int i = (int) Math.floor((double) (super.rd - EPOCH) / 365.24225000000001D);
        int j = i;
        super.year = i - 1;

        ModifiedFrenchCalendar modfrench;

        for (modfrench = new ModifiedFrenchCalendar(1, 1, j);
                super.rd >= modfrench.toRD(); modfrench.set(1, 1, j)) {
            super.year++;
            j++;
        }

        modfrench.set(1, 1, super.year);
        @SuppressWarnings("deprecation")
        long m = AlternateCalendar.fldiv(super.rd -
                modfrench.toRD(), 30L);
        super.month = (int)m + 1;
        modfrench.set(super.month, 1, super.year);
        super.day = (int) ((super.rd - modfrench.toRD()) + 1L);
    }

    /**
     * Returns the day within the 10-day decade (1-10).
     *
     * @return the day number.
     */
    public int getWeekDay() {
        return ((super.day - 1) % 10) + 1;
    }

    /**
     * Returns the decade number within the month (1-3).
     *
     * @return the decade number.
     */
    public int getDecade() {
        return ((super.day - 1) / 10) + 1;
    }

    /**
     * Returns the name of the current month.
     *
     * @return the month name string.
     */
    protected String monthName() {
        if (AlternateCalendar.unicode) {
            return UNIMONTHS[super.month - 1];
        } else {
            return MONTHS[super.month - 1];
        }
    }

    /**
     * Returns the era suffix for the French calendar (none).
     *
     * @return an empty string.
     */
    protected String getSuffix() {
        return "";
    }

    /**
     * Returns an enumeration of all French month names.
     *
     * @return enumeration of month names.
     */
    @Override
    public Enumeration<String> getMonths() {
        if (AlternateCalendar.unicode) {
            return new ArrayEnumeration<>(UNIMONTHS);
        } else {
            return new ArrayEnumeration<>(MONTHS);
        }
    }

    /**
     * Returns a string representation of the French date.
     *
     * @return the date string.
     */
    public String toString() {
        String[] as = { "I", "II", "III" };

        try {
            if (super.month < 13) {
                return "Decade " + as[getDecade() - 1] + ", " +
                DAYS[getWeekDay() - 1] + " de " + monthName() + ", " +
                super.year;
            } else {
                return "Jour " + JOURS[getWeekDay() - 1] + ", " + super.year;
            }
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return "Invalid date: " + super.month + " " + super.day + " " +
            super.year;
        }
    }

    /**
     * Main method for testing the Modified French calendar implementation.
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

        ModifiedFrenchCalendar modfrench = new ModifiedFrenchCalendar(gregorian);
        System.out.println(gregorian + ": " + modfrench);
        modfrench.set(i, j, k);
        System.out.println("ModifiedFrenchCalendar(" + i + "," + j + "," + k +
            "): " + modfrench);
        System.out.println(modfrench.toRD());
    }
}

