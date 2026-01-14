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

import java.awt.*;

import java.util.Random;


/**
 * Implements an agent that flocks with other similar agents. This code is
 * based on Mike Miller's Java code conversion for <a
 * href="http://mitpress.mit.edu/books/FLAOH/cbnhtml/home.html"
 * target="_top">The Computational Beauty of Nature</a> by Gary William Flake.
 * The code has been converted to the Generation5 SDK style and system (using
 * Visualizable etc.).
 *
 * @author James Matthews
 * @author Mike Miller
 * @author Gary William Flake
 *
 * @see FlockingAgent
 */
public class FlockingAgent {
    //    protected static final int tailLen = 10;
    /** The random seed used to generate positional data. */
    protected static Random rnd;

    /** The width of the flocking agent's world. */
    protected static int rows;

    /** The height of the flocking agent's world. */
    protected static int cols;

    /** The viewing angle of the agent. */
    protected static double viewA;

    /** DOCUMENT ME! */
    protected static double vAvoidA;

    /** DOCUMENT ME! */
    protected static double minV;

    /** DOCUMENT ME! */
    protected static double copyR;

    /** DOCUMENT ME! */
    protected static double centroidR;

    /** DOCUMENT ME! */
    protected static double avoidR;

    /** DOCUMENT ME! */
    protected static double vAvoidR;

    /** DOCUMENT ME! */
    protected static double copyW;

    /** DOCUMENT ME! */
    protected static double centroidW;

    /** DOCUMENT ME! */
    protected static double avoidW;

    /** DOCUMENT ME! */
    protected static double vAvoidW;

    /** DOCUMENT ME! */
    protected static double randW;

    /** DOCUMENT ME! */
    protected static double dt;

    /** DOCUMENT ME! */
    protected static double ddt;

    /** The flock this agent is in. */
    protected static FlockingAgent[] myFlock;

    /** DOCUMENT ME! */
    protected static double nx;

    /** DOCUMENT ME! */
    protected static double ny;

    /** The x-position of this agent. */
    protected double positionX;

    /** The positionY-position of this agent. */
    protected double positionY;

    /** The x-velocity of this agent. */
    protected double vx;

    /** The y-velocity of this agent. */
    protected double vy;

    /**
     * The x-velocity to be used in the next frame. Remember that the
     * flocking agents are updated "simultaneously", so they should all have
     * their new values computed using <code>computeNewHeading</code>, then
     * all have them updated using <code>update</code>.
     */
    protected double nvx;

    /**
     * The y-velocity to be used in the next frame. Remember that the
     * flocking agents are updated "simultaneously", so they should all have
     * their new values computed using <code>computeNewHeading</code>, then
     * all have them updated using <code>update</code>.
     */
    protected double nvy;

/**
     * Creates a new instance of FlockingAgent. Random values are automatically
     * assigned to the positional and velocity variables.
     */
    public FlockingAgent() {
        // Set the random position of the FA
        positionX = Math.abs(rnd.nextInt() % cols);
        positionY = Math.abs(rnd.nextInt() % rows);

        // Set the random velocity
        vx = (2 * rnd.nextDouble()) - 1;
        vy = (2 * rnd.nextDouble()) - 1;

        // Normalize the results
        normalize(vx, vy);
        vx = nx;
        vy = ny;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param rr DOCUMENT ME!
     * @param cc DOCUMENT ME!
     * @param va DOCUMENT ME!
     * @param vaa DOCUMENT ME!
     * @param mv DOCUMENT ME!
     */
    public static void initMisc(Random r, int rr, int cc, /*Viewer v,*/
        double va, double vaa, double mv) {
        rnd = r;
        rows = rr;
        cols = cc;
        viewA = (va * Math.PI) / 180.0;
        vAvoidA = (vaa * Math.PI) / 180.0;
        minV = mv;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cr DOCUMENT ME!
     * @param ccr DOCUMENT ME!
     * @param ar DOCUMENT ME!
     * @param vr DOCUMENT ME!
     */
    public static void initRadii(double cr, double ccr, double ar, double vr) {
        copyR = cr;
        centroidR = ccr;
        avoidR = ar;
        vAvoidR = vr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cw DOCUMENT ME!
     * @param ccw DOCUMENT ME!
     * @param aw DOCUMENT ME!
     * @param vw DOCUMENT ME!
     * @param rw DOCUMENT ME!
     */
    public static void initWeights(double cw, double ccw, double aw, double vw,
        double rw) {
        copyW = cw;
        centroidW = ccw;
        avoidW = aw;
        vAvoidW = vw;
        randW = rw;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param tt DOCUMENT ME!
     */
    public static void initTime(double t, double tt) {
        dt = t;
        ddt = tt;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flock DOCUMENT ME!
     */
    public static void setFlock(FlockingAgent[] flock) {
        // FIXME: Check to see if this is member flock?s
        // FIXME: This shouldn't be static if we want multiple flocks.
        myFlock = flock;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    protected static void normalize(double x, double y) {
        // FIXME: Don't like the way this is done, not very OOP. Returning an
        // FIXME: array might have a performance hit though.
        double l = len(x, y);

        if (l != 0.0) {
            nx = x / l;
            ny = y / l;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double len(double x, double y) {
        // TODO: Is it worth sqrting this?
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double dist(double x1, double y1, double x2, double y2) {
        return len(x2 - x1, y2 - y1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected static double dot(double x1, double y1, double x2, double y2) {
        return ((x1 * x2) + (y1 * y2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param self
     */
    public void computeNewHeading(int self) {
        int numcent = 0;
        double xa = 0;
        double ya = 0;
        double xb = 0;
        double yb = 0;
        double xc = 0;
        double yc = 0;
        double xd = 0;
        double yd = 0;
        double xt = 0;
        double yt = 0;
        double mindist;
        double mx = 0;
        double my = 0;
        double d;
        double cosangle;
        double cosvangle;
        double costemp;
        double xtemp;
        double ytemp;
        double maxr;
        double u;
        double v;
        double ss;

        // Maximum radius of visual avoidance, copy, centroid and avoidance.
        maxr = Math.max(vAvoidR, Math.max(copyR, Math.max(centroidR, avoidR)));

        // The cosine of the viewing and visual avoidance angles.
        cosangle = Math.cos(viewA / 2);
        cosvangle = Math.cos(vAvoidA / 2);

        int numBoids = myFlock.length;

        for (int b = 0; b < numBoids; b++) {
            if (b == self) {
                continue;
            }

            mindist = Double.MAX_VALUE;

            for (int j = -cols; j <= cols; j += cols) {
                for (int k = -rows; k <= rows; k += rows) {
                    d = dist(myFlock[b].positionX + j,
                            myFlock[b].positionY + k, positionX, positionY);

                    if (d < mindist) {
                        mindist = d;
                        mx = myFlock[b].positionX + j;
                        my = myFlock[b].positionY + k;
                    }
                }
            }

            if (mindist > maxr) {
                continue;
            }

            xtemp = mx - positionX;
            ytemp = my - positionY;
            costemp = dot(vx, vy, xtemp, ytemp) / (len(vx, vy) * len(xtemp,
                    ytemp));

            if (costemp < cosangle) {
                continue;
            }

            if ((mindist <= centroidR) && (mindist > avoidR)) {
                xa += (mx - positionX);
                ya += (my - positionY);
                numcent++;
            }

            if ((mindist <= copyR) && (mindist > avoidR)) {
                xb += myFlock[b].vx;
                yb += myFlock[b].vy;
            }

            if (mindist <= avoidR) {
                xtemp = positionX - mx;
                ytemp = positionY - my;
                d = 1 / len(xtemp, ytemp);
                xtemp *= d;
                ytemp *= d;
                xc += xtemp;
                yc += ytemp;
            }

            if ((mindist <= vAvoidR) && (cosvangle < costemp)) {
                xtemp = positionX - mx;
                ytemp = positionY - my;

                u = v = 0;

                if ((xtemp != 0) && (ytemp != 0)) {
                    ss = (ytemp / xtemp);
                    ss *= ss;
                    u = Math.sqrt(ss / (1 + ss));
                    v = (-xtemp * u) / ytemp;
                } else if (xtemp != 0) {
                    u = 1;
                } else if (ytemp != 0) {
                    v = 1;
                }

                if (((vx * u) + (vy * v)) < 0) {
                    u = -u;
                    v = -v;
                }

                u = positionX - mx + u;
                v = positionY - my + v;

                d = len(xtemp, ytemp);

                if (d != 0) {
                    u /= d;
                    v /= d;
                }

                xd += u;
                yd += v;
            }
        }

        if (numcent < 2) {
            xa = ya = 0;
        }

        if (len(xa, ya) > 1.0) {
            normalize(xa, ya);
            xa = nx;
            ya = ny;
        }

        if (len(xb, yb) > 1.0) {
            normalize(xb, yb);
            xb = nx;
            yb = ny;
        }

        if (len(xc, yc) > 1.0) {
            normalize(xc, yc);
            xc = nx;
            yc = ny;
        }

        if (len(xd, yd) > 1.0) {
            normalize(xd, yd);
            xd = nx;
            yd = ny;
        }

        xt = (centroidW * xa) + (copyW * xb) + (avoidW * xc) + (vAvoidW * xd);
        yt = (centroidW * ya) + (copyW * yb) + (avoidW * yc) + (vAvoidW * yd);

        if (randW > 0) {
            xt += (randW * ((2 * rnd.nextDouble()) - 1));
            yt += (randW * ((2 * rnd.nextDouble()) - 1));
        }

        nvx = (vx * ddt) + (xt * (1 - ddt));
        nvy = (vy * ddt) + (yt * (1 - ddt));
        d = len(nvx, nvy);

        if (d < minV) {
            nvx *= (minV / d);
            nvy *= (minV / d);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void update() {
        vx = nvx;
        vy = nvy;
        positionX += (vx * dt);
        positionY += (vy * dt);

        // Apply torodial geometry (wrap around)
        if (positionX < 0) {
            positionX += cols;
        } else if (positionX >= cols) {
            positionX -= cols;
        }

        if (positionY < 0) {
            positionY += rows;
        } else if (positionY >= rows) {
            positionY -= rows;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param graphics DOCUMENT ME!
     * @param sx DOCUMENT ME!
     * @param sy DOCUMENT ME!
     */
    public void render(Graphics graphics, int sx, int sy) {
        double x1;
        double x2;
        double x3;
        double y1;
        double y2;
        double y3;
        double a;
        double t;
        double aa;
        double tailLen = 10.0;

        // direction line
        x3 = vx;
        y3 = vy;
        normalize(x3, y3);
        x3 = nx;
        y3 = ny;
        x1 = positionX;
        y1 = positionY;
        x2 = x1 - (x3 * tailLen);
        y2 = y1 - (y3 * tailLen);
        graphics.drawLine((int) x1 + sx, (int) y1 + sy, (int) x2 + sx,
            (int) y2 + sy);

        // head
        t = (x1 - x2) / tailLen;
        t = (t < -1) ? (-1) : ((t > 1) ? 1 : t);
        a = Math.acos(t);
        a = ((y1 - y2) < 0) ? (-a) : a;

        // head	(right)
        aa = a + (viewA / 2);
        x3 = x1 + ((Math.cos(aa) * tailLen) / 3.0);
        y3 = y1 + ((Math.sin(aa) * tailLen) / 3.0);
        graphics.drawLine((int) x1 + sx, (int) y1 + sy, (int) x3 + sx,
            (int) y3 + sy);

        // head	(left)
        aa = a - (viewA / 2);
        x3 = x1 + ((Math.cos(aa) * tailLen) / 3.0);
        y3 = y1 + ((Math.sin(aa) * tailLen) / 3.0);
        graphics.drawLine((int) x1 + sx, (int) y1 + sy, (int) x3 + sx,
            (int) y3 + sy);
    }
}
