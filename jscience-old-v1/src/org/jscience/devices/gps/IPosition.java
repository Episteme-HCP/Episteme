package org.jscience.devices.gps;

/**
 * This interface is implemented by all packets capable of returning a
 * position.
 */
public interface IPosition {
    /**
     * This method returns the latitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude();

    /**
     * This method returns the longitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude();
}
;
