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

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public class Nice_GUI extends JFrame implements GUI_Interface {
    /**
     * DOCUMENT ME!
     */
    private final static MatteBorder MB = new MatteBorder(1, 1, 1, 1,
            Color.BLACK);

    /**
     * DOCUMENT ME!
     */
    public final static int PassangerSpaces = 5;

    /**
     * DOCUMENT ME!
     */
    private PassengerPanel_Class PassengerPanel = null;

    /**
     * DOCUMENT ME!
     */
    public TickPanel_Class TickPanel = null;

    /**
     * DOCUMENT ME!
     */
    private CarPanel_Class CarPanel = null;

    /**
     * DOCUMENT ME!
     */
    private FloorPanel_Class FloorPanel = null;

    /**
     * DOCUMENT ME!
     */
    private InputPanel_Class InputPanel = null;

    /**
     * DOCUMENT ME!
     */
    private OutputsPanel_Class OutputsPanel = null;

    /**
     * DOCUMENT ME!
     */
    private CenterPanel_Class CenterPanel = null;

    /**
     * DOCUMENT ME!
     */
    private int NoC;

    /**
     * DOCUMENT ME!
     */
    private int NoF;

    /**
     * Creates a new Nice_GUI object.
     */
    public Nice_GUI() {
        super();
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getA() {
        return (TickPanel.Animated.isSelected());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTickValue() {
        return (TickPanel.TickTimes[TickPanel.TickTimeSelector.getSelectedIndex()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getTicked() {
        if (TickPanel.Ticked) {
            TickPanel.Ticked = false;

            return (true);
        } else {
            return (false);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void hideGUI() {
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isShown() {
        return (isVisible());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean render() {
        CarPanel.render();
        InputPanel.render();
        OutputsPanel.render();
        PassengerPanel.render();

        pack();
        repaint();
        TickPanel.render();

        return (true);
    }

    /**
     * DOCUMENT ME!
     */
    public void setup() {
        NoF = World.getNoF();
        NoC = World.getNoC();

        FloorPanel = new FloorPanel_Class();
        CarPanel = new CarPanel_Class();
        PassengerPanel = new PassengerPanel_Class(this);
        InputPanel = new InputPanel_Class();
        OutputsPanel = new OutputsPanel_Class();

        TickPanel = new TickPanel_Class();
        CenterPanel = new CenterPanel_Class();
        CenterPanel.add(OutputsPanel, BorderLayout.EAST);
        CenterPanel.add(PassengerPanel, BorderLayout.CENTER);
        CenterPanel.add(InputPanel, BorderLayout.WEST);

        getContentPane().add(TickPanel, BorderLayout.SOUTH);
        getContentPane().add(FloorPanel, BorderLayout.WEST);
        getContentPane().add(CenterPanel, BorderLayout.CENTER);
        getContentPane().add(CarPanel, BorderLayout.EAST);
    }

    /**
     * DOCUMENT ME!
     */
    public void showGUI() {
        pack();
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class CarPanel_Class extends JPanel {
        /**
         * DOCUMENT ME!
         */
        private CarRenderer[] CRs;

        /**
         * Creates a new CarPanel_Class object.
         */
        public CarPanel_Class() {
            super(new GridLayout(NoF, NoC));

            CarCanvas[][] CCs = new CarCanvas[NoC][NoF];

            for (int i = 0; i < NoF; i++) {
                for (int j = 0; j < NoC; j++) {
                    CarCanvas CC = new InnerCarCanvas();
                    JPanel JP = new JPanel(new BorderLayout());

                    CC.setCarPresent(false);
                    CCs[j][NoF - 1 - i] = CC;

                    JP.setBorder(MB);
                    JP.add((JPanel) CC, BorderLayout.CENTER);
                    JP.setBackground(Color.LIGHT_GRAY);
                    add(JP);
                }
            }

            CRs = new CarRenderer[NoC];

            for (int i = 0; i < NoC; i++) {
                CRs[i] = new CarRenderer((World.getCars())[i], CCs[i]);
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void render() {
            for (int j = 0; j < NoC; j++) {
                CRs[j].renderCar();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class CenterPanel_Class extends JPanel {
        /**
         * Creates a new CenterPanel_Class object.
         */
        public CenterPanel_Class() {
            super(new BorderLayout());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class FloorPanel_Class extends JPanel {
        /**
         * Creates a new FloorPanel_Class object.
         */
        public FloorPanel_Class() {
            super(new GridLayout(NoF, 1));

            for (int i = 0; i < NoF; i++) {
                JLabel JL = new JLabel("" + (World.getMaxF() - i), JLabel.RIGHT);

                JL.setFont(JL.getFont().deriveFont((float) 24.0));
                JL.setBorder(MB);
                JL.setForeground(Color.ORANGE.brighter());
                add(JL);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class InputPanel_Class extends JPanel {
        /**
         * DOCUMENT ME!
         */
        private InputRenderer[] IRs;

        /**
         * Creates a new InputPanel_Class object.
         */
        public InputPanel_Class() {
            super(new GridLayout(NoF, 1));
            IRs = new InputRenderer[NoF];

            for (int i = NoF - 1; i >= 0; i--) {
                JPanel JP = new JPanel(new BorderLayout());
                InputRenderer IR = new InputRenderer(World.getInput(i));

                JP.setBorder(MB);
                JP.add(IR, BorderLayout.CENTER);
                IRs[i] = IR;
                add(JP);
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void render() {
            for (int j = 0; j < NoF; j++) {
                IRs[j].repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class OutputsPanel_Class extends JPanel {
        /**
         * DOCUMENT ME!
         */
        private OutputsRenderer[] ORs;

        /**
         * Creates a new OutputsPanel_Class object.
         */
        public OutputsPanel_Class() {
            super(new GridLayout(NoF, 1));
            ORs = new OutputsRenderer[NoF];

            for (int i = NoF - 1; i >= 0; i--) {
                JPanel JP = new JPanel(new BorderLayout());
                OutputsRenderer OR = new OutputsRenderer(World.getOutputs(i));

                ORs[i] = OR;
                JP.setBorder(MB);
                JP.add(OR, BorderLayout.WEST);
                add(JP);
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void render() {
            for (int j = 0; j < NoF; j++) {
                ORs[j].repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class PassengerPanel_Class extends JPanel {
        /**
         * DOCUMENT ME!
         */
        private int FromLevel = -1;

        /**
         * DOCUMENT ME!
         */
        private Nice_GUI F;

        /**
         * DOCUMENT ME!
         */
        private JPanel[] PPs;

        /**
         * Creates a new PassengerPanel_Class object.
         *
         * @param Father DOCUMENT ME!
         */
        public PassengerPanel_Class(Nice_GUI Father) {
            super(new GridLayout(NoF, 1));
            F = Father;
            PPs = new JPanel[NoF];

            for (int i = NoF - 1; i >= 0; i--) {
                JPanel JX = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
                JPanel JP = new JPanel(new BorderLayout());

                JP.setBorder(MB);
                PPs[i] = JX;
                PPs[i].setPreferredSize(new Dimension((2 * (12 + 2)) + 4, 48 +
                        4));
                PPs[i].setSize(new Dimension((2 * (12 + 2)) + 4, 48 + 4));
                JP.add(JX, BorderLayout.CENTER);
                JP.setName("" + i);
                add(JP);
            }

            addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent ME) {
                        if (ME.getButton() == ME.BUTTON1) {
                            FromLevel = Integer.parseInt(ME.getComponent()
                                                           .getComponentAt(ME.getX(),
                                        ME.getY()).getName());
                        }
                    }

                    public void mouseReleased(MouseEvent ME) {
                        int ToLevel;
                        Component Cpt = ME.getComponent()
                                          .getComponentAt(ME.getX(), ME.getY());

                        if (Cpt == null) {
                            return;
                        }

                        ToLevel = Integer.parseInt(Cpt.getName());

                        if (ME.getButton() == ME.BUTTON1) {
                            if (FromLevel != -1) {
                                while (FromLevel == ToLevel) {
                                    ToLevel = (int) (Math.random() * World.getNoF());
                                }

                                World.add(new Passenger(FromLevel, ToLevel));
                                F.render();
                            }
                        }
                    }
                });
        }

        /**
         * DOCUMENT ME!
         */
        public void render() {
            for (int j = 0; j < NoF; j++) {
                ArrayList AAL = World.getPassengers(j);
                ArrayList BAL = World.getPassengersGettingOutAtFloor(j);

                boolean NeedsRefresh = (PPs[j].getComponentCount() != (AAL.size() +
                    BAL.size()));

                if (NeedsRefresh || (BAL.size() > 0)) {
                    PPs[j].removeAll();

                    PassengerRenderer PR;
                    final int fj = j;
                    ActionListener AL = new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                F.render();

                                //								World.refreshSignalOnFloor(fj);
                            }
                        };

                    for (int i = 0; i < BAL.size(); i++) {
                        PR = new PassengerRenderer((Passenger) BAL.get(i));
                        PR.addActionListener(AL);
                        PPs[j].add(PR);
                    }

                    for (int i = 0; i < AAL.size(); i++) {
                        PR = new PassengerRenderer((Passenger) AAL.get(i));
                        PR.addActionListener(AL);
                        PPs[j].add(PR);
                    }
                }

                int PrevWidth = PPs[j].getSize().width;
                int NewWidth = (Math.max(PPs[j].getComponentCount(),
                        PassangerSpaces) * (PassengerRenderer.PreferredWidth +
                    2)) + 4;

                if (NewWidth > PrevWidth) {
                    PPs[j].setPreferredSize(new Dimension(NewWidth,
                            PassengerRenderer.PreferredHeight + 4));
                    PPs[j].setSize(new Dimension(NewWidth,
                            PassengerRenderer.PreferredHeight + 4));
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    public class TickPanel_Class extends JPanel {
        /**
         * DOCUMENT ME!
         */
        public final String[] TickTimeStrings = new String[] {
                "5 ms", "50 ms", "100 ms", "250 ms", "500 ms", "1 s"
            };

        /**
         * DOCUMENT ME!
         */
        public final int[] TickTimes = new int[] { 5, 50, 100, 250, 500, 1000 };

        /**
         * DOCUMENT ME!
         */
        public JButton TickButton = new JButton("Tick");

        /**
         * DOCUMENT ME!
         */
        public JComboBox TickTimeSelector = new JComboBox(TickTimeStrings);

        /**
         * DOCUMENT ME!
         */
        public JTextField TickNumber = new JTextField(6);

        /**
         * DOCUMENT ME!
         */
        public JCheckBox Animated = new JCheckBox("Animate:");

        /**
         * DOCUMENT ME!
         */
        protected boolean Ticked = false;

        /**
         * DOCUMENT ME!
         */
        protected JLabel TotalTicks = new JLabel("Ticks: " +
                World.getTotalTicks());

        /**
         * Creates a new TickPanel_Class object.
         */
        public TickPanel_Class() {
            super(new FlowLayout());
            add(TickNumber);
            add(TickButton);
            add(TickTimeSelector);
            add(Animated);
            add(TotalTicks);

            TickTimeSelector.setSelectedIndex(2);
            addActionListeners();

            refreshAnimated();
        }

        /**
         * DOCUMENT ME!
         */
        public void refreshAnimated() {
            try {
                int AT = TickTimes[TickTimeSelector.getSelectedIndex()];
                String TT = TickNumber.getText().trim();
                int TN = (TT.equals("") ? 1 : Integer.parseInt(TT));

                Animated.setText("Animated (" + ((TN * 1000) / AT) + " T/s)");
            } catch (NumberFormatException NFE) {
                Animated.setText("Animated");
            }
        }

        /**
         * DOCUMENT ME!
         */
        public void render() {
            TotalTicks.setText("Ticks: " + World.getTotalTicks());
        }

        /**
         * DOCUMENT ME!
         */
        private void addActionListeners() {
            TickTimeSelector.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent AE) {
                        refreshAnimated();
                    }
                });
            TickNumber.addCaretListener(new CaretListener() {
                    public void caretUpdate(CaretEvent CE) {
                        refreshAnimated();
                    }
                });
            TickNumber.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent AE) {
                        TickButton.doClick();
                    }
                });
            TickButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent AE) {
                        Ticked = true;
                    }
                });
            Animated.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent AE) {
                        TickPanel.TickButton.setEnabled(!Animated.isSelected());
                    }
                });
        }
    }
}
