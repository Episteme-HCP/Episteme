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
package org.jscience.sports;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class SportsDomainTest {

    @Test
    public void testMatchScoring() {
        Sport soccer = Sport.SOCCER;

        Team home = new Team("Home FC", soccer, "City A");
        Team away = new Team("Away Utd", soccer, "City B");

        LocalDateTime now = LocalDateTime.now();
        Match match = new Match(soccer, now, home.getName(), away.getName());

        assertEquals(Match.Status.SCHEDULED, match.getStatus());

        match.setScore(3, 1);

        assertEquals(Match.Status.COMPLETED, match.getStatus());
        assertEquals("Home FC", match.getWinner());
        assertFalse(match.isTie());
    }

    @Test
    public void testAthleteAndTeam() {
        Sport basket = Sport.BASKETBALL;

        Athlete player = new Athlete("Mike Jordan", basket, "USA");

        assertEquals("Basketball", player.getSport().getName());
        assertEquals("USA", player.getNationality());

        Team bulls = new Team("Chicago Bulls", basket, "Chicago");
        bulls.addPlayer(player.getName());

        assertEquals(1, bulls.getRosterSize());
        assertTrue(bulls.getPlayers().contains("Mike Jordan"));
    }
}
