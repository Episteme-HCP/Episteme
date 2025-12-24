# JScience GPU Acceleration Setup

## Overview

JScience supports GPU acceleration via CUDA for linear algebra operations when available. GPU mode can deliver 10-100x speedups for large matrix operations.

## Prerequisites

### For GPU Mode (Optional)
- NVIDIA GPU with CUDA capability
- CUDA Toolkit 11.0+ installed
- JOCL library (included in dependencies)

### For CPU Mode (Always Available)
- Java 21+ JDK
- No additional requirements

## Quick Start

### Using Launch Scripts

**Windows**:
```bash
# CPU mode (default, always works)
start_cpu.bat

# GPU mode (requires CUDA)
start_gpu.bat
```

**Linux/Mac**:
```bash
# CPU mode
./start_cpu.sh

# GPU mode  
./start_gpu.sh
```

### Programmatic Configuration

```java
import org.jscience.JScience;
import org.jscience.backend.ComputeMode;

// Set compute mode at startup
JScience.setComputeMode(ComputeMode.GPU);  // or ComputeMode.CPU

// Query current mode
ComputeMode mode = JScience.getComputeMode();
System.out.println("Running in: " + mode);

// Check GPU availability
if (JScience.isGPUAvailable()) {
    System.out.println("GPU acceleration available");
}
```

## Usage Examples

### Matrix Operations

GPU acceleration is automatic once configured:

```java
import org.jscience.mathematics.linear.*;
import org.jscience.mathematics.number.Real;

// Create large matrices
Matrix<Real> A = DenseMatrix.create(1000, 1000);
Matrix<Real> B = DenseMatrix.create(1000, 1000);

// Operations automatically use configured backend (CPU or GPU)
Matrix<Real> C = A.multiply(B);  // Uses GPU if available
Matrix<Real> inv = A.inverse();   // GPU-accelerated
```

### Vector Operations

```java
Vector<Real> v1 = DenseVector.create(10000);
Vector<Real> v2 = DenseVector.create(10000);

Real dotProduct = v1.dot(v2);  // GPU-accelerated if available
```

## Performance Tuning

### When to Use GPU

✅ **Good for GPU**:
- Large matrices (1000×1000+)
- Batch operations
- Iterative algorithms
- Matrix multiplication, decompositions

❌ **CPU may be faster for**:
- Small matrices (<100×100)
- Single operations
- Memory-bound tasks

### Benchmarking

```java
import org.jscience.JScience;

// Benchmark CPU
JScience.setComputeMode(ComputeMode.CPU);
long startCPU = System.nanoTime();
Matrix<Real> resultCPU = largeMatrix.inverse();
long cpuTime = System.nanoTime() - startCPU;

// Benchmark GPU
JScience.setComputeMode(ComputeMode.GPU);
long startGPU = System.nanoTime();
Matrix<Real> resultGPU = largeMatrix.inverse();
long gpuTime = System.nanoTime() - startGPU;

System.out.printf("CPU: %.2f ms, GPU: %.2f ms, Speedup: %.1fx%n",
    cpuTime/1e6, gpuTime/1e6, (double)cpuTime/gpuTime);
```

## Troubleshooting

### GPU Not Available

**If `JScience.isGPUAvailable()` returns `false`**:

1. **Check CUDA installation**:
   ```bash
   nvidia-smi  # Should show GPU info
   nvcc --version  # Should show CUDA version
   ```

2. **Verify JOCL**:
   - Ensure JOCL JAR is in classpath
   - Check for native library loading errors

3. **Fallback to CPU**:
   ```java
   if (!JScience.isGPUAvailable()) {
       System.out.println("GPU not available, using CPU");
       JScience.setComputeMode(ComputeMode.CPU);
   }
   ```

### Performance Issues

**GPU slower than expected**:
- Check matrix size (GPU has overhead for small matrices)
- Verify GPU isn't being used by other processes
- Ensure data transfers are minimized

**Memory errors**:
- GPU has limited memory compared to system RAM
- Reduce matrix sizes or batch smaller
- Use sparse matrices when appropriate

## Configuration Reference

### Compute Modes

| Mode | Description | Availability |
|------|-------------|--------------|
| `ComputeMode.CPU` | Uses Java/CPU | Always available |
| `ComputeMode.GPU` | Uses CUDA/GPU | Requires CUDA setup |

### Math Context

```java
import java.math.MathContext;
import java.math.RoundingMode;

// Set precision for Real number operations
JScience.setMathContext(new MathContext(64, RoundingMode.HALF_EVEN));
```

## Maven Configuration

```xml
<dependencies>
    <!-- JScience core -->
    <dependency>
        <groupId>org.jscience</groupId>
        <artifactId>jscience</artifactId>
        <version>2.0.0</version>
    </dependency>
    
    <!-- GPU support (optional) -->
    <dependency>
        <groupId>org.jocl</groupId>
        <artifactId>jocl</artifactId>
        <version>2.0.4</version>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## Best Practices

1. **Set mode once at startup** - Avoid switching modes frequently
2. **Batch operations** - Group multiple operations together for GPU
3. **Benchmark your workload** - CPU may be faster for your specific case
4. **Handle GPU unavailability** - Always have CPU fallback
5. **Use appropriate precision** - Higher precision = slower computation

## Examples

See `src/test/java/org/jscience/examples/` for complete examples:
- `LinearAlgebraGPUDemo.java` - GPU acceleration demonstration
- `MatrixBenchmark.java` - CPU vs GPU performance comparison

## Support

For issues and questions:
- GitHub Issues: https://github.com/jscience/jscience/issues
- Documentation: https://jscience.org/docs
