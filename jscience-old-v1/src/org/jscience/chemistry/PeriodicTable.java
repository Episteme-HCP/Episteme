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

package org.jscience.chemistry;

import org.jscience.chemistry.elements.*;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This class provides access to the elements of the periodic table.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 0.3
 */

//http://www.webelements.com/ for data
//may be we could add support for crystal structure and parameters
public final class PeriodicTable extends Object {
    /** DOCUMENT ME! */
    private static Map table;

    /** DOCUMENT ME! */
    private static Map symbols;

/**
     * Creates a new PeriodicTable object.
     */
    private PeriodicTable() {
        table = new WeakHashMap();
    }

    /**
     * Returns the name for a symbol.
     *
     * @param symbol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getName(String symbol) {
        if (symbols == null) {
            symbols = loadIndex();
        }

        return (String) symbols.get(symbol);
    }

    /**
     * Returns an element.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Element getElement(String name) {
        Element element;

        name = name.toLowerCase();
        element = (Element) table.get(name);

        if (element == null) {
            element = loadElement("periodictable/" + name + ".xml");

            if (element != null) {
                table.put(name, element);
            }
        }

        return element;
    }

    /**
     * Loads the XML index.
     *
     * @return DOCUMENT ME!
     */
    private static Map loadIndex() {
        DocumentBuilderFactory docFactory;
        DocumentBuilder docBuilder;
        Document doc;
        Node root;
        NodeList nl;
        Map index;
        Node node;
        NamedNodeMap attr;

        docFactory = DocumentBuilderFactory.newInstance();

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(PeriodicTable.class.getResource(
                        "periodictable/index.xml").toString());
            root = doc.getDocumentElement();
            nl = root.getChildNodes();
            index = new HashMap();

            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);

                if (node.getNodeName().equals("element")) {
                    attr = node.getAttributes();
                    index.put(attr.getNamedItem("symbol").getNodeValue(),
                        attr.getNamedItem("name").getNodeValue());
                }
            }

            return index;
        } catch (ParserConfigurationException e) {
            return Collections.EMPTY_MAP;
        } catch (IOException e) {
            return Collections.EMPTY_MAP;
        } catch (SAXException e) {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     * Loads an element from its XML resource.
     *
     * @param resname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Element loadElement(String resname) {
        DocumentBuilderFactory docFactory;
        DocumentBuilder docBuilder;
        Document doc;
        Node root;
        String group;
        NodeList nl;
        String name;
        String symbol;
        Element elem;

        docFactory = DocumentBuilderFactory.newInstance();

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(PeriodicTable.class.getResource(resname)
                                                      .toString());
            root = doc.getDocumentElement();
            group = root.getAttributes().getNamedItem("series").getNodeValue();
            nl = root.getChildNodes();
            name = findStringValue(nl, "name");
            symbol = findStringValue(nl, "symbol");

            if (group.equals("non-metal")) {
                elem = new NonMetal(name, symbol);
            } else if (group.equals("halogen")) {
                elem = new Halogen(name, symbol);
            } else if (group.equals("noble-gas")) {
                elem = new NobleGas(name, symbol);
            } else if (group.equals("metal")) {
                elem = new Metal(name, symbol);
            } else if (group.equals("alkali-metal")) {
                elem = new AlkaliMetal(name, symbol);
            } else if (group.equals("alkali-earth-metal")) {
                elem = new AlkaliEarthMetal(name, symbol);
            } else if (group.equals("rare-earth-metal")) {
                elem = new RareEarthMetal(name, symbol);
            } else if (group.equals("transition-metal")) {
                elem = new TransitionMetal(name, symbol);
            } else {
                elem = new Element(name, symbol);
            }

            elem.setAtomicNumber(Integer.parseInt(findStringValue(nl,
                        "atomic-number")));
            elem.setMassNumber(Integer.parseInt(findStringValue(nl,
                        "mass-number")));
            elem.setQuantumLayers(buildIntArray(findStringValue(nl,
                        "quantum-layers")));
            elem.setElectronegativity(findDoubleValue(nl, "electronegativity"));
            elem.setCovalentRadius(findDoubleValue(nl, "covalent-radius"));
            elem.setAtomicRadius(findDoubleValue(nl, "atomic-radius"));
            elem.setIonizationEnergies(buildDoubleArray(findStringValue(nl,
                        "ionization-energies")));
            elem.setElectronicAffinity(findDoubleValue(nl, "electronic-affinity"));
            elem.setMeltingPoint(findDoubleValue(nl, "melting-point"));
            elem.setBoilingPoint(findDoubleValue(nl, "boiling-point"));
            elem.setDensity(findDoubleValue(nl, "density"));
            elem.setFormationEnthalpy(findDoubleValue(nl, "formation-enthalpy"));
            elem.setFormationEnergy(findDoubleValue(nl, "formation-energy"));
            elem.setEntropy(findDoubleValue(nl, "entropy"));
            elem.setSpecificHeat(findDoubleValue(nl, "specific-heat"));
            elem.setElectricalConductivity(findDoubleValue(nl,
                    "electrical-conductivity"));
            elem.setThermalConductivity(findDoubleValue(nl,
                    "thermal-conductivity"));
            elem.setIsotopes(findMapValue(nl, "isotopes"));

            return elem;
        } catch (ParserConfigurationException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (SAXException e) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String findStringValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return item.getFirstChild().getNodeValue();
            }
        }

        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double findDoubleValue(NodeList nl, String name) {
        Node item;

        for (int i = 0; i < nl.getLength(); i++) {
            item = nl.item(i);

            if (item.getNodeName().equals(name)) {
                return Double.parseDouble(item.getFirstChild().getNodeValue());
            }
        }

        return Double.NaN;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int[] buildIntArray(String values) {
        String[] splitedValues;
        int[] result;
        int count;

        splitedValues = values.split(values);
        count = 0;

        for (int x = 0; x < splitedValues.length; x++) {
            if (splitedValues[x].length() > 0) {
                count++;
            }
        }

        result = new int[count];
        count = 0;

        for (int x = 0; x < result.length; x++) {
            if (splitedValues[x].length() > 0) {
                result[count] = Integer.parseInt(splitedValues[x]);
                count++;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double[] buildDoubleArray(String values) {
        String[] splitedValues;
        double[] result;
        int count;

        splitedValues = values.split(values);
        count = 0;

        for (int x = 0; x < splitedValues.length; x++) {
            if (splitedValues[x].length() > 0) {
                count++;
            }
        }

        result = new double[count];
        count = 0;

        for (int x = 0; x < result.length; x++) {
            if (splitedValues[x].length() > 0) {
                result[count] = Double.parseDouble(splitedValues[x]);
                count++;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nl DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Map findMapValue(NodeList nl, String name) {
        Node item1;
        Node item2;
        Map map;
        NodeList subList;

        map = new HashMap();

        for (int i = 0; i < nl.getLength(); i++) {
            item1 = nl.item(i);

            if (item1.getNodeName().equals(name)) {
                subList = item1.getChildNodes();

                for (int j = 0; i < subList.getLength(); j++) {
                    item2 = subList.item(j);
                    map.put(item2.getFirstChild().getFirstChild().getNodeValue(),
                        item2.getLastChild().getFirstChild().getNodeValue());
                }

                return map;
            }
        }

        return map;
    }
}
