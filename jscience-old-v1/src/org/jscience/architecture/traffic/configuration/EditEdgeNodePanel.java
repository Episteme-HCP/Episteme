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
