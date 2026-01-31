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

package org.jscience.social.architecture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;

/**
 * Analytical tool for checking architectural compliance with accessibility 
 * standards such as ADA, EN 17210, and ISO 21542.
 * It provides automated audits for door widths, corridor clearance, ramp slopes, 
 * and maneuvering spaces.
 */
public final class AccessibilityChecker {

    private AccessibilityChecker() {}

    /**
     * Recognized international and regional accessibility standards.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Standard {
        /** Americans with Disabilities Act 2010. */
        ADA_2010("Americans with Disabilities Act 2010"),
        /** EN 17210:2021 European Accessibility standard. */
        EN_17210("EN 17210:2021 European Accessibility"),
        /** BS 8300:2018 UK Standard for inclusive design. */
        BS_8300("BS 8300:2018 UK Standard"),
        /** ISO 21542:2011 International standard for accessibility. */
        ISO_21542("ISO 21542:2011 International");

        private final String description;
        Standard(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    /**
     * Detail of a specific accessibility non-compliance.
     */
    public record AccessibilityIssue(
        String element, 
        String requirement, 
        String violation, 
        Severity severity
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }
    
    /**
     * Level of priority/danger for an accessibility issue.
     */
    public enum Severity { 
        /** Complete barrier to access. */
        CRITICAL, 
        /** Significant difficulty for users. */
        MAJOR, 
        /** Minor deviation or inconvenience. */
        MINOR 
    }

    // Default ADA requirements (SI meters)
    private static final double MIN_DOOR_WIDTH_M = 0.813; // 32 inches
    private static final double MIN_CORRIDOR_WIDTH_M = 0.914; // 36 inches
    private static final double MAX_RAMP_SLOPE = 1.0 / 12.0; // 1:12 or 8.33%
    private static final double MIN_TURNING_RADIUS_M = 1.524; // 60 inches
    private static final double MAX_REACH_HEIGHT_M = 1.219; // 48 inches
    private static final double MIN_REACH_HEIGHT_M = 0.381; // 15 inches

    /**
     * Checks if a door's clear opening width meets the minimum requirement.
     * 
     * @param width the measured width of the door opening
     * @param doorId identifier for the door
     * @return an Optional containing an issue if non-compliant
     */
    public static Optional<AccessibilityIssue> checkDoorWidth(Quantity<Length> width, String doorId) {
        double w = width.to(Units.METER).getValue().doubleValue();
        if (w < MIN_DOOR_WIDTH_M) {
            return Optional.of(new AccessibilityIssue(
                "Door: " + doorId,
                "Minimum clear width: " + (MIN_DOOR_WIDTH_M * 100) + " cm",
                "Actual width: " + String.format("%.1f", w * 100) + " cm",
                Severity.CRITICAL
            ));
        }
        return Optional.empty();
    }

    /**
     * Checks if a corridor's width provides sufficient clearance for wheelchair passage.
     * 
     * @param width the measured width of the corridor
     * @param corridorId identifier for the corridor
     * @return an Optional containing an issue if non-compliant
     */
    public static Optional<AccessibilityIssue> checkCorridorWidth(Quantity<Length> width, String corridorId) {
        double w = width.to(Units.METER).getValue().doubleValue();
        if (w < MIN_CORRIDOR_WIDTH_M) {
            return Optional.of(new AccessibilityIssue(
                "Corridor: " + corridorId,
                "Minimum width: " + (MIN_CORRIDOR_WIDTH_M * 100) + " cm",
                "Actual width: " + String.format("%.1f", w * 100) + " cm",
                Severity.CRITICAL
            ));
        }
        return Optional.empty();
    }

    /**
     * Checks if a ramp's slope exceeds the maximum allowed grade.
     * 
     * @param rise the vertical height increase
     * @param run the horizontal length
     * @param rampId identifier for the ramp
     * @return an Optional containing an issue if the slope is too steep
     */
    public static Optional<AccessibilityIssue> checkRampSlope(Quantity<Length> rise, 
            Quantity<Length> run, String rampId) {
        double r = rise.to(Units.METER).getValue().doubleValue();
        double l = run.to(Units.METER).getValue().doubleValue();
        if (l <= 0) return Optional.empty();
        double slope = r / l;
        
        if (slope > MAX_RAMP_SLOPE) {
            return Optional.of(new AccessibilityIssue(
                "Ramp: " + rampId,
                "Maximum slope: 1:" + (int)(1/MAX_RAMP_SLOPE) + " (" + String.format("%.1f", MAX_RAMP_SLOPE * 100) + "%)",
                "Actual slope: 1:" + String.format("%.1f", 1/slope) + " (" + String.format("%.1f", slope * 100) + "%)",
                Severity.CRITICAL
            ));
        }
        return Optional.empty();
    }

    /**
     * Checks if a defined space provides enough room for a 180-degree wheelchair turn.
     * 
     * @param diameter the diameter of the circular maneuvering space
     * @param spaceId identifier for the space
     * @return an Optional containing an issue if the space is too small
     */
    public static Optional<AccessibilityIssue> checkTurningSpace(Quantity<Length> diameter, String spaceId) {
        double d = diameter.to(Units.METER).getValue().doubleValue();
        if (d < MIN_TURNING_RADIUS_M * 2) {
            return Optional.of(new AccessibilityIssue(
                "Space: " + spaceId,
                "Minimum turning diameter: " + (MIN_TURNING_RADIUS_M * 2 * 100) + " cm",
                "Actual diameter: " + String.format("%.1f", d * 100) + " cm",
                Severity.MAJOR
            ));
        }
        return Optional.empty();
    }

    /**
     * Checks if operable parts (switches, handles) are within the required 
     * vertical reach range (e.g., 38cm to 122cm).
     * 
     * @param height the height of the control from the floor
     * @param controlId identifier for the control
     * @return an Optional containing an issue if out of reach
     */
    public static Optional<AccessibilityIssue> checkReachHeight(Quantity<Length> height, String controlId) {
        double h = height.to(Units.METER).getValue().doubleValue();
        if (h > MAX_REACH_HEIGHT_M) {
            return Optional.of(new AccessibilityIssue(
                "Control: " + controlId,
                "Maximum height: " + (MAX_REACH_HEIGHT_M * 100) + " cm",
                "Actual height: " + String.format("%.1f", h * 100) + " cm",
                Severity.MAJOR
            ));
        }
        if (h < MIN_REACH_HEIGHT_M) {
            return Optional.of(new AccessibilityIssue(
                "Control: " + controlId,
                "Minimum height: " + (MIN_REACH_HEIGHT_M * 100) + " cm",
                "Actual height: " + String.format("%.1f", h * 100) + " cm",
                Severity.MINOR
            ));
        }
        return Optional.empty();
    }

    /**
     * Conducts a full accessibility audit on multiple architectural elements.
     * 
     * @param doorWidths map of door IDs to their widths
     * @param corridorWidths map of corridor IDs to their widths
     * @param ramps map of ramp IDs to their [rise, run] values
     * @param turningSpaces map of space IDs to their turning diameters
     * @return list of all detected accessibility issues sorted by severity
     */
    public static List<AccessibilityIssue> fullAudit(Map<String, Quantity<Length>> doorWidths,
            Map<String, Quantity<Length>> corridorWidths,
            Map<String, double[]> ramps,
            Map<String, Quantity<Length>> turningSpaces) {
        
        List<AccessibilityIssue> issues = new ArrayList<>();
        doorWidths.forEach((id, width) -> checkDoorWidth(width, id).ifPresent(issues::add));
        corridorWidths.forEach((id, width) -> checkCorridorWidth(width, id).ifPresent(issues::add));
        turningSpaces.forEach((id, diameter) -> checkTurningSpace(diameter, id).ifPresent(issues::add));
        
        // Custom check for ramps map format
        ramps.forEach((id, dim) -> {
            if (dim.length >= 2) {
                checkRampSlope(org.jscience.core.measure.Quantities.create(dim[0], Units.METER),
                               org.jscience.core.measure.Quantities.create(dim[1], Units.METER), id)
                    .ifPresent(issues::add);
            }
        });
        
        issues.sort((a, b) -> a.severity().compareTo(b.severity()));
        return issues;
    }
}

