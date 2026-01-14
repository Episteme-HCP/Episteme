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

package org.jscience.astronomy.catalogs.hipparcos;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.util.Iterator;

//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl

/*
        Sky3D some stuff in 3d
        Ripped off from one of suns demos mainly (Bilboard).
 */

public class HipparcosSky3D extends Canvas3D {
    private TransformGroup objTrans;
    private TransformGroup objScale;
    private BranchGroup scene;
    private SimpleUniverse uni = null;
    private Vector3d center;
    protected MouseZoom zoom;
    protected RotationInterpolator rotator;
    protected double alpha;
    protected double delta;
    protected double tol;
    protected double tolsquared;

    /**
     * Creates a new Sky3D object.
     */
    public HipparcosSky3D() {
        super(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getStars() {
        return new GroupIterator(objTrans.getAllChildren());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RotationInterpolator getRotationInterpolator() {
        return rotator;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);

        // Create the transform group node and initialize it to the
        // identity.  Enable the TRANSFORM_WRITE capability so that
        // our behavior code can modify it at runtime.  Add it to the
        // root of the subgraph.
        objScale = new TransformGroup();
        objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        objScale.addChild(objTrans);
        objRoot.addChild(objScale);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                120.0);

        // Set up the background
        Color3f bgColor = new Color3f(0.05f, 0.05f, 0.5f);
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
        Color3f light2Color = new Color3f(0.3f, 0.3f, 0.3f);
        Vector3f light2Direction = new Vector3f(-6.0f, -2.0f, -1.0f);

        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(light2Color,
                light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);

        // Create the rotate behavior node
        //MouseRotateY behavior = new MouseRotateY();
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(objTrans);
        objTrans.addChild(behavior);
        behavior.setSchedulingBounds(bounds);

        // Create the zoom behavior node
        zoom = new MouseZoom();
        zoom.setTransformGroup(objTrans);
        objTrans.addChild(zoom);
        zoom.setSchedulingBounds(bounds);

        // Create the translate behavior node
        MouseTranslate behavior3 = new MouseTranslate();
        behavior3.setTransformGroup(objTrans);
        objTrans.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);

        // Click on star behaviour
        //clicker = new ClickBehaviour(objRoot, this, bounds,PickObject.USE_GEOMETRY);
        //objRoot.addChild(clicker);
        //if (starPanel!=null) clicker.setStarPanel(starPanel);
        // Auto rotator
        Alpha mover = new Alpha(0, 4000);
        rotator = new RotationInterpolator(mover, objTrans);
        rotator.setSchedulingBounds(bounds);
        objTrans.addChild(rotator);

        //rotator.setAlpha(null);
        return objRoot;
    }

    /**
     * public void setStarPanel(StarPanel p) {
     * <p/>
     * this.starPanel = p;
     * if (clicker != null) {
     * clicker.setStarPanel(p);
     * }
     * <p/>
     * }
     */
    public void setupScene() {
        if (scene != null) {
            scene.detach();
            scene = null;
        }

        scene = createSceneGraph();

        if (uni == null) {
            uni = new SimpleUniverse(this);
        }
    }

    protected void setScale(double d) {
        Transform3D scale = new Transform3D();
        float sc = (float) (0.6 / (d));
        scale.setScale(sc);
        System.out.println("Scale " + sc + " d is " + d);
        objScale.setTransform(scale);
        zoom.setFactor(d / 10);
    }

    protected void setCenter(double ra, double dec, double d) {
        alpha = ra;
        delta = dec;
        this.center = makeVecParsec(ra, dec, d);

        Transform3D orig = new Transform3D();
        orig.set(this.center);
        objTrans.setTransform(orig);
        System.out.println("SetCenter " + center + "using " + ra + " " + dec +
                " " + d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector3d center() {
        return this.center;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ra  DOCUMENT ME!
     * @param dec DOCUMENT ME!
     * @param d   DOCUMENT ME!
     * @param par DOCUMENT ME!
     */
    public void setupScene(double ra, double dec, double d, double par) {
        setupScene();
        setScale(par);
        alpha = ra;
        delta = dec;
        tol = par;
        tolsquared = par * par;

        this.center = makeVecParsec(ra, dec, d);

        //setCenter(ra,dec,d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param alpha DOCUMENT ME!
     * @param delta DOCUMENT ME!
     * @param d     DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    static public Vector3d makeVecParsec(double alpha, double delta, double d) {
        //System.out.println ("alpha:"+alpha +" delta:"+delta+" d:"+d );
        double arad = Math.toRadians(alpha);
        double drad = Math.toRadians(delta);
        double cd = Math.cos(drad);
        double sd = Math.sin(drad);
        double sa = Math.sin(arad);
        double ca = Math.cos(arad);

        //System.out.println ("cos delta "+cd +" sd:"+sd+" ca:"+ca+" sa:"+sa );
        float x = (float) (d * cd * ca);
        float y = (float) (d * cd * sa);
        float z = (float) (d * sd);
        Vector3d vec = new Vector3d(x, y, z);

        //System.out.println ("x:"+x +" y:"+y+" z:"+z+"  as vec -> "+vec );
        return vec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param star DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    static public Vector3d makeVec(HipparcosCatalogEntry star) {
        double d = 1000 / star.getParalax();

        return makeVecParsec(star.getAlpha(), star.getDelta(), d);
    }

    /**
     * DOCUMENT ME!
     */
    public void showScene() {
        scene.compile();
        uni.addBranchGraph(scene);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        uni.getViewingPlatform().setNominalViewingTransform();
    }

    /**
     * Add the given Star to the scene
     */
    public void addStar(HipparcosCatalogEntry star) {
        Vector3d spos = makeVec(star);
        spos.sub(this.center);

        double x2y2z2 = Math.pow(spos.x, 2) + Math.pow(spos.y, 2) +
                Math.pow(spos.z, 2);

        if (x2y2z2 < tolsquared) {
            //if (Constants.verbose > 3)
            //    System.out.println("Star at "+spos+ " size "+Star3D.calcSize(star));
            Transform3D mat = new Transform3D();
            TransformGroup g = new TransformGroup(mat);
            g.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
            mat.set(spos);
            g.setTransform(mat);
            g.addChild(new HipparcosStar(star).getGroup());  //TODO check this line
            objTrans.addChild(g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getDelta() {
        return delta;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTol() {
        return tol;
    }
}
