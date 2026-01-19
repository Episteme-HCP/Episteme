package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Draft and transfer simulation for sports leagues.
 */
public final class DraftSimulator {

    private DraftSimulator() {}

    public record Prospect(
        String name,
        String position,
        double overallRating, // 0-100
        double potential,     // 0-100
        int age,
        Map<String, Double> attributes
    ) {}

    public record DraftPick(int round, int pickNumber, String team, Prospect prospect) {}

    /**
     * Simulates a serpentine draft (1st pick in round 1 gets last in round 2).
     */
    public static List<DraftPick> simulateSerpentineDraft(
            List<String> teamOrder, List<Prospect> prospects, int rounds) {
        
        List<DraftPick> results = new ArrayList<>();
        List<Prospect> available = new ArrayList<>(prospects);
        available.sort((a, b) -> Double.compare(b.overallRating(), a.overallRating()));
        
        for (int round = 1; round <= rounds; round++) {
            List<String> roundOrder = new ArrayList<>(teamOrder);
            if (round % 2 == 0) {
                Collections.reverse(roundOrder);
            }
            
            int pickInRound = 1;
            for (String team : roundOrder) {
                if (available.isEmpty()) break;
                
                // Simple AI: pick best available with some randomness
                int index = selectProspect(available, team, round);
                Prospect pick = available.remove(index);
                
                int overallPick = (round - 1) * teamOrder.size() + pickInRound;
                results.add(new DraftPick(round, overallPick, team, pick));
                pickInRound++;
            }
        }
        
        return results;
    }

    /**
     * Simulates a lottery draft (weighted by inverse standings).
     */
    public static List<String> simulateLotteryOrder(Map<String, Integer> teamStandings, 
            int lotteryPicks) {
        
        List<String> teams = new ArrayList<>(teamStandings.keySet());
        int totalTeams = teams.size();
        
        // Calculate lottery weights (worse teams get more chances)
        Map<String, Integer> weights = new HashMap<>();
        for (String team : teams) {
            int standing = teamStandings.get(team);
            weights.put(team, totalTeams - standing + 1);
        }
        
        List<String> lotteryResults = new ArrayList<>();
        Random random = new Random();
        List<String> remaining = new ArrayList<>(teams);
        
        for (int i = 0; i < lotteryPicks && !remaining.isEmpty(); i++) {
            int totalWeight = remaining.stream().mapToInt(weights::get).sum();
            int pick = random.nextInt(totalWeight);
            
            int cumulative = 0;
            for (String team : remaining) {
                cumulative += weights.get(team);
                if (pick < cumulative) {
                    lotteryResults.add(team);
                    remaining.remove(team);
                    break;
                }
            }
        }
        
        // Remaining teams in reverse standings order
        remaining.sort((a, b) -> teamStandings.get(a) - teamStandings.get(b));
        Collections.reverse(remaining);
        lotteryResults.addAll(remaining);
        
        return lotteryResults;
    }

    /**
     * Calculates trade value of picks.
     */
    public static Real calculatePickValue(int pickNumber, int totalPicks) {
        // Trade value formula (exponential decay)
        double value = 1000 * Math.exp(-0.05 * (pickNumber - 1));
        return Real.of(value);
    }

    /**
     * Suggests fair trade packages.
     */
    public static boolean isFairTrade(List<Integer> teamAPicks, List<Integer> teamBPicks, 
            int totalPicks, double maxImbalance) {
        
        double valueA = teamAPicks.stream()
            .mapToDouble(p -> calculatePickValue(p, totalPicks).doubleValue())
            .sum();
        double valueB = teamBPicks.stream()
            .mapToDouble(p -> calculatePickValue(p, totalPicks).doubleValue())
            .sum();
        
        double imbalance = Math.abs(valueA - valueB) / Math.max(valueA, valueB);
        return imbalance <= maxImbalance;
    }

    private static int selectProspect(List<Prospect> available, String team, int round) {
        Random random = new Random();
        // Better teams (later picks) reach more, worse teams pick best available
        int reachRange = Math.min(3, available.size());
        return random.nextInt(reachRange);
    }
}
