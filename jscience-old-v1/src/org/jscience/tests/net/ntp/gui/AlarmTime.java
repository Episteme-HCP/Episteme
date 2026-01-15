package org.jscience.tests.net.ntp.gui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class AlarmTime {
    /** DOCUMENT ME! */
    private int hour;

    /** DOCUMENT ME! */
    private int min;

    /** DOCUMENT ME! */
    private int sec;

    /** DOCUMENT ME! */
    private Calendar c = new GregorianCalendar();

/**
     * Creates a new AlarmTime object.
     *
     * @param hour DOCUMENT ME!
     * @param min  DOCUMENT ME!
     * @param sec  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public AlarmTime(int hour, int min, int sec)
        throws IllegalArgumentException {
        setHour(hour);
        setMin(min);
        setSec(sec);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHour() {
        return hour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMin() {
        return min;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSec() {
        return sec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hour DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalHour(int hour) {
        return (hour >= 0) && (hour <= 23);
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalMin(int min) {
        return (min >= 0) && (min <= 59);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sec DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isLegalSec(int sec) {
        return (sec >= 0) && (sec <= 59);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hour DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setHour(int hour) throws IllegalArgumentException {
        try {
            if (isLegalHour(hour)) {
                this.hour = hour;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setMin(int min) throws IllegalArgumentException {
        try {
            if (isLegalMin(min)) {
                this.min = min;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sec DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSec(int sec) throws IllegalArgumentException {
        try {
            if (isLegalSec(sec)) {
                this.sec = sec;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAlarm(Date d) {
        c.setTime(d);

        return (hour == c.get(Calendar.HOUR_OF_DAY)) &&
        (min == c.get(Calendar.MINUTE)) && (sec == c.get(Calendar.SECOND));
    }
}
