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

package org.jscience.ml.tigerxml.theories.strube02;

import org.jscience.ml.tigerxml.GraphNode;

/**
 * Provides methods for automatically extracting markables and their attribute
 * values as specified in Strube 2002 from the TIGER corpus.
 * <p/>
 * The annotation scheme is described in detail in this article:
 * <p/>
 * Michael Strube, Stefan Rapp & Christoph M&uuml;ller (2002).<br>
 * The influence of minimum edit distance on reference resolution.<br>
 * In <i>Proceedings of the 2002 Conference on Empirical Methods in
 * Natural Language Processing</i>, Philadelphia, Pa., 6-7 July 2002,
 * pp. 312-319.
 * <p/>
 * So far, this is just the template of the class.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84
 *          $Id: Strube02.java,v 1.2 2007-10-21 17:40:33 virtualcall Exp $
 * @see org.jscience.ml.tigerxml.converters.Tiger2MMAX
 */
public class Strube02 {

//   hier weiter machen:
//   np_form bestimmen (anhand der CAT) (=NP, CNP, ...)
//   bestimmtheit bestimmen (anhand des ersten POS in der NP)
//   gram_role bestimmen (kanten =SB oder OA, oder O...)
//   person, numerus, etc --> org.jscience.ml.tigerxml.tools.IndexFeatures

    /**
     * Returns the NP form of a given <code>GraphNode</code>. Possible return
     * values are:<ul>
     * <li>"none"
     * <li>"ne"
     * <li>"defnp"
     * <li>"indefnp"
     * <li>"pper"
     * <li>"ppos"
     * <li>"pds"
     * </ul>
     *
     * @param gn The <code>GraphNode</code> to classify.
     * @return A string denoting the NP form of the given <code>GraphNode</code>.
     */
    public static String getNpForm(GraphNode gn) {
        // Implement me!
        return "none";
    }

    /**
     * Comment me!
     *
     * @param gn
     * @return none
     */
    public static String getGramRole(GraphNode gn) {
        // Implement me!
        return "none";
    }

    /**
     * Comment me!
     *
     * @param gn
     * @return none
     */
    public static String getAgreement(GraphNode gn) {
        // Implement me!
        return "none";
    }

}
