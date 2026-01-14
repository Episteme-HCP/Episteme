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

package org.jscience.ml.sbml.jep;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ParserDumpVisitor implements ParserVisitor {
    /** DOCUMENT ME! */
    private int indent = 0;

    /** DOCUMENT ME! */
    private StringBuffer sbmlString = new StringBuffer();

/**
     * Creates a new ParserDumpVisitor object.
     */
    public ParserDumpVisitor() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSBMLString() {
        return sbmlString.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(SimpleNode node, Object data) {
        System.out.println(indentString() + node +
            ": acceptor not unimplemented in subclass?");
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTPiecewise node, Object data) {
        sbmlString.append(indentString() + "<math:piecewise>\n");
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;
        sbmlString.append(indentString() + "</math:piecewise>\n");

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTStart node, Object data) {
        System.out.println(indentString() + node);
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTFunNode node, Object data) {
        if (JEP.isMathMLFunction(node.toString())) {
            sbmlString.append(indentString() + "<math:apply>\n" +
                indentString() + " <math:" + node + "/>\n");
        } else if (node.toString().trim().equals("sqrt")) {
            sbmlString.append(indentString() + "<math:apply>\n" +
                indentString() + " <math:" + "root" + "/>\n" + indentString() +
                "<math:degree><math:ci>2</math:ci></math:degree>\n");
        } else {
            sbmlString.append(indentString() + "<math:apply>\n" +
                indentString() + "<math:ci>" + node + "</math:ci>\n");
        }

        ++indent;
        data = node.childrenAccept(this, data);
        --indent;
        sbmlString.append(indentString() + "</math:apply>\n");

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTVarNode node, Object data) {
        sbmlString.append(indentString() + "<math:ci>" + node + "</math:ci>\n");
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTConstant node, Object data) {
        sbmlString.append(indentString() + "<math:cn>" + node + "</math:cn>\n");
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTIfThen node, Object data) {
        sbmlString.append(indentString() + "<math:piece>\n");
        ++indent;
        data = node.childrenAcceptReverse(this, data);
        --indent;
        sbmlString.append(indentString() + "</math:piece>\n");

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTElseIfThen node, Object data) {
        sbmlString.append(indentString() + "<math:piece>\n");
        ++indent;
        data = node.childrenAcceptReverse(this, data);
        --indent;
        sbmlString.append(indentString() + "</math:piece>\n");

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object visit(ASTElse node, Object data) {
        sbmlString.append(indentString() + "<math:otherwise>\n");
        ++indent;
        data = node.childrenAccept(this, data);
        --indent;
        sbmlString.append(indentString() + "</math:otherwise>\n");

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String indentString() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < indent; ++i)
            sb.append("  ");

        return sb.toString();
    }
}
