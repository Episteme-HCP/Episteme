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
import org.jscience.mathematics.sets.Reals;

/**
 * A 2D linear triangular finite element with 3 nodes.
 * <p>
 * Local coordinates (xi, eta) range from 0 to 1, with xi + eta <= 1.
 * Shape functions:
 * N1 = 1 - xi - eta
 * N2 = xi
 * N3 = eta
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TriangularElement2D implements Element {

    private final List<Node> nodes;
    private final List<ShapeFunction> shapeFunctions;
    private final List<QuadraturePoint> quadraturePoints;

    public TriangularElement2D(Node n1, Node n2, Node n3) {
        this.nodes = new ArrayList<>();
        this.nodes.add(n1);
        this.nodes.add(n2);
        this.nodes.add(n3);

        this.shapeFunctions = new ArrayList<>();

        // N1 = 1 - xi - eta
        this.shapeFunctions.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> localCoords) {
                Real xi = localCoords.get(0);
                Real eta = localCoords.get(1);
                return Real.ONE.subtract(xi).subtract(eta);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> localCoords) {
                List<Real> grad = new ArrayList<>();
                grad.add(Real.of(-1.0));
                grad.add(Real.of(-1.0));
                return new DenseVector<>(grad, Reals.getInstance());
            }
        });

        // N2 = xi
        this.shapeFunctions.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> localCoords) {
                return localCoords.get(0);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> localCoords) {
                List<Real> grad = new ArrayList<>();
                grad.add(Real.ONE);
                grad.add(Real.ZERO);
                return new DenseVector<>(grad, Reals.getInstance());
            }
        });

        // N3 = eta
        this.shapeFunctions.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> localCoords) {
                return localCoords.get(1);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> localCoords) {
                List<Real> grad = new ArrayList<>();
                grad.add(Real.ZERO);
                grad.add(Real.ONE);
                return new DenseVector<>(grad, Reals.getInstance());
            }
        });

        // 1-point quadrature (centroid) - exact for linear functions
        this.quadraturePoints = new ArrayList<>();
        List<Real> p1Coords = new ArrayList<>();
        p1Coords.add(Real.of(1.0 / 3.0));
        p1Coords.add(Real.of(1.0 / 3.0));

        // Weight is 0.5 (area of reference triangle)
        this.quadraturePoints.add(new QuadraturePoint(new DenseVector<>(p1Coords, Reals.getInstance()), Real.of(0.5)));
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public List<ShapeFunction> getShapeFunctions() {
        return shapeFunctions;
    }

    @Override
    public Matrix<Real> computeJacobian(Vector<Real> localCoords) {
        // J = [ dx/dxi dy/dxi ]
        // [ dx/deta dy/deta]

        Real dxdxi = Real.ZERO;
        Real dydxi = Real.ZERO;
        Real dxdeta = Real.ZERO;
        Real dydeta = Real.ZERO;

        for (int i = 0; i < nodes.size(); i++) {
            Vector<Real> gradN = shapeFunctions.get(i).gradient(localCoords);
            Real dNidxi = gradN.get(0);
            Real dNideta = gradN.get(1);

            Real xi = nodes.get(i).getCoordinates().get(0);
            Real yi = nodes.get(i).getCoordinates().get(1);

            dxdxi = dxdxi.add(dNidxi.multiply(xi));
            dydxi = dydxi.add(dNidxi.multiply(yi));
            dxdeta = dxdeta.add(dNideta.multiply(xi));
            dydeta = dydeta.add(dNideta.multiply(yi));
        }

        List<List<Real>> rows = new ArrayList<>();
        List<Real> row1 = new ArrayList<>();
        row1.add(dxdxi);
        row1.add(dydxi);
        rows.add(row1);

        List<Real> row2 = new ArrayList<>();
        row2.add(dxdeta);
        row2.add(dydeta);
        rows.add(row2);

        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    @Override
    public List<QuadraturePoint> getQuadraturePoints() {
        return quadraturePoints;
    }
}