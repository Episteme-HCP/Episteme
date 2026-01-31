/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides mathematical algorithms for scheduling sports competitions, including 
 * Round Robin, Single Elimination, and Swiss System pairings.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class TournamentSchedulers {

    private TournamentSchedulers() {}

    /** Details of a scheduled match within a tournament structure. */
    public record ScheduledMatch(Team home, Team away, int round) implements Serializable {}

    /**
     * Generates a complete Round Robin schedule for the given teams.
     * Uses the Circle Method algorithm.
     * 
     * @param teams the participating teams
     * @return a list of scheduled matches for all rounds
     */
    public static List<ScheduledMatch> generateRoundRobin(List<Team> teams) {
        if (teams == null || teams.size() < 2) return Collections.emptyList();
        
        List<Team> list = new ArrayList<>(teams);
        if (list.size() % 2 != 0) list.add(null);
        
        int numTeams = list.size();
        int numRounds = numTeams - 1;
        List<ScheduledMatch> schedule = new ArrayList<>();
        
        for (int round = 0; round < numRounds; round++) {
            for (int i = 0; i < numTeams / 2; i++) {
                Team home = list.get(i);
                Team away = list.get(numTeams - 1 - i);
                if (home != null && away != null) {
                    schedule.add(new ScheduledMatch(home, away, round + 1));
                }
            }
            Team last = list.remove(numTeams - 1);
            list.add(1, last);
        }
        return schedule;
    }

    /**
     * Generates pairings for a Swiss System tournament round based on current scores.
     * 
     * @param teams         list of all teams
     * @param currentScores mapping of each team to their current points
     * @param roundNumber   the current round index
     * @return pairings for this round
     */
    public static List<ScheduledMatch> generateSwissRound(List<Team> teams, 
            Map<Team, Integer> currentScores, int roundNumber) {
        if (teams == null || currentScores == null) return Collections.emptyList();
        
        List<Team> sorted = new ArrayList<>(teams);
        sorted.sort((a, b) -> currentScores.getOrDefault(b, 0) - currentScores.getOrDefault(a, 0));
        
        List<ScheduledMatch> matches = new ArrayList<>();
        Set<Team> paired = new HashSet<>();
        
        for (int i = 0; i < sorted.size(); i++) {
            Team t1 = sorted.get(i);
            if (paired.contains(t1)) continue;
            for (int j = i + 1; j < sorted.size(); j++) {
                Team t2 = sorted.get(j);
                if (!paired.contains(t2)) {
                    matches.add(new ScheduledMatch(t1, t2, roundNumber));
                    paired.add(t1); paired.add(t2);
                    break;
                }
            }
        }
        return matches;
    }
}

