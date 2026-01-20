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
public class PersianCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    public static long EPOCH = (new JulianCalendar(3, 19, 622)).toRD();

    /** DOCUMENT ME! */
    private static final long FOUR75 = (new PersianCalendar(1, 1, 475)).toRD();

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar",
            "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand"
        };

/**
     * Creates a new PersianCalendar object.
     */
    public PersianCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public PersianCalendar(long l) {
        set(l);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public PersianCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new PersianCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public PersianCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int yconv(int i) {
        if (i > 0) {
            return i - 474;
        } else {
            return i - 473;
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        int i = yconv(super.year);
        int j = AlternateCalendar.mod(i, 2820) + 474;
        super.rd = (EPOCH - 1L) +
            (0xfb75fL * AlternateCalendar.fldiv(i, 2820L)) +
            (long) (365 * (j - 1)) +
            AlternateCalendar.fldiv((682 * j) - 110, 2816L) +
            (long) ((super.month > 7) ? ((30 * (super.month - 1)) + 6)
                                      : (31 * (super.month - 1))) +
            (long) super.day;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        long l = super.rd - FOUR75;
        int i = (int) AlternateCalendar.fldiv(l, 0xfb75fL);
        int j = AlternateCalendar.mod(l, 0xfb75f);
        int k;

        if (j == 0xfb75e) {
            k = 2820;
        } else {
            k = (int) AlternateCalendar.fldiv((2816L * (long) j) + 0xfbca9L,
                    0xfb1aaL);
        }

        super.year = 474 + (2820 * i) + k;

        if (super.year <= 0) {
            super.year--;
        }

        int i1 = (int) ((super.rd -
            (new PersianCalendar(1, 1, super.year)).toRD()) + 1L);

        if (i1 <= 186) {
            super.month = (int) Math.ceil((double) i1 / 31D);
        } else {
            super.month = (int) Math.ceil((double) (i1 - 6) / 30D);
        }

        super.day = (int) ((super.rd -
            (new PersianCalendar(super.month, 1, super.year)).toRD()) + 1L);
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
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        int j = yconv(i);
        int k = AlternateCalendar.mod(j, 2820) + 474;

        return AlternateCalendar.mod((k + 38) * 682, 2816) < 682;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String monthName() {
        return MONTHS[super.month - 1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return " A.P.";
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

        PersianCalendar persian = new PersianCalendar(gregorian);
        System.out.println(gregorian + ": " + persian);
        persian.set(i, j, k);
        System.out.println("PersianCalendar(" + i + "," + j + "," + k + "): " +
            persian);
        System.out.println(persian.toRD());
    }
}
