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

import org.jscience.architecture.traffic.xml.*;

import java.awt.*;

import java.io.IOException;


/**
 * Basic turn. A turn may later be sophisticated with bends in the road.
 *
 * @author Group Datastructures
 * @version 1.0
 */
public class Turn implements XMLSerializable {
    /** DOCUMENT ME! */
    public static final int TURNPOINT = 1;

    /** DOCUMENT ME! */
    public static final int CONTROLPOINT = 2;

    /** The coordinates of this turn */
    protected Point point;

    /** The angle of this turn */
    protected double angle;

    /** DOCUMENT ME! */
    protected String parentName = "model.infrastructure.node.road";

    /** DOCUMENT ME! */
    protected int type;

    /** The position (index) of this turn on the road */
    protected double position;

/**
     * Creates a new Turn object.
     *
     * @param p DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public Turn(Point p, double a) {
        point = p;
        angle = a;
    }

/**
     * Creates a new Turn object.
     *
     * @param p DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param t DOCUMENT ME!
     */
    public Turn(Point p, double a, int t) {
        point = p;
        angle = a;
        type = 1;
    }

/**
     * Creates a new Turn object.
     *
     * @param p DOCUMENT ME!
     * @param t DOCUMENT ME!
     */
    public Turn(Point p, int t) {
        point = p;
        type = t;
    }

/**
     * Creates a new Turn object.
     */
    public Turn() {
    }

    /**
     * Returns the coordinates of this turn
     *
     * @return DOCUMENT ME!
     */
    public Point getCoord() {
        return point;
    }

    /**
     * Sets the coordinates of this turn
     *
     * @param p DOCUMENT ME!
     */
    public void setCoord(Point p) {
        point = p;
    }

    /**
     * Returns the angle of this turn
     *
     * @return DOCUMENT ME!
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Sets the angle of this turn
     *
     * @param a DOCUMENT ME!
     */
    public void setAngle(double a) {
        angle = a;
    }

    /**
     * Returns the type of this turn
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the position of this turn on the road
     *
     * @return DOCUMENT ME!
     */
    public double getPosition() {
        return position;
    }

    /**
     * Sets the position of this turn on the road
     *
     * @param p DOCUMENT ME!
     */
    public void setPosition(double p) {
        position = p;
    }

    // XML Serializable implementation
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
        throws XMLTreeException, IOException, XMLInvalidInputException {
        point = new Point(myElement.getAttribute("x-pos").getIntValue(),
                myElement.getAttribute("y-pos").getIntValue());
        angle = myElement.getAttribute("angle").getDoubleValue();
        position = myElement.getAttribute("rel-pos").getDoubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws XMLCannotSaveException DOCUMENT ME!
     */
    public XMLElement saveSelf() throws XMLCannotSaveException {
        XMLElement result = new XMLElement("turn");
        result.addAttribute(new XMLAttribute("x-pos", (int) (point.getX())));
        result.addAttribute(new XMLAttribute("y-pos", (int) (point.getY())));
        result.addAttribute(new XMLAttribute("angle", angle));
        result.addAttribute(new XMLAttribute("rel-pos", position));

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
        throws XMLTreeException, IOException, XMLCannotSaveException { // A turn has no childs
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXMLName() {
        return parentName + ".turn";
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentName DOCUMENT ME!
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
