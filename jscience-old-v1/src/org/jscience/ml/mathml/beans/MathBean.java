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

package org.jscience.ml.mathml.beans;

import org.jscience.io.mathml.MathMLExpression;
import org.jscience.io.mathml.MathMLParser;

import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;
import org.jscience.mathematics.algebraic.numbers.Integer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class MathBean extends Object implements java.io.Serializable,
    VariableListener, ActionListener {
    /** DOCUMENT ME! */
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    /** DOCUMENT ME! */
    private MathMLExpression expr;

    /** DOCUMENT ME! */
    private String mathml = "";

    /** DOCUMENT ME! */
    private Hashtable variables = new Hashtable();

    /** DOCUMENT ME! */
    private Object result = new Double(Double.NaN);

/**
     * Creates a new MathBean object.
     */
    public MathBean() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param uri DOCUMENT ME!
     */
    public synchronized void setMathML(String uri) {
        try {
            MathMLParser parser = new MathMLParser();
            parser.parse(uri);
            expr = (MathMLExpression) (parser.translateToJScienceObjects()[0]);
        } catch (Exception e) {
        }

        String oldUri = mathml;
        mathml = uri;
        changes.firePropertyChange("mathml", oldUri, uri);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized String getMathML() {
        return mathml;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized double getResultAs0DArray() {
        if (result instanceof Double) {
            return ((Double) result).value();
        } else if (result instanceof Integer) {
            return ((Integer) result).value();
        } else {
            return Double.NaN;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized double[] getResultAs1DArray() {
        if (result instanceof Complex) {
            double[] array = {
                    ((Complex) result).real(), ((Complex) result).imag()
                };

            return array;
        } else if (result instanceof AbstractDoubleVector) {
            return getDoubleArray((Double[]) ((AbstractDoubleVector) result).toArray());
        } else if (result instanceof AbstractIntegerVector) {
            return getDoubleArray((Integer[]) ((AbstractIntegerVector) result).toArray());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized double[][] getResultAs2DArray() {
        if (result instanceof AbstractComplexVector) {
            double[][] array = new double[2][];
            array[0] = getDoubleArray((Double[]) ((AbstractComplexVector) result).real()
                                                  .toArray());
            array[1] = getDoubleArray((Double[]) ((AbstractComplexVector) result).imag()
                                                  .toArray());

            return array;
        } else if (result instanceof DoubleMatrix) {
            return getDoubleDoubleArray((Double[][]) ((DoubleMatrix) result).toArray());
        } else if (result instanceof IntegerMatrix) {
            return getDoubleDoubleArray((Integer[][]) ((IntegerMatrix) result).toArray());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized double[][][] getResultAs3DArray() {
        if (result instanceof ComplexMatrix) {
            double[][][] array = new double[2][][];
            array[0] = getDoubleDoubleArray((Double[][]) ((ComplexMatrix) result).real()
                                                          .toArray());
            array[1] = getDoubleDoubleArray((Double[][]) ((ComplexMatrix) result).imag()
                                                          .toArray());

            return array;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubles DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[] getDoubleArray(Double[] doubles) {
        double[] result;
        result = new double[doubles.length];

        for (int i = 0; i < doubles.length; i++) {
            result[i] = doubles[i].doubleValue();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubles DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[] getDoubleArray(Integer[] doubles) {
        double[] result;
        result = new double[doubles.length];

        for (int i = 0; i < doubles.length; i++) {
            result[i] = doubles[i].doubleValue();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubles DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[][] getDoubleDoubleArray(Double[][] doubles) {
        double[][] result;
        result = new double[doubles.length][doubles[0].length];

        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; i < doubles[0].length; j++) {
                result[i][j] = doubles[i][j].doubleValue();
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param doubles DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[][] getDoubleDoubleArray(Integer[][] doubles) {
        double[][] result;
        result = new double[doubles.length][doubles[0].length];

        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; i < doubles[0].length; j++) {
                result[i][j] = doubles[i][j].doubleValue();
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void variableChanged(VariableEvent evt) {
        variables.put(evt.getVariable(), evt.getValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent evt) {
        MathMLExpression evalExp = expr;
        Enumeration vars = variables.keys();

        while (vars.hasMoreElements()) {
            Object var = vars.nextElement();
            evalExp = evalExp.substitute(var.toString(), variables.get(var));
        }

        result = evalExp.evaluate();
        changes.firePropertyChange("resultAs0DArray", null,
            new Double(getResultAs0DArray()));
        changes.firePropertyChange("resultAs1DArray", null, getResultAs1DArray());
        changes.firePropertyChange("resultAs2DArray", null, getResultAs2DArray());
        changes.firePropertyChange("resultAs3DArray", null, getResultAs3DArray());
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void removePropertyChangeListener(
        PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
}
