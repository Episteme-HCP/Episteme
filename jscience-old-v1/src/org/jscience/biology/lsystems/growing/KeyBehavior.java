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

package org.jscience.biology.lsystems.growing;

import javax.media.j3d.*;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

/**
 * This class is a simple behavior that implements keyboard navigation.
 * It is meant to modify the view platform transform.
 * Other than the classes KeyNavigatorBehavior and KeyNavigator from SUN this
 * class needs less computing power and is restricted to a few keys with no
 * acceleration.
 * The following keys are used:<br>
 * <li>Up arrow - to move forward.
 * <li>Down arrow - to move backwards.
 * <li>Left arrow - to turn left.
 * <li>Right arrow - to turn right.
 * <li>Page up - to look up.
 * <li>Page down - to look down.
 * <li>Y - to strafe left.
 * <li>X - to strafe right.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class KeyBehavior extends Behavior {
    /**
     * The view transformgroup to modify by the keyboard interaction.
     */
    private TransformGroup m_tgView;

    /**
     * Event when a key is pressed.
     */
    private WakeupOnAWTEvent m_wakeup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);

    /**
     * The key event
     */
    private KeyEvent m_eventKey;

    /**
     * The angle to turn when the direction keys are pressed
     */
    private float m_fAngle = (float) Math.PI / 32;

    /**
     * The step size to move when a direction key is pressed
     */
    private float m_fStep = 5.0f;

    /**
     * The Transform3D of the View TransformGroup
     */
    private Transform3D m_t3dView = new Transform3D();

    /**
     * Translational component of the Transform3D of the view
     */
    private Vector3f m_v3fTrans = new Vector3f();

    /**
     * Temporary Transform3D node for rotation
     */
    private Transform3D m_t3dTrans = new Transform3D();

    /**
     * Rotational component of the Transform3D of the view
     */
    private Matrix3f m_m3fRot = new Matrix3f();

    /**
     * Temporary vector for the translation
     */
    private Vector3f m_v3fTemp = new Vector3f();

    /**
     * Constructs a new key navigator behavior node that operates
     * on the specified transform group.
     *
     * @param targetTG the target transform group
     */
    public KeyBehavior(TransformGroup targetTG) {
        m_tgView = targetTG;
    }

    /**
     * Override Behavior's initialize method to setup wakeup criteria.
     */
    public void initialize() {
        // Establish initial wakeup criteria
        wakeupOn(m_wakeup);
    }

    /**
     * Override Behavior's stimulus method to handle the event.
     * This method is called when a key on the keyboard has been pressed.
     *
     * @param criteria All pressed keys in a list. This will be passed by the system.
     */
    public void processStimulus(Enumeration criteria) {
        WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;

        if (criteria.hasMoreElements()) {
            ev = (WakeupOnAWTEvent) criteria.nextElement();
            events = ev.getAWTEvent();
            m_eventKey = (KeyEvent) events[0];

            int keyCode = m_eventKey.getKeyCode();

            m_tgView.getTransform(m_t3dView);

            switch (keyCode) //handle the different keys typed
            {
                case KeyEvent.VK_UP:
                    //Log.debug("Key up");
                    m_t3dView.get(m_v3fTrans);
                    m_t3dView.get(m_m3fRot);
                    m_m3fRot.transform(new Vector3f(0.0f, 0.0f, -m_fStep), m_v3fTemp);
                    m_v3fTrans.add(m_v3fTemp);

                    m_t3dView.setTranslation(m_v3fTrans);

                    break;

                case KeyEvent.VK_DOWN:
                    //Log.debug("Key down");
                    m_t3dView.get(m_v3fTrans);
                    m_t3dView.get(m_m3fRot);
                    m_m3fRot.transform(new Vector3f(0.0f, 0.0f, m_fStep), m_v3fTemp);
                    m_v3fTrans.add(m_v3fTemp);

                    m_t3dView.setTranslation(m_v3fTrans);

                    break;

                case KeyEvent.VK_LEFT:
                    //Log.debug("Key left");
                    m_t3dTrans.rotY(m_fAngle);
                    m_t3dView.mul(m_t3dTrans);

                    break;

                case KeyEvent.VK_RIGHT:
                    //Log.debug("Key right");
                    m_t3dTrans.rotY(-m_fAngle);
                    m_t3dView.mul(m_t3dTrans);

                    break;

                case KeyEvent.VK_PAGE_UP:
                    //Log.debug("Key page up");
                    m_t3dTrans.rotX(m_fAngle);
                    m_t3dView.mul(m_t3dTrans);

                    break;

                case KeyEvent.VK_PAGE_DOWN:
                    //Log.debug("Key page down");
                    m_t3dTrans.rotX(-m_fAngle);
                    m_t3dView.mul(m_t3dTrans);

                    break;

                case KeyEvent.VK_Y:
                    //Log.debug("Key down");
                    m_t3dView.get(m_v3fTrans);
                    m_t3dView.get(m_m3fRot);
                    m_m3fRot.transform(new Vector3f(-m_fStep, 0.0f, 0.0f), m_v3fTemp);
                    m_v3fTrans.add(m_v3fTemp);

                    m_t3dView.setTranslation(m_v3fTrans);

                    break;

                case KeyEvent.VK_X:
                    //Log.debug("Key down");
                    m_t3dView.get(m_v3fTrans);
                    m_t3dView.get(m_m3fRot);
                    m_m3fRot.transform(new Vector3f(m_fStep, 0.0f, 0.0f), m_v3fTemp);
                    m_v3fTrans.add(m_v3fTemp);

                    m_t3dView.setTranslation(m_v3fTrans);

                    break;

                default:
            }

            m_tgView.setTransform(m_t3dView);
        }

        // Set wakeup criteria for next time
        wakeupOn(m_wakeup);
    }
}
