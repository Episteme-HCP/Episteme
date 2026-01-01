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

import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.analysis.Function;

/**
 * A simple Finite Element Method solver.
 * <p>
 * Currently supports solving the Poisson equation: -div(grad u) = f.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FEMSolver {

    /**
     * Solves the Poisson equation on the given mesh.
     * 
     * @param mesh       the finite element mesh
     * @param sourceTerm the source term function f(x)
     * @return the solution vector u at the nodes
     */
    public Vector<Real> solvePoisson(Mesh mesh, Function<Vector<Real>, Real> sourceTerm) {
        int nNodes = mesh.getNodes().size();
        mesh.indexNodes(); // Ensure nodes are indexed

        // Initialize global stiffness matrix K and load vector F
        // Using DenseMatrix for simplicity, but SparseMatrix would be better for large
        // systems
        List<List<Real>> kRows = new ArrayList<>(nNodes);
        List<Real> fData = new ArrayList<>(nNodes);

        for (int i = 0; i < nNodes; i++) {
            List<Real> row = new ArrayList<>(nNodes);
            for (int j = 0; j < nNodes; j++) {
                row.add(Real.ZERO);
            }
            kRows.add(row);
            fData.add(Real.ZERO);
        }

        // Assemble K and F
        for (Element element : mesh.getElements()) {
            List<Node> nodes = element.getNodes();
            int nElemNodes = nodes.size();

            for (QuadraturePoint qp : element.getQuadraturePoints()) {
                Vector<Real> xi = qp.getCoordinates();
                Real weight = qp.getWeight();

                Matrix<Real> J = element.computeJacobian(xi);
                Real detJ = J.determinant().abs();
                Matrix<Real> invJ = J.inverse();

                // Compute global coordinates of the quadrature point for source term evaluation
                Vector<Real> globalPoint = computeGlobalCoordinates(element, xi);

                // For each pair of nodes in the element
                for (int i = 0; i < nElemNodes; i++) {
                    int globalI = nodes.get(i).getGlobalIndex();
                    Vector<Real> gradNi_local = element.getShapeFunctions().get(i).gradient(xi);
                    // Map gradient to global coordinates: gradN_global = invJ^T * gradN_local
                    Vector<Real> gradNi_global = invJ.transpose().multiply(gradNi_local);

                    // Update Load Vector F
                    // F_i += N_i * f(x) * detJ * weight
                    Real Ni = element.getShapeFunctions().get(i).evaluate(xi);
                    Real fVal = sourceTerm.evaluate(globalPoint);
                    Real fi = fData.get(globalI).add(Ni.multiply(fVal).multiply(detJ).multiply(weight));
                    fData.set(globalI, fi);

                    for (int j = 0; j < nElemNodes; j++) {
                        int globalJ = nodes.get(j).getGlobalIndex();
                        Vector<Real> gradNj_local = element.getShapeFunctions().get(j).gradient(xi);
                        Vector<Real> gradNj_global = invJ.transpose().multiply(gradNj_local);

                        // K_ij += (gradNi . gradNj) * detJ * weight
                        Real dotProd = dot(gradNi_global, gradNj_global);
                        Real val = dotProd.multiply(detJ).multiply(weight);

                        Real currentK = kRows.get(globalI).get(globalJ);
                        kRows.get(globalI).set(globalJ, currentK.add(val));
                    }
                }
            }
        }

        // Apply Boundary Conditions (Dirichlet u=0 at first and last node for 1D test)
        // This is a very simplified BC application
        applyBoundaryConditions(kRows, fData, mesh);

        Matrix<Real> K = new DenseMatrix<>(kRows, Real.ZERO);
        Vector<Real> F = new DenseVector<>(fData, Real.ZERO);

        // Solve Ku = F
        return K.inverse().multiply(F);
    }

    /**
     * Applies Dirichlet boundary conditions to the system.
     * 
     * @param K    the global stiffness matrix (as list of rows)
     * @param F    the global load vector
     * @param mesh the mesh
     */
    private void applyBoundaryConditions(List<List<Real>> K, List<Real> F, Mesh mesh) {
        // Simple hack: Fix first and last node to 0
        int[] fixedNodes = { 0, mesh.getNodes().size() - 1 };

        for (int nodeIdx : fixedNodes) {
            // Zero out the row
            for (int j = 0; j < K.size(); j++) {
                K.get(nodeIdx).set(j, Real.ZERO);
            }
            // Set diagonal to 1
            K.get(nodeIdx).set(nodeIdx, Real.ONE);
            // Set RHS to 0
            F.set(nodeIdx, Real.ZERO);
        }
    }

    private Vector<Real> computeGlobalCoordinates(Element element, Vector<Real> xi) {
        List<Node> nodes = element.getNodes();
        int n = nodes.size();
        if (n == 0)
            return null; // Should not happen

        // We need to sum Ni(xi) * node_coords
        // Assuming Node has getCoordinates() returning Vector<Real>
        // and Vector has scale and add.

        // Initialize with first node contribution
        Real N0 = element.getShapeFunctions().get(0).evaluate(xi);
        Vector<Real> globalPos = scaleVector(nodes.get(0).getCoordinates(), N0);

        for (int i = 1; i < n; i++) {
            Real Ni = element.getShapeFunctions().get(i).evaluate(xi);
            Vector<Real> contribution = scaleVector(nodes.get(i).getCoordinates(), Ni);
            globalPos = addVectors(globalPos, contribution);
        }
        return globalPos;
    }

    // Helper for vector scaling since Vector interface might be read-only or
    // generic
    private Vector<Real> scaleVector(Vector<Real> v, Real s) {
        List<Real> data = new ArrayList<>();
        for (int i = 0; i < v.dimension(); i++) {
            data.add(v.get(i).multiply(s));
        }
        return new DenseVector<>(data, Real.ZERO);
    }

    // Helper for vector addition
    private Vector<Real> addVectors(Vector<Real> a, Vector<Real> b) {
        List<Real> data = new ArrayList<>();
        for (int i = 0; i < a.dimension(); i++) {
            data.add(a.get(i).add(b.get(i)));
        }
        return new DenseVector<>(data, Real.ZERO);
    }

    /**
     * Computes the dot product of two vectors.
     * 
     * @param a first vector
     * @param b second vector
     * @return the dot product
     */
    private Real dot(Vector<Real> a, Vector<Real> b) {
        Real sum = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            sum = sum.add(a.get(i).multiply(b.get(i)));
        }
        return sum;
    }
}

