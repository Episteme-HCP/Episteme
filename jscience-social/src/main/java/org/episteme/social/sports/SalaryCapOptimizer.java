/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides algorithms for optimizing sports rosters under financial salary cap constraints.
 * Uses a greedy approximation of the Knapsack problem.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class SalaryCapOptimizer {

    private SalaryCapOptimizer() {}

    /** Data model for a player's performance and salary. */
    public record Player(String name, double rating, double salary) implements Serializable {}

    /**
     * Optimizes a roster by selecting the best rated players within a total budget.
     * Uses a efficiency-based greedy approach (rating per dollar).
     * 
     * @param pool   available players
     * @param budget maximum total salary spend
     * @return the optimized roster of players
     */
    public static List<Player> optimizeRoster(List<Player> pool, double budget) {
        if (pool == null) return List.of();
        
        List<Player> sorted = new ArrayList<>(pool);
        // Sort by value/efficiency (rating / salary)
        sorted.sort((p1, p2) -> {
            double efficiency1 = p1.salary() > 0 ? p1.rating() / p1.salary() : Double.MAX_VALUE;
            double efficiency2 = p2.salary() > 0 ? p2.rating() / p2.salary() : Double.MAX_VALUE;
            return Double.compare(efficiency2, efficiency1);
        });
        
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

