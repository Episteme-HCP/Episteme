package org.jscience.chemistry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodicTableLimitTest {

    @Test
    public void testElementCount() {
        int count = PeriodicTable.getElementCount();
        System.out.println("Loaded Elements: " + count);
        assertTrue(count >= 118, "Periodic Table should have at least 118 elements, found " + count);
    }

    @Test
    public void testSpecificElements() {
        assertNotNull(PeriodicTable.getElement("Lawrencium"), "Element 103 (Lr) missing");
        assertNotNull(PeriodicTable.getElement("Rutherfordium"), "Element 104 (Rf) missing");
        assertNotNull(PeriodicTable.getElement("Oganesson"), "Element 118 (Og) missing");
    }
}
