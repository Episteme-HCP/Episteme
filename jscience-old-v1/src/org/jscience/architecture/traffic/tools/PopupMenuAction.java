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

package org.jscience.architecture.traffic.tools;

import org.jscience.architecture.traffic.*;

import java.awt.*;

import java.util.LinkedList;


/**
 * This implements the PopupMenu user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class PopupMenuAction implements ToolAction {
    /** DOCUMENT ME! */
    Controller controller;

/**
     * Creates a new PopupMenuAction object.
     *
     * @param con DOCUMENT ME!
     */
    public PopupMenuAction(Controller con) {
        controller = con;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doPopupMenu(View view, Point p) {
        Selection cs = controller.getCurrentSelection();
        Selection s = new Selection(cs);

        s.newSelection(p);

        if (s.getNumSelectedObjects() > 0) {
            cs.setSelectedObjects(s.getSelectedObjects());
        }

        LinkedList list = cs.getSelectedObjects();

        if (list.size() > 0) {
            try {
                Selectable o = (Selectable) list.getFirst();
                PopupMenu menu = controller.getPopupMenuFor(o);
                view.add(menu);

                Point p2 = view.toView(p);
                menu.show(view, p2.x, p2.y);
            } catch (PopupException e) {
                Controller.reportError(e);
            }
        }
    }
}
