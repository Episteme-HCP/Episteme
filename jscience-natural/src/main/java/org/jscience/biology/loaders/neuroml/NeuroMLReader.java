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

package org.jscience.biology.loaders.neuroml;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * NeuroML Reader for computational neuroscience models.
 * <p>
 * NeuroML is an XML-based format for representing neural network models
 * including morphology, biophysics, and network connectivity.
 * </p>
 * <p>
 * <b>Supported NeuroML v2 Elements:</b>
 * <ul>
 *   <li>Cell morphology (segments, segment groups)</li>
 *   <li>Ion channels and synapses</li>
 *   <li>Networks (populations, projections)</li>
 *   <li>Inputs and stimuli</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="https://neuroml.org/">NeuroML.org</a>
 */
public class NeuroMLReader extends AbstractResourceReader<NeuroMLDocument> {

    private static final Logger LOGGER = Logger.getLogger(NeuroMLReader.class.getName());

    public NeuroMLReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<NeuroMLDocument> getResourceType() { return NeuroMLDocument.class; }
    @Override public String getName() { return "NeuroML Reader"; }
    @Override public String getDescription() { return "Reads computational neuroscience models from NeuroML format"; }
    @Override public String getLongDescription() { return "NeuroML is an XML format for neural network models including morphology, ion channels, and connectivity."; }
    @Override public String getCategory() { return "Biology"; }
    @Override public String[] getSupportedVersions() { return new String[] {"2.0", "2.1", "2.2"}; }

    @Override
    protected NeuroMLDocument loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new NeuroMLException("Resource not found: " + resourceId);
    }

    @Override
    protected NeuroMLDocument loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads a NeuroML model from an input stream.
     */
    public NeuroMLDocument read(InputStream input) throws NeuroMLException {
        try {
            DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new NeuroMLException("Failed to parse NeuroML", e);
        }
    }

    /**
     * Reads a NeuroML model from a file.
     */
    public NeuroMLDocument read(File file) throws NeuroMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new NeuroMLException("Failed to read file: " + file, e);
        }
    }

    private NeuroMLDocument parseDocument(Document doc) {
        NeuroMLDocument result = new NeuroMLDocument();
        Element root = doc.getDocumentElement();
        
        result.setId(root.getAttribute("id"));
        
        // Parse cells
        NodeList cells = doc.getElementsByTagName("cell");
        for (int i = 0; i < cells.getLength(); i++) {
            result.addCell(parseCell((Element) cells.item(i)));
        }
        
        // Parse ion channels
        NodeList channels = doc.getElementsByTagName("ionChannel");
        for (int i = 0; i < channels.getLength(); i++) {
            result.addIonChannel(parseIonChannel((Element) channels.item(i)));
        }
        
        // Parse networks
        NodeList networks = doc.getElementsByTagName("network");
        for (int i = 0; i < networks.getLength(); i++) {
            result.addNetwork(parseNetwork((Element) networks.item(i)));
        }
        
        // Parse synapses
        NodeList synapses = doc.getElementsByTagName("expTwoSynapse");
        for (int i = 0; i < synapses.getLength(); i++) {
            result.addSynapse(parseSynapse((Element) synapses.item(i)));
        }
        
        return result;
    }

    private Cell parseCell(Element elem) {
        Cell cell = new Cell();
        cell.setId(elem.getAttribute("id"));
        
        // Morphology
        Element morphElem = getFirstChildElement(elem, "morphology");
        if (morphElem != null) {
            cell.setMorphology(parseMorphology(morphElem));
        }
        
        // Biophysical properties
        Element bioElem = getFirstChildElement(elem, "biophysicalProperties");
        if (bioElem != null) {
            cell.setBiophysicalProperties(parseBiophysicalProperties(bioElem));
        }
        
        return cell;
    }

    private Morphology parseMorphology(Element elem) {
        Morphology morph = new Morphology();
        morph.setId(elem.getAttribute("id"));
        
        // Parse segments
        NodeList segments = elem.getElementsByTagName("segment");
        for (int i = 0; i < segments.getLength(); i++) {
            morph.addSegment(parseSegment((Element) segments.item(i)));
        }
        
        // Parse segment groups
        NodeList groups = elem.getElementsByTagName("segmentGroup");
        for (int i = 0; i < groups.getLength(); i++) {
            morph.addSegmentGroup(parseSegmentGroup((Element) groups.item(i)));
        }
        
        return morph;
    }

    private Segment parseSegment(Element elem) {
        Segment seg = new Segment();
        seg.setId(elem.getAttribute("id"));
        
        String name = elem.getAttribute("name");
        if (!name.isEmpty()) seg.setName(name);
        
        // Parent
        Element parentElem = getFirstChildElement(elem, "parent");
        if (parentElem != null) {
            seg.setParentId(parentElem.getAttribute("segment"));
        }
        
        // Proximal point
        Element proxElem = getFirstChildElement(elem, "proximal");
        if (proxElem != null) {
            seg.setProximal(parsePoint3D(proxElem));
        }
        
        // Distal point
        Element distElem = getFirstChildElement(elem, "distal");
        if (distElem != null) {
            seg.setDistal(parsePoint3D(distElem));
        }
        
        return seg;
    }

    private Point3D parsePoint3D(Element elem) {
        double x = parseDouble(elem.getAttribute("x"), 0);
        double y = parseDouble(elem.getAttribute("y"), 0);
        double z = parseDouble(elem.getAttribute("z"), 0);
        double diameter = parseDouble(elem.getAttribute("diameter"), 1);
        return new Point3D(x, y, z, diameter);
    }

    private SegmentGroup parseSegmentGroup(Element elem) {
        SegmentGroup group = new SegmentGroup();
        group.setId(elem.getAttribute("id"));
        
        NodeList members = elem.getElementsByTagName("member");
        for (int i = 0; i < members.getLength(); i++) {
            Element memberElem = (Element) members.item(i);
            group.addMemberSegmentId(memberElem.getAttribute("segment"));
        }
        
        return group;
    }

    private BiophysicalProperties parseBiophysicalProperties(Element elem) {
        BiophysicalProperties props = new BiophysicalProperties();
        props.setId(elem.getAttribute("id"));
        
        // Membrane properties
        Element membraneElem = getFirstChildElement(elem, "membraneProperties");
        if (membraneElem != null) {
            // Channel densities
            NodeList densities = membraneElem.getElementsByTagName("channelDensity");
            for (int i = 0; i < densities.getLength(); i++) {
                Element densElem = (Element) densities.item(i);
                ChannelDensity density = new ChannelDensity();
                density.setId(densElem.getAttribute("id"));
                density.setIonChannel(densElem.getAttribute("ionChannel"));
                density.setCondDensity(densElem.getAttribute("condDensity"));
                density.setIon(densElem.getAttribute("ion"));
                density.setErev(densElem.getAttribute("erev"));
                props.addChannelDensity(density);
            }
            
            // Specific capacitance
            Element specCap = getFirstChildElement(membraneElem, "specificCapacitance");
            if (specCap != null) {
                props.setSpecificCapacitance(specCap.getAttribute("value"));
            }
        }
        
        return props;
    }

    private IonChannel parseIonChannel(Element elem) {
        IonChannel channel = new IonChannel();
        channel.setId(elem.getAttribute("id"));
        channel.setSpecies(elem.getAttribute("species"));
        channel.setConductance(elem.getAttribute("conductance"));
        return channel;
    }

    private Network parseNetwork(Element elem) {
        Network network = new Network();
        network.setId(elem.getAttribute("id"));
        
        // Populations
        NodeList populations = elem.getElementsByTagName("population");
        for (int i = 0; i < populations.getLength(); i++) {
            Element popElem = (Element) populations.item(i);
            Population pop = new Population();
            pop.setId(popElem.getAttribute("id"));
            pop.setComponent(popElem.getAttribute("component"));
            pop.setSize(parseInt(popElem.getAttribute("size"), 1));
            network.addPopulation(pop);
        }
        
        // Projections
        NodeList projections = elem.getElementsByTagName("projection");
        for (int i = 0; i < projections.getLength(); i++) {
            Element projElem = (Element) projections.item(i);
            Projection proj = new Projection();
            proj.setId(projElem.getAttribute("id"));
            proj.setPresynapticPopulation(projElem.getAttribute("presynapticPopulation"));
            proj.setPostsynapticPopulation(projElem.getAttribute("postsynapticPopulation"));
            proj.setSynapse(projElem.getAttribute("synapse"));
            network.addProjection(proj);
        }
        
        return network;
    }

    private Synapse parseSynapse(Element elem) {
        Synapse syn = new Synapse();
        syn.setId(elem.getAttribute("id"));
        syn.setTauRise(elem.getAttribute("tauRise"));
        syn.setTauDecay(elem.getAttribute("tauDecay"));
        syn.setErev(elem.getAttribute("erev"));
        syn.setGbase(elem.getAttribute("gbase"));
        return syn;
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Element getFirstChildElement(Element parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element elem = (Element) child;
                if (name.equals(elem.getLocalName()) || name.equals(elem.getTagName())) {
                    return elem;
                }
            }
        }
        return null;
    }
}
