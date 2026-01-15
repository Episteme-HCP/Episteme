import org.jscience.physics.waves.optics.elements.*;
import org.jscience.physics.waves.optics.gui.OpticalApp;
import org.jscience.physics.waves.optics.gui.OpticalCanvas;
import org.jscience.physics.waves.optics.gui.OpticalControl;
import org.jscience.physics.waves.optics.materials.ConstantParameter;
import org.jscience.physics.waves.optics.materials.Material;
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
public class FieldLensApp extends OpticalApp {
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
    double initialRadius = 100.0;

    /**
     * DOCUMENT ME!
     */
    double initialApertureRadius = 30.0;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        setLayout(new BorderLayout());
        canvas = new OpticalCanvas();
        buildDevice();
        canvas.setDevice(v);

        /*RC = new ParallelRays(
        new FPoint( 20.0, v.GetAxis().y, 0 ),
        new FPoint( 1, 0.0, 0 ),
        15, 120, 430 );*/
        RC = new PointSource(new FPoint(20.0, v.getAxis().y, 0.0),
                new FPoint(120.0, v.getAxis().y - 48.0, 0.0),
                new FPoint(120.0, v.getAxis().y + 48.0, 0.0), 9, 0.430);
        canvas.setRayCaster(RC);

        add("Center", canvas);
        controls = new OpticalControl(this);
        buildControls();
        add("West", controls);
    }

    /**
     * DOCUMENT ME!
     */
    public void buildDevice() {
        Material vaccum;
        Material glass;
        SimpleLens fl;
        Nothing nofl;

        vaccum = new Material("Vaccum", new ConstantParameter(1.0));

        v = new OpticalDevice();
        v.moveAxis(100, 0);
        v.moveOnAxis(20);

        v.append(new Homogeneous(100, vaccum));

        v.append(new SimpleLens(50, 50));
        v.append(new Homogeneous(100, vaccum));

        fl = new SimpleLens(50, 50);
        nofl = new Nothing();

        ds = new DeviceSwitcher();
        ds.addDevice("With", fl);
        ds.addDevice("Without", nofl);
        ds.setCurrentDevice("Without");
        v.append(ds);

        v.append(new Homogeneous(100, vaccum));
        v.append(new SimpleLens(50, 50));
        v.append(new Homogeneous(100, vaccum));
        v.append(new Screen(100, 100));

        v.rearrange();
    }

    /**
     * DOCUMENT ME!
     */
    public void buildControls() {
        controls.setLayout(null);
        controls.setSize(80, 250);

        Scrollbar sb1 = new Scrollbar(Scrollbar.VERTICAL, 0, // initial
                10, // thumb
                -48, // min
                58 // max
            );

        sb1.setName("RaysPos");
        sb1.addAdjustmentListener(controls);
        controls.add(sb1);
        sb1.setBounds(50, 30, 20, 140);

        Checkbox c = new Checkbox("field lens", false);
        c.addItemListener(controls);
        c.setBounds(10, 180, 70, 30);
        controls.add(c);

        Button Reset = new Button("Reset");
        Reset.setName("Reset");
        Reset.addActionListener(controls);
        controls.add(Reset);
        Reset.setBounds(10, 220, 70, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
        if (name.equals("RaysPos")) {
            FPoint newPos;
            FPoint newDir;

            newPos = new FPoint(20.0, v.GetAxis().y + value, 0);

            //newDir = new FPoint( 50.0, - newPos.y + v.GetAxis().y, 0 );
            ((PointSource) RC).Move(newPos);
        }

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
            ds.setCurrentDevice("With");
        } else {
            ds.setCurrentDevice("Without");
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

            newPos = new FPoint(20.0, v.getAxis().y, 0);
            ((PointSource) RC).Move(newPos);
            ds.setCurrentDevice("Without");
            ((Checkbox) controls.getComponent(1)).setState(false);
            ((Scrollbar) controls.getComponent(0)).setValue(0);
        }

        v.Rearrange();
        canvas.ForceRedraw();
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
