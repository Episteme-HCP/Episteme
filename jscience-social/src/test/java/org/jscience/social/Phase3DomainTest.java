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

import org.jscience.economics.*;
import org.jscience.arts.*;
import org.jscience.sports.*;
import org.jscience.bibliography.*;
import org.jscience.sociology.Person;

public class Phase3DomainTest {

    @Test
    public void testEconomicsDomain() {
        Currency usd = Currency.USD;
        assertEquals("USD", usd.getId());
        assertEquals("USD", usd.getCode());

        // Ensure Money exists and works (basic check)
        Money price = Money.usd(100.0);
        assertNotNull(price);
        assertEquals("USD", price.getCurrency().getCode());
    }

    @Test
    public void testArtsDomain() {
        Person artist = new Person("Da Vinci", Person.Gender.MALE);
        Artwork monaLisa = new Artwork("Mona Lisa", artist, 1503, Artwork.Type.PAINTING);

        assertEquals("Mona Lisa", monaLisa.getTitle());
        assertEquals(artist, monaLisa.getCreator());
        assertEquals(Artwork.Type.PAINTING, monaLisa.getType());
        assertNotNull(monaLisa.getId());
    }

    @Test
    public void testSportsDomain() {
        Sport soccer = new Sport("Soccer", true);
        assertTrue(soccer.isTeamSport());
        assertEquals("Soccer", soccer.getId());

        Team team = new Team("Real Madrid", soccer);
        Person player = new Person("Ronaldo", Person.Gender.MALE);
        team.addMember(player);

        assertEquals(1, team.getMembers().size());
        assertEquals(soccer, team.getSport());
    }

    @Test
    public void testBibliographyDomain() {
        // SimpleCitation is immutable, so we pass authors in constructor or builder
        // For this test, we simplify usage to match current implementation
        // Person darwin = new Person("Darwin", Person.Gender.MALE);
        // Assuming author is a String in SimpleCitation for now based on previous view
        SimpleCitation book = new SimpleCitation("ISBN-123", "The Origin of Species", "Darwin", "1859", null);

        assertEquals("ISBN-123", book.getKey());
        assertEquals("1859", book.getYear());
        assertTrue(book.toString().contains("Darwin") || book.getAuthor().contains("Darwin"));
    }
}


