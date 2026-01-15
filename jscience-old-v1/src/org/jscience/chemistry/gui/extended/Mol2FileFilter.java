package org.jscience.chemistry.gui.extended;

import java.io.File;
import java.io.FilenameFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Mol2FileFilter implements FilenameFilter {
/**
     * Creates a new Mol2FileFilter object.
     */
    public Mol2FileFilter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(File dir, String name) {
        if (name.toLowerCase().endsWith(".mol2")) {
            System.out.println("Accept: " + name);

            return true;
        }

        return false;
    }
}
