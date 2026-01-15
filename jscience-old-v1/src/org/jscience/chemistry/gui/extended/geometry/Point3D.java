package org.jscience.chemistry.gui.extended.geometry;

/**
 * A class that provides mathematical definition of and utility for 3D
 * geometric points
 *
 * @author Zhidong Xie (zxie
 */
public class Point3D {
    /** X coordinates in 3D space */
    protected double x = 0.0;

    /** Y coordinates in 3D space */
    protected double y = 0.0;

    /** Z coordinates in 3D space */
    protected double z = 0.0;

/**
     * default constructor: coordinates are all 0.0;
     */
    public Point3D() {
    }

/**
     * full constructor
     *
     * @param x X coordinate double
     * @param y Y coordinate double
     * @param z Z coordinate double
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

/**
     * copy constructor
     *
     * @param p the point to be copied
     */
    public Point3D(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    /**
     * calculate distance from this point to the other
     *
     * @param P the other Point
     *
     * @return DOCUMENT ME!
     */
    public double distanceTo(Point3D P) {
        return Math.sqrt(((x - P.x) * (x - P.x)) + ((y - P.y) * (y - P.y)) +
            ((z - P.z) * (z - P.z)));
    }

    /**
     * calculate the squre of distance from this point to the other
     *
     * @param P the other Point
     *
     * @return distance  : |P1P2|^2
     */
    public double distSquareTo(Point3D P) {
        return ((x - P.x) * (x - P.x)) + ((y - P.y) * (y - P.y)) +
        ((z - P.z) * (z - P.z));
    }

    /**
     * calculate angle among three points in 3D space note: calling
     * distSquare() is more efficient than distance().
     *
     * @param P1 y1, z1 : coordinates of point P1
     * @param P2 y2, z2 : coordinates of point P2
     *
     * @return angle : angle P1-this point-P2
     */
    public Angle angleWith(Point3D P1, Point3D P2) {
        // notation: O-this point, 2-square, P1,P2-points:
        double P1O_2 = distSquareTo(P1);
        double P2O_2 = distSquareTo(P2);
        double P1P2_2 = P1.distSquareTo(P2);
        double result = Math.acos(((P1O_2 + P2O_2) - P1P2_2) / 2.0 / Math.sqrt(
                    P1O_2 * P2O_2));

        return new Angle(result, false);
    }

    /**
     * move(translate) this point along with the input geometric vector
     *
     * @param gv geometric vector
     */
    public void translate(GeoVector3D gv) {
        x += gv.getX();
        y += gv.getY();
        z += gv.getZ();
    }

    /**
     * move(translate) this point by the input quantity along the 3
     * axises
     *
     * @param dx translation along x axis
     * @param dy translation along y axis
     * @param dz translation along z axis
     */
    public void translate(double dx, double dy, double dz) {
        x += dx;
        y += dy;
        z += dz;
    }

    /**
     * Return X coordinate
     *
     * @return DOCUMENT ME!
     */
    public double getX() {
        return x;
    }

    /**
     * Return Y coordinate
     *
     * @return DOCUMENT ME!
     */
    public double getY() {
        return y;
    }

    /**
     * Return Z coordinate
     *
     * @return DOCUMENT ME!
     */
    public double getZ() {
        return z;
    }

    /**
     * Set X coordinate
     *
     * @param x DOCUMENT ME!
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set Y coordinate
     *
     * @param y DOCUMENT ME!
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set Z coordinate
     *
     * @param z DOCUMENT ME!
     */
    public void setZ(double z) {
        this.z = z;
    }
} // end of Point3D class
