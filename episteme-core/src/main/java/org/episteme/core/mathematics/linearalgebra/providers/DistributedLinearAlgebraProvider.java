package org.episteme.core.mathematics.linearalgebra.providers;

import org.episteme.core.distributed.DistributedCompute;
import org.episteme.core.technical.backend.distributed.DistributedContext;
import org.episteme.core.distributed.RemoteDistributedContext;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.algorithms.DistributedSUMMAAlgorithm;
import org.episteme.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
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
@SuppressWarnings("rawtypes")
public class DistributedLinearAlgebraProvider<E> implements SparseLinearAlgebraProvider<E>, Backend {

    public DistributedLinearAlgebraProvider() {
    }
    
    public DistributedLinearAlgebraProvider(Ring<E> ring) {
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        // Currently only supports Real numbers due to DistributedSUMMAAlgorithm limitation
        return ring instanceof Reals || (ring != null && ring.zero() instanceof Real);
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
    private LinearAlgebraProvider<E> getLocalProvider(Ring<E> ring) {
        LinearAlgebraProvider<E> provider = (LinearAlgebraProvider<E>) org.episteme.core.technical.algorithm.ProviderSelector.select(
            LinearAlgebraProvider.class, 
            org.episteme.core.technical.algorithm.OperationContext.DEFAULT, 
            p -> p != this && (ring == null || p.isCompatible(ring))
        );
        if (provider == null) {
            throw new UnsupportedOperationException("No suitable local provider found for " + getName());
        }
        return provider;
    }

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return getLocalProvider(a.getScalarRing()).add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return getLocalProvider(a.getScalarRing()).subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return getLocalProvider(vector.getScalarRing()).multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return getLocalProvider(a.getScalarRing()).dot(a, b);
    }

    @Override
    public E norm(Vector<E> a) {
        return getLocalProvider(a.getScalarRing()).norm(a);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return getLocalProvider(a.getScalarRing()).add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return getLocalProvider(a.getScalarRing()).subtract(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Check heuristics for distribution
        boolean isLarge = (long)a.rows() * a.cols() * b.cols() > 100_000_000;
        
        if (isLarge && a instanceof TiledMatrix && b instanceof TiledMatrix) {
            try {
                // Delegate to DistributedSUMMA
                // Note: result is TiledMatrix (which extends GenericMatrix<Real>)
                TiledMatrix result = DistributedSUMMAAlgorithm.multiply((TiledMatrix) a, (TiledMatrix) b);
                return (Matrix<E>) (Matrix<?>) result;
            } catch (Exception e) {
               // Fallback on error
                System.err.println("Distributed multiplication failed, falling back to local: " + e.getMessage());
            }
        }
        
        return getLocalProvider(a.getScalarRing()).multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return getLocalProvider(a.getScalarRing()).multiply(a, b);
    }
    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).inverse(a);
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).lu(a);
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).qr(a);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).svd(a);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).cholesky(a);
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).eigen(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return getLocalProvider(a.getScalarRing()).solve(a, b);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return getLocalProvider(b.getScalarRing()).solve(lu, b); 
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return getLocalProvider(b.getScalarRing()).solve(qr, b);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return getLocalProvider(b.getScalarRing()).solve(cholesky, b);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).determinant(a);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return getLocalProvider(a.getScalarRing()).scale(scalar, a);
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return getSparseLocalProvider(a.getScalarRing()).bicgstab(a, b, x0, tolerance, maxIterations);
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return getSparseLocalProvider(a.getScalarRing()).conjugateGradient(a, b, x0, tolerance, maxIterations);
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return getSparseLocalProvider(a.getScalarRing()).gmres(a, b, x0, tolerance, maxIterations, restarts);
    }

    @SuppressWarnings("unchecked")
    private SparseLinearAlgebraProvider<E> getSparseLocalProvider(Ring<E> ring) {
        SparseLinearAlgebraProvider<E> provider = (SparseLinearAlgebraProvider<E>) org.episteme.core.technical.algorithm.ProviderSelector.select(
            SparseLinearAlgebraProvider.class, 
            org.episteme.core.technical.algorithm.OperationContext.DEFAULT, 
            p -> p != this && (ring == null || p.isCompatible(ring))
        );
        if (provider == null) {
            // Fallback to LinearAlgebraProvider if no sparse one found (interface defaults will throw)
            return (SparseLinearAlgebraProvider<E>) getLocalProvider(ring);
        }
        return provider;
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
