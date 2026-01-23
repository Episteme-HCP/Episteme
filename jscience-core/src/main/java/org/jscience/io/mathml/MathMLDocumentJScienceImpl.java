package org.jscience.io.mathml;

import org.jscience.mathematics.FiniteSet;
import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Double;

import org.jscience.ml.mathml.MathMLDocumentImpl;

import org.w3c.dom.*;

import java.io.IOException;
import java.io.Writer;

import java.util.Iterator;


/**
 * The MathMLDocumentJscienceImpl class encapsulates an entire MathML
 * document.
 *
 * @author Mark Hale
 * @version 0.5
 */
public class MathMLDocumentJScienceImpl extends MathMLDocumentImpl {
/**
     * Constructs a MathML document.
     */
    public MathMLDocumentJScienceImpl() {
        super();
    }

    /**
     * Creates a MathML number element (<code>&lt;cn&gt;</code>).
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createNumber(double x) {
        final Element num = createElement("cn");
        num.appendChild(createTextNode(String.valueOf(x)));

        return num;
    }

    /**
     * Creates a MathML number element (<code>&lt;cn&gt;</code>).
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createNumber(int i) {
        final Element num = createElement("cn");
        num.setAttribute("type", "integer");
        num.appendChild(createTextNode(String.valueOf(i)));

        return num;
    }

    /**
     * Creates a MathML number element (<code>&lt;cn&gt;</code>).
     *
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createNumber(Complex z) {
        final Element num = createElement("cn");
        num.setAttribute("type", "complex-cartesian");
        num.appendChild(createTextNode(String.valueOf(z.real())));
        num.appendChild(createElement("sep"));
        num.appendChild(createTextNode(String.valueOf(z.imag())));

        return num;
    }

    /**
     * Creates a MathML variable element (<code>&lt;ci&gt;</code>).
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createVariable(Object obj) {
        final Element var = createElement("ci");
        var.appendChild(createTextNode(obj.toString()));

        return var;
    }

    /**
     * Creates a MathML variable element (<code>&lt;ci&gt;</code>).
     *
     * @param obj DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createVariable(Object obj, String type) {
        final Element var = createElement("ci");
        var.setAttribute("type", type);
        var.appendChild(createTextNode(obj.toString()));

        return var;
    }

    /**
     * Creates a MathML vector element (<code>&lt;vector&gt;</code>).
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createVector(DoubleVector v) {
        final Element vector = createElement("vector");

        for (int i = 0; i < v.getDimension(); i++)
            vector.appendChild(createNumber(v.getPrimitiveElement(i)));

        return vector;
    }

    /**
     * Creates a MathML vector element (<code>&lt;vector&gt;</code>).
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createVector(IntegerVector v) {
        final Element vector = createElement("vector");

        for (int i = 0; i < v.getDimension(); i++)
            vector.appendChild(createNumber(v.getPrimitiveElement(i)));

        return vector;
    }

    /**
     * Creates a MathML vector element (<code>&lt;vector&gt;</code>).
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createVector(ComplexVector v) {
        final Element vector = createElement("vector");

        for (int i = 0; i < v.getDimension(); i++)
            vector.appendChild(createNumber(v.getElement(i)));

        return vector;
    }

    /**
     * Creates a MathML matrix element (<code>&lt;matrix&gt;</code>).
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createMatrix(DoubleMatrix m) {
        final Element matrix = createElement("matrix");
        Element row;

        for (int j, i = 0; i < m.numRows(); i++) {
            row = createElement("matrixrow");

            for (j = 0; j < m.numColumns(); j++)
                row.appendChild(createNumber(m.getPrimitiveElement(i, j)));

            matrix.appendChild(row);
        }

        return matrix;
    }

    /**
     * Creates a MathML matrix element (<code>&lt;matrix&gt;</code>).
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createMatrix(IntegerMatrix m) {
        final Element matrix = createElement("matrix");
        Element row;

        for (int j, i = 0; i < m.numRows(); i++) {
            row = createElement("matrixrow");

            for (j = 0; j < m.numColumns(); j++)
                row.appendChild(createNumber(m.getPrimitiveElement(i, j)));

            matrix.appendChild(row);
        }

        return matrix;
    }

    /**
     * Creates a MathML matrix element (<code>&lt;matrix&gt;</code>).
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createMatrix(ComplexMatrix m) {
        final Element matrix = createElement("matrix");
        Element row;

        for (int j, i = 0; i < m.numRows(); i++) {
            row = createElement("matrixrow");

            for (j = 0; j < m.numColumns(); j++)
                row.appendChild(createNumber(m.getElement(i, j)));

            matrix.appendChild(row);
        }

        return matrix;
    }

    /**
     * Creates a MathML set element (<code>&lt;set&gt;</code>).
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createSet(FiniteSet s) {
        final Element set = createElement("set");
        Iterator iter = s.getElements().iterator();

        while (iter.hasNext())
            set.appendChild(createVariable(iter.next()));

        return set;
    }

    /**
     * Creates a MathML operator/function element
     * (<code>&lt;apply&gt;</code>).
     *
     * @param op a MathML tag name (<code>plus</code>, <code>minus</code>,
     *        <code>times</code>, <code>divide</code>, etc).
     * @param args DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element createApply(String op, DocumentFragment args) {
        final Element apply = createElement("apply");
        apply.appendChild(createElement(op));
        apply.appendChild(args);

        return apply;
    }

    /**
     * Creates a MathML operator/function element
     * (<code>&lt;apply&gt;</code>).
     *
     * @param expr a MathMLExpression object.
     *
     * @return DOCUMENT ME!
     */
    public Element createApply(MathMLExpression expr) {
        final Element apply = createElement("apply");
        apply.appendChild(createElement(expr.getOperation()));

        for (int i = 0; i < expr.length(); i++) {
            Object arg = expr.getArgument(i);

            if (arg instanceof MathMLExpression) {
                apply.appendChild(createApply((MathMLExpression) arg));
            } else if (arg instanceof Double) {
                apply.appendChild(createNumber(((Double) arg).value()));
            } else if (arg instanceof Complex) {
                apply.appendChild(createNumber((Complex) arg));
            } else if (arg instanceof DoubleVector) {
                apply.appendChild(createVector((DoubleVector) arg));
            } else if (arg instanceof DoubleMatrix) {
                apply.appendChild(createMatrix((DoubleMatrix) arg));
            } else {
                apply.appendChild(createVariable(arg));
            }
        }

        return apply;
    }

    /**
     * Prints this MathML document to a stream.
     *
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void print(Writer out) throws IOException {
        out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        out.write("<!DOCTYPE " + getDoctype().getName() + " PUBLIC \"" +
            getDoctype().getPublicId() + "\" \"" + getDoctype().getSystemId() +
            "\">\n");
        printNode(out, getDocumentElement());
        out.flush();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     * @param n DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void printNode(Writer out, Node n) throws IOException {
        if (n.hasChildNodes()) {
            out.write("<" + n.getNodeName());

            final NamedNodeMap attr = n.getAttributes();

            for (int j = 0; j < attr.getLength(); j++)
                out.write(" " + attr.item(j).getNodeName() + "=\"" +
                    attr.item(j).getNodeValue() + "\"");

            out.write(">");

            if (n.getFirstChild().getNodeType() != Node.TEXT_NODE) {
                out.write("\n");
            }

            final NodeList nl = n.getChildNodes();

            for (int i = 0; i < nl.getLength(); i++)
                printNode(out, nl.item(i));

            out.write("</" + n.getNodeName() + ">\n");
        } else {
            if (n.getNodeType() == Node.TEXT_NODE) {
                out.write(n.getNodeValue());
            } else {
                out.write("<" + n.getNodeName() + "/>");

                if (n.getNextSibling().getNodeType() != Node.TEXT_NODE) {
                    out.write("\n");
                }
            }
        }
    }
}
