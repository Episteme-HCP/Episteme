package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Simplified acoustic ray tracer for closed spaces.
 */
public final class AcousticRayTracer {

    private AcousticRayTracer() {}

    public record Ray(double[] origin, double[] direction, double intensity) {}
    public record Reflection(double[] position, double absorptionCoeff) {}

    /**
     * Estimates sound attenuation over distance.
     * I = I0 * exp(-alpha * d)
     * 
     * @param distance Meters
     * @param alpha Absorption coefficient of air
     */
    public static Real airAttenuation(double distance, double alpha) {
        return Real.of(Math.exp(-alpha * distance));
    }

    /**
     * Calculates the Sabine Reverberation Time (RT60).
     * RT60 = 0.161 * V / (S * alpha_avg)
     * 
     * @param volume Room volume (m3)
     * @param surfaceArea Total internal surface area (m2)
     * @param avgAbsorption Average absorption coefficient
     */
    public static Real calculateSabineRT60(double volume, double surfaceArea, double avgAbsorption) {
        if (surfaceArea * avgAbsorption == 0) return Real.ZERO;
        return Real.of(0.161 * volume / (surfaceArea * avgAbsorption));
    }
}
