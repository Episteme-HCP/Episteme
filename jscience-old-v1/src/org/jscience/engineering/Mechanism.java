package org.jscience.engineering;

import java.util.Set;


/**
 * The Mechanism interface is the base class to describe complex physical
 * objects. A watch, a microprocessor are good candidates. There is a close
 * relation with graphs.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Mechanism {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getInputs(); //should contain a non empty set of Connectors

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOutputs(); //should contain a non empty set of Connectors

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParts(); //should contain a non empty set of Mechanisms

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFinal(); //returns true if there is only one mechanism

    /**
     * DOCUMENT ME!
     *
     * @param startTime DOCUMENT ME!
     * @param endTime DOCUMENT ME!
     * @param timeStep DOCUMENT ME!
     */
    public void process(double startTime, double endTime, double timeStep); //given actual values of inputs, tries to process output values

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeasible(); //if there is a way to actually build this mechanism or if this cannot be built (for exemple a motor violating the second thermodynamic principle)
}
