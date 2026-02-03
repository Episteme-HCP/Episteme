package org.jscience.core.mathematics.ml.neural.autograd;

import org.jscience.core.mathematics.linearalgebra.tensors.DenseTensor;
import org.jscience.core.mathematics.numbers.real.Real;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphNodeTest {

    @Test
    public void testSimpleAddition() {
        Real[] d1 = {Real.of(2.0), Real.of(3.0)};
        Real[] d2 = {Real.of(10.0), Real.of(20.0)};
        
        GraphNode<Real> x = new GraphNode<>(new DenseTensor<>(d1, 2));
        GraphNode<Real> y = new GraphNode<>(new DenseTensor<>(d2, 2));
        
        GraphNode<Real> z = x.add(y);
        GraphNode<Real> loss = z.sum();
        
        loss.backward();
        
        // d(loss)/dz = [1, 1]
        // d(loss)/dx = d(loss)/dz * dz/dx = [1, 1] * 1 = [1, 1]
        assertNotNull(x.getGrad());
        assertEquals(Real.ONE, x.getGrad().get(0));
        assertEquals(Real.ONE, x.getGrad().get(1));
    }

    @Test
    public void testMatMulBackward() {
        // A (2x2), B (2x1)
        Real[] dataA = {
            Real.of(1.0), Real.of(2.0),
            Real.of(3.0), Real.of(4.0)
        };
        Real[] dataB = {
            Real.of(5.0),
            Real.of(6.0)
        };
        
        GraphNode<Real> A = new GraphNode<>(new DenseTensor<>(dataA, 2, 2));
        GraphNode<Real> B = new GraphNode<>(new DenseTensor<>(dataB, 2, 1));
        
        GraphNode<Real> C = A.matmul(B); // Result: [1*5+2*6, 3*5+4*6] = [17, 39]
        GraphNode<Real> loss = C.sum();
        
        loss.backward();
        
        // d(loss)/dC = [1, 1] (column vector)
        // d(loss)/dA = (d(loss)/dC) * B^T = [1, 1]^T * [5, 6] = [[5, 6], [5, 6]]
        assertNotNull(A.getGrad());
        assertEquals(Real.of(5.0), A.getGrad().get(0, 0));
        assertEquals(Real.of(6.0), A.getGrad().get(0, 1));
        assertEquals(Real.of(5.0), A.getGrad().get(1, 0));
        assertEquals(Real.of(6.0), A.getGrad().get(1, 1));
        
        // d(loss)/dB = A^T * (d(loss)/dC) = [[1, 3], [2, 4]] * [1, 1] = [4, 6]
        assertNotNull(B.getGrad());
        assertEquals(Real.of(4.0), B.getGrad().get(0, 0));
        assertEquals(Real.of(6.0), B.getGrad().get(1, 0));
    }

    @Test
    public void testReluBackward() {
        Real[] d = {Real.of(-2.0), Real.of(3.0)};
        GraphNode<Real> x = new GraphNode<>(new DenseTensor<>(d, 2));
        
        GraphNode<Real> y = x.relu();
        GraphNode<Real> loss = y.sum();
        
        loss.backward();
        
        // y = [0, 3]
        // dy/dx = [0, 1]
        assertNotNull(x.getGrad());
        assertEquals(Real.ZERO, x.getGrad().get(0));
        assertEquals(Real.ONE, x.getGrad().get(1));
    }
}
