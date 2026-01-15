package org.jscience.devices.gps;

/**
 * This is a class meant for containing positions.
 */
public class Position implements IPosition {
    /** DOCUMENT ME! */
    private PositionRadians lat;

    /** DOCUMENT ME! */
    private PositionRadians lon;

/**
     * Makes a new position. Initializes the latitude and the longitude to 0.
     */
    public Position() {
        this(0, 0);
    }

/**
     * Initializes the Position with la as the latitude and lo as the
     * longitude.
     *
     * @param la DOCUMENT ME!
     * @param lo DOCUMENT ME!
     */
    public Position(double la, double lo) {
        lat = new PositionRadians(la);
        lon = new PositionRadians(lo);
    }

/**
     * Creates a new Position object.
     *
     * @param la DOCUMENT ME!
     * @param lo DOCUMENT ME!
     */
    public Position(PositionRadians la, PositionRadians lo) {
        lat = la;
        lon = lo;
    }

/**
     * Initializes the position object from an IPosition reference.
     *
     * @param pos DOCUMENT ME!
     */
    public Position(IPosition pos) {
        lat = pos.getLatitude();
        lon = pos.getLongitude();
    }

    /**
     * Sets the latitude of this position.
     *
     * @param l DOCUMENT ME!
     */
    public void setLatitude(PositionRadians l) {
        lat = l;
    }

    /**
     * Sets the longitude of this position.
     *
     * @param l DOCUMENT ME!
     */
    public void setLongitude(PositionRadians l) {
        lon = l;
    }

    /**
     * Returns the latitude of this position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude() {
        return lat;
    }

    /**
     * Returns the longitude of this position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude() {
        return lon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if ((lat == null) || (lon == null)) {
            return "error printing position packet, long or lat is null";
        }

        return "position[" + String.valueOf(lat.getDegrees()) + "' " +
        String.valueOf((int) lat.getMinutes()) + "\" x " +
        String.valueOf(lon.getDegrees()) + "' " +
        String.valueOf((int) lon.getMinutes()) + "\"]";
    }
}
