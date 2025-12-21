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

        assertEquals(60.0, election.getVoteShare(blueParty.getName()).doubleValue(), 0.01);
        assertEquals(40.0, election.getVoteShare(redParty.getName()).doubleValue(), 0.01);

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
