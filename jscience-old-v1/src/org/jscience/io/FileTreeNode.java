/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import org.jscience.util.AbstractIterator;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;


/**
 * FileTreeNode implements a TreeNode that can be used to visualize a file
 * structure. Both, directories and files are visible by default. Optionally,
 * a FileFilter can be used to restrict the files in the tree.
 *
 * @author Holger Antelmann
 *
 * @see DirectoryTreeNode
 * @see javax.swing.JTree
 * @see javax.swing.tree.DefaultTreeModel
 */
public class FileTreeNode implements TreeNode, Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 7817879951568130545L;

    /**
     * DOCUMENT ME!
     */
    static Comparator<FileTreeNode> fileDirComparator = new Comparator<FileTreeNode>() {
            public int compare(FileTreeNode node1, FileTreeNode node2) {
                File file1 = node1.getFile();
                File file2 = node2.getFile();
                String one = !file1.isDirectory() + file1.toString();
                String two = !file2.isDirectory() + file2.toString();

                return one.compareTo(two);
            }
        };

    /**
     * DOCUMENT ME!
     */
    final File file;

    /**
     * DOCUMENT ME!
     */
    ArrayList<FileTreeNode> subFiles;

    /**
     * DOCUMENT ME!
     */
    FileFilter filter;

/**
     * initializes the FileTreeNode with the given file
     */
    public FileTreeNode(File file) {
        this(file, null);
    }

/**
     * initializes the FileTreeNode with the given file and filter
     */
    public FileTreeNode(File file, FileFilter filter) {
        if (file == null) {
            throw new NullPointerException();
        }

        this.file = file;
        this.filter = filter;
        subFiles = new ArrayList<FileTreeNode>();
        refresh();
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void refresh() {
        subFiles.clear();

        File[] sf = file.listFiles(filter);

        if (sf != null) {
            for (int i = 0; i < sf.length; i++) {
                subFiles.add(new FileTreeNode(sf[i], filter));
            }

            Collections.sort(subFiles, fileDirComparator);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getFile() {
        return file;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FileFilter getFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized Enumeration children() {
        return AbstractIterator.enumerate(subFiles.iterator());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized int getChildCount() {
        return subFiles.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean getAllowsChildren() {
        if (!file.isDirectory()) {
            return false;
        }

        return (subFiles.size() > 0) ? true : false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param childIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized TreeNode getChildAt(int childIndex) {
        return (FileTreeNode) subFiles.get(childIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized int getIndex(TreeNode node) {
        return subFiles.indexOf(node);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeaf() {
        return (!file.isDirectory());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TreeNode getParent() {
        return new FileTreeNode(new File(file.getParent()), filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        //if (obj == null) return false;
        if (!(obj instanceof FileTreeNode)) {
            return false;
        }

        return ((FileTreeNode) obj).file.equals(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return file.getName();
    }
}
