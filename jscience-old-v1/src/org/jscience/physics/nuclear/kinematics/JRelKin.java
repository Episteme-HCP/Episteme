/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/
package org.jscience.physics.nuclear.kinematics;

import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;
import org.jscience.physics.nuclear.kinematics.nuclear.TableText;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.ComponentPrintable;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.ValuesChooser;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.ValuesListener;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.table.KinematicsOutputTable;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.table.KinematicsOutputTableModel;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.table.ReactionTable;
import org.jscience.physics.nuclear.kinematics.nuclear.gui.table.ReactionTableModel;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.*;

import javax.swing.*;


/**
 * Program to calculate relativistic kinematics for the SplitPole detector.
 * Puts out put in a nice table.
 *
 * @author Dale Visser
 * @version 1.0
 */
public class JRelKin extends JFrame implements ValuesListener, ActionListener {
    /**
     * DOCUMENT ME!
     */
    static final double[] INITIAL_BEAM_ENERGIES = { 90.0 };

    /**
     * DOCUMENT ME!
     */
    static final double[] INITIAL_RESIDUAL_EXCITATIONS = { 0.0 };

    /**
     * DOCUMENT ME!
     */
    static final double[] INITIAL_LAB_ANGLES = { 10.0 };

    /**
     * DOCUMENT ME!
     */
    static ReactionTable rt;

    /**
     * DOCUMENT ME!
     */
    static KinematicsOutputTableModel kotm;

    /**
     * DOCUMENT ME!
     */
    private static final String VERSION = "1.2";

    /**
     * DOCUMENT ME!
     */
    private static final String INTRO = "JRelKin " + VERSION +
        " written by Dale W. Visser\n" +
        "released under the University of Illinois/NCSA Open Source License\n" +
        "see Help|License... for the license text.\n\n" +
        "Double-click on nuclei in top table to edit them.\n" +
        "Enter experimental parameters in the fields in the middle panel.\n" +
        "'Range' expects min,max,delta separated by spaces.\n" +
        "'List of values' expects multiple values separated by spaces.\n" +
        "Changes take effect when user hits return in a field.\n\n" +
        "With finite target thickness the code calculates for beam interaction\n" +
        "at the front (upstream side)  and back (downstream side) of the target,\n" +
        "outputting the results on two separate rows. This is useful for\n" +
        "determining the effect of target thickness on resolution.\n" +
        "The target is assumed to be 100% composed of the target element with\n" +
        "natural abundances.\n";

    /**
     * DOCUMENT ME!
     */
    ValuesChooser be = new ValuesChooser(this, "Beam Energy", "MeV",
            INITIAL_BEAM_ENERGIES);

    /**
     * DOCUMENT ME!
     */
    ValuesChooser ex4 = new ValuesChooser(this, "Ex(Residual)", "MeV",
            INITIAL_RESIDUAL_EXCITATIONS);

    /**
     * DOCUMENT ME!
     */
    ValuesChooser la3 = new ValuesChooser(this, "Lab Angle", "deg",
            INITIAL_LAB_ANGLES);

    /**
     * DOCUMENT ME!
     */
    JTextField tt = new JTextField("0.0");

    /**
     * DOCUMENT ME!
     */
    private Container pane;

    /**
     * DOCUMENT ME!
     */
    private PageFormat mPageFormat;

    /**
     * DOCUMENT ME!
     */
    private char micro = '\u03bc';

    /**
     * DOCUMENT ME!
     */
    private char up2 = '\u00b2';

    /**
     * DOCUMENT ME!
     */
    private String units = micro + "g/cm" + up2;

    /**
     * DOCUMENT ME!
     */
    JDialog licenseD;

    /**
     * DOCUMENT ME!
     */
    JDialog instructD;

    /**
     * Creates a new JRelKin object.
     */
    public JRelKin() {
        super("JRelKin");
        System.out.println(INTRO);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }

                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }
            });
        pane = getContentPane();

        PrinterJob pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();
        mPageFormat.setOrientation(PageFormat.LANDSCAPE);
        rt = new ReactionTable(new ReactionTableModel(this));
        setupMenu();
        pane.add(rt, BorderLayout.NORTH);

        JPanel pcenter = new JPanel(new BorderLayout());
        pane.add(pcenter, BorderLayout.CENTER);
        pcenter.add(getChoicePanel(), BorderLayout.NORTH);

        try {
            kotm = new KinematicsOutputTableModel(rt, be.getValues(),
                    ex4.getValues(), la3.getValues());
        } catch (KinematicsException ke) {
            System.err.println(ke);
        } catch (NuclearException ke) {
            System.err.println(ke);
        }

        KinematicsOutputTable kot = new KinematicsOutputTable(kotm);
        JScrollPane kotsp = new JScrollPane(kot);
        kotsp.setColumnHeaderView(kot.getTableHeader());
        kotsp.setOpaque(true);
        pcenter.add(kotsp, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    private void setupMenu() {
        createInstructDialog();
        createLicenseDialog();

        JMenuBar mbar = new JMenuBar();

        JMenu file = new JMenu("File");
        mbar.add(file);

        JMenuItem exit = new JMenuItem("Exit");
        file.add(new FilePrintAction())
            .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Event.CTRL_MASK));
        file.add(new TextExportAction())
            .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Event.CTRL_MASK));
        file.add(exit);
        exit.setActionCommand("exit");
        exit.addActionListener(this);

        JMenu pref = new JMenu("Preferences");
        JRadioButtonMenuItem m1995 = new JRadioButtonMenuItem("1995 Mass Table");
        m1995.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        Nucleus.setMassTable(TableText.TABLE_1995);
                        rt.setValueAt(rt.getValueAt(1, 1), 1, 1);
                    }
                }
            });

        JRadioButtonMenuItem m2003 = new JRadioButtonMenuItem("2003 Mass Table");
        m2003.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    if (ie.getStateChange() == ItemEvent.SELECTED) {
                        Nucleus.setMassTable(TableText.TABLE_2003);
                        rt.setValueAt(rt.getValueAt(1, 1), 1, 1);
                    }
                }
            });

        ButtonGroup choice = new ButtonGroup();
        choice.add(m1995);
        choice.add(m2003);
        m2003.setSelected(true);
        pref.add(m1995);
        pref.add(m2003);
        mbar.add(pref);

        JMenu help = new JMenu("Help");
        mbar.add(help);

        JMenuItem instruct = new JMenuItem("Instructions...");
        JMenuItem license = new JMenuItem("License...");
        help.add(instruct);
        help.add(license);
        license.setActionCommand("license");
        license.addActionListener(this);
        instruct.setActionCommand("instruct");
        instruct.addActionListener(this);
        setJMenuBar(mbar);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private JPanel getChoicePanel() {
        JPanel jp = new JPanel(new GridLayout(1, 4, 5, 5));
        jp.setOpaque(true);
        jp.add(be);
        jp.add(ex4);
        jp.add(la3);

        JPanel ptt = new JPanel(new GridLayout(2, 1, 5, 5));
        ptt.add(new JLabel("Target Thickness [" + units + "]"));
        tt.addActionListener(this);
        ptt.add(tt);
        jp.add(ptt);

        return jp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getTargetThickness() {
        return Double.parseDouble(tt.getText().trim());
    }

    /**
     * DOCUMENT ME!
     *
     * @param vc DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean receiveValues(ValuesChooser vc, double[] values) {
        boolean rval = true;

        try {
            if (vc == be) {
                kotm.setBeamEnergies(values);
            } else if (vc == ex4) {
                kotm.setResidualExcitations(values);
            } else if (vc == la3) {
                kotm.setLabAngles(values);
            }
        } catch (KinematicsException ke) {
            System.err.println(ke);
            JOptionPane.showMessageDialog(this, ke.getMessage(),
                "Kinematics Error", JOptionPane.WARNING_MESSAGE);
            rval = false;
        } catch (NuclearException ke) {
            System.err.println(ke);
            JOptionPane.showMessageDialog(this, ke.getMessage(),
                "Nuclear Error", JOptionPane.WARNING_MESSAGE);
            rval = false;
        }

        repaint();

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e);
        }

        new JRelKin();
    }

    /**
     * DOCUMENT ME!
     */
    private void createLicenseDialog() {
        licenseD = new JDialog(this,
                "University of Illinois/NCSA Open Source License", false);

        Container contents = licenseD.getContentPane();
        licenseD.setForeground(Color.black);
        licenseD.setBackground(Color.lightGray);
        licenseD.setResizable(false);
        licenseD.setLocation(20, 50);
        contents.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(0, 1));
        InputStream license_in = getClass().getClassLoader()
                                     .getResourceAsStream("license.txt");
        Reader reader = new InputStreamReader(license_in);
        String text = "";
        int length = 0;
        char[] textarray = new char[2000];

        try {
            length = reader.read(textarray);
        } catch (IOException e) {
            System.err.println(e);
        }

        text = new String(textarray, 0, length);
        System.out.println(text);

        JTextArea textarea = new JTextArea(text);
        center.add(new JScrollPane(textarea));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 0));
        contents.add(south, BorderLayout.SOUTH);

        JButton bok = new JButton("OK");
        bok.setActionCommand("l_ok");
        bok.addActionListener(this);
        south.add(bok);
        licenseD.pack();
        //Recieves events for closing the dialog box and closes it.
        licenseD.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    licenseD.dispose();
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    private void createInstructDialog() {
        instructD = new JDialog(this, "Instructions", false);

        Container contents = instructD.getContentPane();
        instructD.setForeground(Color.black);
        instructD.setBackground(Color.lightGray);
        instructD.setResizable(false);
        instructD.setLocation(20, 50);
        contents.setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(0, 1));
        JTextArea textarea = new JTextArea(INTRO);
        center.add(new JScrollPane(textarea));
        contents.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 0));
        contents.add(south, BorderLayout.SOUTH);

        JButton bok = new JButton("OK");
        bok.setActionCommand("i_ok");
        bok.addActionListener(this);
        south.add(bok);
        instructD.pack();
        //Recieves events for closing the dialog box and closes it.
        instructD.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    instructD.dispose();
                }
            });
    }

    /**
     * Show Jam's open source license text.
     */
    public void showLicense() {
        licenseD.show();
    }

    /**
     * Only actions come from target thickness textfield.
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(final java.awt.event.ActionEvent ae) {
        String command = ae.getActionCommand();

        if (command.equals("license")) {
            licenseD.show();
        } else if (command.equals("instruct")) {
            instructD.show();
        } else if (command.equals("i_ok")) {
            instructD.dispose();
        } else if (command.equals("l_ok")) {
            licenseD.dispose();
        } else if (command.equals("exit")) {
            System.exit(0);
        } else {
            double thickness = Double.parseDouble(tt.getText().trim());

            try {
                kotm.setTargetThickness(thickness);
            } catch (KinematicsException ke) {
                System.err.println(ke);
                JOptionPane.showMessageDialog(this, ke.getMessage(),
                    "Kinematics Error", JOptionPane.WARNING_MESSAGE);
            } catch (NuclearException ke) {
                System.err.println(ke);
                JOptionPane.showMessageDialog(this, ke.getMessage(),
                    "Nuclear Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    public class FilePrintAction extends AbstractAction {
/**
         * Creates new FilePrintAction
         */
        public FilePrintAction() {
            super("Print");
        }

        /**
         * DOCUMENT ME!
         *
         * @param actionEvent DOCUMENT ME!
         */
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            ComponentPrintable cp = new ComponentPrintable(pane);
            pj.setPrintable(cp, mPageFormat);

            if (pj.printDialog()) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.err.println(e);
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
    public class TextExportAction extends AbstractAction {
        /**
         * DOCUMENT ME!
         */
        File lastFile;

/**
         * Creates new FilePrintAction
         */
        public TextExportAction() {
            super("Export Text");
        }

        /**
         * DOCUMENT ME!
         *
         * @param actionEvent DOCUMENT ME!
         */
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            JFileChooser jfile;
            File file;

            if (lastFile == null) {
                jfile = new JFileChooser();
            } else {
                jfile = new JFileChooser(lastFile);
            }

            int option = jfile.showSaveDialog(pane);

            if ((option == JFileChooser.APPROVE_OPTION) &&
                    (jfile.getSelectedFile() != null)) {
                lastFile = jfile.getSelectedFile();
                file = lastFile;
            } else {
                file = null;
            }

            if (file != null) {
                exportResults(file);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param file DOCUMENT ME!
         */
        void exportResults(File file) {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(rt.getTarget() + "(" + rt.getBeam() + "," +
                    rt.getProjectile() + ")" + rt.getResidual() + "\n");
                fw.write("Q0 = " + rt.getQ0() + " MeV\n");

                if (getTargetThickness() > 0.0) {
                    fw.write("Assumed " + getTargetThickness() +
                        " ug/cm^2 of pure " + rt.getTarget() + ".\n");
                } else {
                    fw.write("Assumed zero target thickness.\n");
                }

                fw.write("--\n");

                int rows = kotm.getRowCount();
                int cols = kotm.getColumnCount();

                for (int i = 0; i < cols; i++) {
                    fw.write(kotm.getColumnName(i));

                    if (i < (cols - 1)) {
                        fw.write("\t");
                    }
                }

                fw.write("\n");

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        fw.write(kotm.getValueAt(i, j).toString());

                        if (j < (cols - 1)) {
                            fw.write("\t");
                        }
                    }

                    fw.write("\n");
                }

                fw.flush();
                fw.close();
            } catch (IOException ioe) {
                System.err.println(ioe);
                JOptionPane.showMessageDialog(pane, ioe.getMessage(),
                    "Kinematics Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
