package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Area;
import org.jscience.measure.Units;

/**
 * Daylight factor calculations for lighting analysis.
 */
public final class DaylightFactor {

    private DaylightFactor() {}

    /**
     * Calculates the Daylight Factor (DF) using the BRE formula.
     * DF = (Ei / Eo) × 100%
     * 
     * Simplified estimate: DF ≈ (T × W × θ × M) / (A × (1-R²))
     * where:
     * T = glass transmittance
     * W = window area
     * θ = visible sky angle
     * M = maintenance factor
     * A = total room surface area
     * R = average room reflectance
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
        
        double df = (T * W * theta * M) / (A * (1 - R * R)) * 100;
        
        return Real.of(df);
    }

    /**
     * Evaluates daylight adequacy based on room use.
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
     * Calculates the average daylight factor for a room with multiple windows.
     */
    public static Real averageDaylightFactor(java.util.List<Real> windowContributions) {
        if (windowContributions.isEmpty()) return Real.ZERO;
        
        double sum = windowContributions.stream()
            .mapToDouble(Real::doubleValue)
            .sum();
        
        return Real.of(sum);
    }

    /**
     * Estimates the No-Sky Line depth (distance into room where sky is not visible).
     */
    public static Real noSkyLineDepth(double windowHeadHeight, 
            double windowSillHeight, double externalObstructionAngle) {
        
        double windowHeight = windowHeadHeight - windowSillHeight;
        double obstructionRadians = Math.toRadians(externalObstructionAngle);
        
        double depth = windowHeight / Math.tan(obstructionRadians);
        return Real.of(depth);
    }

    /**
     * Standard reflectance values for common surfaces.
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
     * Calculates area-weighted average reflectance for a room.
     */
    public static double averageRoomReflectance(double ceilingArea, double ceilingReflectance,
            double wallArea, double wallReflectance,
            double floorArea, double floorReflectance) {
        
        double totalArea = ceilingArea + wallArea + floorArea;
        return (ceilingArea * ceilingReflectance + 
                wallArea * wallReflectance + 
                floorArea * floorReflectance) / totalArea;
    }
}
