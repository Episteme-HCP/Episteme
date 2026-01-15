package org.jscience.physics.kinematics.force;

/**
 * Abstract base class for force functions in a force simulation. This
 * skeletal version provides support for storing and retrieving float-valued
 * parameters of the force function. Subclasses should use the protected
 * field <code>params</code> to store parameter values.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public abstract class AbstractForce implements Force {

    protected float[] params;
    protected float[] minValues;
    protected float[] maxValues;

    /**
     * Initialize this force function. This default implementation does nothing.
     * Subclasses should override this method with any needed initialization.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        // do nothing.
    }

    /**
     * @see org.jscience.physics.kinematics.force.Force#getParameterCount()
     */
    public int getParameterCount() {
        return ( params == null ? 0 : params.length );
    }

    /**
     * @see org.jscience.physics.kinematics.force.Force#getParameter(int)
     */
    public float getParameter(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return params[i];
        }
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#getMinValue(int)
     */
    public float getMinValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return minValues[i];
        }
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#getMaxValue(int)
     */
    public float getMaxValue(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return maxValues[i];
        }
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#getParameterName(int)
     */
    public String getParameterName(int i) {
        String[] pnames = getParameterNames();
        if ( i < 0 || pnames == null || i >= pnames.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return pnames[i];
        }
    }

    /**
     * @see org.jscience.physics.kinematics.force.Force#setParameter(int, float)
     */
    public void setParameter(int i, float val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            params[i] = val;
        }
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#setMinValue(int, float)
     */
    public void setMinValue(int i, float val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            minValues[i] = val;
        }
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.Force#setMaxValue(int, float)
     */
    public void setMaxValue(int i, float val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            maxValues[i] = val;
        }
    }
    
    protected abstract String[] getParameterNames();
    
    /**
     * Returns false.
     * @see org.jscience.physics.kinematics.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return false;
    }
    
    /**
     * Returns false.
     * @see org.jscience.physics.kinematics.force.Force#isSpringForce()
     */
    public boolean isSpringForce() {
        return false;
    }
    
    /**
     * Throws an UnsupportedOperationException.
     * @see org.jscience.physics.kinematics.force.Force#getForce(org.jscience.physics.kinematics.force.ForceItem)
     */
    public void getForce(ForceItem item) {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
    /**
     * Throws an UnsupportedOperationException.
     * @see org.jscience.physics.kinematics.force.Force#getForce(org.jscience.physics.kinematics.force.Spring)
     */
    public void getForce(Spring spring) {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    }
    
} // end of abstract class AbstractForce
