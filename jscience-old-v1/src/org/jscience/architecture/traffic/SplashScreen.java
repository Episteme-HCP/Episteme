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
package org.jscience.architecture.traffic;

import java.awt.*;


/*
 *The Splash screen for the program, which gets the focus on starting up the application
 */
public class SplashScreen extends Window {
    /** DOCUMENT ME! */
    private static final int WIDTH = 550;

    /** DOCUMENT ME! */
    private static final int HEIGHT = 424;

    /** DOCUMENT ME! */
    Image image;

/**
     * Creates a new SplashScreen object.
     *
     * @param parent DOCUMENT ME!
     */
    public SplashScreen(Controller parent) {
        super(parent);

        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.createImage("org/jscience/architecture/images/splash.jpg");
        requestFocus();

        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);

        try {
            mt.waitForID(0);
        } catch (InterruptedException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        setSize(WIDTH, HEIGHT);
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(image, 0, 0, this);
        toFront();
    }
}
