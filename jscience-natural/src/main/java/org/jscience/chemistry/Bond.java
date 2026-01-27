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

package org.jscience.chemistry;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * A chemical bond between two atoms.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Bond implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final Atom atom1;
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final Atom atom2;
    @Attribute
    private final BondType type;

    public Bond(Atom atom1, Atom atom2) {
        this(atom1, atom2, BondType.SINGLE);
    }

    public Bond(Atom atom1, Atom atom2, BondType type) {
        this.id = new SimpleIdentification("BOND:" + UUID.randomUUID());
        setName("Bond " + atom1.getElement().getSymbol() + "-" + atom2.getElement().getSymbol());
        this.atom1 = Objects.requireNonNull(atom1);
        this.atom2 = Objects.requireNonNull(atom2);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }



    public Atom getAtom1() {
        return atom1;
    }

    public Atom getAtom2() {
        return atom2;
    }

    public BondType getType() {
        return type;
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
        String bondSymbol;
        if (type == BondType.SINGLE) bondSymbol = "-";
        else if (type == BondType.DOUBLE) bondSymbol = "=";
        else if (type == BondType.TRIPLE) bondSymbol = "#";
        else if (type == BondType.AROMATIC) bondSymbol = "~";
        else if (type == BondType.COORDINATION) bondSymbol = "->";
        else bondSymbol = "-";
        return atom1.getElement().getSymbol() + bondSymbol + atom2.getElement().getSymbol();
    }
}


