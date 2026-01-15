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

import org.jscience.physics.nuclear.kinematics.Spanc;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.TargetDefinitionTable;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * Dialog box for defining, layer by layer, the properties of the targets
 * used.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public class DefineTargetDialog extends JDialog implements ActionListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Define Target";

    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

    /**
     * DOCUMENT ME!
     */
    private JTextField _name = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JButton b_addLayer = new JButton("Add Layer");

    /**
     * DOCUMENT ME!
     */
    private JButton b_removeLayer = new JButton("Remove Layer");

    /**
     * DOCUMENT ME!
     */
    private JButton b_ok = new JButton("OK");

    /**
     * DOCUMENT ME!
     */
    private JButton b_apply = new JButton("Apply");

    /**
     * DOCUMENT ME!
     */
    private JButton b_cancel = new JButton("Cancel");

    /**
     * DOCUMENT ME!
     */
    private TargetDefinitionTable table = new TargetDefinitionTable();

/**
     * Creates new DefineTargetDialog
     */

    /*public DefineTargetDialog() {
        super();
        setTitle(TITLE);
        buildGUI();
    }*/
    public DefineTargetDialog(Dialog d, Spanc sp) {
        super(d, TITLE);
        spanc = sp;
        buildGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel north = new JPanel(new FlowLayout());
        north.add(new JLabel("Name"));
        north.add(_name);
        north.add(b_addLayer);
        b_addLayer.addActionListener(this);
        north.add(b_removeLayer);
        b_removeLayer.addActionListener(this);
        contents.add(north, BorderLayout.NORTH);

        JPanel center = new JPanel(new FlowLayout());
        center.add(new JScrollPane(table));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 3));
        south.add(b_ok);
        b_ok.addActionListener(this);
        south.add(b_apply);
        b_apply.addActionListener(this);
        south.add(b_cancel);
        b_cancel.addActionListener(this);
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
        Object source = actionEvent.getSource();

        try {
            if (source == b_addLayer) {
                table.addRow();
            } else if (source == b_removeLayer) {
                table.removeRow(table.getSelectedRow());
            } else if (source == b_apply) {
                table.makeTarget(_name.getText().trim());
                spanc.setButtonStates();
            } else if (source == b_ok) {
                table.makeTarget(_name.getText().trim());
                spanc.setButtonStates();
                hide();
            } else if (source == b_cancel) {
                hide();
            }
        } catch (NuclearException ne) {
            System.err.println(ne);
        }
    }
}
