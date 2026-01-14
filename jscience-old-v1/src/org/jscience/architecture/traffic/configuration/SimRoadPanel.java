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

import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.Road;
import org.jscience.architecture.traffic.util.Hyperlink;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimRoadPanel extends ConfigPanel implements ActionListener,
    ItemListener {
    /** DOCUMENT ME! */
    Road road;

    /** DOCUMENT ME! */
    Hyperlink alphaLink;

    /** DOCUMENT ME! */
    Hyperlink betaLink;

    /** DOCUMENT ME! */
    List alphaList;

    /** DOCUMENT ME! */
    List betaList;

    /** DOCUMENT ME! */
    Label length;

/**
     * Creates a new SimRoadPanel object.
     *
     * @param cd DOCUMENT ME!
     * @param r  DOCUMENT ME!
     */
    public SimRoadPanel(ConfigDialog cd, Road r) {
        super(cd);

        Label alab = new Label("Lanes to");
        alab.setBounds(0, 0, 60, 20);
        add(alab);

        alphaLink = new Hyperlink();
        alphaLink.setBounds(60, 0, 80, 20);
        alphaLink.addActionListener(this);
        add(alphaLink);

        Label blab = new Label("Lanes to");
        blab.setBounds(200, 0, 60, 20);
        add(blab);

        betaLink = new Hyperlink();
        betaLink.setBounds(260, 0, 80, 20);
        betaLink.addActionListener(this);
        add(betaLink);

        alphaList = new List();
        alphaList.setBounds(0, 25, 150, 80);
        alphaList.addItemListener(this);
        add(alphaList);

        betaList = new List();
        betaList.setBounds(200, 25, 150, 80);
        betaList.addItemListener(this);
        add(betaList);

        length = new Label();
        length.setBounds(0, 125, 200, 20);
        add(length);

        setRoad(r);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setRoad(Road r) {
        road = r;
        confd.setTitle(road.getName());

        alphaList.removeAll();
        betaList.removeAll();

        Drivelane[] lanes = road.getAlphaLanes();

        for (int i = 0; i < lanes.length; i++)
            alphaList.add(lanes[i].getName());

        lanes = road.getBetaLanes();

        for (int i = 0; i < lanes.length; i++)
            betaList.add(lanes[i].getName());

        alphaLink.setText(road.getAlphaNode().getName());
        betaLink.setText(road.getBetaNode().getName());
        length.setText("Road is " + road.getLength() + " units long");
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == alphaLink) {
            confd.selectObject(road.getAlphaNode());
        } else if (source == betaLink) {
            confd.selectObject(road.getBetaNode());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        ItemSelectable es = e.getItemSelectable();

        if (es == alphaList) {
            confd.selectObject(road.getAlphaLanes()[alphaList.getSelectedIndex()]);
        } else if (es == betaList) {
            confd.selectObject(road.getBetaLanes()[betaList.getSelectedIndex()]);
        }
    }
}
