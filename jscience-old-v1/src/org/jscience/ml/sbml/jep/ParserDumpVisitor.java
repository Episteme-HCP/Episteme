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
