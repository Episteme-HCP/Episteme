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

import java.awt.*;
import java.awt.event.KeyEvent;

import java.util.Enumeration;

import javax.media.j3d.*;


/**
 * A KeyBehavior which called exit when the ESC key is pressed
 */
public class ExitKeyBehavior extends Behavior {
    // look for released events since sometimes ESCAPE only generates a
    // RELEASE event
    /** DOCUMENT ME! */
    WakeupCriterion[] criterion = {
            new WakeupOnAWTEvent(Event.KEY_PRESS),
            new WakeupOnAWTEvent(Event.KEY_RELEASE),
        };

    /** DOCUMENT ME! */
    WakeupCondition conditions = new WakeupOr(criterion);

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
        WakeupCriterion wakeup;
        AWTEvent[] evt = null;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();

            if (wakeup instanceof WakeupOnAWTEvent) {
                evt = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
            }

            if (evt != null) {
                for (int i = 0; i < evt.length; i++) {
                    if (evt[i] instanceof KeyEvent) {
                        processKeyEvent((KeyEvent) evt[i]);
                    }
                }
            }
        }

        wakeupOn(conditions);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void processKeyEvent(KeyEvent evt) {
        int key = evt.getKeyCode();

        switch (key) {
        case KeyEvent.VK_ESCAPE:
            System.exit(0);

            break;
        }
    }
}
