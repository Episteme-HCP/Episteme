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

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import javax.media.j3d.TransformGroup;
import java.awt.event.MouseEvent;

/**
 * MouseFastRotate.java
 * <p/>
 * <p/>
 * Created: Sat Nov 21 19:45:34 1998
 *
 * @author Stephan Reiling
 */
public class MouseFastRotate extends MouseRotate {
    MolecularScene myScene;

    /**
     * Creates a new MouseFastRotate object.
     *
     * @param transformGroup DOCUMENT ME!
     * @param scene          DOCUMENT ME!
     */
    public MouseFastRotate(TransformGroup transformGroup, MolecularScene scene) {
        super(transformGroup);
        myScene = scene;
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void processMouseEvent(MouseEvent evt) {
        if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
            //System.out.println("mouse down");
            myScene.setFast();
        } else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
            //System.out.println("mouse up");
            myScene.setNice();
        } /*
            else if (evt.getID() == MouseEvent.MOUSE_MOVED) {
            // Process mouse move event
            }*/
        super.processMouseEvent(evt);
    }
} // MouseFastRotate
