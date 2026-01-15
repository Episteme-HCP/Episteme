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

import java.awt.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
final class TleInputPane extends JScrollPane {
    /**
     * DOCUMENT ME!
     */
    JTextArea tleInputArea = new JTextArea();

    /**
     * Creates a new TleInputPane object.
     */
    TleInputPane() {
        tleInputArea.setText(
            "1 88888U          80275.98708465  .00073094  13844-3  66816-4 0    8            \n" +
            "2 88888  72.8435 115.9689 0086731  52.6988 110.5714 16.05824518  105            ");
        tleInputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        tleInputArea.setLineWrap(false);
        tleInputArea.setWrapStyleWord(true);
        setViewportView(tleInputArea);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                        "NORAD Two-Line Element Set"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)), getBorder()));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public ElementSet getTwoLineElementSet() throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new StringReader(tleInputArea.getText()));

            String tleLine1 = in.readLine();
            String tleLine2 = in.readLine();

            return new ElementSet(tleLine1, tleLine2);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable t) {
                }
            }
        }
    }
}
