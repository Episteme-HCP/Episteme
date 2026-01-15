//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
package org.jscience.history.calendars;


// Referenced classes of package calendars:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public abstract class SevenDaysWeek extends AlternateCalendar {
    /** DOCUMENT ME! */
    public static final int SUNDAY = 0;

    /** DOCUMENT ME! */
    public static final int MONDAY = 1;

    /** DOCUMENT ME! */
    public static final int TUESDAY = 2;

    /** DOCUMENT ME! */
    public static final int WEDNESDAY = 3;

    /** DOCUMENT ME! */
    public static final int THURSDAY = 4;

    /** DOCUMENT ME! */
    public static final int FRIDAY = 5;

    /** DOCUMENT ME! */
    public static final int SATURDAY = 6;

    /** DOCUMENT ME! */
    public static final String[] DAYNAMES = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"
        };

/**
     * Creates a new SevenDaysWeek object.
     */
    public SevenDaysWeek() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int weekDay() {
        return AlternateCalendar.mod(toRD(), 7);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int weekDay(long l) {
        return AlternateCalendar.mod(l, 7);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void kDayOnOrBefore(int i) {
        long l = toRD();
        subtract(weekDay(l - (long) i));
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void kDayOnOrAfter(int i) {
        add(6L);
        kDayOnOrBefore(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void kDayNearest(int i) {
        add(3L);
        kDayOnOrBefore(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void kDayAfter(int i) {
        add(7L);
        kDayOnOrBefore(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void kDayBefore(int i) {
        add(1L);
        kDayOnOrBefore(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    public void nthKDay(int i, int j) {
        if (i < 0) {
            kDayBefore(j);
        } else {
            kDayAfter(j);
        }

        add(7 * i);
    }
}
