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
public class PlanoConvex extends OpticalApp {
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
    Homogeneous h;

    /**
     * DOCUMENT ME!
     */
    DeviceSwitcher ds;

    /**
     * DOCUMENT ME!
     */
    RayCaster RC;

    /**
     * DOCUMENT ME!
     */
    Material vacuum;

    /**
     * DOCUMENT ME!
     */
    double LensRadius = 100.0;

    /**
     * DOCUMENT ME!
     */
    double LensAperture = 100.0;

    /**
     * DOCUMENT ME!
     */
    double BeamWidth = 100.0;

    /**
     * DOCUMENT ME!
     */
    double Wavelength = 0.4;

    /**
     * DOCUMENT ME!
     */
    double PlaneCurvature = 0.0;

    /**
     * DOCUMENT ME!
     */
    double LensThickness = 30.0;

    /**
     * DOCUMENT ME!
     */
    double LensWidth = 50.0;

    /**
     * DOCUMENT ME!
     */
    double Distance = 300.0;

    /**
     * DOCUMENT ME!
     */
    int RaysNumber = 20;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        BuildDevice();
        canvas.setDevice(v);

        vacuum = new Material("Vacuum", new ConstantParameter(1.0));

        RC = new ParallelRays(new FPoint(10.0, 100.0, 0.0),
                new FPoint(1.0, 0.0, 0.0), RaysNumber, BeamWidth, Wavelength,
                vacuum);
        canvas.setRayCaster(RC);

        add("Center", canvas);
        controls = new OpticalControl(this);
        buildControls();
        add("South", controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void buildDevice() {
        Material vaccum;
        Material glass;
        ThickLens forward;
        ThickLens backward;

        vacuum = new Material("Vacuum", new ConstantParameter(1.0));
        glass = new Material("Glass", new ConstantParameter(1.5));

        v = new OpticalDevice();
        v.moveAxis(100, 0);
        v.moveOnAxis(20);

        v.append(new Homogeneous(40, vacuum));

        forward = new ThickLens(-1 / LensRadius, PlaneCurvature, LensThickness,
                LensWidth, LensAperture, glass, vacuum);
        backward = new ThickLens(PlaneCurvature, 1 / LensRadius, LensThickness,
                LensWidth, LensAperture, glass, vacuum);

        ds = new DeviceSwitcher();
        ds.addDevice("Right", forward);
        ds.addDevice("Wrong", backward);
        ds.setCurrentDevice("Wrong");
        v.append(ds);

        v.append(new Homogeneous(Distance, vacuum));
        v.append(new Screen(100, 100));

        v.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(200, 60);

        Button Wrong = new Button("Wrong");
        Wrong.setName("Wrong orientation");
        Wrong.addActionListener(controls);
        controls.add(Wrong);
        Wrong.setBounds(240, 20, 60, 20);

        Button Right = new Button("Right");
        Right.setName("Right orientation");
        Right.addActionListener(controls);
        controls.add(Right);
        Right.setBounds(150, 20, 60, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name) {
        if (name.equals("Right")) {
            ds.setCurrentDevice("Right");
        } else {
            ds.setCurrentDevice("Wrong");
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
