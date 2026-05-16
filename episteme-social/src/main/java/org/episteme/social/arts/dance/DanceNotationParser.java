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

package org.episteme.social.arts.dance;

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

