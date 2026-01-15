/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
