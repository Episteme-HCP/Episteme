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
