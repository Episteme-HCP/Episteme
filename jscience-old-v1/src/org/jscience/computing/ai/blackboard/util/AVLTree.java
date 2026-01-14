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

/* AVLTree.java */
package org.jscience.computing.ai.blackboard.util;

import java.util.BitSet;
import java.util.Stack;


/**
 * AVLTree class.  Implements a height balanced binary tree data structure.
 * Elements contain a referencing key and a data value.  Any object may be
 * used as a key and/or data member.
 *
 * @author:   Paul Brown
 * @version:  1.5, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class AVLTree {
    /** The root node of the tree. */
    private AVLNode root;

    /** The total number of nodes in the tree. */
    private int node_count;

/**
         * Constructs a new empty tree.
         */
    public AVLTree() {
        node_count = 0;
    }

    /**
     * Returns true if the tree contains no elements.
     *
     * @return DOCUMENT ME!
     */
    public boolean empty() {
        return (root == null);
    }

    /**
     * Puts the specified element into the tree.  The element is
     * indexed by the hashcode of the specified key.  The same element may be
     * retrieved by calling get() with the same key.  If an element with the
     * same key already exists within the tree, it is replaced by the new
     * element and the data value of the discarded element is returned.
     *
     * @param key the key used to index the element
     * @param data the specified element's contents
     *
     * @return the replaced element's data value, or null if the tree did not
     *         previously contain an element with the specified key
     *
     * @see java.lang.Object#hashCode
     * @see AVLTree#get
     */
    public Object put(Object key, Object data) {
        AVLNode existing_node;
        Object old_data = null;

        if (root == null) {
            root = new AVLNode(key.hashCode(), data);
        } else {
            existing_node = new AVLNode();
            root = root.put(key.hashCode(), data, existing_node);
            old_data = existing_node.data;
        }

        if (old_data == null) {
            node_count++;
        }

        return (old_data);
    }

    /**
     * Gets the element associated with the specified key.
     *
     * @param key the key of the element to get
     *
     * @return the data value of the element referenced by the key, or null if
     *         the key was not found
     *
     * @see AVLTree#put
     */
    public Object get(Object key) {
        if (root == null) {
            return (null);
        } else {
            return (root.get(key.hashCode()));
        }
    }

    /**
     * Removes the element indexed by the specified key.  Does nothing
     * if the key is not present.  If an element is removed from the tree,
     * it's data value is returned as the result of this operation.
     *
     * @param key the key of the element to remove
     *
     * @return the removed element's data value, or null if the tree did not
     *         contain an element with the specified key
     */
    public Object remove(Object key) {
        AVLNode existing_node;
        Object old_data = null;

        if (root != null) {
            existing_node = new AVLNode();
            root = root.remove(key.hashCode(), existing_node);
            old_data = existing_node.data;

            if (old_data != null) {
                node_count--;
            }
        }

        return (old_data);
    }

    /**
     * Removes all nodes from the tree.
     */
    public void clear() {
        root = null;
        node_count = 0;
    }

    /**
     * Returns an array containing key/data pairs of all elements in
     * the tree.
     *
     * @return element array, or null if the tree is empty
     */
    public ValuePair[] elements() {
        ValuePair[] elements;
        Stack stack;
        AVLNode subtree = root;
        int element_index = 0;

        if (root == null) {
            return (null);
        } else {
            elements = new ValuePair[node_count];
            stack = new Stack();

            // perform an inorder traversal of tree
            while ((subtree != null) || (stack.empty() == false)) {
                while (subtree != null) {
                    stack.push(subtree);
                    subtree = subtree.left;
                }

                subtree = (AVLNode) stack.pop();
                // add this element to element array
                elements[element_index] = new ValuePair(new Integer(subtree.key),
                        subtree.data);
                element_index++;
                subtree = subtree.right;
            }

            return (elements);
        }
    }

    /**
     * Returns a String representation of an instance of this class.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        Stack stack = new Stack();
        BitSet levels = new BitSet();
        AVLNode subtree = root;
        int level = 0;
        int level_offset = 0;
        int zero_count = 0;
        StringBuffer buffer = new StringBuffer();
        ValuePair[] element_array = elements();
        boolean backtracked;

        if (element_array == null) {
            buffer.append("tree is empty\n");
        } else {
            buffer.append("tree (showing key values):\n");

            while ((subtree != null) || (stack.empty() == false)) {
                if (subtree != null) {
                    level_offset++;
                }

                while (subtree != null) {
                    stack.push(subtree);
                    level++;
                    subtree = subtree.right;
                }

                subtree = (AVLNode) stack.pop();
                level--;

                if (level == 0) {
                    level = ++zero_count;
                    level_offset = 0;
                }

                if ((level + level_offset) == 1) {
                    buffer.append("root=");
                } else {
                    for (int i = 0; i < ((level + level_offset) - 1); i++) {
                        if (levels.get(i) == true) {
                            buffer.append("|");
                        } else {
                            buffer.append(" ");
                        }

                        buffer.append("    ");
                    }

                    buffer.append("+----");

                    if (levels.get((level + level_offset) - 1) == true) {
                        levels.clear((level + level_offset) - 1);
                    } else {
                        levels.set((level + level_offset) - 1);
                    }
                }

                buffer.append("[").append(Integer.toString(subtree.key))
                      .append("]\n");
                subtree = subtree.left;

                if (subtree != null) {
                    levels.set(level + level_offset);
                } else {
                    levels.clear(level + level_offset);
                    // is current subtree a left subtree?
                    backtracked = false;

                    do

                        if ((((level + level_offset) - 1) > 1) &&
                                (levels.get((level + level_offset) - 1) == false) &&
                                (level_offset > 0)) {
                            level_offset--;
                        } else {
                            backtracked = true;
                        }
                     while (backtracked == false);
                }

                for (int i = 0; i <= (level + level_offset); i++) {
                    if (levels.get(i) == true) {
                        buffer.append("|");
                    } else {
                        buffer.append(" ");
                    }

                    if (i < (level + level_offset)) {
                        buffer.append("    ");
                    } else {
                        buffer.append("\n");
                    }
                }
            }

            buffer.append("tree elements=[");

            for (int i = 0; i < element_array.length; i++) {
                buffer.append("{key=").append(element_array[i].key().toString());
                buffer.append(", data=")
                      .append(element_array[i].data().toString());

                if (i < (element_array.length - 1)) {
                    buffer.append("}, ");
                } else {
                    buffer.append("}]\n");
                }
            }
        }

        return (buffer.toString());
    }
}


/**
 * AVLNode class.  Objects of this type are manipulated by the AVLTree
 * class.
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
class AVLNode {
    /** The key which is used to reference this node. */
    int key;

    /** The element. */
    Object data;

    /** A balance factor, used when rebalancing the tree. */
    byte balance;

    /** The children of this node. */
    AVLNode left;

    /** The children of this node. */
    AVLNode right;

/**
         * Constructs a new node with key and data values uninitialised.
         */
    protected AVLNode() {
        key = 0;
        data = null;
        balance = 0;
    }

/**
         * Constructs a new node with specified key and data values.
         */
    protected AVLNode(int key, Object data) {
        this.key = key;
        this.data = data;
        balance = 0;
    }

    /**
     * Inserts a new node into the tree.  If current node has the same
     * key value as the new node, the current node's data value is retrieved
     * and then replaced by the new node's data value.
     *
     * @param key DOCUMENT ME!
     * @param data DOCUMENT ME!
     * @param existing_node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode put(int key, Object data, AVLNode existing_node) {
        byte old_balance;

        if (key == this.key) {
            // found existing node, replace data value
            existing_node.data = this.data;
            this.data = data;
        } else if (key < this.key) {
            // insert into left subtree
            if (left != null) {
                old_balance = left.balance;
                left = left.put(key, data, existing_node);

                // has tree grown?
                if ((left.balance != old_balance) && (left.balance != 0)) {
                    balance--;
                }
            } else {
                left = new AVLNode(key, data);
                balance--;
            }
        } else {
            // insert into right subtree
            if (right != null) {
                old_balance = right.balance;
                right = right.put(key, data, existing_node);

                // has tree grown?
                if ((right.balance != old_balance) && (right.balance != 0)) {
                    balance++;
                }
            } else {
                right = new AVLNode(key, data);
                balance++;
            }
        }

        // check if we are now out of balance, if so then rebalance
        if ((balance < -1) || (balance > 1)) {
            return (rebalanceNode());
        } else {
            return (this);
        }
    }

    /**
     * A combination of rotations is performed to rebalance the tree
     * based on the balance factor of each node.
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode rebalanceNode() {
        // balance tree rooted at node using rotations
        if (balance < 0) {
            if (left.balance <= 0) {
                return (singleRotateRight());
            } else {
                // perform double rotation
                left = left.singleRotateLeft();

                return (singleRotateRight());
            }
        } else if (right.balance >= 0) {
            return (singleRotateLeft());
        } else {
            // perform double rotation
            right = right.singleRotateRight();

            return (singleRotateLeft());
        }
    }

    /**
     * Rebalances a right heavy subtree.
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode singleRotateLeft() {
        // perform a single left rotation of the current node
        byte child_balance;
        AVLNode right_child = right;
        // make connections
        right = right_child.left;
        right_child.left = this;
        // update balance factors
        child_balance = right_child.balance;

        if (child_balance <= 0) {
            if (balance >= 1) {
                right_child.balance = (byte) (child_balance - 1);
            } else {
                right_child.balance = (byte) ((balance + child_balance) - 2);
            }

            balance = (byte) (balance - 1);
        } else {
            if (balance <= child_balance) {
                right_child.balance = (byte) (balance - 2);
            } else {
                right_child.balance = (byte) (child_balance - 1);
            }

            balance = (byte) (balance - child_balance - 1);
        }

        return (right_child);
    }

    /**
     * Rebalances a left heavy subtree.
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode singleRotateRight() {
        // perform a single right rotation of the current node
        byte child_balance;
        AVLNode left_child = left;
        // make connections
        left = left_child.right;
        left_child.right = this;
        // update balance factors
        child_balance = left_child.balance;

        if (child_balance <= 0) {
            if (child_balance > balance) {
                left_child.balance = (byte) (child_balance + 1);
            } else {
                left_child.balance = (byte) (balance + 2);
            }

            balance = (byte) ((1 + balance) - child_balance);
        } else {
            if (balance <= -1) {
                left_child.balance = (byte) (child_balance + 1);
            } else {
                left_child.balance = (byte) (balance + child_balance + 2);
            }

            balance = (byte) (1 + balance);
        }

        return (left_child);
    }

    /**
     * Retrieves the data value of the node with the specified key.  If
     * the node is not found, this method returns null.
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Object get(int key) {
        if (key == this.key) {
            // found node
            return (data);
        } else if (key < this.key) {
            // search left subtree
            if (left != null) {
                return (left.get(key));
            } else {
                return (null);
            }
        } else {
            // search right subtree
            if (right != null) {
                return (right.get(key));
            } else {
                return (null);
            }
        }
    }

    /**
     * Removes the node with the specified key value from the tree.
     *
     * @param key DOCUMENT ME!
     * @param existing_node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode remove(int key, AVLNode existing_node) {
        byte old_balance;

        if (key == this.key) {
            // found node
            existing_node.data = data;

            if (right == null) {
                return (left);
            } else {
                // find and remove leftmost child of right child
                old_balance = right.balance;

                AVLNode new_root = new AVLNode(this.key, this.data);
                right = right.removeLeftmostDescendant(new_root);
                // connect new root
                new_root.left = left;
                new_root.right = right;
                new_root.balance = balance;

                return (new_root.restoreRightBalance(old_balance));
            }
        } else if (key < this.key) {
            // remove from left child
            if (left == null) {
                // no left child, key not found
                return (this);
            } else {
                // do the deletion
                old_balance = left.balance;
                left = left.remove(key, existing_node);

                return (restoreLeftBalance(old_balance));
            }
        } else {
            // remove from right child
            if (right == null) {
                // no right child, key not found
                return (this);
            } else {
                // do the deletion
                old_balance = right.balance;
                right = right.remove(key, existing_node);

                return (restoreRightBalance(old_balance));
            }
        }
    }

    /**
     * Finds and retrieves the leftmost descendant of the current node.
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode removeLeftmostDescendant(AVLNode child) {
        byte old_balance;

        if (left == null) {
            // this is the leftmost descendant
            child.key = key;
            child.data = data;

            // remove self from tree
            return (right);
        } else {
            old_balance = left.balance;
            // do the deletion
            left = left.removeLeftmostDescendant(child);

            // fix balance factors
            return (restoreLeftBalance(old_balance));
        }
    }

    /**
     * Restores balance of the left subtree after a node removal.
     *
     * @param old_balance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode restoreLeftBalance(byte old_balance) {
        // fix balance factors
        if (left == null) {
            balance++;
        } else if ((left.balance != old_balance) && (left.balance == 0)) {
            balance++;
        }

        // rebalance if necessary
        if (balance > 1) {
            return (rebalanceNode());
        } else {
            return (this);
        }
    }

    /**
     * Restores balance of the right subtree after a node removal.
     *
     * @param old_balance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected AVLNode restoreRightBalance(byte old_balance) {
        // fix balance factors
        if (right == null) {
            balance--;
        } else if ((right.balance != old_balance) && (right.balance == 0)) {
            balance--;
        }

        // rebalance if necessary
        if (balance < -1) {
            return (rebalanceNode());
        } else {
            return (this);
        }
    }
}
