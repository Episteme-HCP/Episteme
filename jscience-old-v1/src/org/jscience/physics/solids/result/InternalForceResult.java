/*
 * Created on Jun 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids.result;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class InternalForceResult {

    private double[] xsForce = null;
    private double[] xsMoment = null;

    public InternalForceResult() {
    }

    public InternalForceResult(double[] f, double[] m) {
        this.xsForce = f;
        this.xsMoment = m;
    }

    /**
     * @return Returns the xsForce.
     */
    public double[] getXsForce() {
        return xsForce;
    }

    /**
     * @param xsForce The xsForce to set.
     */
    public void setXsForce(double[] xsForce) {
        this.xsForce = xsForce;
    }

    /**
     * @return Returns the xsMoment.
     */
    public double[] getXsMoment() {
        return xsMoment;
    }

    /**
     * @param xsMoment The xsMoment to set.
     */
    public void setXsMoment(double[] xsMoment) {
        this.xsMoment = xsMoment;
    }
}
