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
