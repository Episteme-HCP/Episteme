/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.resources.minicatalogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.jscience.resources.MiniCatalog;

/**
 * Mini-catalog of common chemicals.
 * <p>
 * Provides offline access to a subset of common chemical compounds.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 2.0
 */
public class ChemicalCatalog implements MiniCatalog<ChemicalCatalog.ChemicalEntry> {

    public static class ChemicalEntry {
        public final String name;
        public final String formula;
        public final double molarMass; // g/mol
        public final String casNumber;

        public ChemicalEntry(String name, String formula, double molarMass, String casNumber) {
            this.name = name;
            this.formula = formula;
            this.molarMass = molarMass;
            this.casNumber = casNumber;
        }
    }

    private static final List<ChemicalEntry> CHEMICALS = new ArrayList<>();

    static {
        CHEMICALS.add(new ChemicalEntry("Water", "H2O", 18.015, "7732-18-5"));
        CHEMICALS.add(new ChemicalEntry("Carbon Dioxide", "CO2", 44.01, "124-38-9"));
        CHEMICALS.add(new ChemicalEntry("Oxygen", "O2", 31.999, "7782-44-7"));
        CHEMICALS.add(new ChemicalEntry("Nitrogen", "N2", 28.013, "7727-37-9"));
        CHEMICALS.add(new ChemicalEntry("Methane", "CH4", 16.04, "74-82-8"));
        CHEMICALS.add(new ChemicalEntry("Ethanol", "C2H5OH", 46.07, "64-17-5"));
        CHEMICALS.add(new ChemicalEntry("Glucose", "C6H12O6", 180.16, "50-99-7"));
        CHEMICALS.add(new ChemicalEntry("Sodium Chloride", "NaCl", 58.44, "7647-14-5"));
        CHEMICALS.add(new ChemicalEntry("Sulfuric Acid", "H2SO4", 98.079, "7664-93-9"));
        CHEMICALS.add(new ChemicalEntry("Ammonia", "NH3", 17.031, "7664-41-7"));
    }

    private static final ChemicalCatalog INSTANCE = new ChemicalCatalog();

    public static ChemicalCatalog getInstance() {
        return INSTANCE;
    }

    private ChemicalCatalog() {
    }

    @Override
    public List<ChemicalEntry> getAll() {
        return Collections.unmodifiableList(CHEMICALS);
    }

    @Override
    public Optional<ChemicalEntry> findByName(String name) {
        return CHEMICALS.stream()
                .filter(c -> c.name.equalsIgnoreCase(name) || c.formula.equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public int size() {
        return CHEMICALS.size();
    }
}

