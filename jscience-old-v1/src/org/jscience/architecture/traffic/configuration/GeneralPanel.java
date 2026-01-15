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

import org.jscience.architecture.traffic.algorithms.tlc.SignController;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;
import org.jscience.architecture.traffic.simulation.SimModel;
import org.jscience.architecture.traffic.simulation.statistics.StatisticsModel;
import org.jscience.architecture.traffic.simulation.statistics.TrackingView;
import org.jscience.architecture.traffic.util.ArrayUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author Group GUI
 * @version 1.0
 */
public class GeneralPanel extends ConfigPanel implements ActionListener {
    /** DOCUMENT ME! */
    Checkbox alwaysOnTop;

    /** DOCUMENT ME! */
    Checkbox safeNodeCrossing;

    /** DOCUMENT ME! */
    Checkbox crossNodes;

    /** DOCUMENT ME! */
    Checkbox useCustoms;

    /** DOCUMENT ME! */
    TextField sepChar;

    /** DOCUMENT ME! */
    Button setSepChar;

/**
     * Creates a new GeneralPanel object.
     *
     * @param cd DOCUMENT ME!
     */
    public GeneralPanel(ConfigDialog cd) {
        super(cd);

        alwaysOnTop = new Checkbox("Config dialog is always on top");
        alwaysOnTop.setBounds(0, 0, 250, 20);
        add(alwaysOnTop);

        crossNodes = new Checkbox("Roadusers cross nodes");
        crossNodes.setBounds(0, 25, 250, 20);
        add(crossNodes);

        safeNodeCrossing = new Checkbox(
                "Sign controller switches trafficlights safely");
        safeNodeCrossing.setBounds(0, 50, 250, 20);
        add(safeNodeCrossing);

        useCustoms = new Checkbox("Use custom roadusers");
        useCustoms.setBounds(0, 75, 250, 20);
        add(useCustoms);

        Label sclab = new Label("Statistics export separator character:");
        sclab.setBounds(0, 100, 220, 20);
        add(sclab);

        sepChar = new TextField();
        sepChar.addActionListener(this);
        sepChar.setBounds(230, 100, 50, 20);
        add(sepChar);

        setSepChar = new Button("Set");
        setSepChar.addActionListener(this);
        setSepChar.setBounds(285, 100, 40, 20);
        add(setSepChar);

        reset();
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        confd.setTitle("General configuration");

        alwaysOnTop.setState(ConfigDialog.AlwaysOnTop);
        safeNodeCrossing.setState(SignController.CrossNodesSafely);
        crossNodes.setState(SimModel.CrossNodes);
        useCustoms.setState(RoaduserFactory.UseCustoms);

        setSepChar();
    }

    /**
     * DOCUMENT ME!
     */
    public void ok() {
        ConfigDialog.AlwaysOnTop = alwaysOnTop.getState();
        SignController.CrossNodesSafely = safeNodeCrossing.getState();
        SimModel.CrossNodes = crossNodes.getState();
        RoaduserFactory.UseCustoms = useCustoms.getState();

        getSepChar();
    }

    /**
     * DOCUMENT ME!
     */
    public void getSepChar() {
        String text = sepChar.getText();
        byte[] bytes = text.getBytes();
        byte[] newbytes = new byte[bytes.length];
        byte temp;
        int counter = 0;

        for (int i = 0; i < bytes.length; i++) {
            temp = bytes[i];

            if (temp == 92) { // backslash '\'
                i++;
                temp = bytes[i];

                switch (temp) {
                case 116: {
                    temp = 9;

                    break;
                } // change 't' to '\t'

                case 110: {
                    temp = 10;

                    break;
                } // change 'n' to '\n'

                case 114: {
                    temp = 13;

                    break;
                } // change 'r' to '\r'
                }
            }

            newbytes[counter++] = temp;
        }

        String newtext = new String((byte[]) ArrayUtils.cropArray(newbytes,
                    counter));

        TrackingView.SEP = newtext;
        StatisticsModel.SEP = newtext;
    }

    /**
     * DOCUMENT ME!
     */
    public void setSepChar() {
        String text = TrackingView.SEP;
        byte[] bytes = text.getBytes();
        byte[] newbytes = new byte[bytes.length * 2];
        byte temp;
        int counter = 0;

        for (int i = 0; i < bytes.length; i++) {
            temp = bytes[i];

            switch (temp) {
            case 92: {
                newbytes[counter++] = 92;
                newbytes[counter++] = 92;

                break;
            }

            case 9: {
                newbytes[counter++] = 92;
                newbytes[counter++] = 116;

                break;
            }

            case 10: {
                newbytes[counter++] = 92;
                newbytes[counter++] = 110;

                break;
            }

            case 13: {
                newbytes[counter++] = 92;
                newbytes[counter++] = 114;

                break;
            }

            default: {
                newbytes[counter++] = temp;

                break;
            }
            }
        }

        String newtext = new String((byte[]) ArrayUtils.cropArray(newbytes,
                    counter));
        sepChar.setText(newtext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        getSepChar();
    }
}
