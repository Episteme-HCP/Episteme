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

package org.jscience.law;

import org.jscience.biology.Individual;

import org.jscience.economics.Worker;

import org.jscience.politics.Administration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * The Lawyer class provides some useful information for people whose job
 * is to defend individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Lawyer extends Worker {
    /** DOCUMENT ME! */
    private Set clients;

/**
     * Creates a new Lawyer object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param function         DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Lawyer(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
        clients = new HashSet();
    }

/**
     * Creates a new Lawyer object.
     *
     * @param individual       DOCUMENT ME!
     * @param lawSuitSituation DOCUMENT ME!
     * @param administration   DOCUMENT ME!
     */
    public Lawyer(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Lawyer", administration);
        clients = new HashSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getClients() {
        return clients;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clients DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setClients(Set clients) {
        Iterator iterator;
        boolean valid;

        if (clients != null) {
            iterator = clients.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.clients = clients;
            } else {
                throw new IllegalArgumentException(
                    "The Set of clients should contain only Individual.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of clients shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void addClient(Individual individual) {
        clients.add(individual);
    }

    /**
     * DOCUMENT ME!
     *
     * @param individual DOCUMENT ME!
     */
    public void removeClient(Individual individual) {
        clients.remove(individual);
    }
}
