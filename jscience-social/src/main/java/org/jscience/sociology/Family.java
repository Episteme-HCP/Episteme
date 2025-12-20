/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.util.*;

/**
 * Represents a family unit as a social structure.
 * <p>
 * Models family relationships: parents, children, extended family.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Family extends Group {

    private Person parent1;
    private Person parent2;
    private final List<Person> children = new ArrayList<>();

    public Family(String familyName) {
        super(familyName, Type.FAMILY);
    }

    public void setParent1(Person parent) {
        this.parent1 = parent;
        addMember(parent, "parent");
    }

    public void setParent2(Person parent) {
        this.parent2 = parent;
        addMember(parent, "parent");
    }

    public void addChild(Person child) {
        children.add(child);
        addMember(child, "child");
    }

    public Person getParent1() {
        return parent1;
    }

    public Person getParent2() {
        return parent2;
    }

    public List<Person> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public List<Person> getParents() {
        List<Person> parents = new ArrayList<>();
        if (parent1 != null)
            parents.add(parent1);
        if (parent2 != null)
            parents.add(parent2);
        return parents;
    }

    public int getNumChildren() {
        return children.size();
    }

    /**
     * Returns generation size (number of children).
     */
    public int getGenerationCount() {
        return children.size() > 0 ? 2 : 1;
    }

    @Override
    public String toString() {
        return String.format("Family '%s': %d parents, %d children",
                getName(), getParents().size(), children.size());
    }
}
