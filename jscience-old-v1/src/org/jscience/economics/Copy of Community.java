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

import org.jscience.biology.Species;

import org.jscience.geography.Place;

import org.jscience.psychology.social.Group;

import java.util.*;


/**
 * A class representing a loose, primitive organization where resources are
 * simply shared.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//collective property of means of production and consuming, such as in a kibboutz
//but this is also the starting class to consider resource managment for an animal in the wild (consuming preys, etc)
//other communities (or simply individual seen as a community with a single member) exchange some resources with this one
//without the money medium, directly bartering
//all resource are equaly reachable by individual of the community
//resources are NEVER consumed: what you eat is after digestion the fertilizer for someone else
//resources are therefore ALWAYS processed by individuals from the community or simply hold (at the given place)
//the implementation assumes that should one community agree to sell all its resources it can INCLUDING the means of prodcution
//it is up to you to label the goods you want to sell to the public as such
//you can use this class not only for humans, but also for primates (and especially for dominance hierarchies upon a resource), or social insects
public class Community extends Group implements TaskProcessor {
    /** DOCUMENT ME! */
    private Set resources;

    //you start with no ressource
    /**
     * Creates a new Community object.
     *
     * @param species DOCUMENT ME!
     * @param place DOCUMENT ME!
     */
    public Community(Species species, Place place) {
        super(species, place);
        this.resources = Collections.EMPTY_SET;
    }

    //you start with no ressource
    /**
     * Creates a new Community object.
     *
     * @param species DOCUMENT ME!
     * @param individuals DOCUMENT ME!
     * @param place DOCUMENT ME!
     */
    public Community(Species species, Set individuals, Place place) {
        super(species, place);

        setIndividuals(individuals);
        this.resources = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /**
     * public void removeIndividual(Individual individual) { if
     * (individual != null) { if (getIndividuals().contains(individual)) { if
     * (getIndividuals().size() > 1) { super.removeIndividual(individual); }
     * else { throw new IllegalArgumentException( "You can't remove last
     * individual."); }}}}
     *
     * @return DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /**
     * public void setIndividuals(Set individuals) { Iterator iterator;
     * boolean valid; if ((individuals != null) && (individuals.size() > 0)) {
     * super.setIndividuals(individuals); } else { throw new
     * IllegalArgumentException( "You can't set a null or empty individuals
     * set."); }}
     *
     * @return DOCUMENT ME!
     */
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getResources() {
        return resources;
    }

    /**
     * DOCUMENT ME!
     *
     * @param resource DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addResource(Resource resource) {
        if (resource != null) {
            resources.add(resource);
        } else {
            throw new IllegalArgumentException("You can't add a null Resource.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param resource DOCUMENT ME!
     */
    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    /**
     * DOCUMENT ME!
     *
     * @param resources DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setResources(Set resources) {
        Iterator iterator;
        boolean valid;

        if (resources != null) {
            iterator = resources.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                this.resources = resources;
            } else {
                throw new IllegalArgumentException(
                    "The resources Set must contain only Resources.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null Resource set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param offeredResources DOCUMENT ME!
     * @param otherParty DOCUMENT ME!
     * @param wantedResources DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void barterResources(Set offeredResources, Community otherParty,
        Set wantedResources) {
        Iterator iterator;
        boolean valid;
        Set currentResources;

        if ((offeredResources != null) && (otherParty != null) &&
                (wantedResources != null)) {
            iterator = offeredResources.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                iterator = offeredResources.iterator();

                while (iterator.hasNext() && valid) {
                    valid = this.getResources().contains(iterator.next());
                }

                if (valid) {
                    iterator = wantedResources.iterator();

                    while (iterator.hasNext() && valid) {
                        valid = iterator.next() instanceof Resource;
                    }

                    if (valid) {
                        iterator = wantedResources.iterator();

                        while (iterator.hasNext() && valid) {
                            valid = otherParty.getResources()
                                              .contains(iterator.next());
                        }

                        if (valid) { //probably many useless calls here
                            currentResources = otherParty.getResources();
                            currentResources.removeAll(wantedResources);
                            otherParty.setResources(currentResources);
                            currentResources = getResources();
                            currentResources.addAll(wantedResources);
                            setResources(currentResources);
                            currentResources = otherParty.getResources();
                            currentResources.addAll(offeredResources);
                            otherParty.setResources(currentResources);
                            currentResources = getResources();
                            currentResources.removeAll(offeredResources);
                            setResources(currentResources);
                        } else {
                            throw new IllegalArgumentException(
                                "All wantedResources should be owned by otherParty.");
                        }
                    } else {
                        throw new IllegalArgumentException(
                            "wantedResources should be a Set of Resources.");
                    }
                } else {
                    throw new IllegalArgumentException(
                        "All offeredResources should be owned by this community.");
                }
            } else {
                throw new IllegalArgumentException(
                    "offeredResources should be a Set of Resources.");
            }
        } else {
            throw new IllegalArgumentException(
                "offeredResources, otherParty and wantedResources can't be null.");
        }
    }

    //this should not go past the actual workforce available to the consumer
    /**
     * DOCUMENT ME!
     *
     * @param task DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean consumeResources(Task task) {
        Iterator iterator;
        Iterator iterator2;
        Enumeration enumeration;
        Enumeration enumeration2;
        boolean valid;
        double quantity;
        Resource currentResource;
        Hashtable availableResources;
        Hashtable workedResources;
        String string1;
        String string2;
        boolean found;
        boolean result;

        if (task != null) {
            iterator = getResources().iterator();
            availableResources = new Hashtable();

            while (iterator.hasNext()) {
                currentResource = (Resource) iterator.next();

                if (availableResources.containsKey(currentResource.getName())) {
                    quantity = ((Double) availableResources.get(currentResource.getName())).doubleValue();
                    quantity += currentResource.getAmount();
                    availableResources.put(currentResource.getName(),
                        new Double(quantity));
                } else {
                    availableResources.put(currentResource.getName(),
                        new Double(currentResource.getAmount()));
                }
            }

            iterator = task.getResources().iterator();
            workedResources = new Hashtable();

            while (iterator.hasNext()) {
                currentResource = (Resource) iterator.next();

                if (workedResources.containsKey(currentResource.getName())) {
                    quantity = ((Double) workedResources.get(currentResource.getName())).doubleValue();
                    quantity += currentResource.getAmount();
                    workedResources.put(currentResource.getName(),
                        new Double(quantity));
                } else {
                    workedResources.put(currentResource.getName(),
                        new Double(currentResource.getAmount()));
                }
            }

            //resources are matched according to their names
            //if all resources are available and quantities are sufficient
            enumeration = workedResources.keys();
            found = true;

            while (enumeration.hasMoreElements() && found) {
                string1 = (String) enumeration.nextElement();
                enumeration2 = availableResources.keys();
                found = false;

                while (enumeration2.hasMoreElements() && !found) {
                    string2 = (String) enumeration2.nextElement();
                    found = (string2.equals(string1) &&
                        (((Double) availableResources.get(string2)).doubleValue() >= ((Double) workedResources.get(string1)).doubleValue()));
                    availableResources.put(string2,
                        new Double(((Double) availableResources.get(string2)).doubleValue() -
                            ((Double) workedResources.get(string1)).doubleValue()));
                }
            }

            if (found) {
                result = true;

                //subtract the corresponding quantity (and don't care about the price as you own them all)
                while (workedResources.size() > 0) { //warning: we remove elements from the underlying set while iterating through the set
                    string1 = (String) workedResources.keys().nextElement();
                    iterator2 = getResources().iterator(); //warning: we remove elements from the underlying set while iterating through the set
                    found = false;

                    while (iterator2.hasNext() && !valid) {
                        currentResource = (Resource) iterator2.next();

                        if (currentResource.getName().equals(string1)) {
                            quantity = ((Double) workedResources.get(currentResource.getName())).doubleValue();

                            if (currentResource.getAmount() > quantity) {
                                valid = true;
                                getResources().remove(currentResource);
                                currentResource = new Resource(currentResource.getOrganization(),
                                        currentResource.getName(),
                                        currentResource.getDescription(),
                                        currentResource.getValue(),
                                        currentResource.getAmount() - quantity,
                                        currentResource.getIdentification());
                                getResources().add(currentResource);
                            } else {
                                //remove all resources with quantity 0
                                getResources().remove(currentResource);
                            }

                            quantity -= currentResource.getAmount();
                            workedResources.put(currentResource.getName(),
                                new Double(quantity));
                        }
                    }
                }

                //add the corresponding products for the work
                getResources().addAll(task.getResources());
            } else {
                result = false;
            }

            return result;
        } else {
            throw new IllegalArgumentException("The Work can't be null.");
        }
    }
}
