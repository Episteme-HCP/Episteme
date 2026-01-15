/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
 */
package org.jscience.astronomy.solarsystem.artificialsatellites.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
final class PropagationPane extends JPanel {
    /**
     * DOCUMENT ME!
     */
    private final static String[] PROPAGATORS = new String[] {
            "SGP", "SGP4", "SGP8", "SDP4", "SDP8"
        };

    /**
     * DOCUMENT ME!
     */
    private final JComboBox propagatorField = new JComboBox(PROPAGATORS);

    /**
     * DOCUMENT ME!
     */
    private final JTextField startTimeField = new JTextField(10);

    /**
     * DOCUMENT ME!
     */
    private final JTextField endTimeField = new JTextField(10);

    /**
     * DOCUMENT ME!
     */
    private final JTextField timeStepField = new JTextField(10);

    /**
     * Creates a new PropagationPane object.
     */
    PropagationPane() {
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    "Propagation"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        String propagatorFieldString = "Propagator";
        String startTimeFieldString = "Start Time (SSE)";
        String endTimeFieldString = "End Time (SSE)";
        String timeStepFieldString = "Time Step (sec)";

        propagatorField.setSelectedIndex(1);
        add(propagatorField);
        propagatorField.setActionCommand(propagatorFieldString);
        startTimeField.setText("0.0");
        startTimeField.setActionCommand(startTimeFieldString);
        endTimeField.setText("1440.0");
        endTimeField.setActionCommand(endTimeFieldString);
        timeStepField.setText("360.0");
        timeStepField.setActionCommand(timeStepFieldString);

        JLabel propagatorFieldLabel = new JLabel(propagatorFieldString + ": ");
        propagatorFieldLabel.setLabelFor(propagatorField);

        JLabel startTimeFieldLabel = new JLabel(startTimeFieldString + ": ");
        startTimeFieldLabel.setLabelFor(startTimeField);

        JLabel endTimeFieldLabel = new JLabel(endTimeFieldString + ": ");
        endTimeFieldLabel.setLabelFor(endTimeField);

        JLabel timeStepFieldLabel = new JLabel(timeStepFieldString + ": ");
        timeStepFieldLabel.setLabelFor(timeStepField);

        JLabel[] labels = {
                propagatorFieldLabel, startTimeFieldLabel, endTimeFieldLabel,
                timeStepFieldLabel
            };
        JComponent[] textFields = {
                propagatorField, startTimeField, endTimeField, timeStepField
            };
        addLabelTextRows(labels, textFields, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param labels DOCUMENT ME!
     * @param textFields DOCUMENT ME!
     * @param container DOCUMENT ME!
     */
    private void addLabelTextRows(JLabel[] labels, JComponent[] textFields,
        Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;

        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE; //reset to default
            c.weightx = 0.0; //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER; //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPropagatorName() {
        return (String) propagatorField.getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getStartTime() {
        return Double.parseDouble(startTimeField.getText());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEndTime() {
        return Double.parseDouble(endTimeField.getText());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTimeStep() {
        return Double.parseDouble(timeStepField.getText());
    }
}
