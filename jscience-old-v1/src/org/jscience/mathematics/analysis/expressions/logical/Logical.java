package org.jscience.mathematics.analysis.expressions.logical;

import org.jscience.mathematics.analysis.expressions.Expression;

import java.io.Serializable;


/**
 * Interface for creating symbolic logical expressions,
 * which may be evaluated.
 * <BR><B>Example of use:</B><BR>
 * <PRE>
 * Logical x = new LogicalConstant( true );
 * Logical y = new LogicalVariable( "y", true );
 * Logical z = new And( x, y );
 * boolean value = z.truthValue();
 *
 * @author Carsten Knudsen
 * @version 1.0
 * @see org.jscience.mathematics.analysis.expressions.logical.LogicalConstant
 * @see org.jscience.mathematics.analysis.expressions.logical.LogicalVariable
 * @see org.jscience.mathematics.analysis.expressions.logical.LogicalParameter
 * @see org.jscience.mathematics.analysis.expressions.logical.LogicalUnary
 * @see org.jscience.mathematics.analysis.expressions.logical.LogicalBinary
 */
public interface Logical extends Serializable {
    /**
     * Returns the truth value of the Logical object.
     *
     * @return DOCUMENT ME!
     */
    public boolean truthValue();

    /**
     * Returns a new Logical object that represents the optimized
     * logical expression.
     *
     * @return DOCUMENT ME!
     */
    public Logical optimize();

    /**
     * Replace all occurences of the Expression a in the current
     * expression with the Expression b.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Logical replace(Expression a, Expression b);

    /**
     * Returns true if the Logical contains the Expression e, else
     * returns false;
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Expression e);

    /**
     * Return a String representation of the Logical expression.
     *
     * @return DOCUMENT ME!
     */
    public String toString();

    /**
     * Returns String object representing Logical in Java source code.
     *
     * @return String object representing Logical in Java source code.
     */
    public String toJava();

    /**
     * Returns String object representing Logical in XML code.
     *
     * @return String object representing Logical in XML code.
     */
    public String toXML();
} // Logical
