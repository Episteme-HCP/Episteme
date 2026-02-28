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

package org.episteme.social.sociology;

import java.util.Set;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Utility class for modeling social capital and analyzing network properties.
 * Provides metrics for network density, trust, and capital types (Bonding vs Bridging).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
public final class SocialCapitalModel {

    private SocialCapitalModel() {}

    /**
     * Calculates the Network Density.
     * Defined as the ratio of actual connections to the total number of possible connections.
     *
     * @param nodes    number of individuals (nodes)
     * @param edges    number of relationships (edges)
     * @param directed true if relationships are directed (A->B != B->A), false otherwise
     * @return the density value between 0.0 and 1.0
     */
    public static Real calculateDensity(int nodes, int edges, boolean directed) {
        if (nodes < 2) return Real.ZERO;
        Real n = Real.of(nodes);
        Real e = Real.of(edges);
        Real possible = directed ? n.multiply(n.subtract(Real.ONE)) 
                                : n.multiply(n.subtract(Real.ONE)).divide(Real.of(2));
        return e.divide(possible);
    }

    /**
     * Calculates a Trust Index based on reciprocity in relationships.
     *
     * @param mutualLinks count of bidirectional (reciprocated) links
     * @param totalLinks  count of total links
     * @return the reciprocity index
     */
    public static Real reciprocityIndex(int mutualLinks, int totalLinks) {
        if (totalLinks == 0) return Real.ZERO;
        return Real.of(2).multiply(Real.of(mutualLinks)).divide(Real.of(totalLinks));
    }

    /**
     * Calculates the "Bonding Social Capital" of a person within a specific SociologicalGroup.
     * This is measured as the ratio of ties within the SociologicalGroup to the SociologicalGroup size.
     * 
     * @param person the person to analyze
     * @param SociologicalGroup  the SociologicalGroup they belong to
     * @param network the social network
     * @return internal tie ratio
     */
    public static Real calculateBondingCapital(Person person, SociologicalGroup SociologicalGroup, SocialNetwork network) {
        if (SociologicalGroup == null || SociologicalGroup.size() <= 1) return Real.ZERO;
        
        long internalTies = network.getNeighbors(person).stream()
                .filter(neighbor -> SociologicalGroup.getMembers().contains(neighbor))
                .count();
        
        return Real.of(internalTies).divide(Real.of(SociologicalGroup.size() - 1));
    }

    /**
     * Calculates the "Bridging Social Capital" of a person.
     * Measured as the number of ties to people outside their primary groups.
     * 
     * @param person the person to analyze
     * @param primaryGroups the groups the person belongs to
     * @param network the social network
     * @return count of external ties
     */
    public static Real calculateBridgingCapital(Person person, Set<SociologicalGroup> primaryGroups, SocialNetwork network) {
        Set<Person> neighbors = network.getNeighbors(person);
        long externalTies = neighbors.stream()
                .filter(neighbor -> primaryGroups.stream().noneMatch(g -> g.getMembers().contains(neighbor)))
                .count();
        
        return Real.of(externalTies);
    }

    /**
     * Estimates "Social Cohesion" of a SociologicalGroup.
     * Average of all internal bonding capital scores.
     * 
     * @param SociologicalGroup the SociologicalGroup
     * @param network the network
     * @return cohesion index
     */
    public static Real calculateSocialCohesion(SociologicalGroup SociologicalGroup, SocialNetwork network) {
        if (SociologicalGroup == null || SociologicalGroup.size() < 2) return Real.ONE;
        
        Real totalBonding = SociologicalGroup.getMembers().stream()
                .map(p -> calculateBondingCapital(p, SociologicalGroup, network))
                .reduce(Real.ZERO, Real::add);
                
        return totalBonding.divide(Real.of(SociologicalGroup.size()));
    }
}

