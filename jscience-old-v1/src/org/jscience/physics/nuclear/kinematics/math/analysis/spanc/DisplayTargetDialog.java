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

/*
 * DefineTargetDialog.java
 *
 * Created on December 17, 2001, 2:48 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.TargetDefinitionTable;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * Dialog box which displays the properties of a given pre-defined target.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public class DisplayTargetDialog extends JDialog implements ActionListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Display Target";

    /**
     * DOCUMENT ME!
     */
    Target target;

    /**
     * DOCUMENT ME!
     */
    private TargetDefinitionTable table;

    /**
     * DOCUMENT ME!
     */
    private JTextField _name = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JButton b_ok = new JButton("OK");

    /**
     * Creates a new DisplayTargetDialog object.
     *
     * @param d DOCUMENT ME!
     * @param t DOCUMENT ME!
     */
    public DisplayTargetDialog(Dialog d, Target t) {
        super(d, TITLE);
        target = t;
        table = new TargetDefinitionTable(t);
        buildGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel north = new JPanel(new FlowLayout());
        _name.setText(target.getName());
        north.add(_name);
        contents.add(north, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout());
        center.add(new JScrollPane(table));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 3));
        south.add(b_ok);
        b_ok.addActionListener(this);
        contents.add(south, BorderLayout.SOUTH);
        pack();
        show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionEvent DOCUMENT ME!
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        if (actionEvent.getSource() == b_ok) {
            dispose();
        }
    }
}
