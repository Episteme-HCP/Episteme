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

package org.jscience.tests.biology.alignment.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;

import javax.swing.*;


/**
 * About screen.
 *
 * @author Sergio A. de Carvalho Jr.
 */
public class AboutDialog extends JDialog {
    /** DOCUMENT ME! */
    private JLabel image_label;

/**
     * Creates a new instance of the About screen.
     *
     * @param parent the parent frame
     */
    public AboutDialog(Frame parent) {
        super(parent, true);
        initComponents();
        pack();
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        URL image_filename;

        setTitle("About");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    closeDialog(e);
                }
            });

        image_filename = getClass().getResource("icons/about.jpg");

        if (image_filename != null) {
            image_label = new JLabel();
            image_label.setIcon(new ImageIcon(image_filename));
            getContentPane().add(image_label, BorderLayout.CENTER);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    private void closeDialog(WindowEvent e) {
        setVisible(false);
        dispose();
    }
}
