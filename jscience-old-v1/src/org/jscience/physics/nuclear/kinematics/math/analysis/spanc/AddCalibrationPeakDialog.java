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
import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.CalibrationPeakTable;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Dialog box for adding a spectrum peak that will be an input to the focal
 * plane calibration.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public class AddCalibrationPeakDialog extends JDialog implements ActionListener,
    ChangeListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Add Calibration Peak";

    /**
     * DOCUMENT ME!
     */
    CalibrationPeakTable cpTable;

    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

    /**
     * DOCUMENT ME!
     */
    private JSlider _reaction = new JSlider(0,
            SpancReaction.getAllReactions().length - 1, JSlider.HORIZONTAL);

    /**
     * DOCUMENT ME!
     */
    private JTextField _exproj = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _exres = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _delExres = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _channel = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _delCh = new JTextField(8);

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
    SpancReaction reaction;

/**
     * Creates new DefineTargetDialog
     */
    public AddCalibrationPeakDialog(CalibrationPeakTable cpt, Spanc sp) {
        super();
        this.spanc = sp;
        cpTable = cpt;
        setTitle(TITLE);
        buildGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(9, 2));
        center.add(new JLabel("Reaction"));
        center.add(_reaction);
        setupReactionSlider();
        _reaction.addChangeListener(this);
        center.add(new JLabel("Ex Projectile [MeV]"));
        center.add(_exproj);
        _exproj.setText("0");
        center.add(new JLabel("Ex Residual [MeV]"));
        center.add(_exres);
        center.add(new JLabel("Ex Residual Unc. [keV]"));
        center.add(_delExres);
        center.add(new JLabel("Channel"));
        center.add(_channel);
        center.add(new JLabel("delCh"));
        center.add(_delCh);
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 3));
        south.add(b_ok);
        b_ok.setEnabled(false);
        b_ok.addActionListener(this);
        south.add(b_apply);
        b_apply.setEnabled(false);
        b_apply.addActionListener(this);
        south.add(b_cancel);
        b_cancel.addActionListener(this);
        contents.add(south, BorderLayout.SOUTH);
        pack();
        show();
    }

    /**
     * DOCUMENT ME!
     */
    private void setupReactionSlider() {
        _reaction.setMinorTickSpacing(1);
        _reaction.setMajorTickSpacing(1);
        _reaction.setPaintTicks(true);
        _reaction.setPaintLabels(true);
        _reaction.setSnapToTicks(true);
        _reaction.addChangeListener(this);
        _reaction.setValue(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param change DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent change) {
        Object source = change.getSource();

        if (source == _reaction) {
            reaction = (SpancReaction) SpancReaction.getReaction(_reaction.getModel()
                                                                          .getValue());
            b_ok.setEnabled(true);
            b_apply.setEnabled(true);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionEvent DOCUMENT ME!
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == b_apply) {
            CalibrationPeak cp = makePeak();

            if (cp != null) {
                cpTable.addRow(cp);
                spanc.setButtonStates();
                spanc.calculateFit();
            } else {
                System.err.println("There was a problem creating the peak.");
            }
        } else if (source == b_ok) {
            CalibrationPeak cp = makePeak();

            if (cp != null) {
                cpTable.addRow(cp);
                spanc.setButtonStates();
                spanc.calculateFit();
                dispose();
            } else {
                System.err.println("There was a problem creating the peak.");
            }
        } else if (source == b_cancel) {
            dispose();
        }
    }

    /**
     * returns false if unsuccessful
     *
     * @return DOCUMENT ME!
     */
    private CalibrationPeak makePeak() {
        double exproj = Double.parseDouble(_exproj.getText().trim());
        double exres = Double.parseDouble(_exres.getText().trim());
        double delExRes = Double.parseDouble(_delExres.getText().trim()) * 0.001;
        double channel = Double.parseDouble(_channel.getText().trim());
        double delch = Double.parseDouble(_delCh.getText().trim());

        return new CalibrationPeak(reaction, exproj,
            new UncertainNumber(exres, delExRes),
            new UncertainNumber(channel, delch));
    }
}
