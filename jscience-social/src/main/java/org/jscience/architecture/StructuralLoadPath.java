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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Analytical tool for tracing and evaluating structural load paths within a building.
 * It simulates how forces travel from points of application through beams, 
 * columns, and slabs down to the foundation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class StructuralLoadPath {

    private StructuralLoadPath() {}

    /**
     * Common types of structural building members.
     */
    public enum MemberType {
        BEAM, COLUMN, SLAB, WALL, FOUNDATION, TRUSS, BRACE
    }

    /**
     * Represents a discrete structural member with geometry and properties.
     */
    public record StructuralMember(
        String id,
        MemberType type,
        double startX, double startY, double startZ,
        double endX, double endY, double endZ,
        double crossSectionArea,   // m²
        double momentOfInertia,    // m⁴
        String material
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Represents a localized force or moment applied to the structure.
     */
    public record AppliedLoad(
        String id,
        double x, double y, double z,
        double forceX, double forceY, double forceZ,  // kN
        double momentX, double momentY, double momentZ  // kN·m
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Resultant internal forces acting on a specific structural member.
     */
    public record MemberForce(
        StructuralMember member,
        double axialForce,      // kN (+ tension, - compression)
        double shearForceY,     // kN
        double shearForceZ,     // kN
        double bendingMomentY,  // kN·m
        double bendingMomentZ,  // kN·m
        double torsion          // kN·m
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Traces the logical sequence of members that transmit a load down to 
     * the foundation level.
     * 
     * @param members list of all available structural members
     * @param load the applied load to trace
     * @return list of members in the order of load transmission
     */
    public static List<StructuralMember> traceLoadPath(List<StructuralMember> members,
            AppliedLoad load) {
        
        List<StructuralMember> path = new ArrayList<>();
        
        // Find member receiving the load
        StructuralMember receiving = findMemberAt(members, load.x(), load.y(), load.z());
        if (receiving == null) return path;
        path.add(receiving);
        
        // Trace down to foundation (Z=0)
        double currentZ = Math.min(receiving.startZ(), receiving.endZ());
        Set<String> visited = new HashSet<>();
        visited.add(receiving.id());
        
        while (currentZ > 0) {
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
     * Calculates reactions at support points for a set of applied loads using equilibrium.
     * Simplified model assuming equal distribution among identical supports.
     * 
     * @param members all structural members
     * @param loads all applied loads
     * @param supportIds list of IDs for foundation/support points
     * @return map of support IDs to reaction force vectors [Fx, Fy, Fz]
     */
    public static Map<String, double[]> calculateReactions(List<StructuralMember> members,
            List<AppliedLoad> loads, List<String> supportIds) {
        
        Map<String, double[]> reactions = new HashMap<>();
        if (supportIds.isEmpty()) return reactions;
        
        double totalFx = 0, totalFy = 0, totalFz = 0;
        for (AppliedLoad load : loads) {
            totalFx += load.forceX();
            totalFy += load.forceY();
            totalFz += load.forceZ();
        }
        
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
     * Verifies if a member's internal stress exceeds its capacity, considering 
     * both axial and bending stresses.
     * 
     * @param forces internal forces on the member
     * @param yieldStrength material yield strength in MPa
     * @param safetyFactor factor of safety (e.g., 1.5)
     * @return true if the member is structurally adequate
     */
    public static boolean checkMemberCapacity(MemberForce forces, double yieldStrength,
            double safetyFactor) {
        
        double area = forces.member().crossSectionArea();
        double I = forces.member().momentOfInertia();
        if (area <= 0 || I <= 0) return false;
        
        // Axial stress in MPa
        double axialStress = Math.abs(forces.axialForce()) / area * 1000;
        
        // Bending stress in MPa (simplified linear approximation)
        double c = Math.sqrt(area / Math.PI); 
        double bendingStress = Math.abs(forces.bendingMomentY()) * c / I * 1000;
        
        double combinedStress = axialStress + bendingStress;
        return combinedStress < yieldStrength / safetyFactor;
    }

    /**
     * Generates a structural summary (Free Body Diagram data) for a member.
     * 
     * @param member the member to describe
     * @param allForces list of calculated forces
     * @return formatted string description
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
     * Distributes a surface load from a slab to supporting beams using 
     * tributary area principles.
     * 
     * @param slabLoad pressure in kN/m²
     * @param slabLength length in meters
     * @param slabWidth width in meters
     * @param beams available supporting beams
     * @return map of beam IDs to total distributed load in kN
     */
    public static Map<String, Real> slabLoadDistribution(double slabLoad, 
            double slabLength, double slabWidth, List<StructuralMember> beams) {
        
        Map<String, Real> distribution = new HashMap<>();
        if (beams.isEmpty()) return distribution;
        
        double totalLoad = slabLoad * slabLength * slabWidth;
        double loadPerBeam = totalLoad / beams.size();
        
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
        double tolerance = 0.5;
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
        return (distance(a.startX(), a.startY(), a.startZ(), b.startX(), b.startY(), b.startZ()) < tolerance) ||
               (distance(a.startX(), a.startY(), a.startZ(), b.endX(), b.endY(), b.endZ()) < tolerance) ||
               (distance(a.endX(), a.endY(), a.endZ(), b.startX(), b.startY(), b.startZ()) < tolerance) ||
               (distance(a.endX(), a.endY(), a.endZ(), b.endX(), b.endY(), b.endZ()) < tolerance);
    }

    private static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) + Math.pow(z2-z1, 2));
    }
}
