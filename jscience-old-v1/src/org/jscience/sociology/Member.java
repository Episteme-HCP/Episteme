package org.jscience.sociology;

import org.jscience.biology.Individual;

import org.jscience.economics.money.Money;


/**
 * The Member class provides someone who is part of a ritual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Member extends Role {
    /** DOCUMENT ME! */
    private Money price; //to participate in the celebration

/**
     * Creates a new Member object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Member(Individual individual, Celebration situation) {
        super(individual, "Member", situation, Role.CLIENT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Money getPrice() {
        return price;
    }

    /**
     * DOCUMENT ME!
     *
     * @param price DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setPrice(Money price) {
        if (price != null) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("You can't set a null price.");
        }
    }
}
