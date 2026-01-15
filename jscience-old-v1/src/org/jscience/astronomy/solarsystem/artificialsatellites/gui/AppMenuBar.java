/*
 * Copyright (C) 2006 Matthew Funk
 * Licensed under the Academic Free License version 1.2
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Academic Free License version 1.2 for more details.
 */
package org.jscience.astronomy.solarsystem.artificialsatellites.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
final class AppMenuBar extends JMenuBar {
    /**
     * Creates a new AppMenuBar object.
     *
     * @param parentFrame DOCUMENT ME!
     */
    AppMenuBar(final JFrame parentFrame) {
        JMenu fileMenu = new JMenu("File");
        add(fileMenu);

        JMenuItem saveMenuItem = new JMenuItem("Save Output");
        saveMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            });
        fileMenu.add(saveMenuItem);

        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    parentFrame.setVisible(false);
                    parentFrame.dispose();
                    System.exit(0);
                }
            });
        fileMenu.add(exitMenuItem);
    }
}
