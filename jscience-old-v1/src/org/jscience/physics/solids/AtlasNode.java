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

package org.jscience.physics.solids;

import org.jdom.Element;

import org.jscience.physics.solids.geom.AtlasPosition;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;

import javax.vecmath.Color3f;


/**
 * Position object for elements, loads, etc...
 *
 * @author Wegge
 */
public class AtlasNode extends AtlasPosition {
    /**
     * DOCUMENT ME!
     */
    public static String TYPE = "Node";

    /**
     * DOCUMENT ME!
     */
    private int numDOF = 6;

/**
     * Creates a new Node at global location (x,y,z)
     */
    public AtlasNode(String id, double x, double y, double z) {
        super(x, y, z);
        setId(id);
    }

    /**
     * Returns type as "Node".
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Sets the number of Degrees of Freedom for this node.  Default is
     * 6. This value can be changed for different classes of problems.
     *
     * @param num DOCUMENT ME!
     */
    public void setNumberDOF(int num) {
        this.numDOF = num;
    }

    /**
     * Returns the number of Degrees of Freedom for the node.  Default
     * is 6.
     *
     * @return DOCUMENT ME!
     */
    public int getNumberDOF() {
        return numDOF;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Element loadJDOMElement() {
        Element ret = new Element(this.getClass().getName());
        ret.setAttribute("Id", this.getId());

        double[] loc = this.getGlobalPosition();
        ret.setAttribute("Position", AtlasUtils.convertDoubles(loc));

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static AtlasObject unloadJDOMElement(AtlasModel parent, Element e) {
        String id = e.getAttributeValue("Id");

        double[] loc = AtlasUtils.convertStringtoDoubles(e.getAttributeValue(
                    "Position"));

        return new AtlasNode(id, loc[0], loc[1], loc[2]);
    }

    /**
     * Returns the appearance for this node.
     *
     * @return DOCUMENT ME!
     */
    public Appearance getAppearance() {
        Color3f ambientBlue = new Color3f(0.0f, 0.02f, 0.5f);
        Color3f white = new Color3f(1, 1, 1);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.00f, 0.20f, 0.80f);
        Color3f specular = new Color3f(0.7f, 0.7f, 0.7f);

        Material targetMaterial = new Material();
        targetMaterial.setAmbientColor(ambientBlue);
        targetMaterial.setDiffuseColor(blue);
        targetMaterial.setSpecularColor(specular);
        targetMaterial.setShininess(75.0f);
        targetMaterial.setLightingEnable(true);

        Appearance blue_appearance = new Appearance();
        blue_appearance.setMaterial(targetMaterial);

        PolygonAttributes targetPolyAttr = new PolygonAttributes();
        targetPolyAttr.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        blue_appearance.setPolygonAttributes(targetPolyAttr);

        return blue_appearance;
    }

    /**
     * Draws a sphere at the node position.
     *
     * @param geometryRoot DOCUMENT ME!
     */
    public void populateGeometry(BranchGroup geometryRoot) {
        return;

        /*
        Sphere geom = new Sphere( .01f, this.getAppearance() );
        
        
        Vector3d loc = new Vector3d(this.getGlobalPosition());
        Matrix3d m = new Matrix3d();
        m.setIdentity();
        
        Transform3D t3d = new Transform3D( m,loc,1.0);
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(geom);
        
        geometryRoot.addChild( tg );
        
         */
    }
}
