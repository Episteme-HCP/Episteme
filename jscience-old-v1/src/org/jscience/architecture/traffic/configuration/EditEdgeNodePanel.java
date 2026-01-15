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
package org.jscience.architecture.traffic.configuration;

import org.jscience.architecture.traffic.infrastructure.EdgeNode;
import org.jscience.architecture.traffic.infrastructure.Road;
import org.jscience.architecture.traffic.util.Hyperlink;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class EditEdgeNodePanel extends ConfigPanel implements ActionListener {
    /** DOCUMENT ME! */
    EdgeNode edgenode;

    /** DOCUMENT ME! */
    Hyperlink roadLink;

    /** DOCUMENT ME! */
    Hyperlink nodeLink;

/**
     * Creates a new EditEdgeNodePanel object.
     *
     * @param cd DOCUMENT ME!
     * @param e  DOCUMENT ME!
     */
    public EditEdgeNodePanel(ConfigDialog cd, EdgeNode e) {
        super(cd);

        Label clab = new Label("Connects:");
        clab.setBounds(0, 0, 100, 20);
        add(clab);

        roadLink = new Hyperlink();
        roadLink.addActionListener(this);
        roadLink.setBounds(100, 0, 100, 20);
        add(roadLink);

        Label wlab = new Label("With:");
        wlab.setBounds(0, 20, 100, 20);
        add(wlab);

        nodeLink = new Hyperlink();
        nodeLink.addActionListener(this);
        nodeLink.setBounds(100, 20, 100, 20);
        add(nodeLink);

        setEdgeNode(e);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        Road road = edgenode.getRoad();

        if (road != null) {
            roadLink.setText(road.getName());
            roadLink.setEnabled(true);
            nodeLink.setText(road.getOtherNode(edgenode).getName());
            nodeLink.setEnabled(true);
        } else {
            roadLink.setText("null");
            roadLink.setEnabled(false);
            nodeLink.setText("null");
            nodeLink.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void setEdgeNode(EdgeNode e) {
        edgenode = e;
        confd.setTitle(edgenode.getName());
        reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == roadLink) {
            confd.selectObject(edgenode.getRoad());
        } else if (source == nodeLink) {
            confd.selectObject(edgenode.getRoad().getOtherNode(edgenode));
        }
    }
}
