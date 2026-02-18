/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.opencl;

import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.numbers.complex.Complex;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.FFTProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;


import static org.jocl.CL.*;
import org.jocl.*;

import java.util.logging.Logger;

/**
 * OpenCL implementation of FFTProvider using a Direct Fourier Transform (DFT) kernel.
 * <p>
 * This provides a GPU-accelerated implementation using standard O(N^2) DFT logic for correctness verification.
 * Note: For high performance on large N, an optimized Cooley-Tukey (O(N log N)) kernel is required.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({FFTProvider.class, AlgorithmProvider.class})
public class OpenCLFFTProvider implements FFTProvider {

    private static final Logger LOGGER = Logger.getLogger(OpenCLFFTProvider.class.getName());

    // Simple DFT kernel for correctness/verification on GPU
    private static final String KERNEL_SOURCE = 
        "__kernel void dft_naive(__global double* real, __global double* imag, __global double* outReal, __global double* outImag, int n, int sign) {\n" +
        "    int k = get_global_id(0);\n" +
        "    if (k >= n) return;\n" +
        "    double sumReal = 0;\n" +
        "    double sumImag = 0;\n" +
        "    double arg = sign * 2.0 * 3.14159265358979323846 / (double)n;\n" +
        "    for (int t = 0; t < n; t++) {\n" +
        "        double angle = arg * t * k;\n" +
        "        double cosA = cos(angle);\n" +
        "        double sinA = sin(angle);\n" +
        "        sumReal += real[t] * cosA - imag[t] * sinA;\n" +
        "        sumImag += real[t] * sinA + imag[t] * cosA;\n" +
        "    }\n" +
        "    outReal[k] = sumReal;\n" +
        "    outImag[k] = sumImag;\n" +
        "}\n";

    private final OpenCLBackend backend = new OpenCLBackend();
    private boolean initialized = false;
    private cl_program program;
    private cl_kernel kernel;

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    public synchronized void initialize() {
        if (initialized) return;
        if (!backend.isAvailable()) throw new IllegalStateException("OpenCL not available");

        try {
            OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
            cl_context context = ctx.getContext();

            // Create Program
            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);

            // Create Kernel
            kernel = clCreateKernel(program, "dft_naive", null);

            initialized = true;
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize OpenCL FFT: " + e.getMessage());
            throw new RuntimeException("OpenCL initialization error", e);
        }
    }

    @Override
    public String getName() {
        return "Native OpenCL (DFT GPU)";
    }

    @Override
    public int getPriority() {
        return 20;
    }

    /** Minimum FFT size where GPU outperforms CPU. */
    private static final int GPU_FFT_THRESHOLD = 4096;

    /**
     * Context-aware scoring that accounts for GPU data transfer overhead.
     * <p>
     * GPU FFT is particularly beneficial for large transforms where the
     * O(N log N) compute gain outweighs the host→device transfer cost.
     * </p>
     */
    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_FFT_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        return base;
    }

    @Override
    public double[][] transform(double[] real, double[] imag) {
        return executeDFT(real, imag, -1);
    }

    @Override
    public double[][] inverseTransform(double[] real, double[] imag) {
        double[][] res = executeDFT(real, imag, 1);
        int n = real.length;
        for(int i=0; i<n; i++) {
            res[0][i] /= n;
            res[1][i] /= n;
        }
        return res;
    }

    private double[][] executeDFT(double[] real, double[] imag, int sign) {
        initialize();
        int n = real.length;
        if (imag == null) imag = new double[n]; // Handle pure real input
        
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        // Allocate Input/Output
        cl_mem memReal = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(real), null);
        cl_mem memImag = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(imag), null);
        cl_mem memOutReal = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_double * n, null, null);
        cl_mem memOutImag = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_double * n, null, null);

        try {
            // Set Arguments
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memReal));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memImag));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memOutReal));
            clSetKernelArg(kernel, 3, Sizeof.cl_mem, Pointer.to(memOutImag));
            clSetKernelArg(kernel, 4, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 5, Sizeof.cl_int, Pointer.to(new int[]{sign}));
    
            // Execute
            long[] globalWorkSize = new long[]{n};
            clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);
    
            // Read back
            double[] outReal = new double[n];
            double[] outImag = new double[n];
            clEnqueueReadBuffer(queue, memOutReal, CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(outReal), 0, null, null);
            clEnqueueReadBuffer(queue, memOutImag, CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(outImag), 0, null, null);
            
            return new double[][]{outReal, outImag};

        } finally {
            // Cleanup
            clReleaseMemObject(memReal);
            clReleaseMemObject(memImag);
            clReleaseMemObject(memOutReal);
            clReleaseMemObject(memOutImag);
        }
    }

    @Override
    public Real[][] transform(Real[] real, Real[] imag) {
        int n = real.length;
        double[] r = new double[n];
        double[] i = (imag != null) ? new double[n] : new double[n];
        
        for(int k=0; k<n; k++) r[k] = real[k].doubleValue();
        if (imag != null) {
            for(int k=0; k<n; k++) i[k] = imag[k].doubleValue();
        }
        
        double[][] res = transform(r, i);
        
        Real[] resR = new Real[n];
        Real[] resI = new Real[n];
        for(int k=0; k<n; k++) {
            resR[k] = Real.of(res[0][k]);
            resI[k] = Real.of(res[1][k]);
        }
        return new Real[][]{resR, resI};
    }

    @Override
    public Real[][] inverseTransform(Real[] real, Real[] imag) {
        int n = real.length;
        double[] r = new double[n];
        double[] i = (imag != null) ? new double[n] : new double[n];
        
        for(int k=0; k<n; k++) r[k] = real[k].doubleValue();
        if (imag != null) {
            for(int k=0; k<n; k++) i[k] = imag[k].doubleValue();
        }
        
        double[][] res = inverseTransform(r, i);
        
        Real[] resR = new Real[n];
        Real[] resI = new Real[n];
        for(int k=0; k<n; k++) {
            resR[k] = Real.of(res[0][k]);
            resI[k] = Real.of(res[1][k]);
        }
        return new Real[][]{resR, resI};
    }

    @Override
    public Complex[] transformComplex(Complex[] data) {
        int n = data.length;
        double[] r = new double[n];
        double[] i = new double[n];
        for(int k=0; k<n; k++) {
            r[k] = data[k].getReal().doubleValue();
            i[k] = data[k].getImaginary().doubleValue();
        }
        
        double[][] res = transform(r, i);
        
        Complex[] out = new Complex[n];
        for(int k=0; k<n; k++) {
            out[k] = Complex.of(res[0][k], res[1][k]);
        }
        return out;
    }

    @Override
    public Complex[] inverseTransformComplex(Complex[] data) {
        int n = data.length;
        double[] r = new double[n];
        double[] i = new double[n];
        for(int k=0; k<n; k++) {
            r[k] = data[k].getReal().doubleValue();
            i[k] = data[k].getImaginary().doubleValue();
        }
        
        double[][] res = inverseTransform(r, i);
        
        Complex[] out = new Complex[n];
        for(int k=0; k<n; k++) {
            out[k] = Complex.of(res[0][k], res[1][k]);
        }
        return out;
    }
    @Override
    public double[][][] transform2D(double[][] real, double[][] imag) {
        int rows = real.length;
        int cols = real[0].length;

        // Row-wise 1D FFT
        double[][] rowR = new double[rows][cols];
        double[][] rowI = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            double[][] res = transform(real[r], imag[r]);
            rowR[r] = res[0];
            rowI[r] = res[1];
        }

        // Column-wise 1D FFT
        double[][] outR = new double[rows][cols];
        double[][] outI = new double[rows][cols];
        for (int c = 0; c < cols; c++) {
            double[] colR = new double[rows];
            double[] colI = new double[rows];
            for (int r = 0; r < rows; r++) { colR[r] = rowR[r][c]; colI[r] = rowI[r][c]; }
            double[][] res = transform(colR, colI);
            for (int r = 0; r < rows; r++) { outR[r][c] = res[0][r]; outI[r][c] = res[1][r]; }
        }
        return new double[][][]{outR, outI};
    }

    @Override
    public double[][][] inverseTransform2D(double[][] real, double[][] imag) {
        int rows = real.length;
        int cols = real[0].length;

        double[][] rowR = new double[rows][cols];
        double[][] rowI = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            double[][] res = inverseTransform(real[r], imag[r]);
            rowR[r] = res[0];
            rowI[r] = res[1];
        }

        double[][] outR = new double[rows][cols];
        double[][] outI = new double[rows][cols];
        for (int c = 0; c < cols; c++) {
            double[] colR = new double[rows];
            double[] colI = new double[rows];
            for (int r = 0; r < rows; r++) { colR[r] = rowR[r][c]; colI[r] = rowI[r][c]; }
            double[][] res = inverseTransform(colR, colI);
            for (int r = 0; r < rows; r++) { outR[r][c] = res[0][r]; outI[r][c] = res[1][r]; }
        }
        return new double[][][]{outR, outI};
    }

    @Override
    public double[][][][] transform3D(double[][][] real, double[][][] imag) {
        int d0 = real.length, d1 = real[0].length, d2 = real[0][0].length;

        // Apply 2D FFT to each slice along first dimension
        double[][][] outR = new double[d0][d1][d2];
        double[][][] outI = new double[d0][d1][d2];
        for (int i = 0; i < d0; i++) {
            double[][][] res = transform2D(real[i], imag[i]);
            outR[i] = res[0];
            outI[i] = res[1];
        }

        // FFT along the first dimension
        for (int j = 0; j < d1; j++) {
            for (int k = 0; k < d2; k++) {
                double[] colR = new double[d0];
                double[] colI = new double[d0];
                for (int i = 0; i < d0; i++) { colR[i] = outR[i][j][k]; colI[i] = outI[i][j][k]; }
                double[][] res = transform(colR, colI);
                for (int i = 0; i < d0; i++) { outR[i][j][k] = res[0][i]; outI[i][j][k] = res[1][i]; }
            }
        }
        return new double[][][][]{outR, outI};
    }

    @Override
    public double[][][][] inverseTransform3D(double[][][] real, double[][][] imag) {
        int d0 = real.length, d1 = real[0].length, d2 = real[0][0].length;

        double[][][] outR = new double[d0][d1][d2];
        double[][][] outI = new double[d0][d1][d2];
        for (int i = 0; i < d0; i++) {
            double[][][] res = inverseTransform2D(real[i], imag[i]);
            outR[i] = res[0];
            outI[i] = res[1];
        }

        for (int j = 0; j < d1; j++) {
            for (int k = 0; k < d2; k++) {
                double[] colR = new double[d0];
                double[] colI = new double[d0];
                for (int i = 0; i < d0; i++) { colR[i] = outR[i][j][k]; colI[i] = outI[i][j][k]; }
                double[][] res = inverseTransform(colR, colI);
                for (int i = 0; i < d0; i++) { outR[i][j][k] = res[0][i]; outI[i][j][k] = res[1][i]; }
            }
        }
        return new double[][][][]{outR, outI};
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
        double[][] r = new double[a.length][];
        for (int i = 0; i < a.length; i++) {
            r[i] = new double[a[i].length];
            for (int j = 0; j < a[i].length; j++) r[i][j] = a[i][j].doubleValue();
        }
        return r;
    }
    private static double[][][] toDouble3D(Real[][][] a) {
        double[][][] r = new double[a.length][][];
        for (int i = 0; i < a.length; i++) r[i] = toDouble2D(a[i]);
        return r;
    }
    private static Real[][] toReal2D(double[][] a) {
        Real[][] r = new Real[a.length][];
        for (int i = 0; i < a.length; i++) {
            r[i] = new Real[a[i].length];
            for (int j = 0; j < a[i].length; j++) r[i][j] = Real.of(a[i][j]);
        }
        return r;
    }
    private static Real[][][] toReal3D(double[][][] a) {
        Real[][][] r = new Real[a.length][][];
        for (int i = 0; i < a.length; i++) r[i] = toReal2D(a[i]);
        return r;
    }
}
