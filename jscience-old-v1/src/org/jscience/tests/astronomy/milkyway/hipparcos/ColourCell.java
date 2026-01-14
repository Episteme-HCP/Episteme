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

// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
package org.jscience.tests.astronomy.milkyway.hipparcos;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import org.jscience.astronomy.catalogs.hipparcos.Star3D;

import java.awt.*;

import javax.media.j3d.*;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ColourCell extends Canvas3D {
    /** DOCUMENT ME! */
    public static final float swidth = 0.2f;

    /** DOCUMENT ME! */
    final static Font3D font3d = new Font3D(new Font("Times", Font.PLAIN, 8),
            new FontExtrusion());

    /** DOCUMENT ME! */
    private BranchGroup scene;

    /** DOCUMENT ME! */
    private TransformGroup objTrans;

    /** DOCUMENT ME! */
    private SimpleUniverse uni = null;

    /** DOCUMENT ME! */
    private int scale = 2;

/**
     * Creates a new ColourCell object.
     */
    public ColourCell() {
        super(null);
        scene = createSceneGraph();
        uni = new SimpleUniverse(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();
        objTrans = new TransformGroup();

        objRoot.addChild(objTrans);

        // Set up the background
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                50.0);

        Color3f bgColor = new Color3f(0.01f, 0.01f, 0.1f);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        objRoot.addChild(bgNode);

        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight();
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(0.8f, 0.8f, 0.8f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        return objRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ap DOCUMENT ME!
     */
    public void showCol(Appearance ap) {
        objTrans.addChild(new Sphere(swidth, Star3D.flags, 15, ap));

        scene.compile();
        uni.addBranchGraph(scene);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        uni.getViewingPlatform().setNominalViewingTransform();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return new Dimension(20, 30);
    }
}
