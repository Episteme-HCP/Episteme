package org.jscience.engineering;

/**
 * The Connector interface is the base class for connecting parts of a
 * mechanism (inputs, outputs). There is a close relation with graphs.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Connector {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Mechanism getInputMechanism(); //should return null for a source input

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Mechanism getOutputMechanism(); //should return null for a pit output

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(double time); //the value at t, returns null if unknown (although you should try to provide a value when the connecotr is at rest)

    //corresponding setters are highly recommended for this interface although optional
}
