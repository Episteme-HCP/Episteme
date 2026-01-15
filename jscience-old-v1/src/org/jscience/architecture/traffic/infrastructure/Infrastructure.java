/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.infrastructure;

import org.jscience.architecture.traffic.SelectionStarter;
import org.jscience.architecture.traffic.TrafficException;
import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;
import org.jscience.architecture.traffic.util.ArrayEnumeration;
import org.jscience.architecture.traffic.util.ArrayUtils;
import org.jscience.architecture.traffic.util.NumberDispenser;
import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * The encapsulating class
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class Infrastructure implements XMLSerializable, SelectionStarter {
    /** DOCUMENT ME! */
    public static final int blockLength = 10;

    /** DOCUMENT ME! */
    public static final int blockWidth = 10;

    // This one is temporary (for debugging TC-3)
    /** DOCUMENT ME! */
    public static Hashtable laneDictionary;

    /** All nodes in this infrastructure, including edge nodes */
    protected Node[] allNodes;

    /** All exit/entry nodes in this infrastructure */
    protected SpecialNode[] specialNodes;

    /** All nodes that are not EdgeNodes */
    protected Junction[] junctions;

    /** Meta-data provided by the user */
    protected String title;

    /** Meta-data provided by the user */
    protected String author;

    /** Meta-data provided by the user */
    protected String comments;

    /** The infrastructure version of this implementation. For debugging. */
    protected final int version = 1;

    /** The size of this infrastructure, in pixels */
    protected Dimension size;

    /** All the inbound lanes on all the Nodes in our Infrastructure */
    protected Vector allLanes;

    /** Number dispenser for sign id's */
    protected NumberDispenser signNumbers = new NumberDispenser();

    /**
     * The current cycle we're in, manely for Nodes to have access to
     * this data
     */
    protected int curCycle;

    /** DOCUMENT ME! */
    protected String parentName = "model";

/**
     * Creates a new infrastructure object.
     *
     * @param dim The dimension of the new infrastructure
     */
    public Infrastructure(Dimension dim) {
        size = dim;
        allNodes = new Node[0];
        specialNodes = new SpecialNode[0];
        junctions = new Junction[0];
        title = "untitled";
        author = "unknown";
        comments = "";
        curCycle = 0;
    }

/**
     * Creates a new infrastructure object.
     *
     * @param nodes    The Nodes this Infrastructure should contain.
     * @param special  The exit/entry nodes this Infrastructure should contain.
     * @param new_size The size of this Infrastructure in pixels x pixels
     */
    public Infrastructure(Node[] nodes, SpecialNode[] special,
        Dimension new_size) {
        allNodes = nodes;
        specialNodes = special;
        junctions = new Junction[allNodes.length - specialNodes.length];
        copyJunctions();
        size = new_size;
        title = "untitled";
        author = "unknown";
        comments = "";
    }

/**
     * Constructor for loading
     */
    public Infrastructure() {
        allNodes = new Node[0];
        specialNodes = new SpecialNode[0];
        junctions = new Junction[0];
        allLanes = new Vector();
        title = "untitled";
        author = "";
        comments = "";
        size = new Dimension(5000, 3000);
    }

    /*============================================*/
    /* Basic GET and SET methods                  */
    /*============================================*/
    /**
     * Returns the title.
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param s DOCUMENT ME!
     */
    public void setTitle(String s) {
        title = s;
    }

    /**
     * Returns the author.
     *
     * @return DOCUMENT ME!
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     *
     * @param s DOCUMENT ME!
     */
    public void setAuthor(String s) {
        author = s;
    }

    /**
     * Returns the comments.
     *
     * @return DOCUMENT ME!
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments.
     *
     * @param s DOCUMENT ME!
     */
    public void setComments(String s) {
        comments = s;
    }

    /**
     * Returns all exit/entry nodes
     *
     * @return DOCUMENT ME!
     */
    public SpecialNode[] getSpecialNodes() {
        return specialNodes;
    }

    /**
     * Sets all exit/entry nodes
     *
     * @param nodes DOCUMENT ME!
     */
    public void setSpecialNodes(SpecialNode[] nodes) {
        specialNodes = nodes;
    }

    /**
     * Returns the Junctions of this infrastructure.
     *
     * @return DOCUMENT ME!
     */
    public Junction[] getJunctions() {
        return junctions;
    }

    /**
     * Sets all junctions.
     *
     * @param _junctions DOCUMENT ME!
     */
    public void setJunctions(Junction[] _junctions) {
        junctions = junctions;
    }

    /**
     * Returns all nodes (including edge nodes)
     *
     * @return DOCUMENT ME!
     */
    public Node[] getAllNodes() {
        return allNodes;
    }

    /**
     * Sets all nodes (including edge nodes)
     *
     * @param nodes DOCUMENT ME!
     */
    public void setAllNodes(Node[] nodes) {
        allNodes = nodes;
    }

    /**
     * Returns the size of this infrastructure in pixels
     *
     * @return DOCUMENT ME!
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Sets the size of this infrastructure in pixels
     *
     * @param s DOCUMENT ME!
     */
    public void setSize(Dimension s) {
        size = s;
    }

    /**
     * Returns the number of nodes
     *
     * @return DOCUMENT ME!
     */
    public int getNumNodes() {
        return allNodes.length;
    }

    /**
     * Returns the number of edgenodes
     *
     * @return DOCUMENT ME!
     */
    public int getNumSpecialNodes() {
        return specialNodes.length;
    }

    /**
     * Returns the number of junctions
     *
     * @return DOCUMENT ME!
     */
    public int getNumJunctions() {
        return junctions.length;
    }

    /**
     * Sets the current cycle
     *
     * @param c DOCUMENT ME!
     */
    public void setCurCycle(int c) {
        curCycle = c;
    }

    /**
     * Returns the current cycle
     *
     * @return DOCUMENT ME!
     */
    public int getCurCycle() {
        return curCycle;
    }

    /**
     * Returns the total number of signs in the infrastructure
     *
     * @return DOCUMENT ME!
     */
    public int getTotalNumSigns() {
        //count signs
        int result = 0;
        int num_nodes = allNodes.length;

        for (int i = 0; i < num_nodes; i++)
            result += allNodes[i].getNumSigns();

        return result;
    }

    /**
     * Returns an array containing all statistics of the
     * infrastructure. The index in the array corresponds to the Node id.
     *
     * @return DOCUMENT ME!
     */
    public NodeStatistics[][] getNodeStatistics() {
        NodeStatistics[][] stats = new NodeStatistics[allNodes.length][];

        for (int i = 0; i < stats.length; i++)
            stats[i] = allNodes[i].getStatistics();

        return stats;
    }

    /**
     * Returns an array containing all statistics of all EdgeNodes. The
     * index in the array corresponds to the EdgeNode id.
     *
     * @return DOCUMENT ME!
     */
    public NodeStatistics[][] getEdgeNodeStatistics() {
        NodeStatistics[][] stats = new NodeStatistics[specialNodes.length][];

        for (int i = 0; i < stats.length; i++)
            stats[i] = specialNodes[i].getStatistics();

        return stats;
    }

    /**
     * Returns an array containing all statistics of all Junctions. The
     * index in the array corresponds to (Junction_id - edgeNodes.length).
     *
     * @return DOCUMENT ME!
     */
    public NodeStatistics[][] getJunctionStatistics() {
        NodeStatistics[][] stats = new NodeStatistics[junctions.length][];

        for (int i = 0; i < stats.length; i++)
            stats[i] = junctions[i].getStatistics();

        return stats;
    }

    /**
     * Calculates the total size of this infrastructure and adds a
     * small border
     *
     * @return DOCUMENT ME!
     */

    // TODO needs updating to move turn coords
    private Dimension calcSize() {
        Rectangle rect = new Rectangle();
        Node node;
        Road[] roads;
        Point p;

        for (int i = 0; i < allNodes.length; i++) {
            node = allNodes[i];
            roads = node.getAlphaRoads();
            rect.add(node.getBounds());

            for (int j = 0; j < roads.length; j++)
                rect.add(roads[j].getBounds());
        }

        int dx = (int) ((-rect.width / 2) - rect.x);
        int dy = (int) ((-rect.height / 2) - rect.y);

        for (int i = 0; i < allNodes.length; i++) {
            p = allNodes[i].getCoord();
            p.x += dx;
            p.y += dy;
        }

        return new Dimension(rect.width + 100, rect.height + 100);
    }

    /**
     * Gets the EdgeNodes in this Infrastructure. Before using this
     * method think twice if you don't actually need the getSpecialNodes()
     * method. The underscore in the function name was added to emphasize that
     * you probably need another method now.
     *
     * @return DOCUMENT ME!
     */
    public EdgeNode[] getEdgeNodes_() {
        Enumeration e = ArrayUtils.getEnumeration(specialNodes);
        Vector result = new Vector();
        Node tmp;

        while (e.hasMoreElements()) {
            if ((tmp = (Node) e.nextElement()) instanceof EdgeNode) {
                result.add(tmp);
            }
        }

        EdgeNode[] resultArray = new EdgeNode[result.size()];
        result.toArray(resultArray);

        return resultArray;
    }

    /**
     * Gets the number of EdgeNodes in the infrastructure
     *
     * @return DOCUMENT ME!
     */
    public int getNumEdgeNodes_() {
        return getEdgeNodes_().length;
    }

    /*============================================*/
    /* Selectable                                 */
    /*============================================*/
    public boolean hasChildren() {
        return getNumNodes() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Enumeration getChildren() {
        return new ArrayEnumeration(getAllNodes());
    }

    /*============================================*/
    /* MODIFYING DATA                             */
    /*============================================*/
    /**
     * Adds a node to the infrastructure
     *
     * @param node DOCUMENT ME!
     */
    public void addNode(Node node) {
        node.setId(allNodes.length);
        allNodes = (Node[]) ArrayUtils.addElement(allNodes, node);

        if (node instanceof SpecialNode) {
            specialNodes = (SpecialNode[]) ArrayUtils.addElement(specialNodes,
                    node);
        }

        if (node instanceof Junction) {
            junctions = (Junction[]) ArrayUtils.addElement(junctions, node);
        }
    }

    /**
     * Removes a node from the infrastructure
     *
     * @param node DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void remNode(Node node) throws InfraException {
        allNodes = (Node[]) ArrayUtils.remElement(allNodes, node);

        if (node instanceof SpecialNode) {
            specialNodes = (SpecialNode[]) ArrayUtils.remElement(specialNodes,
                    node);
        }

        if (node instanceof Junction) {
            junctions = (Junction[]) ArrayUtils.remElement(junctions, node);
        }
    }

    /**
     * Resets the entire data structure to allow a new simulation to
     * start This will remove all Roadusers and set all Signs to their default
     * positions, as well as reset all cycleMoved and cycleAsked counters.
     *
     * @see Node#reset()
     */
    public void reset() {
        CustomFactory.reset();

        for (int i = 0; i < allNodes.length; i++)
            allNodes[i].reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public void cachInboundLanes() throws InfraException {
        int num_nodes = allNodes.length;
        allLanes = new Vector(num_nodes * 3);

        Drivelane[] temp;
        int num_temp;

        for (int i = 0; i < num_nodes; i++) {
            temp = allNodes[i].getInboundLanes();
            num_temp = temp.length;

            for (int j = 0; j < num_temp; j++)
                allLanes.add(temp[j]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Vector getAllInboundLanes() throws InfraException {
        if (allLanes == null) {
            cachInboundLanes();
        }

        return (Vector) allLanes.clone();
    }

    /*============================================*/
    /* LOAD AND SAVE                              */
    /*============================================*/
    public void prepareSave() throws TrafficException {
        cachInboundLanes();
        size = calcSize();
    }

    /**
     * DOCUMENT ME!
     *
     * @param myElement DOCUMENT ME!
     * @param loader DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLInvalidInputException DOCUMENT ME!
     */
    public void load(XMLElement myElement, XMLLoader loader)
        throws XMLTreeException, IOException, XMLInvalidInputException { // Load parameters
        title = myElement.getAttribute("title").getValue();
        author = myElement.getAttribute("author").getValue();
        comments = myElement.getAttribute("comments").getValue();
        size = new Dimension(myElement.getAttribute("width").getIntValue(),
                myElement.getAttribute("height").getIntValue());

        allLanes = (Vector) XMLArray.loadArray(this, loader);
        allNodes = (Node[]) XMLArray.loadArray(this, loader);
        specialNodes = new SpecialNode[myElement.getAttribute(
                "num-specialnodes").getIntValue()];
        junctions = new Junction[allNodes.length - specialNodes.length];
        copySpecialNodes();
        copyJunctions();

        // Internal second stage load of child objects
        Dictionary mainDictionary;

        try {
            mainDictionary = getMainDictionary();
        } catch (InfraException e) {
            throw new XMLInvalidInputException(
                "Problem with internal 2nd stage load of infra :" + e);
        }

        XMLUtils.loadSecondStage(allLanes.elements(), mainDictionary);
        XMLUtils.loadSecondStage(ArrayUtils.getEnumeration(allNodes),
            mainDictionary);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLElement saveSelf() {
        XMLElement result = new XMLElement("infrastructure");
        result.addAttribute(new XMLAttribute("title", title));
        result.addAttribute(new XMLAttribute("author", author));
        result.addAttribute(new XMLAttribute("comments", comments));
        result.addAttribute(new XMLAttribute("height", size.height));
        result.addAttribute(new XMLAttribute("width", size.width));
        result.addAttribute(new XMLAttribute("file-version", version));
        result.addAttribute(new XMLAttribute("num-specialnodes",
                specialNodes.length));
        laneDictionary = (Hashtable) (getLaneSignDictionary());

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param saver DOCUMENT ME!
     *
     * @throws XMLTreeException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public void saveChilds(XMLSaver saver)
        throws XMLTreeException, IOException, XMLCannotSaveException {
        XMLArray.saveArray(allLanes, this, saver, "lanes");
        XMLArray.saveArray(allNodes, this, saver, "nodes");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".infrastructure";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    public Dictionary getMainDictionary() throws InfraException {
        Dictionary result = new Hashtable();
        result.put("lane", getLaneSignDictionary());
        result.put("node", getNodeDictionary());
        result.put("road", getRoadDictionary());

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Dictionary getLaneSignDictionary() {
        Dictionary result = new Hashtable();
        Enumeration lanes = allLanes.elements();
        Drivelane tmp;

        while (lanes.hasMoreElements()) {
            tmp = (Drivelane) (lanes.nextElement());
            result.put(new Integer(tmp.getId()), tmp);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Dictionary getNodeDictionary() {
        Dictionary result = new Hashtable();
        Enumeration nodes = new ArrayEnumeration(allNodes);
        Node tmp;

        while (nodes.hasMoreElements()) {
            tmp = (Node) (nodes.nextElement());
            result.put(new Integer(tmp.getId()), tmp);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InfraException DOCUMENT ME!
     */
    protected Dictionary getRoadDictionary() throws InfraException {
        Dictionary result = new Hashtable();
        Enumeration nodes = new ArrayEnumeration(allNodes);
        Enumeration roads;
        Node tmp;
        Road road;

        while (nodes.hasMoreElements()) {
            tmp = (Node) (nodes.nextElement());

            if (tmp instanceof SpecialNode) {
                addAlphaRoads(result, (SpecialNode) (tmp));
            } else if (tmp instanceof Junction) {
                addAlphaRoads(result, (Junction) (tmp));
            } else {
                throw new InfraException("Unknown type of node : " +
                    tmp.getName());
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     */
    protected void copySpecialNodes() {
        int specialNodePos = 0;

        for (int t = 0; t < allNodes.length; t++) {
            if (allNodes[t] instanceof SpecialNode) {
                specialNodes[specialNodePos++] = (SpecialNode) (allNodes[t]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void copyJunctions() {
        int junctionPos = 0;

        for (int t = 0; t < allNodes.length; t++) {
            if (allNodes[t] instanceof Junction) {
                junctions[junctionPos++] = (Junction) (allNodes[t]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    protected void addAlphaRoads(Dictionary d, SpecialNode n) {
        if (n.getAlpha()) {
            d.put(new Integer(n.getRoad().getId()), n.getRoad());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    protected void addAlphaRoads(Dictionary d, Junction n) {
        Enumeration roads = new ArrayEnumeration(n.getAlphaRoads());

        while (roads.hasMoreElements()) {
            Road road = (Road) (roads.nextElement());
            d.put(new Integer(road.getId()), road);
        }
    }
}
