/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.mathematics.algebra.algebras;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.algebra.algebras.CliffordAlgebra.Multivector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CliffordAlgebraTest {

    @Test
    public void testComplexNumbersMetric() {
        // Cl(0,1) with metric e1*e1 = -1
        // Analogous to Complex numbers where i = e1
        Real[] metric = { Real.ONE.negate() };
        CliffordAlgebra<Real> algebra = new CliffordAlgebra<>(Real.ZERO, 1, metric);
        
        Multivector<Real> one = algebra.one();
        Multivector<Real> i = algebra.getBasisBlade(1); // e1 (mask 1)
        
        // i^2 should be -1
        Multivector<Real> iSq = i.geometricProduct(i);
        // toString returns map representation e.g. {0=-1.0}
        assertTrue(iSq.toString().contains("-1.0"), "i^2 should contain -1.0, got: " + iSq);
        
        // (1+i)(1-i) = 1 - i^2 = 1 - (-1) = 2
        Multivector<Real> a = one.add(i);
        Multivector<Real> b = one.add(i.negate());
        Multivector<Real> product = a.geometricProduct(b);
        
        assertTrue(product.toString().contains("2.0"), "Product should be 2.0, got: " + product);
    }

    @Test
    public void testNonCommutativity() {
        // Cl(2,0) Euclidean plane: e1*e1=1, e2*e2=1
        Real[] metric = { Real.ONE, Real.ONE };
        CliffordAlgebra<Real> algebra = new CliffordAlgebra<>(Real.ZERO, 2, metric);
        
        Multivector<Real> e1 = algebra.getBasisBlade(1); // mask 1
        Multivector<Real> e2 = algebra.getBasisBlade(2); // mask 2 (bit 1)
        
        // e1 e2
        Multivector<Real> e1e2 = e1.geometricProduct(e2);
        // e2 e1
        Multivector<Real> e2e1 = e2.geometricProduct(e1);
        
        // In geometric algebra with orthogonal basis, e1 e2 = - e2 e1
        Multivector<Real> sum = e1e2.add(e2e1);
        
        // Sum should be zero
        assertEquals("0", sum.toString(), "e1e2 + e2e1 should be 0");
    }
    
    @Test
    public void testEuclideanMetric() {
         // e1*e1 = 1
         Real[] metric = { Real.ONE };
         CliffordAlgebra<Real> algebra = new CliffordAlgebra<>(Real.ZERO, 1, metric);
         Multivector<Real> e1 = algebra.getBasisBlade(1);
         Multivector<Real> sq = e1.geometricProduct(e1);
         assertTrue(sq.toString().contains("1.0"), "e1^2 should be 1.0");
    }
}
