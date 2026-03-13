import org.jocl.*;
import static org.jocl.CL.*;

public class OpenCLCheck {
    public static void main(String[] args) {
        System.out.println("Checking JOCL/OpenCL...");
        try {
            setExceptionsEnabled(true);
            int[] numPlatformsArray = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            System.out.println("Platforms found: " + numPlatformsArray[0]);

            cl_platform_id[] platforms = new cl_platform_id[numPlatformsArray[0]];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[0];

            cl_device_id[] devices = new cl_device_id[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 1, devices, null);
            cl_device_id device = devices[0];

            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            cl_context context = clCreateContext(contextProperties, 1, devices, null, null, null);
            
            cl_command_queue commandQueue = clCreateCommandQueueWithProperties(context, device, new cl_queue_properties(), null);
            
            int n = 10;
            double[] h_A = new double[n];
            double[] h_B = new double[n];
            for (int i=0; i<n; i++) { h_A[i] = i; h_B[i] = i * 2; }
            
            cl_mem memA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(h_A), null);
            cl_mem memB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_double * n, Pointer.to(h_B), null);
            cl_mem memC = clCreateBuffer(context, CL_MEM_WRITE_ONLY, Sizeof.cl_double * n, null, null);
            
            String src = "__kernel void add(__global const double *a, __global const double *b, __global double *c, int n) { int i = get_global_id(0); if (i < n) c[i] = a[i] + b[i]; }";
            cl_program program = clCreateProgramWithSource(context, 1, new String[]{src}, null, null);
            clBuildProgram(program, 0, null, null, null, null);
            cl_kernel kernel = clCreateKernel(program, "add", null);
            
            clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memA));
            clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memB));
            clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memC));
            clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{n}));
            
            clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, new long[]{n}, null, 0, null, null);
            
            double[] h_C = new double[n];
            clEnqueueReadBuffer(commandQueue, memC, CL_TRUE, 0, n * Sizeof.cl_double, Pointer.to(h_C), 0, null, null);
            
            System.out.println("Result[0]: " + h_C[0]);
            System.out.println("Result[9]: " + h_C[9]);
            
            clReleaseMemObject(memA);
            clReleaseMemObject(memB);
            clReleaseMemObject(memC);
            clReleaseKernel(kernel);
            clReleaseProgram(program);
            clReleaseCommandQueue(commandQueue);
            clReleaseContext(context);
            
            System.out.println("JOCL/OpenCL is working!");
        } catch (Throwable t) {
            System.err.println("JOCL/OpenCL FAILURE!");
            t.printStackTrace();
        }
    }
}
