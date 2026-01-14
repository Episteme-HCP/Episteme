/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
