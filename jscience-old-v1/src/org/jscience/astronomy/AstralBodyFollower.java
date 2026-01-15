/*
  File: FollowCelestialObject.java

  University of Applied Science Berne,HTA-Biel/Bienne,
  Computer Science Department.

  Diploma thesis J3D Solar System Simulator
  Originally written by Marcel Portner & Bernhard Hari (c) 2000

  CVS - Information :

  $Header: /zpool01/javanet/scm/svn/tmp/cvs2svn/jade/v1/src/org/jscience/astronomy/AstralBodyFollower.java,v 1.2 2007-10-23 18:14:17 virtualcall Exp $
  $Author: virtualcall $
  $Date: 2007-10-23 18:14:17 $
  $State: Exp $

*/
package org.jscience.astronomy;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnElapsedFrames;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;


/**
 * This class follow a celestial object.
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class AstralBodyFollower extends Behavior {
    /** The TransformGroup node to follow the celestial object. */
    private TransformGroup new_tgView;

    /** The reference to the main class. */
    private AstronomySimulator simulator;

    /** Wake up event for each new frame. */
    private WakeupOnElapsedFrames wakeUp = new WakeupOnElapsedFrames(0);

/**
     * Constructor that allows to specify the desired target transform group.
     *
     * @param targetTG  the target transform group
     * @param simulator reference to the main class
     */
    public AstralBodyFollower(TransformGroup targetTG,
        AstronomySimulator simulator) {
        new_tgView = targetTG;
        this.simulator = simulator;
    }

    /**
     * Override Behavior's initialize method to setup wakeup criteria.
     */
    public void initialize() {
        wakeupOn(wakeUp);
    }

    /**
     * Override Behavior's stimulus method to handle the event. This
     * method is called for each new frame and operates on the specified
     * transform group to follow an object with the camera.
     *
     * @param criteria an enumeration of triggered wakeup criteria for this
     *        behavior.
     */
    public void processStimulus(Enumeration criteria) {
        if (criteria.hasMoreElements()) {
            Transform3D followObject = simulator.getCurrentPosition();

            //boolean follow = !((Boolean) simulator.getInitializationObject().getParameter(XMLConstants.CAMERAATORIGIN)).booleanValue();
            //boolean compressed = ((Boolean) simulator.getInitializationObject().getParameter(XMLConstants.COMPRESSED)).booleanValue();

            // Default camera position
            if (followObject == null) {
                // rotate about 270 degree at the X - axis
                Matrix3d rot = new Matrix3d();
                rot.rotX(Math.toRadians(270));

                Vector3d trans;

                //if (compressed) {
                //trans = new Vector3d(0.0, 70.0, 0.0);
                //} else {
                trans = new Vector3d(0.0, 1000.0, 0.0);
                //}
                followObject = new Transform3D(rot, trans, 1.0);
            } else { //if (follow) {
                     // Give an offset.

                Vector3d offset = new Vector3d();

                followObject.get(offset);

                //if (compressed) {
                //offset.add(new Vector3d(0.0, 0.0, 2.0));
                //} else {
                offset.add(new Vector3d(0.0, 0.0, 5.0));
                //}
                followObject.set(offset);
            }

            new_tgView.setTransform(followObject);
        }

        // Set wakeup criteria for next time.
        wakeupOn(wakeUp);
    }
}
