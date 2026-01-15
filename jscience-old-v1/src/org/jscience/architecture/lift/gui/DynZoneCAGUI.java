package org.jscience.architecture.lift.gui;

import org.jscience.architecture.lift.Car;
import org.jscience.architecture.lift.InOutput;
import org.jscience.architecture.lift.World;
import org.jscience.architecture.lift.ca.DynZoneCA;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public class DynZoneCAGUI extends JFrame {
    /**
     * DOCUMENT ME!
     */
    JLabel[] DJLs;

    /**
     * DOCUMENT ME!
     */
    InputRenderer[][] IRs;

    /**
     * DOCUMENT ME!
     */
    DynZoneCA M;

    /**
     * DOCUMENT ME!
     */
    NorthPanel NP;

    /**
     * DOCUMENT ME!
     */
    int NoC;

    /**
     * DOCUMENT ME!
     */
    int NoF;

    /**
     * DOCUMENT ME!
     */
    SouthPanel SP;

    /**
     * Creates a new DynZoneCAGUI object.
     *
     * @param Master DOCUMENT ME!
     */
    public DynZoneCAGUI(DynZoneCA Master) {
        getContentPane().setLayout(new BorderLayout());

        // fel es le: melyik emelet kie?
        // fulkek: irany?
        M = Master;
        NoF = World.getNoF();
        NoC = World.getNoC();
        NP = new NorthPanel(NoC);
        SP = new SouthPanel(NoC, NoF);
        getContentPane().add(NP, BorderLayout.NORTH);
        getContentPane().add(SP, BorderLayout.SOUTH);
        setLocation(600, 0);
        pack();
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        for (int i = 0; i < NoF; i++) {
            InOutput CI = World.getInput(i);

            for (int j = 0; j < NoC; j++) {
                if (M.UpMasks[i] == j) {
                    if (M.DownMasks[i] == j) {
                        IRs[i][j].setColor(Color.PINK, 0);

                        for (int k = 0; k < NoF; k++) {
                            IRs[i][j].getInput().setSignal(k, CI.getSignal(k));
                        }
                    } else {
                        IRs[i][j].setColor(Color.YELLOW, 0);

                        for (int k = i; k < NoF; k++) {
                            IRs[i][j].getInput().setSignal(k, CI.getSignal(k));
                        }
                    }
                } else {
                    if (M.DownMasks[i] == j) {
                        IRs[i][j].setColor(Color.RED, 0);

                        for (int k = 0; k < i; k++) {
                            IRs[i][j].getInput().setSignal(k, CI.getSignal(k));
                        }
                    } else {
                        IRs[i][j].setColor(Color.LIGHT_GRAY, 0);

                        for (int k = 0; k < NoF; k++) {
                            IRs[i][j].getInput().setSignal(k, false);
                        }
                    }
                }

                IRs[i][j].repaint();
            }
        }

        /*
           *  System.err.print("Up: {");
           *  for (int i = 0; i < NoF; i++) {
           *  System.err.print(M.UpMasks[i] + (i == NoF - 1 ? "" : ", "));
           *  }
           *  System.err.println("}");
           *  System.err.print("Down: {");
           *  for (int i = 0; i < NoF; i++) {
           *  System.err.print(M.DownMasks[i] + (i == NoF - 1 ? "" : ", "));
           *  }
           *  System.err.println("}");
           *
           *  System.err.print("Dirs: {");
           *  for (int i = 0; i < NoC; i++) {
           *  System.err.print(M.getDir(i) + (i == NoC - 1 ? "" : ", "));
           *  }
           *  System.err.println("}");
           */
        for (int i = 0; i < NoC; i++) {
            switch (M.getDirection(i)) {
            case -1:
                DJLs[i].setText("?");
                DJLs[i].setForeground(Color.PINK);

                break;

            case Car.GOING_UP:
                DJLs[i].setText("Up");
                DJLs[i].setForeground(Color.YELLOW);

                break;

            case Car.GOING_DOWN:
                DJLs[i].setText("Down");
                DJLs[i].setForeground(Color.RED);

                break;

            default:
                throw new RuntimeException("Not implemented! #23654");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class NorthPanel extends JPanel {
        /**
         * Creates a new NorthPanel object.
         *
         * @param NoC DOCUMENT ME!
         */
        public NorthPanel(int NoC) {
            setLayout(new GridLayout(1, NoC, 2, 2));
            DJLs = new JLabel[NoC];

            for (int i = 0; i < NoC; i++) {
                DJLs[i] = new JLabel(" UNDEF ");
                add(DJLs[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class SouthPanel extends JPanel {
        /**
         * Creates a new SouthPanel object.
         *
         * @param NoC DOCUMENT ME!
         * @param NoF DOCUMENT ME!
         */
        public SouthPanel(int NoC, int NoF) {
            MatteBorder MB = new MatteBorder(1, 1, 1, 1, Color.BLACK);

            setLayout(new GridLayout(NoF, NoC, 0, 0));
            IRs = new InputRenderer[NoF][NoC];

            for (int i = 0; i < NoF; i++) {
                for (int j = 0; j < NoC; j++) {
                    IRs[NoF - i - 1][j] = new InputRenderer(new InOutput(NoF -
                                i - 1, NoF));
                    IRs[NoF - i - 1][j].setBorder(MB);

                    JPanel JP = new JPanel(new FlowLayout(FlowLayout.CENTER));

                    JP.add(IRs[NoF - i - 1][j]);
                    add(JP);
                }
            }
        }
    }
}
