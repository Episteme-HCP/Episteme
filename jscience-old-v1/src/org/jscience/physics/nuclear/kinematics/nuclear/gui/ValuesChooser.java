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

package org.jscience.physics.nuclear.kinematics.nuclear.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * A special JComboBox for calling up several related dialogues.  If valid
 * entries are recieved in the dialogues, an array of double values specified
 * by the user is returned.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 * @version 1.1
 */
public class ValuesChooser extends JPanel implements ActionListener {
    /**
     * DOCUMENT ME!
     */
    private static final String RANGE = "Min Max Delta";

    /**
     * DOCUMENT ME!
     */
    private static final String MULT = "List of values";

    /**
     * DOCUMENT ME!
     */
    private static final String[] LIST = { MULT, RANGE };

    /**
     * DOCUMENT ME!
     */
    private JComboBox choice = new JComboBox(LIST);

    /**
     * DOCUMENT ME!
     */
    private String quantity;

    /**
     * DOCUMENT ME!
     */
    private String units;

    /**
     * DOCUMENT ME!
     */
    private JTextField entry;

    /**  */
    private double[] values = null;

    /**
     * DOCUMENT ME!
     */
    private double min;

    /**
     * DOCUMENT ME!
     */
    private double max;

    /**
     * DOCUMENT ME!
     */
    private double del;

    /**
     * DOCUMENT ME!
     */
    private double temp_min;

    /**
     * DOCUMENT ME!
     */
    private double temp_max;

    /**
     * DOCUMENT ME!
     */
    private double temp_del;

    /**
     * DOCUMENT ME!
     */
    private ValuesListener vl;

/**
     * Create a new values chooser.
     *
     * @param vl       listener for changed values
     * @param quantity name of category
     * @param units    units of category if appropriate, leave null if dimensionless
     * @param values   initial values to display
     */
    public ValuesChooser(ValuesListener vl, String quantity, String units,
        double[] values) {
        entry = new JTextField("" + values[0]);
        this.vl = vl;
        this.quantity = quantity;
        this.units = units;
        setValues(values);
        setLayout(new GridLayout(3, 1));

        if (units != null) {
            add(new Label(quantity + " [" + units + "]"));
        } else {
            add(new Label(quantity));
        }

        add(choice);
        entry.addActionListener(this);
        add(entry);
        setBorder(new EtchedBorder());
        choice.setToolTipText("Chooses method of entering values.");
    }

    /**
     * Called whenever enter is pressed in the text area. It builds the
     * array of values and calls the code which sends notification to the
     * <code>ValuesListener</code>.
     *
     * @param ae events produced by the text area
     */
    public void actionPerformed(ActionEvent ae) {
        String sval = entry.getText();
        String value = (String) choice.getSelectedItem();

        if (value == RANGE) {
            if (sval != null) {
                try {
                    StreamTokenizer st = new StreamTokenizer(new StringReader(
                                sval));
                    st.nextToken();
                    temp_min = st.nval;
                    st.nextToken();
                    temp_max = st.nval;
                    st.nextToken();
                    temp_del = st.nval;

                    int numVal = (int) Math.floor((temp_max - temp_min) / temp_del);
                    double[] temp = new double[numVal + 1];
                    double val = temp_min;
                    int counter = 0;

                    for (counter = 0; val <= temp_max;
                            val += temp_del, counter++) {
                        temp[counter] = val;
                    }

                    if (counter == (numVal + 1)) {
                        setValues(temp);
                    } else if (counter == numVal) {
                        double[] temp2 = new double[numVal];
                        System.arraycopy(temp, 0, temp2, 0, numVal);
                        setValues(temp2);
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }
            }
        } else if (value == MULT) {
            if (sval != null) {
                try {
                    Vector tempV = new Vector();
                    StreamTokenizer st = new StreamTokenizer(new StringReader(
                                sval));

                    while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        tempV.add(new Double(st.nval));
                    }

                    int numVal = tempV.size();
                    double[] temp = new double[numVal];

                    for (int counter = 0; counter < numVal; counter++) {
                        temp[counter] = ((Double) (tempV.get(counter))).doubleValue();
                    }

                    setValues(temp);
                } catch (IOException ioe) {
                    System.err.println(ioe);
                }
            }
        }
    }

    /*private void setValues(double d) {
        values = new double[1];
        values[0] = d;
        sendValues();
    }*/
    private void setValues(double[] da) {
        if (sendValues(da)) {
            values = da;

            if (choice.getSelectedItem().equals(RANGE)) {
                min = temp_min;
                max = temp_max;
                del = temp_del;
            }
        }

        setTextToValues();
    }

    /**
     * Returns the array of numbers represented by what the user has
     * entered.
     *
     * @return DOCUMENT ME!
     */
    public double[] getValues() {
        return values;
    }

    /**
     * Notify the <code>ValuesListener</code> of any changes.
     *
     * @see ValuesListener
     */
    public boolean sendValues(double[] sendvals) {
        return vl.receiveValues(this, sendvals);
    }

    /**
     * DOCUMENT ME!
     */
    private void setTextToValues() {
        String val = "";

        if (choice.getSelectedItem().equals(MULT)) {
            for (int i = 0; i < values.length; i++) {
                val += (values[i] + " ");
            }
        } else { //RANGE
            val = min + " " + max + " " + del;
        }

        entry.setText(val);
    }
}
