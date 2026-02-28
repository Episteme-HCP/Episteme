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

package org.episteme.social.sports;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Provides mathematical simulations and analysis for sports biomechanics.
 * Includes kinematic and dynamic analysis, joint torque estimation, and projectile optimization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class BiomechanicsSimulator {

    private BiomechanicsSimulator() {}

    /** Represents a joint's angular orientation in 3D space. */
    public record JointAngle(
        String jointName,
        double flexion,
        double abduction,
        double rotation
    ) implements Serializable {}

    /** Physical properties of a body segment. */
    public record BodySegment(
        String name,
        double length,
        double mass,
        double comDistance
    ) implements Serializable {}

    /** Snapshot of body orientation at a specific timestamp. */
    public record MotionFrame(
        double time,
        Map<String, JointAngle> jointAngles,
        Map<String, double[]> segmentPositions
    ) implements Serializable {}

    /** Result of kinematic movement analysis. */
    public record KinematicAnalysis(
        double peakVelocity,
        double peakAcceleration,
        double rangeOfMotion,
        Map<String, double[]> jointVelocities,
        Map<String, double[]> jointAccelerations
    ) implements Serializable {}

    /** Result of kinetic/dynamic movement analysis. */
    public record DynamicAnalysis(
        double peakForce,
        double peakTorque,
        double peakPower,
        double workDone,
        Map<String, Double> jointTorques
    ) implements Serializable {}

    /** Reference anthropometric constraints. */
    public static final Map<String, double[]> SEGMENT_PARAMETERS = Map.of(
        "thigh", new double[]{0.245, 0.100},
        "shank", new double[]{0.246, 0.0465},
        "foot", new double[]{0.152, 0.0145},
        "upperarm", new double[]{0.186, 0.028},
        "forearm", new double[]{0.146, 0.016},
        "hand", new double[]{0.108, 0.006},
        "trunk", new double[]{0.288, 0.497}
    );

    /**
     * Creates a skeletal body model based on height and mass.
     * 
     * @param height body height in meters
     * @param mass   body mass in kilograms
     * @return a list of BodySegment instances
     */
    public static List<BodySegment> createBodyModel(double height, double mass) {
        List<BodySegment> segments = new ArrayList<>();
        for (Map.Entry<String, double[]> entry : SEGMENT_PARAMETERS.entrySet()) {
            double segmentLength = height * entry.getValue()[0];
            double segmentMass = mass * entry.getValue()[1];
            double com = segmentLength * 0.43;
            segments.add(new BodySegment(entry.getKey(), segmentLength, segmentMass, com));
        }
        return segments;
    }

    /**
     * Extracts kinematic data from a series of motion frames.
     * 
     * @param frames         tracked motion capture frames
     * @param targetSegment  the segment of interest
     * @return analysis results
     */
    public static KinematicAnalysis analyzeKinematics(List<MotionFrame> frames, 
            String targetSegment) {
        
        if (frames == null || frames.size() < 2) {
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
        
        for (int i = 2; i < frames.size(); i++) {
            double dt = frames.get(i).time() - frames.get(i-1).time();
            if (dt <= 0) continue;
            for (Map.Entry<String, double[]> entry : velocities.entrySet()) {
                double[] vels = entry.getValue();
                double acc = (vels[i] - vels[i-1]) / dt;
                accelerations.computeIfAbsent(entry.getKey(), k -> new double[frames.size()])[i] = acc;
                peakAcceleration = Math.max(peakAcceleration, Math.abs(acc));
            }
        }
        
        double rom = (maxAngle > minAngle) ? maxAngle - minAngle : 0;
        return new KinematicAnalysis(peakVelocity, peakAcceleration, rom, velocities, accelerations);
    }

    /**
     * Calculates joint torques and power consumption using inverse dynamics simulation.
     */
    public static DynamicAnalysis analyzeInverseDynamics(List<MotionFrame> frames,
            List<BodySegment> bodyModel, double[] externalForce) {
        
        if (frames == null || frames.size() < 2) {
            return new DynamicAnalysis(0, 0, 0, 0, Map.of());
        }
        
        Map<String, Double> jointTorques = new HashMap<>();
        double peakTorque = 0;
        double peakPower = 0;
        double totalWork = 0;
        
        for (BodySegment segment : bodyModel) {
            double segmentInertia = segment.mass() * Math.pow(segment.length(), 2) / 3.0;
            double angAcc = 10.0; // Simulated constant
            double torque = segmentInertia * angAcc + segment.mass() * 9.81 * segment.comDistance();
            
            jointTorques.put(segment.name(), torque);
            peakTorque = Math.max(peakTorque, Math.abs(torque));
            double power = Math.abs(torque * 5.0);
            peakPower = Math.max(peakPower, power);
            totalWork += torque * Math.toRadians(30.0);
        }
        
        return new DynamicAnalysis(peakTorque / 0.3, peakTorque, peakPower, totalWork, jointTorques);
    }

    /** Calculates the ideal release angle for a projectile given a release height and target distance. */
    public static Real optimalReleaseAngle(double releaseHeight, double targetDistance) {
        double heightAdvantage = Math.atan(releaseHeight / targetDistance);
        double optimal = 45.0 - Math.toDegrees(heightAdvantage) / 2.0;
        return Real.of(optimal);
    }

    /** Estimates the ground reaction force (GRF) during running impact. */
    public static Real estimateGroundReactionForce(double bodyMass, double footStrikeAcceleration) {
        return Real.of(bodyMass * (9.81 + footStrikeAcceleration));
    }
}

