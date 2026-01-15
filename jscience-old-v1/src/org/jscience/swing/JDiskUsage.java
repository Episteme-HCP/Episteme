/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.io.ExtendedFile;
import org.jscience.io.FileTreeNode;

import org.jscience.util.StringUtils;

import java.awt.*;

import java.io.File;

import java.text.DecimalFormat;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;


/**
 * shows disk usage based on the given root directory. The 'info'-pane is
 * rather primitive, with only a text representation of the results.
 *
 * @author Holger Antelmann
 */
public class JDiskUsage extends JComponent {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5652010271977749718L;

    /** DOCUMENT ME! */
    static final DecimalFormat dformat = new DecimalFormat("0.###");

    /** DOCUMENT ME! */
    static final DecimalFormat pformat = new DecimalFormat("%");

    /** DOCUMENT ME! */
    final Comparator<File> fileSizeComparator = new Comparator<File>() {
            public int compare(File file1, File file2) {
                // here's a problem w/ conversion if the difference is too large
                return (int) (length(file2) - length(file1));
            }
        };

    /** DOCUMENT ME! */
    JTree tree;

    /**
     * DOCUMENT ME!
     */
/**
     * DOCUMENT ME!
     */
    HashMap<File, Long> cache = new HashMap<File, Long>();

/**
     * Creates a new JDiskUsage object.
     *
     * @param rootDir DOCUMENT ME!
     */
    public JDiskUsage(File rootDir) {
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("not a directory: " + rootDir);
        }

        setName(rootDir.toString());
        tree = new JTree();
        setRoot(rootDir);

        TreeSelectionModel selModel = new DefaultTreeSelectionModel();
        selModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionModel(selModel);

        final JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(final TreeSelectionEvent ev) {
                    textArea.setText("updating; please wait ..");

                    Thread t = new Thread() {
                            public void run() {
                                File file = ((FileTreeNode) ev.getPath()
                                                              .getLastPathComponent()).getFile();
                                final String info = info(file);
                                SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            textArea.setText(info);
                                            textArea.setCaretPosition(0);
                                        }
                                    });
                            }
                        };

                    t.start();
                }
            });
        setLayout(new BorderLayout());

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(textArea));
        pane.setDividerLocation(.5);
        add(pane);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     */
    public void setRoot(File dir) {
        tree.setModel(new DefaultTreeModel(new FileTreeNode(dir)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getRoot() {
        return ((FileTreeNode) tree.getModel().getRoot()).getFile();
    }

    /**
     * DOCUMENT ME!
     */
    public void deleteCache() {
        synchronized (cache) {
            cache.clear();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String info(File file) {
        synchronized (cache) {
            long total = length(file);
            StringBuffer buf = new StringBuffer("total size: " + format(total));
            buf.append(StringUtils.lb + StringUtils.lb);

            if (file.isDirectory()) {
                File[] list = file.listFiles();
                Arrays.sort(list, fileSizeComparator);

                for (File f : list) {
                    buf.append(format(f, total) + StringUtils.lb);
                }
            } else {
                buf.append(format(file, total) + StringUtils.lb);
            }

            return buf.toString();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @param total DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String format(File file, long total) {
        long length = length(file);

        return file.getName() + ": " + format(length) + " (" +
        pformat.format((double) length / (double) total) + ")";
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String format(long size) {
        if (size > 1000000000) {
            return dformat.format((double) size / 1000000000d) + " GB";
        }

        if (size > 1000000) {
            return dformat.format((double) size / 1000000d) + " MB";
        }

        if (size > 1000) {
            return dformat.format((double) size / 1000d) + " KB";
        }

        return String.valueOf(size) + " bytes";
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private long length(File file) {
        Long l = cache.get(file);

        if (l != null) {
            return l.longValue();
        }

        l = new Long(new ExtendedFile(file).length());
        cache.put(file, l);

        return l.longValue();
    }
}
