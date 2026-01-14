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

package org.jscience.physics.solids.gui;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import org.jscience.physics.solids.AtlasModel;
import org.jscience.physics.solids.examples.PlateHoleModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.*;

import javax.swing.*;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 * Java3d canvas that displays an Atlas Model.
 *
 * @author Wegge
 */
public class AtlasViewer extends JPanel {
    //static Logger AtlasLogger = Logger.getLogger((AtlasViewer.class).getName());
    /**
     * DOCUMENT ME!
     */
    BranchGroup objRoot;

    //static Logger AtlasLogger = Logger.getLogger((AtlasViewer.class).getName());
    /**
     * DOCUMENT ME!
     */
    BranchGroup geometryRoot;

    /**
     * DOCUMENT ME!
     */
    TransformGroup objTrans;

    /**
     * Creates a new AtlasViewer object.
     */
    public AtlasViewer() {
        //Set up Java3d stuff
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas = new Canvas3D(config);
        SimpleUniverse simpleU = new SimpleUniverse(canvas);

        // This moves the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
        viewingPlatform.setNominalViewingTransform();

        OrbitBehavior orbit = new OrbitBehavior(canvas,
                OrbitBehavior.REVERSE_ALL);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                1000.0);
        orbit.setSchedulingBounds(bounds);
        viewingPlatform.setViewPlatformBehavior(orbit);

        //Primary BranchGroup
        objRoot = new BranchGroup();
        objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);
        objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(Group.ALLOW_BOUNDS_READ);

        objTrans = new TransformGroup();
        objTrans.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objTrans.setCapability(Group.ALLOW_CHILDREN_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //Add lights
        AmbientLight ambient = new AmbientLight();
        ambient.setInfluencingBounds(bounds);
        objRoot.addChild(ambient);

        DirectionalLight headlight = new DirectionalLight();
        headlight.setColor(new Color3f(1, 1, 1));
        headlight.setInfluencingBounds(bounds);
        objRoot.addChild(headlight);

        geometryRoot = new BranchGroup();
        geometryRoot.setCapability(BranchGroup.ALLOW_DETACH);
        geometryRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        objRoot.addChild(objTrans);
        objTrans.addChild(geometryRoot);
        simpleU.addBranchGraph(objRoot);

        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);

        //Add navigation bar across top
        AtlasViewerButtonBar buttonBar = new AtlasViewerButtonBar();
        this.add(buttonBar, BorderLayout.NORTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fem DOCUMENT ME!
     */
    public void setModel(AtlasModel fem) {
        //Detach from root
        geometryRoot.detach();
        geometryRoot.removeAllChildren();

        fem.populateGeometry(geometryRoot);

        //Reattach
        objTrans.addChild(geometryRoot);
    }

    /**
     * Makes everything in the graphics window visible.
     */
    public void fitView() {
        //Then fit it into the window.
        BoundingSphere bounds = new BoundingSphere(objRoot.getBounds());
        double radius = bounds.getRadius();

        double ratio = 1 / radius;

        //Get the transform
        Transform3D t3d = new Transform3D();
        objTrans.getTransform(t3d);

        //Scale it down
        t3d.setScale(ratio);

        //Reset transformation
        objTrans.setTransform(t3d);

        centerView();
    }

    /**
     * Centers everything in the graphics window.
     */
    public void centerView() {
        BoundingSphere bounds = new BoundingSphere(objRoot.getBounds());
        Point3d center = new Point3d();
        bounds.getCenter(center);

        //AtlasLogger.info(bounds);

        //Load current transformation
        Transform3D t3d = new Transform3D();
        objTrans.getTransform(t3d);

        //Move to center of object
        center.scale(-1.0);
        t3d.setTranslation(new Vector3d(center));

        //Reset transformation
        objTrans.setTransform(t3d);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //AtlasLogger.info("Running ATLAS Viewer.");
        JFrame frame = new JFrame("ATLAS Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 400));

        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        AtlasViewer viewer = new AtlasViewer();
        container.add(viewer, BorderLayout.CENTER);

        frame.setVisible(true);

        PlateHoleModel model = new PlateHoleModel();

        viewer.setModel(model.getModel());
        viewer.fitView();
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    private class AtlasViewerButtonBar extends JToolBar {
        /**
         * Creates a new AtlasViewerButtonBar object.
         */
        AtlasViewerButtonBar() {
            super();

            //Fit View
            JButton fitButton = new JButton("Fit");
            fitButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        fitView();
                    }
                });
            this.add(fitButton);

            //Shade View
            JButton shadeButton = new JButton("Shade");
            shadeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                    }
                });
            this.add(shadeButton);

            //Element labels
            JButton elButton = new JButton("EL");
            elButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                    }
                });
            this.add(elButton);

            //Node labels
            JButton nlButton = new JButton("NL");
            nlButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                    }
                });
            this.add(nlButton);
        }
    }
}
