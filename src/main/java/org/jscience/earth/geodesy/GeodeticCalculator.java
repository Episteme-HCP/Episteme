package org.jscience.earth.geodesy;

import org.jscience.earth.coordinates.GeodeticCoordinate;
import org.jscience.earth.coordinates.ReferenceEllipsoid;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Performs geodetic calculations on a Reference Ellipsoid.
 * <p>
 * Implements Vincenty's formulae for high-precision distance and azimuth
 * calculations.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class GeodeticCalculator {

    private static final double ITERATION_LIMIT = 100;
    private static final double CONVERGENCE_THRESHOLD = 1e-12;

    /**
     * Calculates the geodetic distance between two coordinates on the surface of
     * the ellipsoid.
     * 
     * @param start starting coordinate
     * @param end   ending coordinate
     * @return the distance in Meters
     * @throws IllegalArgumentException if coordinates use different ellipsoids
     */
    public static Quantity<Length> calculateDistance(GeodeticCoordinate start, GeodeticCoordinate end) {
        if (!start.getEllipsoid().getCode().equals(end.getEllipsoid().getCode())) {
            throw new IllegalArgumentException("Coordinates must be on the same reference ellipsoid");
        }

        ReferenceEllipsoid ellipsoid = start.getEllipsoid();
        double a = ellipsoid.getSemiMajorAxis().to(Units.METER).getValue().doubleValue();
        double b = ellipsoid.getSemiMinorAxis().to(Units.METER).getValue().doubleValue();
        double f = ellipsoid.getFlattening().doubleValue();

        double phi1 = start.getLatitude().asType(org.jscience.measure.quantity.Dimensionless.class).to(Units.RADIAN)
                .getValue().doubleValue();
        double lambda1 = start.getLongitude().asType(org.jscience.measure.quantity.Dimensionless.class).to(Units.RADIAN)
                .getValue().doubleValue();
        double phi2 = end.getLatitude().asType(org.jscience.measure.quantity.Dimensionless.class).to(Units.RADIAN)
                .getValue().doubleValue();
        double lambda2 = end.getLongitude().asType(org.jscience.measure.quantity.Dimensionless.class).to(Units.RADIAN)
                .getValue().doubleValue();

        double U1 = Math.atan((1 - f) * Math.tan(phi1));
        double U2 = Math.atan((1 - f) * Math.tan(phi2));
        double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
        double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

        double L = lambda2 - lambda1;
        double lambda = L;

        double sigma = 0, sinSigma = 0, cosSigma = 0;
        double cosSqAlpha = 0, cos2SigmaM = 0;

        for (int i = 0; i < ITERATION_LIMIT; i++) {
            double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);

            sinSigma = Math.sqrt(
                    (cosU2 * sinLambda) * (cosU2 * sinLambda) +
                            (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));

            if (sinSigma == 0)
                return org.jscience.measure.Quantities.create(0, Units.METER); // Co-incident points

            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
            sigma = Math.atan2(sinSigma, cosSigma);

            double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
            cosSqAlpha = 1 - sinAlpha * sinAlpha;

            cos2SigmaM = (cosSqAlpha != 0) ? (cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha) : 0;

            double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
            double lambdaPrev = lambda;
            lambda = L + (1 - C) * f * sinAlpha
                    * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

            if (Math.abs(lambda - lambdaPrev) < CONVERGENCE_THRESHOLD)
                break;
        }

        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
                - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

        double dist = b * A * (sigma - deltaSigma);

        return org.jscience.measure.Quantities.create(dist, Units.METER);
    }
}
