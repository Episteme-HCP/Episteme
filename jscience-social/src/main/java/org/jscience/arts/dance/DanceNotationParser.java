package org.jscience.arts.dance;

import java.util.*;

/**
 * Advanced parser for dance notation based on Laban Movement Analysis (LMA) concepts.
 * Supports direction, level, limb, duration, and effort.
 */
public final class DanceNotationParser {

    private DanceNotationParser() {}

    public enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT, 
        DIAGONAL_FWD_LEFT, DIAGONAL_FWD_RIGHT, 
        DIAGONAL_BWD_LEFT, DIAGONAL_BWD_RIGHT, PLACE
    }

    public enum Level { HIGH, MIDDLE, LOW }

    public record Movement(
        String limb,
        Direction direction,
        Level level,
        double durationTicks,
        double effortWeight, // 0-1
        double effortTime // 0-1
    ) {}

    /**
     * Parses Laban-lite notation.
     * Format: LIMB:DIR:LEVEL:DUR[:WEIGHT:TIME]
     * Example: "RIGHT_ARM:FORWARD:HIGH:4, LEFT_LEG:PLACE:LOW:2:0.8:0.2"
     */
    public static List<Movement> parse(String notation) {
        if (notation == null || notation.isBlank()) return Collections.emptyList();
        
        List<Movement> movements = new ArrayList<>();
        String[] entries = notation.split(",");
        
        for (String entry : entries) {
            String[] tokens = entry.trim().split(":");
            if (tokens.length < 4) continue;
            
            try {
                String limb = tokens[0].toUpperCase();
                Direction dir = Direction.valueOf(tokens[1].toUpperCase());
                Level level = Level.valueOf(tokens[2].toUpperCase());
                double dur = Double.parseDouble(tokens[3]);
                
                double weight = (tokens.length > 4) ? Double.parseDouble(tokens[4]) : 0.5;
                double time = (tokens.length > 5) ? Double.parseDouble(tokens[5]) : 0.5;
                
                movements.add(new Movement(limb, dir, level, dur, weight, time));
            } catch (IllegalArgumentException e) {
                // Skip invalid tokens
                System.err.println("Invalid dance notation token: " + entry);
            }
        }
        
        return movements;
    }

    /**
     * Calculates the total duration of a sequence.
     */
    public static double totalDuration(List<Movement> sequence) {
        return sequence.stream().mapToDouble(Movement::durationTicks).sum();
    }
}
