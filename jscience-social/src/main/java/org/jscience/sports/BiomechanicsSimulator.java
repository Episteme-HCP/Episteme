package org.jscience.sports;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Biomechanical analysis of sports movements.
 */
public final class BiomechanicsSimulator {

    private BiomechanicsSimulator() {}

    public record JointAngle(
        String jointName,
        double flexion,      // degrees
        double abduction,    // degrees
        double rotation      // degrees
    ) {}

    public record BodySegment(
        String name,
        double length,       // m
        double mass,         // kg
        double comDistance   // distance to center of mass from proximal end
    ) {}

    public record MotionFrame(
        double time,
        Map<String, JointAngle> jointAngles,
        Map<String, double[]> segmentPositions  // x, y, z
    ) {}

    public record KinematicAnalysis(
        double peakVelocity,           // m/s
        double peakAcceleration,       // m/s²
        double rangeOfMotion,          // degrees
        Map<String, double[]> jointVelocities,
        Map<String, double[]> jointAccelerations
    ) {}

    public record DynamicAnalysis(
        double peakForce,             // N
        double peakTorque,            // N·m
        double peakPower,             // W
        double workDone,              // J
        Map<String, Double> jointTorques
    ) {}

    // Standard body segment parameters (percentage of body height/mass)
    public static final Map<String, double[]> SEGMENT_PARAMETERS = Map.of(
        "thigh", new double[]{0.245, 0.100},      // length ratio, mass ratio
        "shank", new double[]{0.246, 0.0465},
        "foot", new double[]{0.152, 0.0145},
        "upperarm", new double[]{0.186, 0.028},
        "forearm", new double[]{0.146, 0.016},
        "hand", new double[]{0.108, 0.006},
        "trunk", new double[]{0.288, 0.497}
    );

    /**
     * Creates body model from anthropometric data.
     */
    public static List<BodySegment> createBodyModel(double height, double mass) {
        List<BodySegment> segments = new ArrayList<>();
        
        for (var entry : SEGMENT_PARAMETERS.entrySet()) {
            double segmentLength = height * entry.getValue()[0];
            double segmentMass = mass * entry.getValue()[1];
            double com = segmentLength * 0.43; // Proximal COM
            
            segments.add(new BodySegment(entry.getKey(), segmentLength, segmentMass, com));
        }
        
        return segments;
    }

    /**
     * Calculates kinematic variables from motion capture data.
     */
    public static KinematicAnalysis analyzeKinematics(List<MotionFrame> frames, 
            String targetSegment) {
        
        if (frames.size() < 2) {
            return new KinematicAnalysis(0, 0, 0, Map.of(), Map.of());
        }
        
        double peakVelocity = 0;
        double peakAcceleration = 0;
        double minAngle = Double.MAX_VALUE;
        double maxAngle = Double.MIN_VALUE;
        
        Map<String, double[]> velocities = new HashMap<>();
        Map<String, double[]> accelerations = new HashMap<>();
        
        for (int i = 1; i < frames.size(); i++) {
            MotionFrame prev = frames.get(i - 1);
            MotionFrame curr = frames.get(i);
            double dt = curr.time() - prev.time();
            
            if (dt <= 0) continue;
            
            for (String joint : curr.jointAngles().keySet()) {
                JointAngle anglePrev = prev.jointAngles().get(joint);
                JointAngle angleCurr = curr.jointAngles().get(joint);
                
                if (anglePrev != null && angleCurr != null) {
                    double angVel = (angleCurr.flexion() - anglePrev.flexion()) / dt;
                    velocities.computeIfAbsent(joint, k -> new double[frames.size()])[i] = angVel;
                    
                    minAngle = Math.min(minAngle, angleCurr.flexion());
                    maxAngle = Math.max(maxAngle, angleCurr.flexion());
                }
            }
            
            // Linear velocity of segment
            double[] posPrev = prev.segmentPositions().get(targetSegment);
            double[] posCurr = curr.segmentPositions().get(targetSegment);
            
            if (posPrev != null && posCurr != null) {
                double vel = Math.sqrt(
                    Math.pow((posCurr[0] - posPrev[0]) / dt, 2) +
                    Math.pow((posCurr[1] - posPrev[1]) / dt, 2) +
                    Math.pow((posCurr[2] - posPrev[2]) / dt, 2)
                );
                
                peakVelocity = Math.max(peakVelocity, vel);
            }
        }
        
        // Calculate accelerations
        for (int i = 2; i < frames.size(); i++) {
            double dt = frames.get(i).time() - frames.get(i-1).time();
            for (String joint : velocities.keySet()) {
                double[] vels = velocities.get(joint);
                double acc = (vels[i] - vels[i-1]) / dt;
                accelerations.computeIfAbsent(joint, k -> new double[frames.size()])[i] = acc;
                peakAcceleration = Math.max(peakAcceleration, Math.abs(acc));
            }
        }
        
        double rom = maxAngle > minAngle ? maxAngle - minAngle : 0;
        
        return new KinematicAnalysis(peakVelocity, peakAcceleration, rom, velocities, accelerations);
    }

    /**
     * Calculates joint torques using inverse dynamics.
     */
    public static DynamicAnalysis analyzeInverseDynamics(List<MotionFrame> frames,
            List<BodySegment> bodyModel, double[] externalForce) {
        
        if (frames.size() < 2) {
            return new DynamicAnalysis(0, 0, 0, 0, Map.of());
        }
        
        Map<String, Double> jointTorques = new HashMap<>();
        double peakTorque = 0;
        double peakPower = 0;
        double totalWork = 0;
        
        for (BodySegment segment : bodyModel) {
            // Simplified inverse dynamics
            double segmentInertia = segment.mass() * Math.pow(segment.length(), 2) / 3;
            
            // Estimate angular acceleration from frames
            double angAcc = 10; // Placeholder - would calculate from actual motion data
            
            double torque = segmentInertia * angAcc + 
                           segment.mass() * 9.81 * segment.comDistance(); // Gravity moment
            
            jointTorques.put(segment.name(), torque);
            peakTorque = Math.max(peakTorque, Math.abs(torque));
            
            // Power = torque × angular velocity
            double angVel = 5; // Placeholder
            double power = torque * angVel;
            peakPower = Math.max(peakPower, Math.abs(power));
            
            // Work = torque × angular displacement
            totalWork += torque * Math.toRadians(30); // Placeholder displacement
        }
        
        double peakForce = peakTorque / 0.3; // Approximate moment arm
        
        return new DynamicAnalysis(peakForce, peakTorque, peakPower, totalWork, jointTorques);
    }

    /**
     * Calculates ideal release angle for projectile motion.
     */
    public static Real optimalReleaseAngle(double releaseHeight, double targetDistance) {
        // For flat ground and no air resistance, 45 degrees is optimal
        // Adjust for release height
        double heightAdvantage = Math.atan(releaseHeight / targetDistance);
        double optimal = 45 - Math.toDegrees(heightAdvantage) / 2;
        return Real.of(optimal);
    }

    /**
     * Estimates ground reaction force during running.
     */
    public static Real estimateGroundReactionForce(double bodyMass, double footStrikeAcceleration) {
        // F = ma, with typical multiplier for running impact
        double force = bodyMass * (9.81 + footStrikeAcceleration);
        return Real.of(force);
    }
}
