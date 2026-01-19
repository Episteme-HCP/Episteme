package org.jscience.sports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Algorithms for scheduling sports competitions.
 */
public final class TournamentSchedulers {

    private TournamentSchedulers() {}

    /**
     * Represents a scheduled match between two teams.
     */
    public record ScheduledMatch(Team home, Team away, int round) {}

    /**
     * Generates a Round Robin schedule for a list of teams.
     * Uses the "Circle Method" algorithm.
     * 
     * @param teams List of teams to schedule.
     * @return List of scheduled matches.
     */
    public static List<ScheduledMatch> generateRoundRobin(List<Team> teams) {
        if (teams == null || teams.size() < 2) return Collections.emptyList();
        
        List<Team> list = new ArrayList<>(teams);
        if (list.size() % 2 != 0) {
            list.add(null); // Bye team
        }
        
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
            // Rotate teams (keep first team fixed)
            Team last = list.remove(numTeams - 1);
            list.add(1, last);
        }
        
        return schedule;
    }

    /**
     * Generates a Single Elimination bracket.
     */
    public static List<List<ScheduledMatch>> generateSingleElimination(List<Team> teams) {
        if (teams == null || teams.size() < 2) return Collections.emptyList();
        
        List<Team> bracket = new ArrayList<>(teams);
        // Pad to power of 2
        int size = 1;
        while (size < bracket.size()) size *= 2;
        while (bracket.size() < size) bracket.add(null); // Byes
        
        Collections.shuffle(bracket);
        
        List<List<ScheduledMatch>> rounds = new ArrayList<>();
        int round = 1;
        
        while (bracket.size() > 1) {
            List<ScheduledMatch> roundMatches = new ArrayList<>();
            List<Team> winners = new ArrayList<>();
            
            for (int i = 0; i < bracket.size(); i += 2) {
                Team t1 = bracket.get(i);
                Team t2 = bracket.get(i + 1);
                
                if (t1 == null) {
                    winners.add(t2);
                } else if (t2 == null) {
                    winners.add(t1);
                } else {
                    roundMatches.add(new ScheduledMatch(t1, t2, round));
                    winners.add(t1); // Placeholder - actual winner determined later
                }
            }
            
            rounds.add(roundMatches);
            bracket = winners;
            round++;
        }
        
        return rounds;
    }

    /**
     * Generates a Swiss System pairing for one round.
     * Teams are paired by similar scores.
     */
    public static List<ScheduledMatch> generateSwissRound(List<Team> teams, 
            Map<Team, Integer> currentScores, int roundNumber) {
        
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
                    paired.add(t1);
                    paired.add(t2);
                    break;
                }
            }
        }
        
        return matches;
    }
}
