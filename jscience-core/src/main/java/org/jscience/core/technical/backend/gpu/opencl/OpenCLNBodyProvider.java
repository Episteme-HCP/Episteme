/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.backend.gpu.opencl;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.technical.algorithm.NBodyProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLExecutionContext;
import java.util.logging.Logger;

import org.jocl.cl_context;
import org.jocl.cl_command_queue;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_program;
import org.jocl.Pointer;
import org.jocl.Sizeof;

import static org.jocl.CL.*;

/**
 * GPU-accelerated N-Body simulation provider using OpenCL.
 * Implement O(N^2) brute force interaction.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({NBodyProvider.class, AlgorithmProvider.class})
public class OpenCLNBodyProvider implements NBodyProvider {

    private static final Logger LOGGER = Logger.getLogger(OpenCLNBodyProvider.class.getName());

    // Kernel: x,y,z,mass packed in p[i*4]. v[i*3]. f[i*3].
    private static final String KERNEL_SOURCE = 
        "__kernel void nbody_forces(__global double* p, __global double* v, __global double* f, int n, double dt, double G) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i >= n) return;\n" +
        "    \n" +
        "    double fx = 0;\n" +
        "    double fy = 0;\n" +
        "    double fz = 0;\n" +
        "    \n" +
        "    double pix = p[i*4 + 0];\n" +
        "    double piy = p[i*4 + 1];\n" +
        "    double piz = p[i*4 + 2];\n" +
        "    double mi  = p[i*4 + 3];\n" +
        "    \n" +
        "    for (int j = 0; j < n; j++) {\n" +
        "        if (i == j) continue;\n" +
        "        double dx = p[j*4 + 0] - pix;\n" +
        "        double dy = p[j*4 + 1] - piy;\n" +
        "        double dz = p[j*4 + 2] - piz;\n" +
        "        double mj = p[j*4 + 3];\n" +
        "        double distSqr = dx*dx + dy*dy + dz*dz + 1e-9;\n" +
        "        double dist = sqrt(distSqr);\n" +
        "        double f = (G * mi * mj) / (distSqr * dist);\n" +
        "        fx += f * dx; fy += f * dy; fz += f * dz;\n" +
        "    }\n" +
        "    f[i*3 + 0] = fx;\n" +
        "    f[i*3 + 1] = fy;\n" +
        "    f[i*3 + 2] = fz;\n" +
        "    \n" +
        "    // Simple Euler Integration step within kernel to save transfers\n" +
        "    v[i*3 + 0] += (fx / mi) * dt;\n" +
        "    v[i*3 + 1] += (fy / mi) * dt;\n" +
        "    v[i*3 + 2] += (fz / mi) * dt;\n" +
        "    p[i*4 + 0] += v[i*3 + 0] * dt;\n" +
        "    p[i*4 + 1] += v[i*3 + 1] * dt;\n" +
        "    p[i*4 + 2] += v[i*3 + 2] * dt;\n" +
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
        if (!isAvailable()) throw new IllegalStateException("OpenCL not available");

        try {
            OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
            cl_context context = ctx.getContext();
            
            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            kernel = clCreateKernel(program, "nbody_forces", null);
            
            initialized = true;
        } catch (Exception e) {
            String msg = "OpenCL NBody Init Failed: " + e.getMessage();
            LOGGER.severe(msg);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getName() {
        return "Native OpenCL (O(N^2) GPU)";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        // Interface expects separated arrays. GPU kernel expects packed p[x,y,z,mass].
        // We must bridge.
        if (!initialized) initialize();
        
        int n = masses.length;
        
        // Pack data
        double[] p = new double[n * 4];
        double[] v = new double[n * 3]; // We fake/zero velocity if not provided by interface?
        // Wait, NBodyProvider interface is limited (only pos, mass, force). 
        // It generally implies force calculation only, integration is external.
        // BUT my kernel does integration.
        // If I only calculate forces, I should remove integration from kernel.
        // Let's assume for this method (computeForces), we only return forces.
        // I will zero 'v' and ignore 'dt'.
        // Kernel writes 'f'.
        
        for(int i=0; i<n; i++) {
            p[i*4+0] = positions[i*3+0];
            p[i*4+1] = positions[i*3+1];
            p[i*4+2] = positions[i*3+2];
            p[i*4+3] = masses[i];
        }
        
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        cl_mem memP = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * p.length, Pointer.to(p), null);
        cl_mem memV = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * v.length, Pointer.to(v), null); // Dummy
        cl_mem memF = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * forces.length, null, null);
        
        try {
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memP));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memF));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 4, Sizeof.cl_double, Pointer.to(new double[]{0.0})); // dt=0, no integration
            clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[]{G}));
            
            long[] globalWorkSize = new long[]{n};
            clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);
            
            clEnqueueReadBuffer(queue, memF, CL_TRUE, 0, Sizeof.cl_double * forces.length, Pointer.to(forces), 0, null, null);
            
        } finally {
            clReleaseMemObject(memP);
            clReleaseMemObject(memV);
            clReleaseMemObject(memF);
        }
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        double[] posD = new double[positions.length];
        double[] massD = new double[masses.length];
        double[] forceD = new double[forces.length];

        for (int i = 0; i < positions.length; i++) posD[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) massD[i] = masses[i].doubleValue();

        computeForces(posD, massD, forceD, G.doubleValue(), softening.doubleValue());

        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(forceD[i]);
    }
}
