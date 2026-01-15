package org.jscience.util;

import java.util.*;


/**
 * A class representing a binary relation. You can use this class to design
 * any transition network (compilators and the like for example). This would
 * be a good start for a trellis. It could also be used for a small neural
 * network or why not a Petri net.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can also build up trees with empty node contents with this class although you may prefer to use the class Tree
//you can think of this class as a sort of extended Map
public class BinaryRelation extends Object implements Relation, Cloneable {
    /** DOCUMENT ME! */
    private Set set;

/**
     * Creates a new BinaryRelation object.
     */
    public BinaryRelation() {
        set = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int getDimension() {
        return 2;
    }

    //you don't add the object directly, instead you add a NAry of dimension 2 containing the object
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean add(Object o) {
        NAry nAry;

        if (o != null) {
            if (o instanceof NAry) {
                nAry = (NAry) o;

                if (nAry.getDimension() == 2) {
                    return set.add(o); //this should prevent any duplicate element
                } else {
                    throw new IllegalArgumentException(
                        "NAry element must be of dimension 2.");
                }
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean add(NAry nAry) {
        if (nAry != null) {
            if (nAry.getDimension() == 2) {
                return set.add(nAry);
            } else {
                throw new IllegalArgumentException(
                    "NAry element must be of dimension 2.");
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean add(Object[] o) {
        return add(new NAry(o));
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        set.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Iterator iterator;
        BinaryRelation result;

        //result = (BinaryRelation) super.clone();
        result = new BinaryRelation();
        iterator = set.iterator();

        while (iterator.hasNext()) {
            result.add((NAry) iterator.next());
        }

        return result;
    }

    //signature kept for compatibility
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean contains(Object o) {
        //this implementation tests for NAry which may prove faster or slower depending on lower implementation
        if (o != null) {
            if ((o instanceof NAry) && (((NAry) o).getDimension() == 2)) {
                return set.contains(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean contains(NAry nAry) {
        if (nAry != null) {
            return set.contains(nAry);
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Object[] o) {
        return contains(new NAry(o));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return set.iterator();
    }

    //signature kept for compatibility
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean remove(Object o) {
        //this implementation tests for NAry which may prove faster or slower depending on lower implementation
        if (o != null) {
            if ((o instanceof NAry) && (((NAry) o).getDimension() == 2)) {
                return set.remove(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nAry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean remove(NAry nAry) {
        if (nAry != null) {
            return set.remove(nAry);
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean remove(Object[] o) {
        return set.remove(new NAry(o));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return set.size();
    }

    //adds all the elements contained in r
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean addAll(Relation r) {
        Iterator iterator;
        boolean result;

        if (r != null) {
            if (r instanceof BinaryRelation) {
                result = false;
                iterator = r.iterator();

                while (iterator.hasNext()) {
                    result = result || set.add((NAry) iterator.next());
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //collection must be a BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean addAll(Collection c) {
        Iterator iterator;
        boolean result;

        if (c != null) {
            if (c instanceof BinaryRelation) {
                result = false;
                iterator = ((BinaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.add((NAry) iterator.next()); //this should prevent any duplicate element
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean containsAll(Relation r) {
        Iterator iterator;
        boolean result;

        if (r != null) {
            if (r instanceof BinaryRelation) {
                result = true;
                iterator = r.iterator();

                while (iterator.hasNext()) {
                    result = result && set.contains(iterator.next());
                }

                return result;
            } else {
                return true;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //collection must be a BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean containsAll(Collection c) {
        Iterator iterator;
        boolean result;

        if (c != null) {
            if (c instanceof BinaryRelation) {
                result = true;
                iterator = ((BinaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result && set.contains(iterator.next());
                }

                return result;
            } else {
                return true;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean removeAll(Relation r) {
        Iterator iterator;
        boolean result;

        if (r != null) {
            if (r instanceof BinaryRelation) {
                result = false;
                iterator = r.iterator();

                while (iterator.hasNext()) {
                    result = result || set.remove(iterator.next());
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //collection must be a BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean removeAll(Collection c) {
        Iterator iterator;
        boolean result;

        if (c != null) {
            if (c instanceof BinaryRelation) {
                result = false;
                iterator = ((BinaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.remove(iterator.next());
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //refuse null element
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean retainAll(Relation r) {
        Iterator iterator;
        boolean result;
        NAry currentElement;

        if (r != null) {
            if (r instanceof BinaryRelation) {
                result = false;
                iterator = iterator();

                while (iterator.hasNext()) {
                    currentElement = (NAry) iterator.next();

                    if (!r.contains(currentElement)) {
                        result = result || set.remove(currentElement);
                    }
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    //collection must be a BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public boolean retainAll(Collection c) {
        Iterator iterator;
        boolean result;
        NAry currentElement;
        BinaryRelation binaryRelation;

        if (c != null) {
            if (c instanceof BinaryRelation) {
                result = false;
                iterator = iterator();
                binaryRelation = (BinaryRelation) c;

                while (iterator.hasNext()) {
                    currentElement = (NAry) iterator.next();

                    if (!binaryRelation.contains(currentElement)) {
                        result = result || set.remove(currentElement);
                    }
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "BinaryRelations don't support null elements.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray() {
        return set.toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray(NAry[] a) {
        return set.toArray(a);
    }

    //I am not sure about the meaning of this in the spec
    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray(Object[] a) {
        return set.toArray(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return set.toString();
    }

    //all elements must be the same
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        return (o != null) && (o instanceof BinaryRelation) &&
        (size() == ((BinaryRelation) o).size()) &&
        contains((BinaryRelation) o);
    }

    //we do not throw any error for invalid arguments (expected behavior)
    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Object object, int i) {
        boolean found;
        Iterator iterator;

        found = false;

        if ((i == 0) || (i == 1)) {
            iterator = iterator();

            while (iterator.hasNext() && !found) {
                found = object.equals(((NAry) iterator.next()).getValue(i));
            }
        }

        return found;
    }

    //this method should actually return the HashSet of elements itself
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAll() {
        HashSet result;
        Iterator iterator;
        NAry currentElement;

        result = new HashSet();
        iterator = iterator();

        while (iterator.hasNext()) {
            currentElement = (NAry) iterator.next();
            result.add(currentElement.getValue(0));
            result.add(currentElement.getValue(1));
        }

        return result;
    }

    //i must be equal to 0 or 1 or an error is thrown
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Set get(int i) {
        HashSet result;
        Iterator iterator;

        if ((i == 0) || (i == 1)) {
            result = new HashSet();
            iterator = iterator();

            while (iterator.hasNext()) {
                result.add(((NAry) iterator.next()).getValue(i));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get elements for dimension 0-1.");
        }
    }

    //i must be equal to 0 or 1 or an error is thrown
    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Set getElements(Object object, int i) {
        HashSet result;
        Iterator iterator;
        NAry currentElement;

        if ((i == 0) || (i == 1)) {
            result = new HashSet();
            iterator = iterator();

            while (iterator.hasNext()) {
                currentElement = (NAry) iterator.next();

                if (object.equals(currentElement.getValue(i))) {
                    result.add(currentElement);
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get elements for dimension 0-1.");
        }
    }

    //isReflexive is true if and only if for all element A there is (A,A) in the BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isReflexive() {
        boolean result;
        Iterator iterator;
        Object currentElement;
        Object[] value;

        result = true;
        iterator = getAll().iterator();
        value = new Object[2];

        while (iterator.hasNext() && result) {
            currentElement = iterator.next();
            value[0] = currentElement;
            value[1] = currentElement;
            result = contains(new NAry(value));
        }

        return result;
    }

    //is symetric is true if and only if for all couples (A,B) there is also a couple (B,A)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSymetric() {
        boolean result;
        Iterator iterator;
        NAry currentElement;
        Object[] value;

        //we could have a better algorithm here marking all already visited elements and their reverse
        //this would be twice as fast
        result = true;
        iterator = iterator();
        value = new Object[2];

        while (iterator.hasNext() && result) {
            currentElement = (NAry) iterator.next();
            value[0] = currentElement.getValue(1);
            value[1] = currentElement.getValue(0);
            result = contains(new NAry(value));
        }

        return result;
    }

    //true if the BinaryRelation is such that for all (A,B) and (B,C) there is also (A,C)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTransitive() {
        BinaryRelation result;
        Iterator iterator1;
        Iterator iterator2;
        NAry currentElement1;
        NAry currentElement2;
        Object[] value;

        //there is probably a better algorithm
        result = new BinaryRelation();
        iterator1 = iterator();

        //build the couples we are looking for, in n*n time
        while (iterator1.hasNext()) {
            currentElement1 = (NAry) iterator1.next();
            iterator2 = iterator();

            while (iterator2.hasNext()) {
                currentElement2 = (NAry) iterator2.next();

                if (currentElement2.getValue(0)
                                       .equals(currentElement1.getValue(1))) {
                    value = new Object[2];
                    value[0] = currentElement2.getValue(1);
                    value[1] = currentElement1.getValue(0);
                    result.add(new NAry(value));
                }
            }
        }

        //check these couples are in this BinaryRelation, in n*n time
        return containsAll(result);
    }

    //is true if and only if all elements on the i dimension appear only once
    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isFunction(int i) {
        HashSet hashSet;
        boolean result;
        Iterator iterator;
        NAry currentElement;

        if ((i == 0) || (i == 1)) {
            hashSet = new HashSet();
            result = true;
            iterator = iterator();

            while (iterator.hasNext() && result) {
                currentElement = (NAry) iterator.next();
                result = !hashSet.contains(currentElement.getValue(i));
                hashSet.add(currentElement.getValue(i));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get the function for projection on 0 to getDimension()-1.");
        }
    }

    //build a Map out of this BinaryRelation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassCastException DOCUMENT ME!
     */
    public Map getMap() {
        Hashtable hashtable;
        boolean result;
        Iterator iterator;
        NAry currentElement;

        hashtable = new Hashtable();
        result = true;
        iterator = iterator();

        while (iterator.hasNext() && result) {
            currentElement = (NAry) iterator.next();
            result = !hashtable.containsKey(currentElement.getValue(0));
            hashtable.put(currentElement.getValue(0), currentElement.getValue(1));
        }

        if (result) {
            return hashtable;
        } else {
            throw new ClassCastException(
                "Cannot build a Map out of a BinaryRelation that is not a function.");
        }
    }

    //if all elements on the left side and on the right side appear only once
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBijection() {
        HashSet hashSet1;
        HashSet hashSet2;
        boolean result;
        Iterator iterator;
        NAry currentElement;

        hashSet1 = new HashSet();
        hashSet2 = new HashSet();
        result = true;
        iterator = iterator();

        while (iterator.hasNext() && result) {
            currentElement = (NAry) iterator.next();
            result = !(hashSet1.contains(currentElement.getValue(0)) ||
                hashSet2.contains(currentElement.getValue(1)));
            hashSet1.add(currentElement.getValue(0));
            hashSet2.add(currentElement.getValue(1));
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BinaryRelation getInverse() {
        BinaryRelation result;
        Iterator iterator;
        NAry currentElement;
        Object[] value;

        result = new BinaryRelation();
        iterator = set.iterator();
        value = new Object[2];

        while (iterator.hasNext()) {
            currentElement = (NAry) iterator.next();
            value[0] = currentElement.getValue(1);
            value[1] = currentElement.getValue(0);
            result.add(new NAry(value));
        }

        return result;
    }

    //we could also provide several trellis methods that I don't have in mind
}
