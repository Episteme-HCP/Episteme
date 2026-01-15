package org.jscience.mathematics.algebraic.lattices;

import org.jscience.mathematics.algebraic.numbers.Boolean;


/**
 * The BooleanLattice class encapsulates the tradionnal boolean algebra.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class BooleanLogic extends Object implements BooleanAlgebra {
    /** DOCUMENT ME! */
    public final static Boolean FALSE = new Boolean(false);

    /** DOCUMENT ME! */
    public final static Boolean TRUE = new Boolean(true);

    /** DOCUMENT ME! */
    private static BooleanLogic _instance;

/**
     * Constructs a boolean logic.
     */
    private BooleanLogic() {
    }

    /**
     * Constructs a algebra of boolean logic numbers. Singleton.
     *
     * @return DOCUMENT ME!
     */
    public static final BooleanLogic getInstance() {
        if (_instance == null) {
            synchronized (BooleanLogic.class) {
                if (_instance == null) {
                    _instance = new BooleanLogic();
                }
            }
        }

        return _instance;
    }

    /**
     * Returns the boolean number zero.
     *
     * @return DOCUMENT ME!
     */
    public MeetSemiLattice.Member zero() {
        return FALSE;
    }

    /**
     * Returns true if the boolean number is equal to zero, also named
     * false.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZero(MeetSemiLattice.Member g) {
        return FALSE.equals(g);
    }

    /**
     * Returns the boolean number one.
     *
     * @return DOCUMENT ME!
     */
    public JoinSemiLattice.Member one() {
        return TRUE;
    }

    /**
     * Returns true if the boolean number is equal to one, also named
     * true.
     *
     * @param g DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOne(JoinSemiLattice.Member g) {
        return TRUE.equals(g);
    }

    /**
     * Returns true if one boolean number is the negative of the other.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isComplement(BooleanAlgebra.Member a, BooleanAlgebra.Member b) {
        return a.equals(b.complement());
    }
}
