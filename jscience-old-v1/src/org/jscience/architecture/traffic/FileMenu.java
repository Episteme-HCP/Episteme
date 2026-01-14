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
 * The FileMenu. Used by both editor and simulator
 *
 * @author Group GUI
 * @version 1.0
 */
public class FileMenu extends Menu implements ActionListener {
    /** DOCUMENT ME! */
    Controller controller;

/**
     * Creates a new FileMenu object.
     *
     * @param c       DOCUMENT ME!
     * @param newmenu DOCUMENT ME!
     */
    public FileMenu(Controller c, boolean newmenu) {
        super("File");
        controller = c;

        MenuItem item;

        if (newmenu) {
            item = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N));
            add(item);
            item.addActionListener(this);
        }

        item = new MenuItem("Open...", new MenuShortcut(KeyEvent.VK_O));
        add(item);
        item.addActionListener(this);

        item = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
        add(item);
        item.addActionListener(this);

        item = new MenuItem("Save as...");
        add(item);
        item.addActionListener(this);

        addSeparator();

        item = new MenuItem("Properties...");
        add(item);
        item.addActionListener(this);

        addSeparator();

        item = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q));
        add(item);
        item.addActionListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if (s.equals("New")) {
            controller.newFile();
        } else if (s.equals("Open...")) {
            controller.openFile();
        } else if (s.equals("Save")) {
            controller.saveFile();
        } else if (s.equals("Save as...")) {
            controller.saveFileAs();
        } else if (s.equals("Properties...")) {
            controller.showFilePropertiesDialog();
        } else if (s.equals("Quit")) {
            controller.quit();
        }
    }
}
