/*
 * Observer.java
 *
 * Created on 6 of agosto of 2001, 16:16
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

/**
 * DOCUMENT ME!
 *
 * @author XuGa2000
 */
public interface Observer {
    /**
     * DOCUMENT ME!
     */
    public void close();

    /**
     * DOCUMENT ME!
     *
     * @param inst DOCUMENT ME!
     * @param vx DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void evaluate(double inst, double[][] vx, double[] p);
}
