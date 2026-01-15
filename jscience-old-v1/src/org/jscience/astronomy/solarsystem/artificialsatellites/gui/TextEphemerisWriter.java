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
