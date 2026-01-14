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

package org.jscience.awt.util;

import java.awt.*;

/**
 * A Rectangle which draws itself inside of a Component.<p>
 * <p/>
 * DrawnRectangles may have their thickness and line color set,
 * and are capable of reporting their inner getBounds (the area
 * inside the lines).<p>
 * <p/>
 * Default thickness is 2.<p>
 * <p/>
 * If not set explicitly, the line color used is three shades
 * darker than the background color of the Component being
 * drawn into.<p>
 * <p/>
 * DrawnRectangles may be clear()ed, which clears both the
 * exterior (the lines) and the interior (the area inside of
 * the lines) of the DrawnRectangle.<p>
 * <p/>
 * DrawnRectangles may also be fill()ed with a specified color
 * by calling fill(Color), or by calling setFillColor(Color)
 * followed by fill().<p>
 * <p/>
 * By default, the fill Color is the background color of the
 * Component drawn into.<p>
 *
 * @author David Geary
 * @version 1.1, Nov 8 1996
 *          <p/>
 *          Fixed off-by-one-pixel bug when filling DrawnRectangles
 * @see ThreeDRectangle
 * @see EtchedRectangle
 * @see Border
 * @see EtchedBorder
 * @see ThreeDBorder
 * @see gjt.test.DrawnRectangleTest
 */
public class DrawnRectangle extends Rectangle {
    protected static int _defaultThickness = 2;

    protected Component drawInto;
    private int thick;
    private Color lineColor, fillColor;

    public DrawnRectangle(Component drawInto) {
        this(drawInto, _defaultThickness, 0, 0, 0, 0);
    }

    public DrawnRectangle(Component drawInto, int thick) {
        this(drawInto, thick, 0, 0, 0, 0);
    }

    public DrawnRectangle(Component drawInto, int x, int y,
                          int w, int h) {
        this(drawInto, _defaultThickness, x, y, w, h);
    }

    public DrawnRectangle(Component drawInto, int thick,
                          int x, int y, int w, int h) {

        if (drawInto != null) {
            if (thick > 0) {
                //guenuine code
                //Assert.notNull(drawInto);
                //Assert.notFalse(thick > 0);

                //could have replaced by
                //assert drawInto!=null;
                //assert thick > 0;

                this.drawInto = drawInto;
                this.thick = thick;
                setBounds(x, y, w, h);
            } else
                throw new IllegalArgumentException("");
        } else
            throw new IllegalArgumentException("");

    }

    public Component component() {
        return drawInto;
    }

    public int getThickness() {
        return thick;
    }

    public void setThickness(int thick) {
        this.thick = thick;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void fill() {
        fill(getFillColor());
    }

    public Color getLineColor() {
        if (lineColor == null)
            lineColor = SystemColor.controlShadow;
        return lineColor;
    }

    public Color getFillColor() {
        if (fillColor == null)
            fillColor = drawInto.getBackground();
        return fillColor;
    }

    public Rectangle getInnerBounds() {
        return new Rectangle(x + thick, y + thick,
                width - (thick * 2), height - (thick * 2));
    }

    public void paint() {
        Graphics g = drawInto.getGraphics();
        paintFlat(g, getLineColor());
    }

    private void paintFlat(Graphics g, Color color) {
        if (g != null) {
            g.setColor(color);
            for (int i = 0; i < thick; ++i)
                g.drawRect(x + i, y + i,
                        width - (i * 2) - 1, height - (i * 2) - 1);
            g.dispose();
        }
    }

    public void clearInterior() {
        fill(drawInto.getBackground());
    }

    public void clearExterior() {
        paintFlat(drawInto.getGraphics(),
                drawInto.getBackground());
    }

    public void clear() {
        clearExterior();
        clearInterior();
    }

    public void fill(Color color) {
        Graphics g = drawInto.getGraphics();

        if (g != null) {
            Rectangle r = getInnerBounds();
            g.setColor(color);
            g.fillRect(r.x, r.y, r.width, r.height);
            setFillColor(color);
            g.dispose();
        }
    }

    public String toString() {
        return super.toString() + "[" + paramString() + "]";
    }

    public String paramString() {
        return "color=" + getLineColor() + ",thickness=" +
                thick + ",fillColor=" + getFillColor();
    }

    protected Color brighter() {
        return
                getLineColor().brighter().brighter().brighter().brighter();
    }
}
