package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Area;
import org.jscience.measure.Units;
import java.util.List;

/**
 * Lighting simulation and illuminance calculations.
 */
public final class LightingSimulator {

    private LightingSimulator() {}

    /**
     * Represents a point light source.
     */
    public record LightSource(double x, double y, double z, double luminousIntensity) {}

    /**
     * Calculates the illuminance (Lux) at a point from a single point source.
     * Uses the inverse-square law: E = I * cos(θ) / d²
     * 
     * @param source The light source.
     * @param pointX X coordinate of the measurement point.
     * @param pointY Y coordinate of the measurement point.
     * @param pointZ Z coordinate of the measurement point (surface height).
     * @param normalX X component of surface normal.
     * @param normalY Y component of surface normal.
     * @param normalZ Z component of surface normal.
     * @return Illuminance in Lux.
     */
    public static Real illuminanceFromPoint(LightSource source, 
            double pointX, double pointY, double pointZ,
            double normalX, double normalY, double normalZ) {
        
        double dx = source.x() - pointX;
        double dy = source.y() - pointY;
        double dz = source.z() - pointZ;
        double distSq = dx * dx + dy * dy + dz * dz;
        double dist = Math.sqrt(distSq);
        
        if (dist < 0.001) return Real.of(source.luminousIntensity()); // Very close
        
        // Normalize direction
        double dirX = dx / dist, dirY = dy / dist, dirZ = dz / dist;
        
        // Normalize surface normal
        double normLen = Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        double nX = normalX / normLen, nY = normalY / normLen, nZ = normalZ / normLen;
        
        // cos(θ) = dot product of light direction and surface normal
        double cosTheta = Math.max(0, dirX * nX + dirY * nY + dirZ * nZ);
        
        double illuminance = (source.luminousIntensity() * cosTheta) / distSq;
        return Real.of(illuminance);
    }

    /**
     * Calculates total illuminance at a point from multiple light sources.
     */
    public static Real totalIlluminance(List<LightSource> sources,
            double pointX, double pointY, double pointZ,
            double normalX, double normalY, double normalZ) {
        Real total = Real.ZERO;
        for (LightSource source : sources) {
            total = total.add(illuminanceFromPoint(source, pointX, pointY, pointZ, normalX, normalY, normalZ));
        }
        return total;
    }

    /**
     * Calculates the required luminous intensity to achieve target Lux at a distance.
     * I = E * d²
     */
    public static Real requiredIntensity(Real targetLux, Quantity<Length> distance) {
        double d = distance.to(Units.METER).getValue().doubleValue();
        return targetLux.multiply(Real.of(d * d));
    }

    /**
     * Estimates the number of luminaires needed to achieve target average illuminance.
     * N = (E * A) / (n * F * U * M)
     * where n=lamps/fixture, F=lumens/lamp, U=utilization factor, M=maintenance factor
     */
    public static int requiredLuminaires(Real targetLux, Quantity<Area> area, 
            double lumensPerLamp, int lampsPerFixture, 
            double utilizationFactor, double maintenanceFactor) {
        double E = targetLux.doubleValue();
        double A = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double totalFlux = E * A;
        double fluxPerFixture = lampsPerFixture * lumensPerLamp * utilizationFactor * maintenanceFactor;
        return (int) Math.ceil(totalFlux / fluxPerFixture);
    }
}
