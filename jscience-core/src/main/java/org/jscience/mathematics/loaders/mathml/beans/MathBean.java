package org.jscience.mathematics.loaders.mathml.beans;

import org.jscience.mathematics.loaders.mathml.MathMLExpression;
import org.jscience.mathematics.loaders.mathml.MathMLParser;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.mathematics.linearalgebra.vectors.RealDoubleVector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * MathBean for JScience 6.0.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.4
 */
public final class MathBean implements java.io.Serializable, VariableListener, ActionListener {
    
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private MathMLExpression expr;

    private String mathml = "";

    private Hashtable<Object, Object> variables = new Hashtable<>();

    private Object result = Real.NaN;

    public MathBean() {
    }

    public synchronized void setMathML(String uri) {
        try {
            MathMLParser parser = new MathMLParser();
            parser.parse(uri);
            Object obj = parser.translateToJScienceObjects()[0];
            if (obj instanceof MathMLExpression) {
                expr = (MathMLExpression) obj;
            }
        } catch (Exception e) {
            // Log or handle exception
        }

        String oldUri = mathml;
        mathml = uri;
        changes.firePropertyChange("mathml", oldUri, uri);
    }

    public synchronized String getMathML() {
        return mathml;
    }

    public synchronized double getResultAs0DArray() {
        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        } else if (result instanceof Real) {
            return ((Real) result).doubleValue();
        } else if (result instanceof Integer) {
            return ((Integer) result).doubleValue();
        } else {
            return Double.NaN;
        }
    }

    public synchronized double[] getResultAs1DArray() {
        if (result instanceof Complex) {
            return new double[] {
                    ((Complex) result).real(), ((Complex) result).imaginary()
                };
        } else if (result instanceof RealDoubleVector) {
            return ((RealDoubleVector) result).toDoubleArray();
        } else if (result instanceof Vector) {
            Vector<?> v = (Vector<?>) result;
            double[] array = new double[v.dimension()];
            for (int i = 0; i < v.dimension(); i++) {
                Object element = v.get(i);
                if (element instanceof Number) array[i] = ((Number) element).doubleValue();
                else if (element instanceof Real) array[i] = ((Real) element).doubleValue();
                else if (element instanceof Integer) array[i] = ((Integer) element).doubleValue();
            }
            return array;
        } else {
            return null;
        }
    }

    public synchronized double[][] getResultAs2DArray() {
        if (result instanceof RealDoubleMatrix) {
            return ((RealDoubleMatrix) result).to2DDoubleArray();
        } else if (result instanceof Matrix) {
            Matrix<?> m = (Matrix<?>) result;
            double[][] array = new double[m.rows()][m.cols()];
            for (int i = 0; i < m.rows(); i++) {
                for (int j = 0; j < m.cols(); j++) {
                    Object element = m.get(i, j);
                    if (element instanceof Number) array[i][j] = ((Number) element).doubleValue();
                    else if (element instanceof Real) array[i][j] = ((Real) element).doubleValue();
                    else if (element instanceof Integer) array[i][j] = ((Integer) element).doubleValue();
                }
            }
            return array;
        } else {
            return null;
        }
    }

    public synchronized double[][][] getResultAs3DArray() {
        // Current implementation does not support 3D arrays directly from Matrix/Vector
        return null;
    }

    @Override
    public void variableChanged(VariableEvent evt) {
        variables.put(evt.getVariable(), evt.getValue());
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (expr == null) return;
        
        MathMLExpression evalExp = expr;
        Enumeration<Object> vars = variables.keys();

        while (vars.hasMoreElements()) {
            Object var = vars.nextElement();
            Object value = variables.get(var);
            evalExp = evalExp.substitute(var.toString(), value);
        }

        result = evalExp.evaluate();
        changes.firePropertyChange("resultAs0DArray", null, getResultAs0DArray());
        changes.firePropertyChange("resultAs1DArray", null, getResultAs1DArray());
        changes.firePropertyChange("resultAs2DArray", null, getResultAs2DArray());
        changes.firePropertyChange("resultAs3DArray", null, getResultAs3DArray());
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
}
