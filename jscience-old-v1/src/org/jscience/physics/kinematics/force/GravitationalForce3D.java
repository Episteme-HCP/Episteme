package org.jscience.physics.kinematics.force;

/**
 * Represents a constant gravitational force, like the pull of gravity
 * for an object on the Earth (F = mg). The force experienced by a
 * given item is calculated as the product of a GravitationalConstant 
 * parameter and the mass of the item.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class GravitationalForce3D extends AbstractForce {

    private static final String[] pnames
        = { "GravitationalConstant", "Major Direction", "Minor Direction" };
    
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int MAJOR_DIRECTION = 1;
    public static final int MINOR_DIRECTION = 2;

    public static final float DEFAULT_FORCE_CONSTANT = 1E-4f;
    public static final float DEFAULT_MIN_FORCE_CONSTANT = 1E-5f;
    public static final float DEFAULT_MAX_FORCE_CONSTANT = 1E-3f;
    
    public static final float DEFAULT_MAJOR_DIRECTION = (float)-Math.PI/2;
    public static final float DEFAULT_MAJOR_MIN_DIRECTION = (float)-Math.PI;
    public static final float DEFAULT_MAJOR_MAX_DIRECTION = (float)Math.PI;

    public static final float DEFAULT_MINOR_DIRECTION = (float)-Math.PI/2;
    public static final float DEFAULT_MINOR_MIN_DIRECTION = (float)-Math.PI;
    public static final float DEFAULT_MINOR_MAX_DIRECTION = (float)Math.PI;

    /**
     * Create a new GravitationForce.
     * @param forceConstant the gravitational constant to use
     * @param majorDirection the direction in which gravity should act,
     * @param minorDirection the direction in which gravity should act,
       * in radians.
     */
    public GravitationalForce3D(float forceConstant, float majorDirection, float minorDirection) {
        params = new float[] { forceConstant, majorDirection, minorDirection };
        minValues = new float[] 
            { DEFAULT_MIN_FORCE_CONSTANT, DEFAULT_MAJOR_MIN_DIRECTION, DEFAULT_MINOR_MIN_DIRECTION };
        maxValues = new float[] 
            { DEFAULT_MAX_FORCE_CONSTANT, DEFAULT_MAJOR_MAX_DIRECTION , DEFAULT_MINOR_MAX_DIRECTION };
    }
    
    /**
     * Create a new GravitationalForce with default gravitational
     * constant and direction.
     */
    public GravitationalForce3D() {
        this(DEFAULT_FORCE_CONSTANT, DEFAULT_MAJOR_DIRECTION, DEFAULT_MINOR_DIRECTION);
    }
    
    /**
     * Returns true.
     * @see org.jscience.physics.kinematics.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return true;
    }

    /**
     * @see org.jscience.physics.kinematics.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#getForce(org.jscience.physics.kinematics.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        float theta = params[MAJOR_DIRECTION];
        float alpha = params[MINOR_DIRECTION];
        float coeff = params[GRAVITATIONAL_CONST]*item.mass;
        
        item.force[0] += Math.cos(theta)*Math.cos(alpha)*coeff;
        item.force[1] += Math.sin(theta)*Math.cos(alpha)*coeff;
        item.force[2] += Math.sin(alpha)*coeff;

    }

} // end of class GravitationalForce
