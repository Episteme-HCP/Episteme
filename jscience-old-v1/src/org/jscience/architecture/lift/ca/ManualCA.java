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

package org.jscience.architecture.lift.ca;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.World;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is nice Manual (drive-by-mouse) {@link CA}. Good for debugging. It may
 * have other uses as well.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:53 $
 */
public class ManualCA extends JFrame implements CA {
    /** UP and DOWN */
    public final static short BOTH_WAY = 0;

    /** UP */
    public final static short UP_WAY = 1;

    /** DOWN */
    public final static short DOWN_WAY = 2;

    /**
     * DOCUMENT ME!
     */
    private Car[] Cars = null;

    /**
     * DOCUMENT ME!
     */
    private JComboBox CarSelector = new JComboBox();

    /**
     * DOCUMENT ME!
     */
    private JButton OpenClose = new JButton("Close");

    /**
     * DOCUMENT ME!
     */
    private JButton Send = new JButton("Send");

    /**
     * DOCUMENT ME!
     */
    private JComboBox DirCB = new JComboBox(new String[] { "Both", "Up", "Down", });

    /**
     * DOCUMENT ME!
     */
    private short[] Directions = null;

/**
     * Constructor
     *
     * @param X       The X coordinate of the location of the GUI
     * @param Y       The Y coordinate of the location of the GUI
     * @param Visible {@code true} makes the GUI visible
     */
    public ManualCA(int X, int Y, boolean Visible) {
        CarSelector.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent AE) {
                    refresh();
                }
            });
        OpenClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent AE) {
                    Car C = Cars[CarSelector.getSelectedIndex()];

                    if (OpenClose.getText().equals("Open")) {
                        C.openDoor();
                    }

                    if (OpenClose.getText().equals("Close")) {
                        C.closeDoor();
                    }

                    refresh();
                }
            });
        DirCB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent AE) {
                    int Index = CarSelector.getSelectedIndex();

                    if (DirCB.getSelectedIndex() == 0) {
                        Directions[Index] = BOTH_WAY;
                    }

                    if (DirCB.getSelectedIndex() == 1) {
                        Directions[Index] = UP_WAY;
                    }

                    if (DirCB.getSelectedIndex() == 2) {
                        Directions[Index] = DOWN_WAY;
                    }

                    refresh();
                }
            });

        final JFrame JF = this;

        Send.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent AE) {
                    Car C = Cars[CarSelector.getSelectedIndex()];
                    int TargetLevel = -1;

                    if (Send.getText().equals("Send")) {
                        String Answer = JOptionPane.showInputDialog(JF,
                                "Which level?");

                        try {
                            TargetLevel = Integer.parseInt(Answer);
                        } catch (Exception E) {
                            return;
                        }

                        int TF = World.toAbsFloor(TargetLevel);

                        if ((TF >= 0) && (TF < World.getNoF()) &&
                                (C.getCrtF() != TF)) {
                            C.gotoFloor(TF);
                        } else {
                            JOptionPane.showMessageDialog(JF, "Invalid Floor!");
                        }
                    }

                    refresh();
                }
            });

        setLocation(X, Y);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(CarSelector, BorderLayout.NORTH);
        getContentPane().add(OpenClose, BorderLayout.CENTER);
        getContentPane().add(Send, BorderLayout.SOUTH);
        getContentPane().add(DirCB, BorderLayout.WEST);
        setCars(World.getCars());
        pack();
        setVisible(Visible);
    }

    /**
     * Notifies the user through {@link java.lang.System}.err that a
     * registered call has been deleted to floor {@code To}
     *
     * @param To DOCUMENT ME!
     */
    public void deleteCall(int To) {
        System.err.println("Call deleted to Floor #" + To);
    }

    /**
     * {@link CA}
     *
     * @param From DOCUMENT ME!
     * @param To DOCUMENT ME!
     * @param CarIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean goes(int From, int To, int CarIndex) {
        if (Cars != null) {
            if (Cars[CarIndex].getCrtF() == From) {
                int CS = Cars[CarIndex].getState();

                if ((CS == Car.WAITING) || (CS == Car.OPENING)) {
                    short CD = Directions[CarIndex];

                    if (CD == BOTH_WAY) {
                        return (true);
                    }

                    if ((CD == DOWN_WAY) && (To <= From)) {
                        return (true);
                    }

                    if ((CD == UP_WAY) && (To >= From)) {
                        return (true);
                    }
                }
            }
        }

        return (false);
    }

    /**
     * Notifies the user through {@link javax.swing.JOptionPane} that
     * new command has been registered to floor {@code AbsFloor} in {@link
     * Car}{@code C}
     *
     * @param C DOCUMENT ME!
     * @param AbsFloor DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public void issueCommand(Car C, int AbsFloor) {
        for (int i = 0; i < Cars.length; i++) {
            if (Cars[i] == C) {
                JOptionPane.showMessageDialog(this,
                    "Car #" + i + " should go to Floor #" +
                    World.toRelFloor(AbsFloor), "Message",
                    JOptionPane.INFORMATION_MESSAGE);

                return;
            }
        }

        throw new RuntimeException("There is no such Car! Error #442");
    }

    /**
     * Redraws the GUI. Automatically called after cars' state changed.
     */
    public void refresh() {
        int CarIndex = CarSelector.getSelectedIndex();

        if (Cars == null) {
            System.err.println("KK");
        }

        Car C = Cars[CarIndex];

        DirCB.setSelectedIndex(Directions[CarIndex]);

        boolean notMoving = ((C.getState() == C.PARKING) ||
            (C.getState() == C.WAITING));
        boolean isParking = C.getState() == C.PARKING;

        OpenClose.setText(notMoving ? (isParking ? "Open" : "Close") : "-");
        OpenClose.setEnabled(notMoving);

        Send.setText(isParking ? "Send" : "-");
        Send.setEnabled(isParking);
    }

    /**
     * Notifies the user through {@link java.lang.System}.err that a
     * new call has been registered to floor {@code To}
     *
     * @param To DOCUMENT ME!
     */
    public void registerCall(int To) {
        System.err.println("Call registered to Floor #" + To);
    }

    /**
     * {@link org.jscience.architecture.lift.Tickable}
     */
    public void tick() {
        refresh();
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewCars DOCUMENT ME!
     */
    private void setCars(Car[] NewCars) {
        Cars = NewCars;
        Directions = new short[NewCars.length];

        for (int i = 0; i < NewCars.length; i++) {
            Directions[i] = BOTH_WAY;
        }

        CarSelector.removeAllItems();

        for (int i = 0; i < Cars.length; i++) {
            CarSelector.addItem(Cars[i].toString());
        }

        if (Cars.length > 0) {
            CarSelector.setSelectedIndex(0);
        }

        refresh();
    }
}
