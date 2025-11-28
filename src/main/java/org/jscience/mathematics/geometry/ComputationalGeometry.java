package org.jscience.mathematics.geometry;

import org.jscience.mathematics.number.Real;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Computational geometry algorithms.
 * <p>
 * Convex hull, closest pair, line intersection, etc.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class ComputationalGeometry {

    public static class Point2D {
        public final Real x;
        public final Real y;

        public Point2D(Real x, Real y) {
            this.x = x;
            this.y = y;
        }

        public Real distanceTo(Point2D other) {
            Real dx = x.subtract(other.x);
            Real dy = y.subtract(other.y);
            return dx.multiply(dx).add(dy.multiply(dy)).sqrt();
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    /**
     * Convex Hull using Graham Scan - O(n log n).
     */
    public static List<Point2D> convexHull(List<Point2D> points) {
        if (points.size() < 3)
            return new ArrayList<>(points);

        // Find point with lowest y (and leftmost if tie)
        Point2D pivot = points.get(0);
        for (Point2D p : points) {
            if (p.y.compareTo(pivot.y) < 0 || (p.y.equals(pivot.y) && p.x.compareTo(pivot.x) < 0)) {
                pivot = p;
            }
        }

        final Point2D finalPivot = pivot;
        List<Point2D> sorted = new ArrayList<>(points);
        sorted.remove(pivot);

        // Sort by polar angle
        sorted.sort((p1, p2) -> {
            Real angle1 = polarAngle(finalPivot, p1);
            Real angle2 = polarAngle(finalPivot, p2);
            return angle1.compareTo(angle2);
        });

        List<Point2D> hull = new ArrayList<>();
        hull.add(pivot);
        hull.add(sorted.get(0));

        for (int i = 1; i < sorted.size(); i++) {
            while (hull.size() > 1
                    && !isLeftTurn(hull.get(hull.size() - 2), hull.get(hull.size() - 1), sorted.get(i))) {
                hull.remove(hull.size() - 1);
            }
            hull.add(sorted.get(i));
        }

        return hull;
    }

    private static Real polarAngle(Point2D pivot, Point2D p) {
        Real dx = p.x.subtract(pivot.x);
        Real dy = p.y.subtract(pivot.y);
        return Real.of(Math.atan2(dy.doubleValue(), dx.doubleValue()));
    }

    private static boolean isLeftTurn(Point2D a, Point2D b, Point2D c) {
        Real cross = b.x.subtract(a.x).multiply(c.y.subtract(a.y))
                .subtract(b.y.subtract(a.y).multiply(c.x.subtract(a.x)));
        return cross.compareTo(Real.ZERO) > 0;
    }

    /**
     * Closest pair of points - O(n log n) divide and conquer.
     */
    public static Point2D[] closestPair(List<Point2D> points) {
        if (points.size() < 2)
            return null;

        List<Point2D> sortedByX = new ArrayList<>(points);
        sortedByX.sort(Comparator.comparing(p -> p.x));

        return closestPairRecursive(sortedByX);
    }

    private static Point2D[] closestPairRecursive(List<Point2D> points) {
        if (points.size() <= 3) {
            return bruteForceClosest(points);
        }

        int mid = points.size() / 2;
        Point2D midPoint = points.get(mid);

        List<Point2D> left = points.subList(0, mid);
        List<Point2D> right = points.subList(mid, points.size());

        Point2D[] leftPair = closestPairRecursive(left);
        Point2D[] rightPair = closestPairRecursive(right);

        Real minDist = leftPair[0].distanceTo(leftPair[1]);
        Point2D[] result = leftPair;

        if (rightPair[0].distanceTo(rightPair[1]).compareTo(minDist) < 0) {
            minDist = rightPair[0].distanceTo(rightPair[1]);
            result = rightPair;
        }

        // Check strip
        List<Point2D> strip = new ArrayList<>();
        for (Point2D p : points) {
            if (p.x.subtract(midPoint.x).abs().compareTo(minDist) < 0) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size()
                    && strip.get(j).y.subtract(strip.get(i).y).compareTo(minDist) < 0; j++) {
                Real d = strip.get(i).distanceTo(strip.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point2D[] { strip.get(i), strip.get(j) };
                }
            }
        }

        return result;
    }

    private static Point2D[] bruteForceClosest(List<Point2D> points) {
        Real minDist = Real.of(Double.MAX_VALUE);
        Point2D[] result = new Point2D[] { points.get(0), points.get(1) };

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Real d = points.get(i).distanceTo(points.get(j));
                if (d.compareTo(minDist) < 0) {
                    minDist = d;
                    result = new Point2D[] { points.get(i), points.get(j) };
                }
            }
        }

        return result;
    }

    /**
     * Check if two line segments intersect.
     */
    public static boolean segmentsIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4)
            return true;

        // Collinear cases
        if (o1 == 0 && onSegment(p1, p2, q1))
            return true;
        if (o2 == 0 && onSegment(p1, q2, q1))
            return true;
        if (o3 == 0 && onSegment(p2, p1, q2))
            return true;
        if (o4 == 0 && onSegment(p2, q1, q2))
            return true;

        return false;
    }

    private static int orientation(Point2D p, Point2D q, Point2D r) {
        Real val = q.y.subtract(p.y).multiply(r.x.subtract(q.x))
                .subtract(q.x.subtract(p.x).multiply(r.y.subtract(q.y)));

        if (val.compareTo(Real.ZERO) == 0)
            return 0;
        return val.compareTo(Real.ZERO) > 0 ? 1 : 2;
    }

    private static boolean onSegment(Point2D p, Point2D q, Point2D r) {
        return q.x.compareTo(p.x.min(r.x)) >= 0 && q.x.compareTo(p.x.max(r.x)) <= 0 &&
                q.y.compareTo(p.y.min(r.y)) >= 0 && q.y.compareTo(p.y.max(r.y)) <= 0;
    }
}
