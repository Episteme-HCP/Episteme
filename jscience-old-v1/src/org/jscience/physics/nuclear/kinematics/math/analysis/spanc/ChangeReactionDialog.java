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
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.CalibrationPeakTable;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.ReactionTable;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Dialog box for changing the particulars of a reaction channel.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public class ChangeReactionDialog extends JDialog implements ActionListener,
    ChangeListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Change Reaction";

    /**
     * DOCUMENT ME!
     */
    ReactionTable rtable;

    /**
     * DOCUMENT ME!
     */
    CalibrationPeakTable cptable;

    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

    //int row;
    /**
     * DOCUMENT ME!
     */
    SpancReaction reaction;

    /**
     * DOCUMENT ME!
     */
    private JTextField _beam = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _ebeam = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _bfield = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JComboBox _target = new JComboBox(Target.getComboModel());

    /**
     * DOCUMENT ME!
     */
    private JSlider _layer = new JSlider(0,
            (Target.getTarget((String) (_target.getSelectedItem()))).getNumberOfLayers() -
            1, JSlider.HORIZONTAL);

    /**
     * DOCUMENT ME!
     */
    private JComboBox _targetNuclide = new JComboBox((Target.getTarget(
                (String) (_target.getSelectedItem()))).getLayerNuclideComboModel(
                _layer.getModel().getValue()));

    /**
     * DOCUMENT ME!
     */
    private JTextField _projectile = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _q = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _theta = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JButton b_ok = new JButton("OK");

    //private JButton b_apply = new JButton("Apply");
    /**
     * DOCUMENT ME!
     */
    private JButton b_cancel = new JButton("Cancel");

    /**
     * DOCUMENT ME!
     */
    JCheckBox _beamUncertain = new JCheckBox("Mass uncertain?");

    /**
     * DOCUMENT ME!
     */
    JCheckBox _targetUncertain = new JCheckBox("Mass uncertain?");

    /**
     * DOCUMENT ME!
     */
    JCheckBox _projectileUncertain = new JCheckBox("Mass uncertain?");

    /**
     * DOCUMENT ME!
     */
    JCheckBox _residualUncertain = new JCheckBox("Mass uncertain?");

/**
     * Creates new DefineTargetDialog
     */
    public ChangeReactionDialog(ReactionTable rt, CalibrationPeakTable cpt,
        Spanc sp) {
        super();
        rtable = rt;
        cptable = cpt;
        spanc = sp;
        setTitle(TITLE);

        int row = rt.getSelectedRow();
        reaction = SpancReaction.getReaction(row);
        buildGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(0, 1));
        JPanel temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Beam"));
        temp.add(_beam);
        temp.add(_beamUncertain);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        _beam.setText(reaction.getBeam().toString());
        _beamUncertain.setSelected(reaction.getBeamUncertain());
        temp.add(new JLabel("Ebeam [MeV]"));
        temp.add(_ebeam);
        _ebeam.setText(Double.toString(reaction.getEbeam()));
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("B-field [kG]"));
        temp.add(_bfield);
        _bfield.setText(Double.toString(reaction.getBfield()));
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Target"));
        temp.add(_target);
        _target.setSelectedItem(reaction.getTarget());
        _target.addActionListener(this);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Layer"));
        temp.add(_layer);
        setupLayerSlider();
        _layer.addChangeListener(this);
        _layer.setValue(reaction.getInteractionLayer());
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Target Nuclide"));
        temp.add(_targetNuclide);
        temp.add(_targetUncertain);
        _targetNuclide.setSelectedItem(reaction.getTargetNuclide());
        _targetUncertain.setSelected(reaction.getTargetUncertain());
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Projectile"));
        temp.add(_projectile);
        temp.add(_projectileUncertain);
        _projectile.setText(reaction.getProjectile().toString());
        _projectileUncertain.setSelected(reaction.getProjectileUncertain());
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Residual Nucleus"));
        temp.add(_residualUncertain);
        _residualUncertain.setSelected(reaction.getResidualUncertain());
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Q"));
        temp.add(_q);
        _q.setText(Integer.toString(reaction.getQ()));
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Theta [degrees]"));
        temp.add(_theta);
        _theta.setText(Double.toString(reaction.getTheta()));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 3));
        south.add(b_ok);
        b_ok.addActionListener(this);
        //south.add(b_apply);
        //b_apply.addActionListener(this);
        south.add(b_cancel);
        b_cancel.addActionListener(this);
        contents.add(south, BorderLayout.SOUTH);
        pack();
        show();
    }

    /**
     * DOCUMENT ME!
     */
    private void setupLayerSlider() {
        _layer.setMinorTickSpacing(1);
        _layer.setMajorTickSpacing(1);
        _layer.setPaintTicks(true);
        _layer.setPaintLabels(true);
        _layer.setSnapToTicks(true);
        _layer.addChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param change DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent change) {
        Object source = change.getSource();

        if (source == _layer) {
            Target target = Target.getTarget((String) _target.getSelectedItem());
            _targetNuclide.setModel(target.getLayerNuclideComboModel(
                    _layer.getModel().getValue()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionEvent DOCUMENT ME!
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        try {
            if (source == b_ok) {
                modifyReaction();
                rtable.refreshData();
                cptable.refreshData();
                spanc.calculateFit();
                dispose();
            } else if (source == b_cancel) {
                dispose();
            } else if (source == _target) {
                Target target = Target.getTarget((String) _target.getSelectedItem());
                _layer.setMaximum(target.getNumberOfLayers() - 1);
                _targetNuclide.setModel(target.getLayerNuclideComboModel(
                        _layer.getModel().getValue()));
            }
        } catch (NuclearException ne) {
            JOptionPane.showConfirmDialog(this, ne.getMessage(),
                "Nucleus naming error", JOptionPane.OK_OPTION);
        }
    }

    /**
     * Uses states in dialog to modify the reaction, and update the
     * table.
     *
     * @throws NuclearException DOCUMENT ME!
     */
    private void modifyReaction() throws NuclearException {
        Nucleus beam = Nucleus.parseNucleus(_beam.getText().trim());
        double ebeam = Double.parseDouble(_ebeam.getText().trim());
        double bfield = Double.parseDouble(_bfield.getText().trim());
        Target target = Target.getTarget((String) _target.getSelectedItem());
        int layer = _layer.getModel().getValue();
        Nucleus targetN = (Nucleus) _targetNuclide.getSelectedItem();
        Nucleus projectile = Nucleus.parseNucleus(_projectile.getText().trim());
        int Q = Integer.parseInt(_q.getText().trim());
        double theta = Double.parseDouble(_theta.getText().trim());
        reaction.setValues(beam, targetN, projectile, ebeam, bfield, target,
            layer, Q, theta);
        reaction.setBeamUncertain(_beamUncertain.isSelected());
        reaction.setTargetUncertain(_targetUncertain.isSelected());
        reaction.setProjectileUncertain(_projectileUncertain.isSelected());
        reaction.setResidualUncertain(_residualUncertain.isSelected());
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static public void main(String[] args) {
        //new Nucleus();
        //new DefineTargetDialog();
    }
}
