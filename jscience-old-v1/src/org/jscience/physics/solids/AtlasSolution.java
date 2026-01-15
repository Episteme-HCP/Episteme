/*
 * AtlasSolution.java
 *
 * Created on December 31, 2004, 1:25 AM
 */
package org.jscience.physics.solids;


//import org.apache.log4j.Logger;
/**
 * High level abstract class for a solution object.
 *
 * @author Wegge
 */
public abstract class AtlasSolution extends AtlasObject {
    //static Logger AtlasLogger = Logger.getLogger((AtlasSolution.class).getName());
    /**
     * DOCUMENT ME!
     *
     * @throws InvalidSolutionException DOCUMENT ME!
     */
    public abstract void solveModel() throws InvalidSolutionException;

    /**
     * Utility method that dumps the time needed for each step.
     *
     * @param start DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public void dumpTime(long start, String message) {
        double elapsed = (System.currentTimeMillis() - start) / 1000.;

        //AtlasLogger.info(elapsed + " : " + message);
    }
}
