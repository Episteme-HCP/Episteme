/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * Represents a SociologicalFamily unit within the social structure.
 * Extends the basic {@link SociologicalGroup} to model specific familial relationships like parents and children.
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class SociologicalFamily extends SociologicalGroup {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Person parent1;
    
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Person parent2;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Person> children = new ArrayList<>();

    /**
     * Creates a new SociologicalFamily with the given SociologicalFamily name.
     * @param familyName the surname or name identifying the SociologicalFamily unit
     */
    public SociologicalFamily(String familyName) {
        super(familyName, GroupKind.SociologicalFamily);
    }

    /**
     * Sets the primary parent of the SociologicalFamily.
     * @param parent the parent person
     */
    public void setParent1(Person parent) {
        this.parent1 = parent;
        if (parent != null) {
            addMember(parent);
            assignRole(parent, "Parent");
        }
    }

    /**
     * Sets the secondary parent of the SociologicalFamily.
     * @param parent the parent person
     */
    public void setParent2(Person parent) {
        this.parent2 = parent;
        if (parent != null) {
            addMember(parent);
            assignRole(parent, "Parent");
        }
    }

    /**
     * Adds a child to the SociologicalFamily unit.
     * @param child the child person
     */
    public void addChild(Person child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            addMember(child);
            assignRole(child, "Child");
        }
    }

    /**
     * Returns the primary parent.
     * @return parent1
     */
    public Person getParent1() {
        return parent1;
    }

    /**
     * Returns the secondary parent.
     * @return parent2
     */
    public Person getParent2() {
        return parent2;
    }

    /**
     * Returns an unmodifiable list of children in the SociologicalFamily.
     * @return the children list
     */
    public List<Person> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Returns a list containing all defined parents.
     * @return list of parents
     */
    public List<Person> getParents() {
        List<Person> parents = new ArrayList<>(2);
        if (parent1 != null) parents.add(parent1);
        if (parent2 != null) parents.add(parent2);
        return parents;
    }

    /**
     * Returns the number of children in this SociologicalFamily unit.
     * @return child count
     */
    public int getNumChildren() {
        return children.size();
    }

    /**
     * Simple measure of generational depth within this specific unit.
     * @return 2 if children exist, 1 otherwise
     */
    public int getGenerationCount() {
        return children.isEmpty() ? 1 : 2;
    }
}

