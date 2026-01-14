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

package org.jscience.computing.ai.artificiallife;

import org.jscience.computing.ai.util.ImageHelper;

import org.jscience.util.Steppable;
import org.jscience.util.Visualizable;

import java.awt.*;

import java.io.IOException;

import java.util.Random;


/**
 * This class groups {@link org.jscience.computing.ai.agents.FlockingAgent}
 * into one flock.
 *
 * @author James Matthews
 *
 * @see org.jscience.computing.ai.agents.FlockingAgent
 */
public class Flock implements Visualizable, Steppable {
    /**
     * The number of rows in the flocking world (this is equivalent to
     * pixel height).
     */
    protected int rows = 0;

    /**
     * The number of columns in the flocking world (this is equivalent
     * to pixel width).
     */
    protected int cols = 0;

    /** The number of flocking agents in the flock. */
    protected int numFlockingAgents = 0;

    /** The actual flocking agent array. */
    protected FlockingAgent[] flock;

    /** The background of the flock. */
    protected Color clrBackground = Color.lightGray;

/**
     * Creates a new instance of Flock
     */
    private Flock() {
    }

/**
     * Create a new instance of Flock with size information.
     *
     * @param fa   The number of flocking agents to assign.
     * @param cols The number of columns in the flocking world.
     * @param rows The number of rows in the flocking world.
     */
    public Flock(int fa, int cols, int rows) {
        Random rnd = new Random(0);

        FlockingAgent.initMisc(rnd, rows, cols, 270.0, 90.0, 0.5);
        FlockingAgent.initRadii(80.0, 30.0, 15.0, 40.0);
        FlockingAgent.initWeights(0.2, 0.4, 1.0, 0.8, 0);
        FlockingAgent.initTime(10.0, 0.95);

        flock = new FlockingAgent[fa];

        for (int i = 0; i < fa; i++) {
            flock[i] = new FlockingAgent();
        }

        FlockingAgent.setFlock(flock);

        numFlockingAgents = fa;
        this.cols = cols;
        this.rows = rows;
    }

    /**
     * Set the number of rows and columns in the world.
     *
     * @param rows Number of rows in this world.
     * @param cols Number of columns in this world.
     */
    public void setWorldSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * 
     */
    public void init() {
        for (int i = 0; i < flock.length; i++) {
            flock[i] = new FlockingAgent();
        }
    }

    /**
     * Advance the boids world by one time-step.
     */
    public void doStep() {
        for (int b = 0; b < numFlockingAgents; b++)
            flock[b].computeNewHeading(b);

        for (int b = 0; b < numFlockingAgents; b++)
            flock[b].update();
    }

    /**
     * A test function that writes the 640x480 boids world once every
     * 10 time-steps for 250 iterations.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Flock flock = new Flock(250, 640, 480);

        for (int i = 0; i < 1000; i++) {
            if ((i % 5) == 0) {
                flock.writeImage("boids" + i + ".png", 640, 480);
            }

            flock.doStep();
        }
    }

    /**
     * Draw the boids world. The boids are represented as arrows
     * showing their direction.
     *
     * @param g the graphics context.
     * @param width the width of the context.
     * @param height the height of the context.
     */
    public void render(java.awt.Graphics g, int width, int height) {
        //
        int sx = (int) ((width - cols) / 2.0);
        int sy = (int) ((height - rows) / 2.0);

        //
        g.setColor(clrBackground);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.fillRect(sx, sy, cols, rows);
        g.setColor(Color.WHITE);
        g.setClip(sx, sy, cols, rows);

        for (int b = 0; b < numFlockingAgents; b++) {
            flock[b].render(g, sx, sy);
        }
    }

    /**
     * Write an image of the boids world.
     *
     * @param s the filename.
     * @param width the width of the image.
     * @param height the height of the image.
     */
    public void writeImage(String s, int width, int height) {
        try {
            ImageHelper.writeVisualizedImage(s, width, height, this);
        } catch (IOException e) {
            System.err.println("Error writing image.");
        }
    }

    /**
     * Reset the flock (call <code>init</code>).
     *
     * @see #init
     */
    public void reset() {
        init();
    }

    /**
     * Set the background color.
     *
     * @param clr the background color.
     */
    public void setBackgroundColor(Color clr) {
        clrBackground = clr;
    }

    /**
     * Retrieve the background color.
     *
     * @return the background color in use.
     */
    public Color getBackgroundColor() {
        return clrBackground;
    }
}
