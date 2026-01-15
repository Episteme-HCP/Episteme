package org.jscience.economics;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a holding, or a set of companies owned by an
 * individual or a group of individuals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Holding extends Organization {
    /** DOCUMENT ME! */
    private Set organizations;

/**
     * Creates a new Holding object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     * @param organizations  DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Holding(String name, Identification identification, Set owners,
        BusinessPlace place, Set accounts, Set organizations) {
        super(name, identification, owners, place, accounts);

        Iterator iterator;
        boolean valid;

        if ((organizations != null) && (organizations.size() > 0)) {
            valid = true;
            iterator = organizations.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organization;
            }

            if (valid) {
                this.organizations = organizations;
            } else {
                throw new IllegalArgumentException(
                    "The organizations Set must contain only Organizations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Holding constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOrganizations() {
        return organizations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOrganization(Organization organization) {
        if (organization != null) {
            organizations.add(organization);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null Organization.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeOrganization(Organization organization) {
        if (organization != null) {
            if (organizations.contains(organization)) {
                if (organizations.size() > 1) {
                    organizations.remove(organization);
                } else {
                    throw new IllegalArgumentException(
                        "You can't remove last owned organization.");
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param organizations DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOrganizations(Set organizations) {
        Iterator iterator;
        boolean valid;

        if ((organizations != null) && (organizations.size() > 0)) {
            iterator = organizations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organization;
            }

            if (valid) {
                this.organizations = organizations;
            } else {
                throw new IllegalArgumentException(
                    "The organizations Set must contain only Organizations.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty Organization set.");
        }
    }
}
