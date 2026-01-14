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

package org.jscience.architecture.traffic.tools;

import org.jscience.architecture.traffic.Overlay;
import org.jscience.architecture.traffic.View;

import java.awt.*;
import java.awt.event.InputEvent;


/**
 * All classes implementing this interface can serve as tool to be used in
 * either simulator, editor or both.
 *
 * @author Group GUI
 * @version 1.0
 */
public interface Tool extends Overlay {
    /**
     * Invoked when the user presses a mouse button.
     *
     * @param view The <code>View</code> that the event originates from.
     * @param p The coordinates of the view the mouse cursor was at when the
     *        event was generated.
     * @param mask The mask taken directly from the MouseEvent, wrapped in a
     *        Mask object
     */
    public void mousePressed(View view, Point p, Mask mask);

    /**
     * Invoked when the user releases a mouse button.
     *
     * @param view The <code>View</code> that the event originates from.
     * @param p The coordinates of the view the mouse cursor was at when the
     *        event was generated.
     * @param mask The mask taken directly from the MouseEvent, wrapped in a
     *        Mask object
     */
    public void mouseReleased(View view, Point p, Mask mask);

    /**
     * Invoked when the user moves the mouse over the
     * <code>View</code>.
     *
     * @param view The <code>View</code> that the event originates from.
     * @param p The coordinates of the view the mouse cursor was at when the
     *        event was generated.
     * @param mask The mask taken directly from the MouseEvent, wrapped in a
     *        Mask object
     */
    public void mouseMoved(View view, Point p, Mask mask);

    /**
     * Invoked when this tool is selected as the current tool. The
     * panel will returned will be added to the end of the toolbar, allowing
     * the current tool to add tool specific controls to the toolbar. The tool
     * should take care of the listeners.
     *
     * @return DOCUMENT ME!
     */
    public Panel getPanel();

    /**
     * Class used to pass on the modifiers.
     */
    public static class Mask {
        /** DOCUMENT ME! */
        private int value;

/**
         * Creates a new Mask object.
         *
         * @param v DOCUMENT ME!
         */
        public Mask(int v) {
            value = v;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int getValue() {
            return value;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            return "Mask, val: " + value;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isLeft() {
            return (value & InputEvent.BUTTON1_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isMiddle() {
            return (value & InputEvent.BUTTON2_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isRight() {
            return (value & InputEvent.BUTTON3_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isAnyAltDown() {
            return isAltDown() || isAltGraphDown();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isAltDown() {
            return (value & InputEvent.ALT_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isAltGraphDown() {
            return (value & InputEvent.ALT_GRAPH_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isControlDown() {
            return (value & InputEvent.CTRL_MASK) > 0;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isShiftDown() {
            return (value & InputEvent.SHIFT_MASK) > 0;
        }
    }
}
