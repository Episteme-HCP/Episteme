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
package org.jscience.architecture.traffic.configuration;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public abstract class ConfigPanel extends Panel {
    /** DOCUMENT ME! */
    ConfigDialog confd;

/**
     * Creates a new ConfigPanel object.
     *
     * @param cd DOCUMENT ME!
     */
    public ConfigPanel(ConfigDialog cd) {
        confd = cd;
        setLayout(null);
    }

    /**
     * Returns the configuration dialog this panal is part of
     *
     * @return DOCUMENT ME!
     */
    public ConfigDialog getConfigDialog() {
        return confd;
    }

    /**
     * Sets the configuration dialog this panel is part of
     *
     * @param cd DOCUMENT ME!
     */
    public void setconfigDialog(ConfigDialog cd) {
        confd = cd;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
    }

    /**
     * DOCUMENT ME!
     */
    public void ok() {
    }
}
