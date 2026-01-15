/*
 * AtlasCoordSys.java
 *
 * Created on December 29, 2004, 8:45 PM
 */

package org.jscience.physics.solids.geom;

import org.jscience.physics.solids.AtlasObject;
import org.jscience.physics.solids.AtlasUtils;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

/**
 * Basic coordinate system definition.
 * <p/>
 * Right now, everything is a "get" from this class. This is to prevent confusion when modifying
 * the CS object:  for example, if a Position references a CS, and the origin of that CS changes,
 * should the point also move in space?
 *
 * @author Wegge
 */
public class AtlasCoordSys extends AtlasObject {

    protected static String TYPE = "CoordSys";

    public static String CARTESIAN = "Cartesian";
    public static String CYLINDRICAL = "Cylindrical";
    public static String SPHERICAL = "Spherical";


    private Matrix3d T;
    private double[] origin;

    public final static AtlasCoordSys GLOBAL = new AtlasCoordSys();

    /**
     * Creates a new global Coordinate System.
     */
    public AtlasCoordSys() {

        T = new Matrix3d();
        T.setIdentity();
        origin = new double[3];
    }

    /**
     * Creates a new coordinate system with given location and orientation.
     */
    public AtlasCoordSys(AtlasPosition origin, AtlasVector xAxis, AtlasVector xyAxis) throws InvalidCoordSysException {

        this.origin = origin.getGlobalPosition();

        if (xAxis.isParallel(xyAxis)) {
            throw new InvalidCoordSysException("X axis is parallel to XY vector");
        }

        AtlasVector tempX = xAxis.clone();
        tempX.normalize();

        AtlasVector tempXY = xyAxis.clone();
        tempXY.normalize();

        AtlasVector tempZ = tempX.cross(tempXY);

        tempZ.normalize();

        AtlasVector tempY = tempZ.cross(tempX);

        //Load up the transform
        double m[] = new double[9];

        double xvals[] = tempX.getGlobalDirection();
        double yvals[] = tempY.getGlobalDirection();
        double zvals[] = tempZ.getGlobalDirection();
        for (int i = 0; i < 3; i++) {
            m[i] = xvals[i];
            m[3 + i] = yvals[i];
            m[6 + i] = zvals[i];
        }
        T = new Matrix3d(m);
    }

    /**
     * Returns the origin of this coordinate system.
     */
    public double[] getOrigin() {
        return origin;
    }

    /**
     * Returns the transformation of this coordsys. Note that this is a copy
     * of the transformation ,so you can't screw up the CS.
     */
    public Transform3D getTransformation() {

        return new Transform3D(T, new Vector3d(origin), 1.0);
    }


    /**
     * Returns the inverse of the transformation.
     */
    public Transform3D getInverseTransformation() {

        Transform3D ret = new Transform3D(T, new Vector3d(origin), 1.0);
        ret.invert();

        return ret;
    }


    /**
     * Returns type as "CoordSys".
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Determines whether the coordsys is a global.
     */
    public boolean isGlobal() {

        if (origin[0] != 0) return false;
        if (origin[1] != 0) return false;
        if (origin[2] != 0) return false;

        if (T.m00 != 1) return false;
        if (T.m11 != 1) return false;
        if (T.m22 != 1) return false;

        return true;

    }

    /**
     * Convenience method that returns a summary of the object.
     */
    public String toString() {
        String ret = "CoordSys " + getId() + "\n";

        ret = ret + "Origin :" + AtlasUtils.convertDoubles(origin) + "\n";

        ret = ret + "Transform: " + T.toString();

        return ret;
    }

}
