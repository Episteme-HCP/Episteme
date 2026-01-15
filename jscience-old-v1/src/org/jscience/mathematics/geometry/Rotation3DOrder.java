package org.jscience.mathematics.geometry;

/**
 * This class is a utility representing a rotation order specification
 * for Cardan or Euler angles specification.
 * <p/>
 * This class cannot be instanciated by the user. He can only use one
 * of the twelve predefined supported orders as an argument to either
 * the {@link Rotation#Rotation(Rotation3DOrder,double,double,double)}
 * constructor or the {@link Rotation#getAngles} method.
 *
 * @author L. Maisonobe
 * @version $Id: Rotation3DOrder.java,v 1.2 2007-10-21 17:38:26 virtualcall Exp $
 */
public final class Rotation3DOrder {

    /**
     * Private constructor.
     * This is a utility class that cannot be instantiated by the user,
     * so its only constructor is private.
     *
     * @param name name of the rotation order
     */
    private Rotation3DOrder(String name) {
        this.name = name;
    }

    /**
     * Get a string representation of the instance.
     *
     * @return a string representation of the instance (in fact, its name)
     */
    public String toString() {
        return name;
    }

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around X, then around Y, then
     * around Z
     */
    public static final Rotation3DOrder XYZ = new Rotation3DOrder("XYZ");

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around X, then around Z, then
     * around Y
     */
    public static final Rotation3DOrder XZY = new Rotation3DOrder("XZY");

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around Y, then around X, then
     * around Z
     */
    public static final Rotation3DOrder YXZ = new Rotation3DOrder("YXZ");

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around Y, then around Z, then
     * around X
     */
    public static final Rotation3DOrder YZX = new Rotation3DOrder("YZX");

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around Z, then around X, then
     * around Y
     */
    public static final Rotation3DOrder ZXY = new Rotation3DOrder("ZXY");

    /**
     * Set of Cardan angles.
     * this ordered set of rotations is around Z, then around Y, then
     * around X
     */
    public static final Rotation3DOrder ZYX = new Rotation3DOrder("ZYX");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around X, then around Y, then
     * around X
     */
    public static final Rotation3DOrder XYX = new Rotation3DOrder("XYX");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around X, then around Z, then
     * around X
     */
    public static final Rotation3DOrder XZX = new Rotation3DOrder("XZX");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around Y, then around X, then
     * around Y
     */
    public static final Rotation3DOrder YXY = new Rotation3DOrder("YXY");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around Y, then around Z, then
     * around Y
     */
    public static final Rotation3DOrder YZY = new Rotation3DOrder("YZY");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around Z, then around X, then
     * around Z
     */
    public static final Rotation3DOrder ZXZ = new Rotation3DOrder("ZXZ");

    /**
     * Set of Euler angles.
     * this ordered set of rotations is around Z, then around Y, then
     * around Z
     */
    public static final Rotation3DOrder ZYZ = new Rotation3DOrder("ZYZ");

    /**
     * Name of the rotations order.
     */
    private final String name;

}
