/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
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
