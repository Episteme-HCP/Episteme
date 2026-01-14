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

package org.jscience.economics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.geography.Place;
import org.jscience.sociology.Person;

import java.time.LocalDate;

public class EconomicsRestorationTest {

    @Test
    public void testFactoryAndWorkers() {
        // Setup
        Place london = new Place("London", Place.Type.CITY);
        Money initialCapital = Money.usd(10000.0);
        Factory widgetFactory = new Factory("Acme Widgets", london, initialCapital);

        Person alice = new Person("A001", "Alice", Person.Gender.FEMALE, LocalDate.of(1990, 5, 20), "USA");
        // Person bob = new Person("B002", "Bob", Person.Gender.MALE, LocalDate.of(1985,
        // 8, 15), "UK");

        // Test Worker Assignment
        Worker workerAlice = new Worker(alice, widgetFactory, "Manager", Money.usd(50000));
        widgetFactory.addWorker(workerAlice);

        assertEquals(1, widgetFactory.getWorkers().size());
        assertEquals(widgetFactory, workerAlice.getOrganization());

        // Test Production
        widgetFactory.addProductionType("Widget");
        assertTrue(widgetFactory.canProduce("Widget"));

        Money productionCost = Money.usd(100.0);
        MaterialResource<?> widget = widgetFactory.produce("Widget", 10.0, productionCost);

        assertNotNull(widget);
        assertEquals("Widget", widget.getName());
        assertEquals(10.0, widget.getQuantity().doubleValue(), 0.001);
        assertEquals(london, widget.getLocation());
        assertEquals(productionCost, widget.getValue());

        // Verify Capital Deduction
        // getAmount returns Real, need doubleValue for comparison
        assertEquals(9900.0, widgetFactory.getCapital().getAmount().doubleValue(), 0.001);

        // Verify Inventory
        assertTrue(widgetFactory.getInventory().contains(widget));
    }
}


