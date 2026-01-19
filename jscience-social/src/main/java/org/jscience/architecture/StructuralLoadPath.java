package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Analyzes structural load paths through a building.
 */
public final class StructuralLoadPath {

    private StructuralLoadPath() {}

    public enum MemberType {
        BEAM, COLUMN, SLAB, WALL, FOUNDATION, TRUSS, BRACE
    }

    public record StructuralMember(
        String id,
        MemberType type,
        double startX, double startY, double startZ,
        double endX, double endY, double endZ,
        double crossSectionArea,   // m²
        double momentOfInertia,    // m⁴
        String material
    ) {}

    public record AppliedLoad(
        String id,
        double x, double y, double z,
        double forceX, double forceY, double forceZ,  // kN
        double momentX, double momentY, double momentZ  // kN·m
    ) {}

    public record MemberForce(
        StructuralMember member,
        double axialForce,      // kN (+ tension, - compression)
        double shearForceY,     // kN
        double shearForceZ,     // kN
        double bendingMomentY,  // kN·m
        double bendingMomentZ,  // kN·m
        double torsion          // kN·m
    ) {}

    /**
     * Traces the load path from application point to foundation.
     */
    public static List<StructuralMember> traceLoadPath(List<StructuralMember> members,
            AppliedLoad load) {
        
        List<StructuralMember> path = new ArrayList<>();
        
        // Find member receiving the load
        StructuralMember receiving = findMemberAt(members, load.x(), load.y(), load.z());
        if (receiving == null) return path;
        path.add(receiving);
        
        // Trace down to foundation
        double currentZ = Math.min(receiving.startZ(), receiving.endZ());
        Set<String> visited = new HashSet<>();
        visited.add(receiving.id());
        
        while (currentZ > 0) {
            // Find supporting member
            double z = currentZ;
            StructuralMember support = members.stream()
                .filter(m -> !visited.contains(m.id()))
                .filter(m -> Math.max(m.startZ(), m.endZ()) >= z - 0.1)
                .filter(m -> isConnected(path.get(path.size() - 1), m))
                .findFirst()
                .orElse(null);
            
            if (support == null) break;
            
            path.add(support);
            visited.add(support.id());
            currentZ = Math.min(support.startZ(), support.endZ());
        }
        
        return path;
    }

    /**
     * Calculates reactions at supports using equilibrium.
     */
    public static Map<String, double[]> calculateReactions(List<StructuralMember> members,
            List<AppliedLoad> loads, List<String> supportIds) {
        
        Map<String, double[]> reactions = new HashMap<>();
        
        // Sum of applied loads
        double totalFx = 0, totalFy = 0, totalFz = 0;
        for (AppliedLoad load : loads) {
            totalFx += load.forceX();
            totalFy += load.forceY();
            totalFz += load.forceZ();
        }
        
        // Distribute to supports (simplified: equal distribution)
        int numSupports = supportIds.size();
        for (String supportId : supportIds) {
            reactions.put(supportId, new double[] {
                -totalFx / numSupports,
                -totalFy / numSupports,
                -totalFz / numSupports
            });
        }
        
        return reactions;
    }

    /**
     * Checks member for adequate capacity.
     */
    public static boolean checkMemberCapacity(MemberForce forces, double yieldStrength,
            double safetyFactor) {
        
        double area = forces.member().crossSectionArea();
        double I = forces.member().momentOfInertia();
        
        // Axial stress
        double axialStress = Math.abs(forces.axialForce()) / area * 1000; // kN to N, then MPa
        
        // Bending stress (simplified, assuming symmetric section)
        double c = Math.sqrt(area / Math.PI); // Approximate distance to extreme fiber
        double bendingStress = Math.abs(forces.bendingMomentY()) * c / I * 1000;
        
        // Combined stress
        double combinedStress = axialStress + bendingStress;
        
        return combinedStress < yieldStrength / safetyFactor;
    }

    /**
     * Generates a free body diagram description.
     */
    public static String generateFBD(StructuralMember member, List<MemberForce> allForces) {
        MemberForce force = allForces.stream()
            .filter(f -> f.member().id().equals(member.id()))
            .findFirst()
            .orElse(null);
        
        if (force == null) return "No forces on member " + member.id();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Free Body Diagram: ").append(member.id()).append("\n");
        sb.append("Type: ").append(member.type()).append("\n");
        sb.append(String.format("Axial Force: %.2f kN (%s)\n", 
            force.axialForce(), force.axialForce() > 0 ? "tension" : "compression"));
        sb.append(String.format("Shear Force Y: %.2f kN\n", force.shearForceY()));
        sb.append(String.format("Bending Moment Y: %.2f kN·m\n", force.bendingMomentY()));
        
        return sb.toString();
    }

    /**
     * Calculates load distribution from slab to beams.
     */
    public static Map<String, Real> slabLoadDistribution(double slabLoad, 
            double slabLength, double slabWidth, List<StructuralMember> beams) {
        
        Map<String, Real> distribution = new HashMap<>();
        double totalLoad = slabLoad * slabLength * slabWidth;
        
        // Tributary area method (simplified)
        int numBeams = beams.size();
        double loadPerBeam = totalLoad / numBeams;
        
        for (StructuralMember beam : beams) {
            distribution.put(beam.id(), Real.of(loadPerBeam));
        }
        
        return distribution;
    }

    private static StructuralMember findMemberAt(List<StructuralMember> members,
            double x, double y, double z) {
        
        return members.stream()
            .filter(m -> isPointNearMember(m, x, y, z))
            .findFirst()
            .orElse(null);
    }

    private static boolean isPointNearMember(StructuralMember m, double x, double y, double z) {
        double tolerance = 0.5; // meters
        
        // Check if point is near the line segment of the member
        double minX = Math.min(m.startX(), m.endX()) - tolerance;
        double maxX = Math.max(m.startX(), m.endX()) + tolerance;
        double minY = Math.min(m.startY(), m.endY()) - tolerance;
        double maxY = Math.max(m.startY(), m.endY()) + tolerance;
        double minZ = Math.min(m.startZ(), m.endZ()) - tolerance;
        double maxZ = Math.max(m.startZ(), m.endZ()) + tolerance;
        
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    private static boolean isConnected(StructuralMember a, StructuralMember b) {
        double tolerance = 0.1;
        
        // Check if any endpoints are close
        return (distance(a.startX(), a.startY(), a.startZ(), b.startX(), b.startY(), b.startZ()) < tolerance) ||
               (distance(a.startX(), a.startY(), a.startZ(), b.endX(), b.endY(), b.endZ()) < tolerance) ||
               (distance(a.endX(), a.endY(), a.endZ(), b.startX(), b.startY(), b.startZ()) < tolerance) ||
               (distance(a.endX(), a.endY(), a.endZ(), b.endX(), b.endY(), b.endZ()) < tolerance);
    }

    private static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) + Math.pow(z2-z1, 2));
    }
}
