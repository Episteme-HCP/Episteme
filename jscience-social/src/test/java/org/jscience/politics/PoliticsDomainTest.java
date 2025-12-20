package org.jscience.politics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class PoliticsDomainTest {

    @Test
    public void testElectionResults() {
        LocalDate date = LocalDate.of(2024, 11, 5);
        Election election = new Election("General Election", Election.Type.PRESIDENTIAL, date);

        Party blueParty = new Party("Blue Party", Party.Ideology.LIBERAL);
        Party redParty = new Party("Red Party", Party.Ideology.CONSERVATIVE);

        election.setTotalVotes(1000);
        election.addResult(blueParty.getName(), 600);
        election.addResult(redParty.getName(), 400);

        assertEquals(60.0, election.getVoteShare(blueParty.getName()), 0.01);
        assertEquals(40.0, election.getVoteShare(redParty.getName()), 0.01);

        election.setWinner(blueParty.getName());
        assertEquals("Blue Party", election.getWinner());
    }

    @Test
    public void testConstituency() {
        org.jscience.geography.Place region = new org.jscience.geography.Place("District 9",
                org.jscience.geography.Place.Type.REGION);
        Constituency c = new Constituency("D9", region, 10000);

        assertEquals(10000, c.getPopulation());
        assertTrue(c.getElectorateSize() <= c.getPopulation());
        assertEquals(region, c.getArea());
    }
}
