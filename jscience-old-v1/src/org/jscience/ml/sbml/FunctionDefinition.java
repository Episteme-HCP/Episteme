package org.jscience.ml.sbml;

import org.jscience.ml.sbml.math.MathMLConvertor;

/**
 * An association between an identifier and a mathematical function.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public class FunctionDefinition extends SBaseId implements MathElement {
    private String math;

    public FunctionDefinition(String id, String name) {
        super(id, name);
    }

    public FunctionDefinition() {
        super();
    }

    public String getMath() {
        return math;
    }

    public void setMath(String math) {
        assert math.startsWith("<math:math>");
        this.math = math;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<functionDefinition id=\"" + id + "\" ");
        if (name != null)
            s.append("name=\"" + name + "\"");
        s.append(">\n");
        s.append(super.toString());
        s.append(math);
        s.append("</functionDefinition>\n");
        MathMLConvertor.insertFunction(id);
        return s.toString();
    }
}
