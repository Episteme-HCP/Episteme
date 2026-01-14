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

package org.jscience.sociology;

import org.jscience.psychology.social.Tribe;

import org.jscience.util.Named;

import java.util.HashSet;
import java.util.Set;


/**
 * A class representing the common elements of a group of individuals, what
 * we usually also call civilization.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a society is basically a group of countries sharing the same culture though perhaps not federated
//for example the occidental society
public class Society extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Set tribes;

/**
     * Creates a new Society object.
     *
     * @param name  DOCUMENT ME!
     * @param tribe DOCUMENT ME!
     */
    public Society(String name, Tribe tribe) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            tribes = new HashSet();
            tribes.add(tribe);
        } else {
            throw new IllegalArgumentException(
                "The Society constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTribes() {
        return tribes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tribe DOCUMENT ME!
     */
    public void addTribe(Tribe tribe) {
        tribes.add(tribe);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tribe DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeTribe(Tribe tribe) {
        if ((tribe != null) && tribes.contains(tribe)) {
            if (tribes.size() > 1) {
                tribes.remove(tribe);
            } else {
                throw new IllegalArgumentException(
                    "You can't remove the last tribe of a society.");
            }
        }
    }
}
