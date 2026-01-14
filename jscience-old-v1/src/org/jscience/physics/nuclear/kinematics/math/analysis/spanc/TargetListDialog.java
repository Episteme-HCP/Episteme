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
 * TargetListDialog.java
 *
 * Created on December 17, 2001, 5:18 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.Spanc;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale
 */
public class TargetListDialog extends JDialog implements ActionListener {
    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

    /**
     * DOCUMENT ME!
     */
    JList _list = new JList(Target.getTargetList());

    /**
     * DOCUMENT ME!
     */
    JButton b_add = new JButton("Add");

    /**
     * DOCUMENT ME!
     */
    JButton b_remove = new JButton("Remove");

    /**
     * DOCUMENT ME!
     */
    JButton b_display = new JButton("Display");

    /**
     * DOCUMENT ME!
     */
    private JButton b_ok = new JButton("OK");

/**
     * Creates new TargetListDialog
     */
    public TargetListDialog(Spanc f) {
        super(f, "Target List");
        spanc = f;
        setupGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void setupGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel west = new JPanel(new GridLayout(3, 1));
        west.add(b_add);
        b_add.addActionListener(this);
        west.add(b_remove);
        b_remove.addActionListener(this);
        west.add(b_display);
        b_display.addActionListener(this);
        contents.add(west, BorderLayout.WEST);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(b_ok);
        b_ok.addActionListener(this);
        contents.add(south, BorderLayout.SOUTH);

        JPanel center = new JPanel(new FlowLayout());
        center.add(new JScrollPane(_list));
        contents.add(center, BorderLayout.CENTER);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionEvent DOCUMENT ME!
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == b_add) {
            new DefineTargetDialog(this, spanc);
        } else if (source == b_remove) {
            Object[] values = _list.getSelectedValues();

            for (int i = 0; i < values.length; i++) {
                Target.removeTarget(Target.getTarget((String) values[i]));
            }

            spanc.setButtonStates();
        } else if (source == b_display) {
            Target selected = (Target) Target.getTarget((String) _list.getSelectedValue());

            if (selected != null) {
                new DisplayTargetDialog(this, selected);
            } else {
                System.err.println("No target selected.");
            }
        } else if (source == b_ok) {
            hide();
        }
    }
}
