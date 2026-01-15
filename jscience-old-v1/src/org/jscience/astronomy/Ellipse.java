package org.jscience.astronomy;

import org.jscience.mathematics.geometry.LiteralVector3D;
import org.jscience.mathematics.geometry.Rotation3D;
import org.jscience.mathematics.geometry.Vector3D;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;


/**
 * The Ellipse class defines ellipses in a 3d dimension environment.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this also accounts for circular motions in particular
//this class is used as an approximation of movements for small objects in the gravity field of a big object
//use this to display the path of an Astral Body in the sky
//see the Geotransform package for earth ellispoids (that actually correspond to something else)
public class Ellipse extends Object {
    /**
     * DOCUMENT ME!
     */
    private Point3f origin;

    /**
     * DOCUMENT ME!
     */
    private Vector3f majorAxis;

    /**
     * DOCUMENT ME!
     */
    private Vector3f minorAxis;

    /**
     * Creates a new Ellipse object.
     *
     * @param origin DOCUMENT ME!
     * @param majorAxis DOCUMENT ME!
     * @param minorAxis DOCUMENT ME!
     */
    public Ellipse(Point3f origin, Vector3f majorAxis, Vector3f minorAxis) {
        if ((origin != null) && (majorAxis != null) && (minorAxis != null)) {
            if ((majorAxis.length() > 0) && (minorAxis.length() > 0)) {
                if (majorAxis.dot(minorAxis) != 0) {
                    this.origin = origin;
                    this.majorAxis = majorAxis;
                    this.minorAxis = minorAxis;
                } else {
                    throw new IllegalArgumentException(
                        "Axis must be orthogonal.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Ellipse constructor needs non zero axis.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Ellipse constructor doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point3f getOrigin() {
        return origin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin DOCUMENT ME!
     */
    public void setOrigin(Point3f origin) {
        if (origin != null) {
            this.origin = origin;
        } else {
            throw new IllegalArgumentException("You can't set a null origin.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMinorAxisLength() {
        return minorAxis.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3f getMinorAxis() {
        return minorAxis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param minorAxis DOCUMENT ME!
     */
    public void setMinorAxis(Vector3f minorAxis) {
        if (minorAxis != null) {
            if (minorAxis.length() > 0) {
                if (majorAxis.dot(minorAxis) != 0) {
                    this.minorAxis = minorAxis;
                } else {
                    throw new IllegalArgumentException(
                        "Axis must be orthogonal.");
                }
            } else {
                throw new IllegalArgumentException("You can't set a zero axis.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null minor axis.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getMajorAxisLength() {
        return majorAxis.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3f getMajorAxis() {
        return majorAxis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param majorAxis DOCUMENT ME!
     */
    public void setMajorAxis(Vector3f majorAxis) {
        if (majorAxis != null) {
            if (majorAxis.length() > 0) {
                if (majorAxis.dot(minorAxis) != 0) {
                    this.majorAxis = majorAxis;
                } else {
                    throw new IllegalArgumentException(
                        "Axis must be orthogonal.");
                }
            } else {
                throw new IllegalArgumentException("You can't set a zero axis.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null major axis.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getArea() {
        return Math.PI * getMinorAxisLength() * getMajorAxisLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getLinearEccentricity() {
        return Math.sqrt(Math.abs(getMinorAxisLength() * getMinorAxisLength()) -
            (getMajorAxisLength() * getMajorAxisLength()));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAspectRatio() {
        if ((getMinorAxisLength() / getMajorAxisLength()) > 1) {
            return getMajorAxisLength() / getMinorAxisLength();
        } else {
            return getMinorAxisLength() / getMajorAxisLength();
        }
    }

    // E
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEccentricity() {
        if ((getMinorAxisLength() > getMajorAxisLength())) {
            return Math.sqrt(((getMinorAxisLength() * getMinorAxisLength()) -
                (getMajorAxisLength() * getMajorAxisLength())) / (getMinorAxisLength() * getMinorAxisLength()));
        } else {
            return Math.sqrt(((getMajorAxisLength() * getMajorAxisLength()) -
                (getMinorAxisLength() * getMinorAxisLength())) / (getMajorAxisLength() * getMajorAxisLength()));
        }
    }

    //E'
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEccentricityPrime() {
        if ((getMinorAxisLength() > getMajorAxisLength())) {
            return Math.sqrt(((getMinorAxisLength() * getMinorAxisLength()) -
                (getMajorAxisLength() * getMajorAxisLength())) / (getMajorAxisLength() * getMajorAxisLength()));
        } else {
            return Math.sqrt(((getMajorAxisLength() * getMajorAxisLength()) -
                (getMinorAxisLength() * getMinorAxisLength())) / (getMinorAxisLength() * getMinorAxisLength()));
        }
    }

    //E''
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEccentricitySecond() {
        if ((getMinorAxisLength() > getMajorAxisLength())) {
            return Math.sqrt(((getMinorAxisLength() * getMinorAxisLength()) -
                (getMajorAxisLength() * getMajorAxisLength())) / ((getMinorAxisLength() * getMinorAxisLength()) +
                (getMajorAxisLength() * getMajorAxisLength())));
        } else {
            return Math.sqrt(((getMajorAxisLength() * getMajorAxisLength()) -
                (getMinorAxisLength() * getMinorAxisLength())) / ((getMinorAxisLength() * getMinorAxisLength()) +
                (getMajorAxisLength() * getMajorAxisLength())));
        }
    }

    //f
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getFlattening() {
        if ((getMinorAxisLength() > getMajorAxisLength())) {
            return (getMinorAxisLength() - getMajorAxisLength()) / getMinorAxisLength();
        } else {
            return (getMajorAxisLength() - getMinorAxisLength()) / getMajorAxisLength();
        }
    }

    //f'
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getFlatteningPrime() {
        if ((getMinorAxisLength() > getMajorAxisLength())) {
            return (getMinorAxisLength() - getMajorAxisLength()) / (getMinorAxisLength() +
            getMajorAxisLength());
        } else {
            return (getMajorAxisLength() - getMinorAxisLength()) / (getMinorAxisLength() +
            getMajorAxisLength());
        }
    }

    //minimum 4 points
    /**
     * DOCUMENT ME!
     *
     * @param numPoints DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point3f[] getOrbit(int numPoints) {
        if (numPoints > 3) {
            Point3f[] result;
            double t;
            float x;
            float y;
            float z;

            result = new Point3f[numPoints];

            //start by calculating values in the OXY plane
            //compute rotation vector
            //transform every point from the ellipse into the new system OXYZ
            Vector3D u1 = new LiteralVector3D(getMajorAxisLength(), 0, 0);
            Vector3D u2 = new LiteralVector3D(0, getMinorAxisLength(), 0);
            Vector3D v1 = new LiteralVector3D(getMajorAxis().x,
                    getMajorAxis().y, getMajorAxis().z);
            Vector3D v2 = new LiteralVector3D(getMinorAxis().x,
                    getMinorAxis().y, getMinorAxis().z);
            Rotation3D rotation3D = new Rotation3D(u1, u2, v1, v2);

            for (int i = 0; i < numPoints; i++) {
                t = ((double) (i) / numPoints) * (2 * Math.PI);
                x = (float) (getMajorAxisLength() * Math.cos(t));
                y = (float) (getMinorAxisLength() * Math.sin(t));
                z = 0;

                Vector3D value = rotation3D.applyTo(new LiteralVector3D(x, y, z));
                result[i] = new Point3f((float) (value.x() + getOrigin().x),
                        (float) (value.y() + getOrigin().y),
                        (float) (value.z() + getOrigin().z));
            }

            return result;
        } else {
            throw new IllegalArgumentException(
                "You can't get the orbit of an ellipse in less than 4 points.");
        }
    }
}
