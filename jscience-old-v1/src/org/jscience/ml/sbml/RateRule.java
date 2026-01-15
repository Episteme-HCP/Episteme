package org.jscience.ml.sbml;

/**
 * A rule that expresses equations which set the rate of change of variables.
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Nicholas Allen
 */

public final class RateRule extends Rule {
    private String variable;

    public RateRule() {
        super();
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<rateRule variable=\"" + variable + "\">\n");
        s.append(super.toString());
        s.append(getMath());
        s.append("</rateRule>\n");
        return s.toString();
    }
}
