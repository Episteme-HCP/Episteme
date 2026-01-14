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

package org.jscience.architecture.traffic;

import org.jscience.architecture.traffic.infrastructure.Infrastructure;
import org.jscience.architecture.traffic.infrastructure.Node;
import org.jscience.architecture.traffic.infrastructure.Road;

import java.awt.*;

import java.util.ConcurrentModificationException;


/**
 * This view paints an entire infrastructure
 *
 * @author Group Interfaces
 * @version 1.0
 */
public class MainView extends View {
    /** The current infrastructure */
    protected Infrastructure infra;

/**
     * Creates a new MainView
     *
     * @param i The Infrastructure to be displayed
     */
    public MainView(Infrastructure i) {
        super(i.getSize());
        infra = i;
    }

    /**
     * Returns the infrastructure
     *
     * @return infra The infrastructure
     */
    public Infrastructure getInfrastructure() {
        return infra;
    }

    /**
     * Sets the infrastructure
     *
     * @param i DOCUMENT ME!
     */
    public void setInfrastructure(Infrastructure i) {
        infra = i;
    }

    /**
     * Draw a buffer for the current infrastructure
     *
     * @param g DOCUMENT ME!
     */
    public void fillBuffer(Graphics2D g) {
        Node[] nodes = infra.getAllNodes();
        Road[] roads;

        try {
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].paint(g);
                roads = nodes[i].getAlphaRoads();

                for (int j = 0; j < roads.length; j++) {
                    try {
                        roads[j].paint(g);
                    } catch (ConcurrentModificationException e) {
                        try {
                            roads[j].paint(g); // try once more
                        } catch (ConcurrentModificationException ex) {
                        }
                    }
                }
            }
        } catch (TrafficException e) {
            Controller.reportError(e);
        }
    }
}
