/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.social;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.jscience.geography.*;
import org.jscience.sociology.*;
import org.jscience.politics.*;
import org.jscience.politics.military.*;
import org.jscience.linguistics.Language;

public class Phase4DomainTest {

    @Test
    public void testGeographyExpansion() {
        Country france = new Country("France", "FR");
        City paris = new City("Paris", france, "75000", 2200000);
        Address address = new Address("123 Champs Elysees", paris, "75008");

        assertEquals(paris, address.getCity());
        assertEquals("Paris", paris.getName());
        assertEquals(france, paris.getExactCountry());
        assertTrue(address.toString().contains("Paris"));
    }

    @Test
    public void testSociologyExpansion() {
        Culture western = new Culture("Western", Language.ENGLISH);
        western.addCelebration("New Year");

        assertEquals("Western", western.getName());
        assertTrue(western.getCelebrations().contains("New Year"));
    }

    @Test
    public void testPoliticsExpansion() {
        Country usa = new Country("USA", "US");
        Election election = new Election("2024 Presidential", usa, LocalDate.of(2024, 11, 5));
        Ballot ballot = new Ballot(election);

        ballot.addChoice("Alice");
        ballot.addChoice("Bob");

        ballot.castVote("Alice");
        ballot.castVote("Alice");
        ballot.castVote("Bob");

        assertEquals(2, election.getResults().get("Alice"));
        assertEquals("Alice", election.getWinner());
    }

    @Test
    public void testMilitaryDomain() {
        Country uk = new Country("UK", "GB");
        Country germany = new Country("Germany", "DE");
        Place europe = new Place("Europe", Place.Type.CONTINENT); // Fixed constructor

        Conflict ww2 = new Conflict("WW2", europe, LocalDate.of(1939, 9, 1));
        ww2.addBelligerent(uk);
        ww2.addBelligerent(germany);

        assertTrue(ww2.isActive());
        assertEquals(2, ww2.getBelligerents().size());
        assertEquals("WW2", ww2.getName());

        ww2.setEndDate(LocalDate.of(1945, 5, 8));
        assertFalse(ww2.isActive());
    }
}
