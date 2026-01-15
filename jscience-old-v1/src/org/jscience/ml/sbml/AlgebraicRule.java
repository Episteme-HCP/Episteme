package org.jscience.ml.sbml;

/**
 * A rule that expresses equations with left-hand side of zero.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public final class AlgebraicRule extends Rule {

    public AlgebraicRule() {
        super();
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<algebraicRule>\n");
        s.append(super.toString());
        s.append(getMath());
        s.append("</algebraicRule>\n");
        return s.toString();
    }
}
