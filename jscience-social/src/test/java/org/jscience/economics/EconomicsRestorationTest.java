package org.jscience.economics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.jscience.geography.Place;
import org.jscience.sociology.Person;
import org.jscience.economics.Currency; // Correct import
import javax.measure.Quantity;
import java.time.LocalDate;

public class EconomicsRestorationTest {

    @Test
    public void testFactoryAndWorkers() {
        // Setup
        Place london = new Place("London", Place.Type.CITY);
        Money initialCapital = new Money(10000.0, Currency.USD);
        Factory widgetFactory = new Factory("Acme Widgets", london, initialCapital);

        Person alice = new Person("A001", "Alice", Person.Gender.FEMALE, LocalDate.of(1990, 5, 20), "USA");
        // Person bob = new Person("B002", "Bob", Person.Gender.MALE, LocalDate.of(1985,
        // 8, 15), "UK");

        // Test Worker Assignment
        Worker workerAlice = new Worker(alice, widgetFactory, "Manager", new Money(50000, Currency.USD));
        widgetFactory.addWorker(workerAlice);

        assertEquals(1, widgetFactory.getWorkers().size());
        assertEquals(widgetFactory, workerAlice.getOrganization());

        // Test Production
        widgetFactory.addProductionType("Widget");
        assertTrue(widgetFactory.canProduce("Widget"));

        Money productionCost = new Money(100.0, Currency.USD);
        MaterialResource<?> widget = widgetFactory.produce("Widget", 10.0, productionCost);

        assertNotNull(widget);
        assertEquals("Widget", widget.getName());
        assertEquals(10.0, widget.getQuantity());
        assertEquals(london, widget.getLocation());
        assertEquals(productionCost, widget.getValue());

        // Verify Capital Deduction
        // getAmount returns Real, need doubleValue for comparison
        assertEquals(9900.0, widgetFactory.getCapital().getAmount().doubleValue(), 0.001);

        // Verify Inventory
        assertTrue(widgetFactory.getInventory().contains(widget));
    }
}
