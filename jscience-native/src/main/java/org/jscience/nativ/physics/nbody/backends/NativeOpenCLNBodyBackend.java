/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.nbody.backends;

import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.physics.classical.mechanics.nbody.NBodyProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.algorithm.OperationContext;

import java.util.logging.Logger;
import org.jocl.cl_context;
import org.jocl.cl_command_queue;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_program;
import org.jocl.Pointer;
import org.jocl.Sizeof;

import static org.jocl.CL.*;
import org.jscience.nativ.mathematics.linearalgebra.backends.NativeOpenCLSparseLinearAlgebraBackend;
import org.jscience.nativ.mathematics.linearalgebra.backends.OpenCLExecutionContext;

import org.jscience.core.technical.backend.gpu.GPUBackend;
import org.jscience.core.technical.backend.ComputeBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ExecutionContext;
import java.nio.DoubleBuffer;

/**
 * GPU-accelerated N-Body simulation backend using OpenCL.
 * <p>
 * Uses O(N²) brute-force for small particle counts ({@code n < GPU_NBODY_THRESHOLD})
 * and O(N log N) Barnes-Hut for large counts.  The octree is built on the CPU;
 * force traversal runs on the GPU via a dedicated OpenCL kernel.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({NBodyProvider.class, AlgorithmProvider.class, GPUBackend.class, ComputeBackend.class, Backend.class})
public class NativeOpenCLNBodyBackend implements NBodyProvider, GPUBackend {

    // ------------------------------------------------------------------ //
    //  Inner class: CPU Barnes-Hut Octree                                //
    // ------------------------------------------------------------------ //

    /**
     * Lightweight oct-tree node.
     * Children are indexed 0-7 by (x>cx)|(y>cy)<<1|(z>cz)<<2.
     * Leaf nodes store a single body; internal nodes aggregate center-of-mass.
     */
    private static final class BHNode {
        double cx, cy, cz, size;             // bounding-box center and half-side-length
        double totalMass, comX, comY, comZ;  // aggregate center of mass
        int bodyIdx = -1;                    // >= 0 for leaves only
        final BHNode[] children = new BHNode[8];

        BHNode(double cx, double cy, double cz, double size) {
            this.cx = cx; this.cy = cy; this.cz = cz; this.size = size;
        }

        void insert(double[] p, double[] masses, int idx) {
            if (bodyIdx < 0 && children[0] == null) {
                bodyIdx = idx;
                totalMass = masses[idx];
                comX = p[idx*3]; comY = p[idx*3+1]; comZ = p[idx*3+2];
                return;
            }
            if (children[0] == null) {
                int old = bodyIdx; bodyIdx = -1;
                createChildren();
                insertIntoChild(p, masses, old);
            }
            insertIntoChild(p, masses, idx);
            double nm = totalMass + masses[idx];
            comX = (comX*totalMass + p[idx*3]   * masses[idx]) / nm;
            comY = (comY*totalMass + p[idx*3+1] * masses[idx]) / nm;
            comZ = (comZ*totalMass + p[idx*3+2] * masses[idx]) / nm;
            totalMass = nm;
        }

        private void createChildren() {
            double h = size * 0.5;
            for (int o = 0; o < 8; o++)
                children[o] = new BHNode(
                    cx + ((o & 1) != 0 ? h : -h),
                    cy + ((o & 2) != 0 ? h : -h),
                    cz + ((o & 4) != 0 ? h : -h), h);
        }

        private void insertIntoChild(double[] p, double[] masses, int idx) {
            int oct = ((p[idx*3]   > cx) ? 1 : 0)
                    | ((p[idx*3+1] > cy) ? 2 : 0)
                    | ((p[idx*3+2] > cz) ? 4 : 0);
            children[oct].insert(p, masses, idx);
        }
    }

    /**
     * Flatten the octree BFS-order into a float[nodeCount*7] array for OpenCL.
     * Per-node layout: [comX, comY, comZ, mass, size, childStart, bodyIdx]
     * where childStart=-1 means leaf, and bodyIdx=-1 for internal nodes.
     */
    static float[] flattenTree(BHNode root, int capacityHint) {
        java.util.ArrayDeque<BHNode> queue = new java.util.ArrayDeque<>(capacityHint * 2);
        java.util.ArrayList<BHNode> ordered = new java.util.ArrayList<>(capacityHint * 2);
        queue.add(root);
        while (!queue.isEmpty()) {
            BHNode nd = queue.poll();
            ordered.add(nd);
            if (nd.children[0] != null)
                for (BHNode c : nd.children) queue.add(c);
        }
        java.util.IdentityHashMap<BHNode, Integer> idx = new java.util.IdentityHashMap<>(ordered.size());
        for (int i = 0; i < ordered.size(); i++) idx.put(ordered.get(i), i);

        float[] flat = new float[ordered.size() * 7];
        for (int i = 0; i < ordered.size(); i++) {
            BHNode nd = ordered.get(i);
            int base = i * 7;
            flat[base]   = (float) nd.comX;
            flat[base+1] = (float) nd.comY;
            flat[base+2] = (float) nd.comZ;
            flat[base+3] = (float) nd.totalMass;
            flat[base+4] = (float) nd.size;
            flat[base+5] = nd.children[0] != null ? (float)(int) idx.get(nd.children[0]) : -1f;
            flat[base+6] = nd.bodyIdx;
        }
        return flat;
    }


    private static final Logger LOGGER = Logger.getLogger(NativeOpenCLNBodyBackend.class.getName());

    /** Minimum particle count where GPU N-body outperforms CPU. */
    private static final int GPU_NBODY_THRESHOLD = 500;

    private static final String KERNEL_SOURCE = 
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
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

    /**
     * Barnes-Hut kernel: each work-item traverses the flattened BH octree.
     * nodes[i*7]: comX, comY, comZ, totalMass, size, childStart (-1=leaf), bodyIdx.
     * bodies[i*4]: px, py, pz, mass (used only for self-exclusion via bodyIdx).
     * theta = opening angle criterion (typically 0.5).
     */
    private static final String BH_KERNEL_SOURCE =
        "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
        "__kernel void barnes_hut(\n" +
        "    __global const float* nodes, int nodeCount,\n" +
        "    __global const double* bodies, int n,\n" +
        "    __global double* f, double G, float theta, double softening) {\n" +
        "    int i = get_global_id(0);\n" +
        "    if (i >= n) return;\n" +
        "    double pix = bodies[i*4+0], piy = bodies[i*4+1], piz = bodies[i*4+2], mi = bodies[i*4+3];\n" +
        "    double fx=0, fy=0, fz=0;\n" +
        "    // Iterative traversal via explicit stack (avoid recursion on GPU)\n" +
        "    int stack[256]; int top = 0; stack[top++] = 0;\n" +
        "    while (top > 0) {\n" +
        "        int ni = stack[--top];\n" +
        "        if (ni < 0 || ni >= nodeCount) continue;\n" +
        "        int base = ni * 7;\n" +
        "        float comX = nodes[base+0], comY = nodes[base+1], comZ = nodes[base+2];\n" +
        "        float mj   = nodes[base+3], sz   = nodes[base+4];\n" +
        "        int childStart = (int)nodes[base+5], bodyIdx = (int)nodes[base+6];\n" +
        "        // Skip self-interaction at leaves\n" +
        "        if (childStart < 0 && bodyIdx == i) continue;\n" +
        "        double dx = comX - pix, dy = comY - piy, dz = comZ - piz;\n" +
        "        double dist2 = dx*dx + dy*dy + dz*dz + softening;\n" +
        "        double dist  = sqrt(dist2);\n" +
        "        // Barnes-Hut criterion or leaf: apply force\n" +
        "        if (childStart < 0 || (sz / dist) < theta) {\n" +
        "            double fmag = (G * mi * mj) / (dist2 * dist);\n" +
        "            fx += fmag * dx; fy += fmag * dy; fz += fmag * dz;\n" +
        "        } else {\n" +
        "            // Push 8 children\n" +
        "            for (int c = 0; c < 8 && top < 248; c++) stack[top++] = childStart + c;\n" +
        "        }\n" +
        "    }\n" +
        "    f[i*3+0]=fx; f[i*3+1]=fy; f[i*3+2]=fz;\n" +
        "}\n";


    private final NativeOpenCLSparseLinearAlgebraBackend backend = new NativeOpenCLSparseLinearAlgebraBackend();
    private boolean initialized = false;
    private cl_program program;
    private cl_kernel kernel;     // O(N²) brute-force
    private cl_program bhProgram;
    private cl_kernel bhKernel;   // O(N log N) Barnes-Hut

    /** Barnes-Hut opening angle criterion (s/d < theta => use COM approximation). */
    private static final float BH_THETA = 0.5f;

    @Override
    public org.jscience.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.jscience.core.technical.backend.HardwareAccelerator.GPU;
    }
 
    @Override
    public String getAlgorithmType() {
        return "mechanics";
    }

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

            // Brute-force kernel
            program = clCreateProgramWithSource(context, 1, new String[]{KERNEL_SOURCE}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            kernel = clCreateKernel(program, "nbody_forces", null);

            // Barnes-Hut kernel
            bhProgram = clCreateProgramWithSource(context, 1, new String[]{BH_KERNEL_SOURCE}, null, null);
            clBuildProgram(bhProgram, 0, null, null, null, null);
            bhKernel = clCreateKernel(bhProgram, "barnes_hut", null);

            initialized = true;
        } catch (Exception e) {
            LOGGER.warning("OpenCL NBody init failed (device may lack fp64 support): " + e.getMessage());
            initialized = false;
        }
    }

    @Override
    public String getName() {
        return "Native OpenCL (O(N^2) GPU)";
    }


    @Override
    public DeviceInfo[] getDevices() {
        return new DeviceInfo[0];
    }

    @Override
    public void selectDevice(int deviceId) {}

    @Override
    public long allocateGPUMemory(long sizeBytes) { return 0; }

    @Override
    public void copyToGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void copyFromGPU(long gpuHandle, DoubleBuffer hostBuffer, long sizeBytes) {}

    @Override
    public void freeGPUMemory(long gpuHandle) {}

    @Override
    public void synchronize() {}

    @Override
    public void matrixMultiply(DoubleBuffer A, DoubleBuffer B, DoubleBuffer C, int m, int n, int k) {}

    @Override
    public ExecutionContext createContext() {
        return backend.createContext();
    }

    @Override
    public int getPriority() {
        return 50;
    }

    /**
     * Context-aware scoring that accounts for GPU data transfer overhead.
     * <p>
     * GPU is only beneficial for large data sizes where the compute gain
     * outweighs the host→device transfer cost.
     * </p>
     */
    @Override
    public double score(OperationContext context) {
        if (!isAvailable()) return -1;
        double base = getPriority();
        if (context.getDataSize() < GPU_NBODY_THRESHOLD) base -= 100;
        if (context.hasHint(OperationContext.Hint.GPU_RESIDENT)) base += 30;
        if (context.hasHint(OperationContext.Hint.BATCH)) base += 20;
        if (context.hasHint(OperationContext.Hint.LOW_LATENCY)) base -= 50;
        return base;
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
         // Fallback to double[] for now
         double[] dPos = new double[positions.length];
         for (int i = 0; i < positions.length; i++) dPos[i] = positions[i].doubleValue();
         double[] dMasses = new double[masses.length];
         for (int i = 0; i < masses.length; i++) dMasses[i] = masses[i].doubleValue();
         double[] dForces = new double[forces.length];
         
         computeForces(dPos, dMasses, dForces, G.doubleValue(), softening.doubleValue());
         
         for (int i = 0; i < forces.length; i++) forces[i] = Real.of(dForces[i]);
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        if (!initialized) initialize();
        int n = masses.length;

        if (n >= GPU_NBODY_THRESHOLD) {
            computeForcesBarnesHut(positions, masses, forces, G, softening);
            return;
        }

        // Brute-force O(N²) path for small N
        double[] p = new double[n * 4];
        double[] v = new double[n * 3];
        for (int i = 0; i < n; i++) {
            p[i*4]   = positions[i*3];
            p[i*4+1] = positions[i*3+1];
            p[i*4+2] = positions[i*3+2];
            p[i*4+3] = masses[i];
        }

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();
        cl_mem memP = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * p.length, Pointer.to(p), null);
        cl_mem memV = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * v.length, Pointer.to(v), null);
        cl_mem memF = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * forces.length, null, null);
        try {
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memP));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memF));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 4, Sizeof.cl_double, Pointer.to(new double[]{0.0}));
            clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[]{G}));
            clEnqueueNDRangeKernel(queue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memF, CL_TRUE, 0, Sizeof.cl_double * forces.length, Pointer.to(forces), 0, null, null);
        } finally {
            clReleaseMemObject(memP);
            clReleaseMemObject(memV);
            clReleaseMemObject(memF);
        }
    }

    /**
     * O(N log N) Barnes-Hut force computation.
     * CPU builds and flattens the octree; GPU traverses it.
     */
    private void computeForcesBarnesHut(double[] positions, double[] masses, double[] forces, double G, double softening) {
        int n = masses.length;

        // Find bounding box
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minX = Math.min(minX, positions[i*3]); maxX = Math.max(maxX, positions[i*3]);
            minY = Math.min(minY, positions[i*3+1]); maxY = Math.max(maxY, positions[i*3+1]);
            minZ = Math.min(minZ, positions[i*3+2]); maxZ = Math.max(maxZ, positions[i*3+2]);
        }
        double size = Math.max(Math.max(maxX-minX, maxY-minY), maxZ-minZ) * 0.5 + 1e-10;
        BHNode root = new BHNode((minX+maxX)*0.5, (minY+maxY)*0.5, (minZ+maxZ)*0.5, size);
        for (int i = 0; i < n; i++) root.insert(positions, masses, i);
        float[] flatNodes = flattenTree(root, n);
        int nodeCount = flatNodes.length / 7;

        // Pack body data (x,y,z,mass) for self-exclusion in kernel
        double[] bodies = new double[n * 4];
        for (int i = 0; i < n; i++) {
            bodies[i*4] = positions[i*3]; bodies[i*4+1] = positions[i*3+1];
            bodies[i*4+2] = positions[i*3+2]; bodies[i*4+3] = masses[i];
        }

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();
        cl_mem memNodes   = clCreateBuffer(context, CL_MEM_READ_ONLY  | CL_MEM_COPY_HOST_PTR, (long) Sizeof.cl_float  * flatNodes.length, Pointer.to(flatNodes), null);
        cl_mem memBodies  = clCreateBuffer(context, CL_MEM_READ_ONLY  | CL_MEM_COPY_HOST_PTR, (long) Sizeof.cl_double * bodies.length,    Pointer.to(bodies),    null);
        cl_mem memForces  = clCreateBuffer(context, CL_MEM_WRITE_ONLY,                        (long) Sizeof.cl_double * forces.length,    null,                  null);
        try {
            clSetKernelArg(bhKernel, 0, Sizeof.cl_mem,    Pointer.to(memNodes));
            clSetKernelArg(bhKernel, 1, Sizeof.cl_int,    Pointer.to(new int[]{nodeCount}));
            clSetKernelArg(bhKernel, 2, Sizeof.cl_mem,    Pointer.to(memBodies));
            clSetKernelArg(bhKernel, 3, Sizeof.cl_int,    Pointer.to(new int[]{n}));
            clSetKernelArg(bhKernel, 4, Sizeof.cl_mem,    Pointer.to(memForces));
            clSetKernelArg(bhKernel, 5, Sizeof.cl_double, Pointer.to(new double[]{G}));
            clSetKernelArg(bhKernel, 6, Sizeof.cl_float,  Pointer.to(new float[]{BH_THETA}));
            clSetKernelArg(bhKernel, 7, Sizeof.cl_double, Pointer.to(new double[]{softening}));
            clEnqueueNDRangeKernel(queue, bhKernel, 1, null, new long[]{n}, null, 0, null, null);
            clEnqueueReadBuffer(queue, memForces, CL_TRUE, 0, (long) Sizeof.cl_double * forces.length, Pointer.to(forces), 0, null, null);
        } finally {
            clReleaseMemObject(memNodes);
            clReleaseMemObject(memBodies);
            clReleaseMemObject(memForces);
        }
    }


    @Override
    public void step(double[] positions, double[] velocities, double[] masses, int numBodies, double G, double dt, double softening) {
        if (!initialized) initialize();
        
        // This provider should implement the full step in OpenCL for consistent performance.
        // For now, if we can't do it in one kernel, we fail or implement it properly.
        // The current kernel ALREADY does integration if dt > 0.
        
        // Refactor: use the kernel integration.
        int n = masses.length;
        double[] p = new double[n * 4];
        double[] v = new double[n * 3];
        for(int i=0; i<n; i++) {
            p[i*4+0] = positions[i*3+0];
            p[i*4+1] = positions[i*3+1];
            p[i*4+2] = positions[i*3+2];
            p[i*4+3] = masses[i];
            v[i*3+0] = velocities[i*3+0];
            v[i*3+1] = velocities[i*3+1];
            v[i*3+2] = velocities[i*3+2];
        }

        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context context = ctx.getContext();
        cl_command_queue queue = ctx.getCommandQueue();

        cl_mem memP = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * p.length, Pointer.to(p), null);
        cl_mem memV = clCreateBuffer(context, CL_MEM_READ_WRITE | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * v.length, Pointer.to(v), null);
        cl_mem memF = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * n * 3, null, null);
        
        try {
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memP));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memV));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memF));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            clSetKernelArg(kernel, 4, Sizeof.cl_double, Pointer.to(new double[]{dt}));
            clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[]{G}));
            
            long[] globalWorkSize = new long[]{n};
            clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);
            
            // Read back pos and vel
            clEnqueueReadBuffer(queue, memP, CL_TRUE, 0, Sizeof.cl_double * p.length, Pointer.to(p), 0, null, null);
            clEnqueueReadBuffer(queue, memV, CL_TRUE, 0, Sizeof.cl_double * v.length, Pointer.to(v), 0, null, null);
            
            for(int i=0; i<n; i++) {
                positions[i*3+0] = p[i*4+0];
                positions[i*3+1] = p[i*4+1];
                positions[i*3+2] = p[i*4+2];
                velocities[i*3+0] = v[i*3+0];
                velocities[i*3+1] = v[i*3+1];
                velocities[i*3+2] = v[i*3+2];
            }
        } finally {
            clReleaseMemObject(memP);
            clReleaseMemObject(memV);
            clReleaseMemObject(memF);
        }
    }

    @Override
    public void stepReal(Real[] positions, Real[] velocities, Real[] masses, int numBodies, Real G, Real dt, Real softening) {
        computeForces(positions, masses, new Real[numBodies * 3], Real.ONE, softening);
        // ...
    }

}
