/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.mathematics.loaders.mathml;

import org.episteme.core.mathematics.structures.sets.DiscreteSet;
import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.mathematics.numbers.integers.Integer;
import org.episteme.core.mathematics.linearalgebra.vectors.DenseVector;
import org.episteme.core.mathematics.linearalgebra.vectors.RealDoubleVector;
import org.episteme.core.mathematics.linearalgebra.matrices.DenseMatrix;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;

import org.w3c.dom.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * Modernized MathMLDocumentEpistemeImpl class for Episteme 6.0.
 * Encapsulates an entire MathML document and provides methods to create 
 * MathML elements from Episteme objects.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MathMLDocumentEpistemeImpl extends MathMLDocumentImpl {

    public MathMLDocumentEpistemeImpl() {
        super();
    }

    public Element createNumber(double x) {
        final Element num = createElement("cn");
        num.appendChild(createTextNode(String.valueOf(x)));
        return num;
    }

    public Element createNumber(int i) {
        final Element num = createElement("cn");
        num.setAttribute("type", "integer");
        num.appendChild(createTextNode(String.valueOf(i)));
        return num;
    }

    public Element createNumber(Complex z) {
        final Element num = createElement("cn");
        num.setAttribute("type", "complex-cartesian");
        num.appendChild(createTextNode(String.valueOf(z.real())));
        num.appendChild(createElement("sep"));
        num.appendChild(createTextNode(String.valueOf(z.imaginary())));
        return num;
    }

    public Element createVariable(Object obj) {
        final Element var = createElement("ci");
        var.appendChild(createTextNode(obj.toString()));
        return var;
    }

    public Element createVariable(Object obj, String type) {
        final Element var = createElement("ci");
        var.setAttribute("type", type);
        var.appendChild(createTextNode(obj.toString()));
        return var;
    }

    public Element createVector(RealDoubleVector v) {
        final Element vector = createElement("vector");
        for (int i = 0; i < v.dimension(); i++)
            vector.appendChild(createNumber(v.get(i).doubleValue()));
        return vector;
    }

    public Element createIntegerVector(DenseVector<Integer> v) {
        final Element vector = createElement("vector");
        for (int i = 0; i < v.dimension(); i++)
            vector.appendChild(createNumber(v.get(i).intValue()));
        return vector;
    }

    public Element createComplexVector(DenseVector<Complex> v) {
        final Element vector = createElement("vector");
        for (int i = 0; i < v.dimension(); i++)
            vector.appendChild(createNumber(v.get(i)));
        return vector;
    }

    public Element createMatrix(RealDoubleMatrix m) {
        final Element matrix = createElement("matrix");
        for (int i = 0; i < m.rows(); i++) {
            Element row = createElement("matrixrow");
            for (int j = 0; j < m.cols(); j++)
                row.appendChild(createNumber(m.get(i, j).doubleValue()));
            matrix.appendChild(row);
        }
        return matrix;
    }

    public Element createIntegerMatrix(DenseMatrix<Integer> m) {
        final Element matrix = createElement("matrix");
        for (int i = 0; i < m.rows(); i++) {
            Element row = createElement("matrixrow");
            for (int j = 0; j < m.cols(); j++)
                row.appendChild(createNumber(m.get(i, j).intValue()));
            matrix.appendChild(row);
        }
        return matrix;
    }

    public Element createComplexMatrix(DenseMatrix<Complex> m) {
        final Element matrix = createElement("matrix");
        for (int i = 0; i < m.rows(); i++) {
            Element row = createElement("matrixrow");
            for (int j = 0; j < m.cols(); j++)
                row.appendChild(createNumber(m.get(i, j)));
            matrix.appendChild(row);
        }
        return matrix;
    }

    public Element createSet(DiscreteSet<?> s) {
        final Element set = createElement("set");
        Iterator<?> iter = s.iterator();
        while (iter.hasNext())
            set.appendChild(createVariable(iter.next()));
        return set;
    }

    public Element createApply(String op, DocumentFragment args) {
        final Element apply = createElement("apply");
        apply.appendChild(createElement(op));
        apply.appendChild(args);
        return apply;
    }

    public Element createApply(MathMLExpression expr) {
        final Element apply = createElement("apply");
        apply.appendChild(createElement(expr.getOperation()));

        for (int i = 0; i < expr.length(); i++) {
            Object arg = expr.getArgument(i);

            if (arg instanceof MathMLExpression) {
                apply.appendChild(createApply((MathMLExpression) arg));
            } else if (arg instanceof Real) {
                apply.appendChild(createNumber(((Real) arg).doubleValue()));
            } else if (arg instanceof Complex) {
                apply.appendChild(createNumber((Complex) arg));
            } else if (arg instanceof RealDoubleVector) {
                apply.appendChild(createVector((RealDoubleVector) arg));
            } else if (arg instanceof RealDoubleMatrix) {
                apply.appendChild(createMatrix((RealDoubleMatrix) arg));
            } else {
                apply.appendChild(createVariable(arg));
            }
        }

        return apply;
    }

    public void print(Writer out) throws IOException {
        out.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        if (getDoctype() != null) {
            out.write("<!DOCTYPE " + getDoctype().getName() + " PUBLIC \"" +
                getDoctype().getPublicId() + "\" \"" + getDoctype().getSystemId() +
                "\">\n");
        }
        printNode(out, getDocumentElement());
        out.flush();
    }

    private void printNode(Writer out, Node n) throws IOException {
        if (n.hasChildNodes()) {
            out.write("<" + n.getNodeName());

            final NamedNodeMap attr = n.getAttributes();
            if (attr != null) {
                for (int j = 0; j < attr.getLength(); j++)
                    out.write(" " + attr.item(j).getNodeName() + "=\"" +
                        attr.item(j).getNodeValue() + "\"");
            }

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
                if (n.getNextSibling() != null && n.getNextSibling().getNodeType() != Node.TEXT_NODE) {
                    out.write("\n");
                }
            }
        }
    }
}

