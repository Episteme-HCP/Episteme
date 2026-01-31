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

package org.jscience.social.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Analytical tool for calculating the solar path, sun position, and solar 
 * insulation on building surfaces. It supports architectural site analysis, 
 * shading evaluation, and renewable energy planning.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class SolarPathAnalyzer {

    private SolarPathAnalyzer() {}

    /**
     * Represents the position of the sun in the sky at a specific moment.
     */
    public record SolarPosition(
        double altitude,    // degrees above horizon
        double azimuth,     // degrees from north (clockwise)
        double julianDay,
        double hourAngle
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Results of a 24-hour solar insulation simulation.
     */
    public record InsolationResult(
        double dailyTotal,           // kWh/mÂ²
        double[] hourlyValues,       // W/mÂ² for each hour
        double peakHour,
        double peakValue
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the sun position (altitude and azimuth) for a given location, 
     * date, and time.
     * 
     * @param latitude degrees (-90 to 90)
     * @param longitude degrees (-180 to 180)
     * @param year calendar year
     * @param month month (1-12)
     * @param day day of month
     * @param hour hour of day (0.0 to 24.0)
     * @param timezone UTC offset
     * @return SolarPosition record
     */
    public static SolarPosition calculateSunPosition(double latitude, double longitude,
            int year, int month, int day, double hour, double timezone) {
        
        // Julian day calculation
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        double jd = day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045;
        
        // Solar declination (simplified)
        int dayOfYear = dayOfYear(year, month, day);
        double declination = 23.45 * Math.sin(Math.toRadians(360.0 / 365.25 * (dayOfYear - 81)));
        
        // Hour angle
        double solarTime = hour + (longitude / 15.0) - timezone;
        double hourAngle = 15 * (solarTime - 12);
        
        // Convert to radians
        double latRad = Math.toRadians(latitude);
        double decRad = Math.toRadians(declination);
        double haRad = Math.toRadians(hourAngle);
        
        // Altitude
        double sinAlt = Math.sin(latRad) * Math.sin(decRad) + 
                        Math.cos(latRad) * Math.cos(decRad) * Math.cos(haRad);
        double altitude = Math.toDegrees(Math.asin(Math.max(-1, Math.min(1, sinAlt))));
        
        // Azimuth
        double cosAz = (Math.sin(decRad) - Math.sin(latRad) * sinAlt) / 
                       (Math.cos(latRad) * Math.cos(Math.asin(Math.max(-1, Math.min(1, sinAlt)))));
        double azimuth = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, cosAz))));
        
        if (hourAngle > 0) azimuth = 360 - azimuth;
        
        return new SolarPosition(altitude, azimuth, jd, hourAngle);
    }

    /**
     * Simulates daily solar insulation on a surface of specific tilt and orientation.
     * 
     * @param latitude location latitude
     * @param month simulation month
     * @param day simulation day
     * @param surfaceTilt angle from horizontal (0 to 90)
     * @param surfaceAzimuth compass direction (0=North, 180=South)
     * @return InsolationResult over 24 hours
     */
    public static InsolationResult calculateDailyInsolation(double latitude, 
            int month, int day, double surfaceTilt, double surfaceAzimuth) {
        
        double[] hourly = new double[24];
        double total = 0;
        double peakValue = 0;
        double peakHour = 12;
        
        for (int hour = 0; hour < 24; hour++) {
            SolarPosition pos = calculateSunPosition(latitude, 0, 2024, month, day, hour, 0);
            
            if (pos.altitude() > 0) {
                // Direct normal irradiance (simplified clear sky model)
                double sinAlt = Math.sin(Math.toRadians(pos.altitude()));
                double airmass = sinAlt > 0 ? 1.0 / sinAlt : 0;
                double dni = 1361 * Math.pow(0.7, Math.pow(airmass, 0.678));
                
                // Angle of incidence on tilted surface
                double incidenceAngle = calculateIncidenceAngle(pos, surfaceTilt, surfaceAzimuth);
                
                if (incidenceAngle < 90) {
                    double irradiance = dni * Math.cos(Math.toRadians(incidenceAngle));
                    
                    // Add diffuse component (simplified isotropic model)
                    double diffuseHorizontal = 0.1 * 1361 * sinAlt;
                    double viewFactor = (1 + Math.cos(Math.toRadians(surfaceTilt))) / 2;
                    irradiance += diffuseHorizontal * viewFactor;
                    
                    hourly[hour] = Math.max(0, irradiance);
                    total += irradiance / 1000; // kWh/m2
                    
                    if (irradiance > peakValue) {
                        peakValue = irradiance;
                        peakHour = hour;
                    }
                }
            }
        }
        
        return new InsolationResult(total, hourly, peakHour, peakValue);
    }

    /**
     * Determines the optimal year-round tilt angle for solar collectors.
     * 
     * @param latitude site latitude
     * @return recommended tilt angle as a Real
     */
    public static Real optimalTiltAngle(double latitude) {
        return Real.of(Math.abs(latitude));
    }

    /**
     * Calculates a shading factor (0 to 1) based on an external obstruction.
     * 
     * @param sun current position of the sun
     * @param obstructionHeight height of the object
     * @param obstructionDistance distance to the object
     * @param obstructionAzimuth direction to the object
     * @return shading multiplier (0.0 = full shade, 1.0 = clear)
     */
    public static double shadingFactor(SolarPosition sun, double obstructionHeight,
            double obstructionDistance, double obstructionAzimuth) {
        
        if (obstructionDistance <= 0) return 0.0;
        double obstructionAngle = Math.toDegrees(Math.atan(obstructionHeight / obstructionDistance));
        double azimuthDiff = Math.abs(sun.azimuth() - obstructionAzimuth);
        if (azimuthDiff > 180) azimuthDiff = 360 - azimuthDiff;
        
        if (azimuthDiff < 30 && sun.altitude() < obstructionAngle) {
            return 0.0;
        }
        if (azimuthDiff < 45 && sun.altitude() < obstructionAngle * 1.2) {
            return 0.5;
        }
        return 1.0;
    }

    /**
     * Generates Sun Path diagram data points for various seasons.
     * 
     * @param latitude site latitude
     * @return list of paths (lists of SolarPosition) for solstices and equinoxes
     */
    public static List<List<SolarPosition>> generateSunPathDiagram(double latitude) {
        List<List<SolarPosition>> paths = new ArrayList<>();
        int[][] dates = {{3, 21}, {6, 21}, {9, 21}, {12, 21}};
        
        for (int[] date : dates) {
            List<SolarPosition> dayPath = new ArrayList<>();
            for (int hour = 5; hour <= 19; hour++) {
                SolarPosition pos = calculateSunPosition(latitude, 0, 2024, date[0], date[1], hour, 0);
                if (pos.altitude() > 0) {
                    dayPath.add(pos);
                }
            }
            paths.add(dayPath);
        }
        return paths;
    }

    private static double calculateIncidenceAngle(SolarPosition sun, double surfaceTilt, 
            double surfaceAzimuth) {
        double sAlt = Math.toRadians(sun.altitude());
        double sAz = Math.toRadians(sun.azimuth());
        double tilt = Math.toRadians(surfaceTilt);
        double facing = Math.toRadians(surfaceAzimuth);
        
        double cosInc = Math.cos(sAlt) * Math.sin(tilt) * Math.cos(sAz - facing) +
                        Math.sin(sAlt) * Math.cos(tilt);
        
        return Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, cosInc))));
    }

    private static int dayOfYear(int year, int month, int day) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            daysInMonth[1] = 29;
        }
        int doy = day;
        for (int i = 0; i < month - 1; i++) {
            doy += daysInMonth[i];
        }
        return doy;
    }
}

