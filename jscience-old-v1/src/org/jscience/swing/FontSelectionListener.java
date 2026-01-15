/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.util.EventListener;


/**
 * The listener that's notified when a font selection changes
 *
 * @author Holger Antelmann
 * @see FontSelectionEvent
 * @see JFontChooser
 * @see JFontControl
 */
public interface FontSelectionListener extends EventListener {
    /**
     * called whenever the selected font changes
     *
     * @param font DOCUMENT ME!
     */
    void fontSelectionChanged(FontSelectionEvent font);
}
