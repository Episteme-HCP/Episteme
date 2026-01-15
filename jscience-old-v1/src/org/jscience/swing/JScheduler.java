/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.logging.JTextComponentLog;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.TimerTask;

import javax.swing.*;


/**
 * a convenient GUI to schedule predefined tasks
 *
 * @author Holger Antelmann
 */
public class JScheduler extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2862786793822379542L;

    /** DOCUMENT ME! */
    final static String[] intervalType = new String[] {
            "seconds", "minutes", "hours", "days", "weeks"
        };

    /** DOCUMENT ME! */
    Logger logger;

    /** DOCUMENT ME! */
    java.util.Timer timer;

    /** DOCUMENT ME! */
    Component description;

    /** DOCUMENT ME! */
    JCheckBox fixedRate;

    /** DOCUMENT ME! */
    JComboBox intervalList;

    /** DOCUMENT ME! */
    JSpinner interval;

    /** DOCUMENT ME! */
    JToggleButton activeButton;

    /** DOCUMENT ME! */
    JTextArea text;

/**
     * Creates a new JScheduler object.
     *
     * @param task        DOCUMENT ME!
     * @param description DOCUMENT ME!
     */
    public JScheduler(final Runnable task, String description) {
        this(task, new JLabel(description));
    }

/**
     * The scheduler uses a BoxLayout where the description is put on top.
     *
     * @param task        DOCUMENT ME!
     * @param description DOCUMENT ME!
     */
    public JScheduler(final Runnable task, Component description) {
        super("Scheduler", true, true);
        this.description = description;
        addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent we) {
                    if (timer != null) {
                        timer.cancel();
                    }
                }
            });
        text = new JTextArea(6, 30);
        getContentPane().add(new JScrollPane(text), BorderLayout.CENTER);
        logger = new Logger();

        final JTextComponentLog jlog = new JTextComponentLog(text);
        jlog.setLimit(70000);
        logger.addWriter(jlog);

        JPanel controls = new JPanel();
        getContentPane().add(controls, BorderLayout.NORTH);
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(description);
        activeButton = new JToggleButton("activate");
        activeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    boolean pressed = activeButton.isSelected();
                    fixedRate.setEnabled(!pressed);
                    intervalList.setEnabled(!pressed);
                    interval.setEnabled(!pressed);
                    text.setEditable(!pressed);

                    if (pressed) {
                        JTimeChooser jtc = new JTimeChooser();
                        int a = JOptionPane.showConfirmDialog(JScheduler.this,
                                jtc, "first start time",
                                JOptionPane.OK_CANCEL_OPTION);

                        if (a != JOptionPane.OK_OPTION) {
                            activeButton.setSelected(false);
                            fixedRate.setEnabled(true);
                            intervalList.setEnabled(true);
                            interval.setEnabled(true);
                            text.setEditable(true);

                            return;
                        }

                        activeButton.setText("deactivate");
                        jlog.purge();
                        timer = new java.util.Timer("JScheduler", true);

                        TimerTask timerTask = new TimerTask() {
                                public void run() {
                                    logger.log(JScheduler.this,
                                        "begin scheduled task");

                                    try {
                                        task.run();
                                    } catch (Throwable t) {
                                        logger.log(JScheduler.this, t);
                                    }

                                    logger.log(JScheduler.this,
                                        "end scheduled task");
                                }
                            };

                        long period = ((Integer) interval.getValue()).intValue();
                        String t = intervalList.getSelectedItem().toString();

                        if (t.equals("seconds")) {
                            period = period * 1000;
                        } else if (t.equals("minutes")) {
                            period = period * 1000 * 60;
                        } else if (t.equals("hours")) {
                            period = period * 1000 * 60 * 60;
                        } else if (t.equals("days")) {
                            period = period * 1000 * 60 * 60 * 24;
                        } else if (t.equals("weeks")) {
                            period = period * 1000 * 60 * 60 * 24 * 7;
                        }

                        if (logger != null) {
                            logger.log(JScheduler.this, Level.CONFIG,
                                "first execution scheduled for: " +
                                jtc.getTime());
                        }

                        if (fixedRate.isSelected()) {
                            timer.scheduleAtFixedRate(timerTask, jtc.getTime(),
                                period);
                        } else {
                            timer.schedule(timerTask, jtc.getTime(), period);
                        }

                        updateStatusText("timer scheduled");
                    } else {
                        activeButton.setText("activate");
                        updateStatusText("timer canceled");
                        timer.cancel();
                    }
                }
            });

        //activeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(activeButton);
        intervalList = new JComboBox(intervalType);
        interval = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE,
                    1));

        JLabel rlabel = new JLabel("repeat interval:");
        rlabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(rlabel);

        JPanel repeatPanel = new JPanel();
        repeatPanel.setLayout(new BoxLayout(repeatPanel, BoxLayout.X_AXIS));
        repeatPanel.add(interval);
        repeatPanel.add(intervalList);
        repeatPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(repeatPanel);
        fixedRate = new JCheckBox("schedule at fixed rate");
        fixedRate.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(fixedRate);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getDescription() {
        return description;
    }

    /**
     * contains a JTextComponentLog by default
     *
     * @return DOCUMENT ME!
     */
    public Logger getLogger() {
        return logger;
    }
}
