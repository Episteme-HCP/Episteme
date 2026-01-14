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

import org.jscience.geography.Place;
import org.jscience.geography.Places;

import org.jscience.psychology.Behavior;

import org.jscience.sociology.Role;

import org.jscience.util.Positioned;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an individual from a specie. Could also be called
 * organism.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a set of organs working together and able to reproduce
//science can now make artificial individuals, genetically designed, for some species
//we could also provide support for the robotic, agent oriented view of the system:
//thirst, hunger, air supply
//energy (before exhaustion)  (short term (hour), mid term (day) and long term (fat))
//health
//current injuries or deseases
//age
//corporal temperature
//internal temperature (also see org.jscience.medical.Patient)
//...
//inputs, outputs, memory, planning, and behaviors would be part of this system
//this should be part of jrobotics, and NOT of jscience
//we also don't provide these as we expect to simulate many individuals at once and want to keep the memory overhead small
//you'll have to subclass this class to provide more features
//there is a field named attribute to store an object of your choice to support additional parameters
//you can also define a new Situation and a Role with additional variables

//we should nevertheless add support for Activity (yet most of the species have only a very small set of behavior and no real intent, and there is therefore no Activity ; see for example Bacterias, Plants...)
public class Individual extends Object implements Cloneable, Positioned {
    /** DOCUMENT ME! */
    private Species species;

    /** DOCUMENT ME! */
    private int stage;

    /** DOCUMENT ME! */
    private int sex;

    /** DOCUMENT ME! */
    private Set organs; //we don't expect a full list of organs

    /** DOCUMENT ME! */
    private Set tissues; //surrounding tissues that are not part of organs

    /** DOCUMENT ME! */
    private Place place;

    /** DOCUMENT ME! */
    private Place territory;

    /** DOCUMENT ME! */
    private Set behaviors; //the currently triggered behaviors for this individual

    /** DOCUMENT ME! */
    private Set roles;

    /** DOCUMENT ME! */
    private Object attribute;

/**
     * Creates a new Individual object.
     *
     * @param species DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Individual(Species species) {
        if (species != null) {
            this.species = species;
            this.stage = BiologyConstants.UNKNOWN;
            this.sex = BiologyConstants.UNKNOWN;
            organs = Collections.EMPTY_SET;
            tissues = Collections.EMPTY_SET;
            this.place = Places.EARTH;
            this.territory = Places.EARTH;
            this.behaviors = Collections.EMPTY_SET;
            this.roles = Collections.EMPTY_SET;
            this.attribute = null;
        } else {
            throw new IllegalArgumentException(
                "The Individual constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * DOCUMENT ME!
     *
     * @param species DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setSpecies(Species species) {
        if (species != null) {
            this.species = species;
        } else {
            throw new IllegalArgumentException("You can't set a null species.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStage() {
        return stage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stage DOCUMENT ME!
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSex() {
        return sex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sex DOCUMENT ME!
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOrgans() {
        return organs;
    }

    //all elements of the Set should be organs
    /**
     * DOCUMENT ME!
     *
     * @param organs DOCUMENT ME!
     */
    public void setOrgans(Set organs) {
        Iterator iterator;
        boolean valid;

        if (organs != null) {
            iterator = organs.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Organ;
            }

            if (valid) {
                this.organs = organs;
            } else {
                throw new IllegalArgumentException(
                    "All elements of the Set should be Organs.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of organs can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getTissues() {
        return tissues;
    }

    //all elements of the Set should be Tissue
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
                    "All elements of the Set should be Tissues.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of tissues can't be null.");
        }
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

        iterator = organs.iterator();

        while (iterator.hasNext()) {
            result.addAll(((Organ) iterator.next()).getAllCells());
        }

        return result;
    }

    //checks that all cells from this individual share the same genome
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean checkGenome() {
        Set cells;
        Iterator iterator;
        Genome genome;
        boolean result;

        result = true;
        cells = getAllCells();

        if (cells.size() > 1) {
            iterator = cells.iterator();
            genome = ((Cell) iterator.next()).getGenome();

            while (iterator.hasNext() && result) {
                result = ((Cell) iterator.next()).getGenome().equals(genome);
            }
        }

        return result;
    }

    //a Set of Individuals resulting from this one with related genome
    /**
     * DOCUMENT ME!
     *
     * @param individuals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set reproduce(Set individuals) {
        return species.reproduce(this, individuals);
    }

    //produces an identical twin
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Individual individual;

        //is this the way to use it ?
        try {
            individual = (Individual) super.clone();
            individual.species = getSpecies();
        } catch (java.lang.CloneNotSupportedException e) {
            //manual clone
            individual = new Individual(this.getSpecies());
        }

        individual.stage = getStage();
        individual.sex = getSex();
        individual.organs = getOrgans();
        individual.tissues = getTissues();
        individual.place = getPosition();
        individual.territory = getTerritory();
        individual.behaviors = getBehaviors();
        individual.roles = getRoles();
        individual.attribute = getAttribute();

        return individual;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Individual value;

        if ((o != null) && (o instanceof Individual)) {
            value = (Individual) o;

            return getSpecies().equals(value.getSpecies()) &&
            (getStage() == value.getStage()) &&
            getOrgans().equals(value.getOrgans()) &&
            getTissues().equals(value.getTissues());
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getPosition() {
        return place;
    }

    /**
     * DOCUMENT ME!
     *
     * @param place DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPosition(Place place) {
        if (place != null) {
            this.place = place;
        } else {
            throw new IllegalArgumentException("You can't set a null place.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Place getTerritory() {
        return territory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param territory DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setTerritory(Place territory) {
        if (territory != null) {
            this.territory = territory;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null territory.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getBehaviors() {
        return behaviors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param behaviors DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setBehaviors(Set behaviors) {
        Iterator iterator;
        boolean valid;

        if (behaviors != null) {
            iterator = behaviors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Behavior;
            }

            if (valid) {
                this.behaviors = behaviors;
            } else {
                throw new IllegalArgumentException(
                    "The Set of behaviors should contain only Behaviors.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of behaviors shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    /**
     * DOCUMENT ME!
     *
     * @param behavior DOCUMENT ME!
     */
    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }

    //roles are automatically added to individual when they are created
    /**
     * DOCUMENT ME!
     *
     * @param role DOCUMENT ME!
     */
    public void addRole(Role role) {
        if (role != null) {
            if (role.getIndividual() == this) {
                roles.add(role);
            } else {
                throw new IllegalArgumentException(
                    "You can only set a Role whose Individual is this.");
            }
        } else {
            throw new IllegalArgumentException("You can't add a null Role.");
        }
    }

    //you normally don't have to call this method but rather situation.removeRole(), which automatically in turn remove the role for the individual
    /**
     * DOCUMENT ME!
     *
     * @param role DOCUMENT ME!
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }

    //you should normally not have to call this
    /**
     * DOCUMENT ME!
     *
     * @param roles DOCUMENT ME!
     */
    public void setRoles(Set roles) {
        Iterator iterator;
        boolean valid;

        if (roles != null) {
            iterator = roles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Role;
            }

            if (valid) {
                iterator = roles.iterator();

                while (iterator.hasNext() && valid) {
                    valid = (((Role) iterator.next()).getIndividual() == this);
                }

                if (valid) {
                    this.roles = roles;
                } else {
                    throw new IllegalArgumentException(
                        "You can only set a Role whose Individual is this.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Role Set should contain only Roles.");
            }
        } else {
            throw new IllegalArgumentException("The Set shouldn't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getRoles() {
        return roles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getAttribute() {
        return attribute;
    }

    /**
     * DOCUMENT ME!
     *
     * @param attribute DOCUMENT ME!
     */
    public void setAttribute(Object attribute) {
        this.attribute = attribute;
    }
}
