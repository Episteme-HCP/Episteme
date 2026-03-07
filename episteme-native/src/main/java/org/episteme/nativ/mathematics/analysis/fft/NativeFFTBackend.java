/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.analysis.fft;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.analysis.fft.FFTProvider;
import com.google.auto.service.AutoService;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;

import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.cpu.CPUBackend;

/**
 * FFTW3 implementation of FFTProvider using Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({FFTProvider.class, ComputeBackend.class, CPUBackend.class, Backend.class, NativeBackend.class})
public class NativeFFTBackend implements FFTProvider, CPUBackend, NativeBackend {

    private static MethodHandle DPLAN_R2C_1D;
    private static MethodHandle DPLAN_C2R_1D;
    private static MethodHandle DPLAN_DFT_1D; // For Complex-to-Complex
    private static MethodHandle DPLAN_DFT_2D;
    private static MethodHandle DPLAN_DFT_3D;
    private static MethodHandle DEXECUTE;
    private static MethodHandle DDESTROY_PLAN;

    private static boolean initialized = false;
    private static boolean available = false;

    @Override
    public boolean isLoaded() {
        ensureInitialized();
        return available;
    }

    @Override
    public String getNativeLibraryName() {
        return "fftw3";
    }

    private static synchronized void ensureInitialized() {
        if (initialized) return;
        initialized = true;
        try {
            SymbolLookup lookup = org.episteme.core.technical.backend.nativ.NativeLibraryLoader.loadLibrary("fftw3");
            Linker linker = Linker.nativeLinker();

            DPLAN_R2C_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_r2c_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT));
            DPLAN_C2R_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_c2r_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT));
            
            // fftw_plan fftw_plan_dft_1d(int n, fftw_complex *in, fftw_complex *out, int sign, unsigned flags);
            DPLAN_DFT_1D = linker.downcallHandle(lookup.find("fftw_plan_dft_1d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS,
                            ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            // fftw_plan fftw_plan_dft_2d(int n0, int n1, fftw_complex *in, fftw_complex *out, int sign, unsigned flags);
            DPLAN_DFT_2D = linker.downcallHandle(lookup.find("fftw_plan_dft_2d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, 
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            // fftw_plan fftw_plan_dft_3d(int n0, int n1, int n2, fftw_complex *in, fftw_complex *out, int sign, unsigned flags);
            DPLAN_DFT_3D = linker.downcallHandle(lookup.find("fftw_plan_dft_3d").get(),
                    FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,
                            ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT));

            DEXECUTE = linker.downcallHandle(lookup.find("fftw_execute").get(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            DDESTROY_PLAN = linker.downcallHandle(lookup.find("fftw_destroy_plan").get(),
                    FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
            
            available = true;
        } catch (Throwable t) {
            // Silently mark as unavailable — this is expected when FFTW3 is not installed.
            // The benchmark UI will show "Unavailable" status.
            available = false;
        }
    }

    @Override
    public int getPriority() {
        return 70; // High priority for native FFTW3
    }

    @Override
    public String getName() {
        return "Native FFTW3 (Wrapper)";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null;
    }

    @Override
    public String getAlgorithmType() {
        return "signal-processing";
    }

    @Override
    public boolean isAvailable() {
        ensureInitialized();
        return available;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = real.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n);
            for (int i = 0; i < n; i++)
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i, real[i]);
            // Output for r2c is (n/2+1) complex numbers
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, (n / 2 + 1) * 2);

            MemorySegment plan = (MemorySegment) DPLAN_R2C_1D.invokeExact(n, in, out, 1 << 6); // FFTW_ESTIMATE
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            double[] re = new double[n / 2 + 1];
            double[] im = new double[n / 2 + 1];
            for (int i = 0; i < n / 2 + 1; i++) {
                re[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2);
                im[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1);
            }
            return new double[][] { re, im };
        } catch (Throwable e) {
            throw new RuntimeException("FFT execution failed", e);
        }
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        // FFTW c2r destroys the input buffer and expects hermitically-symmetric data
        int outN = (real.length - 1) * 2;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, real.length * 2);
            for (int i = 0; i < real.length; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, real[i]);
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, imag[i]);
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, outN);

            MemorySegment plan = (MemorySegment) DPLAN_C2R_1D.invokeExact(outN, in, out, 1 << 6);
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            double[] resultReal = new double[outN];
            for (int i = 0; i < outN; i++)
                resultReal[i] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i) / outN;
            return new double[][] { resultReal, new double[outN] };
        } catch (Throwable e) {
            throw new RuntimeException("Inverse FFT execution failed", e);
        }
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] im = new double[imag.length];
        for (int i = 0; i < r.length; i++) r[i] = real[i].doubleValue();
        for (int i = 0; i < im.length; i++) im[i] = imag[i].doubleValue();

        double[][] res = transform(r, im);
        Real[] resR = new Real[res[0].length];
        Real[] resI = new Real[res[1].length];
        for (int i = 0; i < resR.length; i++) resR[i] = Real.of(res[0][i]);
        for (int i = 0; i < resI.length; i++) resI[i] = Real.of(res[1][i]);
        return new Real[][] { resR, resI };
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        double[] r = new double[real.length];
        double[] im = new double[imag.length];
        for (int i = 0; i < r.length; i++) r[i] = real[i].doubleValue();
        for (int i = 0; i < im.length; i++) im[i] = imag[i].doubleValue();

        double[][] res = inverseTransform(r, im);
        Real[] resR = new Real[res[0].length];
        Real[] resI = new Real[res[1].length];
        for (int i = 0; i < resR.length; i++) resR[i] = Real.of(res[0][i]);
        for (int i = 0; i < resI.length; i++) resI[i] = Real.of(res[1][i]);
        return new Real[][] { resR, resI };
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = data.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);
            for (int i = 0; i < n; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, data[i].real());
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, data[i].imaginary());
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);

            // -1 for Forward transform
            MemorySegment plan = (MemorySegment) DPLAN_DFT_1D.invokeExact(n, in, out, -1, 1 << 6); 
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            Complex[] result = new Complex[n];
            for (int i = 0; i < n; i++) {
                double re = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2);
                double im = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1);
                result[i] = Complex.of(re, im);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Complex FFT execution failed", e);
        }
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n = data.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);
            for (int i = 0; i < n; i++) {
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2, data[i].real());
                in.setAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1, data[i].imaginary());
            }
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, n * 2);

            // 1 for Backward transform
            MemorySegment plan = (MemorySegment) DPLAN_DFT_1D.invokeExact(n, in, out, 1, 1 << 6); 
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);

            Complex[] result = new Complex[n];
            for (int i = 0; i < n; i++) {
                double re = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2) / n;
                double im = out.getAtIndex(ValueLayout.JAVA_DOUBLE, i * 2 + 1) / n;
                result[i] = Complex.of(re, im);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Complex Inverse FFT execution failed", e);
        }
    }
    @Override
    public double[][][] transform2D(double[][] real, double[][] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n0 = real.length;
        int n1 = real[0].length;
        long totalElements = (long) n0 * n1;
        
        try (Arena arena = Arena.ofConfined()) {
            // Allocate interleaved complex input (real, imag, real, imag...)
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
            for(int i=0; i<n0; i++) {
                for(int j=0; j<n1; j++) {
                    long offset = ((long) i * n1 + j) * 2;
                    in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset, real[i][j]);
                    in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1, imag[i][j]);
                }
            }
            
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
            
            // -1 for Forward
            MemorySegment plan = (MemorySegment) DPLAN_DFT_2D.invokeExact(n0, n1, in, out, -1, 1 << 6); // FFTW_ESTIMATE
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);
            
            double[][][] result = new double[2][n0][n1];
            for(int i=0; i<n0; i++) {
                for(int j=0; j<n1; j++) {
                    long offset = ((long) i * n1 + j) * 2;
                    result[0][i][j] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset);
                    result[1][i][j] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1);
                }
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("2D FFT execution failed", e);
        }
    }

    @Override
    public double[][][] inverseTransform2D(double[][] real, double[][] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n0 = real.length;
        int n1 = real[0].length;
        long totalElements = (long) n0 * n1;
        
        try (Arena arena = Arena.ofConfined()) {
             MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
             for(int i=0; i<n0; i++) {
                 for(int j=0; j<n1; j++) {
                     long offset = ((long) i * n1 + j) * 2;
                     in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset, real[i][j]);
                     in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1, imag[i][j]);
                 }
             }
             
             MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
             
             // 1 for Backward
             MemorySegment plan = (MemorySegment) DPLAN_DFT_2D.invokeExact(n0, n1, in, out, 1, 1 << 6);
             DEXECUTE.invokeExact(plan);
             DDESTROY_PLAN.invokeExact(plan);
             
             double[][][] result = new double[2][n0][n1];
             double norm = (double) totalElements;
             for(int i=0; i<n0; i++) {
                 for(int j=0; j<n1; j++) {
                     long offset = ((long) i * n1 + j) * 2;
                     result[0][i][j] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset) / norm;
                     result[1][i][j] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1) / norm;
                 }
             }
             return result;
        } catch (Throwable e) {
             throw new RuntimeException("2D Inverse FFT execution failed", e);
        }
    }

    @Override
    public double[][][][] transform3D(double[][][] real, double[][][] imag) {
        ensureInitialized();
        if (!available) throw new UnsupportedOperationException("FFTW3 library not available");

        int n0 = real.length;
        int n1 = real[0].length;
        int n2 = real[0][0].length;
        long totalElements = (long) n0 * n1 * n2;
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
            for(int i=0; i<n0; i++) {
                for(int j=0; j<n1; j++) {
                    for(int k=0; k<n2; k++) {
                        long offset = ((long) i * n1 * n2 + (long) j * n2 + k) * 2;
                        in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset, real[i][j][k]);
                        in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1, imag[i][j][k]);
                    }
                }
            }
            
            MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
            
            // -1 for Forward
            MemorySegment plan = (MemorySegment) DPLAN_DFT_3D.invokeExact(n0, n1, n2, in, out, -1, 1 << 6);
            DEXECUTE.invokeExact(plan);
            DDESTROY_PLAN.invokeExact(plan);
            
            double[][][][] result = new double[2][n0][n1][n2];
            for(int i=0; i<n0; i++) {
                for(int j=0; j<n1; j++) {
                    for(int k=0; k<n2; k++) {
                        long offset = ((long) i * n1 * n2 + (long) j * n2 + k) * 2;
                        result[0][i][j][k] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset);
                        result[1][i][j][k] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1);
                    }
                }
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("3D FFT execution failed", e);
        }
    }

    @Override
    public double[][][][] inverseTransform3D(double[][][] real, double[][][] imag) {
         ensureInitialized();
         if (!available) throw new UnsupportedOperationException("FFTW3 library not available");
 
         int n0 = real.length;
         int n1 = real[0].length;
         int n2 = real[0][0].length;
         long totalElements = (long) n0 * n1 * n2;
         
         try (Arena arena = Arena.ofConfined()) {
             MemorySegment in = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
             for(int i=0; i<n0; i++) {
                 for(int j=0; j<n1; j++) {
                     for(int k=0; k<n2; k++) {
                         long offset = ((long) i * n1 * n2 + (long) j * n2 + k) * 2;
                         in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset, real[i][j][k]);
                         in.setAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1, imag[i][j][k]);
                     }
                 }
             }
             
             MemorySegment out = arena.allocate(ValueLayout.JAVA_DOUBLE, totalElements * 2);
             
             // 1 for Backward
             MemorySegment plan = (MemorySegment) DPLAN_DFT_3D.invokeExact(n0, n1, n2, in, out, 1, 1 << 6);
             DEXECUTE.invokeExact(plan);
             DDESTROY_PLAN.invokeExact(plan);
             
             double[][][][] result = new double[2][n0][n1][n2];
             double norm = (double) totalElements;
             for(int i=0; i<n0; i++) {
                 for(int j=0; j<n1; j++) {
                     for(int k=0; k<n2; k++) {
                         long offset = ((long) i * n1 * n2 + (long) j * n2 + k) * 2;
                         result[0][i][j][k] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset) / norm;
                         result[1][i][j][k] = out.getAtIndex(ValueLayout.JAVA_DOUBLE, offset + 1) / norm;
                     }
                 }
             }
             return result;
         } catch (Throwable e) {
             throw new RuntimeException("3D Inverse FFT execution failed", e);
         }
    }

    @Override
    public Real[][][] transform2D(Real[][] real, Real[][] imag) {
        double[][] r = toDouble2D(real);
        double[][] i = toDouble2D(imag);
        double[][][] res = transform2D(r, i);
        return new Real[][][]{toReal2D(res[0]), toReal2D(res[1])};
    }

    @Override
    public Real[][][] inverseTransform2D(Real[][] real, Real[][] imag) {
        double[][] r = toDouble2D(real);
        double[][] i = toDouble2D(imag);
        double[][][] res = inverseTransform2D(r, i);
        return new Real[][][]{toReal2D(res[0]), toReal2D(res[1])};
    }

    @Override
    public Real[][][][] transform3D(Real[][][] real, Real[][][] imag) {
        double[][][] r = toDouble3D(real);
        double[][][] i = toDouble3D(imag);
        double[][][][] res = transform3D(r, i);
        return new Real[][][][]{toReal3D(res[0]), toReal3D(res[1])};
    }

    @Override
    public Real[][][][] inverseTransform3D(Real[][][] real, Real[][][] imag) {
        double[][][] r = toDouble3D(real);
        double[][][] i = toDouble3D(imag);
        double[][][][] res = inverseTransform3D(r, i);
        return new Real[][][][]{toReal3D(res[0]), toReal3D(res[1])};
    }

    // --- Conversion helpers ---
    private static double[][] toDouble2D(Real[][] a) {
        double[][] result = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            result[i] = new double[a[i].length];
            for (int j = 0; j < a[i].length; j++) result[i][j] = a[i][j].doubleValue();
        }
        return result;
    }
    private static double[][][] toDouble3D(Real[][][] a) {
        double[][][] result = new double[a.length][][];
        for (int i = 0; i < a.length; i++) result[i] = toDouble2D(a[i]);
        return result;
    }
    private static Real[][] toReal2D(double[][] a) {
        Real[][] result = new Real[a.length][];
        for (int i = 0; i < a.length; i++) {
            result[i] = new Real[a[i].length];
            for (int j = 0; j < a[i].length; j++) result[i][j] = Real.of(a[i][j]);
        }
        return result;
    }
    private static Real[][][] toReal3D(double[][][] a) {
        Real[][][] result = new Real[a.length][][];
        for (int i = 0; i < a.length; i++) result[i] = toReal2D(a[i]);
        return result;
    }
}
