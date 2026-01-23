package org.jscience.io.mathml;

import org.apache.xerces.parsers.DOMParser;

import org.jscience.mathematics.linearalgebra.Matrix;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import java.util.ArrayList;
import java.util.List;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;


/**
 * The MathMLParser class will parse a MathML document into org.jscience
 * objects.
 *
 * @author Mark Hale
 * @version 0.8
 */
public final class MathMLParser extends DOMParser {
/**
     * Constructs a MathMLParser.
     */
    public MathMLParser() {
        try {
            setDocumentClassName("org.jscience.mathematics.loaders.mathml.MathMLDocumentImpl");
        } catch (Exception e) {
        }
    }

    /**
     * Translates the document into org.jscience objects.
     *
     * @return DOCUMENT ME!
     */
    public Object[] translateToJScienceObjects() {
        Translator translator = new JScienceObjectTranslator();

        return translator.translate(getDocument().getDocumentElement());
    }

    /**
     * Translates the document into org.jscience code.
     *
     * @return DOCUMENT ME!
     */
    public Object[] translateToJscienceCode() {
        Translator translator = new JscienceCodeTranslator();

        return translator.translate(getDocument().getDocumentElement());
    }

    /**
     * Translator.
     */
    abstract class Translator extends Object {
/**
         * Creates a new Translator object.
         */
        public Translator() {
        }

        /**
         * DOCUMENT ME!
         *
         * @param root DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object[] translate(Node root) {
            return parseMATH(root);
        }

        /**
         * Parses the &lt;math&gt; node.
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Object[] parseMATH(Node n) {
            int len = 0;
            final NodeList nl = n.getChildNodes();
            final Object[] objList = new Object[nl.getLength()];
            Object obj;

            for (int i = 0; i < objList.length; i++) {
                obj = processNode(nl.item(i));

                if (obj != null) {
                    objList[len] = obj;
                    len++;
                }
            }

            final Object[] parseList = new Object[len];
            System.arraycopy(objList, 0, parseList, 0, len);

            return parseList;
        }

        /**
         * Processes a node.
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected Object processNode(Node n) {
            final String nodeName = n.getNodeName();

            if (nodeName.equals("apply") || nodeName.equals("reln")) {
                return parseAPPLY(n);
            } else if (nodeName.equals("cn")) {
                return parseCN(n);
            } else if (nodeName.equals("ci")) {
                return parseCI(n);
            } else if (nodeName.equals("vector")) {
                return parseVECTOR(n);
            } else if (nodeName.equals("matrix")) {
                return parseMATRIX(n);
            } else if (nodeName.equals("set")) {
                return parseSET(n);
            } else if (nodeName.equals("ms")) {
                return parseMS(n);
            } else if (nodeName.equals("mtext")) {
                return parseMTEXT(n);
            } else {
                return null;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseAPPLY(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseCN(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseCI(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseVECTOR(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseMATRIX(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseSET(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseMS(Node n);

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected abstract Object parseMTEXT(Node n);
    }

    /**
     * org.jscience object translator.
     */
    class JScienceObjectTranslator extends Translator {
        /** DOCUMENT ME! */
        private final int INTEGER = 0;

        /** DOCUMENT ME! */
        private final int DOUBLE = 1;

        /** DOCUMENT ME! */
        private final int COMPLEX = 2;

        /** DOCUMENT ME! */
        private int setID = 1;

/**
         * Creates a new JScienceObjectTranslator object.
         */
        public JScienceObjectTranslator() {
        }

        /**
         * Parses &lt;apply&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return MathMLExpression.
         */
        protected Object parseAPPLY(Node n) {
            final MathMLExpression expr = new MathMLExpression();
            final NodeList nl = n.getChildNodes();
            Object obj;
            int i;

            for (i = 0; nl.item(i).getNodeType() == Node.TEXT_NODE; i++)
                ;

            expr.setOperation(nl.item(i).getNodeName());

            for (; i < nl.getLength(); i++) {
                obj = processNode(nl.item(i));

                if (obj != null) {
                    expr.addArgument(obj);
                }
            }

            return expr;
        }

        /**
         * Parses &lt;cn&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return Ring.Member.
         */
        protected Object parseCN(Node n) {
            return parseNumber(n);
        }

        /**
         * DOCUMENT ME!
         *
         * @param n DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Ring.Member parseNumber(Node n) {
            final NamedNodeMap attr = n.getAttributes();

            // support only base 10
            if (!attr.getNamedItem("base").getNodeValue().equals("10")) {
                return null;
            }

            // default type="real"
            if (attr.getNamedItem("type") == null) {
                return Real.of(n.getFirstChild().getNodeValue());
            }

            final String attrType = attr.getNamedItem("type").getNodeValue();

            if (attrType.equals("integer")) {
                return Integer.of(n.getFirstChild().getNodeValue());
            } else if (attrType.equals("real")) {
                return Real.of(n.getFirstChild().getNodeValue());
            } else if (attrType.equals("rational")) {
                final Node num = n.getFirstChild();
                final Node denom = num.getNextSibling().getNextSibling();

                return Real.of(num.getNodeValue()).divide(Real.of(
                        denom.getNodeValue()));
            } else if (attrType.equals("complex-cartesian")) {
                final Node re = n.getFirstChild();
                final Node im = re.getNextSibling().getNextSibling();

                return Complex.of(Real.of(re.getNodeValue()).doubleValue(),
                    Real.of(im.getNodeValue()).doubleValue());
            } else if (attrType.equals("complex-polar")) {
                final Node mod = n.getFirstChild();
                final Node arg = mod.getNextSibling().getNextSibling();

                return Complex.ofPolar(Real.of(mod.getNodeValue()).doubleValue(),
                    Real.of(arg.getNodeValue()).doubleValue());
            } else if (attrType.equals("constant")) {
                final String value = n.getFirstChild().getNodeValue();

                if (value.equals("&pi;")) {
                    return RealField.PI;
                } else if (value.equals("&ee;") ||
                        value.equals("&ExponentialE;")) {
                    return RealField.E;
                } else if (value.equals("&ii;") ||
                        value.equals("&ImaginaryI;")) {
                    return ComplexField.I;
                } else if (value.equals("&gamma;")) {
                    return RealField.GAMMA;
                } else if (value.equals("&infty;") || value.equals("&infin;")) {
                    return RealField.POSITIVE_INFINITY;
                } else if (value.equals("&NaN;") ||
                        value.equals("&NotANumber;")) {
                    return RealField.NaN;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * Parses &lt;ci&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseCI(Node n) {
            return n.getFirstChild().getNodeValue();
        }

        /**
         * Parses &lt;vector&gt; tags.
         *
         * @param vectorNode DOCUMENT ME!
         *
         * @return MathVector.
         */
        protected Object parseVECTOR(Node vectorNode) {
            int len = 0;
            int type = INTEGER;
            final NodeList nl = vectorNode.getChildNodes();
            final Ring.Member[] num = new Ring.Member[nl.getLength()];

            for (int i = 0; i < num.length; i++) {
                Node n = nl.item(i);

                if (n.getNodeName().equals("cn")) {
                    num[len] = parseNumber(n);

                    if (num[len] != null) {
                        // work out number format needed
                        if (num[len] instanceof Real && (type < DOUBLE)) {
                            type = DOUBLE;
                        } else if (num[len] instanceof Complex &&
                                (type < COMPLEX)) {
                            type = COMPLEX;
                        }

                        len++;
                    }
                }
            }

            // output to org.jscience objects
            if (type == INTEGER) {
                final List<Integer> list = new ArrayList<Integer>(len);
                for (int i = 0; i < len; i++) {
                     list.add((Integer) num[i]);
                }
                return DenseVector.of(list, Integer.ZERO.field());
            } else if (type == DOUBLE) {
                final double[] array = new double[len];

                for (int i = 0; i < array.length; i++) {
                    if (num[i] instanceof Integer) {
                        array[i] = ((Integer) num[i]).value();
                    } else {
                        array[i] = ((Real) num[i]).value();
                    }
                }

                return RealDoubleVector.of(array);
            } else {
                final List<Complex> list = new ArrayList<Complex>(len);

                for (int i = 0; i < len; i++) {
                    if (num[i] instanceof Integer) {
                        list.add(Complex.of(((Integer) num[i]).value(), 0.0));
                    } else if (num[i] instanceof Real) {
                        list.add(Complex.of(((Real) num[i]).value(), 0.0));
                    } else {
                        list.add((Complex) num[i]);
                    }
                }

                return DenseVector.valueOf(list);
            }
        }

        /**
         * Parses &lt;matrix&gt; tags.
         *
         * @param matrixNode DOCUMENT ME!
         *
         * @return Matrix.
         */
        protected Object parseMATRIX(Node matrixNode) {
            int rows = 0;
            int cols = Integer.MAX_VALUE;
            final NodeList nl = matrixNode.getChildNodes();
            final Ring.Member[][] num = new Ring.Member[nl.getLength()][];

            for (int i = 0; i < num.length; i++) {
                Node n = nl.item(i);

                if (n.getNodeName().equals("matrixrow")) {
                    num[rows] = parseMatrixRow(n);

                    if (num[rows].length < cols) {
                        cols = num[rows].length;
                    }

                    rows++;
                }
            }

            // work out number format needed
            int type = INTEGER;

            for (int j, i = 0; i < rows; i++) {
                for (j = 0; j < cols; j++) {
                    if (num[i][j] instanceof Real && (type < DOUBLE)) {
                        type = DOUBLE;
                    } else if (num[i][j] instanceof Complex &&
                            (type < COMPLEX)) {
                        type = COMPLEX;
                    }
                }
            }

            // output to org.jscience objects
            if (type == INTEGER) {
                // DenseMatrix<Integer> not directly from int[][]. Using Generic construction.
                // Simplified to return generic DenseMatrix
                List<DenseVector<Integer>> rowsList = new ArrayList<>();
                for (int i = 0; i < rows; i++) {
                     List<Integer> row = new ArrayList<>();
                     for(int j=0; j<cols; j++) row.add((Integer)num[i][j]);
                     rowsList.add(DenseVector.of(row, Integer.ZERO.field()));
                }
                return DenseMatrix.of(rowsList);
            } else if (type == DOUBLE) {
                final double[][] array = new double[rows][cols];

                for (int j, i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (num[i][j] instanceof Integer) {
                            array[i][j] = ((Integer) num[i][j]).value();
                        } else {
                            array[i][j] = ((Real) num[i][j]).value();
                        }
                    }
                }

                return RealDoubleMatrix.of(array);
            } else {
                List<DenseVector<Complex>> rowsList = new ArrayList<>();
                for (int i = 0; i < rows; i++) {
                    List<Complex> row = new ArrayList<>();
                    for (int j = 0; j < cols; j++) {
                        if (num[i][j] instanceof Integer) {
                            row.add(Complex.of(((Integer) num[i][j]).value(), 0.0));
                        } else if (num[i][j] instanceof Real) {
                            row.add(Complex.of(((Real) num[i][j]).value(), 0.0));
                        } else {
                            row.add((Complex) num[i][j]);
                        }
                    }
                    rowsList.add(DenseVector.valueOf(row));
                }
                return DenseMatrix.of(rowsList);
            }
        }

        /**
         * Parses &lt;matrixrow&gt; tags.
         *
         * @param rowNode DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        private Ring.Member[] parseMatrixRow(Node rowNode) {
            int len = 0;
            final NodeList nl = rowNode.getChildNodes();
            final Ring.Member[] num = new Ring.Member[nl.getLength()];

            for (int i = 0; i < num.length; i++) {
                Node n = nl.item(i);

                if (n.getNodeName().equals("cn")) {
                    num[len] = parseNumber(n);

                    if (num[len] != null) {
                        len++;
                    }
                }
            }

            final Ring.Member[] row = new Ring.Member[len];
            System.arraycopy(num, 0, row, 0, len);

            return row;
        }

        /**
         * Parses &lt;set&gt; tags.
         *
         * @param setNode DOCUMENT ME!
         *
         * @return FiniteSet.
         */
        protected Object parseSET(Node setNode) {
            final NodeList nl = setNode.getChildNodes();
            final Set elements = new HashSet(nl.getLength());

            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);

                if (n.getNodeName().equals("ci")) {
                    elements.add(parseCI(n));
                }
            }

            // output to org.jscience objects
            return new FiniteSet(elements);
        }

        /**
         * Parses &lt;ms&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseMS(Node n) {
            return n.getFirstChild().getNodeValue();
        }

        /**
         * Parses &lt;mtext&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseMTEXT(Node n) {
            return n.getFirstChild().getNodeValue();
        }
    }

    /**
     * org.jscience code translator.
     */
    class JscienceCodeTranslator extends Translator {
/**
         * Creates a new JscienceCodeTranslator object.
         */
        public JscienceCodeTranslator() {
        }

        /**
         * Parses &lt;apply&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseAPPLY(Node n) {
            final StringBuffer buf = new StringBuffer();
            final NodeList nl = n.getChildNodes();
            Object obj;
            int i;

            for (i = 0; nl.item(i).getNodeType() == Node.TEXT_NODE; i++)
                ;

            String op = nl.item(i).getNodeName();

            if (op.equals("plus")) {
                op = "add";
            } else if (op.equals("minus")) {
                op = "subtract";
            } else if (op.equals("times")) {
                op = "multiply";
            }

            boolean isFirst = true;

            for (; i < nl.getLength(); i++) {
                obj = processNode(nl.item(i));

                if (obj != null) {
                    if (isFirst) {
                        buf.append(obj);
                        isFirst = false;
                    } else {
                        buf.append('.').append(op).append('(').append(obj)
                           .append(')');
                    }
                }
            }

            return buf;
        }

        /**
         * Parses &lt;cn&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseCN(Node n) {
            final NamedNodeMap attr = n.getAttributes();

            // support only base 10
            if (!attr.getNamedItem("base").getNodeValue().equals("10")) {
                return null;
            }

            // default type="real"
            if (attr.getNamedItem("type") == null) {
                return "Real.of(" + n.getFirstChild().getNodeValue() + ')';
            }

            final String attrType = attr.getNamedItem("type").getNodeValue();

            if (attrType.equals("integer")) {
                return "Integer.of(" + n.getFirstChild().getNodeValue() + ')';
            } else if (attrType.equals("real")) {
                return "Real.of(" + n.getFirstChild().getNodeValue() + ')';
            } else if (attrType.equals("rational")) {
                final Node num = n.getFirstChild();
                final Node denom = num.getNextSibling().getNextSibling();

                return "Real.of(" + num.getNodeValue() + '/' +
                denom.getNodeValue() + ')';
            } else if (attrType.equals("complex-cartesian")) {
                final Node re = n.getFirstChild();
                final Node im = re.getNextSibling().getNextSibling();

                return "Complex.of(" + re.getNodeValue() + ',' +
                im.getNodeValue() + ')';
            } else if (attrType.equals("complex-polar")) {
                final Node mod = n.getFirstChild();
                final Node arg = mod.getNextSibling().getNextSibling();

                return "Complex.ofPolar(" + mod.getNodeValue() + ',' +
                arg.getNodeValue() + ')';
            } else if (attrType.equals("constant")) {
                final String value = n.getFirstChild().getNodeValue();

                if (value.equals("&pi;")) {
                    return "RealField.PI";
                } else if (value.equals("&ee;") ||
                        value.equals("&ExponentialE;")) {
                    return "RealField.E";
                } else if (value.equals("&ii;") ||
                        value.equals("&ImaginaryI;")) {
                    return "ComplexField.I";
                } else if (value.equals("&gamma;")) {
                    return "RealField.GAMMA";
                } else if (value.equals("&infty;") || value.equals("&infin;")) {
                    return "RealField.INFINITY";
                } else if (value.equals("&NaN;") ||
                        value.equals("&NotANumber;")) {
                    return "RealField.NaN";
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        /**
         * Parses &lt;ci&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseCI(Node n) {
            return n.getFirstChild().getNodeValue();
        }

        /**
         * Parses &lt;vector&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseVECTOR(Node n) {
            return null;
        }

        /**
         * Parses &lt;matrix&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseMATRIX(Node n) {
            return null;
        }

        /**
         * Parses &lt;set&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseSET(Node n) {
            return null;
        }

        /**
         * Parses &lt;ms&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseMS(Node n) {
            return n.getFirstChild().getNodeValue();
        }

        /**
         * Parses &lt;mtext&gt; tags.
         *
         * @param n DOCUMENT ME!
         *
         * @return String.
         */
        protected Object parseMTEXT(Node n) {
            return "/*\n" + n.getFirstChild().getNodeValue() + "\n*/";
        }
    }
}
