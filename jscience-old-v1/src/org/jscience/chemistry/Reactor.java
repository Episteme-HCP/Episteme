package org.jscience.chemistry;

/**
 * The Reactor class is the superclass for all reactions.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Reactor extends Object {
    //this is the data you initially put in and where changed due to calls to react() or getEquilibrium()
    /**
     * DOCUMENT ME!
     */
    public abstract void getInitialConditions();

    //this is the simulator part which transforms part of the reactants into products
    /**
     * DOCUMENT ME!
     *
     * @param dt DOCUMENT ME!
     */
    public abstract void react(double dt);

    //proceeds to the end equilibrium by reacting and reacting again from the current parameters to the final equilibrium
    /**
     * DOCUMENT ME!
     */
    public abstract void getEquilibrium();

    //the complete energy produced in that reaction from the initial conditions to the equilibrium
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double getSumEnergy();
}
