/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.growing;

import com.sun.j3d.utils.universe.SimpleUniverse;

import org.jscience.biology.lsystems.common.TextureBuilder;

import java.applet.Applet;

import java.awt.*;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import javax.media.j3d.*;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/**
 * This is the main class for the 3D scene. This class builds the main
 * frame of the scene graph and calls the plants to build itself.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Scene extends Applet {
    /** The speed of how fast the plants shall grow */
    public float m_fTimeFactor;

    /** A list of all plants in this scene */
    public Vector m_vPlants;

    /** A list of all object to be animated of all plants. */
    private Vector m_vAnimation;

/**
     * Constructor. Creates the whole 3D scene and addt the plants to it. The
     * animation is started immediately when the scene is built.
     *
     * @param vPlants     The list of plants to add to the scene
     * @param fTimeFactor The time factor of the growth speed.
     * @throws Exception an exception if received from a caled method.
     */
    public Scene(Vector vPlants, float fTimeFactor) throws Exception {
        m_vPlants = vPlants;
        m_fTimeFactor = fTimeFactor;

        //Log.debug("Building scene");
        //create canvas
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        //set layout of applet and add canvas
        setLayout(new BorderLayout());
        add("Center", canvas);

        //create view platform
        ViewPlatform vp = new ViewPlatform();
        vp.setActivationRadius(500.0f);

        PhysicalBody body = new PhysicalBody();
        PhysicalEnvironment environment = new PhysicalEnvironment();

        //create new view object and make connections to canvas, view platform, physical body and physical environment
        View view = new View();
        view.addCanvas3D(canvas);
        view.attachViewPlatform(vp);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);
        view.setBackClipDistance(100.0);

        //set initial position to a new transform group and add the view platform
        Transform3D t3DView = new Transform3D();
        t3DView.set(new Vector3f(150.0f, 10.0f, 230.0f));

        //t3DView.rotY(-Math.PI/4);
        //t3DView.rotX(-Math.PI/16);
        TransformGroup tgView = new TransformGroup(t3DView);
        tgView.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgView.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgView.addChild(vp);

        BoundingSphere bsRange = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                500.0);

        //create branch group for the view and add the transform group
        BranchGroup bgView = new BranchGroup();
        bgView.addChild(tgView);

        //
        // create the scene branch
        //
        //create transform group for the whole scene
        TransformGroup tgMouse = new TransformGroup();

        /*tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        //add mouse rotation capability for the scene
        MouseRotate mr = new MouseRotate(tgMouse);
        tgMouse.addChild(mr);
        mr.setSchedulingBounds(bsRange);
        
        //add mouse zoom capability for the scene
        MouseZoom mz = new MouseZoom(tgMouse);
        tgMouse.addChild(mz);
        mz.setSchedulingBounds(bsRange);
        
        //add mouse translation capability for the scene
        MouseTranslate mt = new MouseTranslate(tgMouse);
        tgMouse.addChild(mt);
        mt.setSchedulingBounds(bsRange);*/

        //add this if you want to move the scene with the mouse
        Appearance appPath = new Appearance();
        Material mPath = new Material(GrowingPlantsDefinitions.COLOR_DARK_BROWN,
                GrowingPlantsDefinitions.COLOR_BLACK,
                GrowingPlantsDefinitions.COLOR_BROWN,
                GrowingPlantsDefinitions.COLOR_BROWN, 10f);
        appPath.setMaterial(mPath);

        //create appearance for ground
        //a texture showing grass has been tested and is making the navigation too slow
        Appearance appGround = new Appearance();

        //Material mGround = new Material(Def.COLOR_DARK_GREEN, Def.COLOR_BLACK, Def.COLOR_BLACK, Def.COLOR_BLACK, 1f);
        //appGround.setMaterial( mGround );
        TextureBuilder tbGround = new TextureBuilder();
        Texture tGround = tbGround.buildTexture("grass.jpg");
        tGround.setMagFilter(Texture.FASTEST);
        appGround.setTexture(tGround);

        //create ground
        float[] fEdges = {
                -1000.0f, -1000.0f, 0.0f, -1000.0f, 1000.0f, 0.0f, 1000.0f,
                1000.0f, 0.0f, 1000.0f, -1000.0f, 0.0f
            };

        float[] fNormals = {
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.0f,
            };

        QuadArray qaGround = new QuadArray(4,
                QuadArray.COORDINATES | QuadArray.NORMALS |
                QuadArray.TEXTURE_COORDINATE_3);
        qaGround.setCoordinates(0, fEdges);
        qaGround.setNormals(0, fNormals);
        qaGround.setTextureCoordinates(0, fEdges);

        Shape3D s3dGround = new Shape3D(qaGround, appGround);
        Transform3D t3dGround = new Transform3D();
        t3dGround.rotX(Math.PI / 2);

        TransformGroup tgGround = new TransformGroup(t3dGround);
        tgGround.addChild(s3dGround);
        tgMouse.addChild(tgGround);

        //create path
        float[] fPath = {
                1000.0f, 0.01f, 110.0f, //1
                1000.0f, 0.01f, 90.0f, //2
                -1000.0f, 0.01f, 70.0f, -1000.0f, 0.01f, 90.0f
            };

        QuadArray qaPath = new QuadArray(4,
                QuadArray.COORDINATES | QuadArray.NORMALS); //| QuadArray.TEXTURE_COORDINATE_3);
        qaPath.setCoordinates(0, fPath);
        qaPath.setNormals(0, fNormals);

        Shape3D s3dPath = new Shape3D(qaPath, appPath);
        tgMouse.addChild(s3dPath);

        //make house
        //House house = new House(45, 30, 24);
        //TransformGroup tgHouse = house.getTransformGroup();
        //Transform3D t3dHouse = new Transform3D();
        //t3dHouse.setTranslation(new Vector3d(30.0, 0.0, 40.0));
        //tgHouse.setTransform(t3dHouse);
        //tgMouse.addChild(tgHouse);
        m_vAnimation = new Vector();

        //sort list of plants
        Collections.sort(vPlants);

        //build plants
        for (Enumeration enumeration = vPlants.elements();
                enumeration.hasMoreElements();) {
            Plant plant = (Plant) enumeration.nextElement();

            TransformGroup tgPlant = plant.build(); //appBranch,appLeaf);

            Transform3D t3dPlant = new Transform3D();
            t3dPlant.setTranslation(new Vector3d(plant.getX(), 0.0, plant.getY()));
            tgPlant.setTransform(t3dPlant);

            tgMouse.addChild(tgPlant);

            m_vAnimation.addAll(plant.getAnimationList());
        }

        //sort animation list
        Collections.sort(m_vAnimation);

        //growth behavior
        GrowthBehavior behavior = new GrowthBehavior(m_vAnimation, fTimeFactor);
        behavior.setSchedulingBounds(bsRange);
        tgMouse.addChild(behavior);

        //create branch group for the scene
        BranchGroup bgScene = new BranchGroup();

        // set the background for the scene
        TextureBuilder tbBack = new TextureBuilder();
        Background back = new Background(tbBack.buildImage("sky.jpg"));
        back.setApplicationBounds(bsRange);
        bgScene.addChild(back);

        //create light for scene
        DirectionalLight dl = new DirectionalLight(GrowingPlantsDefinitions.COLOR_WHITE,
                new Vector3f(-1.0f, -1.0f, -1.0f));
        dl.setInfluencingBounds(bsRange);
        bgScene.addChild(dl);

        DirectionalLight dl2 = new DirectionalLight(GrowingPlantsDefinitions.COLOR_WHITE,
                new Vector3f(1.0f, 1.0f, -1.0f));
        dl2.setInfluencingBounds(bsRange);
        bgScene.addChild(dl2);

        //create an ambient light
        AmbientLight al = new AmbientLight(GrowingPlantsDefinitions.COLOR_WHITE);
        al.setInfluencingBounds(bsRange);
        bgScene.addChild(al);

        //create virtual universe
        VirtualUniverse vu = new VirtualUniverse();

        //keyboard navigation
        KeyBehavior knb = new KeyBehavior(tgView);
        knb.setSchedulingBounds(bsRange);
        bgView.addChild(knb);

        //add mouse group
        bgScene.addChild(tgMouse);

        //compile scene
        bgScene.compile();

        bgView.compile();

        //create locale and add both, view and scene branches
        Locale locale = new Locale(vu);
        locale.addBranchGraph(bgScene);
        locale.addBranchGraph(bgView);
    }

    /**
     * Adds a plant to the scene.
     *
     * @param plant The plant to add.
     */
    public void addPlant(Plant plant) {
        m_vPlants.add(plant);
    }

    /**
     * Sets the new time factor for the scene.
     *
     * @param fNewFactor The new time factor for the scene.
     */
    public void setTimeFactor(float fNewFactor) {
        m_fTimeFactor = fNewFactor;
    }

    /**
     * Gets the time factor of this scene.
     *
     * @return The time factor.
     */
    public float getTimeFactor() {
        return m_fTimeFactor;
    }
}
