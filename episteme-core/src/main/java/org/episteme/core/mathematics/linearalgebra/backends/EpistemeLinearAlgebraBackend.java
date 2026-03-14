package org.episteme.core.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.linearalgebra.LinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.Matrix;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.solvers.*;
import org.episteme.core.mathematics.linearalgebra.providers.CPUDenseLinearAlgebraProvider;
import org.episteme.core.mathematics.linearalgebra.providers.CPUSparseLinearAlgebraProvider;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.structures.rings.Ring;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.BackendDiscovery;
import org.episteme.core.technical.backend.ExecutionContext;
import org.episteme.core.technical.backend.cpu.CPUExecutionContext;
import org.episteme.core.technical.backend.simd.SIMDBackend;
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
@AutoService({Backend.class, SIMDBackend.class, LinearAlgebraProvider.class})
public class EpistemeLinearAlgebraBackend implements SIMDBackend, LinearAlgebraProvider<Real> {

    private final CPUDenseLinearAlgebraProvider<Real> denseProvider = new CPUDenseLinearAlgebraProvider<>();
    private final CPUSparseLinearAlgebraProvider<Real> sparseProvider = new CPUSparseLinearAlgebraProvider<>();

    @Override
    public String getType() {
        return BackendDiscovery.TYPE_LINEAR_ALGEBRA;
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
        return denseProvider.isCompatible(ring);
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

    // --- Delegation Logic ---

    private LinearAlgebraProvider<Real> getProvider(Matrix<Real> a) {
        return (a instanceof SparseMatrix) ? sparseProvider : denseProvider;
    }

    @Override
    public Vector<Real> add(Vector<Real> a, Vector<Real> b) {
        return denseProvider.add(a, b);
    }

    @Override
    public Vector<Real> subtract(Vector<Real> a, Vector<Real> b) {
        return denseProvider.subtract(a, b);
    }

    @Override
    public Vector<Real> multiply(Vector<Real> vector, Real scalar) {
        return denseProvider.multiply(vector, scalar);
    }

    @Override
    public Real dot(Vector<Real> a, Vector<Real> b) {
        return denseProvider.dot(a, b);
    }

    @Override
    public Real norm(Vector<Real> a) {
        return denseProvider.norm(a);
    }

    @Override
    public Matrix<Real> add(Matrix<Real> a, Matrix<Real> b) {
        return getProvider(a).add(a, b);
    }

    @Override
    public Matrix<Real> subtract(Matrix<Real> a, Matrix<Real> b) {
        return getProvider(a).subtract(a, b);
    }

    @Override
    public Matrix<Real> multiply(Matrix<Real> a, Matrix<Real> b) {
        return getProvider(a).multiply(a, b);
    }

    @Override
    public Vector<Real> multiply(Matrix<Real> a, Vector<Real> b) {
        return getProvider(a).multiply(a, b);
    }

    @Override
    public Matrix<Real> transpose(Matrix<Real> a) {
        return getProvider(a).transpose(a);
    }

    @Override
    public Matrix<Real> scale(Real scalar, Matrix<Real> a) {
        return getProvider(a).scale(scalar, a);
    }

    @Override
    public Real determinant(Matrix<Real> a) {
        return denseProvider.determinant(a);
    }

    @Override
    public Matrix<Real> inverse(Matrix<Real> a) {
        return denseProvider.inverse(a);
    }

    @Override
    public Vector<Real> solve(Matrix<Real> a, Vector<Real> b) {
        return getProvider(a).solve(a, b);
    }

    @Override
    public LUResult<Real> lu(Matrix<Real> a) {
        return denseProvider.lu(a);
    }

    @Override
    public QRResult<Real> qr(Matrix<Real> a) {
        return denseProvider.qr(a);
    }

    @Override
    public SVDResult<Real> svd(Matrix<Real> a) {
        return denseProvider.svd(a);
    }

    @Override
    public CholeskyResult<Real> cholesky(Matrix<Real> a) {
        return denseProvider.cholesky(a);
    }

    @Override
    public EigenResult<Real> eigen(Matrix<Real> a) {
        return denseProvider.eigen(a);
    }

    @Override
    public Vector<Real> solve(LUResult<Real> lu, Vector<Real> b) {
        return denseProvider.solve(lu, b);
    }

    @Override
    public Vector<Real> solve(QRResult<Real> qr, Vector<Real> b) {
        return denseProvider.solve(qr, b);
    }

    @Override
    public Vector<Real> solve(CholeskyResult<Real> cholesky, Vector<Real> b) {
        return denseProvider.solve(cholesky, b);
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
