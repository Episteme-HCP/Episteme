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

import org.jscience.physics.nuclear.kinematics.Spanc;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.ReactionTable;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Dialog box for adding a reaction channel to those considered for the
 * calibration or output.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 */
public class AddReactionDialog extends JDialog implements ActionListener,
    ChangeListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Add Reaction";

    /**
     * DOCUMENT ME!
     */
    ReactionTable rtable;

    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

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
    public AddReactionDialog(ReactionTable rt, Spanc sp) {
        super();
        rtable = rt;
        spanc = sp;
        setTitle(TITLE);
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
        temp.add(new JLabel("Ebeam [MeV]"));
        temp.add(_ebeam);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("B-field [kG]"));
        temp.add(_bfield);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Target"));
        temp.add(_target);
        _target.addActionListener(this);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Layer"));
        temp.add(_layer);
        setupLayerSlider();
        _layer.addChangeListener(this);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Target Nuclide"));
        temp.add(_targetNuclide);
        temp.add(_targetUncertain);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Projectile"));
        temp.add(_projectile);
        temp.add(_projectileUncertain);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Residual Nucleus"));
        temp.add(_residualUncertain);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Q"));
        temp.add(_q);
        temp = new JPanel(new GridLayout(1, 0));
        center.add(temp);
        temp.add(new JLabel("Theta [degrees]"));
        temp.add(_theta);
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

            //_targetNuclide.setEnabled(true);
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
            if (source == b_apply) {
                SpancReaction sr = makeReaction();

                if (sr != null) {
                    rtable.addRow(sr);
                    spanc.setButtonStates();
                } else {
                    System.err.println(
                        "There was a problem creating the reaction.");
                }
            } else if (source == b_ok) {
                SpancReaction sr = makeReaction();

                if (sr != null) {
                    rtable.addRow(sr);
                    spanc.setButtonStates();
                    dispose();
                } else {
                    System.err.println(
                        "There was a problem creating the reaction.");
                }
            } else if (source == b_cancel) {
                dispose();
            } else if (source == _target) {
                Target target = Target.getTarget((String) _target.getSelectedItem());
                _layer.setMaximum(target.getNumberOfLayers() - 1);
                //_layer.setEnabled(true);
                _targetNuclide.setModel(target.getLayerNuclideComboModel(
                        _layer.getModel().getValue()));
            }
        } catch (NuclearException ne) {
            JOptionPane.showMessageDialog(this, ne.getMessage(),
                "Nuclear Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * returns null if unsuccessful
     *
     * @return DOCUMENT ME!
     */
    private SpancReaction makeReaction() {
        Nucleus beam;
        Nucleus projectile;

        try {
            beam = Nucleus.parseNucleus(_beam.getText().trim());
            projectile = Nucleus.parseNucleus(_projectile.getText().trim());
        } catch (NuclearException ne) {
            JOptionPane.showConfirmDialog(spanc, ne.getMessage(),
                "Error parsing nucleus", JOptionPane.ERROR_MESSAGE);

            return null;
        }

        double ebeam = Double.parseDouble(_ebeam.getText().trim());
        double bfield = Double.parseDouble(_bfield.getText().trim());
        Target target = Target.getTarget((String) _target.getSelectedItem());
        int layer = _layer.getModel().getValue();
        Nucleus targetN = (Nucleus) _targetNuclide.getSelectedItem();
        int Q = Integer.parseInt(_q.getText().trim());
        double theta = Double.parseDouble(_theta.getText().trim());
        SpancReaction rval = new SpancReaction(beam, targetN, projectile,
                ebeam, bfield, target, layer, Q, theta);

        if (rval != null) {
            rval.setBeamUncertain(_beamUncertain.isSelected());
            rval.setTargetUncertain(_targetUncertain.isSelected());
            rval.setProjectileUncertain(_projectileUncertain.isSelected());
            rval.setResidualUncertain(_residualUncertain.isSelected());
        }

        return rval;
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
