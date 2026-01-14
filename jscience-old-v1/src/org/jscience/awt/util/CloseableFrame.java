/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
