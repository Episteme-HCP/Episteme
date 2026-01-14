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

package org.jscience.economics;

import org.jscience.biology.Individual;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import org.jscience.sociology.Role;

import org.jscience.util.CircularReferenceException;
import org.jscience.util.NAryTree;
import org.jscience.util.Tree;

import java.util.*;


/**
 * A class representing a worker in an organization. This class also
 * accounts for the real flow of decision in an organization.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//although a Tree structure could account for the structure of many small organizations, the "real life" organization have very loosy bonds where one individual can be at the same time a chief and a subaltern
//a worker makes a request
//it is propagated throught chiefs and validated
//it then comes back by being propagated to subalterns
//may be an implementation of the hierarchical relations as a BinaryRelation may prove to be more fruitful although we have made another choice
//we could also provide a method to export the relations as a Graph
//this class is mostly about modern work among Humans
//it can also define hierarchical relations between animals
//in which case annualIncome, workedHours, organization sqhould be set to Zero value
//more probably you will simply want to use the Role/Situation system provided in org.jscience.sociology
public class Worker extends EconomicAgent {
    /** DOCUMENT ME! */
    private String function; //the recognized main function/title in the organization

    /** DOCUMENT ME! */
    private Amount<Money> annualIncome;

    /** DOCUMENT ME! */
    private double workedHours; //per year

    /** DOCUMENT ME! */
    private Set chiefs; //may have multiple chiefs, chiefs are computed from subalterns

    /** DOCUMENT ME! */
    private Set subalterns;

    /** DOCUMENT ME! */
    private Organization organization;

/**
     * Creates a new Worker object.
     *
     * @param individual    DOCUMENT ME!
     * @param workSituation DOCUMENT ME!
     * @param function      DOCUMENT ME!
     * @param organization  DOCUMENT ME!
     */
    public Worker(Individual individual, WorkSituation workSituation,
        String function, Organization organization) {
        super(individual, "Worker", workSituation, Role.SERVER);

        if ((function != null) && (function.length() > 0) &&
                (annualIncome != null) && (organization != null)) {
            this.function = function;
            this.annualIncome = Amount.valueOf(0, Currency.USD);
            this.workedHours = -1;
            chiefs = Collections.EMPTY_SET;
            subalterns = Collections.EMPTY_SET;
            this.organization = organization;
        } else {
            throw new IllegalArgumentException(
                "The Worker constructor can't have null arguments (and function can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFunction() {
        return function;
    }

    /**
     * DOCUMENT ME!
     *
     * @param function DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setFunction(String function) {
        if ((function != null) && (function.length() > 0)) {
            this.function = function;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty function.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getAnnualIncome() {
        return annualIncome;
    }

    /**
     * DOCUMENT ME!
     *
     * @param annualIncome DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAnnualIncome(Amount<Money> annualIncome) {
        if (annualIncome != null) {
            this.annualIncome = annualIncome;
        } else {
            throw new IllegalArgumentException("You can't set a null income.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWorkedHours() {
        return workedHours;
    }

    /**
     * DOCUMENT ME!
     *
     * @param workedHours DOCUMENT ME!
     */
    public void setWorkedHours(double workedHours) {
        this.workedHours = workedHours;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild() {
        return !subalterns.isEmpty();
    }

    //if tree is one of the children of this
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChild(Worker child) {
        return subalterns.contains(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSubalterns() {
        return subalterns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param subalterns DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSubalterns(Set subalterns)
        throws CircularReferenceException, IllegalArgumentException {
        Iterator iterator;
        Object currentElement;
        Worker child;

        if (subalterns != null) {
            iterator = subalterns.iterator();

            while (iterator.hasNext()) {
                currentElement = iterator.next();

                if (currentElement instanceof Worker) {
                    child = (Worker) currentElement;

                    if ((child != this) && (!child.hasDistantSubaltern(this))) {
                        child.addChief(this);
                    } else {
                        throw new CircularReferenceException(
                            "Cannot add Worker child that is a parent.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "The Set of subalterns must contain only Workers.");
                }
            }

            this.subalterns = subalterns;
        } else {
            throw new IllegalArgumentException(
                "The children Set must be not null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addSubaltern(Worker child)
        throws CircularReferenceException, IllegalArgumentException {
        if (child != null) {
            if ((child != this) && (!child.hasDistantSubaltern(this))) {
                child.addChief(this);
                subalterns.add(child);
            } else {
                throw new CircularReferenceException(
                    "Cannot add Worker child that is a parent.");
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
    public void removeSubaltern(Worker child) {
        if (child != null) {
            child.removeChief(this);
            subalterns.remove(child);
        } else {
            throw new IllegalArgumentException("The child must be not null.");
        }
    }

    //isRoot() == !hasChief();
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChief() {
        return chiefs.size() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChiefs() {
        return chiefs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    private void addChief(Worker parent) {
        chiefs.add(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    private void removeChief(Worker parent) {
        chiefs.remove(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param oldParent DOCUMENT ME!
     * @param newParent DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void reparent(Worker oldParent, Worker newParent)
        throws IllegalArgumentException {
        if ((oldParent != null) && (newParent != null)) {
            if (chiefs.contains(oldParent)) {
                oldParent.removeSubaltern(this);
                newParent.addSubaltern(this);
            } else {
                throw new IllegalArgumentException(
                    "Cannot reparent Worker whose oldParent is not a parent.");
            }
        } else {
            throw new IllegalArgumentException(
                "The oldParent and newParent must be not null.");
        }
    }

    //the root workers
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getLeaders() {
        HashSet hashSet;

        hashSet = new HashSet();
        hashSet.add(this);

        return getLeaders(hashSet, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param individuals DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Set getLeaders(Set individuals, int n) {
        Set result;
        Iterator iterator;

        result = new HashSet();
        iterator = individuals.iterator();

        while (iterator.hasNext()) {
            result.addAll(((Worker) iterator.next()).getChiefs());
        }

        if (result.size() == 0) {
            return individuals;
        } else {
            return getLeaders(result, n + 1);
        }
    }

    //the Set of people with whom you are normally working as you share at least one chief
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCoWorkers() {
        Iterator iterator;
        HashSet result;

        iterator = getChiefs().iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((Worker) iterator.next()).getSubalterns());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param worker1 DOCUMENT ME!
     * @param worker2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Worker getCommonRoot(Worker worker1, Worker worker2) {
        Vector lineage1;
        Vector lineage2;
        Set roots1;
        Set roots2;
        Worker result;
        int i;

        if ((worker1 != null) && (worker2 != null)) {
            roots1 = worker1.getLeaders();
            roots2 = worker2.getLeaders();

            if ((roots1.size() > 0) && (roots2.size() > 0) &&
                    (roots1.equals(roots2))) {
                lineage1 = getLineage(((Worker) roots1.iterator().next()),
                        worker1);
                lineage2 = getLineage(((Worker) roots1.iterator().next()),
                        worker2);
                i = 1;

                while ((i < Math.min(lineage1.size(), lineage2.size())) &&
                        lineage1.get(i).equals(lineage2.get(i))) {
                    i++;
                }

                result = (Worker) lineage1.get(i - 1);
            } else {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    //get all the children from worker1 to worker2
    /**
     * DOCUMENT ME!
     *
     * @param worker1 DOCUMENT ME!
     * @param worker2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Vector getLineage(Worker worker1, Worker worker2) {
        Vector result;
        Vector tempResult;
        Iterator iterator;
        boolean found;
        Set parents;

        if ((worker1 != null) && (worker2 != null)) {
            if (worker2.equals(worker1)) {
                result = new Vector();
                result.add(worker1);
            } else {
                parents = worker2.getChiefs();
                result = new Vector();

                if (parents.size() != 0) {
                    iterator = parents.iterator();
                    found = false;
                    tempResult = new Vector();

                    while (iterator.hasNext() && !found) {
                        tempResult = getLineage(worker1,
                                (Worker) iterator.next());
                        found = tempResult.size() > 0;
                    }

                    if (found) {
                        result = new Vector();
                        result.addAll(tempResult);
                        result.add(worker2);
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
     * @param worker1 DOCUMENT ME!
     * @param worker2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Tree extractTree(Worker worker1, Worker worker2) {
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

        if ((worker1 != null) && (worker2 != null)) {
            roots1 = worker1.getLeaders();
            roots2 = worker2.getLeaders();

            if (roots1.equals(roots2)) {
                lineage1 = getLineage(((Worker) roots1.iterator().next()),
                        worker1);
                lineage2 = getLineage(((Worker) roots1.iterator().next()),
                        worker2);

                //find highest common element
                i = 1;

                if (lineage1.size() < lineage2.size()) {
                    while ((i < lineage1.size()) &&
                            lineage1.get(i).equals(lineage2.get(i))) {
                        i++;
                    }

                    //build a new simple tree
                    result = new NAryTree(((Worker) lineage1.get(i - 1)));
                    currentTree1 = result;
                    currentTree2 = result;

                    while (i < lineage1.size()) {
                        nextTree1 = new NAryTree(((Worker) lineage1.get(i)));
                        currentTree1.addChild(nextTree1);
                        currentTree1 = nextTree1;
                        nextTree2 = new NAryTree(((Worker) lineage2.get(i)));
                        currentTree2.addChild(nextTree2);
                        currentTree2 = nextTree2;
                        i++;
                    }

                    while (i < lineage2.size()) {
                        nextTree2 = new NAryTree(((Worker) lineage2.get(i)));
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
                    result = new NAryTree(((Worker) lineage1.get(i - 1)));
                    currentTree1 = result;
                    currentTree2 = result;

                    while (i < lineage2.size()) {
                        nextTree1 = new NAryTree(((Worker) lineage1.get(i)));
                        currentTree1.addChild(nextTree1);
                        currentTree1 = nextTree1;
                        nextTree2 = new NAryTree(((Worker) lineage2.get(i)));
                        currentTree2.addChild(nextTree2);
                        currentTree2 = nextTree2;
                        i++;
                    }

                    while (i < lineage1.size()) {
                        nextTree1 = new NAryTree(((Worker) lineage1.get(i)));
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllSubalterns() {
        Set currentChildren;
        HashSet newChildren;
        Iterator iterator;
        HashSet result;

        currentChildren = subalterns;
        newChildren = new HashSet();
        result = new HashSet();
        result.addAll(currentChildren);
        iterator = currentChildren.iterator();

        while (iterator.hasNext()) {
            newChildren.addAll(((Worker) iterator.next()).getSubalterns());
        }

        while (newChildren.size() != 0) {
            result.addAll(newChildren);
            currentChildren = newChildren;
            newChildren = new HashSet();
            iterator = currentChildren.iterator();

            while (iterator.hasNext()) {
                newChildren.addAll(((Worker) iterator.next()).getSubalterns());
            }
        }

        return result;
    }

    //level 0 returns this
    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSubalternsAtLevelK(int k) {
        Iterator iterator;
        HashSet result;

        if (k > -1) {
            result = new HashSet();

            if (k == 0) {
                result.add(this);
            } else {
                iterator = getSubalterns().iterator();
                result.addAll(((Worker) iterator.next()).getSubalternsAtLevelK(k -
                        1));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "The depth level must be 0 or above.");
        }
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
    public boolean hasDistantSubaltern(Worker child) {
        Set currentChildren;
        Set tempChildren;
        HashSet newChildren;
        Iterator iterator;
        boolean result;

        if (child != null) {
            currentChildren = subalterns;
            newChildren = new HashSet();
            result = subalterns.contains(child);
            iterator = currentChildren.iterator();

            while (iterator.hasNext() && !result) {
                tempChildren = ((Worker) iterator.next()).getSubalterns();
                result = tempChildren.contains(child);
                newChildren.addAll(tempChildren);
            }

            while ((newChildren.size() != 0) && !result) {
                currentChildren = newChildren;
                newChildren = new HashSet();
                iterator = currentChildren.iterator();

                while (iterator.hasNext() && !result) {
                    tempChildren = ((Worker) iterator.next()).getSubalterns();
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
        Worker value;

        if ((o != null) && (o instanceof Worker)) {
            value = (Worker) o;

            return super.equals(value) &&
            getFunction().equals(value.getFunction()) &&
            getAnnualIncome().equals(value.getAnnualIncome()) &&
            (getWorkedHours() == value.getWorkedHours()) &&
            getSubalterns().equals(value.getSubalterns()) &&
            getChiefs().equals(value.getChiefs());
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOrganization(Organization organization) {
        if (organization != null) {
            this.organization = organization;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null organization.");
        }
    }
}
