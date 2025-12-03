/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.analysis.fem;

import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Matrix;
import org.jscience.mathematics.vector.Vector;
import org.jscience.mathematics.sets.Reals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A 4-node tetrahedral element for 3D finite element analysis.
 * <p>
 * Shape functions:
 * N1 = 1 - x - y - z
 * N2 = x
 * N3 = y
 * N4 = z
 * (Defined on the reference element)
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @see <a href=
 *      "https://www.amazon.com/Finite-Element-Method-Linear-Dynamic/dp/0486411818">Hughes,
 *      T.J.R. (2000). The Finite Element Method: Linear Static and Dynamic
 *      Finite Element Analysis. Dover Publications.</a>
 * @since 1.0
 */
public class TetrahedralElement3D implements Element {

    private final List<Node> nodes;

    public TetrahedralElement3D(Node n1, Node n2, Node n3, Node n4) {
        this.nodes = Arrays.asList(n1, n2, n3, n4);
    }

    @Override
    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public List<ShapeFunction> getShapeFunctions() {
        List<ShapeFunction> shapes = new ArrayList<>();

        // N1 = 1 - x - y - z
        shapes.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> coords) {
                return Real.ONE.subtract(coords.get(0)).subtract(coords.get(1)).subtract(coords.get(2));
            }

            @Override
            public Vector<Real> gradient(Vector<Real> coords) {
                return new DenseVector<>(Arrays.asList(Real.ONE.negate(), Real.ONE.negate(), Real.ONE.negate()),
                        Real.ZERO);
            }
        });

        // N2 = x
        shapes.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> coords) {
                return coords.get(0);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> coords) {
                return new DenseVector<>(Arrays.asList(Real.ONE, Real.ZERO, Real.ZERO), Real.ZERO);
            }
        });

        // N3 = y
        shapes.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> coords) {
                return coords.get(1);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> coords) {
                return new DenseVector<>(Arrays.asList(Real.ZERO, Real.ONE, Real.ZERO), Real.ZERO);
            }
        });

        // N4 = z
        shapes.add(new ShapeFunction() {
            @Override
            public Real evaluate(Vector<Real> coords) {
                return coords.get(2);
            }

            @Override
            public Vector<Real> gradient(Vector<Real> coords) {
                return new DenseVector<>(Arrays.asList(Real.ZERO, Real.ZERO, Real.ONE), Real.ZERO);
            }
        });

        return shapes;
    }

    @Override
    public Matrix<Real> computeJacobian(Vector<Real> localCoords) {
        // For linear tetrahedron, Jacobian is constant
        // J = [ dx/dxi dx/deta dx/dzeta ]
        // [ dy/dxi dy/deta dy/dzeta ]
        // [ dz/dxi dz/deta dz/dzeta ]

        double x1 = nodes.get(0).getCoordinates().get(0).doubleValue();
        double y1 = nodes.get(0).getCoordinates().get(1).doubleValue();
        double z1 = nodes.get(0).getCoordinates().get(2).doubleValue();

        double x2 = nodes.get(1).getCoordinates().get(0).doubleValue();
        double y2 = nodes.get(1).getCoordinates().get(1).doubleValue();
        double z2 = nodes.get(1).getCoordinates().get(2).doubleValue();

        double x3 = nodes.get(2).getCoordinates().get(0).doubleValue();
        double y3 = nodes.get(2).getCoordinates().get(1).doubleValue();
        double z3 = nodes.get(2).getCoordinates().get(2).doubleValue();

        double x4 = nodes.get(3).getCoordinates().get(0).doubleValue();
        double y4 = nodes.get(3).getCoordinates().get(1).doubleValue();
        double z4 = nodes.get(3).getCoordinates().get(2).doubleValue();

        double j11 = x2 - x1; // dx/dxi
        double j12 = x3 - x1; // dx/deta
        double j13 = x4 - x1; // dx/dzeta

        double j21 = y2 - y1;
        double j22 = y3 - y1;
        double j23 = y4 - y1;

        double j31 = z2 - z1;
        double j32 = z3 - z1;
        double j33 = z4 - z1;

        List<List<Real>> rows = new ArrayList<>();
        rows.add(Arrays.asList(Real.of(j11), Real.of(j12), Real.of(j13)));
        rows.add(Arrays.asList(Real.of(j21), Real.of(j22), Real.of(j23)));
        rows.add(Arrays.asList(Real.of(j31), Real.of(j32), Real.of(j33)));

        return new DenseMatrix<>(rows, Reals.getInstance());
    }

    @Override
    public List<QuadraturePoint> getQuadraturePoints() {
        // 1-point Gauss quadrature for tetrahedron (exact for linear elements)
        // Point: (1/4, 1/4, 1/4)
        // Weight: 1/6 (volume of reference tetrahedron)
        List<QuadraturePoint> points = new ArrayList<>();
        Vector<Real> point = new DenseVector<>(Arrays.asList(Real.of(0.25), Real.of(0.25), Real.of(0.25)),
                Reals.getInstance());
        points.add(new QuadraturePoint(point, Real.of(1.0 / 6.0)));
        return points;
    }
}

