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

package org.jscience.chemistry.gui.extended.jviewer;

import java.awt.*;

import java.util.Vector;


/**
 * An implementation of a JViewer's event handler interface which allows
 * the contents of one or more JViewers to be manipulated in response to mouse
 * movements.
 *
 * @author Mike Brusati (brusati
 *
 * @see IEvntHandler
 */
public class EvntHandler implements IEvntHandler {
    /** DOCUMENT ME! */
    protected Vector viewers = new Vector();

    /** DOCUMENT ME! */
    protected int prevX = 0;

    /** DOCUMENT ME! */
    protected int prevY = 0;

/**
     * Default constructor.
     */
    public EvntHandler() {
    }

/**
     * Convenience constructor.  Useful for applications requiring one JViewer.
     *
     * @param v a JViewer whose contents will be manipulated by this event
     *          handler.
     */
    public EvntHandler(JViewer v) {
        addJViewer(v);
    }

    /**
     * Add another JViewer to the list of JViewers manipulated by this
     * event handler.
     *
     * @param v a JViewer whose contents will be manipulated by this event
     *        handler.
     */
    public void addJViewer(JViewer v) {
        viewers.addElement(v);
    }

    /**
     * The event handler for all known JViewers.
     *
     * @param e a mouse event
     *
     * @return DOCUMENT ME!
     */
    public boolean handleEvnt(Event e) {
        switch (e.id) {
        case Event.MOUSE_DOWN:
            prevX = e.x;
            prevY = e.y;

            return true;

        case Event.MOUSE_UP:
            return true;

        case Event.MOUSE_DRAG:

            JViewer v;
            int dx = prevX - e.x;
            int dy = prevY - e.y;

            for (int i = 0; i < viewers.size(); i++) {
                v = (JViewer) viewers.elementAt(i);

                if (v.transform(dx, dy)) {
                    v.repaint();
                }
            }

            prevX = e.x;
            prevY = e.y;

            return true;
        }

        return false;
    }
}
