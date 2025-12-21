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
