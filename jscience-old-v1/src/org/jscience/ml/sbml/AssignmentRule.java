package org.jscience.ml.sbml;

/**
 * A rule that expresses equations which set the value of variables.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public final class AssignmentRule extends Rule {
    private String variable;

    public AssignmentRule() {
        super();
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<assignmentRule variable=\"" + variable + "\">\n");
        s.append(super.toString());
        s.append(getMath());
        s.append("</assignmentRule>\n");
        return s.toString();
    }
}
