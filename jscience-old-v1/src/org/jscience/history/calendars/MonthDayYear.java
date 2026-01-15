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
public abstract class MonthDayYear extends SevenDaysWeek {
    /** DOCUMENT ME! */
    protected int month;

    /** DOCUMENT ME! */
    protected int day;

    /** DOCUMENT ME! */
    protected int year;

/**
     * Creates a new MonthDayYear object.
     */
    public MonthDayYear() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDay() {
        return day;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMonth() {
        return month;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getYear() {
        return year;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    public synchronized void set(int i, int j, int k) {
        month = i;
        day = j;
        year = k;
        recomputeRD();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        try {
            return day + " " + monthName() + " " + year + getSuffix();
        } catch (ArrayIndexOutOfBoundsException _ex) {
            return "Invalid date: " + month + " " + day + " " + year;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract String monthName();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract String getSuffix();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Enumeration getMonths();
}
