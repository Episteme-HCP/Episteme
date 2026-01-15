package org.jscience.astronomy.catalogs.yale;


//this is stolen and rebundled after:
//http://javalab.uoregon.edu/dcaley/sky/Sky.html
//dcaley@germane-software.com
//this is illegal!
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.BitSet;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.media.j3d.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.vecmath.*;


//import org.jscience.astronomy.solarsystem.constellations.Constellation;
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class YaleSkyViewer extends JApplet implements MouseListener,
    MouseMotionListener, KeyListener, ActionListener, ChangeListener, Runnable {
    /** DOCUMENT ME! */
    BoundingSphere bounds;

    /** DOCUMENT ME! */
    SimpleUniverse universe;

    /** DOCUMENT ME! */
    Shape3D stars;

    /** DOCUMENT ME! */
    Shape3D grid;

    /** DOCUMENT ME! */
    Shape3D constellations;

    /** DOCUMENT ME! */
    Shape3D temp;

    /** DOCUMENT ME! */
    View view;

    /** DOCUMENT ME! */
    ViewingPlatform vp = new ViewingPlatform();

    /** DOCUMENT ME! */
    TransformGroup camera = new TransformGroup();

    /** DOCUMENT ME! */
    TransformGroup[] bodies_transform;

    /** DOCUMENT ME! */
    YaleSky3D canvas3D;

    /** DOCUMENT ME! */
    TransparencyAttributes transp;

    /** DOCUMENT ME! */
    PointArray pa;

    /** DOCUMENT ME! */
    LineArray la;

    /** DOCUMENT ME! */
    LineArray ca;

    /** DOCUMENT ME! */
    LineArray ta;

    /** DOCUMENT ME! */
    double size = 10;

    //J3DGraphics2D g;
    /** DOCUMENT ME! */
    double[] ra;

    /** DOCUMENT ME! */
    double[] dec;

    /** DOCUMENT ME! */
    Color4f[] color;

    /** DOCUMENT ME! */
    Color4f[] con_color;

    /** DOCUMENT ME! */
    double[] vmag;

    /** DOCUMENT ME! */
    double[] par;

    /** DOCUMENT ME! */
    String[] name;

    /** DOCUMENT ME! */
    int[] sao;

    /** DOCUMENT ME! */
    Vector lines = new Vector();

    /** DOCUMENT ME! */
    Color4f grid_color = new Color4f(0.5f, 0.5f, 0.5f, 1.0f);

    /** DOCUMENT ME! */
    JMenuItem b_hemisphere = new JMenuItem("Cut Hemisphere");

    /** DOCUMENT ME! */
    JMenuItem b_sphere = new JMenuItem("Full Sky");

    /** DOCUMENT ME! */
    JMenuItem b_optimal = new JMenuItem("Optimal View");

    /** DOCUMENT ME! */
    JMenuItem b_center = new JMenuItem("Center View");

    /** DOCUMENT ME! */
    JRadioButton b_sun = new JRadioButton("Loot at Sun", true);

    /** DOCUMENT ME! */
    JRadioButton b_opposite = new JRadioButton("Look Opposite Sun");

    /** DOCUMENT ME! */
    JMenuItem b_stars_all = new JMenuItem("Display All Stars");

    /** DOCUMENT ME! */
    JMenuItem b_stars_none = new JMenuItem("Hide All Stars");

    /** DOCUMENT ME! */
    JMenuItem b_brightest = new JMenuItem("Display Brightest Stars");

    /** DOCUMENT ME! */
    JCheckBox cb_rotate = new JCheckBox("Rotate", false);

    /** DOCUMENT ME! */
    JCheckBox cb_grid = new JCheckBox("Display Grid", true);

    /** DOCUMENT ME! */
    JMenuItem b_ss_all = new JMenuItem("Display Solar System");

    /** DOCUMENT ME! */
    JMenuItem b_ss_none = new JMenuItem("Hide Solar System");

    /** DOCUMENT ME! */
    JCheckBox cb_sun = new JCheckBox("Sun", true);

    /** DOCUMENT ME! */
    JCheckBox cb_mercury = new JCheckBox("Mercury", true);

    /** DOCUMENT ME! */
    JCheckBox cb_venus = new JCheckBox("Venus", true);

    /** DOCUMENT ME! */
    JCheckBox cb_mars = new JCheckBox("Mars", true);

    /** DOCUMENT ME! */
    JCheckBox cb_jupiter = new JCheckBox("Jupiter", true);

    /** DOCUMENT ME! */
    JCheckBox cb_saturn = new JCheckBox("Saturn", true);

    /** DOCUMENT ME! */
    JTextField tf_brightest = new JTextField(4);

    /** DOCUMENT ME! */
    JTextField latitude = new JTextField(4);

    /** DOCUMENT ME! */
    JSlider s_view = new JSlider(1, 11, 6);

    /** DOCUMENT ME! */
    JSlider s_rotate = new JSlider(0, 20, 0);

    /** DOCUMENT ME! */
    JSlider s_bright;

    /** DOCUMENT ME! */
    int star_count = 0;

    /** DOCUMENT ME! */
    int brightest = 0;

    /** DOCUMENT ME! */
    double camera_offset = 0;

    /** DOCUMENT ME! */
    Thread thread;

    /** DOCUMENT ME! */
    Shape3D[] bodies;

    /** DOCUMENT ME! */
    PickCanvas pickCanvas;

    /** DOCUMENT ME! */
    Vector con_names = new Vector();

    /** DOCUMENT ME! */
    JMenuBar menubar = new JMenuBar();

    /** DOCUMENT ME! */
    JMenu select_con;

    /** DOCUMENT ME! */
    JLabel l_position = new JLabel("", JLabel.RIGHT);

    /** DOCUMENT ME! */
    JLabel l_bright = new JLabel("", JLabel.CENTER);

    /** DOCUMENT ME! */
    Image[] symbols = new Image[6];

    /** DOCUMENT ME! */
    BitSet bs;

    /** DOCUMENT ME! */
    Switch sw;

    /** DOCUMENT ME! */
    Color3f[] body_color = {
            new Color3f(1.0f, 0.5f, 0.0f), new Color3f(1.0f, 1.0f, 1.0f),
            new Color3f(0.0f, 1.0f, 0.0f), new Color3f(1.0f, 0.0f, 0.0f),
            new Color3f(1.0f, 1.0f, 0.0f), new Color3f(0.0f, 0.0f, 1.0f)
        };

    /** DOCUMENT ME! */
    double x;

    /** DOCUMENT ME! */
    double y;

    /** DOCUMENT ME! */
    double z;

    /** DOCUMENT ME! */
    double w;

    /** DOCUMENT ME! */
    double t;

    /** DOCUMENT ME! */
    double d;

    /** DOCUMENT ME! */
    Transform3D t1 = new Transform3D();

    /** DOCUMENT ME! */
    Vector3d v = new Vector3d();

    /** DOCUMENT ME! */
    double rx;

    /** DOCUMENT ME! */
    double ry;

    /** DOCUMENT ME! */
    double rz;

    /** DOCUMENT ME! */
    int startx;

    /** DOCUMENT ME! */
    int starty;

    /** DOCUMENT ME! */
    double drx = Math.PI / 2000;

    /** DOCUMENT ME! */
    double dry = Math.PI / 2000;

    /** DOCUMENT ME! */
    double drz = Math.PI / 2000;

    /** DOCUMENT ME! */
    double distance = size;

    /** DOCUMENT ME! */
    double min_distance = 0.1;

    /** DOCUMENT ME! */
    double l_ra = 0;

    /** DOCUMENT ME! */
    double l_dec = 0;

    /** DOCUMENT ME! */
    double[] au = { 0.3871, 0.7233, 1.5237, 5.2028, 9.5388 };

    /** DOCUMENT ME! */
    double[] rev = { 87.97, 224.70, 686.98, 4431.98, 10760.56 };

    /** DOCUMENT ME! */
    double[] inc = { 7.004, 3.394, 1.850, 1.308, 2.488 };

    /** DOCUMENT ME! */
    Point3d p1 = new Point3d(v);

    /** DOCUMENT ME! */
    Point3d p2 = new Point3d();

    /** DOCUMENT ME! */
    Color4f white = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);

    /** DOCUMENT ME! */
    Color4f clear = new Color4f(0.0f, 0.0f, 0.0f, 0.0f);

    /** DOCUMENT ME! */
    int temp_count = 0;

    /** DOCUMENT ME! */
    int index0 = -1;

    /** DOCUMENT ME! */
    int index1 = -1;

    /** DOCUMENT ME! */
    int index2 = -1;

    /** DOCUMENT ME! */
    Vector list = new Vector();

    /** DOCUMENT ME! */
    boolean hemisphere = false;

    /** DOCUMENT ME! */
    boolean pause = false;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        URL text = null;

        try {
            text = new URL(getCodeBase(),
                    "org/jscience/astronomy/milkyway/yale/yalestars.txt");

            InputStream is = text.openStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer data_string = new StringBuffer();
            String s;

            do {
                s = br.readLine();

                if (s != null) {
                    data_string.append(s + '\t');
                    star_count++;
                } //if
            } //do
             while (s != null);

            sao = new int[star_count];
            ra = new double[star_count];
            dec = new double[star_count];
            color = new Color4f[star_count];
            con_color = new Color4f[star_count];
            vmag = new double[star_count];
            par = new double[star_count];
            name = new String[star_count];

            StringTokenizer st = new StringTokenizer(data_string.toString());

            for (int i = 0; st.hasMoreTokens(); i++) {
                sao[i] = Integer.parseInt(st.nextToken());
                ra[i] = Double.parseDouble(st.nextToken());
                ra[i] += (Double.parseDouble(st.nextToken()) / 60.0);
                ra[i] += (Double.parseDouble(st.nextToken()) / 60.0 / 60.0);
                s = st.nextToken();
                dec[i] = Double.parseDouble(st.nextToken());
                dec[i] += (Double.parseDouble(st.nextToken()) / 60.0);
                dec[i] += (Double.parseDouble(st.nextToken()) / 60.0 / 60.0);

                if (s.equals("-")) {
                    dec[i] *= -1;
                } //if

                vmag[i] = Double.parseDouble(st.nextToken());
                par[i] = Double.parseDouble(st.nextToken());

                /*
                s = st.nextToken();
                if(!s.equals("."))
                    name[i] = s;
                color[i] = 0.0;
                */
                name[i] = Integer.toString(sao[i]);
            } //while

            data_string = new StringBuffer();

            st = new StringTokenizer(data_string.toString());

            while (st.hasMoreTokens()) {
                lines.addElement(new Point(Integer.parseInt(st.nextToken()),
                        Integer.parseInt(st.nextToken())));
            } //while
        } //try
        catch (Exception e) {
            e.printStackTrace();
        } //catch

        try {
            symbols[0] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/sun.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            symbols[1] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/mercury.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            symbols[2] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/venus.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            symbols[3] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/mars.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            symbols[4] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/saturn.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            symbols[5] = getImage(getCodeBase(),
                    "org/jscience/astronomy/solarsystem/constellations/jupiter.png")
                             .getScaledInstance(20, 20, Image.SCALE_SMOOTH);

            MediaTracker mt = new MediaTracker(this);

            for (int i = 0; i < symbols.length; i++) {
                mt.addImage(symbols[i], i);
            } //for

            mt.waitForAll();
        } //try
        catch (Exception e) {
            e.printStackTrace();
        } //catch

        canvas3D = new YaleSky3D();
        canvas3D.addMouseMotionListener(this);
        canvas3D.addMouseListener(this);
        canvas3D.addKeyListener(this);

        transp = new TransparencyAttributes();
        transp.setCapability(TransparencyAttributes.ALLOW_MODE_WRITE);
        transp.setTransparencyMode(TransparencyAttributes.BLENDED);

        BranchGroup scene = createSceneGraph();

        VirtualUniverse vu = new VirtualUniverse();
        camera = vp.getMultiTransformGroup().getTransformGroup(0);

        Viewer viewer = new Viewer(canvas3D);
        viewer.setViewingPlatform(vp);
        view = viewer.getView();
        view.setBackClipDistance(size * 20);
        view.setFieldOfView(Math.PI / 2);

        javax.media.j3d.Locale l = new javax.media.j3d.Locale(vu);
        l.addBranchGraph(scene);
        System.gc();

        JPanel controls = new JPanel(new BorderLayout());

        //controls.add(b_hemisphere);
        b_hemisphere.addActionListener(this);

        //controls.add(b_sphere);
        b_sphere.addActionListener(this);

        //controls.add(tf_brightest);
        //controls.add(b_brightest);
        b_brightest.addActionListener(this);

        //controls.add(new JLabel("Latitude:"));
        //controls.add(latitude);
        latitude.setText("0.0");
        latitude.setEditable(false);

        //controls.add(cb_rotate);
        //controls.add(s_view);
        s_view.setPaintLabels(true);
        s_view.setPaintTicks(true);
        s_view.setSnapToTicks(true);
        s_view.setMajorTickSpacing(1);
        s_view.addChangeListener(this);

        //controls.add(cb_grid);
        cb_grid.addActionListener(this);

        //s_rotate.addChangeListener(this);
        s_rotate.setSnapToTicks(true);
        s_rotate.setMajorTickSpacing(1);
        b_sun.addActionListener(this);
        b_opposite.addActionListener(this);
        b_center.addActionListener(this);
        b_optimal.addActionListener(this);
        b_stars_all.addActionListener(this);
        b_stars_none.addActionListener(this);
        s_bright = new JSlider(0, star_count, star_count);
        s_bright.addChangeListener(this);
        l_bright.setText(star_count + " Brightest Stars");

        ButtonGroup bg = new ButtonGroup();
        bg.add(b_sun);
        bg.add(b_opposite);

        JPanel p_bright = new JPanel(new BorderLayout());
        p_bright.add("North", s_bright);
        p_bright.add("South", l_bright);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        JMenu sky = new JMenu("Sky Objects");
        sky.add(cb_grid);
        sky.addSeparator();
        sky.add(b_stars_all);
        sky.add(b_stars_none);

        JMenu bright = new JMenu("Brightest Stars");
        bright.add(p_bright);
        sky.add(bright);
        sky.addSeparator();

        sky.add(select_con);
        sky.addSeparator();
        sky.add(b_ss_all);
        sky.add(b_ss_none);
        b_ss_all.addActionListener(this);
        b_ss_none.addActionListener(this);

        JMenu ss = new JMenu("Select Solar System");
        ss.setLayout(new GridLayout(6, 1));
        ss.add(cb_sun);
        ss.add(cb_mercury);
        ss.add(cb_venus);
        ss.add(cb_mars);
        ss.add(cb_jupiter);
        ss.add(cb_saturn);
        cb_sun.addActionListener(this);
        cb_mercury.addActionListener(this);
        cb_venus.addActionListener(this);
        cb_mars.addActionListener(this);
        cb_jupiter.addActionListener(this);
        cb_saturn.addActionListener(this);
        sky.add(ss);
        menubar.add(sky);

        JMenu v = new JMenu("View");
        v.add(b_center);
        v.add(b_optimal);
        v.addSeparator();
        v.add(b_sun);
        v.add(b_opposite);
        v.addSeparator();
        v.add(b_hemisphere);
        v.add(b_sphere);
        v.addSeparator();

        JMenu fov = new JMenu("Field of View");
        fov.add(s_view);
        v.add(fov);
        menubar.add(v);

        JMenu rotate = new JMenu("Rotation");
        rotate.add(s_rotate);
        menubar.add(rotate);

        controls.add("West", menubar);
        controls.add("East", l_position);
        l_position.setForeground(Color.blue);

        JFrame top_frame = new JFrame();
        top_frame.getContentPane().setLayout(new BorderLayout());
        top_frame.getContentPane().add("Center", canvas3D);

        //top_frame.getContentPane().add("South", controls);
        top_frame.getContentPane().add("North", controls);
        top_frame.pack();
        top_frame.setBounds(0, 0, 800, 800);
        top_frame.setVisible(true);

        Transform3D t = new Transform3D();
        t.rotY(Math.PI / 2);
        camera.setTransform(t);
        moveCamera();

        canvas3D.requestFocus();

        //g = canvas3D.getGraphics2D();
        //labelStars();
        thread = new Thread(this);
        thread.start();
    } //init

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();

        createStars();
        createGrid();
        createConstellations();
        createBodies();
        createTemp();

        //AmbientLight aLgt = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
        //aLgt.setInfluencingBounds(bounds);
        //objRoot.addChild(aLgt);
        t1 = new Transform3D();
        t1.rotX(Math.toRadians(23.45));

        TransformGroup tilt = new TransformGroup(t1);

        bs = new BitSet(6);
        bs.set(0, 6);
        sw = new Switch(Switch.CHILD_MASK);
        sw.setCapability(Switch.ALLOW_SWITCH_WRITE);

        for (int i = 0; i < bodies.length; i++)
            sw.addChild(bodies_transform[i]);

        sw.setChildMask(bs);
        tilt.addChild(sw);

        objRoot.addChild(stars);
        objRoot.addChild(grid);
        objRoot.addChild(constellations);
        objRoot.addChild(tilt);
        objRoot.addChild(temp);
        objRoot.addChild(vp);

        pickCanvas = new PickCanvas(canvas3D, objRoot);
        pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        pickCanvas.setTolerance(4.0f);

        objRoot.compile();

        return objRoot;
    } //createSceneGraph

    /**
     * DOCUMENT ME!
     */
    public void createBodies() {
        bodies = new Shape3D[6];
        bodies_transform = new TransformGroup[bodies.length];

        for (int i = 0; i < bodies.length; i++) {
            /*
            Appearance a = new Appearance();
            Material m = new Material();
            m.setEmissiveColor(body_color[i]);
            a.setMaterial(m);
            //bodies[i] = new Sphere(0.0436f, a);
            if(i==0)
                    bodies[i] = new Sphere(0.5f, a);
            else
            bodies[i] = new Sphere(0.2f, a);
            */
            BufferedImage bi = new BufferedImage(20, 20,
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = bi.getGraphics();
            g.drawImage(symbols[i], 0, 0, this);
            g.setColor(Color.white);
            g.drawRect(0, 0, 19, 19);

            ImageComponent2D ic = new ImageComponent2D(ImageComponent2D.FORMAT_RGBA,
                    bi);
            javax.media.j3d.Raster r = new javax.media.j3d.Raster();
            r.setSize(20, 20);
            r.setImage(ic);
            r.setDstOffset(10, 10);
            bodies[i] = new Shape3D(r);

            Transform3D t = new Transform3D();
            t.setTranslation(new Vector3d(0.0f, 0.0f, size));
            bodies_transform[i] = new TransformGroup(t);
            bodies_transform[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            bodies_transform[i].addChild(bodies[i]);
        } //for
    } //createBodies

    /**
     * DOCUMENT ME!
     */
    private void createConstellations() {
        ca = new LineArray(lines.size() * 2,
                GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        ca.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
        ca.setCapability(GeometryArray.ALLOW_COUNT_READ);
        ca.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        ca.setCapability(GeometryArray.ALLOW_FORMAT_READ);

        Point p;
        Point3f p1 = new Point3f();
        Point3f p2 = new Point3f();

        //Constellation c = null;
        int con_count = 0;
        int line_count = 0;
        Color4f color = null;

        for (int i = 0; i < lines.size(); i++) {
            if ((line_count == i) && (i != (lines.size() - 1))) {
                //c = (Constellation) con_names.elementAt(con_count++);
                //line_count = c.getRange().x + c.getRange().y;
                //color = c.getColor();
            } //if

            p = (Point) lines.elementAt(i);

            for (int j = 0; j < star_count; j++) {
                if (p.x == sao[j]) {
                    pa.getCoordinate(j, p1);
                    j = star_count;
                } //if
            } //for

            for (int j = 0; j < star_count; j++) {
                if (p.y == sao[j]) {
                    pa.getCoordinate(j, p2);
                    j = star_count;
                } //if
            } //for

            con_color[i * 2] = color;
            con_color[(i * 2) + 1] = color;
            ca.setCoordinate(i * 2, p1);
            ca.setCoordinate((i * 2) + 1, p2);
            ca.setColor(i * 2, color);
            ca.setColor((i * 2) + 1, color);
        } //for

        constellations = new Shape3D(ca);
        constellations.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    } //createConstellations

    /**
     * DOCUMENT ME!
     */
    public void createTemp() {
        ta = new LineArray(100,
                GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        ta.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
        ta.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
        ta.setCapability(GeometryArray.ALLOW_COUNT_READ);
        ta.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        ta.setCapability(GeometryArray.ALLOW_FORMAT_READ);

        temp = new Shape3D(ta);

        Appearance a = new Appearance();
        a.setTransparencyAttributes(transp);
        temp.setAppearance(a);
        temp.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    } //createTemp

    /**
     * DOCUMENT ME!
     */
    public void createGrid() {
        int div = 24;
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        Transform3D t = new Transform3D();
        Vector3d v;

        la = new LineArray(2 * div * div,
                GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        la.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
        la.setCapability(GeometryArray.ALLOW_COUNT_READ);
        la.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        la.setCapability(GeometryArray.ALLOW_FORMAT_READ);

        for (int j = 0; j < div; j++) {
            for (int i = 0; i < div; i++) {
                z = 0;
                x = size * Math.sin((2 * Math.PI * i) / div);
                y = size * Math.cos((2 * Math.PI * i) / div);

                p1 = new Point3d(x, y, z);

                v = new Vector3d(0, (2 * Math.PI * j) / div, 0);
                t.setEuler(v);
                t.transform(p1);

                la.setCoordinate((div * j) + i, p1);
                la.setColor((div * j) + i, grid_color);
            } //for
        } //for

        for (int j = 0; j < (div / 2); j++) {
            for (int i = 0; i < div; i++) {
                la.getCoordinate((div * i) + j, p1);

                la.setCoordinate((div * div) + (div * j) + i, p1);
                la.setColor((div * div) + (div * j) + i, grid_color);

                /*
                if(j==1)
                    la.setColor(div*div+div*j+i, new Color4f(0.0f, 0.0f, 1.0f, 1.0f));
                if(j==2)
                    la.setColor(div*div+div*j+i, new Color4f(0.0f, 1.0f, 0.0f, 1.0f));
                if(j==3)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 0.0f, 1.0f));
                if(j==4)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 1.0f, 1.0f));
                if(j==5)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 1.0f, 0.0f, 1.0f));
                */

                //if(j==6)
                //    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 0.0f, 1.0f));
            } //for
        } //for

        for (int j = div / 2; j < div; j++) {
            for (int i = 0; i < div; i++) {
                if (i == (div - 1)) {
                    la.getCoordinate(j, p1);
                } else {
                    la.getCoordinate((div * (i + 1)) + j, p1);
                }

                la.setCoordinate((div * div) + (div * j) + i, p1);
                la.setColor((div * div) + (div * j) + i, grid_color);

                /*
                if(j==23)
                    la.setColor(div*div+div*j+i, new Color4f(0.0f, 0.0f, 1.0f, 1.0f));
                if(j==22)
                    la.setColor(div*div+div*j+i, new Color4f(0.0f, 1.0f, 0.0f, 1.0f));
                if(j==21)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 0.0f, 1.0f));
                if(j==20)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 1.0f, 1.0f));
                if(j==19)
                    la.setColor(div*div+div*j+i, new Color4f(1.0f, 1.0f, 0.0f, 1.0f));
                */

                //if(j==18)
                //    la.setColor(div*div+div*j+i, new Color4f(1.0f, 0.0f, 0.0f, 1.0f));
            } //for
        } //for

        grid = new Shape3D(la);

        Appearance a = new Appearance();
        a.setTransparencyAttributes(transp);
        grid.setAppearance(a);
        grid.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    } //createGrid

    /**
     * DOCUMENT ME!
     */
    public void createStars() {
        pa = new PointArray(star_count,
                GeometryArray.COORDINATES | GeometryArray.COLOR_4);
        pa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
        pa.setCapability(GeometryArray.ALLOW_COUNT_READ);
        pa.setCapability(GeometryArray.ALLOW_COLOR_WRITE);
        pa.setCapability(GeometryArray.ALLOW_FORMAT_READ);

        Vector3d v = new Vector3d();
        Point3d p;
        Transform3D tr = new Transform3D();
        Transform3D tr2 = new Transform3D();
        double c1;
        Color4f c2;

        for (int i = 0; i < star_count; i++) {
            v.set(0, 2 * Math.PI * (ra[i] / 24.0), Math.PI / 2 * (dec[i] / 90.0));
            p = new Point3d(size, 0, 0);
            tr.rotY(v.y);
            tr2.rotZ(v.z);
            tr.mul(tr, tr2);
            tr.transform(p);

            pa.setCoordinate(i, p);

            if (vmag[i] < 3) {
                c2 = new Color4f(1.0f, 1.0f, 1.0f, 1.0f);
            } //if
            else if (vmag[i] < 4) {
                c2 = new Color4f(0.75f, 0.75f, 0.75f, 1.0f);
            } //else
            else if (vmag[i] < 5) {
                c2 = new Color4f(0.5f, 0.5f, 0.5f, 1.0f);
            } //else
            else {
                c2 = new Color4f(0.25f, 0.25f, 0.25f, 1.0f);
            } //else

            //if(sao[i]==308){
            //	c2 = new Color4f(1.0f, 0.0f, 0.0f, 1.0f);
            //}//if
            pa.setColor(i, c2);
            color[i] = c2;

            stars = new Shape3D(pa);

            Appearance a = new Appearance();
            a.setPointAttributes(new PointAttributes(3.0f, true));
            a.setTransparencyAttributes(transp);
            stars.setAppearance(a);
            stars.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        } //for
    } //createStars

    /**
     * DOCUMENT ME!
     */
    public void createRandomStars() {
        int star_count = 6000;

        pa = new PointArray(star_count,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        pa.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
        pa.setCapability(GeometryArray.ALLOW_COUNT_READ);

        float red;
        float green;
        float blue;
        Random r = new Random();

        for (int i = 0; i < star_count; i++) {
            z = (2.0 * r.nextDouble()) - 1.0;
            t = 2.0 * Math.PI * r.nextDouble();
            w = Math.sqrt(1.0 - (z * z));
            x = w * Math.cos(t);
            y = w * Math.sin(t);
            x *= size;
            y *= size;
            z *= size;

            pa.setCoordinate(i, new Point3d(x, y, z));

            red = r.nextFloat();
            green = red;
            blue = red;

            d = r.nextInt(3);

            if (d == 0) {
                red = red * 2.0f;

                if (red > Byte.MAX_VALUE) {
                    red = Byte.MAX_VALUE;
                }
            } //if
            else if (d == 1) {
                green = red * 2.0f;
                red = red * 2.0f;

                if (green > Byte.MAX_VALUE) {
                    green = Byte.MAX_VALUE;
                }
            } //else
            else if (d == 2) {
                blue = red * 2.0f;

                if (blue > Byte.MAX_VALUE) {
                    blue = Byte.MAX_VALUE;
                }
            } //else

            pa.setColor(i, new Color3f(red, green, blue));
        } //for

        stars = new Shape3D(pa);

        Appearance a = new Appearance();
        a.setPointAttributes(new PointAttributes(3.0f, true));
        stars.setAppearance(a);
    } //createRandomStars

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double round(double d) {
        d *= 1000;
        d = Math.rint(d);
        d /= 1000;

        return d;
    } //round

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent e) {
        if (e.getModifiers() == InputEvent.BUTTON1_MASK) {
            rx -= (drx * (startx - e.getX()));
            ry -= (dry * (starty - e.getY()));

            if (ry > (Math.PI / 2)) {
                ry = Math.PI / 2;
            } else if (ry < (-Math.PI / 2)) {
                ry = -Math.PI / 2;
            }

            markPosition();
        } //if
        else {
            distance += ((starty - e.getY()) / 4.0);

            if (distance < min_distance) {
                distance = min_distance;
            }
        } //else

        startx = e.getX();
        starty = e.getY();

        moveCamera();
    } //mouseDragged

    /**
     * DOCUMENT ME!
     */
    public void markPosition() {
        l_dec = Math.toDegrees(ry);
        l_ra = (24.0 * (rx + Math.PI + camera_offset)) / (Math.PI * 2);

        while (l_ra > 24)
            l_ra -= 24;

        while (l_ra < 0)
            l_ra += 24;

        l_position.setText(round(l_ra) + "   " + round(l_dec));
    } //markPosition

    /**
     * DOCUMENT ME!
     */
    public void moveCamera() {
        //if(rx>2*Math.PI)
        //    rx -= 2*Math.PI;
        v.set(ry, rx + (Math.PI / 2) + camera_offset, 0);
        t1.setEuler(v);

        v.set(0, 0, distance);
        t1.transform(v);
        t1.setTranslation(v);

        camera.setTransform(t1);

        /*
        v.set(Math.toRadians(23.45)*Math.cos(rx-Math.PI/2), rx-Math.PI/2, 0);
        t1.setEuler(v);
        v.set(0, 0, size);
        t1.transform(v);
        t1.setTranslation(v);
        */

        //mercury 0.3871
        //venus 0.7233
        //mars 1.5237
        //jupiter 5.2028
        //saturn 9.5388
        Vector3d sun = new Vector3d();
        sun.x = size * Math.sin(rx - (Math.PI / 2));
        sun.z = size * Math.cos(rx - (Math.PI / 2));

        t1.set(1.0f, sun);
        bodies_transform[0].setTransform(t1);

        for (int i = 0; i < au.length; i++) {
            v = new Vector3d();
            v.x = (size * au[i] * Math.sin(((rx * 365.26) / rev[i]) -
                    (Math.PI / 2))) + sun.x;
            v.z = (size * au[i] * Math.cos(((rx * 365.26) / rev[i]) -
                    (Math.PI / 2))) + sun.z;
            t1.rotX(Math.toRadians(inc[i]));
            t1.transform(v);
            v.scale(size / Math.sqrt((v.x * v.x) + (v.z * v.z)));
            t1.set(1.0f, v);
            bodies_transform[i + 1].setTransform(t1);
        } //for

        //sun.scale(size/Math.sqrt(sun.x*sun.x+sun.z*sun.z));
    } //moveCamera

    /**
     * DOCUMENT ME!
     *
     * @param hemi DOCUMENT ME!
     */
    public void setHemisphere(boolean hemi) {
        double h = Math.sqrt((distance * distance) + (size * size)) - 0.01;

        for (int i = 0; i < la.getVertexCount(); i++) {
            la.getCoordinate(i, p2);
            d = p1.distance(p2);

            if (!hemi || (d >= h)) {
                la.setColor(i, grid_color);
            } else {
                la.setColor(i, clear);
            }
        } //for

        for (int i = 0; i < pa.getVertexCount(); i++) {
            pa.getCoordinate(i, p2);
            d = p1.distance(p2);

            if (!hemi || (d >= h)) {
                pa.setColor(i, color[i]);
            } else {
                pa.setColor(i, clear);
            }
        } //for

        for (int i = 0; i < ca.getVertexCount(); i++) {
            ca.getCoordinate(i, p2);
            d = p1.distance(p2);

            if (!hemi || (d >= h)) {
                ca.setColor(i, con_color[i]);
            } else {
                ca.setColor(i, clear);
            }
        } //for
    } //setHemisphere

    /**
     * DOCUMENT ME!
     */
    public void displayBrightest() {
        for (int i = 0; i < pa.getVertexCount(); i++) {
            pa.getCoordinate(i, p2);

            if (i < brightest) {
                pa.setColor(i, color[i]);
            } //if
            else {
                pa.setColor(i, clear);
            } //else
        } //for
    } //displayBrightest

    /**
     * DOCUMENT ME!
     */
    public void labelStars() {
        canvas3D.getVworldToImagePlate(t1);

        Point3d p = new Point3d();
        Point2d p2 = new Point2d();
        Vector v = new Vector();

        for (int i = 0; i < pa.getVertexCount(); i++) {
            if (i < brightest) {
                pa.getCoordinate(i, p);
                t1.transform(p);

                //to keep out goofy label locations
                if (p.z < 0.1) {
                    p2 = new Point2d();
                    canvas3D.getPixelLocationFromImagePlate(p, p2);
                    v.addElement(name[i]);
                    v.addElement(p2);
                } //if
            } //if
        } //for

        canvas3D.setStars(v);

        camera.getTransform(t1);
        camera.setTransform(t1);
    } //labelStars

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        startx = e.getX();
        starty = e.getY();

        canvas3D.setStars(null);
    } //mousePressed

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        //necessary?
        camera.getTransform(t1);
        camera.setTransform(t1);

        canvas3D.setStars(null);
    } //mouseReleased

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        /*
        if(e.getModifiers()!=InputEvent.BUTTON1_MASK && temp_count!=0){
            list.removeElementAt(list.size()-1);
            temp_count -= 2;
            ta.setColor(temp_count, clear);
            ta.setColor(temp_count+1, clear);
            index0 = -1;
            index1 = -1;
            index2 = -1;
            System.out.println("Deleted");
            return;
        }//if
        
        pickCanvas.setShapeLocation(e);
        PickResult[] results = pickCanvas.pickAll();
        Point3d p1;
        Point3d p2 = new Point3d();
        
        if(results==null)
            return;
        
        p1 = results[0].getIntersection(0).getClosestVertexCoordinates();
        for(int i=0; i<pa.getVertexCount(); i++){
            pa.getCoordinate(i, p2);
            if(p1.equals(p2)){
                System.out.println(sao[i]);
                index0 = i;
                break;
            }//if
        }//for
        
        if(index1==-1){
            index1 = index0;
        }//if
        else if(index0!=index1){
            index2 = index0;
            pa.getCoordinate(index1, p1);
            ta.setCoordinate(temp_count, p1);
            ta.setColor(temp_count, white);
            pa.getCoordinate(index2, p1);
            ta.setCoordinate(temp_count+1, p1);
            ta.setColor(temp_count+1, white);
            temp_count += 2;
        
            System.out.println("Added ["+sao[index1]+", "+sao[index2]+"]"+'\n');
            list.add(new Point(sao[index1], sao[index2]));
            Point p;
            for(int i=0; i<list.size(); i++){
                p = (Point)list.elementAt(i);
                System.out.println(p.x+" "+p.y);
            }//for
        
            index1 = -1;
            index2 = -1;
        }//else
        */
    } //mouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            labelStars();
        } //if
    } //keyPressed

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyReleased(KeyEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        pause = true;

        if (e.getSource() == b_hemisphere) {
            //cb_rotate.setSelected(false);
            v.set(ry, rx + (Math.PI / 2), 0);
            t1.setEuler(v);
            p1.set(0, 0, distance);
            t1.transform(p1);

            hemisphere = true;

            //cb_rotate.setSelected(false);
            //cb_rotate.setEnabled(false);
            setHemisphere(hemisphere);
        } //if
        else if (e.getSource() == b_sphere) {
            hemisphere = false;

            cb_rotate.setEnabled(true);

            setHemisphere(hemisphere);
        } //else
        else if (e.getSource() == b_brightest) {
            try {
                brightest = Integer.parseInt(tf_brightest.getText());
                displayBrightest();
            } //try
            catch (Exception f) {
                tf_brightest.setText("");
            } //catch
        } //else
        else if (e.getSource() == cb_grid) {
            boolean draw = cb_grid.isSelected();

            for (int i = 0; i < la.getVertexCount(); i++) {
                la.getCoordinate(i, p2);

                if (draw) {
                    la.setColor(i, grid_color);
                } //if
                else {
                    la.setColor(i, clear);
                } //else
            } //for
        } //else
        else if (e.getSource() == b_stars_all) {
            for (int i = 0; i < pa.getVertexCount(); i++) {
                pa.getCoordinate(i, p2);
                pa.setColor(i, color[i]);
            } //for
        } //else
        else if (e.getSource() == b_stars_none) {
            for (int i = 0; i < pa.getVertexCount(); i++) {
                pa.getCoordinate(i, p2);
                pa.setColor(i, clear);
            } //for
        } //else
        else if (e.getSource() == b_center) {
            distance = min_distance;
            s_view.setValue(3);
            moveCamera();
        } //else
        else if (e.getSource() == b_optimal) {
            distance = 10;
            s_view.setValue(6);
            moveCamera();
        } //else
        else if (e.getSource() == b_sun) {
            camera_offset = 0;
            moveCamera();
            markPosition();
        } //else
        else if (e.getSource() == b_opposite) {
            camera_offset = Math.PI;
            moveCamera();
            markPosition();
        } //else
        else if (e.getSource() == cb_sun) {
            bs.set(0, cb_sun.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == cb_mercury) {
            bs.set(1, cb_mercury.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == cb_venus) {
            bs.set(2, cb_venus.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == cb_mars) {
            bs.set(3, cb_mars.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == cb_jupiter) {
            bs.set(4, cb_jupiter.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == cb_saturn) {
            bs.set(5, cb_saturn.isSelected());
            sw.setChildMask(bs);
        } //else
        else if (e.getSource() == b_ss_all) {
            bs.set(0, 6);
            sw.setChildMask(bs);
            cb_sun.setSelected(true);
            cb_mercury.setSelected(true);
            cb_venus.setSelected(true);
            cb_mars.setSelected(true);
            cb_jupiter.setSelected(true);
            cb_saturn.setSelected(true);
        } //else
        else if (e.getSource() == b_ss_none) {
            bs.clear();
            sw.setChildMask(bs);
            cb_sun.setSelected(false);
            cb_mercury.setSelected(false);
            cb_venus.setSelected(false);
            cb_mars.setSelected(false);
            cb_jupiter.setSelected(false);
            cb_saturn.setSelected(false);
        } //else

        pause = false;
    } //actionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == s_view) {
            view.setFieldOfView((Math.PI * s_view.getValue()) / 12.0);
        } //if

        if (e.getSource() == s_bright) {
            brightest = s_bright.getValue();
            l_bright.setText(brightest + " Brightest Stars");

            if (!s_bright.getValueIsAdjusting()) {
                displayBrightest();
            }
        } //if
    } //stateChanged

    /**
     * DOCUMENT ME!
     */
    public void run() {
        Transform3D r = new Transform3D();
        int rotation_speed;

        markPosition();

        while (true) {
            rotation_speed = s_rotate.getValue();

            if (!pause && (rotation_speed > 0)) {
                rx -= (((2 * Math.PI) / 365.0 / 10.0 * rotation_speed) / 5.0);

                //if(rx>2*Math.PI)
                //    rx -= 2*Math.PI;
                /*
                v.set(ry, rx+Math.PI/2, 0);
                t1.setEuler(v);
                
                v.set(0, 0, distance);
                t1.transform(v);
                t1.setTranslation(v);
                
                camera.setTransform(t1);
                
                v.set(Math.toRadians(23.45)*Math.cos(rx-Math.PI/2), rx-Math.PI/2, 0);
                t1.setEuler(v);
                v.set(0, 0, size);
                t1.transform(v);
                t1.setTranslation(v);
                
                bodies_transform[0].setTransform(t1);
                */
                moveCamera();
                markPosition();
            } //if

            try {
                thread.sleep(1);
            } //try
            catch (Exception e) {
            }
        } //while
    } //run
}
