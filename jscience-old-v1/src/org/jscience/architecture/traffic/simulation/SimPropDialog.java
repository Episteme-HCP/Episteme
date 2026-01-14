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

package org.jscience.architecture.traffic.simulation;

import org.jscience.architecture.traffic.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * The dialog used to set simulation properties.
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimPropDialog extends Dialog {
    /** DOCUMENT ME! */
    TextField simNameText;

    /** DOCUMENT ME! */
    TextField commentsText;

    /** DOCUMENT ME! */
    boolean ok;

/**
     * Creates an <code>EditPropDialog</code>.
     *
     * @param c        DOCUMENT ME!
     * @param simName  DOCUMENT ME!
     * @param comments DOCUMENT ME!
     */
    public SimPropDialog(Controller c, String simName, String comments) {
        super(c, "Properties...", true);
        this.setResizable(false);
        this.setSize(400, 150);
        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    hide();
                }
            });
        this.setLayout(new BorderLayout());

        ActionListener al = new SPActionListener();
        this.add(new PropPanel(simName, comments), BorderLayout.CENTER);
        this.add(new OkCancelPanel(al), BorderLayout.SOUTH);
    }

    /*============================================*/
    /* GET, SET, ok() and show()                  */
    /*============================================*/
    /**
     * Returns the simulation name the user entered.
     *
     * @return DOCUMENT ME!
     */
    public String getSimName() {
        return simNameText.getText().trim();
    }

    /**
     * Sets the simulation name as shown in the dialog.
     *
     * @param s DOCUMENT ME!
     */
    public void setSimName(String s) {
        simNameText.setText(s);
    }

    /**
     * Returns the comments the user entered.
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return commentsText.getText().trim();
    }

    /**
     * Sets the comments as shown in the dialog.
     *
     * @param s DOCUMENT ME!
     */
    public void setComments(String s) {
        commentsText.setText(s);
    }

    /**
     * Returns true if the user clicked 'Ok' to close this dialog.
     *
     * @return DOCUMENT ME!
     */
    public boolean ok() {
        return ok;
    }

    /**
     * Shows the dialog.
     */
    public void show() {
        ok = false;
        super.show();
    }

    /*============================================*/
    /* Listeners                                  */
    /*============================================*/
    /**
     * Listens to the buttons of the dialog.
     */
    public class SPActionListener implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = ((Button) e.getSource()).getLabel();

            if (sel.equals("Ok")) {
                ok = true;
            }

            hide();
        }
    }

    /*============================================*/
    /* Panels                                     */
    /*============================================*/
    /**
     * Panel containing the necessary components to set the
     * infrastructure properties.
     */
    public class PropPanel extends Panel {
/**
         * Creates a new PropPanel object.
         *
         * @param simName  DOCUMENT ME!
         * @param comments DOCUMENT ME!
         */
        public PropPanel(String simName, String comments) {
            GridBagLayout gridbag = new GridBagLayout();
            this.setLayout(gridbag);

            simNameText = makeRow(gridbag, "Simulation title", simNameText,
                    simName);
            commentsText = makeRow(gridbag, "Comments", commentsText, comments);
        }

        /**
         * DOCUMENT ME!
         *
         * @param gridbag DOCUMENT ME!
         * @param label DOCUMENT ME!
         * @param textField DOCUMENT ME!
         * @param text DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private TextField makeRow(GridBagLayout gridbag, String label,
            TextField textField, String text) {
            GridBagConstraints c = new GridBagConstraints();
            Label lbl;

            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            lbl = new Label(label);
            gridbag.setConstraints(lbl, c);
            this.add(lbl);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 2.0;
            textField = new TextField(text, 40);
            gridbag.setConstraints(textField, c);
            this.add(textField);

            return textField;
        }
    }

    /**
     * Panel containing buttons "Ok" and "Cancel".
     */
    public class OkCancelPanel extends Panel {
/**
         * Creates a new OkCancelPanel object.
         *
         * @param action DOCUMENT ME!
         */
        public OkCancelPanel(ActionListener action) {
            this.setLayout(new FlowLayout(FlowLayout.CENTER));

            String[] labels = { "Ok", "Cancel" };
            Button b;

            for (int i = 0; i < labels.length; i++) {
                b = new Button(labels[i]);
                b.addActionListener(action);
                this.add(b);
            }
        }
    }
}
