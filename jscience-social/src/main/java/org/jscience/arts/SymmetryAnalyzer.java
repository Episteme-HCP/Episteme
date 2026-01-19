package org.jscience.arts;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Analyzes symmetry and repetitive patterns in visual compositions.
 */
public final class SymmetryAnalyzer {

    private SymmetryAnalyzer() {}

    public enum SymmetryType {
        BILATERAL,      // Mirror symmetry
        ROTATIONAL,     // N-fold rotation
        TRANSLATIONAL,  // Repeating pattern
        POINT,          // Central symmetry
        GLIDE,          // Translation + reflection
        NONE
    }

    public record SymmetryResult(
        SymmetryType type,
        double confidence,    // 0-1
        double[] axis,        // [x1, y1, x2, y2] for bilateral
        int foldOrder,        // n for n-fold rotational
        double[] center       // [x, y] for point/rotational
    ) {}

    /**
     * Detects bilateral (mirror) symmetry in a point cloud.
     */
    public static SymmetryResult detectBilateralSymmetry(List<double[]> points) {
        if (points.size() < 4) {
            return new SymmetryResult(SymmetryType.NONE, 0, null, 0, null);
        }
        
        // Find centroid
        double[] centroid = calculateCentroid(points);
        
        // Test vertical axis
        double verticalScore = testAxisSymmetry(points, centroid, Math.PI / 2);
        
        // Test horizontal axis
        double horizontalScore = testAxisSymmetry(points, centroid, 0);
        
        // Test diagonal axes
        double diag1Score = testAxisSymmetry(points, centroid, Math.PI / 4);
        double diag2Score = testAxisSymmetry(points, centroid, 3 * Math.PI / 4);
        
        double bestScore = Math.max(Math.max(verticalScore, horizontalScore),
                                     Math.max(diag1Score, diag2Score));
        
        if (bestScore < 0.5) {
            return new SymmetryResult(SymmetryType.NONE, bestScore, null, 0, centroid);
        }
        
        double bestAngle = verticalScore == bestScore ? Math.PI / 2 :
                          horizontalScore == bestScore ? 0 :
                          diag1Score == bestScore ? Math.PI / 4 : 3 * Math.PI / 4;
        
        double[] axis = new double[] {
            centroid[0] + 100 * Math.cos(bestAngle),
            centroid[1] + 100 * Math.sin(bestAngle),
            centroid[0] - 100 * Math.cos(bestAngle),
            centroid[1] - 100 * Math.sin(bestAngle)
        };
        
        return new SymmetryResult(SymmetryType.BILATERAL, bestScore, axis, 1, centroid);
    }

    /**
     * Detects rotational symmetry.
     */
    public static SymmetryResult detectRotationalSymmetry(List<double[]> points) {
        double[] centroid = calculateCentroid(points);
        
        int bestFold = 1;
        double bestScore = 0;
        
        for (int n = 2; n <= 12; n++) {
            double score = testRotationalSymmetry(points, centroid, n);
            if (score > bestScore) {
                bestScore = score;
                bestFold = n;
            }
        }
        
        if (bestScore < 0.5) {
            return new SymmetryResult(SymmetryType.NONE, bestScore, null, 0, centroid);
        }
        
        return new SymmetryResult(SymmetryType.ROTATIONAL, bestScore, null, bestFold, centroid);
    }

    /**
     * Detects repeating patterns (translational symmetry).
     */
    public static SymmetryResult detectTranslationalSymmetry(List<double[]> points,
            double minPeriod, double maxPeriod) {
        
        if (points.size() < 6) {
            return new SymmetryResult(SymmetryType.NONE, 0, null, 0, null);
        }
        
        double bestScore = 0;
        double[] bestTranslation = null;
        
        // Test horizontal translation
        for (double period = minPeriod; period <= maxPeriod; period += 5) {
            double score = testTranslation(points, period, 0);
            if (score > bestScore) {
                bestScore = score;
                bestTranslation = new double[] {period, 0};
            }
        }
        
        // Test vertical translation
        for (double period = minPeriod; period <= maxPeriod; period += 5) {
            double score = testTranslation(points, 0, period);
            if (score > bestScore) {
                bestScore = score;
                bestTranslation = new double[] {0, period};
            }
        }
        
        if (bestScore < 0.5 || bestTranslation == null) {
            return new SymmetryResult(SymmetryType.NONE, bestScore, null, 0, null);
        }
        
        return new SymmetryResult(SymmetryType.TRANSLATIONAL, bestScore,
            bestTranslation, 0, null);
    }

    /**
     * Calculates the symmetry quotient of a shape (0-1).
     */
    public static Real symmetryQuotient(List<double[]> points) {
        SymmetryResult bilateral = detectBilateralSymmetry(points);
        SymmetryResult rotational = detectRotationalSymmetry(points);
        
        double combined = Math.max(bilateral.confidence(), rotational.confidence());
        return Real.of(combined);
    }

    private static double[] calculateCentroid(List<double[]> points) {
        double sumX = 0, sumY = 0;
        for (double[] p : points) {
            sumX += p[0];
            sumY += p[1];
        }
        return new double[] {sumX / points.size(), sumY / points.size()};
    }

    private static double testAxisSymmetry(List<double[]> points, double[] center, double angle) {
        int matches = 0;
        double tolerance = 10.0;
        
        for (double[] p : points) {
            double[] reflected = reflectPoint(p, center, angle);
            for (double[] q : points) {
                if (distance(reflected, q) < tolerance) {
                    matches++;
                    break;
                }
            }
        }
        
        return (double) matches / points.size();
    }

    private static double testRotationalSymmetry(List<double[]> points, double[] center, int n) {
        double angle = 2 * Math.PI / n;
        int matches = 0;
        double tolerance = 10.0;
        
        for (double[] p : points) {
            double[] rotated = rotatePoint(p, center, angle);
            for (double[] q : points) {
                if (distance(rotated, q) < tolerance) {
                    matches++;
                    break;
                }
            }
        }
        
        return (double) matches / points.size();
    }

    private static double testTranslation(List<double[]> points, double dx, double dy) {
        int matches = 0;
        double tolerance = 10.0;
        
        for (double[] p : points) {
            double[] translated = new double[] {p[0] + dx, p[1] + dy};
            for (double[] q : points) {
                if (distance(translated, q) < tolerance) {
                    matches++;
                    break;
                }
            }
        }
        
        return (double) matches / points.size();
    }

    private static double[] reflectPoint(double[] p, double[] center, double angle) {
        double dx = p[0] - center[0];
        double dy = p[1] - center[1];
        
        double cos2 = Math.cos(2 * angle);
        double sin2 = Math.sin(2 * angle);
        
        return new double[] {
            center[0] + dx * cos2 + dy * sin2,
            center[1] + dx * sin2 - dy * cos2
        };
    }

    private static double[] rotatePoint(double[] p, double[] center, double angle) {
        double dx = p[0] - center[0];
        double dy = p[1] - center[1];
        
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        
        return new double[] {
            center[0] + dx * cos - dy * sin,
            center[1] + dx * sin + dy * cos
        };
    }

    private static double distance(double[] a, double[] b) {
        return Math.sqrt(Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2));
    }
}
