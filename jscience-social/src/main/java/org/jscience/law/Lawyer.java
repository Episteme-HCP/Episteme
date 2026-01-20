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
