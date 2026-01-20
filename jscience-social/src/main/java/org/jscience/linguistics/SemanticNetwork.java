package org.jscience.linguistics;

import org.jscience.computing.graph.graphs.DirectedMultigraph;


/**
 * The SemanticNetwork class provides a placeholder for the semantic
 * information that can be extracted from a text..
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class SemanticNetwork extends DirectedMultigraph {
    //A semantic network is often used as a form of knowledge representation. It is a directed graph consisting of vertices which represent concepts and edges which represent semantic relations between the concepts.
    //Semantic networks are a common type of machine-readable dictionary.
    //Important semantic relations:
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int MERONYMY = 1; // (A is part of B)

    /** DOCUMENT ME! */
    public final static int HOLONYMY = 2; // (B has A as a part of itself)

    /** DOCUMENT ME! */
    public final static int HYPONYMY = 4; //(or troponymy) (A is subordinate of B; A is kind of B)

    /** DOCUMENT ME! */
    public final static int HYPERNYMY = 8; // (A is superordinate of B)

    /** DOCUMENT ME! */
    public final static int SYNONYMY = 16; // (A denotes the same as B)

    /** DOCUMENT ME! */
    public final static int ANTONYMY = 32; // (A denotes the opposite of B)

    /** DOCUMENT ME! */
    public final static int OTHER = 64;
}
