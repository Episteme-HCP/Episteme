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
