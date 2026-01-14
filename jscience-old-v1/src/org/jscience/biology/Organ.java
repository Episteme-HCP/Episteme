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

package org.jscience.biology;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an organ.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an organ is a group of tissues with a joint purpose
//we could add support for neighbors (as organs functions together)
public class Organ extends Object implements Named {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private String function;

    /** DOCUMENT ME! */
    private Set tissues;

    /** DOCUMENT ME! */
    private Set neighbors;

/**
     * Creates a new Organ object.
     *
     * @param name     DOCUMENT ME!
     * @param function DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Organ(String name, String function) {
        if ((name != null) && (name.length() > 0) && (function != null) &&
                (function.length() > 0)) {
            this.name = name;
            this.function = function;
            this.tissues = Collections.EMPTY_SET;
            this.neighbors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Organ constructor can't have null or empty arguments.");
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
    public String getFunction() {
        return function;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTissues() {
        return tissues;
    }

    //tissues set should contain only tissues
    /**
     * DOCUMENT ME!
     *
     * @param tissues DOCUMENT ME!
     */
    public void setTissues(Set tissues) {
        Iterator iterator;
        boolean valid;

        if (tissues != null) {
            iterator = tissues.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Tissue;
            }

            if (valid) {
                this.tissues = tissues;
            } else {
                throw new IllegalArgumentException(
                    "The Set should contain only tissues.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of tissues can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tissue DOCUMENT ME!
     */
    public void addTissue(Tissue tissue) {
        tissues.add(tissue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tissue DOCUMENT ME!
     */
    public void removeTissue(Tissue tissue) {
        tissues.remove(tissue);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllCells() {
        Iterator iterator;
        Set result;

        iterator = tissues.iterator();
        result = new HashSet();

        while (iterator.hasNext()) {
            result.addAll(((Tissue) iterator.next()).getCells());
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getNeighbors() {
        return neighbors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addNeighbor(Organ neighbor) {
        if (neighbor != null) {
            neighbors.add(neighbor);
            neighbor.addRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException("You can't add a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void addRemoteNeighbor(Organ neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeNeighbor(Organ neighbor) {
        if (neighbor != null) {
            neighbors.remove(neighbor);
            neighbor.removeRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException(
                "You can't remove a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void removeRemoteNeighbor(Organ neighbor) {
        neighbors.remove(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setNeighbors(Set neighbors) {
        Iterator iterator;
        boolean valid;

        if (neighbors != null) {
            iterator = neighbors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organ;
            }

            if (valid) {
                this.neighbors = neighbors;
                iterator = neighbors.iterator();

                while (iterator.hasNext()) {
                    ((Organ) iterator.next()).addRemoteNeighbor(this);
                }
            } else {
                throw new IllegalArgumentException(
                    "The neighbors Set must contain only Organ.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null neighbors set.");
        }
    }
}
