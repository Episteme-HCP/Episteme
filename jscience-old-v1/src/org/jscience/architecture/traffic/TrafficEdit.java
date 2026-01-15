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

/**
 * The main class that start the GLD Editor
 *
 * @author Group Model
 * @version 1.0
 */
public class TrafficEdit {
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     */
    public static void main(String[] params) {
        (new TrafficStarter(params, TrafficStarter.EDITOR)).start();
    }
}
