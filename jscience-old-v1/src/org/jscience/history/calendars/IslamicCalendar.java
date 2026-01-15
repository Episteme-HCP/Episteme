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
public class IslamicCalendar extends MonthDayYear {
    /** DOCUMENT ME! */
    public static long EPOCH = (new JulianCalendar(7, 16, 622)).toRD();

    /** DOCUMENT ME! */
    private static final String[] MONTHS = {
            "Muharram", "Safar", "Rabi` I", "Rabi` II", "Jumada I", "Jumada II",
            "Rajab", "Sha`ban", "Ramadan", "Shawwal", "Dhu al-Qa`da",
            "Dhu al-Hijja"
        };

/**
     * Creates a new IslamicCalendar object.
     */
    public IslamicCalendar() {
        this(EPOCH);
    }

/**
     * Creates a new IslamicCalendar object.
     *
     * @param l DOCUMENT ME!
     */
    public IslamicCalendar(long l) {
        set(l);
    }

/**
     * Creates a new IslamicCalendar object.
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public IslamicCalendar(int i, int j, int k) {
        set(i, j, k);
    }

/**
     * Creates a new IslamicCalendar object.
     *
     * @param altcalendar DOCUMENT ME!
     */
    public IslamicCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeRD() {
        super.rd = ((long) super.day +
            (long) Math.ceil(29.5D * (double) (super.month - 1)) +
            (long) ((super.year - 1) * 354) +
            AlternateCalendar.fldiv(3 + (11 * super.year), 30L) + EPOCH) - 1L;
    }

    /**
     * DOCUMENT ME!
     */
    protected synchronized void recomputeFromRD() {
        super.year = (int) AlternateCalendar.fldiv((30L * (super.rd - EPOCH)) +
                10646L, 10631L);

        int i = (int) Math.ceil((double) (super.rd - 29L -
                (new IslamicCalendar(1, 1, super.year)).toRD()) / 29.5D) + 1;
        super.month = (i >= 12) ? 12 : i;
        super.day = (int) (super.rd -
            (new IslamicCalendar(super.month, 1, super.year)).toRD()) + 1;
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
        return MONTHS[super.month - 1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getSuffix() {
        return " A.H.";
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

        IslamicCalendar islamic = new IslamicCalendar(gregorian);
        System.out.println(gregorian + ": " + islamic);
        islamic.set(i, j, k);
        System.out.println("IslamicCalendar(" + i + "," + j + "," + k + "): " +
            islamic);
        System.out.println(islamic.toRD());
    }
}
