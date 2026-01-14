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

import org.jscience.architecture.traffic.Selection;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Node;

import java.awt.*;


/**
 * This implements zoom user action
 *
 * @author Group GUI
 * @version 1.0
 */
public class MoveAction implements ToolAction {
    /** DOCUMENT ME! */
    protected EditModel model;

    /** DOCUMENT ME! */
    protected Node node = null; // the Node we are moving

    /** DOCUMENT ME! */
    protected Point startPoint = null; // in infra space

    /** DOCUMENT ME! */
    protected int xOffset = 0; // node.coord.x - curpoint.x

    /** DOCUMENT ME! */
    protected int yOffset = 0; // node.coord.y - curpoint.y

/**
     * Creates a new MoveAction object.
     *
     * @param em DOCUMENT ME!
     */
    public MoveAction(EditModel em) {
        model = em;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean beingUsed() {
        return node != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean startMove(View view, Point p) {
        Class[] sf = { Node.class };
        Selection s = new Selection(view, sf, model.getInfrastructure());
        s.newSelection(p);

        if (!s.isEmpty()) {
            node = (Node) s.getSelectedObjects().getFirst();
            view.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            startPoint = p;
            xOffset = node.getCoord().x - p.x;
            yOffset = node.getCoord().y - p.y;

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void doMove(View view, Point p) {
        p.x += xOffset;
        p.y += yOffset;

        if ((node != null) && (p.distance(startPoint) > 4) &&
                nodeMayMove(node, view, p)) {
            model.moveNode(node, p);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void endMove(View view, Point p) {
        if (node == null) {
            return;
        }

        p.x += xOffset;
        p.y += yOffset;

        view.setCursor(null);

        // Check if node is allowed to move here
        if (!nodeMayMove(node, view, p)) {
            node = null;

            return;
        }

        // OK, move node
        model.moveNode(node, p);
        node = null;

        return;
    }

    /**
     * Returns true if given node may be moved to given point
     *
     * @param n DOCUMENT ME!
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nodeMayMove(Node n, View view, Point p) {
        Class[] sf = { Node.class };
        Selection s = new Selection(view, sf, model.getInfrastructure());
        Rectangle r = new Rectangle(p);
        int growsize = (Infrastructure.blockLength * 2) +
            (Infrastructure.blockWidth * 8) + 2;
        r.grow(growsize, growsize);
        s.newSelection(r);

        s.deselect(n);

        return s.isEmpty();
    }
}
