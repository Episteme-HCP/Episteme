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
public class HebrewCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    protected static final long EPOCH = (new JulianCalendar(10, 7, -3761)).toRD();

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri",
            "Heshvan", "Kislev", "Tevet", "Shvat", "Adar", "Adar II"
        };

/**
     * Creates a new HebrewCalendar object.
     */
    public HebrewCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new HebrewCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public HebrewCalendar(long l) {
        set(l);
    }

/**
     * Creates a new HebrewCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public HebrewCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new HebrewCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public HebrewCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        return AlternateCalendar.mod((7 * i) + 1, 19) < 7;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int lastMonth(int i) {
        return (!isLeapYear(i)) ? 12 : 13;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static long elapsedDays(int i) {
        long l = AlternateCalendar.fldiv((235 * i) - 234, 19L);
        long l1 = 12084L + (13753L * l);
        long l2 = (29L * l) + AlternateCalendar.fldiv(l1, 25920L);

        if (AlternateCalendar.mod(3L * (l2 + 1L), 7) < 3) {
            l2++;
        }

        return l2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static int delay(int i) {
        long l = elapsedDays(i - 1);
        long l1 = elapsedDays(i);
        long l2 = elapsedDays(i + 1);

        if ((l2 - l1) == 356L) {
            return 2;
        }

        return ((l1 - l) != 382L) ? 0 : 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static int lastDay(int i, int j) {
        return ((i != 2) && (i != 4) && (i != 6) && (i != 10) && (i != 13) &&
        ((i != 12) || isLeapYear(j)) && ((i != 8) || longHeshvan(j)) &&
        ((i != 9) || !shortKislev(j))) ? 30 : 29;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static boolean longHeshvan(int i) {
        return AlternateCalendar.mod(daysInYear(i), 10) == 5;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static boolean shortKislev(int i) {
        return AlternateCalendar.mod(daysInYear(i), 10) == 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static int daysInYear(int i) {
        return (int) ((new HebrewCalendar(7, 1, i + 1)).toRD() -
        (new HebrewCalendar(7, 1, i)).toRD());
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        super.rd = (EPOCH + elapsedDays(super.year) + (long) delay(super.year) +
            (long) super.day) - 1L;

        if (super.month < 7) {
            int i = lastMonth(super.year);

            for (int k = 7; k <= i; k++)
                super.rd += lastDay(k, super.year);

            for (int l = 1; l < super.month; l++)
                super.rd += lastDay(l, super.year);

            return;
        }

        for (int j = 7; j < super.month; j++)
            super.rd += lastDay(j, super.year);
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        int i = (int) Math.floor((double) (super.rd - EPOCH) / 365.25D);
        super.year = i - 1;

        int j = i;
        HebrewCalendar hebrew;

        for (hebrew = new HebrewCalendar(7, 1, j); super.rd >= hebrew.toRD();
                hebrew.set(7, 1, j)) {
            super.year++;
            j++;
        }

        hebrew.set(1, 1, super.year);

        if (super.rd < hebrew.toRD()) {
            super.month = 7;
        } else {
            super.month = 1;
        }

        int k = super.month;
        hebrew.set(k, lastDay(k, super.year), super.year);

        for (; super.rd > hebrew.toRD();
                hebrew.set(k, lastDay(k, super.year), super.year)) {
            super.month++;
            k++;
        }

        hebrew.set(super.month, 1, super.year);
        super.day = (int) ((super.rd - hebrew.toRD()) + 1L);
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
     *
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        if ((super.month == 12) && isLeapYear(super.year)) {
            return MONTHS[super.month - 1] + " I";
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
        return " A.M.";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getMonths() {
        return new ArrayEnumeration(MONTHS);
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

        HebrewCalendar hebrew = new HebrewCalendar(gregorian);
        System.out.println(gregorian + ": " + hebrew);
        hebrew.set(i, j, k);
        System.out.println("HebrewCalendar(" + i + "," + j + "," + k + "): " +
            hebrew);
        System.out.println(hebrew.toRD());
        System.out.println("Hebrew Epoch: " + EPOCH);
    }
}
