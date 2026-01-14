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

package org.jscience.astronomy;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;


/**
 * This class is a keyboard behavior to control the navigation of the view
 * (camera).
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.2 $
 */
public class KeyNavigator extends Behavior {
    /** The angle to turn when the directionkeys are pressed */
    private static final float ANGLE = (float) Math.PI / 100;

    /** The step size to move when a direction key is pressed */
    private static final float F1_STEP = 0.01f;

    /** DOCUMENT ME! */
    private static final float F2_STEP = 0.1f;

    /** DOCUMENT ME! */
    private static final float F3_STEP = 1.0f;

    /** DOCUMENT ME! */
    private static final float F4_STEP = 10.0f;

    /** The reference to the main class. */
    private AstronomySimulator sss3d;

    /** The TransformGroup node to modify by the keyboard interaction. */
    private TransformGroup new_tgView;

    /** Wake up event when a key is pressed. */
    private WakeupOnAWTEvent wakeUp = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);

    /** DOCUMENT ME! */
    private float step = F3_STEP;

    /** DOCUMENT ME! */
    private Transform3D t3dView = new Transform3D();

    /** DOCUMENT ME! */
    private Transform3D t3dRot = new Transform3D();

    /** DOCUMENT ME! */
    private Vector3f v3fTrans = new Vector3f();

    /** DOCUMENT ME! */
    private Vector3f v3fTemp = new Vector3f();

    /** DOCUMENT ME! */
    private Matrix3f m3fRot = new Matrix3f();

    /** The turn or move modus */
    private boolean turn = false;

/**
     * Constructor that allows to specify the desired target transform group.
     *
     * @param targetTG the target transform group
     * @param sss3d    reference to the main class
     */
    public KeyNavigator(TransformGroup targetTG, AstronomySimulator sss3d) {
        new_tgView = targetTG;
        this.sss3d = sss3d;
    }

    /**
     * Override Behavior's initialize method to setup wakeup criteria.
     */
    public void initialize() {
        wakeupOn(wakeUp);
    }

    /**
     * Override Behavior's stimulus method to handle the event. This
     * method is called when a key on the keyboard has been pressed and
     * operates on the specified transform group to move the camera position.
     *
     * @param criteria all pressed keys in a list. This will be passed by the
     *        system.
     */
    public void processStimulus(Enumeration criteria) {
        WakeupOnAWTEvent ev;
        AWTEvent[] events;

        if (criteria.hasMoreElements()) {
            ev = (WakeupOnAWTEvent) criteria.nextElement();
            events = ev.getAWTEvent();

            KeyEvent eventKey = (KeyEvent) events[0];
            int keyCode = eventKey.getKeyCode();

            new_tgView.getTransform(t3dView);

            if (keyCode == KeyEvent.VK_P) {
                if (sss3d.canvas != null) {
                    sss3d.canvas.writeJPEG_ = true;
                    sss3d.canvas.repaint();
                }
            } else if (!turn) {
                switch (keyCode) {
                case KeyEvent.VK_UP: // Up arrow - to move up
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(0.0f, step, 0.0f), v3fTemp);
                    v3fTrans.add(v3fTemp); // v3fTemp = result of the product .transform
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_DOWN: // Down arrow - to move down
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(0.0f, -step, 0.0f), v3fTemp);
                    v3fTrans.add(v3fTemp);
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_RIGHT: // Right arrow - to move right
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(step, 0.0f, 0.0f), v3fTemp);
                    v3fTrans.add(v3fTemp);
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_LEFT: // Left arrow - to move left
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(-step, 0.0f, 0.0f), v3fTemp);
                    v3fTrans.add(v3fTemp);
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_PAGE_UP: // Page Up - to move forward
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(0.0f, 0.0f, -step), v3fTemp);
                    v3fTrans.add(v3fTemp);
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_PAGE_DOWN: // Page Down - to move backward
                    t3dView.get(v3fTrans);
                    t3dView.get(m3fRot);
                    m3fRot.transform(new Vector3f(0.0f, 0.0f, step), v3fTemp);
                    v3fTrans.add(v3fTemp);
                    t3dView.setTranslation(v3fTrans);

                    break;

                case KeyEvent.VK_HOME: // Home - go to the start position
                    t3dView.setIdentity();

                    break;

                default:
                }
            } else {
                switch (keyCode) {
                case KeyEvent.VK_UP: // Up arrow - to turn up
                    t3dRot.rotX(-ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_DOWN: // Down arrow - to turn down
                    t3dRot.rotX(ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_RIGHT: // Right arrow - to turn right
                    t3dRot.rotY(-ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_LEFT: // Left arrow - to turn left
                    t3dRot.rotY(ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_PAGE_UP: // Page Up - to turn
                    t3dRot.rotZ(-ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_PAGE_DOWN: // Page Down - to turn arround
                    t3dRot.rotZ(ANGLE);
                    t3dView.mul(t3dRot);

                    break;

                case KeyEvent.VK_HOME: // Home - go to the start position
                    t3dView.setIdentity();

                    break;

                default:
                }
            }

            new_tgView.setTransform(t3dView);
        }

        // Set wakeup criteria for next time.
        wakeupOn(wakeUp);
    }
}
