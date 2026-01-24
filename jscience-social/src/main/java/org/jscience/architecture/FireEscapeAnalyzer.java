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

package org.jscience.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical engine for fire safety and evacuation planning in architectural designs.
 * It simulates evacuation scenarios using hydraulic flow models to estimate 
 * evacuation times, identify bottlenecks, and verify compliance with safety codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class FireEscapeAnalyzer {

    private FireEscapeAnalyzer() {}

    /**
     * Represents a distinct area or room in a building with specific occupancy.
     */
    public record Compartment(
        String id,
        double area,           // m²
        int occupancy,
        double travelDistance, // meters to nearest exit
        List<String> connectedExits
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents a point of egress (door, stairway, corridor exit).
     */
    public record Exit(
        String id,
        double width,          // meters
        double capacity,       // persons per minute (derived)
        boolean isProtected,   // e.g., fire-rated stairwell
        String type            // "door", "stairwell", "corridor"
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Complete simulation result for a specific building configuration.
     */
    public record EvacuationResult(
        double totalEvacuationTime,    // minutes
        Map<String, Double> exitTimes,
        Map<String, Integer> exitLoads,
        List<String> bottlenecks,
        boolean meetsCode
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    private static final double WALKING_SPEED = 1.2; // m/s on level ground
    private static final double WALKING_SPEED_STAIRS = 0.5; // m/s on stairs
    private static final double FLOW_RATE = 1.3; // persons per second per meter width

    /**
     * Calculates the total evacuation time for a set of compartments and exits.
     * 
     * @param compartments list of rooms/areas
     * @param exits list of available exits
     * @return an EvacuationResult containing time estimates and bottleneck identification
     */
    public static EvacuationResult analyzeEvacuation(List<Compartment> compartments,
            List<Exit> exits) {
        
        // Distribute occupants to exits (assuming efficient distribution)
        Map<String, Integer> exitLoads = new HashMap<>();
        for (Compartment c : compartments) {
            if (c.connectedExits().isEmpty()) continue;
            int occupantsPerExit = c.occupancy() / c.connectedExits().size();
            for (String exitId : c.connectedExits()) {
                exitLoads.merge(exitId, occupantsPerExit, Integer::sum);
            }
        }
        
        Map<String, Double> exitTimes = new HashMap<>();
        List<String> bottlenecks = new ArrayList<>();
        
        for (Exit exit : exits) {
            int load = exitLoads.getOrDefault(exit.id(), 0);
            
            // Flow time through exit (hydraulic model)
            double flowTime = exit.width() > 0 ? load / (exit.width() * FLOW_RATE) : Double.POSITIVE_INFINITY;
            
            // Max travel time to this specific exit
            double maxTravel = compartments.stream()
                .filter(c -> c.connectedExits().contains(exit.id()))
                .mapToDouble(Compartment::travelDistance)
                .max()
                .orElse(0);
            
            double speed = "stairwell".equalsIgnoreCase(exit.type()) ? WALKING_SPEED_STAIRS : WALKING_SPEED;
            double travelTime = maxTravel / speed;
            
            double totalTime = travelTime + flowTime;
            exitTimes.put(exit.id(), totalTime);
            
            // Check for bottleneck (high density/slow flow)
            if (flowTime > travelTime * 2 && load > 10) {
                bottlenecks.add(exit.id() + ": flow capacity exceeded");
            }
        }
        
        double totalEvacTime = exitTimes.values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0);
        
        // Code check (standard safety limit approx 2.5 minutes)
        boolean meetsCode = totalEvacTime <= (2.5 * 60);
        
        return new EvacuationResult(totalEvacTime / 60, exitTimes, exitLoads, 
            bottlenecks, meetsCode);
    }

    /**
     * Calculates the required combined exit width based on occupant load 
     * and a target evacuation time.
     * 
     * @param occupancy total count of people to evacuate
     * @param targetEvacTime target time in minutes
     * @return required total width in meters
     */
    public static Real requiredExitWidth(int occupancy, double targetEvacTime) {
        if (targetEvacTime <= 0) return Real.ZERO;
        double flowNeeded = occupancy / (targetEvacTime * 60); // persons/sec
        double widthNeeded = flowNeeded / FLOW_RATE;
        return Real.of(widthNeeded);
    }

    /**
     * Verifies that travel distances from any point to the nearest exit 
     * stay within code-mandated limits.
     * 
     * @param compartments list of areas to check
     * @param maxDistance global maximum travel distance
     * @param maxDeadEnd maximum distance for areas served by only one exit
     * @return map of compartment IDs to compliance status
     */
    public static Map<String, Boolean> checkTravelDistances(List<Compartment> compartments,
            double maxDistance, double maxDeadEnd) {
        
        Map<String, Boolean> compliance = new HashMap<>();
        for (Compartment c : compartments) {
            boolean compliant = c.travelDistance() <= maxDistance;
            
            // Dead-end check (limited serving exit)
            if (c.connectedExits().size() == 1 && c.travelDistance() > maxDeadEnd) {
                compliant = false;
            }
            compliance.put(c.id(), compliant);
        }
        return compliance;
    }

    /**
     * Determines the minimum number of exits required by standard building codes 
     * based on occupant load.
     * 
     * @param occupancy total number of people
     * @return required exit count (1 to 4+)
     */
    public static int minimumExitsRequired(int occupancy) {
        if (occupancy < 50) return 1;
        if (occupancy < 500) return 2;
        if (occupancy < 1000) return 3;
        return 4;
    }

    /**
     * Calculates the required width for a stairwell serving multiple floors.
     * 
     * @param occupantLoad load per floor
     * @param numFloors number of floors served
     * @return minimum required width in meters
     */
    public static Real requiredStairWidth(int occupantLoad, int numFloors) {
        double baseWidth = 1.1; // Standard minimum m
        double widthPerOccupant = 0.005; // 5mm per person rule
        double required = baseWidth + (occupantLoad * numFloors * widthPerOccupant);
        return Real.of(Math.max(baseWidth, Math.min(3.0, required)));
    }

    /**
     * Identifies necessary safety signage and lighting for evacuation routes.
     * 
     * @param compartments areas served
     * @param exits exit points
     * @return list of localized signage requirements
     */
    public static List<String> signageRequirements(List<Compartment> compartments,
            List<Exit> exits) {
        
        List<String> requirements = new ArrayList<>();
        for (Compartment c : compartments) {
            if (c.travelDistance() > 15) {
                requirements.add("Compartment " + c.id() + ": Directional signs required (travel > 15m)");
            }
        }
        for (Exit exit : exits) {
            requirements.add("Exit " + exit.id() + ": Illuminated EXIT sign required");
            if (exit.isProtected()) {
                requirements.add("Exit " + exit.id() + ": 'Fire door - Keep Closed' sign required");
            }
        }
        return requirements;
    }
}
