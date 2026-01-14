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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class ServerChooser extends JPanel implements ActionListener {
    /** DOCUMENT ME! */
    JTextField serverField = new JTextField();

    /** DOCUMENT ME! */
    JButton trace = new JButton("Trace");

    /** DOCUMENT ME! */
    TraceDialog traceDialog;

/**
     * Creates a new ServerChooser object.
     *
     * @param propertiesListener DOCUMENT ME!
     */
    protected ServerChooser(PropertiesListener propertiesListener) {
        traceDialog = new TraceDialog(propertiesListener);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        serverField.setAlignmentY((float) 0.5);
        trace.setAlignmentY((float) 0.5);
        add(serverField);
        serverField.setColumns(20);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(trace);
        trace.addActionListener(this);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ntp Server"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getServer() {
        return serverField.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultServer DOCUMENT ME!
     */
    protected void setServer(String defaultServer) {
        serverField.setText(defaultServer);
        serverField.setColumns(20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        //    trace.setEnabled(false);
        traceDialog.showTrace(getServer(), trace);

        //    trace.setEnabled(true);
    }
}
