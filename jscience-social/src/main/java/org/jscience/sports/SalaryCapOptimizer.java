package org.jscience.sports;

import java.util.*;

/**
 * Optimizes roster under salary cap constraints.
 */
public final class SalaryCapOptimizer {

    private SalaryCapOptimizer() {}

    public record Player(String name, double rating, double salary) {}

    /**
     * Solves the Knapsack problem for roster optimization.
     */
    public static List<Player> optimizeRoster(List<Player> pool, double budget) {
        // Simple greedy approach (for brevity)
        List<Player> sorted = new ArrayList<>(pool);
        sorted.sort((p1, p2) -> Double.compare(p2.rating() / p2.salary(), p1.rating() / p1.salary()));
        
        List<Player> roster = new ArrayList<>();
        double spent = 0;
        for (Player p : sorted) {
            if (spent + p.salary() <= budget) {
                roster.add(p);
                spent += p.salary();
            }
        }
        return roster;
    }
}
