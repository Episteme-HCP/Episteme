package org.jscience.chemistry.gui.extended.geometry;

/**
 * A class that defines geometric straight line in 3D space
 *
 * @author Zhidong Xie (zxie
 */
public class Line {
    /** a point on the line */
    Point3D pointA;

    /** the other point on the line */
    Point3D pointB;

/**
     * Default constructor
     */
    public Line() {
        pointA = new Point3D(0.0, 0.0, 0.0);
        pointB = new Point3D(0.0, 0.0, 0.0);
    }

/**
     * Full constructor
     *
     * @param pointA a point on the line
     * @param pointB the other point on the line
     */
    public Line(Point3D pointA, Point3D pointB) {
        this.pointA = new Point3D(pointA);
        this.pointB = new Point3D(pointB);
    }

    /**
     * Return the geometric vector from some point on the line to the
     * given point so that the vector is perpendicular to the line. Assume the
     * given point is not on the line
     *
     * @param pointX the give point
     *
     * @return DOCUMENT ME!
     */
    public GeoVector3D vectorTo(Point3D pointX) {
        GeoVector3D ap = new GeoVector3D(pointA, pointX);
        GeoVector3D ab = new GeoVector3D(pointA, pointB);
        GeoVector3D ap1 = GeometryUtils.project(ap, ab);
        ap.minus(ap1);

        return ap;
    }

    /**
     * Return the distance from a given point to the line
     *
     * @param pointX DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double distanceTo(Point3D pointX) {
        return (vectorTo(pointX)).length();
    }
} //end of Line class
