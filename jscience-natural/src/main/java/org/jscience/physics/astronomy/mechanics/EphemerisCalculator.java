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

package org.jscience.physics.astronomy.mechanics;

import org.jscience.physics.astronomy.time.JulianDate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Frequency;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.PhysicalConstants;

/**
 * Low-precision planetary ephemeris calculator.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EphemerisCalculator {

    /**
     * Solar system body declaration with orbital elements at J2000.
     * Data loaded from planets.json.
     */
    public static class Planet {
        private static final List<Planet> REGISTRY = new ArrayList<>();
        private static final Map<String, Planet> BY_NAME = new HashMap<>();

        static {
            try {
                loadData();
            } catch (Throwable t) {
                System.err.println("Failed to load Ephemeris data: " + t.getMessage());
                t.printStackTrace();
            }
        }

        public static final Planet MERCURY = get("MERCURY");
        public static final Planet VENUS = get("VENUS");
        public static final Planet EARTH = get("EARTH");
        public static final Planet MARS = get("MARS");
        public static final Planet JUPITER = get("JUPITER");
        public static final Planet SATURN = get("SATURN");
        public static final Planet URANUS = get("URANUS");
        public static final Planet NEPTUNE = get("NEPTUNE");

        public String name;
        public Quantity<Length> a; // Semi-major axis
        public Real e; // Eccentricity (dimensionless)
        public Quantity<Angle> i; // Inclination
        public Quantity<Angle> omega; // Longitude of perihelion
        public Quantity<Angle> Omega; // Longitude of ascending node
        public Quantity<Angle> L0; // Mean longitude at J2000
        public Quantity<Frequency> n; // Mean motion (deg/day -> converted to correct unit, strictly Frequency isn't
                                      // speed, but close enough for now or use angular velocity)
        // Note: For simplicity and JSR-385 types sometimes AngularVelocity is missing
        // or tricky. defaults to frequency or just use 1/time.
        // Let's use specific generic if available, else Frequency.
        // Actually, let's keep n as Real representing degrees per day for internal
        // calculation convenience OR Quantity<Dimensionless> rate?
        // User asked for "Quantities (for moles, positions, temperature,
        // electronegativity...)".
        // Let's use Real for 'n' to minimize complexity in the calculation formula
        // which relies on day diffs.
        public Real nVal;
        public List<Moon> moons = new ArrayList<>();

        public Planet() {
        }

        // ... (existing setters) ...

        public static class Moon {
            public String name;
            public Quantity<Length> a;
            public Real e;
            public Quantity<Angle> i;
            public Quantity<Frequency> n; // or Real for deg/day
            public Real nVal;
            // Add other parameters as needed for basic moon orbit
        }

        // Setter for JSON mapping (Jackson uses setters or public fields)
        public void setName(String name) {
            this.name = name;
        }

        public void setA(double val) {
            this.a = Quantities.create(val * PhysicalConstants.AU.doubleValue(), org.jscience.measure.Units.METER);
        } // JSON assumes AU

        public void setE(double val) {
            this.e = Real.of(val);
        }

        public void setI(double val) {
            this.i = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        }

        public void setOmega_lower(double val) {
            this.omega = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        } // JSON field mapping might need check
          // The JSON likely uses "omega" and "Omega". Jackson distinguishes case if
          // configured.

        public void setOmega(double val) {
            this.omega = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        }

        public void setOmega_upper(double val) {
            this.Omega = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        }

        public void setNode(double val) {
            this.Omega = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        } // In case JSON uses 'node' or 'Omega'

        public void setL0(double val) {
            this.L0 = Quantities.create(val, org.jscience.measure.Units.DEGREE_ANGLE);
        }

        public void setN(double val) {
            this.nVal = Real.of(val);
        } // keeping as real deg/day for now

        private static void loadData() {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);
                InputStream is = EphemerisCalculator.class
                        .getResourceAsStream("/org/jscience/physics/astronomy/ephemeris/planets.json");
                if (is == null) {
                    System.err.println("Could not find planets.json");
                    return;
                }
                // We need to parse into a temporary DTO or ensure setters work.
                // Let's use a raw generic map or a DTO to avoid Jackson failing on complex
                // types without bindings.
                // Or simplified: define a POJO for loading.
                List<PlanetDTO> dtos = mapper.readValue(is, new TypeReference<List<PlanetDTO>>() {
                });
                for (PlanetDTO dto : dtos) {
                    Planet p = new Planet();
                    p.name = dto.name;
                    p.a = Quantities.create(dto.a * PhysicalConstants.AU.doubleValue(),
                            org.jscience.measure.Units.METER); // Assuming AU in JSON
                    p.e = Real.of(dto.e);
                    p.i = Quantities.create(dto.i, org.jscience.measure.Units.DEGREE_ANGLE);
                    p.omega = Quantities.create(dto.omega, org.jscience.measure.Units.DEGREE_ANGLE);
                    p.Omega = Quantities.create(dto.Omega, org.jscience.measure.Units.DEGREE_ANGLE);
                    p.L0 = Quantities.create(dto.L0, org.jscience.measure.Units.DEGREE_ANGLE);
                    p.nVal = Real.of(dto.n);

                    if (dto.moons != null) {
                        for (PlanetDTO.MoonDTO mDto : dto.moons) {
                            Moon m = new Moon();
                            m.name = mDto.name;
                            m.a = Quantities.create(mDto.a * PhysicalConstants.AU.doubleValue(),
                                    org.jscience.measure.Units.METER);
                            m.e = Real.of(mDto.e);
                            m.nVal = Real.of(mDto.n);
                            p.moons.add(m);
                        }
                    }

                    REGISTRY.add(p);
                    BY_NAME.put(p.name, p);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to load planets", e);
            }
        }

        public static Planet get(String name) {
            return BY_NAME.get(name);
        }

        public static List<Planet> values() {
            return Collections.unmodifiableList(REGISTRY);
        }
    }

    // DTO for JSON loading
    private static class PlanetDTO {
        public String name;
        public double a;
        public double e;
        public double i;
        public double omega;
        public double Omega;
        public double L0;
        public double n;
        public List<MoonDTO> moons;

        public static class MoonDTO {
            public String name;
            public double a;
            public double e;
            public double n;
        }

    }

    /**
     * Calculates heliocentric ecliptic coordinates for a planet.
     * 
     * @param planet Planet to calculate
     * @param jd     Julian Date
     * @return [longitude (deg), latitude (deg), distance (AU)]
     */
    /**
     * Calculates heliocentric position vector (in J2000 ecliptic frame).
     * 
     * @param planet Planet to calculate
     * @param jd     Julian Date
     * @return Vector<Real> [x, y, z] in AU
     */
    public static org.jscience.mathematics.linearalgebra.Vector<Real> heliocentricPositionVector(Planet planet,
            JulianDate jd) {
        double d = jd.getValue() - JulianDate.J2000;

        // Extract elements using Real directly for OrbitalElements

        double i = planet.i.to(org.jscience.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
        double Omega = planet.Omega.to(org.jscience.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
        double omega = planet.omega.to(org.jscience.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
        // L = Mean Longitude
        double L0 = planet.L0.to(org.jscience.measure.Units.DEGREE_ANGLE).getValue().doubleValue();
        double n = planet.nVal.doubleValue();

        double currentL = normalizeAngle(L0 + n * d);

        // M = L - varpi (approx) or M = L - (Omega + omega) if L is mean longitude
        // Note: definitions vary. Assuming L is mean longitude.
        // varpi = Omega + omega
        double varpi = Omega + omega;
        double M = normalizeAngle(currentL - varpi);

        // Standard Gravitational Parameter for Sun in AU^3/day^2 ?
        // Or we use OrbitalElements which works in SI usually?
        // OrbitalElements uses Real. If we pass SI units, we get SI output.
        // Let's stick to SI for internal calculation in OrbitalElements, then convert
        // to AU if needed.

        double muSI = 1.32712440018e20; // m^3/s^2 (Standard parameter for Sun)
        // Adjust inputs to SI (Radians for angles)

        Real aReal = planet.a.to(org.jscience.measure.Units.METER).getValue();
        Real eReal = planet.e;
        Real iReal = Real.of(i).toRadians();
        Real OmegaReal = Real.of(Omega).toRadians();
        Real omegaReal = Real.of(omega).toRadians();
        Real MReal = Real.of(M).toRadians();
        Real muReal = Real.of(muSI);

        org.jscience.physics.astronomy.OrbitalElements elem = new org.jscience.physics.astronomy.OrbitalElements(aReal,
                eReal, iReal, OmegaReal, omegaReal, MReal, muReal);

        Object[] state = elem.toStateVector();
        @SuppressWarnings("unchecked")
        org.jscience.mathematics.linearalgebra.Vector<Real> posSI = (org.jscience.mathematics.linearalgebra.Vector<Real>) state[0];

        // Convert Position from meters to AU for the return
        double xAU = posSI.get(0).doubleValue() / PhysicalConstants.AU.doubleValue();
        double yAU = posSI.get(1).doubleValue() / PhysicalConstants.AU.doubleValue();
        double zAU = posSI.get(2).doubleValue() / PhysicalConstants.AU.doubleValue();

        return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(
                java.util.Arrays.asList(Real.of(xAU), Real.of(yAU), Real.of(zAU)),
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Legacy support wrapper.
     */
    public static double[] heliocentricPosition(Planet planet, JulianDate jd) {
        org.jscience.mathematics.linearalgebra.Vector<Real> vec = heliocentricPositionVector(planet, jd);
        double x = vec.get(0).doubleValue();
        double y = vec.get(1).doubleValue();
        double z = vec.get(2).doubleValue();

        // Convert back to simple spherical for compatibility?
        // Or just return cartesian if usages allow?
        // Original returned [lon, lat, r].
        double r = Math.sqrt(x * x + y * y + z * z);
        double lon = Math.toDegrees(Math.atan2(y, x));
        double lat = Math.toDegrees(Math.asin(z / r));
        return new double[] { normalizeAngle(lon), lat, r };
    }

    /**
     * Calculates geocentric equatorial coordinates.
     * 
     * @param planet Planet
     * @param jd     Julian Date
     * @return [RA (hours), Dec (deg), distance (AU)]
     */
    public static double[] geocentricPosition(Planet planet, JulianDate jd) {
        double[] planetHelio = heliocentricPosition(planet, jd);
        double[] earthHelio = heliocentricPosition(Planet.EARTH, jd);

        // Convert to rectangular
        double lonP = Math.toRadians(planetHelio[0]);
        double lonE = Math.toRadians(earthHelio[0]);
        double rP = planetHelio[2];
        double rE = earthHelio[2];

        double xP = rP * Math.cos(lonP);
        double yP = rP * Math.sin(lonP);
        double xE = rE * Math.cos(lonE);
        double yE = rE * Math.sin(lonE);

        // Geocentric ecliptic
        double x = xP - xE;
        double y = yP - yE;
        double z = 0; // Simplified

        double dist = Math.sqrt(x * x + y * y + z * z);
        double lon = Math.toDegrees(Math.atan2(y, x));
        lon = normalizeAngle(lon);

        // Convert ecliptic to equatorial (obliquity ~23.44 deg)
        double eps = Math.toRadians(23.44);
        double lonR = Math.toRadians(lon);
        double latR = 0; // simplified lat

        double ra = Math.atan2(Math.sin(lonR) * Math.cos(eps), Math.cos(lonR));
        double dec = Math.asin(Math.sin(latR) * Math.cos(eps)
                + Math.cos(latR) * Math.sin(eps) * Math.sin(lonR));

        return new double[] { normalizeAngle(Math.toDegrees(ra)) / 15.0, Math.toDegrees(dec), dist };
    }

    /**
     * Calculates Sun position in geocentric equatorial coordinates.
     * 
     * @param jd Julian Date
     * @return [RA (hours), Dec (deg)]
     */
    public static double[] sunPosition(JulianDate jd) {
        double d = jd.getValue() - JulianDate.J2000;

        // Mean anomaly of Sun
        double M = normalizeAngle(357.529 + 0.98560028 * d);
        double Mr = Math.toRadians(M);

        // Equation of center
        double C = 1.9148 * Math.sin(Mr) + 0.02 * Math.sin(2 * Mr);

        // Ecliptic longitude
        double lon = normalizeAngle(M + C + 180 + 102.9372);
        double lonR = Math.toRadians(lon);

        // Convert to RA/Dec
        double eps = Math.toRadians(23.44);
        double ra = Math.atan2(Math.sin(lonR) * Math.cos(eps), Math.cos(lonR));
        double dec = Math.asin(Math.sin(eps) * Math.sin(lonR));

        return new double[] { normalizeAngle(Math.toDegrees(ra)) / 15.0,
                Math.toDegrees(dec) };
    }

    /**
     * Calculates Moon position (low precision, ~1Ã‚Â° accuracy).
     */
    public static double[] moonPosition(JulianDate jd) {
        double d = jd.getValue() - JulianDate.J2000;

        // Mean elements
        double L = normalizeAngle(218.316 + 13.176396 * d); // Mean longitude
        double M = normalizeAngle(134.963 + 13.064993 * d); // Mean anomaly
        double F = normalizeAngle(93.272 + 13.229350 * d); // Argument of latitude

        double Mr = Math.toRadians(M);
        double Fr = Math.toRadians(F);

        // Ecliptic longitude
        double lon = L + 6.289 * Math.sin(Mr);
        double lat = 5.128 * Math.sin(Fr);

        double lonR = Math.toRadians(lon);
        double latR = Math.toRadians(lat);

        // Convert to RA/Dec
        double eps = Math.toRadians(23.44);
        double ra = Math.atan2(Math.sin(lonR) * Math.cos(eps)
                - Math.tan(latR) * Math.sin(eps), Math.cos(lonR));
        double dec = Math.asin(Math.sin(latR) * Math.cos(eps)
                + Math.cos(latR) * Math.sin(eps) * Math.sin(lonR));

        return new double[] { normalizeAngle(Math.toDegrees(ra)) / 15.0,
                Math.toDegrees(dec) };
    }

    private static double normalizeAngle(double deg) {
        deg = deg % 360.0;
        return deg < 0 ? deg + 360.0 : deg;
    }
}


