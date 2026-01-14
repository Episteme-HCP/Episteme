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
 * A panel containing a single component, around which a border
 * is drawn.  Of course, the single component may be a
 * container which may contain other components, so a Border
 * can surround multiple components.<p>
 * <p/>
 * Thickness of the border, and the gap between the Component
 * and the border are specified at time of construction.
 * Default border thickness is 2 - default gap is 0.<p>
 * <p/>
 * Border color may be set via setLineColor(Color).<p>
 * <p/>
 * Border employs a DrawnRectangle to paint the border.  Derived
 * classes are free to override DrawnRectangle border() if they
 * wish to use an extension of DrawnRectangle for drawing their
 * border.<p>
 * <p/>
 * The following code snippet, from gjt.test.BorderTest creates
 * an AWT Button, and embeds the button in a border.  That
 * border is then embedded in another border.  The AWT Button
 * winds up inside of a cyan border with a pixel width of 7,
 * inside of a black border (pixel width 2):<p>
 * <p/>
 * <pre>
 *      private Border makeBorderedAWTButton() {
 *          Button button;
 *          Border cyanBorder, blackBorder;
 * <p/>
 *          button = new Button("Button Inside Two Borders");
 *          cyanBorder = new Border(button, 7);
 *          cyanBorder.setLineColor(Color.cyan);
 * <p/>
 *          blackBorder = new Border(cyanBorder);
 * <p/>
 *          return blackBorder;
 *      }
 * </pre>
 *
 * @author David Geary
 * @version 1.1, Nov 8 1996
 *          <p/>
 *          Added getComponent() for accessing component bordered.
 * @see DrawnRectangle
 * @see ThreeDBorder
 * @see EtchedBorder
 * @see gjt.test.BorderTest
 */
public class Border extends Panel {
    protected int thickness;
    protected int gap;
    protected DrawnRectangle border;
    protected Component borderMe;

    protected static int _defaultThickness = 2;
    protected static int _defaultGap = 0;

    public Border(Component borderMe) {
        this(borderMe, _defaultThickness, _defaultGap);
    }

    public Border(Component borderMe, int thickness) {
        this(borderMe, thickness, _defaultGap);
    }

    public Border(Component borderMe, int thickness, int gap) {
        this.borderMe = borderMe;
        this.thickness = thickness;
        this.gap = gap;

        setLayout(new BorderLayout());
        add(borderMe, "Center");
    }

    public Component getComponent() {
        return borderMe;
    }

    public Rectangle getInnerBounds() {
        return border().getInnerBounds();
    }

    public void setLineColor(Color c) {
        border().setLineColor(c);
    }

    public Color getLineColor() {
        return border().getLineColor();
    }

    public void paint(Graphics g) {
        border().paint();
        super.paint(g);  // ensures lightweight comps get drawn
    }

    /**
     * @deprecated for JDK1.1
     */
    public Insets insets() {
        return new Insets(thickness + gap, thickness + gap,
                thickness + gap, thickness + gap);
    }

    public Insets getInsets() {
        return insets();
    }

    /**
     * @deprecated for JDK1.1
     */
    public void resize(int w, int h) {
        Point location = getLocation();
        setBounds(location.x, location.y, w, h);
    }

    public void setSize(int w, int h) {
        resize(w, h);
    }

    /**
     * @deprecated for JDK1.1
     */
    public void reshape(int x, int y, int w, int h) {
        // compiler will issue a deprecation warning, but we can't call
        // super.setBounds()!
        super.reshape(x, y, w, h);
        border().setSize(w, h);
    }

    public void setBounds(int x, int y, int w, int h) {
        reshape(x, y, w, h);
    }

    protected String paramString() {
        return super.paramString() + ",border=" +
                border().toString() + ",thickness=" + thickness
                + ",gap=" + gap;
    }

    protected DrawnRectangle border() {
        if (border == null)
            border = new DrawnRectangle(this, thickness);
        return border;
    }
}
