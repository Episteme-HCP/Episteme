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

package org.jscience.arts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides algorithms for optimizing visitor routes through museum galleries.
 * It considers factors such as available time, importance of artworks, 
 * geographical layout of the museum, and visitor interests.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class MuseumRouteOptimizer {

    private MuseumRouteOptimizer() {}

    /**
     * Simplified representation of an artwork for spatial optimization.
     */
    public record Artwork(
        String id,
        String name,
        String gallery,
        double x,
        double y,
        int visitDurationMinutes,
        double importance  // 0-1
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents the logical and spatial layout of a museum.
     */
    public record MuseumLayout(
        String name,
        List<Artwork> artworks,
        Map<String, List<String>> galleryConnections,
        double[] entrance,
        double[] exit
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Recommended sequence of artworks to visit.
     */
    public record OptimizedRoute(
        List<Artwork> order,
        int totalDurationMinutes,
        double totalDistance,
        double importanceScore
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Optimizes a visitor route for a limited available time using a greedy 
     * nearest-neighbor approach weighted by artwork importance.
     * 
     * @param museum the museum layout
     * @param availableMinutes total time available for the visit
     * @param mustSee IDs of artworks that must be included in the tour
     * @param interests keyword list for thematic weighting (not used in current simplified version)
     * @return an OptimizedRoute tailored to the constraints
     */
    public static OptimizedRoute optimizeRoute(MuseumLayout museum, int availableMinutes,
            List<String> mustSee, List<String> interests) {
        
        List<Artwork> candidates = new ArrayList<>(museum.artworks());
        
        // Prioritize must-see items
        Set<String> mustSeeSet = new HashSet<>(mustSee);
        candidates.sort((a, b) -> {
            boolean aMust = mustSeeSet.contains(a.id());
            boolean bMust = mustSeeSet.contains(b.id());
            if (aMust && !bMust) return -1;
            if (bMust && !aMust) return 1;
            return Double.compare(b.importance(), a.importance());
        });
        
        List<Artwork> route = new ArrayList<>();
        int timeUsed = 0;
        double distance = 0;
        double[] currentPos = museum.entrance().clone();
        Set<String> visited = new HashSet<>();
        
        while (timeUsed < availableMinutes && !candidates.isEmpty()) {
            Artwork best = null;
            double bestScore = -1;
            
            for (Artwork candidate : candidates) {
                if (visited.contains(candidate.id())) continue;
                
                double dist = distance(currentPos, new double[]{candidate.x(), candidate.y()});
                int timeNeeded = candidate.visitDurationMinutes() + (int)(dist / 50); // Walking time approx 50m/min
                
                if (timeUsed + timeNeeded > availableMinutes) continue;
                
                // Score: importance / distance (closer + important = better)
                double score = candidate.importance() / Math.max(1, dist);
                if (mustSeeSet.contains(candidate.id())) score *= 10; // Must-see priority
                
                if (score > bestScore) {
                    bestScore = score;
                    best = candidate;
                }
            }
            
            if (best == null) break;
            
            double dist = distance(currentPos, new double[]{best.x(), best.y()});
            route.add(best);
            visited.add(best.id());
            timeUsed += best.visitDurationMinutes() + (int)(dist / 50);
            distance += dist;
            currentPos = new double[]{best.x(), best.y()};
            candidates.remove(best);
        }
        
        // Add walk to exit
        distance += distance(currentPos, museum.exit());
        
        double importanceScore = route.stream()
            .mapToDouble(Artwork::importance)
            .sum();
        
        return new OptimizedRoute(route, timeUsed, distance, importanceScore);
    }

    /**
     * Generates a tour focused on a specific theme or gallery.
     * 
     * @param museum the museum layout
     * @param theme the keyword to filter works by
     * @param maxWorks maximum number of works to include
     * @return an OptimizedRoute for the theme
     */
    public static OptimizedRoute thematicTour(MuseumLayout museum, String theme, 
            int maxWorks) {
        
        // Filter works by theme (simplified: check gallery name)
        List<Artwork> thematic = museum.artworks().stream()
            .filter(a -> a.gallery().toLowerCase().contains(theme.toLowerCase()))
            .sorted((a, b) -> Double.compare(b.importance(), a.importance()))
            .limit(maxWorks)
            .toList();
        
        return optimizeRouteOrder(thematic, museum.entrance(), museum.exit());
    }

    /**
     * Optimizes the order of a fixed set of works using nearest neighbor algorithm.
     */
    private static OptimizedRoute optimizeRouteOrder(List<Artwork> works,
            double[] start, double[] end) {
        
        if (works.isEmpty()) {
            return new OptimizedRoute(Collections.emptyList(), 0, 0, 0);
        }
        
        List<Artwork> remaining = new ArrayList<>(works);
        List<Artwork> route = new ArrayList<>();
        double[] current = start.clone();
        double totalDist = 0;
        int totalTime = 0;
        
        while (!remaining.isEmpty()) {
            Artwork nearest = null;
            double minDist = Double.MAX_VALUE;
            for (Artwork a : remaining) {
                double d = distance(current, new double[]{a.x(), a.y()});
                if (d < minDist) {
                    minDist = d;
                    nearest = a;
                }
            }
            if (nearest != null) {
                route.add(nearest);
                remaining.remove(nearest);
                totalDist += minDist;
                totalTime += nearest.visitDurationMinutes() + (int)(minDist / 50);
                current = new double[]{nearest.x(), nearest.y()};
            }
        }
        
        totalDist += distance(current, end);
        double importance = route.stream().mapToDouble(Artwork::importance).sum();
        return new OptimizedRoute(route, totalTime, totalDist, importance);
    }

    /**
     * Estimates the total visit time for a full tour of the museum.
     * 
     * @param museum the museum layout
     * @return estimated duration in minutes
     */
    public static int estimateFullTourDuration(MuseumLayout museum) {
        int viewing = museum.artworks().stream()
            .mapToInt(Artwork::visitDurationMinutes)
            .sum();
        
        double totalArea = museum.artworks().stream()
            .mapToDouble(a -> Math.sqrt(a.x() * a.x() + a.y() * a.y()))
            .max().orElse(100);
        int walking = (int)(totalArea / 50 * museum.artworks().size() * 0.5);
        
        return viewing + walking;
    }

    private static double distance(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }
}
