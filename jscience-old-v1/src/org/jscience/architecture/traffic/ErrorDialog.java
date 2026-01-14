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
