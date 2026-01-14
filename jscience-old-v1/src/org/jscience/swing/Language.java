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

import java.awt.event.KeyEvent;

import java.util.ListResourceBundle;


/**
 * English language resources for the <dfn>org.jscience.swing</dfn> package
 *
 * @author Holger Antelmann
 */
public class Language extends ListResourceBundle {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object[][] getContents() {
        return new Object[][] {
            { "about", "About this application" },
            { "bold", "bold" },
            { "button_cancel", "Cancel" },
            { "button_close key", new Integer(KeyEvent.VK_C) },
            { "button_close", "Close" },
            { "button_ok", "OK" },
            { "change_look", "Change Look&Feel" },
            { "compare", "compare" },
            { "connect_failure", "could not connect to website for upgrade check" },
            { "connect_failure_title", "Connection failure" },
            { "direction", "direction" },
            { "down", "down" },
            { "ignoreCase", "ignore case" },
            { "italic", "italic" },
            { "license", "License" },
            { "menu_about", "About" },
            { "menu_file key", new Integer(KeyEvent.VK_F) },
            { "menu_file", "File" },
            { "menu_file_close key", new Integer(KeyEvent.VK_C) },
            { "menu_file_close", "Close" },
            { "menu_file_exit key", new Integer(KeyEvent.VK_X) },
            { "menu_file_exit", "Exit" },
            { "menu_file_load key", new Integer(KeyEvent.VK_L) },
            { "menu_file_load", "Load File" },
            { "menu_help key", new Integer(KeyEvent.VK_H) },
            { "menu_help", "Help" },
            { "menu_window key", new Integer(KeyEvent.VK_W) },
            { "menu_window", "Window" },
            { "noMatchFound", "no match found" },
            { "properties", "properties" },
            { "replace", "replace" },
            { "replaceAll", "replace all" },
            { "replaceWith", "replace with" },
            { "reset", "reset" },
            { "search key", new Integer(KeyEvent.VK_S) },
            { "search", "search" },
            { "searchAndReplace", "search & replace" },
            { "searchFor", "search for" },
            { "selectFont", "select font" },
            { "size", "size" },
            { "up", "up" },
            {
                "upgrade_available_later_from",
                "You can download the new version later from: "
            },
            {
                "upgrade_available_question",
                "An upgrade is available; would you like to download the new jar file now?"
            },
            { "upgrade_available_title", "upgrade available" },
            { "upgrade_check", "check for upgrade" },
            { "upgrade_not", "you have the latest available version" },
            { "upgrade_not_title", "no upgrade needed" },
            { "version", "Version" },
            // JTextViewer
            {"textViewer", "Text Viewer" },
            { "save to file", "save to file" },
            { "keySave", new Integer(KeyEvent.VK_S) },
            { "file exists; overwrite?", "file exists; overwrite?" },
            { "overwrite warning", "overwrite warning" },
            { "copyAll", "Copy all" },
            { "keyCopy", new Integer(KeyEvent.VK_C) },
            { "print", "Print" },
            { "keyPrint", new Integer(KeyEvent.VK_P) },
            { "editable", "Editable" },
            { "keyEditable", new Integer(KeyEvent.VK_E) },
            { "selectPrinter", "select printer" },
            { "search", "search" },
            { "keySearch", new Integer(KeyEvent.VK_F) },
            { "password required", "password required" },
            { "enter password", "enter password" },
        };
    }
}
