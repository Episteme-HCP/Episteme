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

package org.jscience.core.mathematics.loaders.mathml.beans;

import org.jscience.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.jscience.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import org.jscience.core.mathematics.numbers.real.Real;

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

