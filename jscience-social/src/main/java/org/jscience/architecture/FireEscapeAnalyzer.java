package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Analyzes fire escape routes and evacuation times.
 */
public final class FireEscapeAnalyzer {

    private FireEscapeAnalyzer() {}

    public record Compartment(
        String id,
        double area,           // m²
        int occupancy,
        double travelDistance, // m to nearest exit
        List<String> connectedExits
    ) {}

    public record Exit(
        String id,
        double width,          // m (door width)
        double capacity,       // persons per minute
        boolean isProtected,   // Protected stairwell
        String type            // door, stairwell, corridor
    ) {}

    public record EvacuationResult(
        double totalEvacuationTime,    // minutes
        Map<String, Double> exitTimes,
        Map<String, Integer> exitLoads,
        List<String> bottlenecks,
        boolean meetsCode
    ) {}

    private static final double WALKING_SPEED = 1.2; // m/s normal
    private static final double WALKING_SPEED_STAIRS = 0.5; // m/s on stairs
    private static final double FLOW_RATE = 1.3; // persons per second per meter width

    /**
     * Calculates evacuation time using hydraulic model.
     */
    public static EvacuationResult analyzeEvacuation(List<Compartment> compartments,
            List<Exit> exits) {
        
        
        // Distribute occupants to exits
        Map<String, Integer> exitLoads = new HashMap<>();
        for (Compartment c : compartments) {
            int occupantsPerExit = c.occupancy() / c.connectedExits().size();
            for (String exitId : c.connectedExits()) {
                exitLoads.merge(exitId, occupantsPerExit, (a, b) -> a + b);
            }
        }
        
        // Calculate time for each exit
        Map<String, Double> exitTimes = new HashMap<>();
        List<String> bottlenecks = new ArrayList<>();
        
        for (Exit exit : exits) {
            int load = exitLoads.getOrDefault(exit.id(), 0);
            
            // Flow time through exit
            double flowTime = load / (exit.width() * FLOW_RATE);
            
            // Max travel time to this exit
            double maxTravel = compartments.stream()
                .filter(c -> c.connectedExits().contains(exit.id()))
                .mapToDouble(Compartment::travelDistance)
                .max()
                .orElse(0);
            
            double speed = exit.type().equals("stairwell") ? WALKING_SPEED_STAIRS : WALKING_SPEED;
            double travelTime = maxTravel / speed;
            
            double totalTime = travelTime + flowTime;
            exitTimes.put(exit.id(), totalTime);
            
            // Check for bottleneck
            if (flowTime > travelTime * 2) {
                bottlenecks.add(exit.id() + ": flow capacity exceeded");
            }
        }
        
        double totalEvacTime = exitTimes.values().stream()
            .mapToDouble(Double::doubleValue)
            .max()
            .orElse(0);
        
        // Code check (simplified: 2.5 min for most buildings)
        boolean meetsCode = totalEvacTime / 60 < 2.5;
        
        return new EvacuationResult(totalEvacTime / 60, exitTimes, exitLoads, 
            bottlenecks, meetsCode);
    }

    /**
     * Calculates required exit width.
     */
    public static Real requiredExitWidth(int occupancy, double targetEvacTime) {
        // Flow-based calculation
        double flowNeeded = occupancy / (targetEvacTime * 60); // persons/sec
        double widthNeeded = flowNeeded / FLOW_RATE;
        return Real.of(widthNeeded);
    }

    /**
     * Checks travel distance compliance.
     */
    public static Map<String, Boolean> checkTravelDistances(List<Compartment> compartments,
            double maxDistance, double maxDeadEnd) {
        
        Map<String, Boolean> compliance = new HashMap<>();
        
        for (Compartment c : compartments) {
            boolean compliant = c.travelDistance() <= maxDistance;
            
            // Dead-end check (single exit)
            if (c.connectedExits().size() == 1 && c.travelDistance() > maxDeadEnd) {
                compliant = false;
            }
            
            compliance.put(c.id(), compliant);
        }
        
        return compliance;
    }

    /**
     * Calculates minimum number of exits required.
     */
    public static int minimumExitsRequired(int occupancy) {
        // Typical code: 1 exit up to 49, 2 exits 50-500, 3 exits 500-1000, 4 exits 1000+
        if (occupancy < 50) return 1;
        if (occupancy < 500) return 2;
        if (occupancy < 1000) return 3;
        return 4;
    }

    /**
     * Calculates required stairwell width.
     */
    public static Real requiredStairWidth(int occupantLoad, int numFloors) {
        // Based on occupant load and number of floors served
        double baseWidth = 1.1; // minimum 1.1m
        double widthPerOccupant = 0.005; // 5mm per person
        
        double required = baseWidth + (occupantLoad * numFloors * widthPerOccupant);
        return Real.of(Math.max(baseWidth, Math.min(3.0, required)));
    }

    /**
     * Generates evacuation signage requirements.
     */
    public static List<String> signageRequirements(List<Compartment> compartments,
            List<Exit> exits) {
        
        List<String> requirements = new ArrayList<>();
        
        // Every compartment needs exit sign visibility
        for (Compartment c : compartments) {
            if (c.travelDistance() > 15) {
                requirements.add("Compartment " + c.id() + ": Illuminated exit signs required at " +
                    (int)(c.travelDistance() / 15) + " intermediate points");
            }
        }
        
        // Exit doors
        for (Exit exit : exits) {
            requirements.add("Exit " + exit.id() + ": EXIT sign with running man pictogram");
            if (exit.isProtected()) {
                requirements.add("Exit " + exit.id() + ": 'Protected stairway - keep closed' sign");
            }
        }
        
        return requirements;
    }
}
