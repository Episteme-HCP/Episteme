/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.architecture;

import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Area;

/**
 * Provides mathematical models for calculating the Daylight Factor (DF) and 
 * evaluating natural lighting adequacy in architectural spaces.
 * It implements standard BRE (Building Research Establishment) estimation formulas.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class DaylightFactor {

    private DaylightFactor() {}

    /**
     * Calculates the Daylight Factor (DF) using the BRE formula.
     * Simplified estimate: DF approx (T * W * theta * M) / (A * (1-R^2)) * 100%
     * 
     * @param windowArea total window glazing area
     * @param roomSurfaceArea total internal surface area (ceiling + walls + floor)
     * @param glassTransmittance transmittance of the glass (0.0 to 1.0)
     * @param skyAngleFactor visible sky angle in radians/factor
     * @param maintenanceFactor factor for dirt/aging (typical 0.8-0.9)
     * @param averageReflectance area-weighted average reflectance of internal surfaces
     * @return the Daylight Factor as a percentage
     */
    public static Real calculateDaylightFactor(
            Quantity<Area> windowArea,
            Quantity<Area> roomSurfaceArea,
            double glassTransmittance,
            double skyAngleFactor,
            double maintenanceFactor,
            double averageReflectance) {
        
        double W = windowArea.to(Units.SQUARE_METER).getValue().doubleValue();
        double A = roomSurfaceArea.to(Units.SQUARE_METER).getValue().doubleValue();
        double T = glassTransmittance;
        double theta = skyAngleFactor;
        double M = maintenanceFactor;
        double R = averageReflectance;
        
        if (A == 0 || (1 - R * R) == 0) return Real.ZERO;
        
        double df = (T * W * theta * M) / (A * (1 - R * R)) * 100;
        return Real.of(df);
    }

    /**
     * Evaluates if the calculated daylight factor is adequate for the intended room use.
     * 
     * @param daylightFactor the calculated DF percentage
     * @param roomType type of room (e.g., "office", "bedroom")
     * @return a qualitative description of daylight adequacy
     */
    public static String evaluateDaylightLevel(Real daylightFactor, String roomType) {
        double df = daylightFactor.doubleValue();
        
        double minimum = switch (roomType.toLowerCase()) {
            case "office", "classroom" -> 2.0;
            case "kitchen" -> 2.0;
            case "living room", "lounge" -> 1.5;
            case "bedroom" -> 1.0;
            case "bathroom" -> 0.5;
            case "corridor", "hallway" -> 0.5;
            case "studio", "art room" -> 4.0;
            default -> 1.5;
        };
        
        if (df >= minimum * 2) return "Excellent - Well daylit";
        if (df >= minimum) return "Good - Meets requirements";
        if (df >= minimum * 0.5) return "Marginal - Supplementary lighting recommended";
        return "Poor - Insufficient daylight, artificial lighting required";
    }

    /**
     * Sums contributions from multiple windows to find the total average DF.
     * 
     * @param windowContributions list of individual window DF values
     * @return total combined Daylight Factor
     */
    public static Real averageDaylightFactor(List<Real> windowContributions) {
        if (windowContributions.isEmpty()) return Real.ZERO;
        double sum = windowContributions.stream()
            .mapToDouble(Real::doubleValue)
            .sum();
        return Real.of(sum);
    }

    /**
     * Estimates the No-Sky Line depth, which is the point in a room beyond 
     * which the sky isä¸å† visible due to external obstructions.
     * 
     * @param windowHeadHeight height of window head from floor
     * @param windowSillHeight height of window sill from floor
     * @param externalObstructionAngle angle of external obstruction in degrees
     * @return the depth distance into the room
     */
    public static Real noSkyLineDepth(double windowHeadHeight, 
            double windowSillHeight, double externalObstructionAngle) {
        
        double windowHeight = windowHeadHeight - windowSillHeight;
        double obstructionRadians = Math.toRadians(externalObstructionAngle);
        if (Math.tan(obstructionRadians) == 0) return Real.ZERO;
        
        double depth = windowHeight / Math.tan(obstructionRadians);
        return Real.of(depth);
    }

    /**
     * Returns typical reflectance values for architectural surfaces.
     * 
     * @param surface name of the surface type
     * @return reflectance coefficient (0.0 to 1.0)
     */
    public static double getSurfaceReflectance(String surface) {
        return switch (surface.toLowerCase()) {
            case "white ceiling" -> 0.8;
            case "light wall" -> 0.5;
            case "medium wall" -> 0.3;
            case "dark wall" -> 0.1;
            case "light floor" -> 0.4;
            case "dark floor" -> 0.1;
            case "carpet" -> 0.1;
            case "wood floor" -> 0.3;
            default -> 0.3;
        };
    }

    /**
     * Calculates the area-weighted average reflectance of a room's interior.
     * 
     * @param ceilingArea total ceiling area
     * @param ceilingReflectance ceiling reflectance
     * @param wallArea total wall area
     * @param wallReflectance wall reflectance
     * @param floorArea total floor area
     * @param floorReflectance floor reflectance
     * @return the weighted average reflectance
     */
    public static double averageRoomReflectance(double ceilingArea, double ceilingReflectance,
            double wallArea, double wallReflectance,
            double floorArea, double floorReflectance) {
        
        double totalArea = ceilingArea + wallArea + floorArea;
        if (totalArea == 0) return 0;
        return (ceilingArea * ceilingReflectance + 
                wallArea * wallReflectance + 
                floorArea * floorReflectance) / totalArea;
    }
}

