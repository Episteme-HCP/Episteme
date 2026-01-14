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

package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.GeneralSettings;

import java.applet.Applet;

import java.awt.*;

import java.io.File;


/**
 * An Automobile can say toot(), that is what distuingishes it from other
 * Roadusers.
 *
 * @author Group Datastructures
 * @version 1.0
 */
public abstract class Automobile extends Roaduser {
    /** DOCUMENT ME! */
    protected static final int type = RoaduserFactory.getTypeByDesc(
            "Automobiles");

    /** DOCUMENT ME! */
    protected static final int speed = 2;

    /** DOCUMENT ME! */
    protected static final int passengers = 1;

    /** DOCUMENT ME! */
    protected static final String soundFileName = "org/jscience/architecture/traffic/infrastructure/carhorn.wav";

/**
     * Creates a new Automobile object.
     *
     * @param new_startNode DOCUMENT ME!
     * @param new_destNode  DOCUMENT ME!
     * @param pos           DOCUMENT ME!
     */
    public Automobile(Node new_startNode, Node new_destNode, int pos) {
        super(new_startNode, new_destNode, pos);
    }

/**
     * Creates a new Automobile object.
     */
    public Automobile() { // Empty constructor for loading
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param zf DOCUMENT ME!
     * @param angle DOCUMENT ME!
     */
    public void paint(Graphics g, int x, int y, float zf, double angle) {
        g.setColor(Color.black);
        g.fillRect((int) ((x - 3) * zf), (int) ((y - 3) * zf), (int) (7 * zf),
            (int) (7 * zf));
    }

    /**
     * DOCUMENT ME!
     */
    public void toot() {
        if (!GeneralSettings.getCurrentSettings()
                                .getPropertyBooleanValue("sound")) {
            return; // Sound is disabled 
        }

        try {
            (Applet.newAudioClip((new File(soundFileName)).toURL())).play();
        } catch (Exception e) {
            // Why can't I toot ???
            // Stupid java....
            // Well... I'll honk via stdout ....
            System.out.println("TOOOOOT!");
        }
    }

    // Specific XMLSerializable implementation 
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".roaduser-auto";
    }
}
