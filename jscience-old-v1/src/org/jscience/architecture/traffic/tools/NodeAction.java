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

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Node;

import java.awt.*;


/**
 * This implements zoom user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class NodeAction implements ToolAction {
    /** DOCUMENT ME! */
    EditModel model;

/**
     * Creates a new NodeAction object.
     *
     * @param m DOCUMENT ME!
     */
    public NodeAction(EditModel m) {
        model = m;
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
     * @param type DOCUMENT ME!
     */
    public void doCreateNode(View view, Point p, int type) {
        Class[] sf = { Node.class };
        Selection s = new Selection(view, sf, model.getInfrastructure());
        Rectangle r = new Rectangle(p);
        int growsize = (Infrastructure.blockLength * 2) +
            (Infrastructure.blockWidth * 8) + 2;
        r.grow(growsize, growsize);
        s.newSelection(r);

        if (s.getNumSelectedObjects() > 0) {
            return;
        }

        try {
            model.addNode(p, type);
        } catch (InfraException e) {
            Controller.reportError(e);
        }
    }
}
