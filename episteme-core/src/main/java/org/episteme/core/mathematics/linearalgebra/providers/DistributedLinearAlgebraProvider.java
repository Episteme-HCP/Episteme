package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.distributed.DistributedCompute;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.distributed.RemoteDistributedContext;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import com.google.auto.service.AutoService;

/**
 * Linear algebra provider that delegates to distributed algorithms when appropriate.
 * <p>
 * This provider automatically activates when a distributed context is detected and
 * the problem size warrants the overhead of distribution.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@AutoService({LinearAlgebraProvider.class, Backend.class})
public class DistributedLinearAlgebraProvider<E> implements SparseLinearAlgebraProvider<E>, Backend {

    public DistributedLinearAlgebraProvider() {
    }
    
    public DistributedLinearAlgebraProvider(Ring<E> ring) {
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        // Now supports any ring as long as the distributed context is available
        return ring != null;
    }

    @Override
    public int getPriority() {
        DistributedContext ctx = DistributedCompute.getContext();
        if (ctx == null) {
            return 0; // Not available
        }
        
        // If we are in restricted local context with parallelism 1, we are just a wrapper overhead
        if (ctx.getParallelism() <= 1 && !(ctx instanceof RemoteDistributedContext)) {
            return 0; 
        }
        
        // Only prioritize if explicitly configured or potentially useful.
        // Lower to 50 to avoid stealing local high-perf backends (Native=90, ND4J=100)
        return 50; 
    }
    
    @Override
    public String getName() {
        DistributedContext ctx = DistributedCompute.getContext();
        String contextType = (ctx != null) ? ctx.getClass().getSimpleName() : "None";
        return "Distributed Linear Algebra Provider (" + contextType + ")";
    }

    @SuppressWarnings("unchecked")
    private <R> R executeOnLocal(Ring<E> ring, java.util.function.Function<LinearAlgebraProvider<E>, R> operation) {
        org.episteme.core.technical.algorithm.OperationContext.Builder builder = new org.episteme.core.technical.algorithm.OperationContext.Builder();
        boolean isHighPrecision = ring != null && (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex);
        if (isHighPrecision) {
            builder.addHint(org.episteme.core.technical.algorithm.OperationContext.Hint.HIGH_PRECISION);
        }
        
        R result = org.episteme.core.technical.algorithm.ProviderSelector.execute(
            LinearAlgebraProvider.class, 
            builder.build(), 
            p -> {
                if (p == this || (ring != null && !p.isCompatible(ring))) {
                    throw new UnsupportedOperationException("Not suitable");
                }
                org.slf4j.LoggerFactory.getLogger(DistributedLinearAlgebraProvider.class).debug("Selected local provider: {} for operation", p.getName());
                R res = operation.apply((LinearAlgebraProvider<E>) (Object) p);
                if (res == null) {
                    org.slf4j.LoggerFactory.getLogger(DistributedLinearAlgebraProvider.class).warn("Provider {} returned null for operation!", p.getName());
                }
                return res;
            }
        );

        // Safety check to avoid ClassCastException (RealDouble -> RealBig)
        if (isHighPrecision && result instanceof Matrix<?> m) {
            if (m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) {
                throw new UnsupportedOperationException("Fallback to double-precision detected for high-precision request in " + getName());
            }
        }
        if (isHighPrecision && result instanceof Vector<?> v) {
            if (v.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) {
                throw new UnsupportedOperationException("Fallback to double-precision detected for high-precision request in " + getName());
            }
        }

        return result;
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(a.getScalarRing(), p -> p.add(a, b)));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(a.getScalarRing(), p -> p.subtract(a, b)));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return wrap((Vector<E>) executeOnLocal(vector.getScalarRing(), p -> p.multiply(vector, scalar)));
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return executeOnLocal(a.getScalarRing(), p -> p.dot(a, b));
    }

    @Override
    public E norm(Vector<E> a) {
        return executeOnLocal(a.getScalarRing(), p -> p.norm(a));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.add(a, b)));
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.subtract(a, b)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Check heuristics for distribution
        boolean isLarge = (long)a.rows() * a.cols() * b.cols() > 100_000_000;
        
        if (isLarge && a instanceof TiledMatrix && b instanceof TiledMatrix) {
            try {
                // Use the MatrixMultiplicationPlanner for best algorithm selection
                TiledMatrix<E> result = org.episteme.core.mathematics.linearalgebra.algorithms.MatrixMultiplicationPlanner.multiply((TiledMatrix<E>) (Object) a, (TiledMatrix<E>) (Object) b);
                return (Matrix<E>) (Object) result;
            } catch (Exception e) {
               // Fallback on error
                System.err.println("Distributed multiplication failed, falling back to local: " + e.getMessage());
            }
        }
        
        Matrix<E> res = (Matrix<E>) (Object) executeOnLocal(a.getScalarRing(), p -> p.multiply(a, b));
        return wrap(res);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(a.getScalarRing(), p -> p.multiply(a, b)));
    }

    @Override
    public Matrix<E> exp(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.exp(a)));
    }

    @Override
    public Matrix<E> log(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.log(a)));
    }

    @Override
    public Matrix<E> log10(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.log10(a)));
    }

    @Override
    public Matrix<E> sin(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.sin(a)));
    }

    @Override
    public Matrix<E> cos(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.cos(a)));
    }

    @Override
    public Matrix<E> tan(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.tan(a)));
    }

    @Override
    public Matrix<E> asin(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.asin(a)));
    }

    @Override
    public Matrix<E> acos(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.acos(a)));
    }

    @Override
    public Matrix<E> atan(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.atan(a)));
    }

    @Override
    public Matrix<E> sinh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.sinh(a)));
    }

    @Override
    public Matrix<E> cosh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.cosh(a)));
    }

    @Override
    public Matrix<E> tanh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.tanh(a)));
    }

    @Override
    public Matrix<E> asinh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.asinh(a)));
    }

    @Override
    public Matrix<E> acosh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.acosh(a)));
    }

    @Override
    public Matrix<E> atanh(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.atanh(a)));
    }

    @Override
    public Matrix<E> pow(Matrix<E> a, E exponent) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.pow(a, exponent)));
    }

    @Override
    public Matrix<E> sqrt(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.sqrt(a)));
    }

    @Override
    public Matrix<E> cbrt(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.cbrt(a)));
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.inverse(a)));
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        LUResult<E> res = executeOnLocal(a.getScalarRing(), p -> p.lu(a));
        return new LUResult<>(wrap(res.L()), wrap(res.U()), res.P());
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        QRResult<E> res = executeOnLocal(a.getScalarRing(), p -> p.qr(a));
        return new QRResult<>(wrap(res.Q()), wrap(res.R()));
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        SVDResult<E> res = executeOnLocal(a.getScalarRing(), p -> p.svd(a));
        return new SVDResult<>(wrap(res.U()), res.S(), wrap(res.V()));
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        CholeskyResult<E> res = executeOnLocal(a.getScalarRing(), p -> p.cholesky(a));
        return new CholeskyResult<>(wrap(res.L()));
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        EigenResult<E> res = executeOnLocal(a.getScalarRing(), p -> p.eigen(a));
        return new EigenResult<>(wrap(res.V()), res.D());
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(a.getScalarRing(), p -> p.solve(a, b)));
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(b.getScalarRing(), p -> p.solve(lu, b))); 
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(b.getScalarRing(), p -> p.solve(qr, b)));
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return wrap((Vector<E>) executeOnLocal(b.getScalarRing(), p -> p.solve(cholesky, b)));
    }

    @Override
    public E determinant(Matrix<E> a) {
        return executeOnLocal(a.getScalarRing(), p -> p.determinant(a));
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.transpose(a)));
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return wrap((Matrix<E>) executeOnLocal(a.getScalarRing(), p -> p.scale(scalar, a)));
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return wrap((Vector<E>) executeOnSparseLocal(a.getScalarRing(), p -> p.bicgstab(a, b, x0, tolerance, maxIterations)));
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return wrap((Vector<E>) executeOnSparseLocal(a.getScalarRing(), p -> p.conjugateGradient(a, b, x0, tolerance, maxIterations)));
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return wrap((Vector<E>) executeOnSparseLocal(a.getScalarRing(), p -> p.gmres(a, b, x0, tolerance, maxIterations, restarts)));
    }
    
    @SuppressWarnings("unchecked")
    private <R> R executeOnSparseLocal(Ring<E> ring, java.util.function.Function<SparseLinearAlgebraProvider<E>, R> operation) {
        org.episteme.core.technical.algorithm.OperationContext.Builder builder = new org.episteme.core.technical.algorithm.OperationContext.Builder();
        boolean isHighPrecision = ring != null && (ring.zero() instanceof org.episteme.core.mathematics.numbers.real.RealBig || ring.zero() instanceof org.episteme.core.mathematics.numbers.complex.Complex);
        if (isHighPrecision) {
            builder.addHint(org.episteme.core.technical.algorithm.OperationContext.Hint.HIGH_PRECISION);
        }

        R result = org.episteme.core.technical.algorithm.ProviderSelector.execute(
            SparseLinearAlgebraProvider.class, 
            builder.build(), 
            p -> {
                if (p == this) throw new UnsupportedOperationException("Not suitable");
                if (ring != null && !p.isCompatible(ring)) throw new UnsupportedOperationException("Not suitable");
                org.slf4j.LoggerFactory.getLogger(DistributedLinearAlgebraProvider.class).debug("Selected sparse local provider: {} for operation", p.getName());
                R res = operation.apply((SparseLinearAlgebraProvider<E>) (Object) p);
                if (res == null) {
                    org.slf4j.LoggerFactory.getLogger(DistributedLinearAlgebraProvider.class).warn("Sparse provider {} returned null for operation!", p.getName());
                }
                return res;
            }
        );

        if (isHighPrecision && result instanceof Matrix<?> m) {
            if (m.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) {
                throw new UnsupportedOperationException("Fallback to double-precision detected for high-precision request in " + getName());
            }
        }
        if (isHighPrecision && result instanceof Vector<?> v) {
            if (v.getScalarRing().zero() instanceof org.episteme.core.mathematics.numbers.real.RealDouble) {
                throw new UnsupportedOperationException("Fallback to double-precision detected for high-precision request in " + getName());
            }
        }

        return result;
    }

    private Matrix<E> wrap(Matrix<E> m) {
        if (m instanceof org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix) return ((org.episteme.core.mathematics.linearalgebra.matrices.GenericMatrix<E>) m).withProvider(this);
        return m;
    }

    private Vector<E> wrap(Vector<E> v) {
        if (v instanceof org.episteme.core.mathematics.linearalgebra.vectors.GenericVector) return ((org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<E>) v).withProvider(this);
        return v;
    }

    @Override
    public String getStatusMessage() {
        if (DistributedCompute.getContext() != null) {
            return "Active (" + DistributedCompute.getContext().getClass().getSimpleName() + ")";
        }
        return "Inactive - No Distributed Context";
    }

    @Override
    public boolean isAvailable() {
        return DistributedCompute.getContext() != null;
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public String getId() {
        return "distributed";
    }

    @Override
    public String getDescription() {
        return "Linear algebra provider delegating to distributed computation contexts";
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public void shutdown() {
        Backend.super.shutdown();
    }
}
