package org.jscience.chemistry;

import org.jscience.chemistry.elements.*;
import org.jscience.measure.Units;

import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.SpecificHeatCapacity;
import org.jscience.measure.quantity.ThermalConductivity;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Temperature;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jscience.measure.Quantities;

/**
 * This class provides access to the elements of the periodic table.
 * Modernized for JScience V5 to return {@link Element} objects with
 * {@link Quantity} properties.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 5.0
 */
public final class PeriodicTable {

    // Custom Units derived for XML data
    private static final Unit<Length> ANGSTROM = Units.NANOMETER.divide(10);
    private static final Unit<Temperature> KELVIN = Units.KELVIN;

    // Note: Creating composite units directly using operations on base units
    private static final Unit<MassDensity> G_PER_CM3 = Units.GRAM.divide(Units.CENTIMETER.pow(3))
            .asType(MassDensity.class);

    // J/(g·K)
    private static final Unit<SpecificHeatCapacity> J_PER_G_K = Units.JOULE.divide(Units.GRAM.multiply(Units.KELVIN))
            .asType(SpecificHeatCapacity.class);

    // W/(cm·K)
    private static final Unit<ThermalConductivity> W_PER_CM_K = Units.WATT
            .divide(Units.CENTIMETER.multiply(Units.KELVIN)).asType(ThermalConductivity.class);

    private static Map<String, Element> table = new HashMap<>();
    private static Map<String, String> symbols;

    private PeriodicTable() {
    }

    /**
     * Returns the name for a symbol (e.g. "H" -> "Hydrogen").
     *
     * @param symbol the chemical symbol.
     * @return the corresponding name.
     */
    public static String getName(String symbol) {
        if (symbols == null) {
            symbols = loadIndex();
        }
        return symbols.get(symbol);
    }

    /**
     * Returns an element from its name or symbol.
     * The elements are loaded on demand from XML resources.
     *
     * @param nameOrSymbol the element name (e.g. "Hydrogen") or symbol (e.g. "H").
     * @return the corresponding element or {@code null} if not found.
     */
    public static Element getElement(String nameOrSymbol) {
        // Check if input is a symbol
        if (nameOrSymbol != null && nameOrSymbol.length() <= 2 && getSymbolMap().containsKey(nameOrSymbol)) {
            String name = getSymbolMap().get(nameOrSymbol);
            return getElementByName(name);
        }
        return getElementByName(nameOrSymbol);
    }

    private static Map<String, String> getSymbolMap() {
        if (symbols == null) {
            symbols = loadIndex();
        }
        return symbols;
    }

    private static Element getElementByName(String name) {
        if (name == null)
            return null;
        String lowerName = name.toLowerCase();

        synchronized (table) {
            // Basic cache check (WeakHashMap)
        }

        Element element = table.get(lowerName);
        if (element == null) {
            element = loadElement("periodictable/" + lowerName + ".xml");
            if (element != null) {
                table.put(lowerName, element);
            }
        }
        return element;
    }

    /**
     * Registers an element manually (e.g. from JSON loader).
     * 
     * @param element the element to register.
     */
    public static void registerElement(Element element) {
        if (element != null && element.getSymbol() != null) {
            String lowerName = element.getName().toLowerCase();
            String symbol = element.getSymbol();

            synchronized (table) {
                table.put(lowerName, element);
            }
            // Ensure symbol map is loaded or updated
            getSymbolMap().put(symbol, element.getName());
        }
    }

    private static Map<String, String> loadIndex() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver(new org.xml.sax.EntityResolver() {
                public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.contains("elements.dtd")) {
                        return new org.xml.sax.InputSource(
                                PeriodicTable.class.getResourceAsStream("periodictable/elements.dtd"));
                    }
                    return null;
                }
            });
            InputStream is = PeriodicTable.class.getResourceAsStream("periodictable/index.xml");
            if (is == null)
                return Collections.emptyMap();

            Document doc = docBuilder.parse(is);
            Node root = doc.getDocumentElement();
            NodeList nl = root.getChildNodes();
            Map<String, String> index = new HashMap<>();

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node.getNodeName().equals("element")) {
                    NamedNodeMap attr = node.getAttributes();
                    String sym = attr.getNamedItem("symbol").getNodeValue();
                    String name = attr.getNamedItem("name").getNodeValue();
                    index.put(sym, name);
                }
            }
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    private static Element loadElement(String resname) {
        try {
            InputStream is = PeriodicTable.class.getResourceAsStream(resname);
            if (is == null)
                return null;

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver(new org.xml.sax.EntityResolver() {
                public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
                    if (systemId.contains("elements.dtd")) {
                        return new org.xml.sax.InputSource(
                                PeriodicTable.class.getResourceAsStream("periodictable/elements.dtd"));
                    }
                    return null;
                }
            });
            Document doc = docBuilder.parse(is);

            Node root = doc.getDocumentElement();
            String group = "";
            Node seriesNode = root.getAttributes().getNamedItem("series");
            if (seriesNode != null)
                group = seriesNode.getNodeValue();

            NodeList nl = root.getChildNodes();
            String name = findStringValue(nl, "name");
            String symbol = findStringValue(nl, "symbol");

            Element elem;
            switch (group) {
                case "non-metal":
                    elem = new NonMetal(name, symbol);
                    break;
                case "halogen":
                    elem = new Halogen(name, symbol);
                    break;
                case "noble-gas":
                    elem = new NobleGas(name, symbol);
                    break;
                case "metal":
                    elem = new Metal(name, symbol);
                    break;
                case "alkali-metal":
                    elem = new AlkaliMetal(name, symbol);
                    break;
                case "alkali-earth-metal":
                    elem = new AlkaliEarthMetal(name, symbol);
                    break;
                case "rare-earth-metal":
                    elem = new RareEarthMetal(name, symbol);
                    break;
                case "transition-metal":
                    elem = new TransitionMetal(name, symbol);
                    break;
                default:
                    elem = new Element(name, symbol);
            }

            String atomicNumStr = findStringValue(nl, "atomic-number");
            if (!atomicNumStr.isEmpty())
                elem.setAtomicNumber(Integer.parseInt(atomicNumStr));

            String massNumStr = findStringValue(nl, "mass-number");
            if (!massNumStr.isEmpty())
                elem.setMassNumber(Integer.parseInt(massNumStr));

            double en = findDoubleValue(nl, "electronegativity");
            if (!Double.isNaN(en))
                elem.setElectronegativity(en);

            double covRad = findDoubleValue(nl, "covalent-radius");
            if (!Double.isNaN(covRad))
                elem.setCovalentRadius(Quantities.create(covRad, ANGSTROM));

            double atomRad = findDoubleValue(nl, "atomic-radius");
            if (!Double.isNaN(atomRad))
                elem.setAtomicRadius(Quantities.create(atomRad, ANGSTROM));

            double melt = findDoubleValue(nl, "melting-point");
            if (!Double.isNaN(melt))
                elem.setMeltingPoint(Quantities.create(melt, KELVIN));

            double boil = findDoubleValue(nl, "boiling-point");
            if (!Double.isNaN(boil))
                elem.setBoilingPoint(Quantities.create(boil, KELVIN));

            double dens = findDoubleValue(nl, "density");
            if (!Double.isNaN(dens))
                elem.setDensity(Quantities.create(dens, G_PER_CM3));

            double specHeat = findDoubleValue(nl, "specific-heat");
            if (!Double.isNaN(specHeat))
                elem.setSpecificHeat(Quantities.create(specHeat, J_PER_G_K));

            double thermCond = findDoubleValue(nl, "thermal-conductivity");
            if (!Double.isNaN(thermCond))
                elem.setThermalConductivity(Quantities.create(thermCond, W_PER_CM_K));

            return elem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String findStringValue(NodeList nl, String name) {
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if (item.getNodeName().equals(name)) {
                Node firstChild = item.getFirstChild();
                if (firstChild != null)
                    return firstChild.getNodeValue();
            }
        }
        return "";
    }

    private static double findDoubleValue(NodeList nl, String name) {
        String val = findStringValue(nl, name);
        if (val.isEmpty())
            return Double.NaN;
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    /**
     * Retrieves an element by its chemical symbol (Legacy alias).
     * 
     * @param symbol the symbol (e.g. "H", "He").
     * @return the corresponding element or null if not found.
     */
    public static Element bySymbol(String symbol) {
        return getElement(symbol);
    }
}
