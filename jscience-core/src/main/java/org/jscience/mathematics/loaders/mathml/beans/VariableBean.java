package org.jscience.mathematics.loaders.mathml.beans;

import org.jscience.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.mathematics.numbers.real.Real;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * VariableBean for JScience 6.0.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.4
 */
public final class VariableBean implements java.io.Serializable {
    
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    private List<VariableListener> variableListeners = new ArrayList<>();

    private String variable = "";

    private Object value;

    public VariableBean() {
    }

    public synchronized void setVariable(String var) {
        String oldVar = variable;
        variable = var;
        changes.firePropertyChange("variable", oldVar, var);
    }

    public synchronized String getVariable() {
        return variable;
    }

    public synchronized void setValueAsNumber(double x) {
        value = Real.of(x);
        changes.firePropertyChange("valueAsNumber", null, x);
        fireVariableChanged(new VariableEvent(this, variable, value));
    }

    public synchronized double getValueAsNumber() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof Real) {
            return ((Real) value).doubleValue();
        } else {
            return Double.NaN;
        }
    }

    public synchronized void setValueAsVector(double[] v) {
        value = RealDoubleVector.of(v);
        changes.firePropertyChange("valueAsVector", null, v);
        fireVariableChanged(new VariableEvent(this, variable, value));
    }

    public synchronized void setValueAsMatrix(double[][] m) {
        value = RealDoubleMatrix.of(m);
        changes.firePropertyChange("valueAsMatrix", null, m);
        fireVariableChanged(new VariableEvent(this, variable, value));
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public synchronized void addVariableListener(VariableListener l) {
        variableListeners.add(l);
    }

    public synchronized void removeVariableListener(VariableListener l) {
        variableListeners.remove(l);
    }

    private void fireVariableChanged(VariableEvent evt) {
        for (VariableListener l : variableListeners) {
            l.variableChanged(evt);
        }
    }
}
