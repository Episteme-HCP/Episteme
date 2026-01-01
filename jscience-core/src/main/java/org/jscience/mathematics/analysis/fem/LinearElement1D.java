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
 * A 1D linear finite element with 2 nodes.
 * <p>
 * Local coordinate xi ranges from -1 to 1.
 * Shape functions:
 * N1 = 0.5 * (1 - xi)
 * N2 = 0.5 * (1 + xi)
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class LinearElement1D implements Element {

    private final List<Node> nodes;
    private final List<ShapeFunction> shapeFunctions;
    private final List<QuadraturePoint> quadraturePoints;

    public LinearElement1D(Node n1, Node n2) {
        this.nodes = new ArrayList<>();
        this.nodes.add(n1);
        this.nodes.add(n2);

        this.shapeFunctions = new ArrayList<>();
        this.shapeFunctions.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> localCoords) {
                Real xi = localCoords.get(0);
                return Real.ONE.subtract(xi).divide(Real.of(2.0));
            }

            @Override
            public Vector<Real> gradient(Vector<Real> localCoords) {
                List<Real> grad = new ArrayList<>();
                grad.add(Real.of(-0.5));
                return new DenseVector<>(grad, Reals.getInstance());
            }
        });

        this.shapeFunctions.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> localCoords) {
                Real xi = localCoords.get(0);
                return Real.ONE.add(xi).divide(Real.of(2.0));
            }

            @Override
            public Vector<Real> gradient(Vector<Real> localCoords) {
                List<Real> grad = new ArrayList<>();
                grad.add(Real.of(0.5));
                return new DenseVector<>(grad, Reals.getInstance());
            }
        });

        // Gauss-Legendre quadrature (2 points for exact integration of polynomials up
        // to degree 3)
        this.quadraturePoints = new ArrayList<>();
        double sqrt3inv = 1.0 / Math.sqrt(3.0);

        List<Real> p1Coords = new ArrayList<>();
        p1Coords.add(Real.of(-sqrt3inv));
        this.quadraturePoints.add(new QuadraturePoint(new DenseVector<>(p1Coords, Reals.getInstance()), Real.ONE));

        List<Real> p2Coords = new ArrayList<>();
        p2Coords.add(Real.of(sqrt3inv));
        this.quadraturePoints.add(new QuadraturePoint(new DenseVector<>(p2Coords, Reals.getInstance()), Real.ONE));
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
        // J = dx/dxi = sum(dN_i/dxi * x_i)
        Real dxdxi = Real.ZERO;

        for (int i = 0; i < nodes.size(); i++) {
            Real dNidxi = shapeFunctions.get(i).gradient(localCoords).get(0);
            Real xi = nodes.get(i).getCoordinates().get(0);
            dxdxi = dxdxi.add(dNidxi.multiply(xi));
        }

        List<List<Real>> rows = new ArrayList<>();
        List<Real> row = new ArrayList<>();
        row.add(dxdxi);
        rows.add(row);

        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    @Override
    public List<QuadraturePoint> getQuadraturePoints() {
        return quadraturePoints;
    }
}

