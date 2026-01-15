/**
 * \ VisualLimitAngularBrightnessData \
 */
package org.jscience.astronomy.solarsystem;

/**
 * A support class for VisualLimit.
 * <p/>
 * <p/>
 * Holds values which vary across the sky
 * </p>
 */
public class VisualLimitAngularBrightnessData {
    /**
     * The zenith angle
     */
    private double zenithAngle;

    /**
     * The lunar angular distance
     */
    private double distMoon;

    /**
     * The solar angular distance
     */
    private double distSun;

    /**
     * Creates a new VisualLimitAngularBrightnessData object.
     */
    public VisualLimitAngularBrightnessData() {
        zenithAngle = 0D;
        distMoon = 0D;
        distSun = 0D;
    }

    /**
     * Creates a new VisualLimitAngularBrightnessData object.
     *
     * @param za DOCUMENT ME!
     * @param dm DOCUMENT ME!
     * @param ds DOCUMENT ME!
     */
    public VisualLimitAngularBrightnessData(double za, double dm, double ds) {
        zenithAngle = za;
        distMoon = dm;
        distSun = ds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZenithAngle() {
        return zenithAngle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param zenithAngle DOCUMENT ME!
     */
    public void setZenithAngle(double zenithAngle) {
        this.zenithAngle = zenithAngle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMoonAngularDistance() {
        return distMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param distMoon DOCUMENT ME!
     */
    public void setMoonAngularDistance(double distMoon) {
        this.distMoon = distMoon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSunAngularDistance() {
        return distSun;
    }

    /**
     * DOCUMENT ME!
     *
     * @param distSun DOCUMENT ME!
     */
    public void setSunAngularDistance(double distSun) {
        this.distSun = distSun;
    }
}
