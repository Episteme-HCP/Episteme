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

import org.jscience.architecture.traffic.Controller;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.infrastructure.EdgeNode;
import org.jscience.architecture.traffic.infrastructure.Road;
import org.jscience.architecture.traffic.infrastructure.Roaduser;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.simulation.SimController;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.simulation.statistics.TrackerFactory;
import org.jscience.architecture.traffic.util.Hyperlink;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ConcurrentModificationException;
import java.util.ListIterator;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class SimEdgeNodePanel extends ConfigPanel implements ItemListener,
    ActionListener {
    /** DOCUMENT ME! */
    EdgeNode edgenode;

    /** DOCUMENT ME! */
    TextField spawnFreq;

    /** DOCUMENT ME! */
    Choice spawnTypes;

    /** DOCUMENT ME! */
    Button setSpawn;

    /** DOCUMENT ME! */
    Hyperlink wqlLink;

    /** DOCUMENT ME! */
    Hyperlink twtLink;

    /** DOCUMENT ME! */
    Hyperlink ruaLink;

    /** DOCUMENT ME! */
    Hyperlink roadLink;

    /** DOCUMENT ME! */
    Hyperlink nodeLink;

    /** DOCUMENT ME! */
    Label[] queue;

/**
     * Creates a new SimEdgeNodePanel object.
     *
     * @param cd DOCUMENT ME!
     * @param e  DOCUMENT ME!
     */
    public SimEdgeNodePanel(ConfigDialog cd, EdgeNode e) {
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

        wqlLink = new Hyperlink("Track waiting queue length");
        wqlLink.addActionListener(this);
        wqlLink.setBounds(0, 50, 200, 20);
        add(wqlLink);

        twtLink = new Hyperlink("Track trip waiting time");
        twtLink.addActionListener(this);
        twtLink.setBounds(0, 70, 200, 20);
        add(twtLink);

        ruaLink = new Hyperlink("Track roadusers arrived");
        ruaLink.addActionListener(this);
        ruaLink.setBounds(0, 90, 200, 20);
        add(ruaLink);

        Label lab = new Label("Spawnfrequency for");
        lab.setBounds(0, 120, 120, 20);
        add(lab);

        spawnTypes = new Choice();
        spawnTypes.addItemListener(this);

        String[] descs = RoaduserFactory.getConcreteTypeDescs();

        for (int i = 0; i < descs.length; i++)
            spawnTypes.addItem(descs[i]);

        spawnTypes.setBounds(0, 140, 100, 20);
        add(spawnTypes);

        lab = new Label("is");
        lab.setBounds(105, 140, 15, 20);
        add(lab);

        spawnFreq = new TextField();
        spawnFreq.setBounds(120, 140, 40, 20);
        spawnFreq.addActionListener(this);
        add(spawnFreq);

        setSpawn = new Button("Set");
        setSpawn.addActionListener(this);
        setSpawn.setBounds(170, 140, 50, 20);
        add(setSpawn);

        lab = new Label("Waiting in queue:");
        lab.setBounds(200, 0, 150, 20);
        add(lab);

        int nrtypes = RoaduserFactory.statArrayLength();
        queue = new Label[nrtypes];

        for (int i = 0; i < nrtypes; i++) {
            lab = new Label();
            lab.setBounds(200, (i * 20) + 20, 150, 20);
            add(lab);
            queue[i] = lab;
        }

        setEdgeNode(e);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        setSpawnFreq();

        try {
            int nrtypes = RoaduserFactory.statArrayLength();
            int[] nrwaiting = new int[nrtypes];
            Roaduser ru;
            ListIterator li = edgenode.getWaitingQueue().listIterator();

            while (li.hasNext()) {
                ru = (Roaduser) li.next();
                nrwaiting[RoaduserFactory.getStatIndexByType(ru.getType())]++;
                nrwaiting[0]++;
            }

            for (int i = 0; i < nrtypes; i++) {
                queue[i].setText(nrwaiting[i] + " - " +
                    RoaduserFactory.getDescByStatIndex(i));
            }
        }
        // SimModel thread changed the queue while we were updating, try again.
        catch (ConcurrentModificationException e) {
            reset();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setSpawnFreq() {
        int type = getSpawnType();
        float freq = edgenode.getSpawnFrequency(type);
        spawnFreq.setText("" + ((freq > 0) ? freq : 0));
    }

    /**
     * DOCUMENT ME!
     */
    public void setSpawnType() {
        try {
            SimModel sm = (SimModel) confd.getController().getModel();
            float fr = Float.parseFloat(spawnFreq.getText());
            sm.setSpawnFrequency(edgenode, getSpawnType(), fr);
        } catch (NumberFormatException ex) {
            confd.showError("You must enter a float");
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
        setSpawnType();

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
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == spawnTypes) {
            setSpawnFreq();
        }
    }

    /**
     * Returns the currently selected roaduser type
     *
     * @return DOCUMENT ME!
     */
    public int getSpawnType() {
        int[] types = RoaduserFactory.getConcreteTypes();

        return types[spawnTypes.getSelectedIndex()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if ((source == setSpawn) || (source == spawnFreq)) {
            setSpawnType();
        } else if (source == wqlLink) {
            track(TrackerFactory.SPECIAL_QUEUE);
        } else if (source == twtLink) {
            track(TrackerFactory.SPECIAL_WAIT);
        } else if (source == ruaLink) {
            track(TrackerFactory.SPECIAL_ROADUSERS);
        } else if (source == roadLink) {
            confd.selectObject(edgenode.getRoad());
        } else if (source == nodeLink) {
            confd.selectObject(edgenode.getRoad().getOtherNode(edgenode));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void track(int type) {
        SimController sc = (SimController) confd.getController();

        try {
            TrackerFactory.showTracker(sc.getSimModel(), sc, edgenode, type);
        } catch (TrafficException ex) {
            Controller.reportError(ex);
        }
    }
}
