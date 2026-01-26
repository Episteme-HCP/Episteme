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
import org.jscience.economics.money.Money;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Time;
import org.jscience.mathematics.discrete.Tree;
import org.jscience.mathematics.discrete.RootedTree;

import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.stream.Collectors;

/**
 * Represents a worker within an {@link Organization}.
 * 
 * <p>A worker is an {@link EconomicAgent} that has a specific job function, 
 * earns an income, and exists within a professional hierarchy of chiefs and 
 * subalterns.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 6.1
 * @since 1.0
 */
@Persistent
public class Worker extends EconomicAgent {

    private static final long serialVersionUID = 1L;

    @Attribute
    private String function;

    @Attribute
    private Money annualIncome;

    @Attribute
    private Quantity<Time> workedHours; // per year

    /** The immediate supervisors of this worker. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Worker> chiefs;

    /** The immediate subordinates of this worker. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Worker> subalterns;

    /** The organization this worker belongs to. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Organization organization;

    /**
     * Creates a new worker.
     *
     * @param individual   the underlying biological or social individual.
     * @param organization the organization they work for.
     * @param function     their job title or function.
     * @param annualIncome their yearly salary.
     */
    public Worker(Individual individual, Organization organization, String function, Money annualIncome) {
        super(individual, new WorkSituation(organization.getName(), organization.getName()));
        this.organization = Objects.requireNonNull(organization, "Organization cannot be null");
        this.function = Objects.requireNonNull(function, "Function cannot be null");
        if (function.isEmpty()) throw new IllegalArgumentException("Function cannot be empty");
        this.annualIncome = Objects.requireNonNull(annualIncome, "Income cannot be null");
        this.workedHours = Quantities.create(Real.ZERO, Units.HOUR);
        this.chiefs = new HashSet<>();
        this.subalterns = new HashSet<>();
    }

    /**
     * Legacy constructor for work situation compatibility.
     */
    public Worker(Individual individual, WorkSituation workSituation, String function, Organization organization) {
        super(individual, workSituation);
        this.organization = Objects.requireNonNull(organization);
        this.function = Objects.requireNonNull(function);
        this.annualIncome = Money.usd(Real.ZERO);
        this.workedHours = Quantities.create(Real.ZERO, Units.HOUR);
        this.chiefs = new HashSet<>();
        this.subalterns = new HashSet<>();
    }

    /**
     * Sets the job function description.
     *
     * @param function the job title or description.
     * @throws IllegalArgumentException if function is null or empty.
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
     * Returns the annual income of this worker.
     *
     * @return the yearly salary.
     */
    public Money getAnnualIncome() {
        return annualIncome;
    }

    /**
     * Sets the annual income for this worker.
     *
     * @param annualIncome the new yearly salary.
     * @throws IllegalArgumentException if income is null.
     */
    public void setAnnualIncome(Money annualIncome) {
        if (annualIncome != null) {
            this.annualIncome = annualIncome;
        } else {
            throw new IllegalArgumentException("You can't set a null income.");
        }
    }

    /**
     * Returns the number of hours worked per year.
     *
     * @return the worked hours.
     */
    public Quantity<Time> getWorkedHours() {
        return workedHours;
    }

    /**
     * Sets the number of hours worked per year.
     *
     * @param workedHours the new hour count.
     */
    public void setWorkedHours(Quantity<Time> workedHours) {
        this.workedHours = Objects.requireNonNull(workedHours);
    }
    
    /**
     * Sets the number of hours worked per year.
     *
     * @param workedHours the new hour count in double (converted to Quantity).
     */
    public void setWorkedHours(double workedHours) {
        this.workedHours = Quantities.create(workedHours, Units.HOUR);
    }

    /**
     * Checks if this worker has any subordinates.
     *
     * @return {@code true} if subalterns set is not empty.
     */
    public boolean hasChild() {
        return !subalterns.isEmpty();
    }

    /**
     * Checks if a specific worker is an immediate subordinate.
     *
     * @param child the worker to check.
     * @return {@code true} if the worker is in the subalterns set.
     */
    public boolean hasChild(Worker child) {
        return subalterns.contains(child);
    }

    /**
     * Returns an unmodifiable set of immediate subordinates.
     *
     * @return the subalterns.
     */
    public Set<Worker> getSubalterns() {
        return Collections.unmodifiableSet(subalterns);
    }

    /**
     * Sets the set of immediate subordinates.
     * 
     * @param subalterns the subordinates to set.
     * @throws IllegalArgumentException if subalterns set is null.
     */
    public void setSubalterns(Set<Worker> subalterns)
        throws IllegalArgumentException {
        Objects.requireNonNull(subalterns, "Subalterns set cannot be null");
        for (Worker child : subalterns) {
            if (child == this || child.hasDistantSubaltern(this)) {
                throw new IllegalArgumentException("Cannot add Worker child that is a parent or self.");
            }
            child.addChief(this);
        }
        this.subalterns = new HashSet<>(subalterns);
    }

    /**
     * Adds an immediate subordinate.
     *
     * @param child the worker to add.
     * @throws IllegalArgumentException if the child is already a supervisor or self.
     */
    public void addSubaltern(Worker child)
        throws IllegalArgumentException {
        Objects.requireNonNull(child, "Child cannot be null");
        if (child == this || child.hasDistantSubaltern(this)) {
            throw new IllegalArgumentException("Cannot add Worker child that is a parent or self.");
        }
        child.addChief(this);
        subalterns.add(child);
    }

    /**
     * Removes an immediate subordinate.
     *
     * @param child the worker to remove.
     */
    public void removeSubaltern(Worker child) {
        if (child != null) {
            child.removeChief(this);
            subalterns.remove(child);
        }
    }

    /**
     * Checks if this worker has any immediate supervisors.
     *
     * @return {@code true} if chiefs set is not empty.
     */
    public boolean hasChief() {
        return !chiefs.isEmpty();
    }

    /**
     * Returns an unmodifiable set of immediate supervisors.
     *
     * @return the chiefs.
     */
    public Set<Worker> getChiefs() {
        return Collections.unmodifiableSet(chiefs);
    }

    private void addChief(Worker parent) {
        chiefs.add(Objects.requireNonNull(parent));
    }

    private void removeChief(Worker parent) {
        chiefs.remove(parent);
    }

    /**
     * Change a supervisor for this worker.
     *
     * @param oldParent the current supervisor to remove.
     * @param newParent the new supervisor to add.
     */
    public void reparent(Worker oldParent, Worker newParent) {
        Objects.requireNonNull(oldParent);
        Objects.requireNonNull(newParent);
        if (chiefs.contains(oldParent)) {
            oldParent.removeSubaltern(this);
            newParent.addSubaltern(this);
        } else {
            throw new IllegalArgumentException("Old parent is not a chief of this worker.");
        }
    }

    /**
     * Returns the set of workers at the top of the hierarchy for this worker.
     *
     * @return the top-level bosses.
     */
    public Set<Worker> getLeaders() {
        Set<Worker> current = new HashSet<>();
        current.add(this);
        while (true) {
            Set<Worker> parents = new HashSet<>();
            for (Worker w : current) {
                parents.addAll(w.getChiefs());
            }
            if (parents.isEmpty()) return current;
            current = parents;
        }
    }

    /**
     * Returns workers having the same immediate supervisors.
     *
     * @return the set of coworkers.
     */
    public Set<Worker> getCoWorkers() {
        return chiefs.stream()
            .flatMap(chief -> chief.getSubalterns().stream())
            .filter(w -> !w.equals(this))
            .collect(Collectors.toSet());
    }

    /**
     * Finds the lowest common supervisor for two workers.
     *
     * @param w1 first worker.
     * @param w2 second worker.
     * @return the common root worker, or {@code null}.
     */
    public static Worker getCommonRoot(Worker w1, Worker w2) {
        if (w1 == null || w2 == null) return null;
        Set<Worker> roots1 = w1.getLeaders();
        Set<Worker> roots2 = w2.getLeaders();
        
        Set<Worker> commonRoots = new HashSet<>(roots1);
        commonRoots.retainAll(roots2);
        
        if (commonRoots.isEmpty()) return null;
        
        Worker sharedRoot = commonRoots.iterator().next();
        List<Worker> l1 = getLineage(sharedRoot, w1);
        List<Worker> l2 = getLineage(sharedRoot, w2);
        
        int i = 0;
        while (i < Math.min(l1.size(), l2.size()) && l1.get(i).equals(l2.get(i))) {
            i++;
        }
        return i > 0 ? l1.get(i - 1) : null;
    }

    /**
     * Returns the workers path from a root to a target worker.
     *
     * @param root the starting worker.
     * @param target the destination worker.
     * @return the list representing the path.
     */
    public static List<Worker> getLineage(Worker root, Worker target) {
        if (root == null || target == null) return new ArrayList<>();
        if (root.equals(target)) {
            List<Worker> res = new ArrayList<>();
            res.add(root);
            return res;
        }
        for (Worker sub : root.getSubalterns()) {
            List<Worker> subLineage = getLineage(sub, target);
            if (!subLineage.isEmpty()) {
                List<Worker> res = new ArrayList<>();
                res.add(root);
                res.addAll(subLineage);
                return res;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Creates a tree structure representing the hierarchy between two workers.
     *
     * @param w1 first worker.
     * @param w2 second worker.
     * @return the tree of workers.
     */
    public static Tree<Worker> extractTree(Worker w1, Worker w2) {
        Worker root = getCommonRoot(w1, w2);
        if (root == null) return new RootedTree<>();
        
        RootedTree<Worker> tree = new RootedTree<>(root);
        List<Worker> l1 = getLineage(root, w1);
        List<Worker> l2 = getLineage(root, w2);
        
        fillTree(tree, l1);
        fillTree(tree, l2);
        return tree;
    }

    private static void fillTree(RootedTree<Worker> tree, List<Worker> lineage) {
        for (int i = 0; i < lineage.size() - 1; i++) {
            Worker parent = lineage.get(i);
            Worker child = lineage.get(i + 1);
            if (!tree.getChildren(parent).contains(child)) {
                tree.addChild(parent, child);
            }
        }
    }

    /**
     * Returns all subordinates at any depth.
     *
     * @return the set of all subalterns.
     */
    public Set<Worker> getAllSubalterns() {
        Set<Worker> result = new HashSet<>();
        collectAllSubalterns(this, result);
        return result;
    }

    private void collectAllSubalterns(Worker w, Set<Worker> result) {
        for (Worker sub : w.getSubalterns()) {
            if (result.add(sub)) {
                collectAllSubalterns(sub, result);
            }
        }
    }

    /**
     * Returns all subordinates at a specific depth level.
     *
     * @param k the depth level (0 for self, 1 for immediate subalterns, etc.).
     * @return the set of workers at depth k.
     */
    public Set<Worker> getSubalternsAtLevelK(int k) {
        if (k < 0) throw new IllegalArgumentException("Depth k must be >= 0");
        Set<Worker> result = new HashSet<>();
        if (k == 0) {
            result.add(this);
        } else {
            for (Worker sub : getSubalterns()) {
                result.addAll(sub.getSubalternsAtLevelK(k - 1));
            }
        }
        return result;
    }

    /**
     * Checks if a worker is a subordinate at any depth.
     *
     * @param target the worker to search for.
     * @return {@code true} if found in the hierarchy.
     */
    public boolean hasDistantSubaltern(Worker target) {
        if (target == null) return false;
        if (subalterns.contains(target)) return true;
        return subalterns.stream().anyMatch(sub -> sub.hasDistantSubaltern(target));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        if (!super.equals(o)) return false;
        Worker that = (Worker) o;
        return Objects.equals(workedHours, that.workedHours) &&
               Objects.equals(function, that.function) &&
               Objects.equals(annualIncome, that.annualIncome) &&
               Objects.equals(chiefs, that.chiefs) &&
               Objects.equals(subalterns, that.subalterns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), function, annualIncome, workedHours);
    }

    /**
     * Returns the organization this worker is employed by.
     *
     * @return the organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the organization for this worker.
     *
     * @param organization the new employer.
     */
    public void setOrganization(Organization organization) {
        this.organization = Objects.requireNonNull(organization);
    }
}
