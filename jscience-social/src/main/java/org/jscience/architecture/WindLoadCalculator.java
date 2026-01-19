package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.Units;

/**
 * Wind load calculations according to Eurocode 1 principles.
 */
public final class WindLoadCalculator {

    private WindLoadCalculator() {}

    public enum TerrainCategory {
        SEA_COAST(0.003, 1.0),           // Category 0
        OPEN_COUNTRY(0.01, 1.0),         // Category I
        SUBURBAN(0.05, 2.0),             // Category II
        URBAN(0.3, 5.0),                 // Category III
        CITY_CENTER(1.0, 10.0);          // Category IV

        private final double z0; // Roughness length (m)
        private final double zMin; // Minimum height (m)

        TerrainCategory(double z0, double zMin) {
            this.z0 = z0;
            this.zMin = zMin;
        }

        public double getRoughnessLength() { return z0; }
        public double getMinimumHeight() { return zMin; }
    }

    /**
     * Calculates basic wind velocity.
     * vb = cdir × cseason × vb,0
     */
    public static Real basicWindVelocity(double vb0, double directionalFactor, double seasonFactor) {
        return Real.of(directionalFactor * seasonFactor * vb0);
    }

    /**
     * Calculates mean wind velocity at height z.
     * vm(z) = cr(z) × c0(z) × vb
     */
    public static Real meanWindVelocity(Real basicVelocity, Quantity<Length> height, 
            TerrainCategory terrain, double orographyFactor) {
        
        double z = height.to(Units.METER).getValue().doubleValue();
        double zEff = Math.max(z, terrain.getMinimumHeight());
        double z0 = terrain.getRoughnessLength();
        
        // Roughness factor cr(z) = kr × ln(z/z0)
        double kr = 0.19 * Math.pow(z0 / 0.05, 0.07);
        double cr = kr * Math.log(zEff / z0);
        
        double vm = cr * orographyFactor * basicVelocity.doubleValue();
        return Real.of(vm);
    }

    /**
     * Calculates peak velocity pressure.
     * qp(z) = [1 + 7×Iv(z)] × 0.5 × ρ × vm²(z)
     */
    public static Real peakVelocityPressure(Real meanVelocity, Quantity<Length> height,
            TerrainCategory terrain, double airDensity) {
        
        double z = height.to(Units.METER).getValue().doubleValue();
        double zEff = Math.max(z, terrain.getMinimumHeight());
        double z0 = terrain.getRoughnessLength();
        
        // Turbulence intensity Iv(z)
        double kl = 1.0; // Turbulence factor
        double Iv = kl / (1.0 * Math.log(zEff / z0));
        
        double vm = meanVelocity.doubleValue();
        double qp = (1 + 7 * Iv) * 0.5 * airDensity * vm * vm;
        
        return Real.of(qp); // Pa (N/m²)
    }

    /**
     * Calculates wind force on a surface.
     * Fw = cs × cd × cf × qp(ze) × Aref
     */
    public static Real windForce(Real peakPressure, double referenceArea,
            double structuralFactor, double forceCoefficient) {
        
        double qp = peakPressure.doubleValue();
        double force = structuralFactor * forceCoefficient * qp * referenceArea;
        
        return Real.of(force); // Newtons
    }

    /**
     * Standard force coefficients for common shapes.
     */
    public static double getForceCoefficient(String shape, double aspectRatio) {
        return switch (shape.toLowerCase()) {
            case "rectangular" -> 1.0 + 0.02 * Math.min(aspectRatio, 10);
            case "circular" -> 0.8; // Smooth cylinder
            case "square" -> 2.0;
            case "triangular" -> 1.2;
            default -> 1.0;
        };
    }

    /**
     * Calculates the reference height for wind pressure.
     */
    public static Real referenceHeight(Quantity<Length> buildingHeight, 
            Quantity<Length> buildingWidth, boolean windward) {
        
        double h = buildingHeight.to(Units.METER).getValue().doubleValue();
        double b = buildingWidth.to(Units.METER).getValue().doubleValue();
        
        if (h <= b) {
            return Real.of(h);
        } else if (h <= 2 * b) {
            return windward ? Real.of(h) : Real.of(b);
        } else {
            return Real.of(h); // Use strips for very tall buildings
        }
    }
}
