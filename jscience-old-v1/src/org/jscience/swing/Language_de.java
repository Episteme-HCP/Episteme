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
 * Deutsche Uebersetzung fuer die <dfn>org.jscience.swing</dfn> Package
 *
 * @author Holger Antelmann
 *
 * @see Language
 */
public class Language_de extends ListResourceBundle {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object[][] getContents() {
        return new Object[][] {
            { "about", "�ber diese Andwendung" },
            { "bold", "fett" },
            { "button_cancel", "Abbrechen" },
            { "button_close key", new Integer(KeyEvent.VK_S) },
            { "button_close", "Schliessen" },
            { "button_ok", "OK" },
            { "change_look", "�ndern der Optik" },
            { "compare", "Vergleichen" },
            {
                "connect_failure",
                "Die Webseite f�r das Update konnte nicht erreicht werden"
            },
            { "connect_failure_title", "Verbindungsfehler" },
            { "direction", "Richtung" },
            { "down", "runter" },
            { "ignoreCase", "gro�/klein ignorieren" },
            { "italic", "kursiv" },
            { "license", "Lizenz" },
            { "menu_about", "�ber" },
            { "menu_file key", new Integer(KeyEvent.VK_D) },
            { "menu_file", "Datei" },
            { "menu_file_close key", new Integer(KeyEvent.VK_S) },
            { "menu_file_close", "Schliessen" },
            { "menu_file_exit key", new Integer(KeyEvent.VK_B) },
            { "menu_file_exit", "Beenden" },
            { "menu_file_load key", new Integer(KeyEvent.VK_L) },
            { "menu_file_load", "Lade Datei" },
            { "menu_help key", new Integer(KeyEvent.VK_H) },
            { "menu_help", "Hilfe" },
            { "menu_window key", new Integer(KeyEvent.VK_F) },
            { "menu_window", "Fenster" },
            { "noMatchFound", "keine �bereinstimmung gefunden" },
            { "properties", "Eigenschaften" },
            { "replace", "Ersetzen" },
            { "replaceAll", "Alles Ersetzen" },
            { "replaceWith", "ersetzen mit" },
            { "reset", "zur�cksetzen" },
            { "search key", new Integer(KeyEvent.VK_S) },
            { "search", "Suchen" },
            { "searchAndReplace", "Suchen & Ersetzen" },
            { "searchFor", "suchen nach" },
            { "selectFont", "Schriftart ausw�hlen" },
            { "size", "Gr��e" },
            { "up", "hoch" },
            {
                "upgrade_available_later_from",
                "Sie k�nnen das Update sp�ter herunterladen unter: "
            },
            {
                "upgrade_available_question",
                "Ein Update ist verf�gbar; wollen Sie die neue Datei herunterladen?"
            },
            { "upgrade_available_title", "Udate verf�gbar" },
            { "upgrade_check", "nach Upate �berpr�fen" },
            { "upgrade_not", "Sie haben die aktuellste verf�gbare Version" },
            { "upgrade_not_title", "kein Update n�tig" },
            { "version", "Version" },
            // JTextViewer
            {"textViewer", "Text Anzeiger" },
            { "save to file", "in Datei sichern" },
            { "keySave", new Integer(KeyEvent.VK_S) },
            { "file exists; overwrite?", "Datei existiert; �berschreiben?" },
            { "overwrite warning", "�berschreibungswarnung" },
            { "copyAll", "alles kopieren" },
            { "keyCopy", new Integer(KeyEvent.VK_K) },
            { "print", "Drucken" },
            { "keyPrint", new Integer(KeyEvent.VK_D) },
            { "editable", "editierbar" },
            { "keyEditable", new Integer(KeyEvent.VK_E) },
            { "selectPrinter", "Drucker w�hlen" },
            { "search", "Suchen" },
            { "keySearch", new Integer(KeyEvent.VK_F) },
            { "password required", "Passwort ben�tigt" },
            { "enter password", "Passwort-Eingabe" },
        };
    }
}
