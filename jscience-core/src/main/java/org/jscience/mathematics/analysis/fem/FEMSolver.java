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

package org.jscience.mathematics.analysis.fem;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.analysis.Function;
import org.jscience.technical.backend.algorithms.FEMProvider;
import org.jscience.technical.backend.algorithms.MulticoreFEMProvider;

/**
 * A simple Finite Element Method solver.
 * <p>
 * Currently supports solving the Poisson equation: -div(grad u) = f.
 * Delegates to {@link FEMProvider}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Press, W. H., Teukolsky, S. A., Vetterling, W. T., & Flannery, B. P. (2007). <i>Numerical Recipes: The Art of Scientific Computing</i> (3rd ed.). Cambridge University Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FEMSolver {

    private FEMProvider provider;

    public FEMSolver() {
        this.provider = new MulticoreFEMProvider();
    }

    public void setProvider(FEMProvider provider) {
        this.provider = provider;
    }

    /**
     * Solves the Poisson equation on the given mesh.
     * 
     * @param mesh       the finite element mesh
     * @param sourceTerm the source term function f(x)
     * @return the solution vector u at the nodes
     */
    public Vector<Real> solvePoisson(Mesh mesh, Function<Vector<Real>, Real> sourceTerm) {
        return provider.solvePoisson(mesh, sourceTerm);
    }
}
