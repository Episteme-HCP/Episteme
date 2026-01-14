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
 * Instances of this class write an ephemeris of StateVector objects as
 * HTML to a JEditorPane instance.
 */
public class HtmlEphemerisWriter implements EphemerisWriter {
    /**
     * DOCUMENT ME!
     */
    private final JEditorPane destination;

/**
     * Constructs an instance of this class.
     */
    public HtmlEphemerisWriter(JEditorPane destination) {
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
        StringWriter html = new StringWriter();
        PrintWriter out = new PrintWriter(html);

        out.println("<html>");
        out.println("<body>");

        out.println("<p>Generated on " + dateGenerated.toString() +
            "&nbsp;</p>");

        out.print("<h2>");
        out.print(title);
        out.println("</h2>");

        out.println("<table align='center' border='1'>");

        out.println("<tr>");
        out.println(
            "<th bgcolor='black'><font color='white'>TSINCE</font></th>");
        out.println("<th bgcolor='black'><font color='white'>X</font></th>");
        out.println("<th bgcolor='black'><font color='white'>Y</font></th>");
        out.println("<th bgcolor='black'><font color='white'>Z</font></th>");
        out.println("<th bgcolor='black'><font color='white'>XDOT</font></th>");
        out.println("<th bgcolor='black'><font color='white'>YDOT</font></th>");
        out.println("<th bgcolor='black'><font color='white'>ZDOT</font></th>");
        out.println("</tr>");

        Iterator iter = ephemeris.iterator();

        while (iter.hasNext()) {
            OrbitalState sv = (OrbitalState) iter.next();

            out.println("<tr>");
            out.println("<td align='right'>");
            out.println(sv.TSINCE);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.X);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.Y);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.Z);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.XDOT);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.YDOT);
            out.println("</td>");
            out.println("<td>");
            out.println(sv.ZDOT);
            out.println("</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");

        destination.setContentType("text/html");
        destination.setText(html.toString());
    }
}
