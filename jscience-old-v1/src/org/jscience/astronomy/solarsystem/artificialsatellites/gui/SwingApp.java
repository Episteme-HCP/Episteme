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

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 
 */
public class SwingApp {
/**
     * Deny the ability to construct an instance of this class.
     */
    private SwingApp() {
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args Array of command line arguments
     */
    public static void main(String[] args) {
        try {
            AppFrame appFrame = new AppFrame();
            appFrame.setTitle(
                "org.jscience.astronomy.solarsystem.artificialsatellites");
            appFrame.setSize(1050, 700);

            appFrame.setVisible(true);
        } catch (Throwable t) {
            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            System.out.println(stackTrace.toString());
        }
    }
}
