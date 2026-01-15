package org.jscience.mathematics.algebraic.matrices.gui;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.mathematics.algebraic.numbers.Quaternion;

import java.awt.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;


/**
 * Graphic editor for Quaternion.
 */
public class QuaternionEditor extends PropertyEditorSupport
    implements PropertyChangeListener {
    /** DOCUMENT ME! */
    QuaternionEditorPanel editor;

    //final Quaternion value;
    /** DOCUMENT ME! */
    Quaternion value;

    /** DOCUMENT ME! */
    private Quaternion editorPanelValue = new Quaternion(0, 0, 0, 0);

/**
     * Creates a new QuaternionEditor object.
     */
    public QuaternionEditor() {
        value = new Quaternion(0, 0, 0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void setValue(Object obj) {
        Quaternion newValue = (Quaternion) obj;

        if (!newValue.equals(value)) {
            Double3Vector double3Vector = newValue.imag();
            value = new Quaternion(newValue.real(), double3Vector.x,
                    double3Vector.y, double3Vector.z);

            if (editor != null) {
                editor.setQuaternion(value); // implies sending an event
            } else {
                firePropertyChange();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        Double3Vector double3Vector = value.imag();

        return new Quaternion(value.real(), double3Vector.x, double3Vector.y,
            double3Vector.z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getCustomEditor() {
        if (editor != null) {
            return editor;
        }

        editor = new QuaternionEditorPanel();
        editor.setQuaternion(value);
        editor.addPropertyChangeListener(QuaternionEditorPanel.QUATERNION_PROPERTY,
            this);

        return editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean supportsCustomEditor() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getJavaInitializationString() {
        Double3Vector double3Vector = value.imag();

        return "new org.jscience.mathematics.algebraic.numbers.Quaternion(" +
        value.real() + ", " + double3Vector.x + ", " + double3Vector.y + ", " +
        double3Vector.z + ")";
    }

    /**
     * This QuaternionEditor listens to used QuaternionEditorPanel and
     * this method is called, if a the "quaternion" property of
     * QuaternionEditorPanel is changed.
     *
     * @param ev DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent ev) {
        editor.getQuaternion(editorPanelValue);

        Double3Vector double3Vector = editorPanelValue.imag();
        value = new Quaternion(editorPanelValue.real(), double3Vector.x,
                double3Vector.y, double3Vector.z);
        firePropertyChange();
    }
}
