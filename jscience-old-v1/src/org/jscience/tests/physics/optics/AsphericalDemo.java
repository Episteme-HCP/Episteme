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

import org.jscience.physics.waves.optics.elements.*;
import org.jscience.physics.waves.optics.gui.OpticalApp;
import org.jscience.physics.waves.optics.gui.OpticalCanvas;
import org.jscience.physics.waves.optics.gui.OpticalControl;
import org.jscience.physics.waves.optics.materials.ConstantParameter;
import org.jscience.physics.waves.optics.materials.Material;
import org.jscience.physics.waves.optics.rays.ParallelRays;
import org.jscience.physics.waves.optics.rays.RayCaster;
import org.jscience.physics.waves.optics.utils.*;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class AsphericalDemo extends OpticalApp {
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
    static final double initBeamY = 0.0;

    /**
     * DOCUMENT ME!
     */
    static final double initBeamWidth = 120.0;

    /**
     * DOCUMENT ME!
     */
    static final double minBeamY = -80.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxBeamY = 80.0;

    /**
     * DOCUMENT ME!
     */
    static final double initBeamWavelength = 0.45;

    /**
     * DOCUMENT ME!
     */
    static final double initAsphRadius = 50.0;

    /**
     * DOCUMENT ME!
     */
    static final double initAsphC = 1 / 30.0;

    /**
     * DOCUMENT ME!
     */
    static final double minAsphC = -1 / 10.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxAsphC = 1 / 10.0;

    /**
     * DOCUMENT ME!
     */
    static final double AsphCFactor = 1000;

    /**
     * DOCUMENT ME!
     */
    static final double initAsphK = -2.25;

    /**
     * DOCUMENT ME!
     */
    static final double minAsphK = -3.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxAsphK = 2.0;

    /**
     * DOCUMENT ME!
     */
    static final double AsphKFactor = 100.0;

    /**
     * DOCUMENT ME!
     */
    static final double initMat1Index = 1.5;

    /**
     * DOCUMENT ME!
     */
    static final double initMat2Index = 1.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxIndex = 2.0;

    /**
     * DOCUMENT ME!
     */
    static final double IndexFactor = 100.0;

    /**
     * DOCUMENT ME!
     */
    static final double initH1Width = 50.0;

    /**
     * DOCUMENT ME!
     */
    static final double initH2Width = 80.0;

    /**
     * DOCUMENT ME!
     */
    static final double initApertureRadius = 40.0;

    /**
     * DOCUMENT ME!
     */
    static final double minApertureRadius = 0.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxApertureRadius = 65.0;

    /**
     * DOCUMENT ME!
     */
    static final double initApertureY = 0.0;

    /**
     * DOCUMENT ME!
     */
    static final double minApertureY = -50.0;

    /**
     * DOCUMENT ME!
     */
    static final double maxApertureY = 50.0;

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
    static final int initBeamRays = 15;

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
    Aspherical asph;

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
    Aperture apert;

    /**
     * DOCUMENT ME!
     */
    RayCaster rc;

    /**
     * DOCUMENT ME!
     */
    Material mat1;

    /**
     * DOCUMENT ME!
     */
    Material mat2;

    /**
     * DOCUMENT ME!
     */
    Label DispIndex;

    /**
     * DOCUMENT ME!
     */
    Label DispK;

    /**
     * DOCUMENT ME!
     */
    Label DispC;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        buildDevice();
        canvas.setDevice(v);

        rc = new ParallelRays(new FPoint(initDeviceX, initDeviceY + initBeamY,
                    0.0),
                new FPoint(asph.GetX() - initDeviceX, -initBeamY, 0.0),
                initBeamRays, initBeamWidth, initBeamWavelength, mat1);
        canvas.setRayCaster(rc);

        add("Center", canvas);
        controls = new OpticalControl(this);
        buildControls();
        add("South", controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void buildDevice() {
        mat1 = new Material("Mat1", new ConstantParameter(initMat1Index));
        mat2 = new Material("Mat2", new ConstantParameter(initMat2Index));

        v = new OpticalDevice();
        v.moveAxis(initDeviceY, 0.0);
        v.moveOnAxis(initDeviceX);

        h1 = new Homogeneous(initH1Width, mat1);
        apert = new Aperture(initApertureRadius);
        h2 = new Homogeneous(initH2Width, mat1);
        asph = new Aspherical(initAsphC, initAsphK, initScreenDistance,
                initAsphRadius, mat2);

        v.append(h1);
        v.append(apert);
        v.append(h2);
        v.append(asph);
        //v.append( new Homogeneous( initScreenDistance, mat2 ) );
        v.append(new Screen(initScreenWidth, initScreenWidth));

        v.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(200, 200);

        Scrollbar sb1 = new Scrollbar( //Scrollbar.VERTICAL,
                Scrollbar.HORIZONTAL, (int) initBeamY, // initial
                1, // thumb
                (int) minBeamY, // min
                (int) maxBeamY + 1 // max
                );

        sb1.setName("Beam");
        sb1.addAdjustmentListener(controls);
        controls.add(sb1);
        //sb1.setBounds( 20, 20, 20, 110 );
        sb1.setBounds(110, 110, 250, 20);

        Label lbl1 = new Label("Beam direction");
        controls.add(lbl1);
        lbl1.setBounds(10, 110, 95, 20);

        Scrollbar sb2 = new Scrollbar( //Scrollbar.VERTICAL,
                Scrollbar.HORIZONTAL, (int) initApertureRadius, // initial
                1, // thumb
                (int) minApertureRadius, // min
                (int) maxApertureRadius + 1 // max
                );

        sb2.setName("ApertureRad");
        sb2.addAdjustmentListener(controls);
        controls.add(sb2);
        //sb2.setBounds( 50, 20, 20, 110 );
        sb2.setBounds(110, 140, 250, 20);

        Label lbl2 = new Label("Aperture rad.");
        controls.add(lbl2);
        lbl2.setBounds(10, 140, 95, 20);

        Scrollbar sb3 = new Scrollbar( //Scrollbar.VERTICAL,
                Scrollbar.HORIZONTAL, (int) initApertureY, // initial
                1, // thumb
                (int) minApertureY, // min
                (int) maxApertureY + 1 // max
                );

        sb3.setName("AperturePos");
        sb3.addAdjustmentListener(controls);
        controls.add(sb3);
        //sb3.setBounds( 80, 20, 20, 110 );
        sb3.setBounds(110, 170, 250, 20);

        Label lbl3 = new Label("Aperture Y");
        controls.add(lbl3);
        lbl3.setBounds(10, 170, 95, 20);

        Scrollbar sb4 = new Scrollbar(Scrollbar.HORIZONTAL,
                (int) (initAsphK * AsphKFactor), // initial
                1, // thumb
                (int) (minAsphK * AsphKFactor), // min
                (int) (maxAsphK * AsphKFactor) + 1 // max
                );

        sb4.setName("K");
        sb4.addAdjustmentListener(controls);
        controls.add(sb4);
        sb4.setBounds(110, 20, 250, 20);

        Label lbl4 = new Label("Asphere K");
        controls.add(lbl4);
        lbl4.setBounds(10, 20, 95, 20);

        DispK = new Label(D2S(initAsphK));
        controls.add(DispK);
        DispK.setBounds(370, 20, 95, 20);

        Scrollbar sb5 = new Scrollbar(Scrollbar.HORIZONTAL,
                (int) (initAsphC * AsphCFactor), // initial
                1, // thumb
                (int) (minAsphC * AsphCFactor), // min
                (int) (maxAsphC * AsphCFactor) + 1 // max
                );

        sb5.setName("C");
        sb5.addAdjustmentListener(controls);
        controls.add(sb5);
        sb5.setBounds(110, 50, 250, 20);

        Label lbl5 = new Label("Asphere C");
        controls.add(lbl5);
        lbl5.setBounds(10, 50, 95, 20);

        DispC = new Label(D2S(initAsphC));
        controls.add(DispC);
        DispC.setBounds(370, 50, 95, 20);

        Scrollbar sb6 = new Scrollbar(Scrollbar.HORIZONTAL,
                (int) ((initMat1Index - 1.0) * IndexFactor), // initial
                1, // thumb
                (int) (-(maxIndex - 1.0) * IndexFactor), // min
                (int) ((maxIndex - 1.0) * IndexFactor) + 1 // max
                );

        //System.out.println( sb6.getMinimum() + " " + sb6.getMaximum() );
        sb6.setName("Index");
        sb6.addAdjustmentListener(controls);
        controls.add(sb6);
        sb6.setBounds(110, 80, 250, 20);

        Label lbl6 = new Label("Index");
        controls.add(lbl6);
        lbl6.setBounds(10, 80, 95, 20);

        DispIndex = new Label("n1=" + D2S(initMat1Index) + "  " + "n2=" +
                D2S(initMat2Index));
        controls.add(DispIndex);
        DispIndex.setBounds(370, 80, 120, 20);

        /*Checkbox c = new Checkbox( "CB1", true );
        c.addItemListener( controls );
        c.setBounds( 370, 50, 100, 30 );
        controls.add( c );*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
        if (name.equals("Beam")) {
            /*System.out.println(
            initDeviceX + " " + asph.GetX() + " " +
            initDeviceY + " " + value
            );*/
            ((ParallelRays) rc).Move(new FPoint(initDeviceX,
                    initDeviceY + (double) value, 0.0),
                new FPoint(asph.GetX() - initDeviceX, -value, 0.0));
        } else if (name.equals("ApertureRad")) {
            apert.setRadius(value);
        } else if (name.equals("AperturePos")) {
            apert.moveOffAxis(value, 0.0);
        } else if (name.equals("K")) {
            asph.setK(value / AsphKFactor);
            DispK.setText(D2S(value / AsphKFactor));
        } else if (name.equals("C")) {
            asph.setC(value / AsphCFactor);
            DispC.setText(D2S(value / AsphCFactor));
        } else if (name.equals("Index")) {
            double n1;
            double n2;

            if (value < 0) {
                n1 = 1.0;
                n2 = -(value / IndexFactor) + 1;
            } else if (value == 0) {
                n1 = 1.0;
                n2 = 1.0;
            } else {
                n1 = (value / IndexFactor) + 1;
                n2 = 1.0;
            }

            ((ConstantParameter) (mat1.Parameters.get("Constant"))).SetN(n1);
            ((ConstantParameter) (mat2.Parameters.get("Constant"))).SetN(n2);
            DispIndex.setText("n1=" + D2S(n1) + "  " + "n2=" + D2S(n2));
        }

        v.rearrange();
        canvas.forceRedraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String D2S(double d) {
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