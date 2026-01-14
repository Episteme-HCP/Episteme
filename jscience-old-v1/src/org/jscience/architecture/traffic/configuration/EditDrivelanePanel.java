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
import org.jscience.architecture.traffic.edit.EditModel;
import org.jscience.architecture.traffic.infrastructure.Drivelane;
import org.jscience.architecture.traffic.infrastructure.InfraException;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.infrastructure.Sign;
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
public class EditDrivelanePanel extends ConfigPanel implements ItemListener,
    ActionListener {
    /** DOCUMENT ME! */
    Drivelane lane;

    /** DOCUMENT ME! */
    Hyperlink alphaLink;

    /** DOCUMENT ME! */
    Hyperlink betaLink;

    /** DOCUMENT ME! */
    Hyperlink roadLink;

    /** DOCUMENT ME! */
    Label sign;

    /** DOCUMENT ME! */
    Choice typeList;

    /** DOCUMENT ME! */
    Checkbox left;

    /** DOCUMENT ME! */
    Checkbox ahead;

    /** DOCUMENT ME! */
    Checkbox right;

    /** DOCUMENT ME! */
    Button delete;

/**
     * Creates a new EditDrivelanePanel object.
     *
     * @param cd DOCUMENT ME!
     * @param l  DOCUMENT ME!
     */
    public EditDrivelanePanel(ConfigDialog cd, Drivelane l) {
        super(cd);

        Label rlab = new Label("Part of:");
        rlab.setBounds(0, 0, 100, 20);
        add(rlab);

        roadLink = new Hyperlink();
        roadLink.addActionListener(this);
        roadLink.setBounds(100, 0, 100, 20);
        add(roadLink);

        Label alab = new Label("Leads to:");
        alab.setBounds(0, 20, 100, 20);
        add(alab);

        alphaLink = new Hyperlink();
        alphaLink.addActionListener(this);
        alphaLink.setBounds(100, 20, 100, 20);
        add(alphaLink);

        Label blab = new Label("Comes from:");
        blab.setBounds(0, 40, 100, 20);
        add(blab);

        betaLink = new Hyperlink();
        betaLink.addActionListener(this);
        betaLink.setBounds(100, 40, 100, 20);
        add(betaLink);

        sign = new Label();
        sign.setBounds(0, 70, 200, 20);
        add(sign);

        Label allows = new Label("This drivelane allows:");
        allows.setBounds(0, 100, 150, 20);
        add(allows);

        typeList = new Choice();

        String[] descs = RoaduserFactory.getTypeDescs();
        int[] types = RoaduserFactory.getTypes();

        for (int i = 0; i < descs.length; i++)
            typeList.addItem(descs[i]);

        typeList.addItemListener(this);
        typeList.setBounds(150, 100, 100, 20);
        add(typeList);

        Label turnlab = new Label("Roadusers are allowed to:");
        turnlab.setBounds(0, 130, 300, 20);
        add(turnlab);

        left = new Checkbox("Turn left");
        left.setBounds(0, 150, 100, 20);
        left.addItemListener(this);
        add(left);

        ahead = new Checkbox("Go straight ahead");
        ahead.setBounds(100, 150, 150, 20);
        ahead.addItemListener(this);
        add(ahead);

        right = new Checkbox("Turn right");
        right.setBounds(250, 150, 100, 20);
        right.addItemListener(this);
        add(right);

        delete = new Button("Delete this drivelane");
        delete.setBounds(0, 190, 150, 24);
        delete.addActionListener(this);
        add(delete);

        setLane(l);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        if (lane.getSign().getType() == Sign.NO_SIGN) {
            sign.setText("Drivelane has no sign");
        } else {
            sign.setText("Drivelane has normal trafficlight");
        }

        if (lane.getRoad().getNumAllLanes() > 1) {
            delete.setEnabled(true);
        } else {
            delete.setEnabled(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void setLane(Drivelane l) {
        lane = l;
        confd.setTitle(lane.getName());
        reset();

        alphaLink.setText(lane.getNodeLeadsTo().getName());
        betaLink.setText(lane.getNodeComesFrom().getName());
        roadLink.setText(lane.getRoad().getName());

        try {
            left.setState(lane.getTarget(0));
            ahead.setState(lane.getTarget(1));
            right.setState(lane.getTarget(2));
        } catch (InfraException e) {
            Controller.reportError(e);
        }

        typeList.select(RoaduserFactory.getDescByType(lane.getType()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        ItemSelectable es = e.getItemSelectable();
        EditModel em = (EditModel) confd.getController().getModel();

        try {
            if (es == typeList) {
                int[] types = RoaduserFactory.getTypes();
                em.setLaneType(lane, types[typeList.getSelectedIndex()]);
            } else if (es == left) {
                em.setLaneTarget(lane, 0, left.getState());
            } else if (es == ahead) {
                em.setLaneTarget(lane, 1, ahead.getState());
            } else if (es == right) {
                em.setLaneTarget(lane, 2, right.getState());
            }
        } catch (TrafficException ex) {
            Controller.reportError(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == alphaLink) {
            confd.selectObject(lane.getNodeLeadsTo());
        } else if (source == betaLink) {
            confd.selectObject(lane.getNodeComesFrom());
        } else if (source == roadLink) {
            confd.selectObject(lane.getRoad());
        } else if (source == delete) {
            if (lane.getRoad().getNumAllLanes() > 1) {
                EditModel em = (EditModel) confd.getController().getModel();

                try {
                    confd.selectObject(lane.getRoad());
                    em.remLane(lane);
                } catch (InfraException ex) {
                    Controller.reportError(ex);
                }
            }
        }
    }
}
