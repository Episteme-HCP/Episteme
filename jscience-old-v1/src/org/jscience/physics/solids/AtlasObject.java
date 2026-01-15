/*
 * AtlasObject.java
 *
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

import org.jdom.Element;

import javax.media.j3d.BranchGroup;

/**
 * Abstract object that everything that resides in an AtlasModel must extend.
 * <p/>
 * There are two attributes of an AtlasObject that are important, the id and the type of the object.
 * These are used to store and retrieve the object from an AtlasModel.
 *
 * @author Wegge
 */
public abstract class AtlasObject implements Comparable {

    private String id;

    private AtlasModel parent;


    /**
     * Sets the identifier of the object.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the id of the object.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the type of the object.  The type returned will be specific to each object that
     * extends this class.
     */
    public abstract String getType();

    /**
     * Convenience method to dump information about the object.
     */
    public String toString() {
        String ret = getType() + " " + getId();
        return ret;
    }


    /**
     * Sets the model that owns this object.
     */
    public void setParentModel(AtlasModel parent) {
        this.parent = parent;
    }

    /**
     * Returns the parent model, or null if this is a standalone object.
     */
    public AtlasModel getParentModel() {
        return this.parent;
    }

    /**
     * Compares two objects for sorting.  If object types are different, returns the alpha
     * compariaon of the type.  If the types are the same, this method will try to do a numerical
     * comparison between the id- failing that, it reverts to alpha.  This approach prevents "Node 2"
     * from falling after "Node 10".
     */
    public int compareTo(Object rhs) {

        if (rhs instanceof AtlasObject) {

            AtlasObject arhs = (AtlasObject) rhs;

            if (this.getType().equals(arhs.getType())) {
                try {
                    Integer lhs_int = Integer.valueOf(this.getId());
                    Integer rhs_int = Integer.valueOf(arhs.getId());
                    return lhs_int.compareTo(rhs_int);
                } catch (NumberFormatException nfe) {
                }

            } else {
                return this.getType().compareTo(arhs.getType());
            }
        }

        return this.toString().compareTo(rhs.toString());
    }

    /**
     * Checks for equality.
     */
    public boolean equals(Object rhs) {
        return this.toString().equals(rhs.toString());
    }

    /**
     * Method to load e JDOM element up with information.  This should be ovverriden
     * by subclasses.
     */
    public Element loadJDOMElement() {
        return null;
    }

    /**
     * Method to marshall AtlasObjects from the XML.  This must be overridden
     * by subclasses.
     */
    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {
        return null;
    }

    /**
     * Adds geometry contributions to the scene graph.  This only needs to
     * be overridden if this object needs to be displayed.
     */
    public void populateGeometry(BranchGroup geometryRoot) {

    }
}
