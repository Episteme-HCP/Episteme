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

package org.jscience.util;

import java.util.*;


/**
 * A class representing a relation in 1 dimension, this is a Set
 * implementation.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//mainly for completion, you will probably prefer to use a Set directly
public class UnaryRelation extends Object implements Relation, Cloneable {
    /** DOCUMENT ME! */
    private Set set;

/**
     * Creates a new UnaryRelation object.
     */
    public UnaryRelation() {
        set = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int getDimension() {
        return 1;
    }

    //ytou don't add the object directly, instead you add a NAry of dimension 1 containing the object
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

                if (nAry.getDimension() == 1) {
                    return set.add(o); //this should prevent any duplicate element
                } else {
                    throw new IllegalArgumentException(
                        "NAry element must be of dimension 1.");
                }
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
            if (nAry.getDimension() == 1) {
                return set.add(nAry);
            } else {
                throw new IllegalArgumentException(
                    "NAry element must be of dimension 1.");
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
        UnaryRelation result;

        result = new UnaryRelation();

        //result = (UnaryRelation)super.clone();
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
            if (o instanceof NAry) {
                return set.contains(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
                "UnaryRelations don't support null elements.");
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
            if (o instanceof NAry) {
                return set.remove(o); //this should prevent any duplicate element
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
                "UnaryRelations don't support null elements.");
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
            if (r instanceof UnaryRelation) {
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
                "UnaryRelations don't support null elements.");
        }
    }

    //collection must be a UnaryRelation
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
            if (c instanceof UnaryRelation) {
                result = false;
                iterator = ((UnaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.add((NAry) iterator.next()); //this should prevent any duplicate element
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
            if (r instanceof UnaryRelation) {
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
                "UnaryRelations don't support null elements.");
        }
    }

    //collection must be a UnaryRelation
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
            if (c instanceof UnaryRelation) {
                result = true;
                iterator = ((UnaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result && set.contains(iterator.next());
                }

                return result;
            } else {
                return true;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
            if (r instanceof UnaryRelation) {
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
                "UnaryRelations don't support null elements.");
        }
    }

    //collection must be a UnaryRelation
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
            if (c instanceof UnaryRelation) {
                result = false;
                iterator = ((UnaryRelation) c).iterator();

                while (iterator.hasNext()) {
                    result = result || set.remove(iterator.next());
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
            if (r instanceof UnaryRelation) {
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
                "UnaryRelations don't support null elements.");
        }
    }

    //collection must be a UnaryRelation
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
        UnaryRelation unaryRelation;

        if (c != null) {
            if (c instanceof UnaryRelation) {
                result = false;
                iterator = iterator();
                unaryRelation = (UnaryRelation) c;

                while (iterator.hasNext()) {
                    currentElement = (NAry) iterator.next();

                    if (!unaryRelation.contains(currentElement)) {
                        result = result || set.remove(currentElement);
                    }
                }

                return result;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException(
                "UnaryRelations don't support null elements.");
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
        return (o != null) && (o instanceof UnaryRelation) &&
        (size() == ((UnaryRelation) o).size()) && contains((UnaryRelation) o);
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
        iterator = iterator();

        while (iterator.hasNext() && !found) {
            found = object.equals(((NAry) iterator.next()).getValue(0));
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

        result = new HashSet();
        iterator = iterator();

        while (iterator.hasNext()) {
            result.add(((NAry) iterator.next()).getValue(0));
        }

        return result;
    }

    //i must be equal to 0 or an error is thrown
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

        if (i == 0) {
            result = new HashSet();
            iterator = iterator();

            while (iterator.hasNext()) {
                result.add(((NAry) iterator.next()).getValue(0));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get elements for dimension 0.");
        }
    }

    //i must be equal to 0 or an error is thrown
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

        if (i == 0) {
            result = new HashSet();
            iterator = iterator();

            while (iterator.hasNext()) {
                currentElement = (NAry) iterator.next();

                if (object.equals(currentElement.getValue(0))) {
                    result.add(currentElement);
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can only get elements for dimension 0.");
        }
    }

    //returns false since we need at least two dimensions
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
        if (i == 0) {
            return false;
        } else {
            throw new IllegalArgumentException(
                "You can only get the function for projection on 0 to getDimension()-1.");
        }
    }
}
