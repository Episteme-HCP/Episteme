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

import java.util.Enumeration;
import java.util.Vector;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupOnElapsedTime;

import javax.vecmath.Vector3f;


/**
 * This class makes the animation of growth of the plants in a scene.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class GrowthBehavior extends Behavior {
    /** A list of all objects which have to be modified */
    private Vector m_vAnimationList;

    /** the global time of the animation */
    private float m_fGlobalTime = 0.0f;

    /** The time factor of the growth speed. */
    private float m_fTimeFactor = 1.0f;

/**
     * The constructor can be used to pass all needed values for this behavior.
     * Here the list of objects to be animatated is passed. It has to be a
     * vector containing AnimationGroup objects.
     *
     * @param vAnimationList The list containing all objects of type
     *                       AnimationGroup which have to be scaled or translated.
     * @param fTimeFactor    DOCUMENT ME!
     */
    GrowthBehavior(Vector vAnimationList, float fTimeFactor) {
        m_vAnimationList = vAnimationList;

        if ((fTimeFactor > 0.0f) && (fTimeFactor <= 1000)) { //values of zero or less and more than 1000 are not valid
            m_fTimeFactor = fTimeFactor;
        }
    }

    /**
     * The first method the system calls. used to make initialization.
     * This method calls the WakeUpOnElapsedTime method which then calls the
     * processStimulus method after the specified time.
     */
    public void initialize() {
        //Log.debug("Timefactor " + m_fTimeFactor);
        //Log.debug("Wakeup after milliseconds: " +(long) (1000.0f / m_fTimeFactor));
        wakeupOn(new WakeupOnElapsedTime((long) (1000.0f / m_fTimeFactor)));
    }

    /**
     * This method is called m_fTimeFactor times every second. It is
     * responsible to modify the transformgroups of the branches and leafs of
     * a plant. A sorted list of all objects which have to be modified is
     * passed in the constructor. This method is never called by the user.
     *
     * @param criteria DOCUMENT ME!
     */
    public void processStimulus(Enumeration criteria) {
        m_fGlobalTime += 0.05;

        for (Enumeration enumeration = m_vAnimationList.elements();
                enumeration.hasMoreElements();) { //parse objects in the ordered list

            AnimationGroup ag = (AnimationGroup) enumeration.nextElement();

            //Log.debug("GlobalTime: " + m_fGlobalTime + ag.toString());
            if ((m_fGlobalTime > ag.m_fStartTime) &&
                    (m_fGlobalTime < ag.m_fEndTime)) { //this object has to be scaled

                Transform3D t3dScale = new Transform3D();
                ag.m_tgScale.getTransform(t3dScale);
                t3dScale.setScale((m_fGlobalTime - ag.m_fStartTime) / ag.m_fAge);
                ag.m_tgScale.setTransform(t3dScale);

                if (ag.m_tgTrans != null) { //this is a branch

                    Transform3D t3dTrans = new Transform3D();
                    ag.m_tgTrans.getTransform(t3dTrans);

                    Vector3f v3f = new Vector3f(0.0f,
                            (m_fGlobalTime - ag.m_fStartTime), 0.0f);
                    t3dTrans.setTranslation(v3f);
                    ag.m_tgTrans.setTransform(t3dTrans);
                }
            } else if (m_fGlobalTime > ag.m_fEndTime) { //remove object from list if it has not to be scaled any more
                m_vAnimationList.remove(ag);
            } else if (m_fGlobalTime < ag.m_fStartTime) { //abort if the actual element has not to be modified yet (list is sorted)

                //Log.debug("Break");
                break;
            }
        }

        //call this method again in time specified by the time factor
        wakeupOn(new WakeupOnElapsedTime((long) (1000.0f / m_fTimeFactor)));
    }
}
