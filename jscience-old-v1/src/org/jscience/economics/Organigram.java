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

import org.jscience.util.NAryTree;
import org.jscience.util.Named;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a formal organigram, that is the organization a it
 * is seen from above and meant to be by the boss. It is different from the
 * real process of work (see Worker, the hierarchy of workers is not really a
 * hierarchy) and different from the flow of resources (see Work).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//all children elements of this organigram should also be organigrams
//an alternate solution as to design this class would be to have the workers stored directly as children of the organigram along with sub-organigrams
public class Organigram extends NAryTree implements Named {
    /** DOCUMENT ME! */
    private Set workers;

    //the name is the name for that hierarchy level, ex: production, research team...
    /**
     * Creates a new Organigram object.
     *
     * @param name DOCUMENT ME!
     */
    public Organigram(String name) {
        super(name);

        if ((name != null) && (name.length() > 0)) {
            //super(name);
            this.workers = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Organigram constructor doesn't accept null (and name can't be empty).");
        }
    }

    //there is usually more workers at each level below current level if any
    /**
     * Creates a new Organigram object.
     *
     * @param name DOCUMENT ME!
     * @param workers DOCUMENT ME!
     */
    public Organigram(String name, Set workers) {
        super(name);

        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (workers != null)) {
            //super(name);
            iterator = workers.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Worker;
            }

            if (valid) {
                this.workers = workers;
            } else {
                throw new IllegalArgumentException(
                    "The Set of workers should contain only Workers.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Organigram constructor doesn't accept null (and name can't be empty).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return (String) getContents();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            setContents(name);
        } else {
            throw new IllegalArgumentException(
                "The name of an organigram shoudn't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setContents(Object obj) {
        if ((obj != null) && (obj instanceof String) &&
                (((String) obj).length() > 0)) {
            super.setContents(obj);
        } else {
            throw new IllegalArgumentException(
                "The name of an organigram shoudn't be null or empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getWorkers() {
        return workers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param worker DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addWorker(Worker worker) {
        if (worker != null) {
            workers.add(worker);
        } else {
            throw new IllegalArgumentException("You can't add a null worker.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param worker DOCUMENT ME!
     */
    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }

    /**
     * DOCUMENT ME!
     *
     * @param workers DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setWorkers(Set workers) {
        Iterator iterator;
        boolean valid;

        if (workers != null) {
            iterator = workers.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Worker;
            }

            if (valid) {
                this.workers = workers;
            } else {
                throw new IllegalArgumentException(
                    "The Set of workers should contain only Workers.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of workers can't be null.");
        }
    }

    //in the beginning I had defined getGroup() which returned getAllChildren() but there is no garanty
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllWorkers() {
        Iterator iterator;
        Set result;

        iterator = getChildren().iterator();
        result = new HashSet(getWorkers());

        while (iterator.hasNext()) {
            result.addAll(((NAryTree) iterator.next()).getAllChildren());
        }

        return result;
    }
}
