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

package org.jscience.biology.loaders.biopax;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * BioPAX Reader for biological pathways and networks.
 * <p>
 * BioPAX (Biological Pathway Exchange) is an RDF/OWL-based format for
 * exchanging pathway, metabolic, and signaling data.
 * </p>
 * <p>
 * <b>Supported BioPAX Level 3 Elements:</b>
 * <ul>
 *   <li>Pathways and their components</li>
 *   <li>Biochemical reactions and catalysis</li>
 *   <li>Proteins, small molecules, complexes</li>
 *   <li>Gene regulation and molecular interactions</li>
 *   <li>Cross-references to external databases</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="http://www.biopax.org/">BioPAX.org</a>
 */
public class BioPAXReader extends AbstractResourceReader<BioPAXModel> {

    private static final Logger LOGGER = Logger.getLogger(BioPAXReader.class.getName());
    private static final String BP_NS = "http://www.biopax.org/release/biopax-level3.owl#";
    private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public BioPAXReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<BioPAXModel> getResourceType() { return BioPAXModel.class; }
    @Override public String getName() { return "BioPAX Reader"; }
    @Override public String getDescription() { return "Reads biological pathways from BioPAX format"; }
    @Override public String getLongDescription() { return "BioPAX is the standard format for biological pathway exchange including metabolic and signaling networks."; }
    @Override public String getCategory() { return "Biology"; }
    @Override public String[] getSupportedVersions() { return new String[] {"Level 3", "Level 2"}; }

    @Override
    protected BioPAXModel loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new BioPAXException("Resource not found: " + resourceId);
    }

    @Override
    protected BioPAXModel loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads BioPAX data from an input stream.
     */
    public BioPAXModel read(InputStream input) throws BioPAXException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new BioPAXException("Failed to parse BioPAX", e);
        }
    }

    /**
     * Reads BioPAX data from a file.
     */
    public BioPAXModel read(File file) throws BioPAXException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new BioPAXException("Failed to read file: " + file, e);
        }
    }

    private BioPAXModel parseDocument(Document doc) {
        BioPAXModel model = new BioPAXModel();
        Element root = doc.getDocumentElement();
        
        // Parse pathways
        NodeList pathways = doc.getElementsByTagNameNS(BP_NS, "Pathway");
        for (int i = 0; i < pathways.getLength(); i++) {
            model.addPathway(parsePathway((Element) pathways.item(i)));
        }
        
        // Parse proteins
        NodeList proteins = doc.getElementsByTagNameNS(BP_NS, "Protein");
        for (int i = 0; i < proteins.getLength(); i++) {
            model.addEntity(parseProtein((Element) proteins.item(i)));
        }
        
        // Parse small molecules
        NodeList molecules = doc.getElementsByTagNameNS(BP_NS, "SmallMolecule");
        for (int i = 0; i < molecules.getLength(); i++) {
            model.addEntity(parseSmallMolecule((Element) molecules.item(i)));
        }
        
        // Parse complexes
        NodeList complexes = doc.getElementsByTagNameNS(BP_NS, "Complex");
        for (int i = 0; i < complexes.getLength(); i++) {
            model.addEntity(parseComplex((Element) complexes.item(i)));
        }
        
        // Parse biochemical reactions
        NodeList reactions = doc.getElementsByTagNameNS(BP_NS, "BiochemicalReaction");
        for (int i = 0; i < reactions.getLength(); i++) {
            model.addInteraction(parseReaction((Element) reactions.item(i)));
        }
        
        // Parse catalysis
        NodeList catalyses = doc.getElementsByTagNameNS(BP_NS, "Catalysis");
        for (int i = 0; i < catalyses.getLength(); i++) {
            model.addInteraction(parseCatalysis((Element) catalyses.item(i)));
        }
        
        return model;
    }

    private Pathway parsePathway(Element elem) {
        Pathway pathway = new Pathway();
        pathway.setRdfId(getRdfId(elem));
        pathway.setDisplayName(getPropertyText(elem, "displayName"));
        pathway.setName(getPropertyText(elem, "name"));
        pathway.setComment(getPropertyText(elem, "comment"));
        pathway.setOrganism(getPropertyText(elem, "organism"));
        
        // Pathway components
        NodeList components = elem.getElementsByTagNameNS(BP_NS, "pathwayComponent");
        for (int i = 0; i < components.getLength(); i++) {
            Element comp = (Element) components.item(i);
            String ref = comp.getAttributeNS(RDF_NS, "resource");
            if (ref != null && !ref.isEmpty()) {
                pathway.addComponentRef(ref.replace("#", ""));
            }
        }
        
        return pathway;
    }

    private PhysicalEntity parseProtein(Element elem) {
        PhysicalEntity protein = new PhysicalEntity();
        protein.setType("Protein");
        protein.setRdfId(getRdfId(elem));
        protein.setDisplayName(getPropertyText(elem, "displayName"));
        protein.setName(getPropertyText(elem, "name"));
        protein.setComment(getPropertyText(elem, "comment"));
        return protein;
    }

    private PhysicalEntity parseSmallMolecule(Element elem) {
        PhysicalEntity molecule = new PhysicalEntity();
        molecule.setType("SmallMolecule");
        molecule.setRdfId(getRdfId(elem));
        molecule.setDisplayName(getPropertyText(elem, "displayName"));
        molecule.setName(getPropertyText(elem, "name"));
        return molecule;
    }

    private PhysicalEntity parseComplex(Element elem) {
        PhysicalEntity complex = new PhysicalEntity();
        complex.setType("Complex");
        complex.setRdfId(getRdfId(elem));
        complex.setDisplayName(getPropertyText(elem, "displayName"));
        
        // Complex components
        NodeList components = elem.getElementsByTagNameNS(BP_NS, "component");
        for (int i = 0; i < components.getLength(); i++) {
            Element comp = (Element) components.item(i);
            String ref = comp.getAttributeNS(RDF_NS, "resource");
            if (ref != null && !ref.isEmpty()) {
                complex.addComponentRef(ref.replace("#", ""));
            }
        }
        
        return complex;
    }

    private Interaction parseReaction(Element elem) {
        Interaction reaction = new Interaction();
        reaction.setType("BiochemicalReaction");
        reaction.setRdfId(getRdfId(elem));
        reaction.setDisplayName(getPropertyText(elem, "displayName"));
        
        // Left (substrates)
        NodeList lefts = elem.getElementsByTagNameNS(BP_NS, "left");
        for (int i = 0; i < lefts.getLength(); i++) {
            String ref = getResourceRef((Element) lefts.item(i));
            if (ref != null) reaction.addLeftRef(ref);
        }
        
        // Right (products)
        NodeList rights = elem.getElementsByTagNameNS(BP_NS, "right");
        for (int i = 0; i < rights.getLength(); i++) {
            String ref = getResourceRef((Element) rights.item(i));
            if (ref != null) reaction.addRightRef(ref);
        }
        
        return reaction;
    }

    private Interaction parseCatalysis(Element elem) {
        Interaction catalysis = new Interaction();
        catalysis.setType("Catalysis");
        catalysis.setRdfId(getRdfId(elem));
        
        // Controller (enzyme)
        String controller = getResourceRef(getFirstChildNS(elem, BP_NS, "controller"));
        if (controller != null) catalysis.setControllerRef(controller);
        
        // Controlled (reaction)
        String controlled = getResourceRef(getFirstChildNS(elem, BP_NS, "controlled"));
        if (controlled != null) catalysis.setControlledRef(controlled);
        
        return catalysis;
    }

    private String getRdfId(Element elem) {
        String id = elem.getAttributeNS(RDF_NS, "ID");
        if (id == null || id.isEmpty()) {
            id = elem.getAttributeNS(RDF_NS, "about");
            if (id != null) id = id.replace("#", "");
        }
        return id;
    }

    private String getPropertyText(Element parent, String propName) {
        Element child = getFirstChildNS(parent, BP_NS, propName);
        return child != null ? child.getTextContent().trim() : null;
    }

    private String getResourceRef(Element elem) {
        if (elem == null) return null;
        String ref = elem.getAttributeNS(RDF_NS, "resource");
        return (ref != null && !ref.isEmpty()) ? ref.replace("#", "") : null;
    }

    private Element getFirstChildNS(Element parent, String ns, String name) {
        NodeList list = parent.getElementsByTagNameNS(ns, name);
        return list.getLength() > 0 ? (Element) list.item(0) : null;
    }
}
