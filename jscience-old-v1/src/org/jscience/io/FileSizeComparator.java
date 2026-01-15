/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.File;

import java.util.Comparator;


/**
 * 
DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
public class FileSizeComparator implements Comparator<File> {
    /**
     * DOCUMENT ME!
     *
     * @param file1 DOCUMENT ME!
     * @param file2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(File file1, File file2) {
        return (int) (file1.length() - file2.length());
    }
}
