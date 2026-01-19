package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Tactical analysis of team formations and player movements.
 */
public final class TacticalAnalyzer {

    private TacticalAnalyzer() {}

    public record Position(double x, double y) {}

    public record PlayerMovement(String playerId, List<Position> trajectory, double timestamp) {}

    public record Formation(String name, Map<String, Position> basePositions) {}

    public record Pass(String fromPlayer, String toPlayer, Position start, Position end, 
            double timestamp, boolean successful) {}

    // Common football/soccer formations
    public static final Formation F_4_4_2 = new Formation("4-4-2", Map.ofEntries(
        Map.entry("GK", new Position(5, 50)),
        Map.entry("LB", new Position(20, 15)), Map.entry("CB1", new Position(20, 35)), 
        Map.entry("CB2", new Position(20, 65)), Map.entry("RB", new Position(20, 85)),
        Map.entry("LM", new Position(50, 15)), Map.entry("CM1", new Position(50, 35)),
        Map.entry("CM2", new Position(50, 65)), Map.entry("RM", new Position(50, 85)),
        Map.entry("ST1", new Position(80, 35)), Map.entry("ST2", new Position(80, 65))
    ));

    public static final Formation F_4_3_3 = new Formation("4-3-3", Map.ofEntries(
        Map.entry("GK", new Position(5, 50)),
        Map.entry("LB", new Position(20, 15)), Map.entry("CB1", new Position(20, 35)),
        Map.entry("CB2", new Position(20, 65)), Map.entry("RB", new Position(20, 85)),
        Map.entry("CM1", new Position(45, 25)), Map.entry("CM2", new Position(45, 50)), Map.entry("CM3", new Position(45, 75)),
        Map.entry("LW", new Position(75, 15)), Map.entry("ST", new Position(80, 50)), Map.entry("RW", new Position(75, 85))
    ));

    /**
     * Generates a heatmap of player positions.
     */
    public static double[][] generateHeatmap(List<Position> positions, int gridWidth, int gridHeight,
            double fieldWidth, double fieldHeight) {
        
        double[][] heatmap = new double[gridHeight][gridWidth];
        double cellWidth = fieldWidth / gridWidth;
        double cellHeight = fieldHeight / gridHeight;
        
        for (Position pos : positions) {
            int gridX = Math.min(gridWidth - 1, (int)(pos.x() / cellWidth));
            int gridY = Math.min(gridHeight - 1, (int)(pos.y() / cellHeight));
            heatmap[gridY][gridX] += 1.0;
        }
        
        // Normalize
        double max = Arrays.stream(heatmap).flatMapToDouble(Arrays::stream).max().orElse(1);
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                heatmap[i][j] /= max;
            }
        }
        
        return heatmap;
    }

    /**
     * Calculates passing network metrics.
     */
    public static Map<String, Map<String, Integer>> buildPassingNetwork(List<Pass> passes) {
        Map<String, Map<String, Integer>> network = new HashMap<>();
        
        for (Pass pass : passes) {
            if (pass.successful()) {
                network.computeIfAbsent(pass.fromPlayer(), k -> new HashMap<>())
                    .merge(pass.toPlayer(), 1, (a, b) -> a + b);
            }
        }
        
        return network;
    }

    /**
     * Identifies the most central player in passing network.
     */
    public static String findPlaymaker(Map<String, Map<String, Integer>> passingNetwork) {
        Map<String, Integer> centrality = new HashMap<>();
        
        // Simple degree centrality (passes given + received)
        for (var entry : passingNetwork.entrySet()) {
            String player = entry.getKey();
            int outgoing = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            centrality.merge(player, outgoing, (a, b) -> a + b);
            
            for (var target : entry.getValue().entrySet()) {
                centrality.merge(target.getKey(), target.getValue(), (a, b) -> a + b);
            }
        }
        
        return centrality.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
    }

    /**
     * Calculates team compactness (average distance between players).
     */
    public static Real calculateCompactness(Map<String, Position> playerPositions) {
        List<Position> positions = new ArrayList<>(playerPositions.values());
        
        double totalDistance = 0;
        int pairs = 0;
        
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                totalDistance += distance(positions.get(i), positions.get(j));
                pairs++;
            }
        }
        
        return Real.of(pairs > 0 ? totalDistance / pairs : 0);
    }

    /**
     * Calculates pressing intensity (average position advancement).
     */
    public static Real calculatePressingLine(Map<String, Position> playerPositions, 
            boolean excludeGoalkeeper) {
        
        double sum = 0;
        int count = 0;
        
        for (var entry : playerPositions.entrySet()) {
            if (excludeGoalkeeper && entry.getKey().contains("GK")) continue;
            sum += entry.getValue().x();
            count++;
        }
        
        return Real.of(count > 0 ? sum / count : 0);
    }

    /**
     * Detects formation from player positions.
     */
    public static String detectFormation(Map<String, Position> positions) {
        // Simplified: count players in zones
        int defenders = 0, midfielders = 0, attackers = 0;
        
        for (Position pos : positions.values()) {
            if (pos.x() < 30) defenders++;
            else if (pos.x() < 60) midfielders++;
            else attackers++;
        }
        
        return String.format("%d-%d-%d", defenders - 1, midfielders, attackers); // -1 for GK
    }

    private static double distance(Position a, Position b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
