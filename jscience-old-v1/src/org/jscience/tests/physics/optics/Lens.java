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

import org.jscience.physics.waves.optics.elements.Homogeneous;
import org.jscience.physics.waves.optics.elements.OpticalDevice;
import org.jscience.physics.waves.optics.elements.Screen;
import org.jscience.physics.waves.optics.elements.SimpleLens;
import org.jscience.physics.waves.optics.gui.OpticalApp;
import org.jscience.physics.waves.optics.gui.OpticalCanvas;
import org.jscience.physics.waves.optics.gui.OpticalControl;
import org.jscience.physics.waves.optics.materials.ConstantParameter;
import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.rays.PointSource;
import org.jscience.physics.waves.optics.rays.RayCaster;
import org.jscience.physics.waves.optics.rays.ThreeRays;
import org.jscience.physics.waves.optics.utils.*;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Lens extends OpticalApp {
    /**
     * DOCUMENT ME!
     */
    static final double focale = 25.0;

    /**
     * DOCUMENT ME!
     */
    OpticalCanvas canvas;

    /**
     * DOCUMENT ME!
     */
    OpticalControl controls;

    /**
     * DOCUMENT ME!
     */
    OpticalDevice v;

    /**
     * DOCUMENT ME!
     */
    RayCaster RC;

    /**
     * DOCUMENT ME!
     */
    Homogeneous before;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        BuildDevice();
        canvas.setDevice(v);

        RC = new ThreeRays(new FPoint(v.GetX(), v.GetAxis().y - 15.0, 0.0),
                new FPoint((before.GetX() + before.GetWidth()) - focale,
                    v.GetAxis().y, 0.0),
                new FPoint(before.GetX() + before.GetWidth(), v.GetAxis().y, 0.0),
                477);
        canvas.setRayCaster(RC);

        add("Center", canvas);
        controls = new OpticalControl(this);
        BuildControls();
        add("South", controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void buildDevice() {
        Material vaccum = new Material("Vaccum", new ConstantParameter(1.0));

        v = new OpticalDevice();
        v.moveAxis(100, 0);
        v.moveOnAxis(20);

        before = new Homogeneous(200, vaccum);
        v.append(before);

        v.append(new SimpleLens(focale, 50));
        v.append(new Homogeneous(200, vaccum));

        v.append(new Screen(100, 100));

        v.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(200, 120);

        Scrollbar sb1 = new Scrollbar(Scrollbar.HORIZONTAL, 0, // initial
                10, // thumb
                5, // min
                210 // max
            );

        sb1.setName("RaysPos");
        sb1.addAdjustmentListener(controls);
        controls.add(sb1);
        sb1.setBounds(50, 20, 300, 20);

        Button Reset = new Button("Reset");
        Reset.setName("Reset");
        Reset.addActionListener(controls);
        controls.add(Reset);
        Reset.setBounds(50, 50, 70, 20);

        Checkbox c = new Checkbox("CB1", false);
        c.addItemListener(controls);
        c.setBounds(370, 50, 100, 30);
        controls.add(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
        /*if( name.equals( "RaysPos" ) )
        {
            FPoint newPos, newDir;
        
            newPos = new FPoint( 20.0, v.getAxis().y + value, 0 );
            //newDir = new FPoint( 50.0, - newPos.y + v.getAxis().y, 0 );
            ((PointSource)RC).move( newPos );
        }*/
        before.setWidth(value);
        ((ThreeRays) RC).SetIntersections(new FPoint((before.getX() +
                before.getWidth()) - focale, v.getAxis().y, 0.0),
            new FPoint(before.getX() + before.getWidth(), v.getAxis().y, 0.0));
        v.rearrange();
        canvas.forceRedraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void checkbox(String name, boolean value) {
        if (value) {
            //ds.setDevice( "With" );
        } else {
            //ds.setDevice( "Without" );
        }

        v.rearrange();
        canvas.forceRedraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name) {
        if (name.equals("Reset")) {
            FPoint newPos;
            FPoint newDir;

            newPos = new FPoint(20.0, v.GetAxis().y, 0);
            ((PointSource) RC).Move(newPos);
        }

        v.rearrange();
        canvas.forceRedraw();
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        remove(canvas);
        remove(controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        controls.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        controls.setEnabled(false);
    }
}
