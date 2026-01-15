/*
 * AtlasPosition.java
 *
 * Created on December 29, 2004, 8:58 PM
 */
package org.jscience.physics.solids.geom;

import org.jscience.physics.solids.AtlasObject;

import javax.media.j3d.Transform3D;

import javax.vecmath.Point3d;


/**
 * A spatial location.
 *
 * @author Wegge
 */
public class AtlasPosition extends AtlasObject {
    /**
     * DOCUMENT ME!
     */
    protected static String TYPE = "Position";

    /**
     * DOCUMENT ME!
     */
    private double[] coords = new double[3];

    /**
     * DOCUMENT ME!
     */
    private AtlasCoordSys cs = AtlasCoordSys.GLOBAL;

/**
     * CRreates a point at global origin.
     */
    public AtlasPosition() {
    }

/**
     * Creates a point at the specified xyz location in the global CS.
     */
    public AtlasPosition(double x, double y, double z) {
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
    }

/**
     * Creates a point at the specified xyz location in the specified CS.
     */
    public AtlasPosition(AtlasCoordSys cs, double x, double y, double z) {
        this.cs = cs;
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Returns the location in the global coordinate system. The
     * returned array is three elements long, in the order of x,y,z.
     *
     * @return DOCUMENT ME!
     */
    public double[] getGlobalPosition() {
        if (!cs.isGlobal()) {
            Transform3D T = cs.getInverseTransformation();

            Point3d p = new Point3d(coords);
            T.transform(p);

            double[] ret = new double[3];
            p.get(ret);

            return ret;
        }

        return coords;
    }

    /**
     * Computes the distance between the two points.
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeDistance(AtlasPosition p) {
        double[] lhs = getGlobalPosition();
        double[] rhs = p.getGlobalPosition();

        double dx = lhs[0] - rhs[0];
        double dy = lhs[1] - rhs[1];
        double dz = lhs[2] - rhs[2];

        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = getType() + " " + getId() + " : ( ";

        double[] loc = coords;
        ret = ret + loc[0] + " , " + loc[1] + " , " + loc[2] + " )";

        if (!cs.isGlobal()) {
            ret = ret + "\n\n" + cs;
        }

        return ret;
    }

    /**
     * Creates numPoints evenly spaced between the two nodes. ALl of
     * the returned AtlasPositions will be in the GLOBAL coordinate system
     * (but this might change).
     *
     * @param numPoints DOCUMENT ME!
     * @param otherPoint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasPosition[] interpolatePoints(int numPoints,
        AtlasPosition otherPoint) {
        double[] lhs = this.getGlobalPosition();
        double[] rhs = otherPoint.getGlobalPosition();
        double dx = (rhs[0] - lhs[0]) / (numPoints + 1);
        double dy = (rhs[1] - lhs[1]) / (numPoints + 1);
        double dz = (rhs[2] - lhs[2]) / (numPoints + 1);

        AtlasPosition[] ret = new AtlasPosition[numPoints];

        for (int i = 0; i < numPoints; i++) {
            double xloc = lhs[0] + ((i + 1) * dx);
            double yloc = lhs[1] + ((i + 1) * dy);
            double zloc = lhs[2] + ((i + 1) * dz);

            ret[i] = new AtlasPosition(xloc, yloc, zloc);
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getX() {
        return coords[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getY() {
        return coords[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getZ() {
        return coords[2];
    }

    /**
     * Determines whether the two vectors have the same direction
     *
     * @param v DOCUMENT ME!
     * @param tolerance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEquivalent(AtlasPosition v, double tolerance) {
        if (this.computeDistance(v) > tolerance) {
            return false;
        }

        return true;
    }
}
