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

package org.jscience.biology;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This class provides access to amino acids.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public final class AminoAcidFactory {
    /** DOCUMENT ME! */
    private static Map table;

    /** DOCUMENT ME! */
    private static Map symbols;

/**
     * Creates a new AminoAcidFactory object.
     */
    private AminoAcidFactory() {
        table = new WeakHashMap();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static char getSymbol(String name) {
        Iterator iterator;
        boolean found;
        String result;

        if ((name != null) && (name.length() > 0)) {
            if (symbols == null) {
                symbols = loadIndex();
            }

            iterator = symbols.keySet().iterator();
            found = false;
            result = new String();

            while (iterator.hasNext() && !found) {
                result = (String) iterator.next();
                found = ((String) symbols.get(result)).equals(name);
            }

            if (found) {
                return ((String) symbols.get(result)).toCharArray()[0];
            } else {
                throw new IllegalArgumentException(
                    "This name is not recognized.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't get symbol for a null or empty name.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param symbol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getName(char symbol) {
        char[] chars;

        if (symbols == null) {
            symbols = loadIndex();
        }

        chars = new char[1];
        chars[0] = symbol;

        return (String) symbols.get(new String(chars));
    }

    /**
     * DOCUMENT ME!
     *
     * @param symbol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AminoAcid getAminoAcid(char symbol) {
        return getAminoAcid(getName(symbol));
    }

    /**
     * Returns an amino acid.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static AminoAcid getAminoAcid(String name) {
        AminoAcid aminoacid;

        if ((name != null) && (name.length() > 0)) {
            name = name.toLowerCase();
            aminoacid = (AminoAcid) table.get(name);

            if (aminoacid == null) {
                aminoacid = loadAminoAcid("aminoacids/" +
                        name.replace(' ', '-') + ".xml");

                if (aminoacid != null) {
                    table.put(name, aminoacid);
                }
            }

            return aminoacid;
        } else {
            throw new IllegalArgumentException(
                "You can't get AminoAcid for a null or empty name.");
        }
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
        NodeList nl;
        Node root;
        Map index;
        Node node;
        NamedNodeMap attr;

        docFactory = DocumentBuilderFactory.newInstance();

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(AminoAcidFactory.class.getResource(
                        "aminoacids/index.xml").toString());
            root = doc.getDocumentElement();
            nl = root.getChildNodes();
            index = new HashMap();

            for (int i = 0; i < nl.getLength(); i++) {
                node = nl.item(i);

                if (node.getNodeName().equals("amino-acid")) {
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
     * Loads an amino acid from its XML resource.
     *
     * @param resname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static AminoAcid loadAminoAcid(String resname) {
        DocumentBuilderFactory docFactory;
        DocumentBuilder docBuilder;
        Document doc;
        Node root;
        NodeList nl;
        AminoAcid aminoacid;
        Class c;
        Object[] objects;

        docFactory = DocumentBuilderFactory.newInstance();

        try {
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(AminoAcidFactory.class.getResource(resname)
                                                         .toString());
            root = doc.getDocumentElement();
            nl = root.getChildNodes();

            try {
                c = Class.forName("org.jscience.biology.aminoacids." +
                        resname.substring(0, 1).toUpperCase() +
                        resname.substring(1, resname.length()));
                objects = new Object[0];
                aminoacid = (AminoAcid) c.getConstructors()[0].newInstance(objects);
                aminoacid.setName(findStringValue(nl, "name"));
                aminoacid.setAbbreviation(findStringValue(nl, "abbreviation"));
                aminoacid.setSymbol(findStringValue(nl, "symbol"));
                aminoacid.setMolecularWeight(findDoubleValue(nl,
                        "molecular-weight"));
                aminoacid.setIsoelectricPoint(findDoubleValue(nl,
                        "isoelectric-point"));
                aminoacid.setHydrophobicity(findDoubleValue(nl, "hydrophobicity"));
                aminoacid.setCASRegistryNumber(findStringValue(nl,
                        "CAS-registry-number"));
            } catch (Exception e) {
                aminoacid = null;
            }

            return aminoacid;
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
}
