/*
 * FlockingAgent.java
 * Created on 21 July 2004, 19:27
 *
 * Copyright 2004, Generation5. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.episteme.natural.computing.ai.simulation;

import org.episteme.natural.computing.ai.agents.Agent;
import org.episteme.natural.computing.ai.agents.Behavior;
import org.episteme.natural.computing.ai.agents.Environment;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
public class FlockingAgent implements Agent {
    private final UUID id = UUID.randomUUID();
    private String name;
    private Environment environment;
    private final List<Behavior> behaviors = new ArrayList<>();

    //    protected static final int tailLen = 10;
    /** The random seed used to generate positional data. */
    protected static Random rnd;

    /** The width of the flocking agent's world. */
    protected int worldRows;

    /** The height of the flocking agent's world. */
    protected int worldCols;

    /** The viewing angle of the agent. */
    protected static double viewA;

    /** The visual avoidance angle (in radians). */
    protected static double vAvoidA;

    /** The minimum velocity magnitude. */
    protected static double minV;

    /** The radius for the alignment (copy) rule. */
    protected static double copyR;

    /** The radius for the cohesion (centroid) rule. */
    protected static double centroidR;

    /** The radius for the separation (avoidance) rule. */
    protected static double avoidR;

    /** The radius for visual avoidance. */
    protected static double vAvoidR;

    /** The weight for the alignment (copy) rule. */
    protected static double copyW;

    /** The weight for the cohesion (centroid) rule. */
    protected static double centroidW;

    /** The weight for the separation (avoidance) rule. */
    protected static double avoidW;

    /** The weight for the visual avoidance rule. */
    protected static double vAvoidW;

    /** The weight for random motion (noise). */
    protected static double randW;

    /** The time step for the simulation. */
    protected static double dt;

    /** The damping/inertia factor (0.0 to 1.0). High values mean more inertia. */
    protected static double ddt;

    /** The flock this agent is in. */
    protected FlockingAgent[] myFlock;

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
        positionX = Math.abs(rnd.nextInt() % (worldCols > 0 ? worldCols : 800));
        positionY = Math.abs(rnd.nextInt() % (worldRows > 0 ? worldRows : 600));

        // Set the random velocity
        vx = (2 * rnd.nextDouble()) - 1;
        vy = (2 * rnd.nextDouble()) - 1;

        // Normalize the results
        double[] n = normalize(vx, vy);
        vx = n[0];
        vy = n[1];
        this.name = "Boid-" + id.toString().substring(0, 8);
        Behavior flockBehavior = new FlockingBehavior(this, -1);
        flockBehavior.setAgent(this);
        addBehavior(flockBehavior);
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public void addBehavior(Behavior behavior) { 
        behavior.setAgent(this);
        behaviors.add(behavior); 
    }

    @Override
    public void removeBehavior(Behavior behavior) { behaviors.remove(behavior); }

    @Override
    public Environment getEnvironment() { return environment; }

    @Override
    public void setEnvironment(Environment environment) { this.environment = environment; }

    @Override
    public void receive(org.episteme.natural.computing.ai.agents.acl.ACLMessage message) {
        // Flocking agents don't use ACL messages yet.
    }

    @Override
    public void interact(Agent other) {
        // Flocking agents interact through perception in computeNewHeading
    }

    @Override
    public void run() {
        for (Behavior b : behaviors) {
            if (!b.done()) b.action();
        }
    }



    /**
     * Sets the dimensions of the toroidal world.
     *
     * @param rows height of the world
     * @param cols width of the world
     */
    public void setWorldDimensions(int rows, int cols) {
        this.worldRows = rows;
        this.worldCols = cols;
    }
    
    /**
     * Initializes miscellaneous simulation parameters.
     *
     * @param r random number generator
     * @param rr unused parameter (legacy)
     * @param cc unused parameter (legacy)
     * @param va view angle in degrees
     * @param vaa visual avoidance angle in degrees
     * @param mv minimum velocity
     */
    public static void initMisc(Random r, int rr, int cc,
        double va, double vaa, double mv) {
        rnd = r;
        viewA = (va * Math.PI) / 180.0;
        vAvoidA = (vaa * Math.PI) / 180.0;
        minV = mv;
    }

    /**
     * Initializes the interaction radii for the Boids rules.
     *
     * @param cr alignment (copy) radius
     * @param ccr cohesion (centroid) radius
     * @param ar separation (avoid) radius
     * @param vr visual avoidance radius
     */
    public static void initRadii(double cr, double ccr, double ar, double vr) {
        copyR = cr;
        centroidR = ccr;
        avoidR = ar;
        vAvoidR = vr;
    }

    /**
     * Initializes the weights for the Boids rules.
     *
     * @param cw alignment (copy) weight
     * @param ccw cohesion (centroid) weight
     * @param aw separation (avoid) weight
     * @param vw visual avoidance weight
     * @param rw random perturbation weight
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
     * Initializes time parameters.
     *
     * @param t time step (delta time)
     * @param tt damping/inertia factor
     */
    public static void initTime(double t, double tt) {
        dt = t;
        ddt = tt;
    }

    /**
     * Updates the reference to the entire flock.
     *
     * @param flock array of all agents in the flock
     */
    public void setFlock(FlockingAgent[] flock) {
        this.myFlock = flock;
    }

    /**
     * Normalizes a 2D vector.
     *
     * @param x x component
     * @param y y component
     * @return the normalized vector {x, y}
     */
    protected static double[] normalize(double x, double y) {
        double l = len(x, y);

        if (l != 0.0) {
            return new double[] { x / l, y / l };
        }
        return new double[] { x, y };
    }

     /**
     * Calculates the Euclidean length of a 2D vector.
     *
     * @param x the x component
     * @param y the y component
     *
     * @return the length
     */
    protected static double len(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Calculates the Euclidean distance between two points.
     *
     * @param x1 x-coordinate of the first point
     * @param y1 y-coordinate of the first point
     * @param x2 x-coordinate of the second point
     * @param y2 y-coordinate of the second point
     *
     * @return the distance
     */
    protected static double dist(double x1, double y1, double x2, double y2) {
        return len(x2 - x1, y2 - y1);
    }

    /**
     * Calculates the dot product of two 2D vectors.
     *
     * @param x1 x component of vector 1
     * @param y1 y component of vector 1
     * @param x2 x component of vector 2
     * @param y2 y component of vector 2
     *
     * @return the dot product
     */
    protected static double dot(double x1, double y1, double x2, double y2) {
        return ((x1 * x2) + (y1 * y2));
    }

    /**
     * Computes the new velocity vector for this agent based on Boids rules.
     *
     * @param self index of this agent in the flock array
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

            for (int j = -worldCols; j <= worldCols; j += worldCols) {
                for (int k = -worldRows; k <= worldRows; k += worldRows) {
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
            double[] n = normalize(xa, ya);
            xa = n[0];
            ya = n[1];
        }

        if (len(xb, yb) > 1.0) {
            double[] n = normalize(xb, yb);
            xb = n[0];
            yb = n[1];
        }

        if (len(xc, yc) > 1.0) {
            double[] n = normalize(xc, yc);
            xc = n[0];
            yc = n[1];
        }

        if (len(xd, yd) > 1.0) {
            double[] n = normalize(xd, yd);
            xd = n[0];
            yd = n[1];
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
     * Updates the agent's position and velocity based on the computed heading.
     * Handles toroidal world wrap-around.
     */
    public void update() {
        vx = nvx;
        vy = nvy;
        positionX += (vx * dt);
        positionY += (vy * dt);

        // Apply torodial geometry (wrap around)
        if (positionX < 0) {
            positionX += worldCols;
        } else if (positionX >= worldCols) {
            positionX -= worldCols;
        }

        if (positionY < 0) {
            positionY += worldRows;
        } else if (positionY >= worldRows) {
            positionY -= worldRows;
        }
    }

    /**
     * Renders the agent.
     *
     * @param graphics the graphics context
     * @param sx scroll x offset
     * @param sy scroll y offset
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
        double[] n = normalize(x3, y3);
        x3 = n[0];
        y3 = n[1];
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
