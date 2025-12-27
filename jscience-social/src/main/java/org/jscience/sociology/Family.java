/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.sociology;

import java.util.*;

/**
 * Represents a family unit as a social structure.
 * <p>
 * Models family relationships: parents, children, extended family.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
