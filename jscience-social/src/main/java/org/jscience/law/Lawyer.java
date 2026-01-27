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
import java.util.Set;

/**
 * The Lawyer class represents a legal professional whose primary role is to 
 * provide legal advice and represent individuals or organizations in legal matters.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Lawyer extends Worker {
    
    /** The set of individuals represented by this lawyer. */
    private Set<Individual> clients;

    /**
     * Creates a new Lawyer object.
     *
     * @param individual the biological individual who is a lawyer
     * @param lawSuitSituation the legal situation or context
     * @param function the specific role or title (e.g., "Counsel", "Advocate")
     * @param administration the administrative body the lawyer belongs to
     * @throws IllegalArgumentException if individual, lawSuitSituation, or administration is null
     */
    public Lawyer(Individual individual, LawSuitSituation lawSuitSituation,
        String function, Administration administration) {
        super(individual, lawSuitSituation, function, administration);
        this.clients = new HashSet<>();
    }

    /**
     * Creates a new Lawyer object with the default function "Lawyer".
     *
     * @param individual the biological individual who is a lawyer
     * @param lawSuitSituation the legal situation or context
     * @param administration the administrative body the lawyer belongs to
     * @throws IllegalArgumentException if individual, lawSuitSituation, or administration is null
     */
    public Lawyer(Individual individual, LawSuitSituation lawSuitSituation,
        Administration administration) {
        super(individual, lawSuitSituation, "Lawyer", administration);
        this.clients = new HashSet<>();
    }

    /**
     * Returns the set of clients currently represented by this lawyer.
     * @return the set of clients
     */
    public Set<Individual> getClients() {
        return clients;
    }

    /**
     * Sets the set of clients for this lawyer.
     *
     * @param clients the new set of clients
     * @throws IllegalArgumentException if the set is null or contains non-Individual entities
     */
    public void setClients(Set<Individual> clients) {
        if (clients == null) {
            throw new IllegalArgumentException("The Set of clients cannot be null.");
        }

        for (Object client : clients) {
            if (!(client instanceof Individual)) {
                throw new IllegalArgumentException("The Set of clients must contain only Individual.");
            }
        }
        this.clients = clients;
    }

    /**
     * Adds a new client to the lawyer's representation.
     * @param individual the individual to add as a client
     * @throws IllegalArgumentException if individual is null
     */
    public void addClient(Individual individual) {
        if (individual == null) {
            throw new IllegalArgumentException("Cannot add a null client.");
        }
        clients.add(individual);
    }

    /**
     * Removes a client from the lawyer's representation.
     * @param individual the individual to remove
     */
    public void removeClient(Individual individual) {
        clients.remove(individual);
    }
}
