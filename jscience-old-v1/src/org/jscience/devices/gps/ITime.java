package org.jscience.devices.gps;

/**
 * This interface is implemented by all packets capable of returning the time
 * of day.
 */
public interface ITime {
    /**
     * Returns the hour of the day.
     *
     * @return DOCUMENT ME!
     */
    public int getHours();

    /**
     * Returns the minute of the hour.
     *
     * @return DOCUMENT ME!
     */
    public short getMinutes();

    /**
     * Returns the second of the minute.
     *
     * @return DOCUMENT ME!
     */
    public short getSeconds();
}
;
