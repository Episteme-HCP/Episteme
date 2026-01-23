package org.jscience.ml.mathml.beans;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;
import org.jscience.mathematics.algebraic.matrices.DoubleVector;
import org.jscience.mathematics.algebraic.numbers.Double;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class VariableBean extends Object implements java.io.Serializable {
    /** DOCUMENT ME! */
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    /** DOCUMENT ME! */
    private Vector variableListeners = new Vector();

    /** DOCUMENT ME! */
    private String variable = new String();

    /** DOCUMENT ME! */
    private Object value;

/**
     * Creates a new VariableBean object.
     */
    public VariableBean() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     */
    public synchronized void setVariable(String var) {
        String oldVar = variable;
        variable = var;
        changes.firePropertyChange("variable", oldVar, var);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized String getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public synchronized void setValueAsNumber(double x) {
        value = new Double(x);
        changes.firePropertyChange("valueAsNumber", null, new Double(x));
        fireVariableChanged(new VariableEvent(this, variable, value));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized double getValueAsNumber() {
        if (value instanceof Double) {
            return ((Double) value).value();
        } else {
            return Double.NaN;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public synchronized void setValueAsVector(double[] v) {
        value = new DoubleVector(v);
        changes.firePropertyChange("valueAsVector", null, v);
        fireVariableChanged(new VariableEvent(this, variable, value));
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public synchronized void setValueAsMatrix(double[][] m) {
        value = new DoubleMatrix(m);
        changes.firePropertyChange("valueAsMatrix", null, m);
        fireVariableChanged(new VariableEvent(this, variable, value));
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

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void addVariableListener(VariableListener l) {
        variableListeners.addElement(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public synchronized void removeVariableListener(VariableListener l) {
        variableListeners.removeElement(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void fireVariableChanged(VariableEvent evt) {
        for (int i = 0; i < variableListeners.size(); i++)
            ((VariableListener) variableListeners.elementAt(i)).variableChanged(evt);
    }
}
