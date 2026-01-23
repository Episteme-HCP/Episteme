/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.geography;

import java.util.Objects;
import java.util.Random;

/**
 * A boundary with uncertainty or probability distribution.
 * <p>
 * FuzzyBoundary extends the standard Boundary concept to include spatial
 * uncertainty. This is useful for:
 * </p>
 * <ul>
 *   <li>Historical locations where exact positions are unknown</li>
 *   <li>Natural phenomena with indeterminate edges (e.g., forest boundaries)</li>
 *   <li>Mobile entities with position uncertainty</li>
 *   <li>Archaeological sites with estimated locations</li>
 * </ul>
 * <p>
 * The uncertainty is modeled as a Gaussian distribution around the nominal
 * boundary, with configurable standard deviation.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 * @since 2.0
 */
public class FuzzyBoundary extends Boundary {

    private static final long serialVersionUID = 1L;

    /**
     * Types of uncertainty distribution.
     */
    public enum UncertaintyType {
        /** Gaussian (normal) distribution */
        GAUSSIAN,
        /** Uniform distribution within radius */
        UNIFORM,
        /** Triangular distribution (peak at center) */
        TRIANGULAR
    }

    /** Standard deviation of uncertainty in meters. */
    private final double uncertaintyMeters;

    /** The type of uncertainty distribution. */
    private final UncertaintyType uncertaintyType;

    /** Confidence level (0-1) that the actual boundary is within the uncertainty. */
    private final double confidence;

    /** Optional description of the uncertainty source. */
    private final String uncertaintySource;

    /**
     * Creates a fuzzy boundary with Gaussian uncertainty.
     *
     * @param boundary          the nominal (central) boundary
     * @param uncertaintyMeters the standard deviation in meters
     */
    public FuzzyBoundary(Boundary boundary, double uncertaintyMeters) {
        super(boundary.getCoordinates(), boundary.getEdgesIncluded());
        this.uncertaintyMeters = Math.abs(uncertaintyMeters);
        this.uncertaintyType = UncertaintyType.GAUSSIAN;
        this.confidence = 0.68; // 1 sigma for Gaussian
        this.uncertaintySource = null;
    }

    /**
     * Creates a fuzzy point boundary (single coordinate with uncertainty).
     *
     * @param coordinate        the nominal location
     * @param uncertaintyMeters the uncertainty radius in meters
     */
    public FuzzyBoundary(Coordinate coordinate, double uncertaintyMeters) {
        super(coordinate);
        this.uncertaintyMeters = Math.abs(uncertaintyMeters);
        this.uncertaintyType = UncertaintyType.GAUSSIAN;
        this.confidence = 0.68;
        this.uncertaintySource = null;
    }

    /**
     * Creates a fully specified fuzzy boundary.
     *
     * @param coordinates       the nominal boundary coordinates
     * @param uncertaintyMeters the uncertainty in meters
     * @param type              the uncertainty distribution type
     * @param confidence        the confidence level (0-1)
     * @param source            description of uncertainty source
     */
    public FuzzyBoundary(Coordinate[] coordinates, double uncertaintyMeters,
                         UncertaintyType type, double confidence, String source) {
        super(coordinates);
        this.uncertaintyMeters = Math.abs(uncertaintyMeters);
        this.uncertaintyType = Objects.requireNonNull(type);
        this.confidence = Math.max(0, Math.min(1, confidence));
        this.uncertaintySource = source;
    }

    /**
     * Returns the uncertainty in meters.
     *
     * @return uncertainty (standard deviation for Gaussian, radius for others)
     */
    public double getUncertaintyMeters() {
        return uncertaintyMeters;
    }

    /**
     * Returns the uncertainty type.
     *
     * @return uncertainty distribution type
     */
    public UncertaintyType getUncertaintyType() {
        return uncertaintyType;
    }

    /**
     * Returns the confidence level.
     *
     * @return confidence (0-1)
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Returns the source of uncertainty information.
     *
     * @return source description, or null
     */
    public String getUncertaintySource() {
        return uncertaintySource;
    }

    /**
     * Returns the 95% confidence radius in meters.
     * For Gaussian, this is approximately 2 sigma.
     *
     * @return 95% confidence radius
     */
    public double get95ConfidenceRadius() {
        switch (uncertaintyType) {
            case GAUSSIAN:
                return uncertaintyMeters * 1.96; // 95% for normal distribution
            case UNIFORM:
            case TRIANGULAR:
            default:
                return uncertaintyMeters;
        }
    }

    /**
     * Generates a sample point within the uncertainty region.
     * Useful for Monte Carlo simulations.
     *
     * @param random the random number generator
     * @return a sampled coordinate within uncertainty bounds
     */
    public Coordinate samplePoint(Random random) {
        Coordinate center = getCentroid();
        if (center == null) {
            return null;
        }

        double distanceMeters;
        double bearingRadians = random.nextDouble() * 2 * Math.PI;

        switch (uncertaintyType) {
            case GAUSSIAN:
                distanceMeters = Math.abs(random.nextGaussian() * uncertaintyMeters);
                break;
            case TRIANGULAR:
                double u = random.nextDouble();
                distanceMeters = uncertaintyMeters * (1.0 - Math.sqrt(1.0 - u));
                break;
            case UNIFORM:
            default:
                distanceMeters = random.nextDouble() * uncertaintyMeters;
                break;
        }

        // Calculate offset point
        double earthRadius = 6371000;
        double angularDistance = distanceMeters / earthRadius;

        double lat1 = Math.toRadians(center.getLatitudeDegrees().doubleValue());
        double lon1 = Math.toRadians(center.getLongitudeDegrees().doubleValue());

        double lat2 = Math.asin(
            Math.sin(lat1) * Math.cos(angularDistance) +
            Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearingRadians)
        );
        double lon2 = lon1 + Math.atan2(
            Math.sin(bearingRadians) * Math.sin(angularDistance) * Math.cos(lat1),
            Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2)
        );

        return new Coordinate(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    /**
     * Computes the probability that a given point is within this fuzzy boundary.
     *
     * @param point the point to test
     * @return probability (0-1)
     */
    public double probabilityContains(Coordinate point) {
        Coordinate center = getCentroid();
        if (center == null || point == null) {
            return 0.0;
        }

        double distance = center.distanceTo(point).getValue().doubleValue();

        switch (uncertaintyType) {
            case GAUSSIAN:
                // Probability from Gaussian distribution
                double z = distance / uncertaintyMeters;
                return Math.exp(-0.5 * z * z);
            case UNIFORM:
                return distance <= uncertaintyMeters ? 1.0 : 0.0;
            case TRIANGULAR:
                if (distance > uncertaintyMeters) return 0.0;
                return 1.0 - (distance / uncertaintyMeters);
            default:
                return 0.0;
        }
    }

    @Override
    public boolean contains(Coordinate point) {
        // For fuzzy boundaries, we check against nominal + 2 sigma
        Coordinate center = getCentroid();
        if (center == null || point == null) {
            return false;
        }

        double distance = center.distanceTo(point).getValue().doubleValue();
        return distance <= get95ConfidenceRadius();
    }

    @Override
    public String toString() {
        if (isPoint()) {
            return String.format("FuzzyPoint[%s ± %.0fm (%s)]",
                getCentroid(), uncertaintyMeters, uncertaintyType);
        }
        return String.format("FuzzyBoundary[%d vertices ± %.0fm (%s)]",
            getVertexCount(), uncertaintyMeters, uncertaintyType);
    }

    // ===== Factory methods =====

    /**
     * Creates a fuzzy point with uniform uncertainty.
     *
     * @param lat            latitude
     * @param lon            longitude
     * @param radiusMeters   uncertainty radius
     * @return fuzzy point boundary
     */
    public static FuzzyBoundary uniformPoint(double lat, double lon, double radiusMeters) {
        return new FuzzyBoundary(
            new Coordinate[] { new Coordinate(lat, lon) },
            radiusMeters, UncertaintyType.UNIFORM, 1.0, null
        );
    }

    /**
     * Creates a fuzzy point with Gaussian uncertainty.
     *
     * @param lat            latitude
     * @param lon            longitude
     * @param sigmaMeters    standard deviation in meters
     * @return fuzzy point boundary
     */
    public static FuzzyBoundary gaussianPoint(double lat, double lon, double sigmaMeters) {
        return new FuzzyBoundary(
            new Coordinate[] { new Coordinate(lat, lon) },
            sigmaMeters, UncertaintyType.GAUSSIAN, 0.68, null
        );
    }

    /**
     * Creates a fuzzy boundary for historical location.
     *
     * @param lat            estimated latitude
     * @param lon            estimated longitude
     * @param uncertaintyKm  uncertainty in kilometers
     * @param source         historical source
     * @return fuzzy boundary
     */
    public static FuzzyBoundary historical(double lat, double lon, double uncertaintyKm, String source) {
        return new FuzzyBoundary(
            new Coordinate[] { new Coordinate(lat, lon) },
            uncertaintyKm * 1000, UncertaintyType.UNIFORM, 0.9, source
        );
    }
}
