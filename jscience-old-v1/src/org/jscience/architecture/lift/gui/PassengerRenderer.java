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

package org.jscience.architecture.lift.gui;

import org.jscience.architecture.lift.Passenger;
import org.jscience.architecture.lift.World;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:54 $
 */
public class PassengerRenderer extends JPanel {
    /**
     * DOCUMENT ME!
     */
    public final static int PreferredWidth = 12;

    /**
     * DOCUMENT ME!
     */
    public final static int PreferredHeight = 48;

    /**
     * DOCUMENT ME!
     */
    public final static Dimension PreferredSize = new Dimension(PreferredWidth,
            PreferredHeight);

    /**
     * DOCUMENT ME!
     */
    Passenger P = null;

    /**
     * DOCUMENT ME!
     */
    final Color[] Colors = new Color[] {
            Color.LIGHT_GRAY, Color.BLACK, Color.GREEN.darker(),
            Color.RED.darker(), Color.GRAY, Color.YELLOW, Color.BLUE
        };

    /**
     * DOCUMENT ME!
     */
    int HU;

    /**
     * DOCUMENT ME!
     */
    int WU;

    /**
     * DOCUMENT ME!
     */
    ActionListener ALAL = null;

    /**
     * Creates a new PassengerRenderer object.
     *
     * @param MyPassenger DOCUMENT ME!
     */
    public PassengerRenderer(Passenger MyPassenger) {
        P = MyPassenger;
        setBackground(Colors[0]);
        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent ME) {
                    if (ME.getButton() == ME.BUTTON1) {
                        P.increaseDstF();

                        if (ALAL != null) {
                            ALAL.actionPerformed(new ActionEvent(
                                    (Object) this, 66, "x"));
                        }
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return (PreferredSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param AL DOCUMENT ME!
     */
    public void addActionListener(ActionListener AL) {
        ALAL = AL;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOpaque() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public void paintComponent(Graphics G) {
        G.setFont(G.getFont().deriveFont(Font.BOLD));

        HU = getSize().height / 24;
        WU = getSize().width / 12;

        Color BodyColor;
        Color ProgressBGColor;
        Color ProgressFGColor;
        Color StringColor;

        switch (P.getAngerLevel()) {
        case 0:
            BodyColor = Colors[2];
            StringColor = Colors[2];
            ProgressBGColor = Colors[1];
            ProgressFGColor = Colors[2];

            break;

        case 1:
            BodyColor = Colors[5];
            StringColor = Colors[5];
            ProgressBGColor = Colors[5];
            ProgressFGColor = Colors[3];

            break;

        case 2:
            BodyColor = Colors[3];
            StringColor = Colors[3];
            ProgressBGColor = Colors[3];
            ProgressFGColor = Colors[3];

            break;

        default:
            throw new RuntimeException("Not Implemented!");
        }

        G.setColor(Colors[0]);
        G.fillRect(WU * 0, HU * 0, 12 * WU, HU * 15);

        G.setColor(BodyColor);
        G.fillOval(WU * 3, HU * 0, WU * 6, HU * 3);
        // Head
        G.drawLine(WU * 6, HU * 3, WU * 6, HU * 10);
        // Body
        G.drawLine(WU * 0, HU * 3, WU * 6, HU * 6);
        // Left arm
        G.drawLine(WU * 12, HU * 3, WU * 6, HU * 6);
        // Rigth arm
        G.drawLine(WU * 10, HU * 14, WU * 6, HU * 9);
        // Left leg
        G.drawLine(WU * 2, HU * 14, WU * 6, HU * 9);

        // Rigth leg
        if (P.getState() == P.GETTING_IN) {
            G.setColor(Colors[0]);
            G.fillRect(WU * 0, HU * 0, P.get12Progress(), HU * 15);
            ProgressBGColor = Colors[1];
            ProgressFGColor = Colors[6];
            StringColor = Colors[6];
        }

        if (P.getState() == P.GETTING_OUT) {
            G.setColor(Colors[0]);
            G.fillRect(WU * P.get12Progress(), HU * 0,
                WU * (12 - P.get12Progress()), HU * 15);
            ProgressBGColor = Colors[1];
            ProgressFGColor = Colors[6];
            StringColor = Colors[6];
        }

        G.setColor(ProgressBGColor);
        G.fillRect(WU * 0, HU * 16, WU * 12, HU * 1);

        G.setColor(ProgressFGColor);
        G.fillRect(WU * 0, HU * 16, WU * P.get12Progress(), HU * 1);

        G.setColor(StringColor);
        G.drawString("" + World.toRelFloor(P.getDstF()), WU * 0, HU * 24);
    }

    /**
     * DOCUMENT ME!
     *
     * @param PP DOCUMENT ME!
     */
    public void renderPassenger(JPanel PP) {
        System.err.println("Rendering passenger #" + P.getID());
    }
}
