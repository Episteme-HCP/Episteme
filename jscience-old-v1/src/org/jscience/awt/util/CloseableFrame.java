// Closable Frame Class
// Written by: Craig A. Lindley
// Last Update: 08/02/98
package org.jscience.awt.util;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CloseableFrame extends Frame {
    // Private class data
    /** DOCUMENT ME! */
    private CloseableFrameIF closeableIF = null;

/**
     * Creates a new CloseableFrame object.
     *
     * @param title DOCUMENT ME!
     */
    public CloseableFrame(String title) {
        super(title);

        // Add window listener, which allows Frame window to close
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    windowIsClosing();
                }
            });
    }

    // Register listener
    /**
     * DOCUMENT ME!
     *
     * @param closeableIF DOCUMENT ME!
     */
    public void registerCloseListener(CloseableFrameIF closeableIF) {
        this.closeableIF = closeableIF;
    }

    // Called on close event
    /**
     * DOCUMENT ME!
     */
    public void windowIsClosing() {
        // Do call back if appropriate
        if (closeableIF != null) {
            closeableIF.windowClosing();
        }

        // Close the frame down
        dispose();
    }
}
