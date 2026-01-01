/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/**
 * Represents a geographic location with uncertainty.
 * <p>
 * Historical and archaeological locations often have imprecise coordinates.
 * FuzzyLocation supports:
 * <ul>
 * <li>Exact coordinates (latitude/longitude)</li>
 * <li>Named regions and areas</li>
 * <li>Uncertainty radius</li>
 * <li>Historical boundaries that may have changed</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyLocation {

    /**
     * Precision levels for geographic locations.
     */
    public enum Precision {
        EXACT, // Known to meter-level
        APPROXIMATE, // Within a few kilometers
        CITY, // City-level precision
        REGION, // Regional area
        COUNTRY, // Country-level
        CONTINENT, // Continental
        UNKNOWN // Unknown location
    }

    private final Double latitude;
    private final Double longitude;
    private final double uncertaintyKm; // Radius of uncertainty
    private final String name;
    private final String region;
    private final String country;
    private final Precision precision;
    private final String historicalName; // Name at a historical period

    private FuzzyLocation(Double latitude, Double longitude, double uncertaintyKm,
            String name, String region, String country,
            Precision precision, String historicalName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.uncertaintyKm = uncertaintyKm;
        this.name = name;
        this.region = region;
        this.country = country;
        this.precision = precision;
        this.historicalName = historicalName;
    }

    // ========== Factory Methods ==========

    /**
     * Creates an exact location.
     */
    public static FuzzyLocation of(double latitude, double longitude) {
        return new FuzzyLocation(latitude, longitude, 0.0, null, null, null,
                Precision.EXACT, null);
    }

    /**
     * Creates a named location with coordinates.
     */
    public static FuzzyLocation of(String name, double latitude, double longitude) {
        return new FuzzyLocation(latitude, longitude, 0.0, name, null, null,
                Precision.EXACT, null);
    }

    /**
     * Creates an approximate location with uncertainty.
     */
    public static FuzzyLocation approximate(double latitude, double longitude, double uncertaintyKm) {
        return new FuzzyLocation(latitude, longitude, uncertaintyKm, null, null, null,
                Precision.APPROXIMATE, null);
    }

    /**
     * Creates a city-level location.
     */
    public static FuzzyLocation city(String cityName, String country) {
        return new FuzzyLocation(null, null, 10.0, cityName, null, country,
                Precision.CITY, null);
    }

    /**
     * Creates a region-level location.
     */
    public static FuzzyLocation region(String regionName, String country) {
        return new FuzzyLocation(null, null, 100.0, null, regionName, country,
                Precision.REGION, null);
    }

    /**
     * Creates a country-level location.
     */
    public static FuzzyLocation country(String country) {
        return new FuzzyLocation(null, null, 500.0, null, null, country,
                Precision.COUNTRY, null);
    }

    /**
     * Creates an unknown location.
     */
    public static FuzzyLocation unknown() {
        return new FuzzyLocation(null, null, 0.0, null, null, null,
                Precision.UNKNOWN, null);
    }

    /**
     * Creates a historical location with modern and historical names.
     */
    public static FuzzyLocation historical(String modernName, String historicalName,
            double latitude, double longitude) {
        return new FuzzyLocation(latitude, longitude, 0.0, modernName, null, null,
                Precision.EXACT, historicalName);
    }

    // ========== Getters ==========

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public double getUncertaintyKm() {
        return uncertaintyKm;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public Precision getPrecision() {
        return precision;
    }

    public String getHistoricalName() {
        return historicalName;
    }

    public boolean hasCoordinates() {
        return latitude != null && longitude != null;
    }

    public boolean isKnown() {
        return precision != Precision.UNKNOWN;
    }

    /**
     * Calculates approximate distance to another location in kilometers.
     * Uses Haversine formula.
     *
     * @param other the other location
     * @return distance in km, or NaN if coordinates unavailable
     */
    public double distanceTo(FuzzyLocation other) {
        if (!this.hasCoordinates() || !other.hasCoordinates()) {
            return Double.NaN;
        }

        double R = 6371.0; // Earth radius in km
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Checks if this location could overlap with another considering uncertainty.
     */
    public boolean couldOverlap(FuzzyLocation other) {
        if (!this.hasCoordinates() || !other.hasCoordinates()) {
            return false;
        }
        double distance = distanceTo(other);
        return distance <= (this.uncertaintyKm + other.uncertaintyKm);
    }

    @Override
    public String toString() {
        if (precision == Precision.UNKNOWN) {
            return "Unknown location";
        }

        StringBuilder sb = new StringBuilder();

        if (name != null) {
            sb.append(name);
            if (historicalName != null) {
                sb.append(" (historically: ").append(historicalName).append(")");
            }
        } else if (region != null) {
            sb.append(region);
        }

        if (country != null) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(country);
        }

        if (hasCoordinates()) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(String.format("[%.4fÃ‚Â°, %.4fÃ‚Â°]", latitude, longitude));
            if (uncertaintyKm > 0) {
                sb.append(String.format(" Ã‚Â±%.1f km", uncertaintyKm));
            }
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FuzzyLocation other))
            return false;
        return Objects.equals(latitude, other.latitude) &&
                Objects.equals(longitude, other.longitude) &&
                Objects.equals(name, other.name) &&
                Objects.equals(country, other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, name, country);
    }
}


