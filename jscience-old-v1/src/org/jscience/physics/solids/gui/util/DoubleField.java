/*
 * DoubleField.java
 *
 * Created on December 16, 2005, 11:36 PM
 *
    Copyright (C) 2005 Brandon Wegge and Herb Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 *
 *  This is a utility widget to obtain user input doubles.
 */
package org.jscience.physics.solids.gui.util;

import java.awt.*;

import javax.swing.*;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class DoubleField extends JPanel {
    /**
     * DOCUMENT ME!
     */
    private NamedDouble namedDouble;

    /**
     * DOCUMENT ME!
     */
    private JTextField textField = new JTextField();

/**
     * Creates a new instance of DoubleField
     */
    public DoubleField(NamedDouble nd) {
        namedDouble = nd;
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        setLayout(new GridLayout(1, 2, 5, 5));
        add(new JLabel(namedDouble.name));
        add(textField);
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setValue(double val) {
        textField.setText(String.valueOf(val));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NumberFormatException DOCUMENT ME!
     */
    public double getValue() throws NumberFormatException {
        String val = textField.getText();
        double ret = Double.valueOf(val).doubleValue();

        return ret;
    }
}
