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
package org.jscience.architecture.traffic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * The dialog used to show errors.
 *
 * @author Group GUI
 * @version 1.0
 */
public class ErrorDialog extends Dialog implements ActionListener {
/**
     * Creates an <code>ErrorDialog</code>.
     *
     * @param f   DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public ErrorDialog(Frame f, String msg) {
        super(f, "Error", true);
        setup(msg);
    }

/**
     * Creates a new ErrorDialog object.
     *
     * @param d   DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public ErrorDialog(Dialog d, String msg) {
        super(d, "Error", true);
        setup(msg);
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     */
    public void setup(String msg) {
        this.setResizable(false);
        this.setSize(300, 200);
        this.addWindowListener(new ErrorWindowListener());
        this.setLayout(new BorderLayout());

        this.add(new OkPanel(this), BorderLayout.SOUTH);
        this.add(new MessagePanel(msg), BorderLayout.CENTER);
        this.show();
    }

    /*============================================*/
    /* Listeners                                  */
    /*============================================*/
    /**
     * Invoked when 'Ok' is clicked.
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    /**
     * Listens to the <code>EditPropDialog</code> window.
     */
    public class ErrorWindowListener extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(WindowEvent e) {
            dispose();
        }
    }

    /*============================================*/
    /* Panels                                     */
    /*============================================*/
    /**
     * Panel showing the error message.
     */
    public class MessagePanel extends Panel {
/**
         * Creates a new MessagePanel object.
         *
         * @param msg DOCUMENT ME!
         */
        public MessagePanel(String msg) {
            TextArea text = new TextArea(msg, 7, 35, TextArea.SCROLLBARS_NONE);
            text.setEditable(false);
            this.add(text);
        }
    }

    /**
     * Panel containing button "Ok".
     */
    public class OkPanel extends Panel {
/**
         * Creates a new OkPanel object.
         *
         * @param action DOCUMENT ME!
         */
        public OkPanel(ActionListener action) {
            this.setLayout(new FlowLayout(FlowLayout.CENTER));

            Button b = new Button("Ok");
            b.addActionListener(action);
            this.add(b);
        }
    }
}
