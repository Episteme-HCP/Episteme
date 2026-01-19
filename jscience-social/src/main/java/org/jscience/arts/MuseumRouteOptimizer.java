package org.jscience.arts;

import java.util.*;

/**
 * Optimizes visitor routes through museum galleries.
 */
public final class MuseumRouteOptimizer {

    private MuseumRouteOptimizer() {}

    public record Artwork(
        String id,
        String name,
        String gallery,
        double x,
        double y,
        int visitDurationMinutes,
        double importance  // 0-1
    ) {}

    public record MuseumLayout(
        String name,
        List<Artwork> artworks,
        Map<String, List<String>> galleryConnections,
        double[] entrance,
        double[] exit
    ) {}

    public record OptimizedRoute(
        List<Artwork> order,
        int totalDurationMinutes,
        double totalDistance,
        double importanceScore
    ) {}

    /**
     * Optimizes route for available time using greedy nearest-neighbor with importance weighting.
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
                int timeNeeded = candidate.visitDurationMinutes() + (int)(dist / 50); // Walking time
                
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
     * Generates a tour for specific interests/themes.
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
     * Optimizes the order of a fixed set of works using nearest neighbor.
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
     * Estimates visit time for full museum tour.
     */
    public static int estimateFullTourDuration(MuseumLayout museum) {
        int viewing = museum.artworks().stream()
            .mapToInt(Artwork::visitDurationMinutes)
            .sum();
        
        // Estimate walking time
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
