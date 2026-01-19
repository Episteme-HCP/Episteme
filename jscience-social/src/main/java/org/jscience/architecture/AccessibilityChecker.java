package org.jscience.architecture;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.Units;
import java.util.*;

/**
 * Accessibility compliance checker for buildings.
 */
public final class AccessibilityChecker {

    private AccessibilityChecker() {}

    public enum Standard {
        ADA_2010("Americans with Disabilities Act 2010"),
        EN_17210("EN 17210:2021 European Accessibility"),
        BS_8300("BS 8300:2018 UK Standard"),
        ISO_21542("ISO 21542:2011 International");

        private final String description;
        Standard(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    public record AccessibilityIssue(String element, String requirement, String violation, Severity severity) {}
    
    public enum Severity { CRITICAL, MAJOR, MINOR }

    // Default ADA requirements
    private static final double MIN_DOOR_WIDTH_M = 0.813; // 32 inches
    private static final double MIN_CORRIDOR_WIDTH_M = 0.914; // 36 inches
    private static final double MAX_RAMP_SLOPE = 1.0 / 12.0; // 8.33%
    private static final double MIN_TURNING_RADIUS_M = 1.524; // 60 inches
    private static final double MAX_REACH_HEIGHT_M = 1.219; // 48 inches
    private static final double MIN_REACH_HEIGHT_M = 0.381; // 15 inches

    /**
     * Checks if a door width meets accessibility requirements.
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
     * Checks if a corridor width meets requirements.
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
     * Checks if a ramp slope is accessible.
     */
    public static Optional<AccessibilityIssue> checkRampSlope(Quantity<Length> rise, 
            Quantity<Length> run, String rampId) {
        double r = rise.to(Units.METER).getValue().doubleValue();
        double l = run.to(Units.METER).getValue().doubleValue();
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
     * Checks if a space allows for wheelchair turning.
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
     * Checks if controls are within reach range.
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
     * Comprehensive accessibility audit.
     */
    public static List<AccessibilityIssue> fullAudit(Map<String, Quantity<Length>> doorWidths,
            Map<String, Quantity<Length>> corridorWidths,
            Map<String, double[]> ramps, // rise, run pairs
            Map<String, Quantity<Length>> turningSpaces) {
        
        List<AccessibilityIssue> issues = new ArrayList<>();
        
        doorWidths.forEach((id, width) -> checkDoorWidth(width, id).ifPresent(issues::add));
        corridorWidths.forEach((id, width) -> checkCorridorWidth(width, id).ifPresent(issues::add));
        turningSpaces.forEach((id, diameter) -> checkTurningSpace(diameter, id).ifPresent(issues::add));
        
        // Sort by severity
        issues.sort((a, b) -> a.severity().compareTo(b.severity()));
        
        return issues;
    }
}
