/*
 * AtlasModel.java
 *
    Copyright (C) 2005  Brandon Wegge and Herb Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */
package org.jscience.physics.solids;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import org.jdom.input.SAXBuilder;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;

import javax.media.j3d.BranchGroup;


/**
 * 
DOCUMENT ME!
 *
 * @author Wegge
 */
public class AtlasModel {
    // static Logger AtlasLogger = Logger.getLogger((AtlasModel.class).getName());
    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private AtlasPreferences pref;

    /** Internal HASH that stores all objects. */
    private HashMap hash = new HashMap();

    /**
     * Creates a new AtlasModel object.
     */
    public AtlasModel() {
    }

/**
     * Creates a new instance of AtlasModel.
     */
    public AtlasModel(String name) {
        setName(name);
        setPref(new AtlasPreferences());
    }

/**
     * Creates a new instance of AtlasModel.
     */
    public AtlasModel(String name, AtlasPreferences prefer) {
        setName(name);
        setPref(prefer);
    }

    /**
     * Returns the name of the model.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the model.
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds the specified object to this model.  If there is an
     * existing entity with the same ID and TYPE, then it is replaced.
     *
     * @param o DOCUMENT ME!
     */
    public void addObject(AtlasObject o) {
        String type = o.getType();
        HashMap subhash = (HashMap) hash.get(type);

        if (subhash == null) {
            subhash = new HashMap();
            hash.put(type, subhash);
        }

        subhash.put(o.getId(), o);
        o.setParentModel(this);

        //AtlasLogger.info( o.getType() + " " + o.getId() + " added to model.");
    }

    /**
     * Returns all of the types in this model.
     *
     * @return DOCUMENT ME!
     */
    public String[] getAllObjectTypes() {
        String[] keys = (String[]) hash.keySet().toArray(new String[0]);

        return keys;
    }

    /**
     * Returns all of the entities in the model.
     *
     * @return DOCUMENT ME!
     */
    public AtlasObject[] getAllObjects() {
        ArrayList ret = new ArrayList();

        for (Iterator iter = hash.values().iterator(); iter.hasNext();) {
            HashMap subhash = (HashMap) iter.next();

            for (Iterator jter = subhash.values().iterator(); jter.hasNext();) {
                ret.add(jter.next());
            }
        }

        return (AtlasObject[]) ret.toArray(new AtlasObject[0]);
    }

    /**
     * Returns all of the entities of a specified type.
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasObject[] getAllObjects(String type) {
        HashMap subhash = (HashMap) hash.get(type);

        if (subhash == null) {
            return new AtlasObject[0];
        }

        return (AtlasObject[]) subhash.values().toArray(new AtlasObject[0]);
    }

    /**
     * Returns the object, or null if ot cannot be found
     *
     * @param type DOCUMENT ME!
     * @param id DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasObject getObject(String type, String id) {
        HashMap subhash = (HashMap) hash.get(type);

        if (subhash == null) {
            return null;
        }

        return (AtlasObject) subhash.get(id);
    }

    /**
     * Convenience method to print out information about the model.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String ret = " \n\n";
        ret = ret + "---------------------------------\n";
        ret = ret + " ATLAS Model Summary: " + name + "\n";
        ret = ret + "---------------------------------\n";

        AtlasObject[] objs = this.getAllObjects();
        Arrays.sort(objs);

        for (int i = 0; i < objs.length; i++) {
            ret = ret + objs[i].toString() + "\n";
        }

        ret = ret + "---------------------------------\n";
        ret = ret + " End of Model Summary \n";
        ret = ret + "---------------------------------\n";

        return ret;
    }

    /**
     * Returns all of the nodes in the model.
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode[] getNodes() {
        HashMap nodeHash = (HashMap) hash.get(AtlasNode.TYPE);

        if (nodeHash == null) {
            return new AtlasNode[0];
        }

        return (AtlasNode[]) nodeHash.values().toArray(new AtlasNode[0]);
    }

    /**
     * Retursn the specified node, or null if it cannot be found.
     *
     * @param nid DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasNode getNode(String nid) {
        return (AtlasNode) getObject(AtlasNode.TYPE, nid);
    }

    /**
     * Returns all finite element objects in this model. This is not
     * terribly efficient yet...
     *
     * @return DOCUMENT ME!
     */
    public AtlasElement[] getElements() {
        AtlasObject[] entities = this.getAllObjects();
        ArrayList ret = new ArrayList();

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] instanceof AtlasElement) {
                ret.add(entities[i]);
            }
        }

        return (AtlasElement[]) ret.toArray(new AtlasElement[0]);
    }

    /**
     * Returns all applied load objects in this model. This is not
     * terribly efficient yet...
     *
     * @return DOCUMENT ME!
     */
    public AtlasLoad[] getLoads() {
        AtlasObject[] entities = this.getAllObjects();
        ArrayList ret = new ArrayList();

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] instanceof AtlasLoad) {
                ret.add(entities[i]);
            }
        }

        return (AtlasLoad[]) ret.toArray(new AtlasLoad[0]);
    }

    /**
     * Returns all constraint objects in this model. This is not
     * terribly efficient yet...
     *
     * @return DOCUMENT ME!
     */
    public AtlasConstraint[] getConstraints() {
        AtlasObject[] entities = this.getAllObjects();
        ArrayList ret = new ArrayList();

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] instanceof AtlasConstraint) {
                ret.add(entities[i]);
            }
        }

        return (AtlasConstraint[]) ret.toArray(new AtlasConstraint[0]);
    }

    /**
     * Returns all material objects in this model. This is not terribly
     * efficient yet...
     *
     * @return DOCUMENT ME!
     */
    public AtlasMaterial[] getMaterials() {
        AtlasObject[] entities = this.getAllObjects();
        ArrayList ret = new ArrayList();

        for (int i = 0; i < entities.length; i++) {
            if (entities[i] instanceof AtlasMaterial) {
                ret.add(entities[i]);
            }
        }

        return (AtlasMaterial[]) ret.toArray(new AtlasMaterial[0]);
    }

    /**
     * Returns material specified by id, or null if it cannot be found.
     *
     * @param matId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AtlasMaterial getMaterial(String matId) {
        AtlasMaterial[] mats = getMaterials();

        for (int i = 0; i < mats.length; i++) {
            if (mats[i].getId().equals(matId)) {
                return mats[i];
            }
        }

        return null;
    }

    /**
     * Returns populated solution matrices.
     *
     * @return DOCUMENT ME!
     */
    public SolutionMatrices getSolutionMatrices() {
        try {
            SolutionMatrices ret = new SolutionMatrices(getNodes());

            //AtlasLogger.info(" Assemble Element Stiffness Arrays: ");
            //Add all element contributions
            AtlasElement[] elements = this.getElements();

            for (int i = 0; i < elements.length; i++) {
                elements[i].contributeMatrices(ret);
            }

            //AtlasLogger.info(" Assemble Load Arrays: ");
            //Add all load contributions
            AtlasLoad[] loads = this.getLoads();

            for (int i = 0; i < loads.length; i++) {
                loads[i].contributeLoad(ret);
            }

            //AtlasLogger.info(" Assemble Constraints: ");
            //Add all consraints
            AtlasConstraint[] constraints = this.getConstraints();

            for (int i = 0; i < constraints.length; i++) {
                constraints[i].contributeConstraint(ret);
            }

            return ret;
        } catch (AtlasException err) {
            //AtlasLogger.debug(err.getMessage());
            return null;
        }
    }

    /**
     * Removes all enitites from the model.
     */
    public void clear() {
        hash.clear();

        //AtlasLogger.info( "AtlasModel has been emptied. ");
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeXML(String filename) throws IOException {
        //Try to open file
        File f = new File(filename);
        FileOutputStream fout = new FileOutputStream(f, false);

        Element root = new Element("AtlasModel");
        root.setAttribute("Name", this.getName());

        //Add all children
        AtlasObject[] children = this.getAllObjects();
        Arrays.sort(children); //SO that there is a logical output order

        for (int i = 0; i < children.length; i++) {
            Element e = children[i].loadJDOMElement();

            if (e != null) {
                root.addContent(e);
            }
        }

        //Load up JDOM and output to file
        Document doc = new Document(root);
        XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
        xml.output(doc, fout);
        fout.close();
    }

    /**
     * Loads a model from an XML file.  NOTE: this will clear the model
     * out... this might change in the future- I'm just trying to get
     * something to work right now.
     *
     * @param filename DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void readXML(String filename) throws IOException {
        //AtlasLogger.info("Reading in model " + filename );
        try {
            File f = new File(filename);

            SAXBuilder xml = new SAXBuilder();
            Document doc = xml.build(f);
            Element root = doc.getRootElement();

            //Clear out this model
            this.clear();

            String name = root.getAttributeValue("Name");
            this.setName(name);

            //Load all of the entites up, in the order prescribed.
            String[] unloadOrder = {
                    "org.jscience.physics.solids.AtlasMaterial",
                    "org.jscience.physics.solids.AtlasNode",
                    "org.jscience.physics.solids.AtlasElement",
                    "org.jscience.physics.solids.AtlasLoad",
                    "org.jscience.physics.solids.AtlasConstraint",
                    "org.jscience.physics.solids.AtlasSolution",
                };

            List children = root.getChildren();

            for (int citer = 0; citer < unloadOrder.length; citer++) {
                for (Iterator iter = children.iterator(); iter.hasNext();) {
                    Element child = (Element) iter.next();
                    String classname = child.getName();

                    //Try to instantiate this class
                    try {
                        Class classType = Class.forName(unloadOrder[citer]);
                        Class c = Class.forName(classname);

                        if (classType.isAssignableFrom(c)) {
                            try {
                                Class[] cargs = new Class[2];
                                cargs[0] = this.getClass();
                                cargs[1] = Class.forName("org.jdom.Element");

                                Method factory = c.getMethod("unloadJDOMElement",
                                        cargs);

                                Object[] oargs = new Object[2];
                                oargs[0] = this;
                                oargs[1] = child;

                                Object ret = factory.invoke(null, oargs);

                                if (ret != null) {
                                    this.addObject((AtlasObject) ret);
                                }
                            } catch (NoSuchMethodException ie) {
                                //AtlasLogger.error(ie.getMessage());
                                //AtlasLogger.error(ie.getStackTrace());
                            } catch (IllegalAccessException iae) {
                                //AtlasLogger.error(iae.getMessage());
                                //AtlasLogger.error(iae.getStackTrace());
                            } catch (InvocationTargetException iae) {
                                //AtlasLogger.error(iae.getMessage());
                                //AtlasLogger.error(iae.getStackTrace());
                            }
                        }
                    } catch (ClassNotFoundException cnfe) {
                        //AtlasLogger.error(cnfe.getMessage());
                    }
                }
            }
        } catch (JDOMException jde) {
            throw new IOException(jde.getMessage());
        }

        //AtlasLogger.info("Finished reading in model.");
        return;
    }

    /**
     * Method to test reading in an XML file.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            AtlasPreferences pref = new AtlasPreferences();
            AtlasModel fem = new AtlasModel("Twmp", pref);
            fem.readXML("C:\\temp\\model.xml");
            fem.writeXML("C:\\temp\\model2.xml");

            System.out.println(fem.toString());
        } catch (IOException io) {
            io.printStackTrace();
        }

        //AtlasLogger.info("Done");
    }

    /**
     * Returns BranchGroup needed to visualize this model.
     *
     * @param geometryRoot DOCUMENT ME!
     */
    public void populateGeometry(BranchGroup geometryRoot) {
        AtlasObject[] objects = this.getAllObjects();

        for (int i = 0; i < objects.length; i++) {
            objects[i].populateGeometry(geometryRoot);
        }
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return Returns the pref.
     */
    public AtlasPreferences getPref() {
        return pref;
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param pref The pref to set.
     */
    public void setPref(AtlasPreferences pref) {
        this.pref = pref;
    }
}
