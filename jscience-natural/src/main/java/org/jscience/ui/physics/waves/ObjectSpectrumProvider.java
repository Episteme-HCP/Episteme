package org.jscience.ui.physics.waves;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Object-based Spectrum Provider.
 * Uses Complex numbers and Vector types to simulate signal processing.
 */
public class ObjectSpectrumProvider implements SpectrumProvider {

    private final int BANDS;
    private Vector<Real> spectrum; // Storing as Real vector for result

    // We could mock "Complex" signal processing by generating a Complex vector
    // and taking magnitude.

    public ObjectSpectrumProvider(int bands) {
        this.BANDS = bands;
        // Initialize with zeros
    }

    @Override
    public void update(double time, double sensitivity) {
        List<Real> magnitudes = new ArrayList<>(BANDS);

        Real rTime = Real.of(time);
        Real rSens = Real.of(sensitivity);

        for (int i = 0; i < BANDS; i++) {
            Real idx = Real.of(i);

            // Mock signal as Complex number components
            // Signal = (sin(t + i*0.2)*0.5 + 0.5) + j(sin(t*2.5 + i*0.5)*0.3)

            Real r02 = Real.of(0.2);
            Real r05 = Real.of(0.5);
            Real r25 = Real.of(2.5);
            Real r03 = Real.of(0.3);

            // Real Part Calculation
            // base = sin(t + i*0.2) * 0.5 + 0.5
            Real theta1 = rTime.add(idx.multiply(r02));
            // Real has sin() as per outline
            Real base = theta1.sin().multiply(r05).add(r05);

            // Imaginary Part Calculation
            // high = sin(t*2.5 + i*0.5) * 0.3
            Real theta2 = rTime.multiply(r25).add(idx.multiply(r05));
            Real high = theta2.sin().multiply(r03);

            // Create Complex Signal
            Complex signal = Complex.of(base, high);

            // Add Noise (Real)
            double rnd = Math.random() * 0.2;
            Real noise = Real.of(rnd);

            // Magnitude = |signal| + noise
            // Complex.abs() returns Real (magnitude)
            Real mag = signal.abs().add(noise);

            // Falloff
            // exp(-i / BANDS)
            double dFalloff = Math.exp(-i / (double) BANDS);
            Real falloff = Real.of(dFalloff);

            // Final = mag * falloff * sens
            Real val = mag.multiply(falloff).multiply(rSens);

            magnitudes.add(val);
        }

        // Create immutable vector (Heavy allocation)
        this.spectrum = DenseVector.of(magnitudes, org.jscience.mathematics.sets.Reals.getInstance());
    }

    @Override
    public double[] getSpectrum() {
        if (spectrum == null)
            return new double[BANDS];
        double[] arr = new double[BANDS];
        for (int i = 0; i < BANDS; i++) {
            arr[i] = spectrum.get(i).doubleValue();
        }
        return arr;
    }

    @Override
    public String getName() {
        return "Scientific (Complex Objects)";
    }
}
