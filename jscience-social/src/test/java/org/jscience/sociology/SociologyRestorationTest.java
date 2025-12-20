package org.jscience.sociology;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class SociologyRestorationTest {

    @Test
    public void testSocialRolesAndSituations() {
        // Setup
        Person alice = new Person("P001", "Alice", Person.Gender.FEMALE, LocalDate.of(1990, 1, 1), "USA");
        Person bob = new Person("P002", "Bob", Person.Gender.MALE, LocalDate.of(1985, 1, 1), "UK");

        Situation dinnerParty = new Situation("Dinner Party", "A social gathering");

        // Assign Roles
        // Alice is the host (Supervisor-like or Server in legacy terms, let's use
        // SERVER for host)
        Role hostRole = new Role(alice, "Host", dinnerParty, Role.SERVER);

        // Bob is the guest (Client)
        Role guestRole = new Role(bob, "Guest", dinnerParty, Role.CLIENT);

        // Verify Situation has roles
        assertTrue(dinnerParty.getRoles().contains(hostRole));
        assertTrue(dinnerParty.getRoles().contains(guestRole));

        // Verify Persons have roles
        assertTrue(alice.getStructuralRoles().contains(hostRole));
        assertTrue(bob.getStructuralRoles().contains(guestRole));

        // Verify Linkage
        assertEquals(dinnerParty, hostRole.getSituation());
        assertEquals(alice, hostRole.getPerson());

        // Test Ritual
        Ritual wedding = new Ritual("Wedding", "A marriage ceremony");
        assertTrue(wedding instanceof Situation);
    }
}
