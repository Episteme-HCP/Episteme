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

    private String pattern = "Voice";

    @Override
    public void setSourcePattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void update(double time, double sensitivity) {
        for (int i = 0; i < BANDS; i++) {
            double val = 0.0;

            if ("Sine".equals(pattern)) {
                // Moving sine wave peak
                double peakPos = (Math.sin(time * 0.5) + 1.0) * 0.5 * BANDS; // 0 to BANDS
                double dist = Math.abs(i - peakPos);
                val = Math.exp(-dist * dist * 0.5); // Gaussian
            } else if ("Noise".equals(pattern)) {
                val = Math.random();
            } else if ("Harmonics".equals(pattern)) {
                // 3 harmonics
                double t = time * 2.0;
                val = Math.sin(t + i * 0.5) > 0.8 ? 0.8 : 0.0;
                if (i % 5 == 0)
                    val += 0.5;
            } else { // Voice / Default
                double base = Math.sin(time + i * 0.2) * 0.5 + 0.5;
                double high = Math.sin(time * 2.5 + i * 0.5) * 0.3;
                double random = Math.random() * 0.2;
                val = base + high + random;
            }

            // Falloff for high frequencies
            double falloff = Math.exp(-i / (double) BANDS);

            spectrum[i] = val * falloff * sensitivity;
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
