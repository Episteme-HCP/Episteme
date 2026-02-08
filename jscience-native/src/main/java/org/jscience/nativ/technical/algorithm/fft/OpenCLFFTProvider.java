package org.jscience.nativ.technical.algorithm.fft;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;
import org.jscience.core.technical.algorithm.fft.StandardFFTProvider;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;

import java.util.logging.Logger;

/**
 * OpenCL implementation of FFTProvider.
 * <p>
 * Currently acts as a placeholder/verification class for the Context x Backend architecture.
 * Delegates to CPU StandardFFTProvider until pure JOCL kernels are fully implemented.
 * This ensures the provider exists and is discoverable in the matrix.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
import org.jscience.core.technical.algorithm.AlgorithmProvider;

@AutoService({FFTProvider.class, AlgorithmProvider.class})
public class OpenCLFFTProvider implements FFTProvider {

    private static final Logger LOGGER = Logger.getLogger(OpenCLFFTProvider.class.getName());
    private final StandardFFTProvider cpuDelegate = new StandardFFTProvider();
    private final OpenCLBackend backend = new OpenCLBackend();
    private boolean warningLogged = false;

    private void logWarning() {
        if (!warningLogged) {
            LOGGER.warning("OpenCL FFT kernel not yet optimized. Falling back to CPU delegation for verification.");
            warningLogged = true;
        }
    }

    @Override
    public String getName() {
        return "OpenCL (GPU)";
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    @Override
    public int getPriority() {
        // Lower priority until fully optimized
        return 20;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        logWarning();
        return cpuDelegate.transform(real, imag);
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        logWarning();
        return cpuDelegate.inverseTransform(real, imag);
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        logWarning();
        return cpuDelegate.transform(real, imag);
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        logWarning();
        return cpuDelegate.inverseTransform(real, imag);
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        logWarning();
        return cpuDelegate.transformComplex(data);
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        logWarning();
        return cpuDelegate.inverseTransformComplex(data);
    }
}
