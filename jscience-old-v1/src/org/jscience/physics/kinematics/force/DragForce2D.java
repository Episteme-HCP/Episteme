package org.jscience.physics.kinematics.force;

/**
 * Implements a viscosity/drag force to help stabilize items.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class DragForce2D extends AbstractForce {

    private static String[] pnames = new String[] { "DragCoefficient" };
    
    public static final float DEFAULT_DRAG_COEFF = 0.01f;
    public static final float DEFAULT_MIN_DRAG_COEFF = 0.0f;
    public static final float DEFAULT_MAX_DRAG_COEFF = 0.1f;
    public static final int DRAG_COEFF = 0;

    /**
     * Create a new DragForce.
     * @param dragCoeff the drag co-efficient
     */
    public DragForce2D(float dragCoeff) {
        params = new float[] { dragCoeff };
        minValues = new float[] { DEFAULT_MIN_DRAG_COEFF };
        maxValues = new float[] { DEFAULT_MAX_DRAG_COEFF };
    }

    /**
     * Create a new DragForce with default drag co-efficient.
     */
    public DragForce2D() {
        this(DEFAULT_DRAG_COEFF);
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
        item.force[0] -= params[DRAG_COEFF]*item.velocity[0];
        item.force[1] -= params[DRAG_COEFF]*item.velocity[1];
    }

} // end of class DragForce
