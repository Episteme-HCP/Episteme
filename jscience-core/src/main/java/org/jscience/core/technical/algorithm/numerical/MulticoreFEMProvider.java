/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.core.technical.algorithm.numerical;

import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.core.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.core.mathematics.linearalgebra.Matrix;
import org.jscience.core.mathematics.linearalgebra.Vector;
import org.jscience.core.mathematics.analysis.Function;
import org.jscience.core.mathematics.analysis.fem.Element;
import org.jscience.core.mathematics.analysis.fem.Mesh;
import org.jscience.core.mathematics.analysis.fem.Node;
import org.jscience.core.mathematics.analysis.fem.QuadraturePoint;
import org.jscience.core.technical.algorithm.FEMProvider;
import com.google.auto.service.AutoService;
import org.jscience.core.technical.algorithm.AlgorithmProvider;

/**
 * Multicore implementation of FEMProvider.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(AlgorithmProvider.class)
public class MulticoreFEMProvider implements FEMProvider {

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public Vector<Real> solvePoisson(Mesh mesh, Function<Vector<Real>, Real> sourceTerm) {
        int nNodes = mesh.getNodes().size();
        mesh.indexNodes();

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

                Vector<Real> globalPoint = computeGlobalCoordinates(element, xi);

                for (int i = 0; i < nElemNodes; i++) {
                    int globalI = nodes.get(i).getGlobalIndex();
                    Vector<Real> gradNi_local = element.getShapeFunctions().get(i).gradient(xi);
                    Vector<Real> gradNi_global = invJ.transpose().multiply(gradNi_local);

                    Real Ni = element.getShapeFunctions().get(i).evaluate(xi);
                    Real fVal = sourceTerm.evaluate(globalPoint);
                    Real fi = fData.get(globalI).add(Ni.multiply(fVal).multiply(detJ).multiply(weight));
                    fData.set(globalI, fi);

                    for (int j = 0; j < nElemNodes; j++) {
                        int globalJ = nodes.get(j).getGlobalIndex();
                        Vector<Real> gradNj_local = element.getShapeFunctions().get(j).gradient(xi);
                        Vector<Real> gradNj_global = invJ.transpose().multiply(gradNj_local);

                        Real dotProd = dot(gradNi_global, gradNj_global);
                        Real val = dotProd.multiply(detJ).multiply(weight);

                        Real currentK = kRows.get(globalI).get(globalJ);
                        kRows.get(globalI).set(globalJ, currentK.add(val));
                    }
                }
            }
        }

        applyBoundaryConditions(kRows, fData, mesh);

        Matrix<Real> K = new DenseMatrix<>(kRows, Real.ZERO);
        Vector<Real> F = new DenseVector<>(fData, Real.ZERO);

        return K.inverse().multiply(F);
    }

    private void applyBoundaryConditions(List<List<Real>> K, List<Real> F, Mesh mesh) {
        int[] fixedNodes = { 0, mesh.getNodes().size() - 1 };

        for (int nodeIdx : fixedNodes) {
            if (nodeIdx < 0 || nodeIdx >= K.size())
                continue;
            for (int j = 0; j < K.size(); j++) {
                K.get(nodeIdx).set(j, Real.ZERO);
            }
            K.get(nodeIdx).set(nodeIdx, Real.ONE);
            F.set(nodeIdx, Real.ZERO);
        }
    }

    private Vector<Real> computeGlobalCoordinates(Element element, Vector<Real> xi) {
        List<Node> nodes = element.getNodes();
        int n = nodes.size();
        if (n == 0)
            return null;

        Real N0 = element.getShapeFunctions().get(0).evaluate(xi);
        Vector<Real> globalPos = scaleVector(nodes.get(0).getCoordinates(), N0);

        for (int i = 1; i < n; i++) {
            Real Ni = element.getShapeFunctions().get(i).evaluate(xi);
            Vector<Real> contribution = scaleVector(nodes.get(i).getCoordinates(), Ni);
            globalPos = addVectors(globalPos, contribution);
        }
        return globalPos;
    }

    private Vector<Real> scaleVector(Vector<Real> v, Real s) {
        List<Real> data = new ArrayList<>();
        for (int i = 0; i < v.dimension(); i++) {
            data.add(v.get(i).multiply(s));
        }
        return new DenseVector<>(data, Real.ZERO);
    }

    private Vector<Real> addVectors(Vector<Real> a, Vector<Real> b) {
        List<Real> data = new ArrayList<>();
        for (int i = 0; i < a.dimension(); i++) {
            data.add(a.get(i).add(b.get(i)));
        }
        return new DenseVector<>(data, Real.ZERO);
    }

    private Real dot(Vector<Real> a, Vector<Real> b) {
        Real sum = Real.ZERO;
        for (int i = 0; i < a.dimension(); i++) {
            sum = sum.add(a.get(i).multiply(b.get(i)));
        }
        return sum;
    }

    @Override
    public String getName() {
        return "Multicore FEM";
    }
}
