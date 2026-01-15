/*
 * A very minimal implementation of FileFilter for JFileChooser.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * A very minimal implementation of FileFilter for JFileChooser.
 */
public class BasicFileFilter extends FileFilter {
    /**
     * DOCUMENT ME!
     */
    String ext = null;

    /**
     * DOCUMENT ME!
     */
    String desc = null;

    /**
     * Creates a new BasicFileFilter object.
     *
     * @param extension DOCUMENT ME!
     * @param description DOCUMENT ME!
     */
    public BasicFileFilter(String extension, String description) {
        if (extension != null) {
            ext = extension.toLowerCase();
        }

        desc = description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }

            if (f.getName().toLowerCase().endsWith(ext)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return desc;
    }
}
