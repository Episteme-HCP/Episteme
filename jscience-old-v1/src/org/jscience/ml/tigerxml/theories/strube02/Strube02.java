/*
 * Strube02.java
 *
 * Created on October 15, 2003, 4:40 AM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
