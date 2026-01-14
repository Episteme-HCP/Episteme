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
import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables.OutputPeakTable;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale
 */
public class ChangeOutputPeakDialog extends JDialog implements ActionListener,
    ChangeListener {
    /**
     * DOCUMENT ME!
     */
    static final String TITLE = "Change Output Peak";

    /**
     * DOCUMENT ME!
     */
    OutputPeakTable opTable;

    /**
     * DOCUMENT ME!
     */
    CalibrationFit calFit;

    /**
     * DOCUMENT ME!
     */
    Spanc spanc;

    /**
     * DOCUMENT ME!
     */
    OutputPeak op;

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
    private JTextField _channel = new JTextField(8);

    /**
     * DOCUMENT ME!
     */
    private JTextField _delCh = new JTextField(8);

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
    SpancReaction reaction;

/**
     * Creates new Change Output Peak Dialog
     */
    public ChangeOutputPeakDialog(OutputPeakTable opt, CalibrationFit cf,
        Spanc sp) {
        super();
        opTable = opt;
        calFit = cf;
        spanc = sp;
        setTitle(TITLE);
        op = OutputPeak.getPeak(opTable.getSelectedRow());
        buildGUI();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildGUI() {
        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(0, 2));
        center.add(new JLabel("Reaction"));
        center.add(_reaction);
        setupReactionSlider();
        _reaction.setValue(op.getReactionIndex());
        _reaction.addChangeListener(this);
        center.add(new JLabel("Ex Projectile [MeV]"));
        center.add(_exproj);
        _exproj.setText(Double.toString(op.getExProjectile()));
        center.add(new JLabel("Channel"));
        center.add(_channel);
        center.add(new JLabel("delCh"));
        center.add(_delCh);
        _channel.setText(Double.toString(op.getChannel().value));
        _delCh.setText(Double.toString(op.getChannel().error));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 3));
        south.add(b_ok);
        b_ok.setEnabled(false);
        b_ok.addActionListener(this);
        //south.add(b_apply);
        //b_apply.setEnabled(false);
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

            //b_apply.setEnabled(true);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionEvent DOCUMENT ME!
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == b_ok) {
            modifyPeak();
            opTable.refreshData();
            spanc.setButtonStates();
            dispose();
        } else if (source == b_cancel) {
            dispose();
        }
    }

    /**
     * Modifies peak
     */
    private void modifyPeak() {
        double exproj = Double.parseDouble(_exproj.getText().trim());
        double channel = Double.parseDouble(_channel.getText().trim());
        double delch = Double.parseDouble(_delCh.getText().trim());

        try {
            op.setValues(reaction, exproj, new UncertainNumber(channel, delch));
        } catch (KinematicsException ke) {
            System.out.println("Problem modifying output peak: " + ke);
        } catch (org.jscience.physics.nuclear.kinematics.math.statistics.StatisticsException se) {
            System.out.println("Problem modifying output peak: " + se);
        } catch (org.jscience.physics.nuclear.kinematics.math.MathException me) {
            System.out.println("Problem modifying output peak: " + me);
        } catch (NuclearException me) {
            System.out.println("Problem modifying output peak: " + me);
        }
    }
}
