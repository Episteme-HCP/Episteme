/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/

/*
 * ComponentPrintable.java
 *
 * Created on October 24, 2001, 11:28 AM
 */
package org.jscience.physics.nuclear.kinematics.nuclear.gui;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import javax.swing.*;


/**
 * Class copied from "Java 2D Graphics" by J. Knudsen, wraps AWT & Swing
 * Componenents in order to render them on a printer.
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class ComponentPrintable implements Printable {
    /**
     * DOCUMENT ME!
     */
    private Component mComponent;

/**
     * Creates new ComponentPrintable
     */
    public ComponentPrintable(Component c) {
        mComponent = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param pageFormat DOCUMENT ME!
     * @param pageIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     */
    public int print(java.awt.Graphics g, PageFormat pageFormat, int pageIndex)
        throws java.awt.print.PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        boolean wasBuffered = disableDoubleBuffering(mComponent);
        mComponent.paint(g2);
        restoreDoubleBuffering(mComponent, wasBuffered);

        return PAGE_EXISTS;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean disableDoubleBuffering(Component c) {
        if (!(c instanceof JComponent)) {
            return false;
        }

        JComponent jc = (JComponent) c;
        boolean wasBuffered = jc.isDoubleBuffered();
        jc.setDoubleBuffered(false);

        return wasBuffered;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param wasBuffered DOCUMENT ME!
     */
    private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
        if (c instanceof JComponent) {
            ((JComponent) c).setDoubleBuffered(wasBuffered);
        }
    }
}
