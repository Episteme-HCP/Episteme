/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.io.ExtendedFile;

import org.jscience.util.Monitor;
import org.jscience.util.Settings;
import org.jscience.util.Stopwatch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class JCompare extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6857130071002561511L;

    /** DOCUMENT ME! */
    final JFileField fileField1;

    /** DOCUMENT ME! */
    final JFileField fileField2;

    /** DOCUMENT ME! */
    JTextArea ta;

    /** DOCUMENT ME! */
    Monitor monitor;

    /** DOCUMENT ME! */
    JButton stopButton;

/**
     * Creates a new JCompare object.
     */
    public JCompare() {
        this(null, null);
    }

/**
     * Creates a new JCompare object.
     *
     * @param file1 DOCUMENT ME!
     * @param file2 DOCUMENT ME!
     */
    public JCompare(File file1, File file2) {
        super("JCompare", true, false);
        monitor = new Monitor();
        fileField1 = new JFileField(file1, 40);
        fileField2 = new JFileField(file2, 40);
        fileField1.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileField2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        ta = new JTextArea(20, 40);

        JButton compareButton = new JButton("compare");
        compareButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    ta.append("\n----------------------------------------\n");

                    Thread t = new Thread(new Runnable() {
                                public void run() {
                                    monitor.enable();

                                    try {
                                        compare(new ExtendedFile(
                                                fileField1.getFile()),
                                            new ExtendedFile(
                                                fileField2.getFile()));
                                    } catch (Exception ex) {
                                        //ex.printStackTrace();
                                        ta.append("\nexception caught: " +
                                            ex.getMessage());
                                        JOptionPane.showMessageDialog(JCompare.this,
                                            ex.getMessage(),
                                            ex.getClass().getName(),
                                            JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                    t.start();
                    ta.setCaretPosition(ta.getText().length());
                }
            });

        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    ta.setText(null);
                }
            });
        stopButton = new JButton(new ImageIcon(Settings.getResource(
                        "org/jscience/awt/icons/general/Stop16.gif")));
        stopButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    stopButton.setEnabled(false);
                    monitor.disable();
                }
            });
        stopButton.setEnabled(false);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(fileField1);
        top.add(fileField2);

        JPanel buttons = new JPanel();
        buttons.add(compareButton);
        buttons.add(clearButton);
        buttons.add(stopButton);
        top.add(buttons);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(ta), BorderLayout.CENTER);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param file1 DOCUMENT ME!
     * @param file2 DOCUMENT ME!
     * @param beginIndex DOCUMENT ME!
     * @param maxBytes DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void fileCompare(ExtendedFile file1, ExtendedFile file2, long beginIndex,
        long maxBytes) throws IOException {
        ta.append(file1.fileCompareText(beginIndex, file2, beginIndex, maxBytes));
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir1 DOCUMENT ME!
     * @param dir2 DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void dirCompare(ExtendedFile dir1, ExtendedFile dir2)
        throws IOException {
        OutputStream out = new OutputStream() {
                public void write(int b) {
                    ta.append(new String(new byte[] { (byte) b }));
                }
            };

        PrintStream stream = new PrintStream(out);
        stopButton.setEnabled(true);
        dir1.directoryCompare(dir2, stream, monitor);
        stopButton.setEnabled(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file1 DOCUMENT ME!
     * @param file2 DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void compare(ExtendedFile file1, ExtendedFile file2)
        throws IOException {
        Stopwatch sw = new Stopwatch(true);

        if (file1.isDirectory()) {
            if (file2.isDirectory()) {
                dirCompare(file1, file2);
            } else {
                ta.append("cannot compare file with directory\n");
            }
        } else {
            if (file2.isDirectory()) {
                ta.append("cannot compare file with directory\n");
            } else {
                long beginIndex = 0;
                long maxBytes = 0;

                if (!file1.exists()) {
                    ta.append("File " + file1 + " doesn't exist\n");

                    return;
                }

                if (!file2.exists()) {
                    ta.append("File " + file2 + " doesn't exist\n");

                    return;
                }

                fileCompare(file1, file2, beginIndex, maxBytes);
            }
        }

        ta.append("\ndone in " + sw.elapsedAsString() + "\n\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        File file1 = null;
        File file2 = null;

        if (args.length > 0) {
            file1 = new File(args[0]);
        }

        if (args.length > 1) {
            file2 = new File(args[1]);
        }

        new JCompare(file1, file2).setVisible(true);
    }
}
