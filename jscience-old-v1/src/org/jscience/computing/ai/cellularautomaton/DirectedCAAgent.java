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

package org.jscience.computing.ai.cellularautomaton;

/**
 * An extension of <code>CAAgent</code> that adds a directional
 * functionality. A directed agent can move left or right, allowing for agents
 * with more realistic movement.
 *
 * @author James Matthews
 */
public class DirectedCAAgent extends CAAgent {
    /** Direction array, from top in a clockwise direction. */
    static final int[][] directionArray = {
            { 0, -1 }, // up
            { 1, -1 }, // up-right
            { 1, 0 }, // right
            { 1, 1 }, // down-right
            { 0, 1 }, // bottom
            { -1, 1 }, // bottom-left
            { -1, 0 }, // left
            { -1, -1 } // top-left
        };

    /**  */
    /** DOCUMENT ME! */
    static public final int TOP = 0;

    /** DOCUMENT ME! */
    static public final int TOP_RIGHT = 1;

    /** DOCUMENT ME! */
    static public final int RIGHT = 2;

    /** DOCUMENT ME! */
    static public final int BOTTOM_RIGHT = 3;

    /** DOCUMENT ME! */
    static public final int BOTTOM = 4;

    /** DOCUMENT ME! */
    static public final int BOTTOM_LEFT = 5;

    /** DOCUMENT ME! */
    static public final int LEFT = 6;

    /** DOCUMENT ME! */
    static public final int TOP_LEFT = 7;

    /** DOCUMENT ME! */
    protected int direction = TOP;

/**
     * Creates a new instance of DirectedCAAgent
     */
    public DirectedCAAgent() {
        this(0, 0, 0);
    }

/**
     * Creates a new DirectedCAAgent object.
     *
     * @param x     DOCUMENT ME!
     * @param y     DOCUMENT ME!
     * @param state DOCUMENT ME!
     */
    public DirectedCAAgent(int x, int y, int state) {
        this(x, y, state, TOP);
    }

/**
     * Creates a new DirectedCAAgent object.
     *
     * @param x         DOCUMENT ME!
     * @param y         DOCUMENT ME!
     * @param state     DOCUMENT ME!
     * @param direction DOCUMENT ME!
     */
    public DirectedCAAgent(int x, int y, int state, int direction) {
        super(x, y, state);

        this.direction = direction;
    }

    /**
     * DOCUMENT ME!
     */
    public void reverse() {
        direction = (direction + 4) % 8;
    }

    /**
     * DOCUMENT ME!
     */
    public void moveLeft() {
        direction = (direction + 7) % 8;
    }

    /**
     * DOCUMENT ME!
     */
    public void moveRight() {
        direction = (direction + 1) % 8;
    }

    /**
     * DOCUMENT ME!
     *
     * @param world DOCUMENT ME!
     */
    public void move(CellularAutomataLayered world) {
        int gx = world.translateGeometry(pos_x + directionArray[direction][0], 0);
        int gy = world.translateGeometry(pos_y + directionArray[direction][1], 1);

        if (world.getCollisionDetection()) {
            /*            if (positionTest[gx][gy] == true) return;
            
                        positionTest[cx][cy] = false;
                        positionTest[gx][gy] = true;*/
        }

        setPosition(gx, gy);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDX() {
        return directionArray[direction][0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param dx DOCUMENT ME!
     */
    public void setDX(int dx) {
        direction = getDirection(dx, directionArray[direction][1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDY() {
        return directionArray[direction][1];
    }

    /**
     * DOCUMENT ME!
     *
     * @param dy DOCUMENT ME!
     */
    public void setDY(int dy) {
        direction = getDirection(directionArray[direction][0], dy);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDirection() {
        return direction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dir DOCUMENT ME!
     */
    public void setDirection(int dir) {
        direction = dir;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getDirection(int dx, int dy) {
        for (int i = 0; i < directionArray.length; i++) {
            if ((directionArray[i][0] == dx) && (directionArray[i][1] == dy)) {
                return i;
            }
        }

        // if dx and dy == 0, return random direction.
        return (int) (Math.random() * 8);
    }
}
