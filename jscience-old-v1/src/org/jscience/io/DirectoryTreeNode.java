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
