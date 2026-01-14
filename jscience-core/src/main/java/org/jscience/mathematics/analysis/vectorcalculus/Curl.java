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

package org.jscience.mathematics.analysis.vectorcalculus;

import org.jscience.mathematics.analysis.VectorField;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.geometry.PointND;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Computes the curl of a 3D vector field.
 * <p>
 * The curl measures the rotation or circulation of a vector field.
 * </p>
 * <p>
 * Definition (3D only): Ã¢Ë†â€¡Ãƒâ€”F = (Ã¢Ë†â€šFÃ¢â€šÆ’/Ã¢Ë†â€šy - Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šz, Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šz - Ã¢Ë†â€šFÃ¢â€šÆ’/Ã¢Ë†â€šx, Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šx -
 * Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šy)
 * </p>
 * <p>
 * Physical interpretation:
 * - Measures local rotation/vorticity
 * - Fluid dynamics: vorticity Ãâ€° = Ã¢Ë†â€¡Ãƒâ€”v
 * - Electromagnetism: Faraday's law Ã¢Ë†â€¡Ãƒâ€”E = -Ã¢Ë†â€šB/Ã¢Ë†â€št
 * - AmpÃƒÂ¨re's law: Ã¢Ë†â€¡Ãƒâ€”B = ÃŽÂ¼Ã¢â€šâ‚¬J + ÃŽÂ¼Ã¢â€šâ‚¬ÃŽÂµÃ¢â€šâ‚¬Ã¢Ë†â€šE/Ã¢Ë†â€št
 * - Conservative fields have zero curl: Ã¢Ë†â€¡Ãƒâ€”(Ã¢Ë†â€¡Ãâ€ ) = 0
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Curl {

    /**
     * Computes the curl of a 3D vector field at a point.
     * <p>
     * The curl is only defined for 3D vector fields.
     * </p>
     * 
     * @param field the vector field (must be 3D)
     * @param point the point at which to compute curl
     * @param h     the step size for numerical differentiation
     * @return the curl vector
     * @throws IllegalArgumentException if field is not 3D
     */
    public static Vector<Real> compute(VectorField<PointND> field, PointND point, Real h) {
        if (point.ambientDimension() != 3) {
            throw new IllegalArgumentException("Curl is only defined for 3D vector fields");
        }

        // curl = (Ã¢Ë†â€šFÃ¢â€šÆ’/Ã¢Ë†â€šy - Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šz, Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šz - Ã¢Ë†â€šFÃ¢â€šÆ’/Ã¢Ë†â€šx, Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šx - Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šy)
        // = (Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šxÃ¢â€šÂ - Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šxÃ¢â€šâ€š, Ã¢Ë†â€šFÃ¢â€šâ‚¬/Ã¢Ë†â€šxÃ¢â€šâ€š - Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šxÃ¢â€šâ‚¬, Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šxÃ¢â€šâ‚¬ - Ã¢Ë†â€šFÃ¢â€šâ‚¬/Ã¢Ë†â€šxÃ¢â€šÂ)
        // where xÃ¢â€šâ‚¬=x, xÃ¢â€šÂ=y, xÃ¢â€šâ€š=z and FÃ¢â€šâ‚¬=Fx, FÃ¢â€šÂ=Fy, FÃ¢â€šâ€š=Fz

        // curlX = Ã¢Ë†â€šFz/Ã¢Ë†â€šy - Ã¢Ë†â€šFy/Ã¢Ë†â€šz = Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šxÃ¢â€šÂ - Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šxÃ¢â€šâ€š
        Real curlX = partialDerivative(field, point, 2, 1, h)
                .subtract(partialDerivative(field, point, 1, 2, h));

        // curlY = Ã¢Ë†â€šFx/Ã¢Ë†â€šz - Ã¢Ë†â€šFz/Ã¢Ë†â€šx = Ã¢Ë†â€šFÃ¢â€šâ‚¬/Ã¢Ë†â€šxÃ¢â€šâ€š - Ã¢Ë†â€šFÃ¢â€šâ€š/Ã¢Ë†â€šxÃ¢â€šâ‚¬
        Real curlY = partialDerivative(field, point, 0, 2, h)
                .subtract(partialDerivative(field, point, 2, 0, h));

        // curlZ = Ã¢Ë†â€šFy/Ã¢Ë†â€šx - Ã¢Ë†â€šFx/Ã¢Ë†â€šy = Ã¢Ë†â€šFÃ¢â€šÂ/Ã¢Ë†â€šxÃ¢â€šâ‚¬ - Ã¢Ë†â€šFÃ¢â€šâ‚¬/Ã¢Ë†â€šxÃ¢â€šÂ
        Real curlZ = partialDerivative(field, point, 1, 0, h)
                .subtract(partialDerivative(field, point, 0, 1, h));

        return new DenseVector<>(
                Arrays.asList(curlX, curlY, curlZ),
                org.jscience.mathematics.sets.Reals.getInstance());
    }

    /**
     * Returns a vector field representing the curl of the input field.
     * 
     * @param field the vector field (must be 3D)
     * @param h     the step size for numerical differentiation
     * @return the curl as a vector field
     */
    public static VectorField<PointND> asField(VectorField<PointND> field, Real h) {
        return VectorField.of(
                point -> compute(field, point, h),
                3);
    }

    /**
     * Computes the partial derivative Ã¢Ë†â€šFÃ¡ÂµÂ¢/Ã¢Ë†â€šxÃ¢Â±Â¼ at a point.
     * 
     * @param field          the vector field
     * @param point          the point
     * @param componentIndex the component index i (which component of F)
     * @param variableIndex  the variable index j (which coordinate)
     * @param h              the step size
     * @return the partial derivative
     */
    private static Real partialDerivative(
            VectorField<PointND> field,
            PointND point,
            int componentIndex,
            int variableIndex,
            Real h) {

        PointND pointPlus = shiftPoint(point, variableIndex, h);
        PointND pointMinus = shiftPoint(point, variableIndex, h.negate());

        Real fPlus = field.evaluate(pointPlus).get(componentIndex);
        Real fMinus = field.evaluate(pointMinus).get(componentIndex);

        return fPlus.subtract(fMinus).divide(h.multiply(Real.of(2)));
    }

    /**
     * Shifts a point by a given amount in a specific coordinate direction.
     */
    private static PointND shiftPoint(PointND point, int coordinateIndex, Real delta) {
        int n = point.ambientDimension();
        List<Real> newCoords = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            if (i == coordinateIndex) {
                newCoords.add(point.get(i).add(delta));
            } else {
                newCoords.add(point.get(i));
            }
        }

        return new PointND(newCoords);
    }
}


