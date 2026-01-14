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

package org.jscience.arts.hobbies;

import org.jscience.biology.Individual;

import org.jscience.sociology.Role;
import org.jscience.sociology.Situation;
import org.jscience.arts.Collection;

import java.util.Collections;
import java.util.Set;


/**
 * A class representing a person who has hobbies (may be collecting objects
 * or stuff).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//as multiple inheritance is not possible we have choosen to extend Person although you may only need the superclass data
public final class Hobbyist extends Role {
    /** DOCUMENT ME! */
    private Set hobbies;

    /** DOCUMENT ME! */
    private Set collections;

/**
     * Creates a new Hobbyist object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Hobbyist(Individual individual, Situation situation) {
        super(individual, "Hobbyist", situation, Role.OBSERVER);
        this.hobbies = Collections.EMPTY_SET;
        this.collections = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getHobbies() {
        return hobbies;
    }

    //there is no setHobbies() but I feel like it
    /**
     * DOCUMENT ME!
     *
     * @param hobby DOCUMENT ME!
     */
    public void addHobby(Hobby hobby) {
        hobbies.add(hobby);
    }

    /**
     * DOCUMENT ME!
     *
     * @param hobby DOCUMENT ME!
     */
    public void removeHobby(Hobby hobby) {
        hobbies.remove(hobby);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCollections() {
        return collections;
    }

    //there is no setCollections() but I feel like it
    /**
     * DOCUMENT ME!
     *
     * @param collection DOCUMENT ME!
     */
    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    /**
     * DOCUMENT ME!
     *
     * @param collection DOCUMENT ME!
     */
    public void removeCollection(Collection collection) {
        collections.remove(collection);
    }
}
