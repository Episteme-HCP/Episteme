package org.jscience.politics;

import org.jscience.biology.human.Human;

import org.jscience.economics.Organization;

import org.jscience.geography.Place;

import org.jscience.law.Code;
import org.jscience.law.Constitution;
import org.jscience.law.Treaty;

import org.jscience.psychology.social.Tribe;

import org.jscience.sociology.Culture;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing the basic facts about an organized human group.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this class is to be used for tribes that keep record about themselves which normally happens with the discovery of writing

//should probably be renamed State: http://en.wikipedia.org/wiki/State

public class Nation extends Tribe {
    /** DOCUMENT ME! */
    private int kind;

    //all these humans are normally contained in the underlying population
    //this is not checked (as this may not be the rule in your country)
    /** DOCUMENT ME! */
    private Administration government; //the people that decide on the actions for the country, the leader should be at the top of the organigram

    /** DOCUMENT ME! */
    private Set parliment; //a Set of humans that decide of the laws

    /** DOCUMENT ME! */
    private Constitution constitution;

    /** DOCUMENT ME! */
    private Set codes; //a Set of codes

    /** DOCUMENT ME! */
    private Set treaties; //a Set of treaties

    /** DOCUMENT ME! */
    private Set organizations;

/**
     * Creates a new Tribe object.
     *
     * @param name            DOCUMENT ME!
     * @param formalTerritory DOCUMENT ME!
     * @param culture         DOCUMENT ME!
     */
    public Nation(String name, Place formalTerritory, Culture culture) {
        super(name, formalTerritory, culture);

        this.kind = PoliticsConstants.UNKNOWN;
        this.government = null;
        this.parliment = Collections.EMPTY_SET;
        this.constitution = null;
        this.codes = Collections.EMPTY_SET;
        this.treaties = Collections.EMPTY_SET;
        this.organizations = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }

    //a good policy is to set the leader to getGovernement().getOrganigram().getWorkers().toArray()[0]
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Administration getGovernment() {
        return government;
    }

    /**
     * DOCUMENT ME!
     *
     * @param government DOCUMENT ME!
     */
    public void setGovernment(Administration government) {
        this.government = government;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParliment() {
        return parliment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parliment DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setParliment(Set parliment) {
        Iterator iterator;
        boolean valid;

        if (parliment != null) {
            iterator = parliment.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.parliment = parliment;
            } else {
                throw new IllegalArgumentException(
                    "The parliment Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The parliment Set can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param human DOCUMENT ME!
     */
    public void addParlimentMember(Human human) {
        parliment.add(human);
    }

    /**
     * DOCUMENT ME!
     *
     * @param human DOCUMENT ME!
     */
    public void removeParlimentMember(Human human) {
        parliment.remove(human);
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Constitution getConstitution() {
        return constitution;
    }

    /**
     * DOCUMENT ME!
     *
     * @param constitution DOCUMENT ME!
     */
    public void setConstitution(Constitution constitution) {
        this.constitution = constitution;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getCodes() {
        return codes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param codes DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCodes(Set codes) {
        Iterator iterator;
        boolean valid;

        if (codes != null) {
            iterator = codes.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Code;
            }

            if (valid) {
                this.codes = codes;
            } else {
                throw new IllegalArgumentException(
                    "The Set of Codes must contain only Codes.");
            }
        } else {
            throw new IllegalArgumentException("The codes Set can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param code DOCUMENT ME!
     */
    public void addCode(Code code) {
        codes.add(code);
    }

    /**
     * DOCUMENT ME!
     *
     * @param code DOCUMENT ME!
     */
    public void removeCode(Code code) {
        codes.remove(code);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTreaties() {
        return treaties;
    }

    /**
     * DOCUMENT ME!
     *
     * @param treaties DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTreaties(Set treaties) {
        Iterator iterator;
        boolean valid;

        if (treaties != null) {
            iterator = treaties.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Treaty;
            }

            if (valid) {
                this.treaties = treaties;
            } else {
                throw new IllegalArgumentException(
                    "The Set of Treaties must contain only Treaties.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of Treaties can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param treaty DOCUMENT ME!
     */
    public void addTreaty(Treaty treaty) {
        treaties.add(treaty);
    }

    /**
     * DOCUMENT ME!
     *
     * @param treaty DOCUMENT ME!
     */
    public void removeTreaty(Treaty treaty) {
        treaties.remove(treaty);
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
     * @param organizations DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOrganizations(Set organizations) {
        Iterator iterator;
        boolean valid;

        if (organizations != null) {
            iterator = organizations.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organization;
            }

            if (valid) {
                this.organizations = organizations;
            } else {
                throw new IllegalArgumentException(
                    "The Set of Treaties must contain only Organizations.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of Organizations can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void addTreaty(Organization organization) {
        organizations.add(organization);
    }

    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void removeTreaty(Organization organization) {
        organizations.remove(organization);
    }
}
