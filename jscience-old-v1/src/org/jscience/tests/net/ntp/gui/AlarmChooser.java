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

package org.jscience.tests.net.ntp.gui;

import java.awt.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AlarmChooser extends JPanel {
    /** DOCUMENT ME! */
    private JComboBox onOffComboBox;

    /** DOCUMENT ME! */
    private String[] onOff = { "  on  ", "  off  " };

    /** DOCUMENT ME! */
    private JTextField hourTextField = new JTextField(2);

    /** DOCUMENT ME! */
    private JTextField minTextField = new JTextField(2);

    /** DOCUMENT ME! */
    private JTextField secTextField = new JTextField(2);

    /** DOCUMENT ME! */
    private JTextField msgTextField = new JTextField();

    /** DOCUMENT ME! */
    private JLabel colon1 = new JLabel(" : ");

    /** DOCUMENT ME! */
    private JLabel colon2 = new JLabel(" : ");

    /** DOCUMENT ME! */
    private JLabel time = new JLabel("Time:  ");

    /** DOCUMENT ME! */
    private JLabel msg = new JLabel("Msg:  ");

    /** DOCUMENT ME! */
    private int originalHour;

    /** DOCUMENT ME! */
    private int originalMin;

    /** DOCUMENT ME! */
    private int originalSec;

    /** DOCUMENT ME! */
    private NumberFormat f = new DecimalFormat("00");

/**
     * Creates a new AlarmChooser object.
     */
    public AlarmChooser() {
        onOffComboBox = new JComboBox(onOff);
        hourTextField.setMaximumSize(hourTextField.getPreferredSize());
        minTextField.setMaximumSize(minTextField.getPreferredSize());
        secTextField.setMaximumSize(secTextField.getPreferredSize());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        add(top);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(bottom);
        top.add(onOffComboBox);
        top.add(Box.createGlue());
        top.add(time);
        top.add(hourTextField);
        top.add(colon1);
        top.add(minTextField);
        top.add(colon2);
        top.add(secTextField);
        bottom.add(msg);
        bottom.add(msgTextField);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Alarm"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAlarmTimeHour() {
        try {
            int temp = Integer.parseInt(hourTextField.getText());

            if (AlarmTime.isLegalHour(temp)) {
                originalHour = temp;
            }
        } catch (NumberFormatException e) {
        }

        hourTextField.setText(f.format(originalHour));

        return originalHour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAlarmTimeMin() {
        try {
            int temp = Integer.parseInt(minTextField.getText());

            if (AlarmTime.isLegalMin(temp)) {
                originalMin = temp;
            }
        } catch (NumberFormatException e) {
        }

        minTextField.setText(f.format(originalMin));

        return originalMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAlarmTimeSec() {
        try {
            int temp = Integer.parseInt(secTextField.getText());

            if (AlarmTime.isLegalSec(temp)) {
                originalSec = temp;
            }
        } catch (NumberFormatException e) {
        }

        secTextField.setText(f.format(originalSec));

        return originalSec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hour DOCUMENT ME!
     */
    public void setAlarmTimeHour(int hour) {
        originalHour = hour;
        hourTextField.setText(f.format(hour));
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     */
    public void setAlarmTimeMin(int min) {
        originalMin = min;
        minTextField.setText(f.format(min));
    }

    /**
     * DOCUMENT ME!
     *
     * @param sec DOCUMENT ME!
     */
    public void setAlarmTimeSec(int sec) {
        originalSec = sec;
        secTextField.setText(f.format(sec));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAlarmMessage() {
        return msgTextField.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmMessage DOCUMENT ME!
     */
    public void setAlarmMessage(String alarmMessage) {
        msgTextField.setText(alarmMessage);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAlarmOn() {
        if (onOffComboBox.getSelectedIndex() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param alarmOn DOCUMENT ME!
     */
    public void setAlarmOn(boolean alarmOn) {
        if (alarmOn) {
            onOffComboBox.setSelectedIndex(0);
        } else {
            onOffComboBox.setSelectedIndex(1);
        }
    }
}
