/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.Stopwatch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


/**
 * A little GUI app with a JWatchLabel that can be run, halted and set. It
 * doesn't really have any alarm clock functionality, yet.
 *
 * @author Holger Antelmann
 *
 * @see JWatchLabel
 * @see org.jscience.util.Stopwatch
 */
public class JAlarmClock extends JMainFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6160891457957795647L;

    /** DOCUMENT ME! */
    JWatchLabel jwatch;

/**
     * Creates a new JAlarmClock object.
     */
    public JAlarmClock() {
        super("JAlarmClock", false, false);
        setJMenuBar(getMenu());
        getContentPane().add(getToolBar(), BorderLayout.NORTH);
        jwatch = new JWatchLabel(new Stopwatch(true));
        getContentPane().add(jwatch);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Stopwatch getStopwatch() {
        return jwatch.getStopwatch();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar getMenu() {
        JMenuBar menu = new JMenuBar();
        JMenuItem item;
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        {
            item = new JMenuItem("show exact snapshot");
            item.setMnemonic(KeyEvent.VK_E);

            final JAlarmClock me = this;
            item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(me,
                            jwatch.getStopwatch().elapsedAsString(),
                            "exact time", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            file.add(item);
            item = new JMenuItem("set");
            item.setMnemonic(KeyEvent.VK_S);
            item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String s = JOptionPane.showInputDialog(me,
                                "enter seconds:", null);
                        int n = 0;

                        try {
                            n = Integer.parseInt(s);
                        } catch (NumberFormatException ex) {
                            return;
                        }

                        jwatch.getStopwatch().reset(n * 1000);
                    }
                });
            file.add(item);
            item = new JMenuItem("reset");
            item.setMnemonic(KeyEvent.VK_R);
            item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jwatch.getStopwatch().reset();
                    }
                });
            file.add(item);
            item = new JMenuItem("Exit");
            item.setMnemonic(KeyEvent.VK_X);
            item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //dispose();
                        System.exit(0);
                    }
                });
            file.add(item);
        }

        menu.add(file);

        JMenu window = new JMenu("window");
        window.setMnemonic(KeyEvent.VK_W);
        window.add(Menus.createLookAndFeelMenu(this));
        menu.add(window);

        return menu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JToolBar getToolBar() {
        JToolBar tbar = new JToolBar();
        JButton button = new JButton("run");
        button.setMnemonic(KeyEvent.VK_R);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jwatch.getStopwatch().resume();
                }
            });
        tbar.add(button);
        button = new JButton("halt");
        button.setMnemonic(KeyEvent.VK_H);
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jwatch.getStopwatch().pause();
                }
            });
        tbar.add(button);

        return tbar;
    }

    /**
     * just starts a JAlarmClock
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new JAlarmClock().setVisible(true);
    }
}
