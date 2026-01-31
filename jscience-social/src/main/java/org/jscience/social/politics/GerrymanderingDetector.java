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

package org.jscience.social.politics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides algorithms to detect gerrymandering and partisan bias in electoral district maps.
 * Includes implementations for Efficiency Gap, Mean-Median Difference, and Declination.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class GerrymanderingDetector {

    private GerrymanderingDetector() {}

    /** Data model for an individual electoral district. */
    public record District(
        String id,
        int totalVoters,
        int partyAVotes,
        int partyBVotes,
        double compactnessScore,
        List<double[]> boundary
    ) implements Serializable {}

    /** Comprehensive analysis result for a full district map. */
    public record GerrymanderAnalysis(
        double efficiencyGap,
        double meanMedianDiff,
        double declination,
        double overallCompactness,
        List<String> packedDistricts,
        List<String> crackedDistricts,
        String verdict
    ) implements Serializable {}

    /**
     * Performs a multi-metric analysis of a set of districts to detect partisan bias.
     * 
     * @param districts the list of districts in the map
     * @return a detailed analysis result
     */
    public static GerrymanderAnalysis analyze(List<District> districts) {
        if (districts == null || districts.isEmpty()) {
            return new GerrymanderAnalysis(0, 0, 0, 0, List.of(), List.of(), "Insufficient data");
        }

        int totalWastedA = 0, totalWastedB = 0, totalVotes = 0;
        List<Double> partyAShares = new ArrayList<>();
        
        for (District d : districts) {
            int threshold = (d.totalVoters() / 2) + 1;
            boolean aWins = d.partyAVotes() > d.partyBVotes();
            
            if (aWins) {
                totalWastedA += d.partyAVotes() - threshold;
                totalWastedB += d.partyBVotes();
            } else {
                totalWastedA += d.partyAVotes();
                totalWastedB += d.partyBVotes() - threshold;
            }
            
            totalVotes += d.totalVoters();
            partyAShares.add((double) d.partyAVotes() / d.totalVoters());
        }
        
        double efficiencyGap = totalVotes > 0 ? (double) Math.abs(totalWastedA - totalWastedB) / totalVotes : 0;
        
        double mean = partyAShares.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        Collections.sort(partyAShares);
        double median = partyAShares.get(partyAShares.size() / 2);
        double meanMedianDiff = mean - median;
        
        double declination = calculateDeclination(districts);
        double avgCompactness = districts.stream().mapToDouble(District::compactnessScore).average().orElse(0.5);
        
        List<String> packed = new ArrayList<>();
        List<String> cracked = new ArrayList<>();
        
        for (District d : districts) {
            double voteShare = (double) d.partyAVotes() / d.totalVoters();
            if (voteShare > 0.75) {
                packed.add(d.id() + " (Party A packed)");
            } else if (voteShare < 0.25) {
                packed.add(d.id() + " (Party B packed)");
            }
            if (voteShare > 0.45 && voteShare < 0.5) {
                cracked.add(d.id() + " (narrowly lost)");
            }
        }
        
        String verdict;
        if (efficiencyGap > 0.08 || Math.abs(meanMedianDiff) > 0.04) {
            verdict = "LIKELY GERRYMANDERED - High partisan asymmetry";
        } else if (efficiencyGap > 0.05 || avgCompactness < 0.3) {
            verdict = "POSSIBLY GERRYMANDERED - Irregular indicators";
        } else {
            verdict = "NO STRONG EVIDENCE of gerrymandering";
        }
        
        return new GerrymanderAnalysis(
            efficiencyGap, meanMedianDiff, declination, avgCompactness,
            packed, cracked, verdict
        );
    }

    private static double calculateDeclination(List<District> districts) {
        List<Double> wonShares = new ArrayList<>();
        List<Double> lostShares = new ArrayList<>();
        
        for (District d : districts) {
            double share = (double) d.partyAVotes() / d.totalVoters();
            if (share > 0.5) wonShares.add(share);
            else lostShares.add(share);
        }
        
        double avgWon = wonShares.stream().mapToDouble(Double::doubleValue).average().orElse(0.6);
        double avgLost = lostShares.stream().mapToDouble(Double::doubleValue).average().orElse(0.4);
        
        return Math.atan2(avgWon - 0.5, 1.0 / wonShares.size()) - 
               Math.atan2(0.5 - avgLost, 1.0 / lostShares.size());
    }

    /**
     * Calculates geometric compactness using the Polsby-Popper method.
     * PP = 4Ï€ Ã— Area / PerimeterÂ²
     * 
     * @param polygon list of vertices
     * @return compactness ratio between 0 and 1
     */
    public static double polsbyPopperCompactness(List<double[]> polygon) {
        if (polygon == null || polygon.size() < 3) return 0;
        double area = calculatePolygonArea(polygon);
        double perimeter = calculatePolygonPerimeter(polygon);
        return perimeter > 0 ? (4.0 * Math.PI * area) / (perimeter * perimeter) : 0;
    }

    private static double calculatePolygonArea(List<double[]> polygon) {
        double area = 0;
        for (int i = 0; i < polygon.size(); i++) {
            int j = (i + 1) % polygon.size();
            area += polygon.get(i)[0] * polygon.get(j)[1];
            area -= polygon.get(j)[0] * polygon.get(i)[1];
        }
        return Math.abs(area) / 2.0;
    }

    private static double calculatePolygonPerimeter(List<double[]> polygon) {
        double perimeter = 0;
        for (int i = 0; i < polygon.size(); i++) {
            int j = (i + 1) % polygon.size();
            double dx = polygon.get(j)[0] - polygon.get(i)[0];
            double dy = polygon.get(j)[1] - polygon.get(i)[1];
            perimeter += Math.sqrt(dx * dx + dy * dy);
        }
        return perimeter;
    }

    /**
     * Simulates a seats-votes curve to visualize partisan lean.
     * 
     * @param districts base districts
     * @param points    number of points on the curve
     * @return map of total vote share to resulting seat share
     */
    public static Map<Double, Double> seatsVotesCurve(List<District> districts, int points) {
        Map<Double, Double> curve = new LinkedHashMap<>();
        for (int i = 0; i <= points; i++) {
            double swing = (i - points / 2.0) * 0.01;
            int seatsWon = 0;
            for (District d : districts) {
                double adjustedVoteShare = (double) d.partyAVotes() / d.totalVoters() + swing;
                if (adjustedVoteShare > 0.5) seatsWon++;
            }
            double voteShare = 0.5 + swing;
            double seatShare = (double) seatsWon / districts.size();
            curve.put(voteShare, seatShare);
        }
        return curve;
    }
}

