package org.jscience.chemistry;

import java.util.Iterator;
import java.util.Set;

/**
 * The NuclearReaction class provides support for atomic transition between elements.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//nuclides as well as software are available from http://www.nea.fr/html/dbdata/projects/nds_jef.htm
//and http://www.nea.fr/janis/welcome.html

//also look at http://wwwndc.tokai.jaeri.go.jp/CN03/index.html or http://ie.lbl.gov/toi/pdf/chart.pdf or http://atom.kaeri.re.kr/
//you can also look at http://www.webelements.com/ or http://environmentalchemistry.com/yogi/periodic/Hg-pg2.html
public class NuclearReaction extends Reaction {

    public final static int ALPHA = 1;// Helium 4
    public final static int BETA_PLUS = 2;
    public final static int BETA_MINUS = 3;//only by artificial elements decay
    public final static int GAMMA = 4;//photon emitted when returning to normal state

    private Set reactants;
    private Set products;

    private double halfLife;

    public NuclearReaction(Set reactants, Set products) {

        //here reactants and products are Elements
        Iterator iterator;
        boolean valid;

        if ((reactants != null) && (reactants.size() > 0) && (products != null) && (products.size() > 0)) {
            iterator = reactants.iterator();
            valid = true;
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof XXXXXXXXXXXXXXX;
            }
            iterator = products.iterator();
            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof XXXXXXXXXXXXXXX;
            }
            if (valid) {
                this.reactants = reactants;
                this.products = products;
                halfLife = 0;
            } else
                throw new IllegalArgumentException("Nuclear reactants and products Sets must contain only Elements.");
        } else
            throw new IllegalArgumentException("Nuclear reactants and products can't be empty sets.");

        //one reactant and a decay type
        XXX

    }

    public Set getReactants() {

        return reactants;

    }

    public Set getProducts() {

        return products;

    }

    //check that the reactants can actually produce the products (equation is balanced)
    //note that a valid reaction may still not occur because its cinectic processes are too slow
    public boolean isValid() {
        XX
    }

    public double computeEnergy() {

        //delta mC2
        XXX
    }

    public double getHalfLife() {

        return halfLife;

    }

    public void setHalfLife(double halfLife) {

        this.halfLife = halfLife;

    }

    //compute age (carbon14 like)
    public double computeAge() {

        return XXXXX
        Math.ln(2) / getHalfLife();

    }

    //num is the initial number of atoms
    //time is the elapsed time
    //return the number of remaining atoms that haven't decayed
    public int computeRemains(int num, double time) {

        return Math.floor(num * Math.exp(-halfLife * time));

    }

}
