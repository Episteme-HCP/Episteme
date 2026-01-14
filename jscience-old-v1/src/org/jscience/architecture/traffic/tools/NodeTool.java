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

import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.View;
import org.jscience.architecture.traffic.edit.EditController;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * Use this Tool to create Nodes in the infrastructure.
 *
 * @author Group GUI
 * @version 1.0
 */
public class NodeTool extends PopupMenuTool {
    /** DOCUMENT ME! */
    NodeAction na;

    /** DOCUMENT ME! */
    NodeTypeChoice typePanel;

/**
     * Creates a new NodeTool object.
     *
     * @param c DOCUMENT ME!
     */
    public NodeTool(EditController c) {
        super(c);
        na = new NodeAction(c.getEditModel());
        typePanel = new NodeTypeChoice();
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mousePressed(View view, Point p, Tool.Mask mask) {
        super.mousePressed(view, p, mask);

        if (mask.isLeft()) {
            na.doCreateNode(view, p, typePanel.getNodeType());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseReleased(View view, Point p, Tool.Mask mask) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param mask DOCUMENT ME!
     */
    public void mouseMoved(View view, Point p, Tool.Mask mask) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int overlayType() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     *
     * @throws TrafficException DOCUMENT ME!
     */
    public void paint(Graphics g) throws TrafficException {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Panel getPanel() {
        return typePanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    public class NodeTypeChoice extends Panel implements ItemListener {
        /** DOCUMENT ME! */
        int nodeType = 2;

/**
         * Creates a new NodeTypeChoice object.
         */
        public NodeTypeChoice() {
            super();
            setLayout(null);

            Choice nodeTypeSel = new Choice();
            nodeTypeSel.add("Edge node");
            nodeTypeSel.add("Traffic lights");
            nodeTypeSel.add("No signs");
            nodeTypeSel.add("Net-tunnel");
            nodeTypeSel.select(1);
            nodeTypeSel.addItemListener(this);
            add(nodeTypeSel);
            nodeTypeSel.setBounds(0, 0, 100, 20);
            setSize(200, 24);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getNodeType() {
            return nodeType;
        }

        /**
         * DOCUMENT ME!
         *
         * @param type DOCUMENT ME!
         */
        public void setNodeType(int type) {
            nodeType = type;
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            setNodeType(((Choice) e.getSource()).getSelectedIndex() + 1);
        }
    }
}
