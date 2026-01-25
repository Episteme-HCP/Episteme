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

package org.jscience.mathematics.loaders.mathml;

import org.apache.xerces.parsers.DOMParser;
import org.jscience.mathematics.numbers.complex.Complex;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.integers.Integer;
import org.jscience.mathematics.structures.sets.DiscreteSet;
import org.jscience.mathematics.sets.Integers;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Modernized MathMLParser class for JScience 6.0.
 * Parses a MathML document into JScience objects.
 *
 * @author Mark Hale
 * @author Silvere Martin-Michiellot (Modernization)
 * @version 1.1
 */
public final class MathMLParser extends DOMParser {

    public MathMLParser() {
        try {
            setDocumentClassName("org.jscience.mathematics.loaders.mathml.MathMLDocumentJScienceImpl");
        } catch (Exception e) {
            // Log warning
        }
    }

    public Object[] translateToJScienceObjects() {
        Translator translator = new JScienceObjectTranslator();
        return translator.translate(getDocument().getDocumentElement());
    }

    abstract class Translator {
        public Translator() {
        }

        public Object[] translate(Node root) {
            return parseMATH(root);
        }

        private Object[] parseMATH(Node n) {
            int len = 0;
            final NodeList nl = n.getChildNodes();
            final Object[] objList = new Object[nl.getLength()];

            for (int i = 0; i < objList.length; i++) {
                Object obj = processNode(nl.item(i));
                if (obj != null) {
                    objList[len++] = obj;
                }
            }

            final Object[] parseList = new Object[len];
            System.arraycopy(objList, 0, parseList, 0, len);
            return parseList;
        }

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
            } else {
                return null;
            }
        }

        protected abstract Object parseAPPLY(Node n);
        protected abstract Object parseCN(Node n);
        protected abstract Object parseCI(Node n);
        protected abstract Object parseVECTOR(Node n);
        protected abstract Object parseMATRIX(Node n);
        protected abstract Object parseSET(Node n);
    }

    class JScienceObjectTranslator extends Translator {
        private final int TYPE_INTEGER = 0;
        private final int TYPE_DOUBLE = 1;
        private final int TYPE_COMPLEX = 2;

        public JScienceObjectTranslator() {
        }

        @Override
        protected Object parseAPPLY(Node n) {
            final MathMLExpression expr = new MathMLExpression();
            final NodeList nl = n.getChildNodes();
            int i = 0;
            while (i < nl.getLength() && nl.item(i).getNodeType() == Node.TEXT_NODE) i++;
            if (i >= nl.getLength()) return null;

            expr.setOperation(nl.item(i).getNodeName());
            for (; i < nl.getLength(); i++) {
                Object obj = processNode(nl.item(i));
                if (obj != null) expr.addArgument(obj);
            }
            return expr;
        }

        @Override
        protected Object parseCN(Node n) {
            return parseNumber(n);
        }

        private Object parseNumber(Node n) {
            final NamedNodeMap attr = n.getAttributes();
            Node baseNode = attr.getNamedItem("base");
            if (baseNode != null && !baseNode.getNodeValue().equals("10")) return null;

            Node typeNode = attr.getNamedItem("type");
            if (typeNode == null) return Real.of(n.getTextContent().trim());

            final String attrType = typeNode.getNodeValue();
            return switch (attrType) {
                case "integer" -> Integer.valueOf(n.getTextContent().trim());
                case "real" -> Real.of(n.getTextContent().trim());
                case "complex-cartesian" -> {
                    NodeList children = n.getChildNodes();
                    List<String> parts = new ArrayList<>();
                    for (int i=0; i<children.getLength(); i++) {
                        if (children.item(i).getNodeType() == Node.TEXT_NODE) parts.add(children.item(i).getNodeValue().trim());
                    }
                    if (parts.size() >= 2) yield Complex.of(Double.parseDouble(parts.get(0)), Double.parseDouble(parts.get(1)));
                    yield Complex.of(Double.parseDouble(n.getTextContent().trim()));
                }
                case "constant" -> {
                    String val = n.getTextContent().trim();
                    yield switch (val) {
                        case "&pi;", "pi" -> Real.PI;
                        case "&ee;", "e" -> Real.E;
                        case "&ii;", "i" -> Complex.I;
                        case "&infty;", "inf" -> Real.POSITIVE_INFINITY;
                        default -> null;
                    };
                }
                default -> Real.of(n.getTextContent().trim());
            };
        }

        @Override
        protected Object parseCI(Node n) {
            return n.getTextContent().trim();
        }

        @Override
        protected Object parseVECTOR(Node vectorNode) {
            List<Object> elements = new ArrayList<>();
            int overallType = TYPE_INTEGER;
            NodeList nl = vectorNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals("cn")) {
                    Object num = parseNumber(nl.item(i));
                    if (num != null) {
                        elements.add(num);
                        if (num instanceof Real && overallType < TYPE_DOUBLE) overallType = TYPE_DOUBLE;
                        else if (num instanceof Complex) overallType = TYPE_COMPLEX;
                    }
                }
            }

            if (overallType == TYPE_INTEGER) {
                List<org.jscience.mathematics.numbers.integers.Integer> list = elements.stream()
                    .map(o -> (org.jscience.mathematics.numbers.integers.Integer)o)
                    .collect(java.util.stream.Collectors.toList());
                return org.jscience.mathematics.linearalgebra.vectors.DenseVector.of(list, org.jscience.mathematics.sets.Integers.getInstance());
            } else if (overallType == TYPE_DOUBLE) {
                double[] arr = elements.stream().mapToDouble(o -> ((Number)o).doubleValue()).toArray();
                return org.jscience.mathematics.linearalgebra.vectors.RealDoubleVector.of(arr);
            } else {
                List<Complex> list = elements.stream()
                    .map(o -> o instanceof Complex ? (Complex)o : Complex.of(((Number)o).doubleValue()))
                    .collect(java.util.stream.Collectors.toList());
                return org.jscience.mathematics.linearalgebra.vectors.DenseVector.valueOf(list);
            }
        }

        @Override
        protected Object parseMATRIX(Node matrixNode) {
            List<List<Object>> rowsRaw = new ArrayList<>();
            NodeList nl = matrixNode.getChildNodes();
            int overallType = TYPE_INTEGER;
            
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals("matrixrow")) {
                    List<Object> rowData = parseMatrixRow(nl.item(i));
                    rowsRaw.add(rowData);
                    for (Object o : rowData) {
                        if (o instanceof Real && overallType < TYPE_DOUBLE) overallType = TYPE_DOUBLE;
                        else if (o instanceof Complex) overallType = TYPE_COMPLEX;
                    }
                }
            }

            if (overallType == TYPE_INTEGER) {
                List<List<org.jscience.mathematics.numbers.integers.Integer>> rows = rowsRaw.stream()
                    .map(row -> row.stream()
                        .map(o -> (org.jscience.mathematics.numbers.integers.Integer)o)
                        .collect(java.util.stream.Collectors.toList()))
                    .collect(java.util.stream.Collectors.toList());
                return org.jscience.mathematics.linearalgebra.matrices.GenericMatrix.of(rows, org.jscience.mathematics.sets.Integers.getInstance());
            } else if (overallType == TYPE_DOUBLE) {
                List<List<Real>> rows = rowsRaw.stream()
                    .map(row -> row.stream()
                        .map(o -> o instanceof Real ? (Real)o : Real.of(((Number)o).doubleValue()))
                        .collect(java.util.stream.Collectors.toList()))
                    .collect(java.util.stream.Collectors.toList());
                return org.jscience.mathematics.linearalgebra.matrices.DenseMatrix.of(rows, org.jscience.mathematics.sets.Reals.getInstance());
            } else {
                List<List<Complex>> rows = rowsRaw.stream()
                    .map(row -> row.stream()
                        .map(o -> o instanceof Complex ? (Complex)o : Complex.of(((Number)o).doubleValue()))
                        .collect(java.util.stream.Collectors.toList()))
                    .collect(java.util.stream.Collectors.toList());
                return org.jscience.mathematics.linearalgebra.matrices.GenericMatrix.of(rows, Complex.ZERO.getScalarRing());
            }
        }

        private List<Object> parseMatrixRow(Node rowNode) {
            List<Object> row = new ArrayList<>();
            NodeList nl = rowNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals("cn")) {
                    Object num = parseNumber(nl.item(i));
                    if (num != null) row.add(num);
                }
            }
            return row;
        }

        @Override
        protected Object parseSET(Node setNode) {
            Set<Object> elements = new HashSet<>();
            NodeList nl = setNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeName().equals("ci")) {
                    elements.add(parseCI(nl.item(i)));
                } else if (nl.item(i).getNodeName().equals("cn")) {
                    elements.add(parseCN(nl.item(i)));
                }
            }
            return new DiscreteSet<>(elements);
        }
    }
}
