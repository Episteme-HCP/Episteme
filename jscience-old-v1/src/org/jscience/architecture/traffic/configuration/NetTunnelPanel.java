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

import org.jscience.architecture.traffic.infrastructure.NetTunnel;
import org.jscience.architecture.traffic.infrastructure.Road;
import org.jscience.architecture.traffic.simulation.SimulationRunningException;
import org.jscience.architecture.traffic.util.Hyperlink;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author Siets El snel & J Moritz
 */
public class NetTunnelPanel extends ConfigPanel implements ActionListener {
    /** DOCUMENT ME! */
    NetTunnel netTunnel;

    /** DOCUMENT ME! */
    TextField lpField;

    /** DOCUMENT ME! */
    TextField rpField;

    /** DOCUMENT ME! */
    TextField rhField;

    /** DOCUMENT ME! */
    Button lpSet;

    /** DOCUMENT ME! */
    Button rpSet;

    /** DOCUMENT ME! */
    Button rhSet;

    /** DOCUMENT ME! */
    Hyperlink roadLink;

    /** DOCUMENT ME! */
    Hyperlink nodeLink;

/**
     * Creates a new NetTunnelPanel object.
     *
     * @param cd        DOCUMENT ME!
     * @param netTunnel DOCUMENT ME!
     */
    public NetTunnelPanel(ConfigDialog cd, NetTunnel netTunnel) {
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

        Label lplab = new Label("Local port:");
        lplab.setBounds(0, 50, 100, 20);
        add(lplab);

        lpField = new TextField();
        lpField.addActionListener(this);
        lpField.setBounds(100, 50, 100, 20);
        add(lpField);

        lpSet = new Button("Set");
        lpSet.addActionListener(this);
        lpSet.setBounds(210, 50, 40, 20);
        add(lpSet);

        Label rplab = new Label("Remote port:");
        rplab.setBounds(0, 75, 100, 20);
        add(rplab);

        rpField = new TextField();
        rpField.addActionListener(this);
        rpField.setBounds(100, 75, 100, 20);
        add(rpField);

        rpSet = new Button("Set");
        rpSet.addActionListener(this);
        rpSet.setBounds(210, 75, 40, 20);
        add(rpSet);

        Label rhlab = new Label("Remote host:");
        rhlab.setBounds(0, 100, 100, 20);
        add(rhlab);

        rhField = new TextField();
        rhField.addActionListener(this);
        rhField.setBounds(100, 100, 100, 20);
        add(rhField);

        rhSet = new Button("Set");
        rhSet.addActionListener(this);
        rhSet.setBounds(210, 100, 40, 20);
        add(rhSet);

        setNetTunnel(netTunnel);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        Road road = netTunnel.getRoad();

        if (road != null) {
            roadLink.setText(road.getName());
            roadLink.setEnabled(true);
            nodeLink.setText(road.getOtherNode(netTunnel).getName());
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
     * @param nt DOCUMENT ME!
     */
    public void setNetTunnel(NetTunnel nt) {
        netTunnel = nt;
        confd.setTitle(netTunnel.getName());
        reset();

        lpField.setText(netTunnel.getLocalPort() + "");
        rpField.setText(netTunnel.getRemotePort() + "");
        rhField.setText(netTunnel.getRemoteHostname() + "");
    }

    /**
     * DOCUMENT ME!
     */
    public void ok() {
        setLocalPort();
        setRemotePort();
        setRemoteHost();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if ((source == lpField) || (source == lpSet)) {
            setLocalPort();
        } else if ((source == rpField) || (source == rpSet)) {
            setRemotePort();
        } else if ((source == rhField) || (source == rhSet)) {
            setRemoteHost();
        } else if (source == roadLink) {
            confd.selectObject(netTunnel.getRoad());
        } else if (source == nodeLink) {
            confd.selectObject(netTunnel.getRoad().getOtherNode(netTunnel));
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setLocalPort() {
        try {
            int pn = Integer.parseInt(lpField.getText());

            if ((pn > 65535) || (pn < 0)) {
                confd.showError("I have set the local port number to " +
                    netTunnel.getRemotePort() +
                    ", but that is not a legal value if " +
                    "you use TCP (you almost certainly do). The tunnel probably " +
                    "won't work.");
            }

            netTunnel.setLocalPort(pn);
        } catch (NumberFormatException e) {
            confd.showError("You can only use integers as port numbers.");
        } catch (SimulationRunningException e) {
            confd.showError("You must stop the simulation before changing the " +
                "properties of this NetTunnel");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setRemotePort() {
        try {
            int pn = Integer.parseInt(rpField.getText());

            if ((pn > 65535) || (pn < 0)) {
                confd.showError("I have set the remote port number to " +
                    netTunnel.getRemotePort() +
                    ", but that is not a legal value if " +
                    "you use TCP (you almost certainly do). The tunnel probably " +
                    "won't work.");
            }

            netTunnel.setRemotePort(pn);
        } catch (NumberFormatException e) {
            confd.showError("You can only use integers as port numbers.");
        } catch (SimulationRunningException e) {
            confd.showError("You must stop the simulation before changing the " +
                "properties of this NetTunnel");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setRemoteHost() {
        try {
            netTunnel.setRemoteHostname(rhField.getText());
        } catch (SimulationRunningException e) {
            confd.showError("You must stop the simulation before changing the " +
                "properties of this NetTunnel");
        }
    }
}
