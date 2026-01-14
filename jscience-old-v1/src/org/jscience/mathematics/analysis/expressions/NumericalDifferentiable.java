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

package org.jscience.mathematics.analysis.expressions;

import java.io.Serializable;


/**
 * The interface specifies the methods that must be supplied for
 * a class to be able to perform automatic differentiation in
 * the reverse mode.
 * <BR><B>Example of use:</B><BR>
 * The example uses the classes from the symbolic computation
 * package {@link org.jscience.mathematics.analysis.expressions.symbolic} that supports automatic differentiation.
 * Note that since all classes do not implement the NumericalDifferentiable
 * interface some class castings are necessary.
 * In the example x, y, and z are independent variables/parameters, and
 * e and f are dependent variables.
 * All other expressions are auxiliaries.
 * <PRE>
 * Variable x = new Variable( "x" );
 * Variable y = new Variable( "y" );
 * Parameter z = new Parameter( "z" );
 * Expression a = new Sin( x );
 * Expression b = new Multiplication( new Constant( 1.0 ), x );
 * Expression c = new Division( y, a );
 * Expression d = new Multiplication( a, z );
 * Expression e = new Subtraction( b, c );
 * Expression f = new Addition( c, d );
 * for(int k = 0; k < 10; k++) f = new Sin( f );
 * x.setIndex( 1 );
 * y.setIndex( 2 );
 * z.setIndex( 3 );
 * ((NumericalDifferentiable)e).setNumberOfIndependents( 3 );
 * ((NumericalDifferentiable)f).setNumberOfIndependents( 3 );
 * x.setValue( 0.13 );
 * y.setValue( 0.46 );
 * z.setValue( 0.73 );
 * ((NumericalDifferentiable)e).resetNumEval();
 * ((NumericalDifferentiable)f).resetNumEval();
 * ((NumericalDifferentiable)e).resetNumDiff( 1 );
 * ((NumericalDifferentiable)f).resetNumDiff( 1 );
 * double dedx = ((NumericalDifferentiable)e).numDiff( 1 );
 * double dfdx = ((NumericalDifferentiable)f).numDiff( 1 );
 * ((NumericalDifferentiable)e).resetNumDiff( 2 );
 * ((NumericalDifferentiable)f).resetNumDiff( 2 );
 * double dedy = ((NumericalDifferentiable)e).numDiff( 2 );
 * double dfdy = ((NumericalDifferentiable)f).numDiff( 2 );
 * ((NumericalDifferentiable)e).resetNumDiff( 3 );
 * ((NumericalDifferentiable)f).resetNumDiff( 3 );
 * double dedz = ((NumericalDifferentiable)e).numDiff( 3 );
 * double dfdz = ((NumericalDifferentiable)f).numDiff( 3 );
 * <PRE>
 *
 * @author Carsten Knudsen
 * @author Martin Egholm Nielsen
 * @version 1.0
 */
public interface NumericalDifferentiable extends Serializable {
    /**
     * Returns the derivative of an Expression with respect to a
     * NamedDataExpression whose index is the parameter.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double numDiff(int i);

    /**
     * Reset the computational tree for derivative calculation for the
     * indexed independent variable.
     *
     * @param i DOCUMENT ME!
     */
    public void resetNumDiff(int i);

    /**
     * Sets the number of independent variables in the computational
     * tree.
     *
     * @param n DOCUMENT ME!
     */
    public void setNumberOfIndependents(int n);

    /**
     * Sets the index of an independent variable. The index should be
     * used to identify an independent variable
     *
     * @param i DOCUMENT ME!
     */
    public void setIndex(int i);

    /**
     * Compute the numerical value of the function expression.
     *
     * @return DOCUMENT ME!
     */
    public double numEval();

    /**
     * Reset the function evaluation such that the next invocation of
     * {@link #numEval} carries out the full computation.
     */
    public void resetNumEval();
} // NumericalDifferentiable
