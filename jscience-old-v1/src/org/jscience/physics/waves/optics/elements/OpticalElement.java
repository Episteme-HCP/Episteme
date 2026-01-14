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

package org.jscience.physics.waves.optics.elements;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.physics.waves.ColorConverter;
import org.jscience.physics.waves.optics.rays.Ray;
import org.jscience.physics.waves.optics.rays.RayPoint;

import java.awt.*;

/**
 * The <code>OpticalElement</code> class is an abstract class which implements the
 * standard behaviour of an optical element. Every element inherits from it.
 * <p/>
 * The standard use of this class is to build a system using <code>OpticalElement</code>
 * subclasses into an <code>OpticalDevice</code> object and to use an
 * <code>OpticalCanvas</code> object to display it.
 *
 * @author Olivier Scherler
 * @see        OpticalDevice
 * @see        org.jscience.physics.waves.optics.gui.OpticalCanvas
 */

abstract public class OpticalElement implements Cloneable {
    /*
    * Optical axis : x, left.
    * Orthogonal plane : y down, z 'into the screen'.
    */

    // Optical axis position (in y and z directions).
    private double axis_y, axis_z;
    // Position on the x axis.
    private double x;
    // Off axis offset.
    private double offaxis_y, offaxis_z;
    // Width (in direction x)
    private double width;

    /**
     * Default constructor, puts the elements at (0, 0, 0).
     */
    public OpticalElement() {
        super();
        axis_y = axis_z = 0;
        x = 0;
        offaxis_y = offaxis_z = 0;
        width = 0;
    }

    public OpticalElement(double w) {
        this();
        width = w;
    }

    public void rearrange() {
    }

    public Object clone() {
        Object o = null;

        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return o;
    }

    /**
     * Moves the element's optical axis along the y and z directions.
     * It only has effect if the element is the first one of an
     * <code>OpticalDevice</code>, because each element aligns itself
     * on the previous one.
     *
     * @param    y    the y coordinate of the optical axis.
     * @param    z    the z coordinate of the optical axis.
     */
    public void moveAxis(double y, double z) {
        axis_y = y;
        axis_z = z;
    }

    /**
     * Moves the element along the optical axis, i.e. along the x direction.
     * It only has effect if the element is the first one of an <code>OpticalDevice</code>,
     * because elements stack next to each other.
     *
     * @param    x    the x coordinate of the element.
     */
    public void moveOnAxis(double x) {
        this.x = x;
    }

    /**
     * Moves the element along the y and z directions (NOT the optical axis).
     * It is used to uncenter the element from the optical axis.
     *
     * @param    y    off-axis displacement along the y axis.
     * @param    z    off-axis displacement along the z axis.
     */
    public void moveOffAxis(double y, double z) {
        offaxis_y = y;
        offaxis_z = z;
    }

    /**
     * Gets the x coordinate of the element.
     *
     * @return the x coordinate of the element on the optical axis.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y and z coordinates of the optical axis.
     *
     * @return an <code>Double3Vector</code> with the y and z coordinates of the optical axis.
     * The x coordinate is set to 0.
     * @see        Double3Vector
     */
    public Double3Vector getAxis() {
        return new Double3Vector(0, axis_y, axis_z);
    }

    /**
     * Returns the off axis properties of the element in an <code>Double3Vector</code>.
     * Coordinate x is set to 0.
     *
     * @return an <code>Double3Vector</code> with the y and z coordinates of the element centers
     * relative to the optical axis. The x coordinate is set to 0.
     * @see        Double3Vector
     */
    public Double3Vector getOffAxis() {
        return new Double3Vector(0, offaxis_y, offaxis_z);
    }

    /**
     * Propagates a <code>Ray</code> through the element. The last <code>RayPoint</code> of
     * the <code>Ray</code> is sent to <code>PropagateRayPoint</code> and the result is
     * appended to the <code>Ray</code>.
     *
     * @param    r    the <code>Ray</code> to propagate. If it is not empty, its last
     * <code>RayPoint</code> is propagated through the element and the
     * resulting <code>RayPoint</code> is appended to its end.
     * @see        Ray
     * @see        RayPoint
     * @see        #propagateRayPoint
     */
    public void propagate(Ray r) {
        if (!r.empty()) {
            RayPoint rp = propagateRayPoint(r.last());
            r.append(rp);
        }
    }

    /**
     * Called by <code>Propagate</code>. Prepares the <code>RayPoint</code> before sending
     * it to <code>PropagateRayPointSelf</code>. Its existence validity are tested, and a
     * call to <code>TranslateOrigin</code> is made to adjust the <code>RayPoint</code> to
     * the element's local coordinates system. Then <code>PropagateRayPointSelf</code> is
     * called and eventually <code>TranslateOriginBack</code> to restore the coordinates system.
     * <p/>
     * <b>Note:</b> This method should not be called by the user, nor overridden. One
     * should only call <code>PropagateRay</code> and override <code>PropagateRayPointSelf</code>.
     *
     * @param    r    the <code>RayPoint</code> to be propagated.
     * @return the propagated <code>RayPoint</code>, to be added to the end of the <code>Ray</code>.
     * @see        RayPoint
     * @see        #propagate
     * @see        #propagateRayPointSelf
     */
    public RayPoint propagateRayPoint(RayPoint r) {
        RayPoint rp = new RayPoint();

        rp.invalidate();
        if (r != null && r.isValid()) {
            rp = (RayPoint) r.clone();
            // Translate the RayPoint in the opposite direction of the Element's OffAxis.
            translateOrigin(rp);
            propagateRayPointSelf(rp);
            // Restore the RayPoint's position.
            translateOriginBack(rp);
        }
        return rp;
    }

    /**
     * Translates the <code>RayPoint</code> in order to put the origin of its coordinates system
     * at the element's center. The element's center is its center in directions y
     * and z, and its left side in direction x. Calls <code>GetCenter</code> for the center.
     *
     * @param    rp    the <code>RayPoint</code> to translate.
     * @see        #translateOriginBack
     * @see        #getCenter
     * @see        RayPoint
     */
    public void translateOrigin(RayPoint rp) {
        Double3Vector center = getCenter();

        rp.translate(-center.x, -center.y, -center.z);
    }

    /**
     * Translates the <code>RayPoint</code> back in order to restore the origin of its coordinates system.
     * Calls GetCenter for the center.
     *
     * @param    rp    the <code>RayPoint</code> to translate.
     * @see        #translateOrigin
     * @see        #getCenter
     * @see        RayPoint
     */
    public void translateOriginBack(RayPoint rp) {
        Double3Vector center = getCenter();

        rp.translate(center.x, center.y, center.z);
    }

    /**
     * Gets the center of the element. Called by <code>TranslateOrigin</code>,
     * <code>TranslateOriginBack</code> and <code>Draw</code>. Elements can override
     * this method to set the element's center to a point that is convenient to use
     * as the origin for propagation and/or drawing.
     *
     * @return an <code>Double3Vector</code> with the coordinates of the center.
     * @see        Double3Vector
     * @see        #translateOrigin
     * @see        #translateOriginBack
     * @see        #draw
     */
    public Double3Vector getCenter() {
        return new Double3Vector(getX(), getAxis().y + getOffAxis().y, getAxis().z + getOffAxis().z);
    }

    /**
     * This method implements the element's actual behaviour. Any subclass of
     * <code>OpticalElement</code> must override it. The <code>RayPoint</code> passes
     * as a parameter is non null, valid and a clone of the last <code>RayPoint</code>
     * of the <code>Ray</code> that was passed to <code>Propagate</code>.
     * <code>PropagateRayPointSelf</code> should move the <code>RayPoint</code> to the
     * last location where the element changes the wave vector (k), and set the wave
     * vector accordingly. The <code>RayPoint's</code> position is calculated relative to
     * the element's center, given by the result of the <code>GetCenter</code> method.
     *
     * @param    r    the <code>RayPoint</code> to propagate.
     * @see        RayPoint
     * @see        #getCenter
     */
    abstract public void propagateRayPointSelf(RayPoint r);

    /*  public Beam propagate( BeamPoint b )
     {
     }*/

    /**
     * Draws the element's symbol into a <code>Graphics</code> object. The
     * <code>Graphics</code> is translated in order to place the element's center
     * at the origin via a call to <code>Graphics.translate</code>, <code>DrawSelf</code>
     * is called, which performs the actual drawing, and another call to
     * <code>Graphics.translate</code> restores the <code>Graphics</code> origin.
     * This method should not be overridden except is special cases like in
     * <code>OpticalDevice</code>. Instead, <code>DrawSelf</code> must be overriden.
     *
     * @param    g    the <code>Graphics</code> object in which the element must be drawn.
     * @see        #getCenter
     * @see        #drawSelf
     * @see        OpticalDevice#draw
     */
    public void draw(Graphics g) {
        Double3Vector center = getCenter();

        g.translate((int) Math.round(center.x), (int) Math.round(center.y));
        drawSelf(g);
        g.translate(-(int) Math.round(center.x), -(int) Math.round(center.y));
    }

    /**
     * Draws the element's symbol into a <code>Graphics</code> object. The origin is the
     * element's center, as defined by <code>GetCenter</code>. As it is abstract, every
     * subclass of <code>OpticalElement</code> must override this method.
     *
     * @param    g    the <code>Graphics</code> object in which the element must be drawn.
     */
    abstract public void drawSelf(Graphics g);

    /**
     * Draws a part of a <code>Ray</code> (a <code>RayPoint</code>) going through the element.
     * The <code>Ray</code> is drawn as a straight line connecting the previous element's
     * <code>Raypoint</code> with the current element's <code>RayPoint</code> in the
     * <code>Ray</code>. Elements that must draw <code>Rays</code> that propagate
     * differently (with multiple direction changes or elements like a index graded
     * optical fiber) should override this method.
     *
     * @param    g        the <code>Graphics</code> object in which the <code>Ray</code>
     * must be drawn.
     * @param    r        the <code>Ray</code> to draw.
     * @param    index    the index of the <code>RayPoint</code> to draw. Used to keep track of
     * the position in the <code>Ray</code> in case of multiple
     * <code>OpticalDevice</code> nesting.
     * @return the index of the next <code>RayPoint</code> to be drawn by the next element.
     * @see        Ray
     * @see        RayPoint
     * @see        OpticalDevice
     */
    public int drawRay(Graphics g, Ray r, int index) {
        Color old = g.getColor();

        if (r.raypoints().size() > index + 1) {
            Double3Vector s, e;
            RayPoint r1, r2;

            r1 = ((RayPoint) r.raypoints().elementAt(index));
            g.setColor(ColorConverter.getColor(1000 * r1.getWavelength(), 0.95));

            r2 = ((RayPoint) r.raypoints().elementAt(index + 1));

            if (r1.isValid() && r2 != null) {
                s = r1.getPosition();
                e = r2.getPosition();

                if (s.x <= e.x) {
                    int x1 = (int) Math.round(s.x),
                            y1 = (int) Math.round(s.y),
                            x2 = (int) Math.round(e.x),
                            y2 = (int) Math.round(e.y);

                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
        g.setColor(old);
        return index + 1;
    }

    /*public void drawBeam( Graphics g, BeamPoint d )
    {
    }*/

    /*public Rectangle boundsToRectangle()
    {
        return new Rectangle(
            (int)Math.round( x ),
            (int)Math.round( axis_y - height / 2 ),
            (int)Math.round( width ),
            (int)Math.round( height ) );
    }*/

    /**
     * Aligns the element on the same axis as the element passed as a parameter
     * and moves it on the x direction directly to the right of the element.
     *
     * @param    e    the element on which to align the current element. The current
     * element will be place to its right.
     */
    public void putAfter(OpticalElement e) {
        moveAxis(e.axis_y, e.axis_z);
        moveOnAxis(e.x + e.width);
    }

    /*public void putAt( OpticalElement e )
    {
        moveAxis( e.axis_y, e.axis_z );
        moveOnAxis( e.x );
    }*/

    /**
     * Gets the width of the element.
     *
     * @return the width of the element.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the element.
     *
     * @param    width    the new width of the element.
     */
    public void setWidth(double width) {
        this.width = width;
    }
}
