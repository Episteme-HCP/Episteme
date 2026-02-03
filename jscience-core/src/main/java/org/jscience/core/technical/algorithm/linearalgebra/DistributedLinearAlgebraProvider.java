package org.jscience.core.technical.algorithm.linearalgebra;

import org.jscience.core.distributed.DistributedCompute;
import org.jscience.core.distributed.DistributedContext;
import org.jscience.core.distributed.RemoteDistributedContext;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.linearalgebra.algorithms.DistributedSUMMAAlgorithm;
import org.jscience.core.mathematics.linearalgebra.matrices.TiledMatrix;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.sets.Reals;
import org.jscience.core.mathematics.structures.rings.Ring;
import org.jscience.core.technical.algorithm.LinearAlgebraProvider;

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
public class DistributedLinearAlgebraProvider<E> implements LinearAlgebraProvider<E> {

    // Fallback provider for local or small operations
    private final CPUDenseLinearAlgebraProvider<E> localProvider;

    public DistributedLinearAlgebraProvider() {
        this(null);
    }
    
    public DistributedLinearAlgebraProvider(Ring<E> ring) {
        this.localProvider = new CPUDenseLinearAlgebraProvider<>(ring);
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
        
        // Only prioritize if explicitly configured or potentially useful
        return 200; 
    }
    
    @Override
    public String getName() {
        DistributedContext ctx = DistributedCompute.getContext();
        String contextType = (ctx != null) ? ctx.getClass().getSimpleName() : "None";
        return "Distributed Linear Algebra Provider (" + contextType + ")";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        // Check heuristics for distribution
        boolean isLarge = (long)a.rows() * a.cols() * b.cols() > 1_000_000;
        
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
        
        return localProvider.multiply(a, b);
    }

    // --- Delegation to Local Provider for non-distributed ops ---

    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return localProvider.add(a, b);
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return localProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return localProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return localProvider.dot(a, b);
    }

    @Override
    public E norm(Vector<E> a) {
        return localProvider.norm(a);
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return localProvider.add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return localProvider.subtract(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return localProvider.multiply(a, b);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return localProvider.inverse(a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return localProvider.determinant(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return localProvider.solve(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return localProvider.transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return localProvider.scale(scalar, a);
    }
}
