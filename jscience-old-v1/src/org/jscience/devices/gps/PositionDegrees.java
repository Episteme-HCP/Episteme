package org.jscience.devices.gps;

/**
 * Class used to store degrees, usually latitude or longitude.
 */
public class PositionDegrees {
    /** DOCUMENT ME! */
    protected double value;

/**
     * Creates a new PositionDegrees object.
     *
     * @param v DOCUMENT ME!
     */
    public PositionDegrees(double v) {
        value = v;
    }

    /**
     * Returns the degrees part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public int getDegrees() {
        return (int) value;
    }

    /**
     * Converts the degrees to Radians.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians convertToRadians() {
        return new PositionRadians((value * Math.PI) / 180.0d);
    }

    /**
     * Returns the minutes part of this object, when converted to
     * coordinates.
     *
     * @return DOCUMENT ME!
     */
    public double getMinutes() {
        double v = value;
        v -= getDegrees();

        return 60 * v; // 60 minutes in one degree.
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
