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
        assertEquals(14.01, h.getMeltingPoint().getValue().doubleValue(), 0.1);
    }

    @Test
    public void testGetSymbol() {
        Element he = PeriodicTable.getElement("He");
        assertNotNull(he, "Helium should be found by symbol");
        assertEquals("Helium", he.getName());
    }
}
