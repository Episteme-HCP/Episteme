/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.awt.*;

import java.util.EventObject;


/**
 * An event that characterizes a change in the current font selection.
 *
 * @author Holger Antelmann
 *
 * @see FontSelectionListener
 */
public class FontSelectionEvent extends EventObject {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6260752888655538400L;

    /** DOCUMENT ME! */
    Font font;

/**
     * Creates a new FontSelectionEvent object.
     *
     * @param source DOCUMENT ME!
     * @param font   DOCUMENT ME!
     */
    public FontSelectionEvent(Object source, Font font) {
        super(source);
        this.font = font;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        return font;
    }
}
