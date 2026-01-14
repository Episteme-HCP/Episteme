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
import java.awt.event.ActionListener;

import java.util.Date;
import java.util.SortedSet;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
final class OutputPane extends JPanel {
    /**
     * DOCUMENT ME!
     */
    private final static String[] FORMATS = new String[] {
            "Plain Text", "HTML", "XML"
        };

    /**
     * DOCUMENT ME!
     */
    private final JComboBox formatField = new JComboBox(FORMATS);

    /**
     * DOCUMENT ME!
     */
    private final JEditorPane outputArea = new JEditorPane();

    /**
     * Creates a new OutputPane object.
     *
     * @param computeAction DOCUMENT ME!
     */
    OutputPane(ActionListener computeAction) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                    "Propagated Ephemeris"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel topPane = new JPanel();
        topPane.setLayout(new GridLayout(1, 2));

        JPanel computePane = new JPanel();
        FlowLayout computePaneLayout = new FlowLayout();
        computePaneLayout.setAlignment(FlowLayout.LEFT);
        computePane.setLayout(computePaneLayout);

        JButton computeButton = new JButton("Compute");
        computeButton.addActionListener(computeAction);
        computePane.add(computeButton);

        topPane.add(computePane);

        JPanel formatPane = new JPanel();
        FlowLayout formatPaneLayout = new FlowLayout();
        formatPaneLayout.setAlignment(FlowLayout.RIGHT);
        formatPane.setLayout(formatPaneLayout);

        formatPane.add(new JLabel("Format: "));
        formatPane.add(formatField);

        topPane.add(formatPane);

        add(topPane, BorderLayout.NORTH);

        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param dateGenerated DOCUMENT ME!
     * @param ephemeris DOCUMENT ME!
     * @param tle DOCUMENT ME!
     */
    void write(String title, Date dateGenerated, SortedSet ephemeris,
        ElementSet tle) {
        EphemerisWriter out = null;

        switch (formatField.getSelectedIndex()) {
        case 2:
            out = new XmlEphemerisWriter(outputArea);

            break;

        case 1:
            out = new HtmlEphemerisWriter(outputArea);

            break;

        case 0:default:
            out = new TextEphemerisWriter(outputArea);

            break;
        }

        out.write(title, dateGenerated, ephemeris, tle);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    void setText(String text) {
        outputArea.setText(text);
    }
}
