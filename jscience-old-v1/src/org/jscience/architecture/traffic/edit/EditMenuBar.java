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

package org.jscience.architecture.traffic.edit;

import org.jscience.architecture.traffic.FileMenu;
import org.jscience.architecture.traffic.HelpMenu;

import java.awt.*;
import java.awt.event.*;


/**
 * The MenuBar for the editor
 *
 * @author Group GUI
 * @version 1.0
 */
public class EditMenuBar extends MenuBar {
    /** DOCUMENT ME! */
    EditController controller;

/**
     * Creates a new EditMenuBar object.
     *
     * @param ec DOCUMENT ME!
     */
    public EditMenuBar(EditController ec) {
        controller = ec;

        Menu menu;
        MenuItem item;

        add(new FileMenu(controller, true));

        menu = new Menu("Edit");
        add(menu);

        EditMenuListener eml = new EditMenuListener();

        item = new MenuItem("Delete", new MenuShortcut(KeyEvent.VK_DELETE));
        menu.add(item);
        item.addActionListener(eml);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Select all", new MenuShortcut(KeyEvent.VK_A));
        menu.add(item);
        item.addActionListener(eml);

        item = new MenuItem("Deselect", new MenuShortcut(KeyEvent.VK_D));
        menu.add(item);
        item.addActionListener(eml);

        menu = new Menu("Options");
        add(menu);

        OptionMenuListener oml = new OptionMenuListener();

        CheckboxMenuItem citem = new CheckboxMenuItem("Toggle grid", false);
        menu.add(citem);
        citem.addItemListener(oml);

        item = new MenuItem("Change size...");
        menu.add(item);
        item.addActionListener(oml);

        menu.add(new MenuItem("-"));

        item = new MenuItem("Validate");
        menu.add(item);
        item.addActionListener(oml);

        item = new MenuItem("Settings...");
        menu.add(item);
        item.addActionListener(oml);

        add(new HelpMenu(controller));
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.2 $
     */
    private class EditMenuListener implements ActionListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = ((MenuItem) e.getSource()).getLabel();

            if (sel.equals("Delete")) {
                controller.deleteSelection();
            } else if (sel.equals("Select all")) {
                controller.selectAll();
            } else if (sel.equals("Deselect")) {
                controller.deselectAll();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.2 $
     */
    private class OptionMenuListener implements ActionListener, ItemListener {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void actionPerformed(ActionEvent e) {
            String sel = ((MenuItem) e.getSource()).getLabel();

            if (sel == "Validate") {
                controller.validateInfra();
            } else if (sel == "Change size...") {
                controller.showChangeSizeDialog();
            } else if (sel == "Settings...") {
                ;
            }

            controller.showSettings();
        }

        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                controller.enableGrid();
            } else {
                controller.disableGrid();
            }
        }
    }
}
