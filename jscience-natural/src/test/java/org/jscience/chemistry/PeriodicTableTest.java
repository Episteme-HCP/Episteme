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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.chemistry.loaders.ChemistryDataLoader;

public class PeriodicTableTest {

    @BeforeAll
    public static void setup() {
        ChemistryDataLoader.loadElements();
    }

    @Test
    public void testGetHydrogen() {
        Element h = PeriodicTable.getElement("Hydrogen");
        assertNotNull(h, "Hydrogen should be found");
        assertEquals("H", h.getSymbol());
        assertEquals("Hydrogen", h.getName());
        assertEquals(1, h.getAtomicNumber());
        // JSON loader uses Enum category
        assertEquals(Element.ElementCategory.NONMETAL, h.getCategory());

        // Check properties
        // Atomic Radius not in JSON yet
        // assertNotNull(h.getAtomicRadius(), "Atomic radius should be present");

        assertNotNull(h.getMeltingPoint(), "Melting point should be present");
        // 14.01 K in JSON
        assertEquals(13.81, h.getMeltingPoint().getValue().doubleValue(), 0.1);
    }

    @Test
    public void testGetSymbol() {
        Element he = PeriodicTable.getElement("He");
        assertNotNull(he, "Helium should be found by symbol");
        assertEquals("Helium", he.getName());
    }
}
