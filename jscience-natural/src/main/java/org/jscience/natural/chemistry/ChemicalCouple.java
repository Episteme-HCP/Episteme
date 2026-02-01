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

package org.jscience.natural.chemistry;

import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.ElectricPotential;
import org.jscience.core.measure.quantity.Dimensionless;

/**
 * Represents a chemical couple (e.g., Oxidant/Reductant or Acid/Base).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ChemicalCouple {

    private final Compound species1;
    private final Compound species2;
    private final CoupleType type;
    
    // For Redox: Standard Potential E0
    private Quantity<ElectricPotential> standardPotential;
    
    // For Acid/Base: pKa
    private Quantity<Dimensionless> pKa;

    public enum CoupleType {
        REDOX,      // Oxidant / Reductant
        ACID_BASE   // Acid / Base
    }


    private ChemicalCouple(Compound species1, Compound species2, CoupleType type, 
                          Quantity<ElectricPotential> e0, Quantity<Dimensionless> pka) {
        this.species1 = species1;
        this.species2 = species2;
        this.type = type;
        this.standardPotential = e0;
        this.pKa = pka;
    }

    public static ChemicalCouple ofRedox(Compound oxidant, Compound reductant, Quantity<ElectricPotential> e0) {
        return new ChemicalCouple(oxidant, reductant, CoupleType.REDOX, e0, null);
    }

    public static ChemicalCouple ofAcidBase(Compound acid, Compound base, Quantity<Dimensionless> pka) {
        return new ChemicalCouple(acid, base, CoupleType.ACID_BASE, null, pka);
    }

    public Compound getSpecies1() {
        return species1;
    }

    public Compound getSpecies2() {
        return species2;
    }

    public CoupleType getType() {
        return type;
    }

    public Quantity<ElectricPotential> getStandardPotential() {
        return standardPotential;
    }

    public Quantity<Dimensionless> getPKa() {
        return pKa;
    }
    
    public Compound getOxidant() {
        if (type != CoupleType.REDOX) throw new IllegalStateException("Not a redox couple");
        return species1;
    }

    public Compound getReductant() {
        if (type != CoupleType.REDOX) throw new IllegalStateException("Not a redox couple");
        return species2;
    }

    public Compound getAcid() {
        if (type != CoupleType.ACID_BASE) throw new IllegalStateException("Not an acid-base couple");
        return species1;
    }

    public Compound getBase() {
        if (type != CoupleType.ACID_BASE) throw new IllegalStateException("Not an acid-base couple");
        return species2;
    }

    @Override
    public String toString() {
        if (type == CoupleType.REDOX) {
            return species1 + "/" + species2 + " (E0=" + standardPotential + ")";
        } else {
            return species1 + "/" + species2 + " (pKa=" + pKa + ")";
        }
    }
}
