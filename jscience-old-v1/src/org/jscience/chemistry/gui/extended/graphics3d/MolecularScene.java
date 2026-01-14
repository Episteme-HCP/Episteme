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

package org.jscience.chemistry.gui.extended.graphics3d;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;

import org.jscience.chemistry.gui.extended.molecule.Atom;
import org.jscience.chemistry.gui.extended.molecule.AtomVector;
import org.jscience.chemistry.gui.extended.molecule.Molecule;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.media.j3d.*;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MolecularScene {
    /** DOCUMENT ME! */
    Canvas3D canvas;

    /** DOCUMENT ME! */
    VirtualUniverse universe;

    /** DOCUMENT ME! */
    Locale locale;

    /** DOCUMENT ME! */
    View view;

    /** DOCUMENT ME! */
    Vector others = new Vector();

    /** DOCUMENT ME! */
    Hashtable niceNodes;

    /** DOCUMENT ME! */
    Hashtable fastNodes;

    /** DOCUMENT ME! */
    TransformGroup sceneTrans;

    /** DOCUMENT ME! */
    TransformGroup sceneOffset;

    /** DOCUMENT ME! */
    Switch sceneSwitch;

    /** DOCUMENT ME! */
    BranchGroup fastGroup;

    /** DOCUMENT ME! */
    BranchGroup niceGroup;

    /** DOCUMENT ME! */
    BranchGroup root = null;

    /** DOCUMENT ME! */
    TransformGroup rootTrans = null;

    /** DOCUMENT ME! */
    BranchGroup localeRoot = null;

    /** DOCUMENT ME! */
    BoundingSphere bounds = null;

    /** DOCUMENT ME! */
    LinearFog fog = null;

    /** DOCUMENT ME! */
    RenderTable rTable = RenderTable.getTable();

    /** DOCUMENT ME! */
    boolean isFast = false;

    /** DOCUMENT ME! */
    boolean isWire = false;

/**
     * Creates a new MolecularScene object.
     *
     * @param c DOCUMENT ME!
     */
    protected MolecularScene(Canvas3D c) {
        this.canvas = c;

        niceNodes = new Hashtable();
        fastNodes = new Hashtable();

        createUniverse();

        initSceneGraph();

        initLighting();

        // Attach the branch graph to the universe, via the Locale.
        // The scene graph is now live!
        root.compile();
        localeRoot.addChild(root);
        locale.addBranchGraph(localeRoot);
    }

    /**
     * DOCUMENT ME!
     */
    void createUniverse() {
        // Establish a virtual universe, with a single hi-res Locale
        universe = new VirtualUniverse();
        locale = new Locale(universe);

        // Create a PhysicalBody and Physical Environment object
        PhysicalBody body = new PhysicalBody();
        PhysicalEnvironment environment = new PhysicalEnvironment();

        // Create a View and attach the Canvas3D and the physical
        // body and environment to the view.
        view = new View();
        view.addCanvas3D(canvas);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);
        view.setBackClipDistance(500.0);

        //System.out.println("Clip front/back: "+view.getFrontClipDistance()+"/"+
        //			 view.getBackClipDistance());
        // Create a branch group node for the view platform
        root = new BranchGroup();
        localeRoot = new BranchGroup();
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        localeRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        root.setCapability(BranchGroup.ALLOW_DETACH);

        ViewPlatform vp = new ViewPlatform();
        rootTrans = new TransformGroup();

        bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);

        rootTrans.addChild(vp);

        BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
        rootTrans.addChild(boundingLeaf);
        localeRoot.addChild(rootTrans);
        view.attachViewPlatform(vp);
    }

    /**
     * DOCUMENT ME!
     */
    void initSceneGraph() {
        Transform3D t = new Transform3D();
        t.set(new Vector3f(0.0f, 0.0f, -100.0f));
        sceneOffset = new TransformGroup(t);
        sceneOffset.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneOffset.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        sceneTrans = new TransformGroup();
        sceneTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        sceneTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        sceneSwitch = new Switch(Switch.CHILD_MASK);
        sceneSwitch.setCapability(Switch.ALLOW_SWITCH_READ);
        sceneSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

        fastGroup = new BranchGroup();
        niceGroup = new BranchGroup();
        fastGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        fastGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        fastGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

        niceGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        niceGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        niceGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);

        sceneSwitch.addChild(niceGroup);
        sceneSwitch.addChild(fastGroup);
        sceneSwitch.setWhichChild(0);

        // Create the drag behavior node
        MouseFastRotate behavior = new MouseFastRotate(sceneTrans, this);
        behavior.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior);

        // Create the zoom behavior node
        MouseZoom behavior2 = new MouseZoom(sceneTrans);
        behavior2.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior2);

        // Create the zoom behavior node
        MouseTranslate behavior3 = new MouseTranslate(sceneTrans);
        behavior3.setSchedulingBounds(bounds);
        sceneTrans.addChild(behavior3);

        sceneTrans.addChild(sceneSwitch);

        sceneOffset.addChild(sceneTrans);
        rootTrans.addChild(sceneOffset);
    }

    /**
     * DOCUMENT ME!
     */
    void initLighting() {
        Color3f dlColor = new Color3f(1.0f, 1.0f, 1.0f);

        Vector3f lDirect1 = new Vector3f(1.0f, -1.0f, -1.0f);
        Vector3f lDirect2 = new Vector3f(-1.0f, 1.0f, 1.0f);

        DirectionalLight lgt1 = new DirectionalLight(dlColor, lDirect1);
        lgt1.setInfluencingBounds(bounds);

        DirectionalLight lgt2 = new DirectionalLight(dlColor, lDirect2);
        lgt2.setInfluencingBounds(bounds);

        Color3f alColor = new Color3f(0.65f, 0.65f, 0.65f);
        AmbientLight aLgt = new AmbientLight(alColor);
        aLgt.setInfluencingBounds(bounds);

        root.addChild(aLgt);
        root.addChild(lgt1);
        root.addChild(lgt2);

        // Try some fog for depth cueing
        fog = new LinearFog();
        fog.setCapability(LinearFog.ALLOW_DISTANCE_WRITE);
        fog.setColor(new Color3f(0.0f, 0.0f, 0.0f));
        fog.setFrontDistance(70.0);
        fog.setBackDistance(500.0);

        //fog.setInfluencingBounds(bounds);
        //fog.addScope(sceneTrans);
        //sceneTrans.addChild(fog);
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setRenderStyle(int style) {
        canvas.stopRenderer();

        if (style == RenderStyle.WIRE) {
            setFast();
            isWire = true;
        } else {
            isWire = false;
            rTable.setStyle(style);

            if (isFast) {
                setNice();
            }
        }

        canvas.startRenderer();
    }

    // remove everything
    /**
     * DOCUMENT ME!
     */
    public void clear() {
        detachRoot();

        // find the child
        Enumeration e = niceNodes.keys();
        Vector tmp = new Vector();

        while (e.hasMoreElements()) {
            Molecule m = (Molecule) e.nextElement();
            removeMolNode(m, niceNodes, niceGroup);
            removeMolNode(m, fastNodes, fastGroup);
        }

        niceNodes = new Hashtable();
        fastNodes = new Hashtable();
        root.compile();
        attachRoot();
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void removeMolecule(Molecule m) {
        Node n = (Node) niceNodes.get(m);

        if (m == null) {
            System.out.println("Could not find node for molecule: " + m);

            return;
        }

        detachRoot();
        removeMolNode(m, niceNodes, niceGroup);
        removeMolNode(m, fastNodes, fastGroup);
        root.compile();
        attachRoot();
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param nodes DOCUMENT ME!
     * @param group DOCUMENT ME!
     */
    void removeMolNode(Molecule m, Hashtable nodes, BranchGroup group) {
        Node n = (Node) nodes.get(m);
        int nc = group.numChildren();

        for (int i = 0; i < nc; i++) {
            if (group.getChild(i) == n) {
                group.removeChild(i);
                nodes.remove(m);

                return;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void addMolecule(Molecule m) {
        detachRoot();
        centerMolecule(m);
        setBounds(m);

        MoleculeNode node = new MoleculeNode(m);
        niceNodes.put(m, node);

        node.compile();
        niceGroup.addChild(node);

        node = new MoleculeNode(m, RenderStyle.WIRE);
        fastNodes.put(m, node);
        node.compile();
        fastGroup.addChild(node);
        root.compile();
        attachRoot();
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    void centerMolecule(Molecule m) {
        m.findBB();

        float cenX = 0.5f * (m.getXmin() + m.getXmax());
        float cenY = 0.5f * (m.getYmin() + m.getYmax());
        float cenZ = 0.5f * (m.getZmin() + m.getZmax());
        AtomVector av = m.getMyAtoms();
        int nAtoms = av.size();

        for (int i = 0; i < nAtoms; i++) {
            Atom a = av.getAtom(i);
            a.setX(a.getX() - cenX);
            a.setY(a.getY() - cenY);
            a.setZ(a.getZ() - cenZ);
        }

        m.findBB();
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    void setBounds(Molecule m) {
        float x = m.getXmax() - m.getXmin();
        float y = m.getYmax() - m.getYmin();
        float ex = Math.max(x, y);
        System.out.println("ex: " + ex + " " + m.getZmin() + " " + m.getZmax());

        float backPlane = (m.getZmax() > 150.0f) ? (m.getZmax() + 50.0f) : 150.0f;
        view.setBackClipDistance(backPlane);

        fog.setFrontDistance(0.5f * (m.getZmin() + m.getZmax()));
        fog.setBackDistance(backPlane);

        Transform3D t = new Transform3D();
        t.set(new Vector3f(0.0f, 0.0f, (m.getZmin() - (2.0f * ex))));

        //t.setScale(15.0f/ex);
        sceneOffset.setTransform(t);
    }

    /**
     * DOCUMENT ME!
     */
    void detachRoot() {
        root.detach();
    }

    /**
     * DOCUMENT ME!
     */
    void attachRoot() {
        localeRoot.addChild(root);
    }

    // Can be removed from code
    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    void addNode(Node node) {
        BranchGroup b = new BranchGroup();
        b.addChild(node);
        detachRoot();
        others.addElement(node);
        niceGroup.addChild(b);
        root.compile();
        attachRoot();
    }

    /**
     * DOCUMENT ME!
     */
    public void setFast() {
        if (!isWire) {
            sceneSwitch.setWhichChild(1);
            isFast = true;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setNice() {
        if (!isWire) {
            sceneSwitch.setWhichChild(0);
            isFast = false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFast() {
        return isFast;
    }
}
