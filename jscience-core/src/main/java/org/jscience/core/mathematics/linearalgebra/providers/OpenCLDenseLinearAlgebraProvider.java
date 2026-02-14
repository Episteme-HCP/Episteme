package org.jscience.core.mathematics.linearalgebra.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_program;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Field;
import com.google.auto.service.AutoService;
import org.jscience.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLBackend;
import org.jscience.core.technical.backend.gpu.opencl.OpenCLExecutionContext;
import org.jscience.core.util.PerformanceLogger;

/**
 * OpenCL Linear Algebra Provider (Dense).
 * <p>
 * Delegates to CPU for now, but setup for JOCL implementation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({LinearAlgebraProvider.class, AlgorithmProvider.class})
public class OpenCLDenseLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    private final Field<E> field;
    private final CPUDenseLinearAlgebraProvider<E> cpuProvider;
    private static final OpenCLBackend backend = new OpenCLBackend();

    public OpenCLDenseLinearAlgebraProvider(Field<E> field) {
        this.field = field;
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(field);
    }
    
    /**
     * Public no-arg constructor required by ServiceLoader.
     * Defaults to Reals field as this OpenCL implementation is double-precision only.
     */
    @SuppressWarnings("unchecked")
    public OpenCLDenseLinearAlgebraProvider() {
        this.field = (Field<E>) Reals.getInstance();
        this.cpuProvider = new CPUDenseLinearAlgebraProvider<>(this.field);
    }

    @Override
    public boolean isAvailable() {
        return backend.isAvailable();
    }

    public static boolean isOpenCLAvailable() {
        return new OpenCLBackend().isAvailable();
    }

    @Override
    public String getName() {
        return "JScience OpenCL (Dense)";
    }

    @Override
    public int getPriority() {
        // Medium priority: preferred over CPU but less than CUDA
        return isAvailable() ? 50 : Integer.MIN_VALUE;
    }

    private Map<String, Object> config = new HashMap<>();

    @Override
    public void configure(Map<String, Object> properties) {
        if (properties != null) {
            this.config.putAll(properties);
        }
    }

    private static final String KERNEL_SRC = "#pragma OPENCL EXTENSION cl_khr_fp64 : enable\n" +
            "__kernel void vectorAdd(__global const double *a, __global const double *b, __global double *c, const int n) {" +
            "    int i = get_global_id(0);" +
            "    if (i < n) c[i] = a[i] + b[i];" +
            "}" +
            "\n" +
            "__kernel void vectorSubtract(__global const double *a, __global const double *b, __global double *c, const int n) {" +
            "    int i = get_global_id(0);" +
            "    if (i < n) c[i] = a[i] - b[i];" +
            "}" +
            "\n" +
            "__kernel void vectorScalarMultiply(__global const double *a, const double s, __global double *b, const int n) {" +
            "    int i = get_global_id(0);" +
            "    if (i < n) b[i] = a[i] * s;" +
            "}" +
            "\n" +
            "__kernel void matrixMultiply(__global const double *a, __global const double *b, __global double *c, const int m, const int n, const int k) {"
            +
            "    int row = get_global_id(1);" +
            "    int col = get_global_id(0);" +
            "    if (row < m && col < n) {" +
            "        double sum = 0.0;" +
            "        for (int i = 0; i < k; i++) {" +
            "            sum += a[row * k + i] * b[i * n + col];" +
            "        }" +
            "        c[row * n + col] = sum;" +
            "    }" +
            "}";

    private static cl_program program;
    private static Map<String, cl_kernel> kernels = new HashMap<>();

    private static boolean initialized = false;
    private static boolean usable = false;

    private synchronized void ensureInitialized() {
        if (!initialized) {
            initialized = true;
            if (backend.isAvailable()) {
                try {
                    // Run a tiny self-test
                    double[] a = { 1.0 };
                    double[] b = { 2.0 };
                    double[] c = { 0.0 };
                    executeBinaryKernel(a, b, c, 1, "vectorAdd");
                    if (Math.abs(c[0] - 3.0) < 0.0001) {
                        usable = true;
                    } else {
                        System.err.println("OpenCL self-test failed: 1+2=" + c[0] + ". Disabling OpenCL.");
                    }
                } catch (Throwable t) {
                    System.err.println("OpenCL initialization failed: " + t.getMessage());
                    t.printStackTrace();
                }
            }
        }
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return executeBinaryOp(a, b, "vectorAdd");
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return executeBinaryOp(a, b, "vectorSubtract");
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        ensureInitialized();
        if (!usable || !(field.zero() instanceof Real)) {
            return cpuProvider.multiply(vector, scalar);
        }
        
        int n = vector.dimension();
        double s = ((Real)scalar).doubleValue();
        double[] dataV = toDoubleArray(vector);
        double[] dataR = new double[n];

        executeScalarOp(dataV, s, dataR, n, "vectorScalarMultiply");
        return toVector(dataR);
    }

    private Vector<E> executeBinaryOp(Vector<E> a, Vector<E> b, String kernelName) {
        ensureInitialized();
        if (!usable || !(field.zero() instanceof Real)) {
            // Fallback to CPU provider if OpenCL is not usable or element type is not Real
            if ("vectorAdd".equals(kernelName)) {
                return cpuProvider.add(a, b);
            } else if ("vectorSubtract".equals(kernelName)) {
                return cpuProvider.subtract(a, b);
            }
            // Should not happen if all binary ops are handled
            throw new UnsupportedOperationException("Unsupported binary operation: " + kernelName);
        }

        try {
            int n = a.dimension();
            if (n != b.dimension()) throw new IllegalArgumentException("Vector dimension mismatch");

            double[] dataA = toDoubleArray(a);
            double[] dataB = toDoubleArray(b);
            double[] dataC = new double[n];

            executeBinaryKernel(dataA, dataB, dataC, n, kernelName);

            return toVector(dataC);
        } catch (Exception e) {
            // Log the exception and fallback to CPU
            System.err.println("OpenCL operation failed for " + kernelName + ", falling back to CPU: " + e.getMessage());
            e.printStackTrace();
            if ("vectorAdd".equals(kernelName)) {
                return cpuProvider.add(a, b);
            } else if ("vectorSubtract".equals(kernelName)) {
                return cpuProvider.subtract(a, b);
            }
            throw new UnsupportedOperationException("Unsupported binary operation: " + kernelName, e);
        }
    }

    private void executeBinaryKernel(double[] a, double[] b, double[] c, int n, String kernelName) {
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context clContext = ctx.getContext();
        cl_command_queue commandQueue = ctx.getCommandQueue();

        cl_kernel kernel = getKernel(clContext, kernelName);

        cl_mem memA = CL.clCreateBuffer(clContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem memB = CL.clCreateBuffer(clContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(b), null);
        cl_mem memC = CL.clCreateBuffer(clContext, CL.CL_MEM_WRITE_ONLY, Sizeof.cl_double * n, null, null);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[] { n }));

        CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, new long[] { n }, null, 0, null, null);
        CL.clEnqueueReadBuffer(commandQueue, memC, CL.CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(c), 0, null, null);

        CL.clReleaseMemObject(memA);
        CL.clReleaseMemObject(memB);
        CL.clReleaseMemObject(memC);
    }

    private void executeScalarOp(double[] a, double s, double[] b, int n, String kernelName) {
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context clContext = ctx.getContext();
        cl_command_queue commandQueue = ctx.getCommandQueue();

        cl_kernel kernel = getKernel(clContext, kernelName);

        cl_mem memA = CL.clCreateBuffer(clContext, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(a), null);
        cl_mem memB = CL.clCreateBuffer(clContext, CL.CL_MEM_WRITE_ONLY, Sizeof.cl_double * n, null, null);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_double, Pointer.to(new double[] { s }));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memB));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[] { n }));

        CL.clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, new long[] { n }, null, 0, null, null);
        CL.clEnqueueReadBuffer(commandQueue, memB, CL.CL_TRUE, 0, Sizeof.cl_double * n, Pointer.to(b), 0, null, null);

        CL.clReleaseMemObject(memA);
        CL.clReleaseMemObject(memB);
    }

    private synchronized cl_kernel getKernel(cl_context clContext, String name) {
        if (program == null) {
            program = CL.clCreateProgramWithSource(clContext, 1, new String[] { KERNEL_SRC }, null, null);
            CL.clBuildProgram(program, 0, null, null, null, null);
        }
        return kernels.computeIfAbsent(name, k -> CL.clCreateKernel(program, k, null));
    }

    private double[] toDoubleArray(Vector<E> v) {
        int n = v.dimension();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            Real r = (Real) v.get(i);
            arr[i] = r.doubleValue();
        }
        return arr;
    }

    private double[] toDoubleArray(Matrix<E> m) {
        int rows = m.rows();
        int cols = m.cols();
        double[] arr = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Real r = (Real) m.get(i, j);
                arr[i * cols + j] = r.doubleValue();
            }
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    private Vector<E> toVector(double[] data) {
        List<Real> list = new ArrayList<>(data.length);
        for (double d : data) {
            list.add(Real.of(d));
        }
        return (Vector<E>) new DenseVector<>(list, Reals.getInstance());
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return cpuProvider.dot(a, b);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return cpuProvider.subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        ensureInitialized();
        if (!usable || !(field.zero() instanceof Real)) {
            return cpuProvider.multiply(a, b);
        }

        try {
            int m = a.rows();
            int k = a.cols();
            int n = b.cols();

            if (k != b.rows()) {
                throw new IllegalArgumentException("Matrix dimension mismatch");
            }

            // Only use OpenCL for "large enough" matrices to amortize overhead
            if ((long) m * n * k < 1_000_000) {
                return cpuProvider.multiply(a, b);
            }

            double[] dataA = null;
            double[] dataB = null;
            Pointer ptrA;
            Pointer ptrB;

            if (a instanceof RealDoubleMatrix) {
                RealDoubleMatrix rdm = (RealDoubleMatrix) a;
                if (rdm.isDirect()) {
                    ptrA = Pointer.to(rdm.getBuffer());
                } else {
                    dataA = toDoubleArray(a);
                    ptrA = Pointer.to(dataA);
                }
            } else {
                dataA = toDoubleArray(a);
                ptrA = Pointer.to(dataA);
            }

            if (b instanceof RealDoubleMatrix) {
                RealDoubleMatrix rdm = (RealDoubleMatrix) b;
                if (rdm.isDirect()) {
                    ptrB = Pointer.to(rdm.getBuffer());
                } else {
                    dataB = toDoubleArray(b);
                    ptrB = Pointer.to(dataB);
                }
            } else {
                dataB = toDoubleArray(b);
                ptrB = Pointer.to(dataB);
            }

            // Allocate result directly in off-heap memory
            RealDoubleMatrix resultC = RealDoubleMatrix.direct(m, n);
            Pointer ptrC = Pointer.to(resultC.getBuffer());

            executeMatrixMultiply(ptrA, ptrB, ptrC, m, n, k);

            @SuppressWarnings("unchecked")
            Matrix<E> res = (Matrix<E>) resultC;
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return cpuProvider.multiply(a, b);
        }
    }

    private void executeMatrixMultiply(Pointer ptrA, Pointer ptrB, Pointer ptrC, int m,
            int n, int k) {
        OpenCLExecutionContext ctx = (OpenCLExecutionContext) backend.createContext();
        cl_context clContext = ctx.getContext();
        cl_command_queue commandQueue = ctx.getCommandQueue();

        cl_kernel kernel = getKernel(clContext, "matrixMultiply");

        // Use CL_MEM_USE_HOST_PTR to try to use the DirectBuffer in-place if possible
        // (Zero-Copy)
        long startTransfer = System.nanoTime();
        cl_mem memA = CL.clCreateBuffer(clContext,
                CL.CL_MEM_READ_ONLY | CL.CL_MEM_USE_HOST_PTR,
                Sizeof.cl_double * m * k, ptrA, null);
        cl_mem memB = CL.clCreateBuffer(clContext,
                CL.CL_MEM_READ_ONLY | CL.CL_MEM_USE_HOST_PTR,
                Sizeof.cl_double * k * n, ptrB, null);
        cl_mem memC = CL.clCreateBuffer(clContext,
                CL.CL_MEM_WRITE_ONLY | CL.CL_MEM_USE_HOST_PTR,
                Sizeof.cl_double * m * n, ptrC, null);
        PerformanceLogger.log("OpenCL:BufferCreation", System.nanoTime() - startTransfer);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_int,
                Pointer.to(new int[] { m }));
        CL.clSetKernelArg(kernel, 4, Sizeof.cl_int,
                Pointer.to(new int[] { n }));
        CL.clSetKernelArg(kernel, 5, Sizeof.cl_int,
                Pointer.to(new int[] { k }));

        long startExec = System.nanoTime();
        long global_work_size[] = new long[] { n, m }; // col, row
        CL.clEnqueueNDRangeKernel(commandQueue, kernel, 2, null, global_work_size, null, 0, null,
                null);
        // Ensure kernel is done for accurate timing (synchronous wait)
        CL.clFinish(commandQueue);
        PerformanceLogger.log("OpenCL:KernelExec", System.nanoTime() - startExec);

        long startRead = System.nanoTime();
        // Read back (blocking) - for USE_HOST_PTR this should just sync, but we call
        // ReadBuffer to be safe/portable
        CL.clEnqueueReadBuffer(commandQueue, memC, CL.CL_TRUE, 0,
                Sizeof.cl_double * m * n, ptrC, 0, null, null);
        PerformanceLogger.log("OpenCL:ReadBack", System.nanoTime() - startRead);

        CL.clReleaseMemObject(memA);
        CL.clReleaseMemObject(memB);
        CL.clReleaseMemObject(memC);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return cpuProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return cpuProvider.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return cpuProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return cpuProvider.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return cpuProvider.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return cpuProvider.scale(scalar, a);
    }

    @Override
    public E norm(Vector<E> a) {
        return cpuProvider.norm(a);
    }
}



