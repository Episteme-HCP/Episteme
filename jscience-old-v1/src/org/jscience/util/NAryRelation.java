package org.jscience.util;

import java.util.*;


/**
 * A class representing a generalized relation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class NAryRelation extends Object implements Relation, Cloneable {
    /** DOCUMENT ME! */
    private Set set;

    /** DOCUMENT ME! */
    private int dimension;

/**
     * Creates a new NAryRelation object.
     *
     * @param dim DOCUMENT ME!
     */
    public NAryRelation(int dim) {
        set = Collections.EMPTY_SET;
        this.dimension = dim;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        return dimension;
    }

    //you don't add the object directly, instead you add a NAry of dimension this.getDimension() containing the object
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

                if (nAry.getDimension() == getDimension()) {
                    return set.add(o); //this should prevent any duplicate element
                } else {
                    throw new IllegalArgumentException(
                        "NAry element must be equal to getDimension().");
                }
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
            if (nAry.getDimension() == getDimension()) {
                return set.add(nAry);
            } else {
                throw new IllegalArgumentException(
                    "NAry element must be equal to getDimension().");
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
        NAryRelation result;

        result = new NAryRelation(getDimension());

        //result = (NAryRelation)super.clone();
        //result.dimension=getDimension();
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
            if ((o instanceof NAry) &&
                    (((NAry) o).getDimension() == getDimension())) {
                return set.contains(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
                "NAryRelations don't support null elements.");
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
            if ((o instanceof NAry) &&
                    (((NAry) o).getDimension() == getDimension())) {
                return set.remove(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
                "NAryRelations don't support null elements.");
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
            if (r instanceof NAryRelation) {
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
                "NAryRelations don't support null elements.");
        }
    }

    //collection must be a NAryRelation
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
            if (c instanceof NAryRelation) {
                result = false;
                iterator = ((NAryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.add((NAry) iterator.next()); //this should prevent any duplicate element
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
            if (r instanceof NAryRelation) {
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
                "NAryRelations don't support null elements.");
        }
    }

    //collection must be a NAryRelation
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
            if (c instanceof NAryRelation) {
                result = true;
                iterator = ((NAryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result && set.contains(iterator.next());
                }

                return result;
            } else {
                return true;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
            if (r instanceof NAryRelation) {
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
                "NAryRelations don't support null elements.");
        }
    }

    //collection must be a NAryRelation
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
            if (c instanceof NAryRelation) {
                result = false;
                iterator = ((NAryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.remove(iterator.next());
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
            if (r instanceof NAryRelation) {
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
                "NAryRelations don't support null elements.");
        }
    }

    //collection must be a NAryRelation
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
        NAryRelation nAryRelation;

        if (c != null) {
            if (c instanceof NAryRelation) {
                result = false;
                iterator = iterator();
                nAryRelation = (NAryRelation) c;

                while (iterator.hasNext()) {
                    currentElement = (NAry) iterator.next();

                    if (!nAryRelation.contains(currentElement)) {
                        result = result || set.remove(currentElement);
                    }
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "NAryRelations don't support null elements.");
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
        return (o != null) && (o instanceof NAryRelation) &&
        (size() == ((NAryRelation) o).size()) && contains((NAryRelation) o);
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

            for (int i = 0; i < getDimension(); i++) {
                result.add(currentElement.getValue(i));
            }
        }

        return result;
    }

    //i must be between 0 and this.getDimension() or an error is thrown
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

        if ((i >= 0) || (i < getDimension())) {
            result = new HashSet();
            iterator = iterator();

            while (iterator.hasNext()) {
                result.add(((NAry) iterator.next()).getValue(i));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get elements for dimension between 0 and this.getDimension().");
        }
    }

    //i must be between 0 and this.getDimension() or an error is thrown
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

        if ((i >= 0) || (i < getDimension())) {
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
                "You can only get elements for dimension between 0 and this.getDimension().");
        }
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

        if ((i >= 0) || (i < getDimension())) {
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
}
