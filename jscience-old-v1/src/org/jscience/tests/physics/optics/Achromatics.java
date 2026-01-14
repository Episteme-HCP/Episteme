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

package org.jscience.physics.waves.optics.gui;

import org.jscience.physics.waves.optics.elements.*;
import org.jscience.physics.waves.optics.materials.ConradyParameters;
import org.jscience.physics.waves.optics.materials.ConstantParameter;
import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.materials.Sellmeier1Parameters;
import org.jscience.physics.waves.optics.rays.ParallelRays;
import org.jscience.physics.waves.optics.rays.RayCaster;
import org.jscience.physics.waves.optics.rays.RayCastersCollection;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Achromatics extends OpticalApp {
    /**
     * DOCUMENT ME!
     */
    static final double initDeviceX = 20.0;

    /**
     * DOCUMENT ME!
     */
    static final double initDeviceY = 100.0;

    /**
     * DOCUMENT ME!
     */
    static final double initLensDistance = 80.0;

    /**
     * DOCUMENT ME!
     */
    static final double initScreenDistance = 300.0;

    /**
     * DOCUMENT ME!
     */
    static final double initScreenWidth = 150.0;

    /**
     * DOCUMENT ME!
     */
    static final double initBeamWidth = 65.0;

    /**
     * DOCUMENT ME!
     */
    static final double initBeamY = 0.0;

    /**
     * DOCUMENT ME!
     */
    static final double lambdaRed = 0.6563;

    /**
     * DOCUMENT ME!
     */
    static final double lambdaGreen = 0.5876;

    /**
     * DOCUMENT ME!
     */
    static final double lambdaBlue = 0.4861;

    /**
     * DOCUMENT ME!
     */
    static final double minGap = 0.001;

    /**
     * DOCUMENT ME!
     */
    static final int initBeamRays = 7;

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
    Spherical s1;

    /**
     * DOCUMENT ME!
     */
    Spherical s2;

    /**
     * DOCUMENT ME!
     */
    RayCaster rcRed;

    /**
     * DOCUMENT ME!
     */
    RayCaster rcBlue;

    /**
     * DOCUMENT ME!
     */
    RayCaster rcGreen;

    /**
     * DOCUMENT ME!
     */
    RayCastersCollection rcTotal;

    /**
     * DOCUMENT ME!
     */
    DeviceSwitcher DS;

    /**
     * DOCUMENT ME!
     */
    ThickLens a1;

    /**
     * DOCUMENT ME!
     */
    ThickLens a2;

    /**
     * DOCUMENT ME!
     */
    Material BK7;

    /**
     * DOCUMENT ME!
     */
    Material F2;

    /**
     * DOCUMENT ME!
     */
    Material Vaccum;

    /**
     * DOCUMENT ME!
     */
    Material Exa;

    /**
     * DOCUMENT ME!
     */
    Material Exa2;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        BuildDevice();
        canvas.setDevice(v);

        rcRed = new ParallelRays(new FPoint(initDeviceX,
                    initDeviceY + initBeamY, 0.0), new FPoint(1.0, 0.0, 0.0),
                initBeamRays, initBeamWidth, lambdaRed, Vaccum);
        rcBlue = new ParallelRays(new FPoint(initDeviceX,
                    initDeviceY + initBeamY, 0.0), new FPoint(1.0, 0.0, 0.0),
                initBeamRays, initBeamWidth, lambdaBlue, Vaccum);
        rcGreen = new ParallelRays(new FPoint(initDeviceX,
                    initDeviceY + initBeamY, 0.0), new FPoint(1.0, 0.0, 0.0),
                initBeamRays, initBeamWidth, lambdaGreen, Vaccum);

        rcTotal = new RayCastersCollection();
        rcTotal.append(rcRed);
        rcTotal.append(rcBlue);

        canvas.setRayCaster(rcTotal);
        //canvas.SetRayCaster( rcRed );
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

        F2 = new Material("F2",
                new Sellmeier1Parameters(1.34533359, 9.97743871e-3,
                    2.09073176e-1, 4.70450767e-2, 9.37357162e-1, 1.11886764e+2));

        Exa = new Material("Exa", new ConradyParameters(1.3, 0.1, 0.0));

        Exa2 = new Material("Exa2", new ConradyParameters(1.1, 0.2, 0.0));

        /*System.out.println( "Exa" );
        System.out.println( Exa.AbbeIndex() );
        System.out.println( Exa.AbbeNumber() );
        System.out.println( "Exa2" );
        System.out.println( Exa2.AbbeIndex() );
        System.out.println( Exa2.AbbeNumber() );*/

        //System.out.println( Exa.IndexAtWavelength( 0.6563 ) );
        Vaccum = new Material("Vaccum", new ConstantParameter(1.0));

        v = new OpticalDevice();
        v.moveAxis(initDeviceY, 0.0);
        v.moveOnAxis(initDeviceX);

        v.append(new Homogeneous(initLensDistance, Vaccum));

        DS = new DeviceSwitcher();

        DS.addDevice("1",
            new ThickLens(-1 / 100.0, 1 / 100.0, 60.0, 50.0, 60.0, Exa, Vaccum));

        OpticalDevice temp = new OpticalDevice();
        a1 = new ThickLens(-1 / 100.0, 1 / 100.0, 60.0, minGap, 60.0, Exa, Exa2);
        a2 = new ThickLens(1 / 100.0, 0.0, 20.0, 20.0, 60.0, Exa2, Vaccum);
        temp.append(a1);
        temp.append(a2);

        DS.addDevice("2", temp);
        DS.setCurrentDevice("1");
        //DS.rearrange();
        v.append(DS);
        //v.append( temp );
        v.append(new Homogeneous(initScreenDistance, Vaccum));
        v.append(new Screen(initScreenWidth, initScreenWidth));

        v.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(200, 200);

        Button b1 = new Button("Both");
        b1.addActionListener(controls);
        b1.setBounds(20, 20, 80, 20);
        controls.add(b1);

        b1 = new Button("Red");
        b1.addActionListener(controls);
        b1.setBounds(120, 20, 80, 20);
        controls.add(b1);

        b1 = new Button("Variable");
        b1.addActionListener(controls);
        b1.setBounds(20, 50, 80, 20);
        controls.add(b1);

        b1 = new Button("Blue");
        b1.addActionListener(controls);
        b1.setBounds(120, 50, 80, 20);
        controls.add(b1);

        b1 = new Button("Normal");
        b1.addActionListener(controls);
        b1.setBounds(20, 80, 80, 20);
        controls.add(b1);

        b1 = new Button("Achromat");
        b1.addActionListener(controls);
        b1.setBounds(120, 80, 80, 20);
        controls.add(b1);

        /*        b1 = new Button( "Print" );
              b1.addActionListener( controls );
              b1.setBounds( 220, 80, 80, 20 );
              controls.add( b1 );
        */
        Scrollbar sb = new Scrollbar(Scrollbar.HORIZONTAL, 486, // initial
                20, // thumb
                400, // min
                657 + 20 // max
                );

        sb.setName("Lambda");
        sb.addAdjustmentListener(controls);
        controls.add(sb);
        sb.setBounds(20, 120, 300, 20);

        sb = new Scrollbar(Scrollbar.HORIZONTAL, 0, // initial
                2, // thumb
                -80, // min
                80 + 2 // max
                );

        sb.setName("C3");
        sb.addAdjustmentListener(controls);
        controls.add(sb);
        sb.setBounds(20, 150, 300, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name) {
        if (name.equals("Both")) {
            canvas.setRayCaster(rcTotal);
        }

        if (name.equals("Red")) {
            canvas.setRayCaster(rcRed);
        }

        if (name.equals("Variable")) {
            canvas.setRayCaster(rcGreen);
        }

        if (name.equals("Blue")) {
            canvas.setRayCaster(rcBlue);
        }

        if (name.equals("Normal")) {
            DS.setCurrentDevice("1");
        }

        if (name.equals("Achromat")) {
            DS.setCurrentDevice("2");
        }

        if (name.equals("Print")) {
            System.out.println(Exa.indexAtWavelength(
                    ((ParallelRays) rcGreen).GetWavelength()) + " " +
                Exa2.indexAtWavelength(((ParallelRays) rcGreen).GetWavelength()));
        }

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
        if (value == true) {
            canvas.setRayCaster(rcBlue);

            //DS.SetDevice( "2" );
        } else {
            canvas.setRayCaster(rcRed);

            //DS.SetDevice( "1" );
        }

        v.rearrange();
        canvas.forceRedraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
        if (name.equals("Lambda")) {
            ((ParallelRays) rcGreen).SetWavelength(value / 1000.0);
            canvas.forceRedraw();
        }

        if (name.equals("C3")) {
            double c1;
            double c2;
            double c3;
            double v1;
            double v2;
            double f1;
            double f2;
            double f = 200.0;

            v1 = Exa.abbeNumber();
            v2 = Exa2.abbeNumber();

            f1 = f * (1 - (v2 / v1));
            f2 = f * (1 - (v1 / v2));

            c3 = value / 10000.0;
            c2 = (1 / (f2 * (Exa2.abbeIndex() - 1))) + c3;
            c1 = (1 / (f1 * (Exa.abbeIndex() - 1))) + c2;
            a1.setC1(-c1);
            a1.setC2(-c2);
            a2.setC1(-c2);
            a2.setC2(-c3);
            v.rearrange();
            canvas.forceRedraw();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String d2S(double d) {
        return Double.toString(Math.round(d * 100.0) / 100.0);
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
