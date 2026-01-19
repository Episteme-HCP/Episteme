package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Calculates solar path and building insolation.
 */
public final class SolarPathAnalyzer {

    private SolarPathAnalyzer() {}

    public record SolarPosition(
        double altitude,    // degrees above horizon
        double azimuth,     // degrees from north (clockwise)
        double julianDay,
        double hourAngle
    ) {}

    public record InsolationResult(
        double dailyTotal,           // kWh/m²
        double[] hourlyValues,       // W/m² for each hour
        double peakHour,
        double peakValue
    ) {}

    /**
     * Calculates sun position for given location and time.
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
        double declination = 23.45 * Math.sin(Math.toRadians(360.0 / 365 * (dayOfYear - 81)));
        
        // Hour angle
        double solarTime = hour + (longitude / 15.0) - timezone;  // Approximate
        double hourAngle = 15 * (solarTime - 12);
        
        // Convert to radians
        double latRad = Math.toRadians(latitude);
        double decRad = Math.toRadians(declination);
        double haRad = Math.toRadians(hourAngle);
        
        // Altitude
        double sinAlt = Math.sin(latRad) * Math.sin(decRad) + 
                        Math.cos(latRad) * Math.cos(decRad) * Math.cos(haRad);
        double altitude = Math.toDegrees(Math.asin(sinAlt));
        
        // Azimuth
        double cosAz = (Math.sin(decRad) - Math.sin(latRad) * sinAlt) / 
                       (Math.cos(latRad) * Math.cos(Math.asin(sinAlt)));
        double azimuth = Math.toDegrees(Math.acos(Math.max(-1, Math.min(1, cosAz))));
        
        if (hourAngle > 0) azimuth = 360 - azimuth;
        
        return new SolarPosition(altitude, azimuth, jd, hourAngle);
    }

    /**
     * Calculates daily insolation on a surface.
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
                double airmass = 1 / Math.sin(Math.toRadians(pos.altitude()));
                double dni = 1361 * Math.pow(0.7, Math.pow(airmass, 0.678));
                
                // Angle of incidence on tilted surface
                double incidenceAngle = calculateIncidenceAngle(pos, surfaceTilt, surfaceAzimuth);
                
                if (incidenceAngle < 90) {
                    double irradiance = dni * Math.cos(Math.toRadians(incidenceAngle));
                    
                    // Add diffuse component (simplified)
                    double diffuseHorizontal = 0.1 * 1361 * Math.sin(Math.toRadians(pos.altitude()));
                    double viewFactor = (1 + Math.cos(Math.toRadians(surfaceTilt))) / 2;
                    irradiance += diffuseHorizontal * viewFactor;
                    
                    hourly[hour] = Math.max(0, irradiance);
                    total += irradiance / 1000; // Convert to kWh/m²
                    
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
     * Determines optimal tilt angle for solar panels.
     */
    public static Real optimalTiltAngle(double latitude) {
        // Rule of thumb: tilt = latitude for year-round, latitude-15 for summer, latitude+15 for winter
        return Real.of(Math.abs(latitude));
    }

    /**
     * Calculates shading from nearby obstructions.
     */
    public static double shadingFactor(SolarPosition sun, double obstructionHeight,
            double obstructionDistance, double obstructionAzimuth) {
        
        // Calculate angle to top of obstruction
        double obstructionAngle = Math.toDegrees(Math.atan(obstructionHeight / obstructionDistance));
        
        // Check if sun is behind obstruction
        double azimuthDiff = Math.abs(sun.azimuth() - obstructionAzimuth);
        if (azimuthDiff > 180) azimuthDiff = 360 - azimuthDiff;
        
        // If sun is lower than obstruction and in same direction, shaded
        if (azimuthDiff < 30 && sun.altitude() < obstructionAngle) {
            return 0.0; // Fully shaded
        }
        
        if (azimuthDiff < 45 && sun.altitude() < obstructionAngle * 1.2) {
            return 0.5; // Partially shaded
        }
        
        return 1.0; // Not shaded
    }

    /**
     * Generates sun path diagram data for a location.
     */
    public static List<List<SolarPosition>> generateSunPathDiagram(double latitude) {
        List<List<SolarPosition>> paths = new ArrayList<>();
        
        // Generate for solstices and equinoxes
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
