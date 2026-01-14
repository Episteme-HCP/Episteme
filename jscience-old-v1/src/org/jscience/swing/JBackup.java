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

package org.jscience.swing;


//import org.jscience.sound.SoundPlayer;
import org.jscience.util.Backup;
import org.jscience.util.Debug;
import org.jscience.util.MonitorDelegator;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.LevelFilter;
import org.jscience.util.logging.LogFile;
import org.jscience.util.logging.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import javax.swing.*;


/**
 * a gui utility to run and schedule backups
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.util.Backup
 * @see JScheduler
 */
public class JBackup extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -8531512629106917751L;

    /** DOCUMENT ME! */
    JFileField origFileField;

    /** DOCUMENT ME! */
    JFileField destFileField;

    /** DOCUMENT ME! */
    JFileField logFileField;

    /** DOCUMENT ME! */
    JCheckBox deleteCheckBox;

    /** DOCUMENT ME! */
    JButton scheduleButton;

    /** DOCUMENT ME! */
    JButton startButton;

/**
     * Creates a new JBackup object.
     *
     * @param originDir      DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     * @param deleteAtTarget DOCUMENT ME!
     */
    public JBackup(File originDir, File destinationDir, boolean deleteAtTarget) {
        this(originDir, destinationDir, deleteAtTarget, null);
    }

/**
     * Creates a new JBackup object.
     *
     * @param originDir      DOCUMENT ME!
     * @param destinationDir DOCUMENT ME!
     * @param deleteAtTarget DOCUMENT ME!
     * @param logFile        DOCUMENT ME!
     */
    public JBackup(File originDir, File destinationDir, boolean deleteAtTarget,
        File logFile) {
        super("Backup Tool", true, false);
        origFileField = new JFileField(originDir);
        destFileField = new JFileField(destinationDir);
        origFileField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        destFileField.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        logFileField = new JFileField(logFile, 20);
        deleteCheckBox = new JCheckBox("delete files at target", deleteAtTarget);

        JPanel fieldPanel = new JPanel(new SpringLayout());
        fieldPanel.add(new JLabel("origin: "));
        fieldPanel.add(origFileField);
        fieldPanel.add(new JLabel("target: "));
        fieldPanel.add(destFileField);
        fieldPanel.add(new JLabel("logFile: "));
        fieldPanel.add(logFileField);
        fieldPanel.add(new JLabel(""));
        fieldPanel.add(deleteCheckBox);
        Menus.makeCompactSpringGrid(fieldPanel, 4, 2, 1, 1, 2, 2);

        JPanel main = new JPanel(new BorderLayout());
        main.add(fieldPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        startButton = new JButton("start backup now");
        startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        int a = JOptionPane.showConfirmDialog(JBackup.this,
                                "start backup now?");

                        if (a != JOptionPane.OK_OPTION) {
                            return;
                        }

                        final Backup backup = makeBackup();
                        final MonitorDelegator mon = new MonitorDelegator(new ProgressMonitor(
                                    JBackup.this, makeComponent(backup),
                                    "backing up ..", 0, 1));
                        mon.getProgressMonitor().setMillisToPopup(0);
                        mon.getProgressMonitor().setMillisToDecideToPopup(0);
                        backup.setMonitor(mon);

                        final Thread t = new Thread(new Runnable() {
                                    public void run() {
                                        backup.run();
                                        setControls(true);

                                        //SoundPlayer.beep();
                                        if (mon.enabled()) {
                                            JOptionPane.showMessageDialog(JBackup.this,
                                                "done");
                                        } else {
                                            JOptionPane.showMessageDialog(JBackup.this,
                                                "aborted");
                                        }
                                    }
                                });
                        t.setDaemon(true);
                        t.start();
                        setControls(false);
                    } catch (Exception ex) {
                        Menus.showExceptionDialog(JBackup.this, ex);
                    }
                }
            });
        buttonPanel.add(startButton);
        scheduleButton = new JButton("schedule backup");
        scheduleButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        final Backup backup = makeBackup();
                        Runnable task = new Runnable() {
                                public void run() {
                                    MonitorDelegator mon = new MonitorDelegator(new ProgressMonitor(
                                                JBackup.this,
                                                makeComponent(backup),
                                                "backing up ..", 0, 1));
                                    mon.getProgressMonitor().setMillisToPopup(0);
                                    mon.getProgressMonitor()
                                       .setMillisToDecideToPopup(0);
                                    backup.setMonitor(mon);
                                    backup.run();
                                }
                            };

                        new JScheduler(task, makeComponent(backup)).setVisible(true);
                    } catch (Exception ex) {
                        Menus.showExceptionDialog(JBackup.this, ex);
                    }
                }
            });
        buttonPanel.add(scheduleButton);
        main.add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(main);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    private void setControls(final boolean flag) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    origFileField.setEnabled(flag);
                    destFileField.setEnabled(flag);
                    logFileField.setEnabled(flag);
                    deleteCheckBox.setEnabled(flag);
                    scheduleButton.setEnabled(flag);
                    startButton.setEnabled(flag);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param backup DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Component makeComponent(Backup backup) {
        StringBuilder buffer = new StringBuilder("<html>Backup Task");
        buffer.append("<br>origin: " + backup.getOriginDir());
        buffer.append("<br>target: " + backup.getDestinationDir());

        if (backup.getDeleteAtTarget()) {
            buffer.append("<br>target files will be deleted");
        }

        File logFile = logFileField.getFile();

        if (logFile != null) {
            buffer.append("<br>logFile: " + logFile);
        }

        buffer.append("<br>&nbsp;");

        return new JLabel(buffer.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private Backup makeBackup() throws IOException {
        Logger logger = null;
        Backup backup = new Backup(origFileField.getFile(),
                destFileField.getFile(), null, deleteCheckBox.isSelected());
        File logFile = logFileField.getFile();

        if (logFile != null) {
            logger = new Logger(new LogFile(logFile));
            logger.setFilter(new LevelFilter(Level.FINE));
            backup.setLogger(logger);
        }

        return backup;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Debug.dialogExceptionHandler);
        SplashScreen.splash().disposeAfter(2000);

        File origin = null;
        File target = null;
        File logFile = null;

        if (args.length > 0) {
            origin = new File(args[0]);
        }

        if (args.length > 1) {
            target = new File(args[1]);
        }

        if (args.length > 2) {
            logFile = new File(args[2]);
        }

        final File o = origin;
        final File t = target;
        final File l = logFile;
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JBackup jb = new JBackup(o, t, false, l);
                    jb.setDefaultCloseOperation(JBackup.EXIT_ON_CLOSE);
                    jb.setVisible(true);
                }
            });
    }
}
