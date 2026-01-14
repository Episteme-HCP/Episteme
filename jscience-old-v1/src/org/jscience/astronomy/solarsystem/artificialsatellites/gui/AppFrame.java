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

import org.jscience.astronomy.solarsystem.artificialsatellites.ElementSet;
import org.jscience.astronomy.solarsystem.artificialsatellites.Propagator;
import org.jscience.astronomy.solarsystem.artificialsatellites.PropagatorFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Date;
import java.util.SortedSet;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
final class AppFrame extends JFrame {
    /**
     * DOCUMENT ME!
     */
    private static final PropagatorFactory propagatorFactory = new PropagatorFactory();

    /**
     * DOCUMENT ME!
     */
    private final PropagationPane propagationPane = new PropagationPane();

    /**
     * DOCUMENT ME!
     */
    private final TleInputPane tlePane = new TleInputPane();

    /**
     * DOCUMENT ME!
     */
    private final OutputPane outputPane = new OutputPane(new ComputeAction());

    /**
     * Creates a new AppFrame object.
     */
    AppFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(new AppMenuBar(this));

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputPanel.add(propagationPane, BorderLayout.WEST);
        inputPanel.add(tlePane, BorderLayout.CENTER);

        contentPane.add(inputPanel, BorderLayout.NORTH);

        contentPane.add(outputPane, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private final class ComputeAction implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            outputPane.setText("Computing...");

            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            ElementSet tle = tlePane.getTwoLineElementSet();
                            String propagatorName = propagationPane.getPropagatorName();
                            Propagator p = propagatorFactory.newInstance(propagatorName,
                                    tle);

                            SortedSet ephemeris = p.generateEphemeris(propagationPane.getStartTime(),
                                    propagationPane.getEndTime(),
                                    propagationPane.getTimeStep());

                            String title = propagatorName;

                            if (tle.isDeep()) {
                                if (!p.isDeep()) {
                                    title += " (should use deep space ephemeris)";
                                }
                            } else {
                                if (p.isDeep()) {
                                    title += " (should use near earth ephemeris)";
                                }
                            }

                            outputPane.write(title, new Date(), ephemeris, tle);
                        } catch (Throwable t) {
                            StringWriter stackTrace = new StringWriter();
                            t.printStackTrace(new PrintWriter(stackTrace));
                            outputPane.setText(stackTrace.toString());
                        }
                    }
                });
        }
    }
}
