package org.jscience.ui.physics.waves;

/**
 * Primitive implementation of Spectrum Provider.
 * Uses simple double arrays and Math functions.
 */
public class PrimitiveSpectrumProvider implements SpectrumProvider {

    private final int BANDS;
    private double[] spectrum;

    public PrimitiveSpectrumProvider(int bands) {
        this.BANDS = bands;
        this.spectrum = new double[BANDS];
    }

    @Override
    public void update(double time, double sensitivity) {
        for (int i = 0; i < BANDS; i++) {
            // Mock harmonic noise
            double base = Math.sin(time + i * 0.2) * 0.5 + 0.5;
            double high = Math.sin(time * 2.5 + i * 0.5) * 0.3;
            double random = Math.random() * 0.2;

            // Falloff for high frequencies
            double falloff = Math.exp(-i / (double) BANDS);

            spectrum[i] = (base + high + random) * falloff * sensitivity;
        }
    }

    @Override
    public double[] getSpectrum() {
        return spectrum;
    }

    @Override
    public String getName() {
        return "Primitive (double[])";
    }
}
