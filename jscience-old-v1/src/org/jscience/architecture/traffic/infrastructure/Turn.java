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
