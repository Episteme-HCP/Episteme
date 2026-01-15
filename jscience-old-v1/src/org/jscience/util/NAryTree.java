package org.jscience.util;

import java.io.Serializable;

import java.util.*;


/**
 * A class representing a tree data structure with many child elements.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//perhaps we should add synchronization support here
//null contents for each node accepted
//we could have some methods for clustering here, see 
//http://128.32.125.151/riot/Applications/Clustering/index.html or http://www2.biology.ualberta.ca/jbrzusto/cluster.php
public class NAryTree extends Object implements Tree, Cloneable, Serializable {
    /** DOCUMENT ME! */
    private Object obj;

    /** DOCUMENT ME! */
    private NAryTree parent;

    /** DOCUMENT ME! */
    private Set children;

/**
     * Creates a new NAryTree object.
     */
    public NAryTree() {
        obj = null;
        children = Collections.EMPTY_SET;
    }

/**
     * Creates a new NAryTree object.
     *
     * @param o DOCUMENT ME!
     */
    public NAryTree(Object o) {
        obj = o;
        children = Collections.EMPTY_SET;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getContents() {
        return obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void setContents(Object obj) {
        this.obj = obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild() {
        return !children.isEmpty();
    }

    //if tree is one of the children of this
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild(NAryTree child) {
        return children.contains(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        //perhaps we should return a copy of the Set to be sure that the contents are not modified and NAryTree structure is destroyed
        return children;
    }

    //all elements should be of class NAryTree
    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setChildren(Set children) throws CircularReferenceException {
        Iterator iterator;
        Object currentElement;
        NAryTree child;

        if (children != null) {
            iterator = children.iterator();

            while (iterator.hasNext()) {
                currentElement = iterator.next();

                if (currentElement instanceof NAryTree) {
                    child = (NAryTree) currentElement;

                    if ((child.getParent() == null) && (child != this)) {
                        child.setParent(this);
                    } else {
                        throw new CircularReferenceException(
                            "Cannot add NAryTree child that has already a parent.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The children Set must contain only NAryTrees.");
                }
            }

            this.children = children;
        } else {
            throw new IllegalArgumentException(
                "The children Set must be not null.");
        }
    }

    //we cannot add self or a (distant) parent
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean addChild(NAryTree child) throws CircularReferenceException {
        //synchronized (this) {
        if (child != null) {
            if ((child != this) && (child.getParent() == null)) {
                child.setParent(this);

                return children.add(child);
            } else {
                throw new CircularReferenceException(
                    "Cannot add NAryTree child that has already a parent.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }

        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean removeChild(NAryTree child) {
        //synchronized (this) {
        if (child != null) {
            if (children.contains(child)) {
                child.setParent(null);

                return children.remove(child);
            } else {
                throw new IllegalArgumentException(
                    "The child is not a child of this.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }

        //}
    }

    //isRoot() == !hasParent();
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasParent() {
        return parent != null;
    }

    //to set the parent, use element.getParent().removeChild(element);newParent.addChild(element);
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NAryTree getParent() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    private void setParent(NAryTree child) {
        parent = child;
    }

    //0 if this element is the root
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDepth() {
        NAryTree element;
        int result;

        element = this;
        result = 0;

        while (element.getParent() != null) {
            result += 1;
            element = element.getParent();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NAryTree getRoot() {
        NAryTree element;
        NAryTree result;

        element = this;
        result = null;

        while (element != null) {
            result = element;
            element = element.getParent();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static NAryTree getRoot(NAryTree tree) {
        NAryTree element;
        NAryTree result;

        result = null;

        if (tree != null) {
            element = tree;

            while (element != null) {
                result = element;
                element = element.getParent();
            }
        }

        return result;
    }

    //the common parent
    /**
     * DOCUMENT ME!
     *
     * @param tree1 DOCUMENT ME!
     * @param tree2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static NAryTree getCommonRoot(NAryTree tree1, NAryTree tree2) {
        Vector lineage1;
        Vector lineage2;
        NAryTree root1;
        NAryTree root2;
        NAryTree result;
        int i;

        if ((tree1 != null) && (tree2 != null)) {
            root1 = tree1.getRoot();
            root2 = tree2.getRoot();

            if ((root1 != null) && (root2 != null) && (root1.equals(root2))) {
                lineage1 = getLineage(root1, tree1);
                lineage2 = getLineage(root2, tree2);
                i = 1;

                while ((i < Math.min(lineage1.size(), lineage2.size())) &&
                        lineage1.get(i).equals(lineage2.get(i))) {
                    i++;
                }

                result = (NAryTree) lineage1.get(i - 1);
            } else {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    //get all the children from tree1 to tree2
    /**
     * DOCUMENT ME!
     *
     * @param tree1 DOCUMENT ME!
     * @param tree2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector getLineage(NAryTree tree1, NAryTree tree2) {
        NAryTree element;
        Vector result;

        if ((tree1 != null) && (tree2 != null)) {
            result = new Vector();
            element = tree2;

            while ((element != null) && !element.equals(tree1)) {
                result.add(element);
                element = element.getParent();
            }

            if (element == null) {
                result = new Vector();
            } else {
                result.add(element);
            }
        } else {
            result = null;
        }

        return result;
    }

    //extracts the shortest and simpliest NAryTree between two individuals
    /**
     * DOCUMENT ME!
     *
     * @param tree1 DOCUMENT ME!
     * @param tree2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static NAryTree extractNAryTree(NAryTree tree1, NAryTree tree2) {
        Vector lineage1;
        Vector lineage2;
        NAryTree currentNAryTree1;
        NAryTree nextNAryTree1;
        NAryTree currentNAryTree2;
        NAryTree nextNAryTree2;
        NAryTree result;
        int i;

        if ((tree1 != null) && (tree2 != null)) {
            lineage1 = getLineage(tree1.getRoot(), tree1);
            lineage2 = getLineage(tree2.getRoot(), tree2);

            if (lineage1.get(0).equals(lineage2.get(0))) {
                result = null;

                try {
                    //find highest common element
                    i = 1;

                    if (lineage1.size() < lineage2.size()) {
                        while ((i < lineage1.size()) &&
                                lineage1.get(i).equals(lineage2.get(i))) {
                            i++;
                        }

                        //build a new simple tree
                        result = new NAryTree(((NAryTree) lineage1.get(i - 1)).getContents());
                        currentNAryTree1 = result;
                        currentNAryTree2 = result;

                        while (i < lineage1.size()) {
                            nextNAryTree1 = new NAryTree(((NAryTree) lineage1.get(
                                        i)).getContents());
                            currentNAryTree1.addChild(nextNAryTree1);
                            currentNAryTree1 = nextNAryTree1;
                            nextNAryTree2 = new NAryTree(((NAryTree) lineage2.get(
                                        i)).getContents());
                            currentNAryTree2.addChild(nextNAryTree2);
                            currentNAryTree2 = nextNAryTree2;
                            i++;
                        }

                        while (i < lineage2.size()) {
                            nextNAryTree2 = new NAryTree(((NAryTree) lineage2.get(
                                        i)).getContents());
                            currentNAryTree2.addChild(nextNAryTree2);
                            currentNAryTree2 = nextNAryTree2;
                            i++;
                        }
                    } else {
                        while ((i < lineage1.size()) &&
                                lineage1.get(i).equals(lineage2.get(i))) {
                            i++;
                        }

                        //build a new simple tree
                        result = new NAryTree(((NAryTree) lineage1.get(i - 1)).getContents());
                        currentNAryTree1 = result;
                        currentNAryTree2 = result;

                        while (i < lineage2.size()) {
                            nextNAryTree1 = new NAryTree(((NAryTree) lineage1.get(
                                        i)).getContents());
                            currentNAryTree1.addChild(nextNAryTree1);
                            currentNAryTree1 = nextNAryTree1;
                            nextNAryTree2 = new NAryTree(((NAryTree) lineage2.get(
                                        i)).getContents());
                            currentNAryTree2.addChild(nextNAryTree2);
                            currentNAryTree2 = nextNAryTree2;
                            i++;
                        }

                        while (i < lineage1.size()) {
                            nextNAryTree1 = new NAryTree(((NAryTree) lineage1.get(
                                        i)).getContents());
                            currentNAryTree1.addChild(nextNAryTree1);
                            currentNAryTree1 = nextNAryTree1;
                            i++;
                        }
                    }
                } catch (CircularReferenceException e) {
                }
            } else {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    //return all children and grandchildren, etc...
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllChildren() {
        Iterator iterator;
        Set result;

        iterator = children.iterator();
        result = new HashSet(children);

        while (iterator.hasNext()) {
            result.addAll(((NAryTree) iterator.next()).getAllChildren());
        }

        return result;
    }

    //perhaps this implementation is faster

    /*
    public Set getAllChildren() {
    
        Set currentChildren;
        HashSet newChildren;
        Iterator iterator;
        HashSet result;
    
        currentChildren = children;
        newChildren = new HashSet();
        result = new HashSet();
        result.addAll(currentChildren);
        iterator = currentChildren.iterator();
        while (iterator.hasNext()) {
            newChildren.addAll(((NAryTree)iterator.next()).getChildren());
        }
        while (newChildren.size()!=0) {
            result.addAll(newChildren);
            currentChildren=newChildren;
            newChildren = new HashSet();
            iterator = currentChildren.iterator();
            while (iterator.hasNext()) {
                newChildren.addAll(((NAryTree)iterator.next()).getChildren());
            }
        }
    
        return result;
    
    }
     */
    public boolean hasDistantChild(NAryTree child) {
        Set currentChildren;
        Set tempChildren;
        Set newChildren;
        Iterator iterator;
        boolean result;

        if (child != null) {
            currentChildren = children;
            newChildren = Collections.EMPTY_SET;
            result = children.contains(child);
            iterator = currentChildren.iterator();

            while (iterator.hasNext() && !result) {
                tempChildren = ((NAryTree) iterator.next()).getChildren();
                result = tempChildren.contains(child);
                newChildren.addAll(tempChildren);
            }

            while ((newChildren.size() != 0) && !result) {
                currentChildren = newChildren;
                newChildren = new HashSet();
                iterator = currentChildren.iterator();

                while (iterator.hasNext() && !result) {
                    tempChildren = ((NAryTree) iterator.next()).getChildren();
                    result = tempChildren.contains(child);
                    newChildren.addAll(tempChildren);
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    //complete equality, of contents and relations
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        NAryTree tree;

        if ((o != null) && (o instanceof NAryTree)) {
            tree = (NAryTree) o;

            return getContents().equals(tree.getContents()) &&
            getChildren().equals(tree.getChildren()) &&
            getParent().equals(tree.getParent());
        } else {
            return false;
        }
    }

    //builds a shallow NAryTree (new NAryTree elements but same contents) from this element and under
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        NAryTree result;
        Iterator iterator;

        //result = (NAryTree) super.clone();
        result = new NAryTree(this.getContents());

        //result.obj = getContents();
        iterator = getChildren().iterator();

        while (iterator.hasNext()) {
            try {
                result.addChild((NAryTree) (((NAryTree) iterator.next()).clone()));
            } catch (CircularReferenceException e) {
            }
        }

        return result;
    }
}
