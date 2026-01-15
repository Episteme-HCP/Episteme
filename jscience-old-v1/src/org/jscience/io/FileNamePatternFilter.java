/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.File;


/**
 * filters by matching the file name to a regular expression.
 *
 * @author Holger Antelmann
 */
public class FileNamePatternFilter {
    /** DOCUMENT ME! */
    String regex;

    /** DOCUMENT ME! */
    boolean fullPath;

/**
     * Creates a new FileNamePatternFilter object.
     *
     * @param regexFileName DOCUMENT ME!
     */
    public FileNamePatternFilter(String regexFileName) {
        this(regexFileName, false);
    }

/**
     * Creates a new FileNamePatternFilter object.
     *
     * @param regexFileName   DOCUMENT ME!
     * @param includeFullPath DOCUMENT ME!
     */
    public FileNamePatternFilter(String regexFileName, boolean includeFullPath) {
        regex = regexFileName;
        fullPath = includeFullPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(File file) {
        return fullPath ? file.getAbsolutePath().matches(regex)
                        : file.getName().matches(regex);
    }
}
