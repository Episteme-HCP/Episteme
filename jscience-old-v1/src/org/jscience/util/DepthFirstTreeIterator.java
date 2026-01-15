package org.jscience.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * Provides an iterator through all the children of tree including tree
 * itself. This is an iterator over the tree elements, not the contents of the
 * tree elements. You have to call getContents() over each element to get
 * contents, or use the conveninece method.
 */
public class DepthFirstTreeIterator implements Iterator {
    /** DOCUMENT ME! */
    private Tree tree;

    /** DOCUMENT ME! */
    private Iterator iterator;

/**
     * Create an Iterator from a tree. The iterator is built at construction
     * time. If you change the tree elements afterwards, the changes won't be
     * reflected here.
     *
     * @param tree of objects on which to enumerate.
     */
    public DepthFirstTreeIterator(Tree tree) {
        this.tree = tree;
        this.iterator = getDepthFirstVector(tree).iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Vector getDepthFirstVector(Tree tree) {
        Vector vector;
        Iterator iterator;

        vector = new Vector();
        vector.add(tree);
        iterator = tree.getChildren().iterator();

        while (iterator.hasNext()) {
            vector.addAll(getDepthFirstVector((Tree) iterator.next()));
        }

        return vector;
    }

    /**
     * Tests if this Iterator contains more elements.
     *
     * @return true if and only if this Iterator object contains at least one
     *         more element to provide; false otherwise.
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element of this Iterator if this Iterator
     * object has at least one more element to provide.
     *
     * @return the next element of this Iterator.
     *
     * @throws NoSuchElementException if no more elements exist.
     */
    public synchronized Object next() throws NoSuchElementException {
        return iterator.next();
    }

    /**
     * Removes from the underlying collection the last element returned
     * by the iterator (optional operation).  This method can be called only
     * once per call to <tt>next</tt>.  The behavior of an iterator is
     * unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *         is not supported by this Iterator.
     */
    public void remove() {
        throw new UnsupportedOperationException();

        //we don't support this method since we would probably rapidly make a real mess out of the underlying tree
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getContentsIterator() {
        Vector result;

        result = new Vector();

        while (hasNext()) {
            result.add(((Tree) next()).getContents());
        }

        return result.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Tree getTree() {
        return tree;
    }
}
