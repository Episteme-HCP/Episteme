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

import java.util.Enumeration;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ModifiedFrenchCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    protected static final long EPOCH = (new GregorianCalendar(9, 22, 1792)).toRD();

    /** DOCUMENT ME! */
    private static final String[] UNIMONTHS = {
            "Vend\351miaire", "Brumaire", "Frimaire", "Niv\364se", "Pluvi\364se",
            "Vent\364se", "Germinal", "Flor\351al", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Vendemiaire", "Brumaire", "Frimaire", "Nivose", "Pluviose",
            "Ventose", "Germinal", "Floreal", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    /** DOCUMENT ME! */
    protected static final String[] DAYS = {
            "Primidi", "Duodi", "Tridi", "Quartidi", "Quintidi", "Sextidi",
            "Septidi", "Octidi", "Nonidi", "Decadi"
        };

    /** DOCUMENT ME! */
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
     * @param l DOCUMENT ME!
     */
    public ModifiedFrenchCalendar(long l) {
        set(l);
    }

/**
     * Creates a new ModifiedFrenchCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public ModifiedFrenchCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new ModifiedFrenchCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public ModifiedFrenchCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        int j = AlternateCalendar.mod(i, 400);

        return (AlternateCalendar.mod(i, 4) == 0) && (j != 100) && (j != 200) &&
        (j != 300) && (AlternateCalendar.mod(i, 4000) != 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        int i = super.year - 1;
        super.rd = ((((EPOCH - 1L) + (long) (365 * i) +
            AlternateCalendar.fldiv(i, 4L)) - AlternateCalendar.fldiv(i, 100L)) +
            AlternateCalendar.fldiv(i, 400L)) -
            AlternateCalendar.fldiv(i, 4000L);
        super.rd += ((30 * (super.month - 1)) + super.day);
    }

    /**
     * DOCUMENT ME!
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
        super.month = (int) AlternateCalendar.fldiv(super.rd -
                modfrench.toRD(), 30L) + 1;
        modfrench.set(super.month, 1, super.year);
        super.day = (int) ((super.rd - modfrench.toRD()) + 1L);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWeekDay() {
        return ((super.day - 1) % 10) + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDecade() {
        return ((super.day - 1) / 10) + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        if (AlternateCalendar.unicode) {
            return UNIMONTHS[super.month - 1];
        } else {
            return MONTHS[super.month - 1];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getMonths() {
        if (AlternateCalendar.unicode) {
            return new ArrayEnumeration(UNIMONTHS);
        } else {
            return new ArrayEnumeration(MONTHS);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
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
