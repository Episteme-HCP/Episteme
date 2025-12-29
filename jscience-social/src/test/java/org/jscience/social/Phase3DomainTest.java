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
