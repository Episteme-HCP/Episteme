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
import org.jscience.astronomy.solarsystem.artificialsatellites.OrbitalState;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import javax.swing.*;


/**
 * Instances of this class format an ephemeris of StateVector objects as
 * plain text to a JEditorPane instance.
 */
public class TextEphemerisWriter implements EphemerisWriter {
    /**
     * DOCUMENT ME!
     */
    private final JEditorPane destination;

/**
     * Constructs an instance of this class.
     */
    public TextEphemerisWriter(JEditorPane destination) {
        this.destination = destination;
    }

    /**
     * Writes the ephemeris of StateVector instances to the
     * destination.
     *
     * @param title Title of the ephemeris.
     * @param dateGenerated Date that the ephemeris was generated.
     * @param ephemeris Ephemeris of StateVector objects to write.
     * @param tle Two-line element set from which the ephemeris was generated.
     */
    public void write(String title, Date dateGenerated, SortedSet ephemeris,
        ElementSet tle) {
        StringWriter text = new StringWriter();
        PrintWriter out = new PrintWriter(text);

        out.println("Generated on " + dateGenerated.toString());

        out.println(title);

        out.print("       TSINCE");
        out.print("             X");
        out.print("                Y");
        out.print("                Z");
        out.print("                XDOT");
        out.print("             YDOT");
        out.print("             ZDOT");
        out.println();

        Iterator iter = ephemeris.iterator();

        while (iter.hasNext()) {
            OrbitalState sv = (OrbitalState) iter.next();
            out.println(sv.toString());
        }

        destination.setContentType("text/plain");
        destination.setText(text.toString());
    }
}
