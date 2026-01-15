/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.File;
import java.io.FileFilter;


/**
 * DirectoryTreeNode implements a TreeNode that can be used to visualize a
 * directory structure. Only directories are visible.
 *
 * @author Holger Antelmann
 *
 * @see FileTreeNode
 * @see javax.swing.JTree
 * @see javax.swing.tree.DefaultTreeModel
 */
public class DirectoryTreeNode extends FileTreeNode {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3298009520950499306L;

    /** DOCUMENT ME! */
    static FileFilter dirFilter = new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isDirectory());
            }
        };

/**
     * initializes the DirectoryTreeNode with the given directory
     *
     * @param dir DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public DirectoryTreeNode(File dir) {
        super(dir, dirFilter);

        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + " is not a directory");
        }
    }
}
