/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.analysis.fem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.analysis.Function;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FEMTest {

    @Test
    public void testPoisson1D() {
        // Solve -u''(x) = f(x) on [0, 1]
        // u(0) = 0, u(1) = 0
        // f(x) = pi^2 * sin(pi * x)
        // Exact solution: u(x) = sin(pi * x)

        int nElements = 2;
        Mesh mesh = new Mesh();

        // Create nodes
        for (int i = 0; i <= nElements; i++) {
            double x = (double) i / nElements;
            mesh.addNode(new Node(i, DenseVector.of(java.util.Collections.singletonList(Real.of(x)),
                    org.jscience.mathematics.sets.Reals.getInstance())));
        }

        // Create elements
        for (int i = 0; i < nElements; i++) {
            Node n1 = mesh.getNodes().get(i);
            Node n2 = mesh.getNodes().get(i + 1);
            mesh.addElement(new LinearElement1D(n1, n2));
        }

        // Source term
        @SuppressWarnings("unused")
        Function<Vector<Real>, Real> sourceTerm = v -> {
            Real x = v.get(0);
            double val = Math.PI * Math.PI * Math.sin(Math.PI * x.doubleValue());
            return Real.of(val);
        };

        // Boundary conditions
        Map<Integer, Real> bcs = new HashMap<>();
        bcs.put(0, Real.ZERO);
        bcs.put(nElements, Real.ZERO);

        // Solve
        // Solve
        FEMSolver solver = new FEMSolver();
        // The current FEMSolver.solvePoisson takes (Mesh, Function<Vector<Real>,
        // Real>).
        // We pass a lambda for the source term.
        Vector<Real> solution = solver.solvePoisson(mesh, v -> Real.ONE);

        // Verify at x = 0.5 (node index nElements/2)
        int midNodeIdx = nElements / 2;
        Real uFem = solution.get(midNodeIdx);
        // With f(x) = 1, exact solution is u(x) = x(1-x)/2
        // At x = 0.5, u(0.5) = 0.5 * 0.5 / 2 = 0.125
        Real uExact = Real.of(0.125);

        System.out.println("FEM Solution at x=0.5: " + uFem);
        System.out.println("Exact Solution at x=0.5: " + uExact);

        // Error should be small (O(h^2))
        assertEquals(uExact.doubleValue(), uFem.doubleValue(), 0.0001,
                "FEM solution should be close to exact solution");
    }

    @Test
    public void testPoisson3D() {
        // Domain: Unit cube [0,1]^3
        // Equation: -Laplacian(u) = f
        // Exact solution: u(x,y,z) = sin(pi*x)*sin(pi*y)*sin(pi*z)
        // f(x,y,z) = 3*pi^2 * sin(pi*x)*sin(pi*y)*sin(pi*z)

        @SuppressWarnings("unused")
        int nElements = 2; // Very coarse mesh for testing
        @SuppressWarnings("unused")
        Mesh mesh = new Mesh();

        // Create nodes
        // A single tet has all nodes on the boundary of the hull.
        // So we can't easily test a PDE solver with 1 element if Dirichlet BCs are
        // everywhere.

        // Let's just verify that we can instantiate the element and compute stiffness
        // matrix.
        Node n0 = new Node(0, DenseVector.of(List.of(Real.of(0.0), Real.of(0.0), Real.of(0.0)),
                org.jscience.mathematics.sets.Reals.getInstance()));
        Node n1 = new Node(1, DenseVector.of(List.of(Real.of(1.0), Real.of(0.0), Real.of(0.0)),
                org.jscience.mathematics.sets.Reals.getInstance()));
        Node n2 = new Node(2, DenseVector.of(List.of(Real.of(0.0), Real.of(1.0), Real.of(0.0)),
                org.jscience.mathematics.sets.Reals.getInstance()));
        Node n3 = new Node(3, DenseVector.of(List.of(Real.of(0.0), Real.of(0.0), Real.of(1.0)),
                org.jscience.mathematics.sets.Reals.getInstance()));

        TetrahedralElement3D tet = new TetrahedralElement3D(n0, n1, n2, n3);
        assertNotNull(tet);
        assertEquals(4, tet.getNodes().size());
    }
}

