/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
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
