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

// Message Box Code
// Written by: Craig A. Lindley
// Last Update: 08/04/98
package org.jscience.awt.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MessageBox extends Dialog implements ActionListener {
    // Private class data
    /** DOCUMENT ME! */
    private Button okButton;

/**
     * Creates a new MessageBox object.
     *
     * @param parent DOCUMENT ME!
     * @param title  DOCUMENT ME!
     * @param text   DOCUMENT ME!
     */
    public MessageBox(Frame parent, String title, String text) {
        // Create a modal dialog with parent window, title and text
        super(parent, title, true);

        Panel p1 = new Panel();

        // Instantiate label for display of text
        Label label = new Label(text, Label.CENTER);
        p1.add(label);
        add("North", p1);

        Panel p2 = new Panel();
        p2.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Instantiate the OK button in the dialog box
        okButton = new Button("OK");
        p2.add(okButton);
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    dispose();
                }
            });
        add("South", p2);

        // Add a handler for closing dialog from menu
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            });

        pack();
    }

/**
     * Creates a new MessageBox object.
     *
     * @param parent DOCUMENT ME!
     * @param Text   DOCUMENT ME!
     */
    public MessageBox(Frame parent, String Text) {
        this(parent, "User Advisory", Text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        dispose();
    }

    // Give OK button focus so enter will dismiss dialog
    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
        okButton.requestFocus();
        super.doLayout();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        MessageBox mb = new MessageBox(new Frame(), "User Advisory",
                "File is not an AU or WAV file");
        mb.show();
    }
}
