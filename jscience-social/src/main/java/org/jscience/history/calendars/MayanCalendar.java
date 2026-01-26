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

/**
 * Implementation of the Mayan calendar system.
 * The Mayan calendar consists of several interlocking cycles including the
 * Long Count, Haab (365-day civil calendar), and Tzolkin (260-day ritual calendar).
 *
 * <p>Key features:</p>
 * <ul>
 *   <li>Long Count: B'ak'tun.K'atun.Tun.Winal.K'in format</li>
 *   <li>Haab: 18 months of 20 days + 5 Wayeb days</li>
 *   <li>Tzolkin: 13 numbers × 20 day names = 260 days</li>
 *   <li>Calendar Round: 52-year cycle combining Haab and Tzolkin</li>
 * </ul>
 *
 * @author Mark E. Shoulson (original implementation)
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public class MayanCalendar extends AlternateCalendar {

    private static final long serialVersionUID = 1L;
    /** The Rata Die of the Mayan epoch (13.0.0.0.0 4 Ajaw 8 Kumk'u). */
    static final long EPOCH = AlternateCalendar.fromJD(584284.5D);

    /** The 19 month names of the Haab calendar. */
    static final String[] HAAB = {
            "Pop", "Uo", "Zip", "Zotz", "Tzec", "Xul", "Yaxkin", "Mol", "Chen",
            "Yax", "Zac", "Ceh", "Mac", "Kankin", "Muan", "Pax", "Kayab",
            "Cumku", "Uayeb"
        };

    /** The 20 day names of the Tzolkin calendar. */
    static final String[] TZOLKIN = {
            "Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat",
            "Muluc", "Oc", "Chuen", "Eb", "Ben", "Ix", "Men", "Cib", "Caban",
            "Etznab", "Cauac", "Ahau"
        };

    /** An object representing the epoch in the Mayan system. */
    static final MayanCalendar EPOCH_OBJ;

    /** The Haab date at the epoch. */
    static final Haab HAAB_EPOCH;

    /** The Tzolkin date at the epoch. */
    static final Tzolkin TZOLKIN_EPOCH;

    static {
        EPOCH_OBJ = new MayanCalendar(8, 18, 4, 20);
        HAAB_EPOCH = EPOCH_OBJ.haab;
        TZOLKIN_EPOCH = EPOCH_OBJ.tzolkin;
    }

    /** The long count value in days since the epoch. */
    long longcount;

    /** The civil Haab calendar part. */
    Haab haab;

    /** The ritual Tzolkin calendar part. */
    Tzolkin tzolkin;

/**
     * Creates a new MayanCalendar object.
     */
    public MayanCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param i the B'ak'tun.
     * @param j the K'atun.
     * @param k the Tun.
     * @param l the Winal.
     * @param i1 the K'in.
     */
    public MayanCalendar(int i, int j, int k, int l, int i1) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(i, j, k, l, i1);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param l the Rata Die number.
     */
    public MayanCalendar(long l) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(l);
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param altcalendar another calendar to initialize from.
     */
    public MayanCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new MayanCalendar object.
     *
     * @param i Haab day.
     * @param j Haab month.
     * @param k Tzolkin number.
     * @param l Tzolkin name.
     */
    protected MayanCalendar(int i, int j, int k, int l) {
        longcount = 1L;
        super.rd = EPOCH;
        haab = new Haab(i, j);
        tzolkin = new Tzolkin(k, l);
    }

    /**
     * Sets the Mayan Long Count parameters and recomputes the date.
     *
     * @param i B'ak'tun.
     * @param j K'atun.
     * @param k Tun.
     * @param l Winal.
     * @param i1 K'in.
     */
    public synchronized void set(int i, int j, int k, int l, int i1) {
        longcount = (i * 0x23280) + (j * 7200) + (k * 360) + (l * 20) + i1;
        super.rd = longcount + EPOCH;
        recomputeFromRD();
    }

    /**
     * Recomputes the Mayan date components from the current Rata Die number.
     */
    protected synchronized void recomputeFromRD() {
        longcount = super.rd - EPOCH;
        haab.recompute();
        tzolkin.recompute();
    }

    /**
     * Recomputes the Rata Die number. Mayan components use Rata Die directly.
     */
    protected synchronized void recomputeRD() {
    }

    /**
     * Sets the Rata Die number and recomputes Mayan fields.
     *
     * @param l the Rata Die number.
     */
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    /**
     * Returns the Long Count representation as a dot-separated string.
     *
     * @return the long count string.
     */
    public String longCountString() {
        long l = AlternateCalendar.mod(longcount, 0x23280);
        long l1 = AlternateCalendar.mod(l, 7200);
        long l2 = AlternateCalendar.mod(l1, 360);

        return AlternateCalendar.floorDiv(longcount, 0x23280L) + "." +
        AlternateCalendar.floorDiv(l, 7200L) + "." +
        AlternateCalendar.floorDiv(l1, 360L) + "." +
        AlternateCalendar.floorDiv(l2, 20L) + "." + AlternateCalendar.mod(l2, 20);
    }

    /**
     * Returns a string representation of the Mayan date (Long Count, Tzolkin, and Haab).
     *
     * @return the date string.
     */
    public String toString() {
        return longCountString() + ": " + tzolkin + "; " + haab;
    }

    /**
     * Main method for testing the Mayan calendar implementation.
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

        MayanCalendar mayan = new MayanCalendar(gregorian);
        System.out.println(gregorian + ": " + mayan);
        System.out.println(mayan.toRD());
        System.out.println("\nHaab Epoch: " + HAAB_EPOCH + "\nTzolkin Epoch: " +
            TZOLKIN_EPOCH);
    }

    /**
     * Inner class representing the Haab part of the Mayan calendar.
     */
    class Haab implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        /** The day of the month (0-19, or 0-4 for Uayeb). */
        private int day;

        /** The month of the year (1-19). */
        private int month;

/**
         * Creates a new Haab object.
         *
         * @param i the day.
         * @param j the month.
         */
        public Haab(int i, int j) {
            month = j;
            day = i;
        }

/**
         * Creates a new Haab object.
         *
         * @param haab1 another Haab object to copy.
         */
        public Haab(Haab haab1) {
            month = haab1.month;
            day = haab1.day;
        }

/**
         * Creates a new Haab object.
         */
        public Haab() {
            this(MayanCalendar.HAAB_EPOCH);
        }

        /**
         * Returns the month.
         *
         * @return the month number (1-19).
         */
        public int getMonth() {
            return month;
        }

        /**
         * Returns the day.
         *
         * @return the day number (0-19).
         */
        public int getDay() {
            return day;
        }

        /**
         * Recomputes the Haab date from the outer class's long count.
         */
        public synchronized void recompute() {
            int i = AlternateCalendar.mod(longcount +
                    (long) MayanCalendar.HAAB_EPOCH.getDay() +
                    (long) (20 * (MayanCalendar.HAAB_EPOCH.getMonth() - 1)), 365);
            day = i % 20;
            month = (i / 20) + 1;
        }

        /**
         * Returns a string representation of the Haab date.
         *
         * @return the Haab date string.
         */
        public String toString() {
            try {
                return day + " " + MayanCalendar.HAAB[month - 1];
            } catch (ArrayIndexOutOfBoundsException _ex) {
                return "Invalid Date: " + day + " " + month;
            }
        }
    }

    /**
     * Inner class representing the Tzolkin part of the Mayan calendar.
     */
    class Tzolkin implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        /** The tzolkin number (1-13). */
        private int number;

        /** The tzolkin day name index (1-20). */
        private int name;

/**
         * Creates a new Tzolkin object.
         */
        public Tzolkin() {
            this(MayanCalendar.TZOLKIN_EPOCH);
        }

/**
         * Creates a new Tzolkin object.
         *
         * @param i the tzolkin number.
         * @param j the tzolkin day name index.
         */
        public Tzolkin(int i, int j) {
            number = i;
            name = j;
        }

/**
         * Creates a new Tzolkin object.
         *
         * @param tzolkin1 another Tzolkin object to copy.
         */
        public Tzolkin(Tzolkin tzolkin1) {
            number = tzolkin1.getNumber();
            name = tzolkin1.getName();
        }

        /**
         * Returns the tzolkin number.
         *
         * @return the number (1-13).
         */
        public int getNumber() {
            return number;
        }

        /**
         * Returns the tzolkin day name index.
         *
         * @return the name index (1-20).
         */
        public int getName() {
            return name;
        }

        /**
         * Recomputes the Tzolkin date from the outer class's long count.
         */
        public synchronized void recompute() {
            number = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getNumber(), 13L);
            name = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getName(), 20L);
        }

        /**
         * Returns a string representation of the Tzolkin date.
         *
         * @return the Tzolkin date string.
         */
        public String toString() {
            try {
                return number + " " + MayanCalendar.TZOLKIN[name - 1];
            } catch (ArrayIndexOutOfBoundsException _ex) {
                return "Invalid Date: " + number + " " + name;
            }
        }
    }
}
