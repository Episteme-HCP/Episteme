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

package org.jscience.util.mapper;

/**
 * This interface is used to map objects to and from simple arrays.
 * <p/>
 * <p> Lots of mathematical algorithms are generic ones which can
 * process the data from domain objects despite they ignore what
 * this data represent. As an example, the same algorithm can
 * integrate either the orbit evolution of a spacecraft under a
 * specified force model or the electrical characteristics of a
 * circuit after a switch is opened.  </p>
 * <p/>
 * <p> The approach of the Mantissa library is to define an interface
 * for each such algorithm to represent the type of problem they can
 * handle ({@link
 * org.jscience.mathematics.analysis.ode.FirstOrderDifferentialEquations
 * FirstOrderDifferentialEquations} for an ODE integrators, {@link
 * org.jscience.mathematics.analysis.estimation.EstimationProblem
 * EstimationProblem} for least squares estimators, ...). Furthermore,
 * the state data that is handled by these algorithms is often a
 * mixture of data coming from several domain objects (the orbit,
 * plus the aerodynamical coefficients of the spacecraft, plus the
 * characteristics of the thrusters, plus ...). Therefore, the user
 * needs to gather and dispatch data between different objects
 * representing different levels of abstraction.  </p>
 * <p/>
 * <p> This interface is designed to copy data back and forth between
 * existing objects during the iterative processing of these
 * algorithms and avoid the cost of recreating the objects.  </p>
 * <p/>
 * <p> The nominal way to use this interface is to have the domain
 * objects implement it (either directly or using inheritance to add
 * this feature to already existing objects) and to create one class
 * that implements the problem interface (for example {@link
 * org.jscience.mathematics.analysis.ode.FirstOrderDifferentialEquations}) and
 * uses the {@link ArrayMapper} class to dispatch the data to and from
 * the domain objects.</p>
 *
 * @author L. Maisonobe
 * @version $Id: ArraySliceMappable.java,v 1.1 2006-09-07 22:04:17 virtualcall Exp $
 * @see ArrayMapper
 */
public interface ArraySliceMappable {
    /**
     * Get the dimension of the object.
     *
     * @return dimension of the object
     */
    public int getStateDimension();

    /**
     * Reinitialize internal state from the specified array slice data.
     *
     * @param start start index in the array
     * @param array array holding the data to extract
     */
    public void mapStateFromArray(int start, double[] array);

    /**
     * Store internal state data into the specified array slice.
     *
     * @param start start index in the array
     * @param array array where data should be stored
     */
    public void mapStateToArray(int start, double[] array);
}
