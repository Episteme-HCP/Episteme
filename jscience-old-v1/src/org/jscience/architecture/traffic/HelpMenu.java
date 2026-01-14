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

package org.jscience.architecture.traffic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


/**
 * The HelpMenu. Used by both editor and simulator
 *
 * @author Group GUI
 * @version 1.0
 */
public class HelpMenu extends Menu implements ActionListener {
    /** DOCUMENT ME! */
    Controller controller;

/**
     * Creates a new HelpMenu object.
     *
     * @param c DOCUMENT ME!
     */
    public HelpMenu(Controller c) {
        super("Help");
        controller = c;

        MenuItem item;

        item = new MenuItem("Help", new MenuShortcut(KeyEvent.VK_H));
        add(item);
        item.addActionListener(this);

        addSeparator();

        item = new MenuItem("Specifications");
        add(item);
        item.addActionListener(this);

        item = new MenuItem("License");
        add(item);
        item.addActionListener(this);

        item = new MenuItem("Website");
        add(item);
        item.addActionListener(this);

        addSeparator();

        item = new MenuItem("About");
        add(item);
        item.addActionListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String sel = e.getActionCommand();

        if (sel.equals("Help")) {
            controller.showHelp(HelpViewer.HELP_INDEX);
        } else if (sel.equals("Specifications")) {
            controller.showHelp(HelpViewer.HELP_SPECS);
        } else if (sel.equals("Website")) {
            controller.showHelp(HelpViewer.HELP_WEBSITE);
        } else if (sel.equals("License")) {
            controller.showHelp(HelpViewer.HELP_LICENSE);
        } else if (sel.equals("About")) {
            controller.showHelp(HelpViewer.HELP_ABOUT);
        }
    }
}
