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

/*
 * AtlasFileChooser.java
 *
 * This is a wrapper around a JFileCHooser that will return to the last
 * directory- even if the GUI is closed.
 *
 * Created on March 9, 2005, 11:30 PM
 */
package org.jscience.physics.solids.gui;

import java.awt.*;

import java.io.File;

import java.util.prefs.Preferences;

import javax.swing.*;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class AtlasFileChooser {
    /**
     * DOCUMENT ME!
     */
    private JFileChooser fc = new JFileChooser();

    /**
     * DOCUMENT ME!
     */
    private Preferences prefs = Preferences.userRoot();

    /**
     * DOCUMENT ME!
     */
    private String prefName = "AtlasFileChooser";

/**
     * Creates a new instance of AtlasFileChooser
     */
    public AtlasFileChooser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public void setCurrentPath(String path) {
        prefs.put(prefName, path);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCurrentPath() {
        String def = fc.getCurrentDirectory().getAbsolutePath();

        String ret = prefs.get(prefName, def);

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int showOpenDialog(Component parent) {
        //Set to the correct location
        fc.setCurrentDirectory(new File(this.getCurrentPath()));

        int retval = fc.showOpenDialog(parent);

        if (retval == JFileChooser.APPROVE_OPTION) {
            this.setCurrentPath(fc.getCurrentDirectory().getAbsolutePath());
        }

        return retval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getSelectedFile() {
        return fc.getSelectedFile();
    }
}
