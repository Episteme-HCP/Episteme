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

import org.jscience.architecture.traffic.infrastructure.Junction;
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
public class EditJunctionPanel extends ConfigPanel implements ActionListener {
    /** DOCUMENT ME! */
    Junction junction;

    /** DOCUMENT ME! */
    Hyperlink[] roadLinks;

    /** DOCUMENT ME! */
    Label nrsigns;

    /** DOCUMENT ME! */
    Label width;

/**
     * Creates a new EditJunctionPanel object.
     *
     * @param cd DOCUMENT ME!
     * @param j  DOCUMENT ME!
     */
    public EditJunctionPanel(ConfigDialog cd, Junction j) {
        super(cd);

        String[] dirs = { "north", "east", "south", "west" };

        roadLinks = new Hyperlink[4];

        for (int i = 0; i < 4; i++) {
            Label lab = new Label("Road " + dirs[i] + ": ");
            lab.setBounds(0, i * 20, 100, 20);
            add(lab);

            roadLinks[i] = new Hyperlink();
            roadLinks[i].addActionListener(this);
            roadLinks[i].setBounds(100, i * 20, 100, 20);
            add(roadLinks[i]);
        }

        nrsigns = new Label();
        nrsigns.setBounds(0, 100, 200, 20);
        add(nrsigns);

        width = new Label();
        width.setBounds(0, 120, 200, 20);
        add(width);

        setJunction(j);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        Road[] roads = junction.getAllRoads();

        for (int i = 0; i < 4; i++) {
            if (roads[i] != null) {
                roadLinks[i].setText(roads[i].getName());
                roadLinks[i].setEnabled(true);
            } else {
                roadLinks[i].setText("null");
                roadLinks[i].setEnabled(false);
            }
        }

        nrsigns.setText("Junction has " + junction.getNumRealSigns() +
            " trafficlights");
        width.setText("Junction is " + junction.getWidth() + " units wide");
    }

    /**
     * DOCUMENT ME!
     *
     * @param j DOCUMENT ME!
     */
    public void setJunction(Junction j) {
        junction = j;
        confd.setTitle(junction.getName());
        reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        for (int i = 0; i < 4; i++)
            if (source == roadLinks[i]) {
                confd.selectObject(junction.getAllRoads()[i]);
            }
    }
}
