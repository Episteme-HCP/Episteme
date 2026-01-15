package org.jscience.chemistry.gui.extended.geometry;

/**
 * A geometric vector class that provides vector computations: length,
 * normalize, dot and cross. note: Vector is a coordinate independent concept.
 * Translating a vector in 3D (or 2D) space yields the same vector.
 *
 * @author: Zhidong Xie (zxie
 * @see Point3D
 */
public class GeoVector3D {
    /**
     * X component of the vector
     */
    protected double x = 0.0;

    /**
     * Y component of the vector
     */
    protected double y = 0.0;

    /**
     * Z component of the vector
     */
    protected double z = 0.0;

    /**
     * Default constructor, all component are 0.0
     */
    public GeoVector3D() {
    }

    /**
     * Full constructor
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public GeoVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * constructor
     *
     * @param p1 start point of the vector
     * @param p2 end   point of the vector
     */
    public GeoVector3D(Point3D p1, Point3D p2) {
        x = p2.getX() - p1.getX();
        y = p2.getY() - p1.getY();
        z = p2.getZ() - p1.getZ();
    }

    /**
     * copy constructor
     *
     * @param gv the vector to be copied
     */
    public GeoVector3D(GeoVector3D gv) {
        x = gv.x;
        y = gv.y;
        z = gv.z;
    }

    /**
     * calculate the length of the vector
     *
     * @return DOCUMENT ME!
     */
    public double length() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * change the vector's length so that it becomes a unit vector
     */
    public void normalize() {
        double len = length();

        if (len > Double.MIN_VALUE) {
            x /= len;
            y /= len;
            z /= len;
        } else {
            x = 0.0;
            y = 0.0;
            z = 0.0;
        }
    }

    /**
     * cross product with another vector
     *
     * @param gv the other vector to cross
     * @return (GeoVector3D) this cross gv
     */
    public GeoVector3D cross(GeoVector3D gv) {
        double iComponent = (y * gv.z) - (gv.y * z);
        double jComponent = (z * gv.x) - (gv.z * x);
        double kComponent = (x * gv.y) - (gv.x * y);

        return new GeoVector3D(iComponent, jComponent, kComponent);
    }

    /**
     * dot product with another vector
     *
     * @param gv the other vector to dot
     * @return the (double) this dot gv
     */
    public double dot(GeoVector3D gv) {
        return ((x * gv.x) + (y * gv.y) + (z * gv.z));
    }

    /**
     * scale the vector by a factor
     *
     * @param factor the scaling factor
     */
    public void scale(double factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    /**
     * rotat the vector by a factor
     *
     * @param gv the scaling factor public void scale( double factor ){ x =
     *           factor; y = factor; }
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     *           <p/>
     *           add a vector to this vector
     * @param gv the other vector to add
     */
    /**
     * add a vector to this vector
     *
     * @param gv the other vector to add
     */
    public void add(GeoVector3D gv) {
        x += gv.x;
        y += gv.y;
        z += gv.z;
    }

    /**
     * subtract a vector from this vector
     *
     * @param gv the other vector to subtract
     */
    public void minus(GeoVector3D gv) {
        x -= gv.x;
        y -= gv.y;
        z -= gv.z;
    }

    /**
     * Return component x
     *
     * @return DOCUMENT ME!
     */
    public double getX() {
        return x;
    }

    /**
     * Return component y
     *
     * @return DOCUMENT ME!
     */
    public double getY() {
        return y;
    }

    /**
     * Return component z
     *
     * @return DOCUMENT ME!
     */
    public double getZ() {
        return z;
    }

    /**
     * Set component x
     *
     * @param x x component
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set component y
     *
     * @param y y component
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set component z
     *
     * @param z z component
     */
    public void setZ(double z) {
        this.z = z;
    }
} // end of GeoVector3D class
