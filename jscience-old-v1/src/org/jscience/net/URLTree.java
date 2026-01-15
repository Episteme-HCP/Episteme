/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.io.Serializable;

import java.net.URL;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


/**
 * URLTree represents the model of a tree where each node is made of a URL
 * wrapped in a URLNode object. The tree is configurable in size by adding
 * restrictions that determine whether a URLNode is included in the tree or not.<br>
 * Note that changing parameters of the CrawlerSetting used by the URL tree
 * without recalculating the tree may put the tree in an inconsistent state.
 *
 * @author Holger Antelmann
 *
 * @see URLNode
 */
public class URLTree extends DefaultTreeModel implements Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -5976999431784737271L;

    /**
     * DOCUMENT ME!
     */
    private boolean singlePath;

    /**
     * DOCUMENT ME!
     */
    private CrawlerSetting crawler;

    /** Vector of URL objects */
    private Vector<URL> allNodes;

/**
     * calls <code>URLTree(root, new SampleCrawlerSetting(), false)</code>
     */
    public URLTree(URL root) {
        this(root, new SampleCrawlerSetting(), false);
    }

/**
     * @param root       the root node of the tree
     * @param crawler    restrictions that define the content of the URLTree
     * @param singlePath if true, the tree allows each URL to occur only once in the tree
     */
    public URLTree(URL root, CrawlerSetting crawler, boolean singlePath) {
        super(new URLNode(root, null, null));
        this.crawler = crawler;
        setAsksAllowsChildren(true);
        ((URLNode) getRoot()).tree = this;
        ((URLNode) getRoot()).parent = null;
        allNodes = new Vector<URL>();
        allNodes.add(root);

        //revalidate(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     */
    public synchronized void setRoot(URL root) {
        setRoot(new URLNode(root, null, this));
    }

    /**
     * overwritten to ensure proper immediate calculation of the entire
     * new tree based on the new settings.
     *
     * @param root DOCUMENT ME!
     *
     * @throws ClassCastException if given node is not a URLNode
     */
    public synchronized void setRoot(TreeNode root) {
        if (!(root instanceof URLNode)) {
            throw new ClassCastException(
                "the new TreeNode root must be a URLNode");
        }

        super.setRoot(root);
        ((URLNode) getRoot()).tree = this;
        ((URLNode) getRoot()).parent = null;
        revalidate(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrawlerSetting getCrawler() {
        return crawler;
    }

    /**
     * on calling this method, the entire tree will be recalculated
     * based on the settings of the new crawler
     *
     * @param crawler DOCUMENT ME!
     */
    public synchronized void setCrawler(CrawlerSetting crawler) {
        this.crawler = crawler;
        revalidate(false);
    }

    /**
     * if true, the URLTree allows every URL to only occur once in the
     * tree
     *
     * @return DOCUMENT ME!
     */
    public boolean getSinglePath() {
        return singlePath;
    }

    /**
     * if set to true, the URLTree allows every URL to only occur once
     * in the tree; calling this function forces the entire tree to be
     * revalidated from the root
     *
     * @param singlePath DOCUMENT ME!
     */
    public synchronized void setSinglePath(boolean singlePath) {
        this.singlePath = singlePath;
        revalidate(false);
    }

    /**
     * recalculates the entire tree recursively from the root based on
     * the current settings; this should be done every time when parameters of
     * the crawler changed, otherwise the tree could be in an inconsisten
     * state
     *
     * @param refresh if true, each cached node contend is refreshed, i.e.
     *        reloaded from the net
     */
    public synchronized void revalidate(boolean refresh) {
        allNodes.clear();
        allNodes.add(((URLNode) getRoot()).getURL());
        revalidateNode((URLNode) getRoot(), refresh);
        nodeStructureChanged((TreeNode) getRoot());
    }

    /**
     * internally called by recalculate(); recalculates sub-tree
     * recursively
     *
     * @param node DOCUMENT ME!
     * @param refresh DOCUMENT ME!
     */
    protected void revalidateNode(URLNode node, boolean refresh) {
        node.createChildren(refresh);

        Enumeration e = node.children();

        while (e.hasMoreElements()) {
            revalidateNode((URLNode) e.nextElement(), refresh);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfNodes() {
        return allNodes.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param page DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean containsPage(URL page) {
        for (URL u : allNodes) {
            if (page.sameFile(u)) {
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
/**
     * accessed by URLNode to add new URL objects to the list
     */
    List<URL> getNodeList() {
        return allNodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += (" (crawler in use:" + crawler.toString() + ")");

        return s;
    }
}
