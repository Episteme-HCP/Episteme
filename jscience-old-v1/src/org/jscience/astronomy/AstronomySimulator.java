package org.jscience.astronomy;


import com.sun.j3d.utils.universe.SimpleUniverse;
import org.jscience.physics.kinematics.KinematicsSimulation;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Point3d;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Main Program.<br>
 *
 * @version $Revision: 1.3 $
 */
public class AstronomySimulator extends JFrame implements KinematicsSimulation {

     /**
     * DOCUMENT ME!
     */
    private CapturingCanvas3D canvas;

    /**
     * DOCUMENT ME!
     */
    private Locale locale;

    /**
     * DOCUMENT ME!
     */
    private GraphicsConfiguration config;

    /**
     * DOCUMENT ME!
     */
    private Dimension screenSize;

    /**
     * Default constructor. Builds up the whole solar system simulator.
     */
    public AstronomySimulator() {
        super("J3D Astronomy Simulator Version 1.0");

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setVisible(false);
        this.addWindowListener(new CloseWindowListener());

        // Force SwingApplet to come up in the System L&F
        String laf = UIManager.getSystemLookAndFeelClassName();

        try {
            UIManager.setLookAndFeel(laf);

            // If you want the Cross Platform L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException exc) {
            System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
        } catch (Exception exc) {
            System.err.println("Error loading " + laf + ": " + exc);
        }

        GraphicsConfigTemplate3D tmpl = new GraphicsConfigTemplate3D();
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        config = device.getBestConfiguration(tmpl);

        startbox = new StartBox(this);
    }

    /**
     * Creates the solar system 3D scene.
     */
    public void createScene() {
        this.setSize(screenSize.width - 200, screenSize.height - 150);
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2);

        canvas = new CapturingCanvas3D(config); // The used Canvas3D
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(canvas, BorderLayout.CENTER);

         AnimationSpeed animationSpeed = (AnimationSpeed) objInfo.getParameter(XMLConstants.ANIMATIONSPEED);
        int factor = 0;

        switch (animationSpeed.getType()) {
            case AnimationSpeed.DAYS_PER_SECOND:
                factor = 1;

                break;

            case AnimationSpeed.HOURS_PER_SECOND:
                factor = 24;

                break;

            case AnimationSpeed.MINUTES_PER_SECOND:
                factor = 24 * 60;

                break;

            default:
        }

        int animSpeed = animationSpeed.getValue();

        // Create a virtual universe.
        VirtualUniverse universe = new VirtualUniverse();

        // A single hi-res. Locale node is created and attached to
        // the virtual universe.
        locale = new Locale(universe);

        // Necessary to use AstronomyTextureLoader in other classes.
        AstronomyTextureLoader.setImageObserver(this); // AWT Component

        lnkSceneSolarSystem = new SceneSolarSystem(canvas, objPos, objInfo, this);

        BranchGroup scene = lnkSceneSolarSystem.createSceneGraph();

        // The ViewBranch class creates the instances of ViewPlatform,
        // View, etc.
        lnkViewBranch = new ViewBranch(this, canvas, null);

        BranchGroup view = lnkViewBranch.myViewBranch();

        this.setVisible(true);

        locale.addBranchGraph(view);
        locale.addBranchGraph(scene);
    }

    /**
     * Creates the solar system 3D scene. Used if simulator is started with 3D
     * Glasses.
     */
    public void createStereoScene() {
        this.setSize(screenSize.width, screenSize.height);
        this.setLocation(0, 0);

        Canvas3D canvasL = new Canvas3D(config);
        Canvas3D canvasR = new Canvas3D(config);

        canvasL.setMonoscopicViewPolicy(View.LEFT_EYE_VIEW);
        canvasR.setMonoscopicViewPolicy(View.RIGHT_EYE_VIEW);

        canvasL.setStereoEnable(true);
        canvasR.setStereoEnable(true);

        /*
              Point3d pointL = new Point3d(0.0142, 0.135, 0.4572);
              Point3d pointR = new Point3d(0.0208, 0.135, 0.4572);
              canvasL.setLeftManualEyeInImagePlate(pointL);
              canvasR.setRightManualEyeInImagePlate(pointR);
        */
        Screen3D screenL = canvasL.getScreen3D();
        Screen3D screenR = canvasR.getScreen3D();

        screenL.setPhysicalScreenHeight((screenL.getPhysicalScreenHeight() * 1.5));
        screenR.setPhysicalScreenHeight((screenR.getPhysicalScreenHeight() * 1.5));

        Glasses3DLayout layout = new Glasses3DLayout(screenSize.width,
                screenSize.height);

        this.getContentPane().setBackground(Color.black);
        this.getContentPane().setLayout(layout);
        this.getContentPane().add(canvasL, layout.getLeftPanel());
        this.getContentPane().add(canvasR, layout.getRightPanel());

        // Create a virtual universe.
        VirtualUniverse universe = new VirtualUniverse();

        // A single hi-res. Locale node is created and attached to
        // the virtual universe.
        Locale locale = new Locale(universe);

        // Necessary to use AstronomyTextureLoader in other classes.
        AstronomyTextureLoader.setImageObserver(this); // AWT Component

        lnkSceneSolarSystem = new SceneSolarSystem(canvasL, objPos, objInfo,
                this);

        locale.addBranchGraph(lnkSceneSolarSystem.createSceneGraph());

        // The ViewBranch class creates the instances of ViewPlatform,
        // View, etc.
        lnkViewBranch = new ViewBranch(this, canvasL, canvasR);

        locale.addBranchGraph(lnkViewBranch.myViewBranch());

        this.setVisible(true);
        this.setResizable(false);
    }

    /**
     * Removes the scene.
     *
     * @param scene a reference to the BranchGroup that has to be removed
     */
    public void removeScene(BranchGroup scene) {
        locale.removeBranchGraph(scene);
    }

    /**
     * Adds a new scene.
     *
     * @param scene a reference to the BranchGroup that has to be added
     */
    public void addScene(BranchGroup scene) {
        locale.addBranchGraph(scene);
    }

    public Transform3D getCurrentPosition() {

    }

    /* Default constructor.  Here we create the universe.
     */
    public Stars() {
        setLayout(new BorderLayout());
        Canvas3D canvas = createCanvas();
        add("Center", canvas);
        u = new SimpleUniverse(canvas);
        BranchGroup scene = createContent();
        u.getViewingPlatform().setNominalViewingTransform();  // back away from object a little
        OrbitBehavior orbit = new OrbitBehavior(canvas);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY));
        u.getViewingPlatform().setViewPlatformBehavior(orbit);
        scene.compile();
        u.addBranchGraph(scene);
    }

    public void init() {

        if (bgImage == null) {
            // the path to the image for an applet
            try {
                bgImage = new java.net.URL(getCodeBase().toString() +
                        "../images/bg.jpg");
            } catch (java.net.MalformedURLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
                SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);
        add("Center", c);

        BranchGroup scene = createSceneGraph();
        u = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

        TransformGroup viewTrans =
                u.getViewingPlatform().getViewPlatformTransform();

        // Create the rotate behavior node
        MouseRotate behavior1 = new MouseRotate(viewTrans);
        scene.addChild(behavior1);
        behavior1.setSchedulingBounds(bounds);

        // Create the zoom behavior node
        MouseZoom behavior2 = new MouseZoom(viewTrans);
        scene.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);

        // Create the translate behavior node
        MouseTranslate behavior3 = new MouseTranslate(viewTrans);
        scene.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);

        // Let Java 3D perform optimizations on this scene graph.
        scene.compile();

        u.addBranchGraph(scene);
    }

    /* Create a canvas to draw the 3D world on.
    */
    private Canvas3D createCanvas() {
        GraphicsConfigTemplate3D graphicsTemplate = new GraphicsConfigTemplate3D();
        GraphicsConfiguration gc1 =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice().getBestConfiguration(graphicsTemplate);
        return new Canvas3D(gc1);
    }

    /**
     * Allows SolarSystemSimulator to be run as an application.
     *
     * @param args args[]
     */
    public static void main(String[] args) {
        System.out.println("\nAstronomySimulator copyright JScience");
        new AstronomySimulator();
    }

    /**
     * Window Listener for the SolarSystemSimulator JFrame.
     */
    class CloseWindowListener extends WindowAdapter {
        /**
         * DOCUMENT ME!
         *
         * @param e DOCUMENT ME!
         */
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}


