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
public class CopticCalendar extends JulianCalendar {
    /** DOCUMENT ME! */
    public static long EPOCH = (new JulianCalendar(8, 29, 284)).toRD();

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Tut", "Babah", "Hatur", "Kiyahk", "Tubah", "Amshir", "Baramhat",
            "Baramundah", "Bashans", "Ba'unah", "Abib", "Misra", "al-Nasi"
        };

/**
     * Creates a new CopticCalendar object.
     */
    public CopticCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public CopticCalendar(long l) {
        set(l);
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public CopticCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }

/**
     * Creates a new CopticCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public CopticCalendar(int i, int j, int k) {
        super(1, 1, 1);
        set(i, j, k);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLeapYear(int i) {
        return AlternateCalendar.mod(i, 4) == 3;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        super.rd = (EPOCH - 1L) + (long) (365 * (super.year - 1)) +
            AlternateCalendar.fldiv(super.year, 4L) +
            (long) (30 * (super.month - 1)) + (long) super.day;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((4L * (super.rd - EPOCH)) +
                1463L, 1461L);
        super.month = (int) AlternateCalendar.fldiv(super.rd -
                (new CopticCalendar(1, 1, super.year)).toRD(), 30L) + 1;
        super.day = (int) ((super.rd + 1L) -
            (new CopticCalendar(super.month, 1, super.year)).toRD());
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
        return "";
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
     * @return DOCUMENT ME!
     */
    public String toString() {
        try {
            return super.day + " " + monthName() + " " + super.year +
            getSuffix();
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

        CopticCalendar coptic = new CopticCalendar(gregorian);
        System.out.println(gregorian + ": " + coptic);
        coptic.set(i, j, k);
        System.out.println("CopticCalendar(" + i + "," + j + "," + k + "): " +
            coptic);
        System.out.println(coptic.toRD());
    }
}
