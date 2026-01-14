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
import org.jscience.physics.waves.optics.elements.ThickLens;
import org.jscience.physics.waves.optics.gui.OpticalApp;
import org.jscience.physics.waves.optics.gui.OpticalCanvas;
import org.jscience.physics.waves.optics.gui.OpticalControl;
import org.jscience.physics.waves.optics.materials.ConstantParameter;
import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.materials.Sellmeier1Parameters;
import org.jscience.physics.waves.optics.rays.PointSource;
import org.jscience.physics.waves.optics.rays.RayCaster;
import org.jscience.physics.waves.optics.utils.*;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RealDoublet extends OpticalApp {
    /**
     * DOCUMENT ME!
     */
    static final double minGap = 0.01;

    /**
     * DOCUMENT ME!
     */
    OpticalCanvas canvas;

    /**
     * DOCUMENT ME!
     */
    OpticalControl controls;

    // details of controls
    /**
     * DOCUMENT ME!
     */
    Scrollbar position_scrollbar;

    /**
     * DOCUMENT ME!
     */
    Button reset_button;

    /**
     * DOCUMENT ME!
     */
    OpticalDevice dev;

    // details of the Device
    /**
     * DOCUMENT ME!
     */
    RayCaster object_point;

    /**
     * DOCUMENT ME!
     */
    ThickLens lens1;

    /**
     * DOCUMENT ME!
     */
    ThickLens lens2;

    /**
     * DOCUMENT ME!
     */
    Homogeneous h1;

    /**
     * DOCUMENT ME!
     */
    Homogeneous h2;

    /**
     * DOCUMENT ME!
     */
    Homogeneous h3;

    /**
     * DOCUMENT ME!
     */
    Screen screen;

    /**
     * DOCUMENT ME!
     */
    double lens1Position = 100.0;

    /**
     * DOCUMENT ME!
     */
    double lensCurvatureRadius = 200.0;

    /**
     * DOCUMENT ME!
     */
    double lensDistance = 100.0;

    /**
     * DOCUMENT ME!
     */
    double lensAperture = 80.0;

    /**
     * DOCUMENT ME!
     */
    double screenDistance = 250.0;

    /**
     * DOCUMENT ME!
     */
    double screensize = 200.0;

    /**
     * DOCUMENT ME!
     */
    double lensThickness = 30.0;

    /**
     * DOCUMENT ME!
     */
    double effectiveLens1Position;

    /**
     * DOCUMENT ME!
     */
    double effectiveLens2Position;

    /**
     * DOCUMENT ME!
     */
    double effectiveScreenPosition;

    /**
     * DOCUMENT ME!
     */
    Material vacuum;

    /**
     * DOCUMENT ME!
     */
    Material BK7;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        BuildDevice();
        canvas.setDevice(dev);

        object_point = new PointSource(new FPoint(0.0, dev.getAxis().y + 0.0,
                    0.0), new FPoint(100.0, dev.getAxis().y - 48.0, 0.0),
                new FPoint(100.0, dev.getAxis().y + 48.0, 0.0), 9, 0.430);
        canvas.setRayCaster(object_point);

        add("Center", canvas);
        controls = new OpticalControl(this);
        BuildControls();
        add("South", controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void buildDevice() {
        BK7 = new Material("BK7",
                new Sellmeier1Parameters(1.03961212, 6.00069867e-3,
                    2.31792344e-1, 2.00179144e-2, 1.01046945, 1.03560653e-2));

        vacuum = new Material("Vaccum", new ConstantParameter(1.0));

        dev = new OpticalDevice();
        dev.moveAxis(150, 0);
        //dev.MoveOnAxis( 20 );

        // definition of the elements
        h1 = new Homogeneous(lens1Position, vacuum);
        lens1 = new ThickLens(-1 / lensCurvatureRadius,
                1 / lensCurvatureRadius, lensThickness, minGap, lensAperture,
                BK7, vacuum);
        effectiveLens1Position = lens1Position;
        h2 = new Homogeneous(lensDistance, vacuum);
        lens2 = new ThickLens(-1 / lensCurvatureRadius,
                1 / lensCurvatureRadius, lensThickness, minGap, lensAperture,
                BK7, vacuum);
        effectiveLens2Position = lens1Position + lensDistance;
        h3 = new Homogeneous(screenDistance, vacuum);
        screen = new Screen(screensize, screensize);
        effectiveScreenPosition = lens1Position + lensDistance +
            screenDistance;

        // appending them to the system
        dev.append(h1);
        dev.append(lens1);
        dev.append(h2);
        dev.append(lens2);
        dev.append(h3);
        dev.append(screen);

        dev.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(250, 80);

        Scrollbar sb11 = new Scrollbar(Scrollbar.HORIZONTAL, 0, // initial
                1, // thumb
                -95, // min
                155 // max
            );

        sb11.setName("lens1Pos");
        sb11.addAdjustmentListener(controls);
        controls.add(sb11);
        sb11.setBounds(10, 0, 200, 20);

        Scrollbar sb12 = new Scrollbar(Scrollbar.HORIZONTAL, 0, // initial
                1, // thumb
                -130, // min
                130 // max
            );

        sb12.setName("lens2Pos");
        sb12.addAdjustmentListener(controls);
        controls.add(sb12);
        sb12.setBounds(10, 40, 200, 20);

        /*Button Reset = new Button( "Reset" );
        Reset.setName( "Reset" );
        Reset.addActionListener( controls );
        controls.add( Reset );
        Reset.setBounds( 10, 220, 70, 20 );*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
        if (name.equals("lens1Pos")) {
            effectiveLens1Position = lens1Position + value;
            h1.setWidth(effectiveLens1Position);
            h2.setWidth(effectiveLens2Position - effectiveLens1Position);
            dev.rearrange();
        } else if (name.equals("lens2Pos")) {
            effectiveLens2Position = lens1Position + lensDistance + value;
            //effectiveScreenPosition = lens1Position + lensDistance + screenDistance;
            h2.setWidth(effectiveLens2Position - effectiveLens1Position);
            //((OpticalElement) h3).setWidth( screenDistance );
            h3.setWidth(effectiveScreenPosition - effectiveLens2Position);
            dev.rearrange();
        }

        canvas.forceRedraw();
    }

    /*        public void checkbox( String name, boolean value )
            {
                    if( value )
                    {
    //                        ds.setDevice( "With" );
                    }
                    else
                    {
    //                        ds.setDevice( "Without" );
                    }
                    dev.rearrange();
                    canvas.forceRedraw();
            }*/

    /*        public void button( String name )
        {
            if( name.equals( "Reset" ) )
            {
                FPoint newPos, newDir;
    
                newPos = new FPoint( 20.0, dev.GetAxis().y, 0 );
                ((PointSource)object_point).move( newPos );
                //ds.setDevice( "Without" );
                   ((Checkbox) controls.getComponent(1)).setState(false);
                ((Scrollbar) controls.getComponent(0)).setValue(0);
            }
            dev.rearrange();
            canvas.forceRedraw();
        }
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
