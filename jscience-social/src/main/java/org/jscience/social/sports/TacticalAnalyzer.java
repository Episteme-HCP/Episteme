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

package org.jscience.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jscience.core.mathematics.numbers.real.Real;

/**
 * Provides tactical analysis of team formations, player heatmaps, and passing networks.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class TacticalAnalyzer {

    private TacticalAnalyzer() {}

    /** Geometric coordinate on a sports field. */
    public record Position(double x, double y) implements Serializable {}

    /** Temporal trajectory of a player. */
    public record PlayerMovement(String playerId, List<Position> trajectory, double timestamp) implements Serializable {}

    /** Structured arrangement of player base positions. */
    public record Formation(String name, Map<String, Position> basePositions) implements Serializable {}

    /** Record of a ball transfer between players. */
    public record Pass(String fromPlayer, String toPlayer, Position start, Position end, 
            double timestamp, boolean successful) implements Serializable {}

    /**
     * Generates a 2D intensity heatmap based on player occupancy.
     */
    public static double[][] generateHeatmap(List<Position> positions, int gridWidth, int gridHeight,
            double fieldWidth, double fieldHeight) {
        
        double[][] heatmap = new double[gridHeight][gridWidth];
        if (positions == null || positions.isEmpty()) return heatmap;
        
        double cellWidth = fieldWidth / gridWidth;
        double cellHeight = fieldHeight / gridHeight;
        
        for (Position pos : positions) {
            int gridX = Math.min(gridWidth - 1, (int)(pos.x() / cellWidth));
            int gridY = Math.min(gridHeight - 1, (int)(pos.y() / cellHeight));
            heatmap[gridY][gridX] += 1.0;
        }
        
        double max = Arrays.stream(heatmap).flatMapToDouble(Arrays::stream).max().orElse(1);
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                heatmap[i][j] /= max;
            }
        }
        return heatmap;
    }

    /**
     * Constructs a passing network graph showing successful links between players.
     */
    public static Map<String, Map<String, Integer>> buildPassingNetwork(List<Pass> passes) {
        Map<String, Map<String, Integer>> network = new HashMap<>();
        if (passes == null) return network;
        
        for (Pass pass : passes) {
            if (pass.successful()) {
                network.computeIfAbsent(pass.fromPlayer(), k -> new HashMap<>())
                    .merge(pass.toPlayer(), 1, (a, b) -> a + b);
            }
        }
        return network;
    }

    /** Calculates the average distance between all players (compactness). */
    public static Real calculateCompactness(Map<String, Position> playerPositions) {
        if (playerPositions == null || playerPositions.isEmpty()) return Real.ZERO;
        List<Position> positions = new ArrayList<>(playerPositions.values());
        double totalDistance = 0;
        int pairs = 0;
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                double dx = positions.get(i).x() - positions.get(j).x();
                double dy = positions.get(i).y() - positions.get(j).y();
                totalDistance += Math.sqrt(dx * dx + dy * dy);
                pairs++;
            }
        }
        return Real.of(pairs > 0 ? totalDistance / pairs : 0);
    }
}

