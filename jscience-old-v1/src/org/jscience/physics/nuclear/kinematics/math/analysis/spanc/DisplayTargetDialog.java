/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/

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
