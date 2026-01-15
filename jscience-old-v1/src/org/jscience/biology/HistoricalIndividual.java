package org.jscience.biology;

import org.jscience.psychology.social.Biography;

import org.jscience.util.CircularReferenceException;
import org.jscience.util.NAryTree;
import org.jscience.util.Tree;

import java.util.*;


/**
 * A class representing an individual as part of a genealogical tree. Also
 * used to represent the genealogical relations between individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//please note that you have to manage by yourself addition and deletion of individuals from a population
//a general behavior (not choosen by all cultures, though) is to get rid of dead people in a way or another
//therefore you should call population.addIndividual() when birthdate of an HistoricalIndividual is reached
//and call population.removeIndividual() when when deathdate of an HistoricalIndividual is reached
//this assumes that you have a scheduler managing a timed population and a Set of "potentially living individuals"
public class HistoricalIndividual extends Individual {
    /** DOCUMENT ME! */
    private Date dateOfBirth;

    /** DOCUMENT ME! */
    private Date dateOfDeath;

    /** DOCUMENT ME! */
    private Set parents; //double linked

    /** DOCUMENT ME! */
    private Set children;

    /** DOCUMENT ME! */
    private Biography biography;

    //it is assumed the HistoricalIndividual is still alive at current system time
    /**
     * Creates a new HistoricalIndividual object.
     *
     * @param species DOCUMENT ME!
     * @param dateOfBirth DOCUMENT ME!
     */
    public HistoricalIndividual(Species species, Date dateOfBirth) {
        super(species);

        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
            this.dateOfDeath = null;
            parents = Collections.EMPTY_SET;
            children = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException("Date of birth can't be null.");
        }
    }

    //the recommended constructor
    /**
     * Creates a new HistoricalIndividual object.
     *
     * @param species DOCUMENT ME!
     * @param dateOfBirth DOCUMENT ME!
     * @param dateOfDeath DOCUMENT ME!
     */
    public HistoricalIndividual(Species species, Date dateOfBirth,
        Date dateOfDeath) {
        super(species);

        if ((dateOfBirth != null) && (dateOfDeath != null)) {
            if (dateOfBirth.before(dateOfDeath)) {
                this.dateOfBirth = dateOfBirth;
                this.dateOfDeath = dateOfDeath;
                parents = Collections.EMPTY_SET;
                children = Collections.EMPTY_SET;
            } else {
                throw new IllegalArgumentException(
                    "Date of birth must be before date of death.");
            }
        } else {
            throw new IllegalArgumentException(
                "Date of birth and date of death can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    //must happen before death date
    /**
     * DOCUMENT ME!
     *
     * @param dateOfBirth DOCUMENT ME!
     */
    public void setDateOfBirth(Date dateOfBirth) {
        if ((dateOfBirth != null) && (dateOfBirth.before(dateOfDeath))) {
            this.dateOfBirth = dateOfBirth;
        } else {
            throw new IllegalArgumentException(
                "Date of birth must be before date of death.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    //must happen after birth date
    /**
     * DOCUMENT ME!
     *
     * @param dateOfDeath DOCUMENT ME!
     */
    public void setDateOfDeath(Date dateOfDeath) {
        if ((dateOfDeath != null) && (dateOfBirth.before(dateOfDeath))) {
            this.dateOfDeath = dateOfDeath;
        } else {
            throw new IllegalArgumentException(
                "Date of birth must be before date of death.");
        }
    }

    //Biography is defined as a separate class as people may be only interested in the genealogical relations
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Biography getBiography() {
        return biography;
    }

    /**
     * DOCUMENT ME!
     *
     * @param biography DOCUMENT ME!
     */
    public void setBiography(Biography biography) {
        this.biography = biography;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild() {
        return !children.isEmpty();
    }

    //if hIndividual is one of the children of this
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild(HistoricalIndividual child) {
        return children.contains(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        return children;
    }

    //all elements should be of class HistoricalIndividual
    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setChildren(Set children)
        throws CircularReferenceException, IllegalArgumentException {
        Iterator iterator;
        Object currentElement;
        HistoricalIndividual child;

        if (children != null) {
            iterator = children.iterator();

            while (iterator.hasNext()) {
                currentElement = iterator.next();

                if (currentElement instanceof HistoricalIndividual) {
                    child = (HistoricalIndividual) currentElement;

                    if (child.getParents().size() < 2) {
                        if ((child != this) && (!child.hasDistantChild(this))) {
                            child.addParent(this);
                        } else {
                            throw new CircularReferenceException(
                                "Cannot add HistoricalIndividual child that is a parent.");
                        }
                    } else {
                        throw new CircularReferenceException(
                            "Cannot add HistoricalIndividual child that has already both parents.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The children Set must contain only HistoricalIndividuals.");
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
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addChild(HistoricalIndividual child)
        throws CircularReferenceException, IllegalArgumentException {
        if (child != null) {
            if (child.getParents().size() < 2) {
                if ((child != this) && (!child.hasDistantChild(this))) {
                    child.addParent(this);
                    children.add(child);
                } else {
                    throw new CircularReferenceException(
                        "Cannot add HistoricalIndividual child that is a parent.");
                }
            } else {
                throw new CircularReferenceException(
                    "Cannot add HistoricalIndividual child that has already both parents.");
            }
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeChild(HistoricalIndividual child) {
        if (child != null) {
            child.removeParent(this);
            children.remove(child);
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    //checks for direct parent
    /**
     * DOCUMENT ME!
     *
     * @param hIndividual DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isParent(HistoricalIndividual hIndividual) {
        return parents.contains(hIndividual);
    }

    //could also be called isRoot()
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasParent() {
        return parents.size() != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasBothParents() {
        return parents.size() == 2;
    }

    //automatically set by laying children
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParents() {
        return parents;
    }

    //you must have checked before hand that valid conditions are applied
    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    private void addParent(HistoricalIndividual parent) {
        parents.add(parent);
    }

    //you must have checked before hand that valid conditions are applied
    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    private void removeParent(HistoricalIndividual parent) {
        parents.remove(parent);
    }

    //oldParent must be a parent of this
    /**
     * DOCUMENT ME!
     *
     * @param oldParent DOCUMENT ME!
     * @param newParent DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void reparent(HistoricalIndividual oldParent,
        HistoricalIndividual newParent) throws IllegalArgumentException {
        if ((oldParent != null) && (newParent != null)) {
            if (parents.contains(oldParent)) {
                oldParent.removeChild(this);
                newParent.addChild(this);
            } else {
                throw new IllegalArgumentException(
                    "Cannot reparent HistoricalIndividual whose oldParent is not a parent.");
            }
        } else {
            throw new IllegalArgumentException(
                "The oldParent and newParent must be not null.");
        }
    }

    //returns all the individuals who historicalIndividual had children with
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getMates() {
        HashSet result;
        Iterator iterator;

        iterator = children.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((HistoricalIndividual) iterator.next()).getParents());
        }

        result.remove(this);

        return result;
    }

    //returns me, my children and my mates in one set
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getFamily() {
        HashSet result;
        Iterator iterator;

        iterator = children.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((HistoricalIndividual) iterator.next()).getParents());
        }

        result.addAll(children);

        return result;
    }

    //the father or mother of all
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRoots() {
        HashSet hashSet;

        hashSet = new HashSet();
        hashSet.add(this);

        return getRoots(hashSet, 0);
    }

    //the fathers or mothers of all
    /**
     * DOCUMENT ME!
     *
     * @param hIndividual DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Set getRoots(HistoricalIndividual hIndividual) {
        Set elements;
        HashSet hashSet;

        if (hIndividual != null) {
            hashSet = new HashSet();
            hashSet.add(hIndividual);
            elements = getRoots(hashSet, 0);
        } else {
            elements = null;
        }

        return elements;
    }

    /**
     * DOCUMENT ME!
     *
     * @param individuals DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Set getRoots(Set individuals, int n) {
        Set result;
        Iterator iterator;

        result = new HashSet();
        iterator = individuals.iterator();

        while (iterator.hasNext()) {
            result.addAll(((HistoricalIndividual) iterator.next()).getParents());
        }

        if (result.size() == 0) {
            return individuals;
        } else {
            return getRoots(result, n + 1);
        }
    }

    //the common parent
    /**
     * DOCUMENT ME!
     *
     * @param hIndividual1 DOCUMENT ME!
     * @param hIndividual2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static HistoricalIndividual getCommonRoot(
        HistoricalIndividual hIndividual1, HistoricalIndividual hIndividual2) {
        Vector lineage1;
        Vector lineage2;
        Set roots1;
        Set roots2;
        HistoricalIndividual result;
        int i;

        if ((hIndividual1 != null) && (hIndividual2 != null)) {
            roots1 = hIndividual1.getRoots();
            roots2 = hIndividual2.getRoots();

            if ((roots1.size() > 0) && (roots2.size() > 0) &&
                    (roots1.equals(roots2))) {
                lineage1 = getLineage(((HistoricalIndividual) roots1.iterator()
                                                                    .next()),
                        hIndividual1);
                lineage2 = getLineage(((HistoricalIndividual) roots1.iterator()
                                                                    .next()),
                        hIndividual2);
                i = 1;

                while ((i < Math.min(lineage1.size(), lineage2.size())) &&
                        lineage1.get(i).equals(lineage2.get(i))) {
                    i++;
                }

                result = (HistoricalIndividual) lineage1.get(i - 1);
            } else {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    //get all the children from historicalIndividual1 to historicalIndividual2
    /**
     * DOCUMENT ME!
     *
     * @param hIndividual1 DOCUMENT ME!
     * @param hIndividual2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector getLineage(HistoricalIndividual hIndividual1,
        HistoricalIndividual hIndividual2) {
        HistoricalIndividual element;
        Vector result;
        Vector tempResult1;
        Vector tempResult2;
        Iterator iterator;
        Set parents;

        if ((hIndividual1 != null) && (hIndividual2 != null)) {
            if (hIndividual2.equals(hIndividual1)) {
                result = new Vector();
                result.add(hIndividual1);
            } else {
                parents = hIndividual2.getParents();

                if (parents.size() == 0) {
                    result = new Vector();
                } else if (parents.size() == 1) {
                    iterator = parents.iterator();
                    tempResult1 = getLineage(hIndividual1,
                            (HistoricalIndividual) iterator.next());

                    result = new Vector();

                    if (tempResult1.size() > 0) {
                        result.addAll(tempResult1);
                        result.add(hIndividual2);
                    }
                } else {
                    iterator = parents.iterator();
                    tempResult1 = getLineage(hIndividual1,
                            (HistoricalIndividual) iterator.next());
                    tempResult2 = getLineage(hIndividual1,
                            (HistoricalIndividual) iterator.next());

                    if (tempResult1.size() > 0) {
                        result = new Vector();

                        if (tempResult2.size() > 0) {
                            //can this happen ???
                            if (tempResult2.size() > tempResult1.size()) {
                                result.addAll(tempResult1);
                            } else {
                                result.addAll(tempResult2);
                            }
                        } else {
                            result.addAll(tempResult1);
                        }

                        result.add(hIndividual2);
                    } else {
                        if (tempResult2.size() > 0) {
                            result = new Vector();
                            result.addAll(tempResult2);
                            result.add(hIndividual2);
                        } else {
                            result = new Vector();
                        }
                    }
                }
            }
        } else {
            result = null;
        }

        return result;
    }

    //returns the shortest and simpliest relation between two individuals
    /**
     * DOCUMENT ME!
     *
     * @param hIndividual1 DOCUMENT ME!
     * @param hIndividual2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Tree extractTree(HistoricalIndividual hIndividual1,
        HistoricalIndividual hIndividual2) {
        Vector lineage1;
        Vector lineage2;
        Set roots1;
        Set roots2;
        NAryTree currentTree1;
        NAryTree nextTree1;
        NAryTree currentTree2;
        NAryTree nextTree2;
        NAryTree result;
        int i;

        if ((hIndividual1 != null) && (hIndividual2 != null)) {
            roots1 = hIndividual1.getRoots();
            roots2 = hIndividual2.getRoots();

            if (roots1.equals(roots2)) {
                lineage1 = getLineage(((HistoricalIndividual) roots1.iterator()
                                                                    .next()),
                        hIndividual1);
                lineage2 = getLineage(((HistoricalIndividual) roots1.iterator()
                                                                    .next()),
                        hIndividual2);

                //find highest common element
                i = 1;

                if (lineage1.size() < lineage2.size()) {
                    while ((i < lineage1.size()) &&
                            lineage1.get(i).equals(lineage2.get(i))) {
                        i++;
                    }

                    //build a new simple tree
                    result = new NAryTree(((HistoricalIndividual) lineage1.get(i -
                                1)));
                    currentTree1 = result;
                    currentTree2 = result;

                    while (i < lineage1.size()) {
                        nextTree1 = new NAryTree(((HistoricalIndividual) lineage1.get(
                                    i)));
                        currentTree1.addChild(nextTree1);
                        currentTree1 = nextTree1;
                        nextTree2 = new NAryTree(((HistoricalIndividual) lineage2.get(
                                    i)));
                        currentTree2.addChild(nextTree2);
                        currentTree2 = nextTree2;
                        i++;
                    }

                    while (i < lineage2.size()) {
                        nextTree2 = new NAryTree(((HistoricalIndividual) lineage2.get(
                                    i)));
                        currentTree2.addChild(nextTree2);
                        currentTree2 = nextTree2;
                        i++;
                    }
                } else {
                    while ((i < lineage1.size()) &&
                            lineage1.get(i).equals(lineage2.get(i))) {
                        i++;
                    }

                    //build a new simple tree
                    result = new NAryTree(((HistoricalIndividual) lineage1.get(i -
                                1)));
                    currentTree1 = result;
                    currentTree2 = result;

                    while (i < lineage2.size()) {
                        nextTree1 = new NAryTree(((HistoricalIndividual) lineage1.get(
                                    i)));
                        currentTree1.addChild(nextTree1);
                        currentTree1 = nextTree1;
                        nextTree2 = new NAryTree(((HistoricalIndividual) lineage2.get(
                                    i)));
                        currentTree2.addChild(nextTree2);
                        currentTree2 = nextTree2;
                        i++;
                    }

                    while (i < lineage1.size()) {
                        nextTree1 = new NAryTree(((HistoricalIndividual) lineage1.get(
                                    i)));
                        currentTree1.addChild(nextTree1);
                        currentTree1 = nextTree1;
                        i++;
                    }
                }
            } else {
                result = new NAryTree();
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
            newChildren.addAll(((HistoricalIndividual) iterator.next()).getChildren());
        }

        while (newChildren.size() != 0) {
            result.addAll(newChildren);
            currentChildren = newChildren;
            newChildren = new HashSet();
            iterator = currentChildren.iterator();

            while (iterator.hasNext()) {
                newChildren.addAll(((HistoricalIndividual) iterator.next()).getChildren());
            }
        }

        return result;
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
    public boolean hasDistantChild(HistoricalIndividual child) {
        Set currentChildren;
        Set tempChildren;
        HashSet newChildren;
        Iterator iterator;
        boolean result;

        if (child != null) {
            currentChildren = children;
            newChildren = new HashSet();
            result = children.contains(child);
            iterator = currentChildren.iterator();

            while (iterator.hasNext() && !result) {
                tempChildren = ((HistoricalIndividual) iterator.next()).getChildren();
                result = tempChildren.contains(child);
                newChildren.addAll(tempChildren);
            }

            while ((newChildren.size() != 0) && !result) {
                currentChildren = newChildren;
                newChildren = new HashSet();
                iterator = currentChildren.iterator();

                while (iterator.hasNext() && !result) {
                    tempChildren = ((HistoricalIndividual) iterator.next()).getChildren();
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
        HistoricalIndividual value;

        if ((o != null) && (o instanceof HistoricalIndividual)) {
            value = (HistoricalIndividual) o;

            return super.equals(value) &&
            getDateOfBirth().equals(value.getDateOfBirth()) &&
            getDateOfDeath().equals(value.getDateOfDeath()) &&
            getChildren().equals(value.getChildren()) &&
            getParents().equals(value.getParents()) &&
            getBiography().equals(value.getBiography());
        } else {
            return false;
        }
    }
}
