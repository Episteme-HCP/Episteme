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
