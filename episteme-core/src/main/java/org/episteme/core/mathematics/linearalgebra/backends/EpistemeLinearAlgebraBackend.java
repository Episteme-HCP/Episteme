package org.episteme.core.mathematics.linearalgebra.backends;

import java.util.List;
import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.CholeskyResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.EigenResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.LUResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.QRResult;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.SVDResult;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.SparseLinearAlgebraProvider;
import org.episteme.core.technical.algorithm.AlgorithmManager;
import org.episteme.core.technical.algorithm.OperationContext;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import org.episteme.core.technical.backend.simd.SIMDBackend;
import com.google.auto.service.AutoService;
import org.episteme.core.mathematics.structures.rings.Ring;

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
@AutoService(Backend.class)
public class EpistemeLinearAlgebraBackend<E> implements SparseLinearAlgebraProvider<E>, SIMDBackend, CPUBackend {

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
        return "Episteme CPU Foundation";
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }

    @Override
    public String getType() {
        return "cpu-simd";
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
    public void shutdown() {
        // Resolve conflict between AlgorithmProvider and Backend
        denseProvider.shutdown();
        sparseProvider.shutdown();
    }

    @Override
    public String getSimdLevel() {
        return "GENERIC";
    }

    @Override
    public int getPreferredVectorWidth() {
        return 32; // Default to 256-bit
    }

    // --- Delegation Logic ---

    @SuppressWarnings("unchecked")
    private LinearAlgebraProvider<E> getBestProvider(Matrix<E> a) {
        // Strict delegation: prioritize specialized backends, fallback to foundation
        Class providerClass = (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) ? SparseLinearAlgebraProvider.class : LinearAlgebraProvider.class;
        LinearAlgebraProvider<E> internal = (a instanceof org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix) ? (LinearAlgebraProvider<E>) sparseProvider : (LinearAlgebraProvider<E>) denseProvider;

        try {
            List<LinearAlgebraProvider> available = AlgorithmManager.getProviders(providerClass);
            LinearAlgebraProvider<E> best = (LinearAlgebraProvider<E>) available.stream()
                .filter(p -> !p.getClass().equals(this.getClass()) && !(p instanceof EpistemeLinearAlgebraBackend))
                .findFirst()
                .orElse(null);

            if (best == null || best == this || best.getClass().equals(this.getClass())) {
                return internal;
            }
            return best;
        } catch (Exception e) {
            return internal;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T executeComplexOperation(java.util.function.Function<LinearAlgebraProvider<E>, T> operation) {
        try {
            List<LinearAlgebraProvider> available = AlgorithmManager.getProviders(LinearAlgebraProvider.class);
            LinearAlgebraProvider<E> best = (LinearAlgebraProvider<E>) available.stream()
                .filter(p -> !p.getClass().equals(this.getClass()) && !(p instanceof EpistemeLinearAlgebraBackend))
                .findFirst()
                .orElse(null);

            if (best == null || best == this || best.getClass().equals(this.getClass())) {
                return operation.apply(denseProvider);
            }
            return operation.apply(best);
        } catch (Exception e) {
            return operation.apply(denseProvider);
        }
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
    public Matrix<E> scale(E scalar, Matrix<E> a) {
        return getBestProvider(a).scale(scalar, a);
    }

    @Override
    public Vector<E> multiply(Vector<E> vector, E scalar) {
        // Vectors don't have sparse variants in current SPI, route to dense
        return denseProvider.multiply(vector, scalar);
    }

    @Override
    public E dot(Vector<E> a, Vector<E> b) {
        return denseProvider.dot(a, b);
    }

    @Override
    public E norm(Vector<E> a) {
        return denseProvider.norm(a);
    }

    @Override
    public Matrix<E> transpose(Matrix<E> a) {
        return getBestProvider(a).transpose(a);
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
    public LUResult<E> lu(Matrix<E> a) {
        return getBestProvider(a).lu(a);
    }

    @Override
    public QRResult<E> qr(Matrix<E> a) {
        return getBestProvider(a).qr(a);
    }

    @Override
    public CholeskyResult<E> cholesky(Matrix<E> a) {
        return getBestProvider(a).cholesky(a);
    }

    @Override
    public EigenResult<E> eigen(Matrix<E> a) {
        return getBestProvider(a).eigen(a);
    }

    @Override
    public SVDResult<E> svd(Matrix<E> a) {
        return getBestProvider(a).svd(a);
    }

    @Override
    public Vector<E> solve(Matrix<E> a, Vector<E> b) {
        return getBestProvider(a).solve(a, b);
    }

    @Override
    public Vector<E> solve(LUResult<E> lu, Vector<E> b) {
        return executeComplexOperation(p -> p.solve(lu, b));
    }

    @Override
    public Vector<E> solve(QRResult<E> qr, Vector<E> b) {
        return executeComplexOperation(p -> p.solve(qr, b));
    }

    @Override
    public Vector<E> solve(CholeskyResult<E> cholesky, Vector<E> b) {
        return executeComplexOperation(p -> p.solve(cholesky, b));
    }

    @Override
    public void close() {
        shutdown();
    }
}
