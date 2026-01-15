package org.jscience.devices.gps;

/**
 * This interface is implemented by all packets capable of returning a date.
 */
public interface IDate {
    /**
     * Returns the day of the month.
     *
     * @return DOCUMENT ME!
     */
    public short getDay();

    /**
     * Returns the month.
     *
     * @return DOCUMENT ME!
     */
    public short getMonth();

    /**
     * returns the year.
     *
     * @return DOCUMENT ME!
     */
    public int getYear();
}
;
