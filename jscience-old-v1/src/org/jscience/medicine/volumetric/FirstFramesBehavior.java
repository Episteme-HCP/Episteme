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

package org.jscience.medicine.volumetric;

import java.util.Enumeration;

import javax.media.j3d.*;


/**
 * A Behavior waits for the initial frame to be displayed, calls
 * VolRend.eyePtChanged() and stops running
 */
public class FirstFramesBehavior extends Behavior {
    /** DOCUMENT ME! */
    VolRendListener volRend;

    /** DOCUMENT ME! */
    int numFrames = 0;

    /** DOCUMENT ME! */
    WakeupCriterion[] criterion = { new WakeupOnElapsedFrames(0), };

    /** DOCUMENT ME! */
    WakeupCondition conditions = new WakeupOr(criterion);

/**
     * Creates a new FirstFramesBehavior object.
     *
     * @param volRend DOCUMENT ME!
     */
    FirstFramesBehavior(VolRendListener volRend) {
        this.volRend = volRend;
    }

    /**
     * DOCUMENT ME!
     */
    public void initialize() {
        wakeupOn(conditions);
    }

    /**
     * DOCUMENT ME!
     *
     * @param criteria DOCUMENT ME!
     */
    public void processStimulus(Enumeration criteria) {
        //System.out.println("frame");
        volRend.eyePtChanged();

        // don't wakeup after the first couple frame
        if (numFrames++ < 5) {
            wakeupOn(conditions);
        }
    }
}
