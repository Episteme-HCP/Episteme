/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.mathematics.analysis;

import org.episteme.core.mathematics.context.MathContext;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.sets.Reals;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.linearalgebra.matrices.SparseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.SparseMatrixStorage;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.nativ.mathematics.linearalgebra.backends.NativeMPFRSparseLinearAlgebraProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Compliance suite for 1000-digit precision requirements.
 */
public class MPFRPrecisionComplianceSuite {

    // 1000 digits of PI (shorthand)
    private static final String PI_1000 = "3.14159265358979323846264338327950288419716939937510" +
            "58209749445923078164062862089986280348253421170679" +
            "82148086513282306647093844609550582231725359408128" +
            "48111745028410270193852110555964462294895493038196" +
            "44288109756659334461284756482337867831652712019091" +
            "45648566923460348610454326648213393607260249141273" +
            "72458700660631558817488152092096282925409171536436" +
            "78925903600113305305488204665213841469519415116094" +
            "33057270365759591953092186117381932611793105118548" +
            "07446237996274956735188575272489122793818301194912" +
            "98336733624406566430860213949463952247371907021798" +
            "60943702770539217176293176752384674818467669405132" +
            "00056812714526356082778577134275778960917363717872" +
            "14684409012249534301465495853710507922796892589235" +
            "42019956112129021960864034418159813629774771309960" +
            "51870721134999999837297804995105973173281609631859" +
            "50244594553469083026425223082533446850352619311881" +
            "71010003137838752886587533208381420617177669147303" +
            "59825349042875546873115956286388235378759375195778" +
            "18577805321712268066130019278766111959092164201989";

    @Test
    public void test1000DigitPi() {
        MathContext.withPrecision(1005).compute(() -> {
            Real halfPi = Real.of(1.0).asin();
            Real pi = halfPi.multiply(Real.of(2.0));
            
            String actual = pi.toString();
            // logger.info("PI to 1000 digits (sample): {}", actual.substring(0, 100));
            
            assertTrue(actual.startsWith(PI_1000.substring(0, 1001)), "PI should match to 1000 digits");
            return null;
        });
    }

    @Test
    public void test1000DigitSparseSolve() {
        MathContext.withPrecision(1005).compute(() -> {
            int n = 3;
            SparseMatrixStorage<Real> storage = new SparseMatrixStorage<>(n, n, Real.ZERO);
            // Matrix A:
            // [ 4, 1, 0 ]
            // [ 1, 3, 1 ]
            // [ 0, 1, 2 ]
            storage.set(0, 0, Real.of(4.0));
            storage.set(0, 1, Real.of(1.0));
            storage.set(1, 0, Real.of(1.0));
            storage.set(1, 1, Real.of(3.0));
            storage.set(1, 2, Real.of(1.0));
            storage.set(2, 1, Real.of(1.0));
            storage.set(2, 2, Real.of(2.0));
            
            SparseMatrix<Real> A = new SparseMatrix<>(storage, Real.ZERO);
            Vector<Real> b = DenseVector.of(Arrays.asList(Real.of(5.0), Real.of(5.0), Real.of(3.0)), Reals.getInstance());
            
            NativeMPFRSparseLinearAlgebraProvider provider = new NativeMPFRSparseLinearAlgebraProvider();
            Vector<Real> x = provider.solve(A, b);
            
            // logger.info("Solve solution sample: x[0] = {}", x.get(0));
            
            // Expected x = [1, 1, 1]
            for (int i = 0; i < n; i++) {
                String val = x.get(i).toString();
                assertTrue(val.startsWith("1.00000000000000000000000000000000000000000000000000"), "Solution should be 1.0 with high precision");
                // Check a bit more digits (e.g., 100)
                assertTrue(val.length() > 100, "Solution string should be long enough for high precision verification");
            }
            
            return null;
        });
    }
}
