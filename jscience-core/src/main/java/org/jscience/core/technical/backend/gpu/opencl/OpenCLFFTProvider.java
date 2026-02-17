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
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLExecutionContext;

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
}
