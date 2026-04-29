package org.episteme.core.mathematics.linearalgebra;

import org.junit.jupiter.api.Test;
import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.technical.algorithm.AlgorithmManager;

public class TestNormAudit {
    @Test
    public void testNorm() {
        try {
            MathContext.normal().compute(() -> {
                LinearAlgebraProvider<Real> ref = new org.episteme.core.mathematics.linearalgebra.backends.EpistemeLinearAlgebraBackend<>(Reals.getInstance());
                
                Vector<Real> v = new org.episteme.core.mathematics.linearalgebra.vectors.GenericVector<>(
                    AlgorithmManager.getRegistry().createVectorStorage(10, Reals.getInstance(), 1.0),
                    ref, Reals.getInstance()
                );
                
                System.out.println("Vector created: " + v);
                Real norm = ref.norm(v);
                System.out.println("Norm: " + norm);
                return null;
            });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }
}
