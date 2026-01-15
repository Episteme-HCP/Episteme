package org.jscience.devices.gps;

/**
 * Class used to store radians, usually latitude or longitude. Contains
 * methods for converting to the format degress,minutes.
 */
public class PositionRadians {
    /** DOCUMENT ME! */
    private final double value;

/**
     * Initializes the PositionRadians-object. After the object is constructed,
     * it can't change is value.
     *
     * @param v DOCUMENT ME!
     */
    public PositionRadians(double v) {
        value = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRadians() {
        return value;
    }

    /**
     * Returns the degrees part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public int getDegrees() {
        return (int) (value * (180.0d / Math.PI));
    }

    /**
     * Returns the minutes part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public double getMinutes() {
        double v = value * (180.0d / Math.PI);
        v -= getDegrees();

        return 60 * v; // 60 minutes in one degree.
    }

    /**
     * Tests if the two PositionRadians contains the same value.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(PositionRadians p) {
        if (value == p.value) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if this PositionRadians is greater than p.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean greaterThan(PositionRadians p) {
        if (value > p.value) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if this PositionRadians is smaller than p.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean smallerThan(PositionRadians p) {
        if (value < p.value) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return String.valueOf(getDegrees()) + "\' " +
        String.valueOf((int) getMinutes()) + "\"";
    }
}
