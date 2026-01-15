package org.jscience.biology;

import java.util.Set;


/**
 * An interface used to catch the common system underlying DNA and RNA.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public abstract class Chain implements Cloneable {
    //the bases in this chain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Base[] getBases();

    //the number of bases in this chain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getLength();

    //the atomic complement
    /**
     * DOCUMENT ME!
     *
     * @param base DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Base getComplementary(Base base);

    //the complement Chain
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Chain getComplementary();

    //the set of valid Bases classes for this molecule
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Set getValidBases();

    //length and all bases must be of same kind
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Chain value;
        int i;
        boolean result;

        if ((o != null) && (o instanceof Chain)) {
            value = (Chain) o;

            result = (getLength() == value.getLength());
            i = 0;

            while ((i < getLength()) && result) {
                result = (getBases()[i] == value.getBases()[i]);
                i++;
            }

            return result;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object clone();
}
