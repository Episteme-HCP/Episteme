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

import java.util.List;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;

/**
 * Interface for a finite element.
 * <p>
 * An element is defined by a set of nodes and shape functions.
 * It provides methods to compute the Jacobian and integrate over the element.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Element {

    /**
     * Returns the nodes associated with this element.
     * 
     * @return the list of nodes
     */
    List<Node> getNodes();

    /**
     * Returns the shape functions associated with this element.
     * 
     * @return the list of shape functions
     */
    List<ShapeFunction> getShapeFunctions();

    /**
     * Computes the Jacobian matrix at the given local coordinates.
     * <p>
     * J = d(x,y,z)/d(xi,eta,zeta)
     * </p>
     * 
     * @param localCoords the local coordinates
     * @return the Jacobian matrix
     */
    Matrix<Real> computeJacobian(Vector<Real> localCoords);

    /**
     * Returns the quadrature points and weights for numerical integration over this
     * element.
     * 
     * @return a list of quadrature points (local coords + weight)
     */
    List<QuadraturePoint> getQuadraturePoints();
}