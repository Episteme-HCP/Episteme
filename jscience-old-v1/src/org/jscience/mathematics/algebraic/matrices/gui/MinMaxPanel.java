package org.jscience.mathematics.algebraic.matrices.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * An instance of this class is a JPanel containing one JSlider and 3
 * JTextFields representing the Sliders minimum, maximum and current value.
 *
 * @author marcel
 */
public class MinMaxPanel extends JPanel implements ActionListener,
    ChangeListener {
    /** DOCUMENT ME! */
    private JSlider scrol;

    /** DOCUMENT ME! */
    private DoubleBoundedRangeModel minMaxModel;

    /** DOCUMENT ME! */
    private JTextField minTF;

    /** DOCUMENT ME! */
    private JTextField maxTF;

    /** DOCUMENT ME! */
    private JTextField valTF;

    /** DOCUMENT ME! */
    private double target;

/**
     * Creates a MinMaxPanel instance with 1 JSlider, 3 JTextFields and 1
     * JLabel with the specified name. The JLabel only represents what kind of
     * value is showing by the JSlider (for a better handling with 2 or more
     * MinMaxPanels, e.g.: mfc.field.QuaternionEditor).
     *
     * @param name DOCUMENT ME!
     */
    public MinMaxPanel(String name) {
        super(new GridBagLayout());

        GridBagConstraints lab = new GridBagConstraints();
        GridBagConstraints gbc = new GridBagConstraints();
        lab.insets.right = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1f;
        add(scrol = new JSlider(minMaxModel = new DoubleBoundedRangeModel()),
            gbc);
        minMaxModel.addChangeListener(this);
        gbc.insets.top = 0;
        lab.gridwidth = 1;
        add(new JLabel("min"), lab);
        gbc.gridwidth = 2;
        gbc.weightx = 0.f;
        add(minTF = new JTextField(), gbc);
        minTF.addActionListener(this);
        minTF.setColumns(4);
        minTF.setHorizontalAlignment(JTextField.LEFT);
        lab.gridwidth = 3;
        lab.insets.left = 20;
        add(new JLabel(name, JLabel.RIGHT), lab);
        gbc.gridwidth = 4;
        gbc.weightx = 1f;
        add(valTF = new JTextField(), gbc);
        valTF.addActionListener(this);
        valTF.setColumns(12);
        valTF.setHorizontalAlignment(JTextField.LEFT);
        lab.gridwidth = 5;
        add(new JLabel("max"), lab);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.f;
        add(maxTF = new JTextField(), gbc);
        maxTF.addActionListener(this);
        maxTF.setColumns(4);
        maxTF.setHorizontalAlignment(JTextField.LEFT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleBoundedRangeModel getModel() {
        return minMaxModel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     */
    public void setMinimum(double min) {
        minMaxModel.setDoubleMinimum(min);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMinimum() {
        return minMaxModel.getDoubleMinimum();
    }

    /**
     * DOCUMENT ME!
     *
     * @param max DOCUMENT ME!
     */
    public void setMaximum(double max) {
        minMaxModel.setDoubleMaximum(max);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMaximum() {
        return minMaxModel.getDoubleMaximum();
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setValue(double val) {
        minMaxModel.setDoubleValue(val);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return minMaxModel.getDoubleValue();
    }

    //-------------------------event-handling------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ev) {
        final JTextField src = (JTextField) ev.getSource();
        final double v = Double.parseDouble(ev.getActionCommand());

        if (src == minTF) {
            setMinimum(v);
        } else if (src == maxTF) {
            setMaximum(v);
        } else if (src == valTF) {
            setValue(v);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent ev) {
        double doubleVal = minMaxModel.getDoubleValue();
        double doubleMin = minMaxModel.getDoubleMinimum();
        double doubleMax = minMaxModel.getDoubleMaximum();
        minTF.setText(String.valueOf(doubleMin));
        maxTF.setText(String.valueOf(doubleMax));
        valTF.setText(String.valueOf(doubleVal));
    }
}
