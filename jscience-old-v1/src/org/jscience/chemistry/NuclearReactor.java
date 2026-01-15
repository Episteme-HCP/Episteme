package org.jscience.chemistry;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The NuclearReactor class works out nuclear reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class NuclearReactor extends Reactor {
    /** DOCUMENT ME! */
    private HashMap contents;

    //hashmap of Atom/mass
    /**
     * Creates a new NuclearRector object.
     *
     * @param nuclearReaction DOCUMENT ME!
     * @param contents DOCUMENT ME!
     */
    public NuclearRector(NuclearReaction nuclearReaction, HashMap contents) {
        Iterator iterator;
        boolean valid;
        Object currentElement;

        if ((nuclearReaction != null) && (contents != null)) {
            if (contents.size() > 0) {
                iterator = contents.keySet().iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    currentElement = iterator.next();
                    valid = (currentElement instanceof Atom) &&
                        (contents.get(currentElement) != null) &&
                        (contents.get(currentElement) instanceof Double);
                }

                if (valid) {
                    this.contents = contents;
                    this.initialContents = contents;
                } else {
                    throw new IllegalArgumentException(
                        "Nuclear reactor contents must contain only Atom/Double pairs.");
                }
            } else {
                throw new IllegalArgumentException(
                    "Nuclear reactor contents can't be an empty HashMap.");
            }
        } else {
            throw new IllegalArgumentException(
                "NuclearRector doesn't accept null arguments.");
        }
    }

    //this is the data you initially put in and where changed due to calls to react() or getEquilibrium()
    /**
     * DOCUMENT ME!
     */
    public void getInitialConditions() {
        contents = initialContents;
    }

    //this is the simulator part which transforms part of the reactants into products
    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     */
    public void react(double dt) {
    }

    //proceeds to the end equilibrium by reacting and reacting again from the current parameters to the final equilibrium
    /**
     * DOCUMENT ME!
     */
    public void getEquilibrium() {
    }

    //the complete energy produced in that reaction from the initial conditions to the equilibrium
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSumEnergy() {
    }

 }
