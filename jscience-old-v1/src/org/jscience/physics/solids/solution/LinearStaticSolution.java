/*
* LinearStaticSolution.java
*
* Created on December 31, 2004, 1:52 AM
*/
package org.jscience.physics.solids.solution;

import org.jdom.Element;

import org.jscience.mathematics.algebraic.matrices.DoubleMatrix;

import org.jscience.physics.solids.*;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Linear static finite element solution.
 *
 * @author Wegge
 */
public class LinearStaticSolution extends AtlasSolution {
    //static Logger AtlasLogger = Logger.getLogger((LinearStaticSolution.class).getName());
    /**
     * DOCUMENT ME!
     */
    public static String TYPE = "Linear Static Solution";

    /**
     * DOCUMENT ME!
     */
    DecimalFormat efmt = new DecimalFormat("0.0000E0");

/**
     * Creates a new instance of LinearStaticSolution
     */
    public LinearStaticSolution(String id) {
        setId(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InvalidSolutionException DOCUMENT ME!
     */
    public void solveModel() throws InvalidSolutionException {
        try {
            //Start time
            long start = System.currentTimeMillis();
            dumpTime(start, "Begin Problem Formulation.");

            //Get solution matrices for the parent model
            // This builds all the matrices needed for problem solution
            // at this particular state of the problem
            //
            SolutionMatrices m = this.getParentModel().getSolutionMatrices();
            dumpTime(start, "Built All Matrices.");

            // Partition all solution matrices
            m.partitionAll();
            dumpTime(start, "Partitioned Out Constrained DOFs.");

            //Remove any singularities
            //m.removeSingularities();
            dumpTime(start, "Removed Singularities.");

            DoubleMatrix K = m.getReducedStiffnessMatrix();

            //AtlasLogger.debug("Reduced Stiffness Array");
            //AtlasLogger.debug(K.toString(efmt,12));
            //K.print(efmt,12);
            DoubleMatrix R = m.getReducedForceMatrix();

            //AtlasLogger.debug("Force Array");
            //AtlasLogger.debug(R.toString(efmt,12));
            DoubleMatrix U = new LUDecomposition(K).solve(R);
            m.setDisplacements(U);

            dumpTime(start, "Decomposition and Back Substitution Complete.");

            //AtlasLogger.info(" Global Displacements: ");
            //AtlasLogger.info(U.toString(efmt, 12));

            //Compute element results...
            AtlasElement[] elems = this.getParentModel().getElements();
            Arrays.sort(elems);

            for (int i = 0; i < elems.length; i++) {
                elems[i].computeResults(m);

                ArrayList elemRes = elems[i].getResults();

                //AtlasLogger.info(" Results for Element: " + elems[i].getId());
                //for (int j=0; j<elemRes.size(); j++){
                //AtlasLogger.info(((AtlasResult)elemRes.get(j)).toString());
                //}
            }

            dumpTime(start, "Solution Complete.");
        } catch (AtlasException err) {
            //AtlasLogger.error("FATAL: " + err.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = getType() + " : " + getId();

        return ret;
    }

    /**
     * Marshalls object to XML.
     *
     * @return DOCUMENT ME!
     */
    public Element loadJDOMElement() {
        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {
        String id = e.getAttributeValue("Id");

        return new LinearStaticSolution(id);
    }
}
