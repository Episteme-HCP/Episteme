package org.jscience.devices.gps;

/**
 * This interface is used to receive notification each time the GPS transmits
 * one of the common data, ie. position, time and date.
 * <p/>
 * <ul>
 * <li>
 * The GPS does not necessarily transmit these things periodially by itself!
 * Some GPS-units needs a request before transmitting anything. Use the method
 * GPS.setAutoTransmission(true) if you want the GPS to periodically send this
 * data.
 * </li>
 * <li>
 * Don't perform any long calculations or big operations in these methods.
 * They're called by a dispatching thread, and putting it to too much work
 * will slow performance on the communication with the GPS.
 * </li>
 * </ul>
 */
public interface IGPSlistener {
    /**
     * Invoked when the GPS transmits time-data.
     *
     * @param t DOCUMENT ME!
     */
    public void timeReceived(ITime t);

    /**
     * Invoked when the GPS transmits date-data.
     *
     * @param d DOCUMENT ME!
     */
    public void dateReceived(IDate d);

    /**
     * Invoked when the GPS transmits position-data.
     *
     * @param pos DOCUMENT ME!
     */
    public void positionReceived(IPosition pos);
}
