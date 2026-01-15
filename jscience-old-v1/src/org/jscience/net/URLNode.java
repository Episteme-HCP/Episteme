/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

/**
 * URLNode wrapps a URL and places it into a URLTree, which determines
 * how the URLNode calculates its children.
 *
 * @author Holger Antelmann
 * @see URLTree
 */
public class URLNode implements TreeNode, Serializable {
    static final long serialVersionUID = 5325742446217277586L;

    URLTree tree;
    URLNode parent;
    private URLCache uc;
    private Vector<URLNode> children = null;
    boolean leaf;
    boolean expanded = false;

    public URLNode(URL url, URLNode parent, URLTree tree) {
        this(new URLCache(url), parent, tree);
    }

    public URLNode(URLCache uc, URLNode parent, URLTree tree) {
        this.tree = tree;
        this.parent = parent;
        this.uc = uc;
    }

    public Enumeration<URLNode> children() {
        if (children == null) createChildren(false);
        return children.elements();
    }

    public boolean getAllowsChildren() {
        if (!expanded &&
                !tree.getCrawler().followLinks(uc.getURL(),
                        ((parent == null) ? null : parent.uc.getURL()),
                        tree.getPathToRoot(this).length,
                        null, tree.getNodeList(), null))
            return false;
        return true;
    }

    public TreeNode getChildAt(int childIndex) {
        if (children == null) createChildren(false);
        return children.get(childIndex);
    }

    public int getChildCount() {
        if (children == null) createChildren(false);
        return children.size();
    }

    public int getIndex(TreeNode node) {
        if (children == null) createChildren(false);
        if (node instanceof URLNode) {
            URLCache urlc = ((URLNode) node).uc;
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i).uc.equals(urlc)) return i;
            }
        }
        return -1;
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        if (children == null) createChildren(false);
        return (children.isEmpty());
    }

    public String toString() {
        return uc.getURL().toString();
    }

    /**
     * refreshes the cached URL content and recursively all children
     */
    public synchronized void refresh() {
        createChildren(true);
    }

    void createChildren(boolean refresh) {
        synchronized (tree) {
            HashMap<URL, URLCache> ucmap = new HashMap<URL, URLCache>();
            if (refresh) {
                uc.clearCache();
            } else {
                // keep a copy of the children URLCache objects
                // for potential re-use
                for (int i = 0; (children != null) && (i < children.size()); i++) {
                    ucmap.put(((URLNode) children.get(i)).getURL(),
                            ((URLNode) children.get(i)).getURLCache());
                }
            }
            children = null;
            if (!getAllowsChildren()) {
                children = new Vector<URLNode>();
                leaf = false;
                return;
            }
            children = new Vector<URLNode>();
            URL[] links;
            try {
                links = uc.getLinks();
            } catch (IOException e) {
                links = new URL[0];
            }
            for (int i = 0; i < links.length; i++) {
                // check whether the link has been encountered before if
                // unique path is set for the tree
                if ((tree.getSinglePath()) && tree.containsPage(links[i]))
                    continue;
                // ask the tree's crawler whether the URL is allowed in the tree
                if (!tree.getCrawler().matchesCriteria(links[i], uc.getURL(),
                        tree.getPathToRoot(this).length, tree.getNodeList(), null))
                    continue;
                // check whether there is a URLCache object that we can reuse
                if (ucmap.containsKey(links[i])) {
                    children.add(new URLNode(ucmap.get(links[i]), this, tree));
                } else {
                    children.add(new URLNode(links[i], this, tree));
                }
                // add the newly created node to the list of all nodes in the tree
                // to be able to count total number of nodes and
                // to check for uniqueness if required
                tree.getNodeList().add(links[i]);
            }
            if (children.isEmpty()) leaf = true;
            else leaf = false;
        }
    }

    /**
     * makes this node expand beyond the crawler rules of the associated URLTree
     */
    public void expand() {
        expanded = true;
        if (children == null) createChildren(false);
        if (leaf) return;
        createChildren(false);
    }

    /**
     * reverses the effect of expand()
     */
    public void collapse() {
        expanded = false;
        children = null;
    }

    public URLCache getURLCache() {
        return uc;
    }

    public URL getURL() {
        return uc.getURL();
    }
}
