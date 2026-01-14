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
