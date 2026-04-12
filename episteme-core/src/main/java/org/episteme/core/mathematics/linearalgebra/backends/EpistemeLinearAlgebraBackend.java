package org.episteme.core.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import org.episteme.core.technical.backend.simd.SIMDBackend;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import com.google.auto.service.AutoService;

/**
 * Unified CPU backend for Episteme linear algebra.
 * <p>
 * This backend acts as a foundation (socle) and relay, delegating to optimized
 * dense and sparse providers while ensuring a robust Java environment.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({LinearAlgebraProvider.class, SparseLinearAlgebraProvider.class, Backend.class})
public class EpistemeLinearAlgebraBackend<E> implements SparseLinearAlgebraProvider<E>, SIMDBackend {

    private final CPUDenseLinearAlgebraProvider<E> denseProvider;
    private final CPUSparseLinearAlgebraProvider<E> sparseProvider;

    @SuppressWarnings("unchecked")
    public EpistemeLinearAlgebraBackend() {
        this.denseProvider = new CPUDenseLinearAlgebraProvider<>(null);
        this.sparseProvider = new CPUSparseLinearAlgebraProvider<>(null);
    }

    public EpistemeLinearAlgebraBackend(org.episteme.core.mathematics.structures.rings.Ring<E> ring) {
        this.denseProvider = new CPUDenseLinearAlgebraProvider<>((ring instanceof org.episteme.core.mathematics.structures.rings.Field) ? (org.episteme.core.mathematics.structures.rings.Field<E>)ring : null);
        this.sparseProvider = new CPUSparseLinearAlgebraProvider<>(ring);
    }

    @Override
    public String getId() {
        return "episteme";
    }

    @Override
    public String getName() {
        return "Episteme CPU (Unified)";
    }

    @Override
    public String getDescription() {
        return "Unified CPU-based linear algebra backend providing both dense (SIMD) and sparse optimized operations.";
    }

    @Override
    public boolean isAvailable() {
        return !isExplicitlyDisabled();
    }

    @Override
    public int getPriority() {
        return 0; // Final fallback foundation
    }

    @Override
    public boolean isCompatible(Ring<?> ring) {
        if (ring == null) return false;
        // Optimized check for supported CPU implementations (Real and Complex)
        Object zero = ring.zero();
        return ring instanceof org.episteme.core.mathematics.sets.Reals ||
               ring instanceof org.episteme.core.mathematics.sets.Complexes ||
               zero instanceof org.episteme.core.mathematics.numbers.real.Real ||
               zero instanceof org.episteme.core.mathematics.numbers.complex.Complex;
    }

    @Override
    public ExecutionContext createContext() {
        return new CPUExecutionContext();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public String getType() {
        return "math";
    }

    @Override
    public void shutdown() {
        // Resolve conflict between AlgorithmProvider and Backend
        denseProvider.shutdown();
        sparseProvider.shutdown();
    }

    // --- Delegation Logic ---

    private LinearAlgebraProvider<E> getBestProvider(Matrix<E> a) {
        Class<? extends LinearAlgebraProvider> providerClass = (a instanceof SparseMatrix) ? SparseLinearAlgebraProvider.class : LinearAlgebraProvider.class;
        try {
            LinearAlgebraProvider<E> best = (LinearAlgebraProvider<E>) AlgorithmManager.getProvider(providerClass);
            if (best == this || best.getClass().equals(this.getClass())) {
                return (LinearAlgebraProvider<E>) AlgorithmManager.getNextProvider(providerClass, this);
            }
            return best;
        } catch (Exception e) {
            // Fallback to internal foundation components
            return (a instanceof SparseMatrix) ? (LinearAlgebraProvider<E>) sparseProvider : (LinearAlgebraProvider<E>) denseProvider;
        }
    }

    private <T> T executeComplexOperation(java.util.function.Function<LinearAlgebraProvider<E>, T> operation) {
        try {
            return operation.apply((LinearAlgebraProvider<E>) AlgorithmManager.getProvider(LinearAlgebraProvider.class));
        } catch (ClassCastException | UnsupportedOperationException e) {
            return operation.apply(denseProvider);
        }
    }



    @Override
    public Vector<E> add(Vector<E> a, Vector<E> b) {
        return AlgorithmManager.executeWithFallback(LinearAlgebraProvider.class, p -> ((LinearAlgebraProvider<E>)p).add(a, b));
    }

    @Override
    public Vector<E> subtract(Vector<E> a, Vector<E> b) {
        return AlgorithmManager.executeWithFallback(LinearAlgebraProvider.class, p -> ((LinearAlgebraProvider<E>)p).subtract(a, b));
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        return AlgorithmManager.executeWithFallback(LinearAlgebraProvider.class, p -> ((LinearAlgebraProvider<E>)p).multiply(vector, scalar));
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return AlgorithmManager.executeWithFallback(LinearAlgebraProvider.class, p -> ((LinearAlgebraProvider<E>)p).dot(a, b));
    }

    @Override
    public E norm(Vector<E> a) {
        return AlgorithmManager.executeWithFallback(LinearAlgebraProvider.class, p -> ((LinearAlgebraProvider<E>)p).norm(a));
    }

    @Override
    public Matrix<E> add(Matrix<E> a, Matrix<E> b) {
        return getBestProvider(a).add(a, b);
    }

    @Override
    public Matrix<E> subtract(Matrix<E> a, Matrix<E> b) {
        return getBestProvider(a).subtract(a, b);
    }

    @Override
    public Matrix<E> multiply(Matrix<E> a, Matrix<E> b) {
        return getBestProvider(a).multiply(a, b);
    }

    @Override
    public Vector<E> multiply(Matrix<E> a, Vector<E> b) {
        return getBestProvider(a).multiply(a, b);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return getBestProvider(a).transpose(a);
    }

    @Override
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return getBestProvider(a).scale(scalar, a);
    }

    @Override
    public E determinant(Matrix<E> a) {
        return getBestProvider(a).determinant(a);
    }

    @Override
    public Matrix<E> inverse(Matrix<E> a) {
        return getBestProvider(a).inverse(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return getBestProvider(a).solve(a, b);
    }

    @Override
    public LUResult<E> lu(Matrix<E> a) {
        return getBestProvider(a).lu(a);
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return getBestProvider(a).qr(a);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return getBestProvider(a).svd(a);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return getBestProvider(a).cholesky(a);
    }


    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return denseProvider.eigen(a);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return denseProvider.solve(lu, b);
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return denseProvider.solve(qr, b);
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return denseProvider.solve(cholesky, b);
    }

    @Override
    public Vector<E> bicgstab(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return sparseProvider.bicgstab(a, b, x0, tolerance, maxIterations);
    }

    @Override
    public Vector<E> conjugateGradient(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations) {
        return sparseProvider.conjugateGradient(a, b, x0, tolerance, maxIterations);
    }

    @Override
    public Vector<E> gmres(Matrix<E> a, Vector<E> b, Vector<E> x0, E tolerance, int maxIterations, int restarts) {
        return sparseProvider.gmres(a, b, x0, tolerance, maxIterations, restarts);
    }

    // ---- SIMDBackend ---

    @Override
    public String getSimdLevel() {
        return "GENERIC";
    }

    @Override
    public int getPreferredVectorWidth() {
        return 32;
    }
}
