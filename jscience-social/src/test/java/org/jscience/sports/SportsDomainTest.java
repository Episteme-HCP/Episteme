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
