package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;

/**
 * A chemical bond between two atoms.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class Bond {

    private final Atom atom1;
    private final Atom atom2;
    private final BondOrder order;

    public enum BondOrder {
        SINGLE(1),
        DOUBLE(2),
        TRIPLE(3),
        AROMATIC(1.5),
        COORDINATION(1); // Dative bond

        private final double electrons;

        BondOrder(double electrons) {
            this.electrons = electrons;
        }

        public double getElectronPairs() {
            return electrons;
        }
    }

    public Bond(Atom atom1, Atom atom2) {
        this(atom1, atom2, BondOrder.SINGLE);
    }

    public Bond(Atom atom1, Atom atom2, BondOrder order) {
        this.atom1 = atom1;
        this.atom2 = atom2;
        this.order = order;
    }

    public Atom getAtom1() {
        return atom1;
    }

    public Atom getAtom2() {
        return atom2;
    }

    public BondOrder getOrder() {
        return order;
    }

    /**
     * Bond length (distance between atoms).
     */
    public Quantity<Length> getLength() {
        return atom1.distanceTo(atom2);
    }

    /**
     * Returns the other atom in the bond.
     */
    public Atom getOtherAtom(Atom atom) {
        if (atom == atom1)
            return atom2;
        if (atom == atom2)
            return atom1;
        throw new IllegalArgumentException("Atom not in this bond");
    }

    /**
     * Is this atom part of the bond?
     */
    public boolean contains(Atom atom) {
        return atom == atom1 || atom == atom2;
    }

    @Override
    public String toString() {
        String bondSymbol = switch (order) {
            case SINGLE -> "-";
            case DOUBLE -> "=";
            case TRIPLE -> "≡";
            case AROMATIC -> "~";
            case COORDINATION -> "→";
        };
        return atom1.getElement().getSymbol() + bondSymbol + atom2.getElement().getSymbol();
    }
}
