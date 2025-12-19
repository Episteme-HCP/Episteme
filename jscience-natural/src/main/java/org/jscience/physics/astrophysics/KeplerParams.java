package org.jscience.physics.astrophysics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;

/**
 * Represents the standard Keplerian orbital elements.
 */
public class KeplerParams {

    private final Quantity<Length> semiMajorAxis; // a
    private final double eccentricity; // e
    private final Quantity<Angle> inclination; // i
    private final Quantity<Angle> longitudeAscendingNode; // Omega
    private final Quantity<Angle> argumentPeriapsis; // omega
    // trueAnomaly or meanAnomaly depends on state, usually kept separate

    public KeplerParams(Quantity<Length> semiMajorAxis, double eccentricity,
            Quantity<Angle> inclination, Quantity<Angle> longitudeAscendingNode,
            Quantity<Angle> argumentPeriapsis) {
        this.semiMajorAxis = semiMajorAxis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.longitudeAscendingNode = longitudeAscendingNode;
        this.argumentPeriapsis = argumentPeriapsis;
    }

    public Quantity<Length> getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public Quantity<Angle> getInclination() {
        return inclination;
    }

    public Quantity<Angle> getLongitudeAscendingNode() {
        return longitudeAscendingNode;
    }

    public Quantity<Angle> getArgumentPeriapsis() {
        return argumentPeriapsis;
    }

    @Override
    public String toString() {
        return "KeplerParams{" +
                "a=" + semiMajorAxis +
                ", e=" + eccentricity +
                ", i=" + inclination +
                '}';
    }
}
