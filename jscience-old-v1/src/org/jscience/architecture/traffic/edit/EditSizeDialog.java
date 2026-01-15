/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.edit;

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.ErrorDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * The dialog used to change the size of the infrastructure
 *
 * @author Group GUI
 * @version 1.0
 */
public class EditSizeDialog extends Dialog implements ActionListener,
    WindowListener {
    /** DOCUMENT ME! */
    SizePanel sizePanel;

    /** DOCUMENT ME! */
    int width;

    /** DOCUMENT ME! */
    int height;

    /** DOCUMENT ME! */
    boolean ok;

/**
     * Creates an <code>EditPropDialog</code>.
     *
     * @param c      DOCUMENT ME!
     * @param width  DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public EditSizeDialog(Controller c, int width, int height) {
        super(c, "Change size", true);
        this.setResizable(false);
        this.setSize(400, 200);
        this.addWindowListener(this);
        this.setLayout(new BorderLayout());

        sizePanel = new SizePanel(this, width, height);
        this.add(sizePanel, BorderLayout.CENTER);
        this.add(new OkCancelPanel(this), BorderLayout.SOUTH);
    }

    /*============================================*/
    /* GET, SET, ok() and show()                  */
    /*============================================*/

    // I & S erachter anders clash met Component.getWidth() en zo
    /**
     * Returns the width entered in the textfield
     *
     * @return DOCUMENT ME!
     */
    public int getWidthI() {
        return width;
    }

    /**
     * Sets the text of the width textfield
     *
     * @param w DOCUMENT ME!
     */
    public void setWidthI(int w) {
        width = w;
        sizePanel.setWidthS(w);
    }

    /**
     * Returns the height entered in the textfield
     *
     * @return DOCUMENT ME!
     */
    public int getHeightI() {
        return height;
    }

    /**
     * Sets the text of the height textfield
     *
     * @param h DOCUMENT ME!
     */
    public void setHeightI(int h) {
        height = h;
        sizePanel.setHeightS(h);
    }

    /**
     * Returns true if the user clicked 'Ok' to close this dialog
     *
     * @return DOCUMENT ME!
     */
    public boolean ok() {
        return ok;
    }

    /**
     * Shows the dialog
     */
    public void show() {
        ok = false;
        super.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Ok") ||
                e.getSource() instanceof TextField) {
            try {
                width = Integer.parseInt(sizePanel.getWidthS());
                height = Integer.parseInt(sizePanel.getHeightS());
            } catch (NumberFormatException exp) {
                new ErrorDialog(this, "You must enter an integer");
                ok = false;

                return;
            }

            ok = true;
        } else {
            ok = false;
        }

        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosing(WindowEvent e) {
        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowActivated(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowClosed(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowIconified(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void windowOpened(WindowEvent e) {
    }

    /*============================================*/
    /* Panels                                     */
    /*============================================*/
    /**
     * Panel containing the necessary components to set the
     * infrastructure properties.
     */
    private static class SizePanel extends Panel {
        /** DOCUMENT ME! */
        TextField tfWidth;

        /** DOCUMENT ME! */
        TextField tfHeight;

        /** DOCUMENT ME! */
        EditSizeDialog esd;

/**
         * Creates a new SizePanel object.
         *
         * @param al     DOCUMENT ME!
         * @param width  DOCUMENT ME!
         * @param height DOCUMENT ME!
         */
        public SizePanel(ActionListener al, int width, int height) {
            this.esd = esd;

            GridBagLayout gridbag = new GridBagLayout();
            this.setLayout(gridbag);

            tfWidth = makeRow(gridbag, "Width", "" + width);
            tfHeight = makeRow(gridbag, "Height", "" + height);

            tfWidth.addActionListener(al);
            tfHeight.addActionListener(al);
        }

        /**
         * DOCUMENT ME!
         *
         * @param gridbag DOCUMENT ME!
         * @param label DOCUMENT ME!
         * @param text DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private TextField makeRow(GridBagLayout gridbag, String label,
            String text) {
            GridBagConstraints c = new GridBagConstraints();
            Label lbl;
            TextField tf;

            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            lbl = new Label(label);
            gridbag.setConstraints(lbl, c);
            this.add(lbl);
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.weightx = 2.0;
            tf = new TextField(text, 40);
            gridbag.setConstraints(tf, c);
            this.add(tf);

            return tf;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getWidthS() {
            return tfWidth.getText().trim();
        }

        /**
         * DOCUMENT ME!
         *
         * @param w DOCUMENT ME!
         */
        public void setWidthS(int w) {
            tfWidth.setText("" + w);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getHeightS() {
            return tfHeight.getText().trim();
        }

        /**
         * DOCUMENT ME!
         *
         * @param h DOCUMENT ME!
         */
        public void setHeightS(int h) {
            tfHeight.setText("" + h);
        }
    }

    /**
     * Panel containing buttons "Ok" and "Cancel".
     */
    public class OkCancelPanel extends Panel {
/**
         * Creates a new OkCancelPanel object.
         *
         * @param al DOCUMENT ME!
         */
        public OkCancelPanel(ActionListener al) {
            this.setLayout(new FlowLayout(FlowLayout.CENTER));

            String[] labels = { "Ok", "Cancel" };
            Button b;

            for (int i = 0; i < labels.length; i++) {
                b = new Button(labels[i]);
                b.addActionListener(al);
                this.add(b);
            }
        }
    }
}
