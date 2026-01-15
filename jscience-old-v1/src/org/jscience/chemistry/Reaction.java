package org.jscience.chemistry;

/**
 * The Reaction class is the superclass for all chemical reactions and even
 * nuclear ones.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public abstract class Reaction extends Object {
    /** DOCUMENT ME! */
    private double energy; //the energy that is produced or consumed in the reaction

    //reaction is an equilibrium that leads products to reactants
    /**
     * Creates a new Reaction object.
     */
    public Reaction() {
        this.energy = 0;
    }

    //check that the reactants can actually produce the products (equation is balanced)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract boolean isValid();

    //the energy that is consumed or produced by the atomistic transformation of reactants into products
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param energy DOCUMENT ME!
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    //tries to compute the energy out of the reactants and products
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract double computeEnergy();
}
