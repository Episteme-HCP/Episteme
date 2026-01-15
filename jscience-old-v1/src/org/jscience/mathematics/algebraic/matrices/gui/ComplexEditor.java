// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package org.jscience.mathematics.algebraic.matrices.gui;

import org.jscience.mathematics.algebraic.numbers.Complex;

import java.beans.PropertyEditorSupport;


/**
 * Text based editor for Field.Complex.
 */
public class ComplexEditor extends PropertyEditorSupport {
    //final Complex value;
    /** DOCUMENT ME! */
    Complex value;

/**
     * Creates a new ComplexEditor object.
     */
    public ComplexEditor() {
        value = new Complex(0, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAsText() {
        return value.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public synchronized void setAsText(String s) {
        setValue(new Complex(s));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return new Complex(value.real(), value.imag());
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     */
    public void setValue(Object o) {
        Complex newVal = (Complex) o;

        if (!value.equals(newVal)) {
            value = new Complex(newVal.real(), newVal.imag());
            firePropertyChange();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getJavaInitializationString() {
        return "new org.jscience.mathematics.algebraic.numbers.Complex(" +
        value.real() + ", " + value.imag() + ")";
    }
}
