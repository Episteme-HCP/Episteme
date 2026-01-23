package org.jscience.util;

import java.io.Serializable;
import java.util.*;

/**
 * A class representing a tree data structure with many child elements.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
public class NAryTree<T> implements Tree<T>, Cloneable, Serializable {
    /** DOCUMENT ME! */
    private T obj;

    /** DOCUMENT ME! */
    private NAryTree<T> parent;

    /** DOCUMENT ME! */
    private Set<NAryTree<T>> children;

    /**
     * Creates a new NAryTree object.
     */
    public NAryTree() {
        obj = null;
        children = new HashSet<>();
    }

    /**
     * Creates a new NAryTree object.
     *
     * @param o the contents of the tree
     */
    public NAryTree(T o) {
        obj = o;
        children = new HashSet<>();
    }

    /**
     * Returns the contents of this tree node.
     *
     * @return the contents
     */
    @Override
    public T getContents() {
        return obj;
    }

    /**
     * Sets the contents of this tree node.
     *
     * @param obj the new contents
     */
    public void setContents(T obj) {
        this.obj = obj;
    }

    /**
     * Checks if this node has any children.
     *
     * @return true if it has children
     */
    public boolean hasChild() {
        return !children.isEmpty();
    }

    /**
     * Checks if the given tree is a child of this node.
     *
     * @param child the potential child
     * @return true if it is a child
     */
    public boolean hasChild(NAryTree<T> child) {
        return children.contains(child);
    }

    /**
     * Returns the children of this tree node.
     *
     * @return the set of children
     */
    @Override
    public Set<NAryTree<T>> getChildren() {
        return children;
    }

    /**
     * Sets the children of this tree node.
     *
     * @param children the new set of children
     * @throws CircularReferenceException if a circular reference is detected
     */
    public void setChildren(Set<NAryTree<T>> children) throws CircularReferenceException {
        if (children != null) {
            for (NAryTree<T> child : children) {
                if ((child.getParent() == null) && (child != this)) {
                    child.setParent(this);
                } else if (child == this) {
                     throw new CircularReferenceException("Cannot add self as child.");
                } else if (child.getParent() != this) {
                    throw new CircularReferenceException("Cannot add NAryTree child that already has another parent.");
                }
            }
            this.children = children;
        } else {
            throw new IllegalArgumentException("The children Set must be not null.");
        }
    }

    /**
     * Adds a child to this tree node.
     *
     * @param child the child to add
     * @return true if the child was added
     * @throws CircularReferenceException if a circular reference is detected
     */
    public boolean addChild(NAryTree<T> child) throws CircularReferenceException {
        if (child != null) {
            if (child == this) {
                throw new CircularReferenceException("Cannot add self as child.");
            }
            if (child.getParent() != null && child.getParent() != this) {
                throw new CircularReferenceException("Cannot add NAryTree child that already has a parent.");
            }
            child.setParent(this);
            return children.add(child);
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    /**
     * Removes a child from this tree node.
     *
     * @param child the child to remove
     * @return true if the child was removed
     */
    public boolean removeChild(NAryTree<T> child) {
        if (child != null) {
            if (children.contains(child)) {
                child.setParent(null);
                return children.remove(child);
            } else {
                throw new IllegalArgumentException("The child is not a child of this.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    /**
     * Checks if this node has a parent.
     *
     * @return true if it has a parent
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Returns the parent of this tree node.
     *
     * @return the parent
     */
    public NAryTree<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of this tree node.
     *
     * @param child the new parent
     */
    private void setParent(NAryTree<T> child) {
        parent = child;
    }

    /**
     * Returns the depth of this node in the tree.
     *
     * @return the depth
     */
    public int getDepth() {
        NAryTree<T> element = this;
        int result = 0;
        while (element.getParent() != null) {
            result += 1;
            element = element.getParent();
        }
        return result;
    }

    /**
     * Returns the root of the tree.
     *
     * @return the root
     */
    public NAryTree<T> getRoot() {
        NAryTree<T> element = this;
        NAryTree<T> result = null;
        while (element != null) {
            result = element;
            element = element.getParent();
        }
        return result;
    }

    /**
     * Returns all descendants (children, grandchildren, etc.).
     *
     * @return the set of descendants
     */
    public Set<NAryTree<T>> getAllChildren() {
        Set<NAryTree<T>> result = new HashSet<>(children);
        for (NAryTree<T> child : children) {
            result.addAll(child.getAllChildren());
        }
        return result;
    }

    /**
     * Checks if the given node is a descendant of this node.
     *
     * @param child the potential descendant
     * @return true if it is a descendant
     */
    public boolean hasDistantChild(NAryTree<T> child) {
        if (child == null) return false;
        if (children.contains(child)) return true;
        for (NAryTree<T> c : children) {
            if (c.hasDistantChild(child)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NAryTree)) return false;
        NAryTree<?> that = (NAryTree<?>) o;
        return Objects.equals(obj, that.obj) &&
               Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            NAryTree<T> result = new NAryTree<>(this.obj);
            for (NAryTree<T> child : children) {
                result.addChild((NAryTree<T>) child.clone());
            }
            return result;
        } catch (CircularReferenceException e) {
             throw new RuntimeException("Cloning failed due to circular reference", e);
        }
    }
}
