package org.jscience.politics;

import java.util.*;

/**
 * Detects gerrymandering in electoral district maps.
 */
public final class GerrymanderingDetector {

    private GerrymanderingDetector() {}

    public record District(
        String id,
        int totalVoters,
        int partyAVotes,
        int partyBVotes,
        double compactnessScore,  // 0-1 (1 = perfectly compact)
        List<double[]> boundary   // Polygon vertices
    ) {}

    public record GerrymanderAnalysis(
        double efficiencyGap,      // |wasted votes difference| / total votes
        double meanMedianDiff,     // Difference between mean and median vote share
        double declination,        // Asymmetry measure
        double overallCompactness,
        List<String> packedDistricts,
        List<String> crackedDistricts,
        String verdict
    ) {}

    /**
     * Analyzes district map for gerrymandering.
     */
    public static GerrymanderAnalysis analyze(List<District> districts) {
        // Calculate efficiency gap
        int totalWastedA = 0, totalWastedB = 0, totalVotes = 0;
        List<Double> partyAShares = new ArrayList<>();
        
        for (District d : districts) {
            int threshold = d.totalVoters() / 2 + 1;
            boolean aWins = d.partyAVotes() > d.partyBVotes();
            
            if (aWins) {
                totalWastedA += d.partyAVotes() - threshold;  // Excess votes
                totalWastedB += d.partyBVotes();              // All losing votes
            } else {
                totalWastedA += d.partyAVotes();
                totalWastedB += d.partyBVotes() - threshold;
            }
            
            totalVotes += d.totalVoters();
            partyAShares.add((double) d.partyAVotes() / d.totalVoters());
        }
        
        double efficiencyGap = Math.abs(totalWastedA - totalWastedB) / (double) totalVotes;
        
        // Mean-median difference
        double mean = partyAShares.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        Collections.sort(partyAShares);
        double median = partyAShares.get(partyAShares.size() / 2);
        double meanMedianDiff = mean - median;
        
        // Declination (simplified)
        double declination = calculateDeclination(districts);
        
        // Compactness
        double avgCompactness = districts.stream()
            .mapToDouble(District::compactnessScore)
            .average()
            .orElse(0.5);
        
        // Identify packed and cracked districts
        List<String> packed = new ArrayList<>();
        List<String> cracked = new ArrayList<>();
        
        for (District d : districts) {
            double voteShare = (double) d.partyAVotes() / d.totalVoters();
            if (voteShare > 0.75) {
                packed.add(d.id() + " (Party A packed: " + String.format("%.1f%%", voteShare * 100) + ")");
            } else if (voteShare < 0.25) {
                packed.add(d.id() + " (Party B packed: " + String.format("%.1f%%", (1-voteShare) * 100) + ")");
            }
            if (voteShare > 0.45 && voteShare < 0.5) {
                cracked.add(d.id() + " (narrowly lost: " + String.format("%.1f%%", voteShare * 100) + ")");
            }
        }
        
        // Verdict
        String verdict;
        if (efficiencyGap > 0.08 || Math.abs(meanMedianDiff) > 0.04) {
            verdict = "LIKELY GERRYMANDERED - Significant asymmetry detected";
        } else if (efficiencyGap > 0.05 || avgCompactness < 0.3) {
            verdict = "POSSIBLY GERRYMANDERED - Some indicators elevated";
        } else {
            verdict = "NO STRONG EVIDENCE of gerrymandering";
        }
        
        return new GerrymanderAnalysis(
            efficiencyGap, meanMedianDiff, declination, avgCompactness,
            packed, cracked, verdict
        );
    }

    /**
     * Calculates compactness using Polsby-Popper method.
     * PP = 4π × Area / Perimeter²
     */
    public static double polsbyPopperCompactness(List<double[]> polygon) {
        if (polygon.size() < 3) return 0;
        
        double area = calculatePolygonArea(polygon);
        double perimeter = calculatePolygonPerimeter(polygon);
        
        if (perimeter <= 0) return 0;
        return 4 * Math.PI * area / (perimeter * perimeter);
    }

    /**
     * Calculates compactness using convex hull ratio.
     */
    public static double convexHullCompactness(List<double[]> polygon) {
        double originalArea = calculatePolygonArea(polygon);
        List<double[]> hull = convexHull(polygon);
        double hullArea = calculatePolygonArea(hull);
        
        return hullArea > 0 ? originalArea / hullArea : 0;
    }

    /**
     * Simulates seats-votes curve.
     */
    public static Map<Double, Double> seatsVotesCurve(List<District> districts, int points) {
        Map<Double, Double> curve = new LinkedHashMap<>();
        
        for (int i = 0; i <= points; i++) {
            double swing = (i - points / 2) * 0.01; // -0.5 to +0.5
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

    private static double calculateDeclination(List<District> districts) {
        // Simplified declination calculation
        List<Double> wonShares = new ArrayList<>();
        List<Double> lostShares = new ArrayList<>();
        
        for (District d : districts) {
            double share = (double) d.partyAVotes() / d.totalVoters();
            if (share > 0.5) {
                wonShares.add(share);
            } else {
                lostShares.add(share);
            }
        }
        
        double avgWon = wonShares.stream().mapToDouble(Double::doubleValue).average().orElse(0.6);
        double avgLost = lostShares.stream().mapToDouble(Double::doubleValue).average().orElse(0.4);
        
        return Math.atan2(avgWon - 0.5, 1.0 / wonShares.size()) - 
               Math.atan2(0.5 - avgLost, 1.0 / lostShares.size());
    }

    private static double calculatePolygonArea(List<double[]> polygon) {
        if (polygon.size() < 3) return 0;
        
        double area = 0;
        for (int i = 0; i < polygon.size(); i++) {
            int j = (i + 1) % polygon.size();
            area += polygon.get(i)[0] * polygon.get(j)[1];
            area -= polygon.get(j)[0] * polygon.get(i)[1];
        }
        return Math.abs(area) / 2;
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

    private static List<double[]> convexHull(List<double[]> points) {
        // Graham scan - simplified
        if (points.size() < 3) return new ArrayList<>(points);
        
        // Just return points sorted by angle from centroid as approximation
        double cx = points.stream().mapToDouble(p -> p[0]).average().orElse(0);
        double cy = points.stream().mapToDouble(p -> p[1]).average().orElse(0);
        
        List<double[]> sorted = new ArrayList<>(points);
        sorted.sort((a, b) -> Double.compare(
            Math.atan2(a[1] - cy, a[0] - cx),
            Math.atan2(b[1] - cy, b[0] - cx)
        ));
        
        return sorted;
    }
}
