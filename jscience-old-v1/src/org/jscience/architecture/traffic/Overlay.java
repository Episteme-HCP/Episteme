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


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public interface Overlay {
    /**
     * Draws this Overlay on a given Graphics object.
     *
     * @param g The Graphics object to draw this Paintable on
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException;

    /**
     * Returns the type of overlay. 1 indicates a view overlay. 2
     * indicates a buffer overlay.
     *
     * @return DOCUMENT ME!
     */
    public int overlayType();
}
