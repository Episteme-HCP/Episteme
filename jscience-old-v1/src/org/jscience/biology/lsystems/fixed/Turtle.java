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

/* 
---------------------------------------------------------------------
VIRTUAL PLANTS
==============

University of applied sciences Biel

hosted by Claude Schwab

programmed by Rene Gressly

March - July 1999

---------------------------------------------------------------------
*/

package org.jscience.biology.lsystems.fixed;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.universe.SimpleUniverse;
import org.jscience.biology.lsystems.common.TruncatedCone;

import javax.media.j3d.*;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.applet.Applet;
import java.awt.*;
import java.util.Stack;

/**
 * This class is responsible for all the drawing of the LSystem. It has all capabilities to
 * draw and show a 3 dimensional model of a system of the class LSystem also defined in this
 * package. This class extends an applet and so it can be viewed in a frame or on a web page.
 * <br>
 * This class uses the Java3D 1.1.1 package to build the scene.
 * <p/>
 * The method of drawing the lsystem is the one of the turtle interpretation described in
 * the book "The algorithmic beauty of plants" written by Przemyslaw Prusinkiewicz and
 * Aristid Lindenmayer. See chapter 1 "Graphical modeling using L-Systems" for more details.
 * <br>
 * The coordinate system used here has been slightly changed from the one used in the book.
 * It now matches a normal coordinate system as we know it. See analysis of this project for
 * more information.
 * <p/>
 * When parsing the lsystem all signs make the turtle change its orientation and every
 * occurence of a capital letter 'F' draws a truncated cone from the position of the turtle
 * in it's actual direction. The truncated cone has the length, specified in the lsystem.
 * Depending on how many times the sign '!' has already occurred the diameters of the
 * truncated cone vary. It is the users responsibility to choose the right initial diameter
 * and decrement so that the cones never have negative values.
 * <br>
 * Leafes, indicated between signs '{' and '}', are drawn as a set of triangles. It is also
 * the users job to make the turtle begin and end a leaf at the same position. That means
 * that all signs between starting leaf and ending leaf must be chosen the way to make the
 * turtle 'return' to it's starting point, else the leaf won't be displayed correctely mabe
 * not at all.
 */
public class Turtle extends Applet {

    /**
     * The actual length of the branch.
     *
     * @serial
     */
    private float m_fLength;

    /**
     * The actual thickness of the branch.
     *
     * @serial
     */
    private float m_fThickness;

    /**
     * The actual position of the turtle
     *
     * @serial
     */
    private Vector3f m_v3fPosition = new Vector3f(0f, 0f, 0f);

    /**
     * Direction in wich the turtle is heading
     *
     * @serial
     */
    private Vector3f m_v3fHeading = new Vector3f(0f, 1f, 0f);

    /**
     * Directoin to the left of the turtle
     *
     * @serial
     */
    private Vector3f m_v3fRight = new Vector3f(1f, 0f, 0f);

    /**
     * Direction up from the turtle
     *
     * @serial
     */
    private Vector3f m_v3fUp = new Vector3f(0f, 0f, 1f);

    /**
     * Indicates if a leaf is beeing built
     *
     * @serial
     */
    private boolean m_bLeaf = true;

    /**
     * A stack for all the points representing the edges of a leaf
     *
     * @serial
     */
    private static Stack m_stackLeaf = new Stack();

    /**
     * A stack to store the actual position and values of the turtle
     *
     * @serial
     */
    private static Stack m_stackTurtle = new Stack();

    /**
     * Constructs the Lindenmayer System in form of a 3 dimensional structure respecting the rules and values given
     * in the passed LSystem. By default the branches are drawn in brown and the leafes in green color. Directional
     * light and ambient light are set. The user may turn the scene using the mouse.
     *
     * @param lsSystem The LSystem to use. Note that the LSystem has to be built before it can be used.
     */
    public Turtle(LSystem lsSystem) {
        TransformGroup tgScene;
        Transform3D t3dScene;
        TruncatedCone truncCone;
        Matrix3f m3fRot;
        Matrix3f m3fRotation;
        Vector3f v3fTemp2;

        m_fLength = lsSystem.getLength();
        m_fThickness = lsSystem.getThickness();

        //
        //Create view branch
        //

        //create canvas
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        //set layout of applet and add canvas
        setLayout(new BorderLayout());
        add("Center", canvas);

        //create view platform
        ViewPlatform vp = new ViewPlatform();
        vp.setActivationRadius(50.0f);

        PhysicalBody body = new PhysicalBody();
        PhysicalEnvironment environment = new PhysicalEnvironment();

        //create new view object and make connections to canvas, view platform, physical body and physical environment
        View view = new View();
        view.addCanvas3D(canvas);
        view.attachViewPlatform(vp);
        view.setPhysicalBody(body);
        view.setPhysicalEnvironment(environment);

        //set initial position to a new transform group and add the view platform
        Transform3D t3DView = new Transform3D();
        t3DView.set(new Vector3f(0.0f, 5.0f, 20.0f));
        TransformGroup tgView = new TransformGroup(t3DView);
        tgView.addChild(vp);

        BoundingSphere bsRange = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 50.0);

        //create branch group for the view and add the transform group
        BranchGroup bgView = new BranchGroup();
        bgView.addChild(tgView);

        //
        // create the scene branch
        //

        //create transform group for the whole scene
        TransformGroup tgMouse = new TransformGroup();
        tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //add mouse rotation capability for the scene
        MouseRotate mr = new MouseRotate(tgMouse);
        tgMouse.addChild(mr);
        mr.setSchedulingBounds(bsRange);

        //add mouse zoom capability for the scene
        MouseZoom mz = new MouseZoom(tgMouse);
        tgMouse.addChild(mz);
        mz.setSchedulingBounds(bsRange);

        //add mouse translation capability for the scene
        MouseTranslate mt = new MouseTranslate(tgMouse);
        tgMouse.addChild(mt);
        mt.setSchedulingBounds(bsRange);

        //create appearance for branches
        Appearance appCylinder = new Appearance();
        Material mBranch = new Material(FixedPlantsDefinitions.COLOR_DARK_BROWN, FixedPlantsDefinitions.COLOR_BLACK, FixedPlantsDefinitions.COLOR_BROWN, FixedPlantsDefinitions.COLOR_BROWN, 5f);
        appCylinder.setMaterial(mBranch);

        //create appearance for leafes
        Appearance appLeaf = new Appearance();
        Material mLeaf = new Material(FixedPlantsDefinitions.COLOR_DARK_GREEN, FixedPlantsDefinitions.COLOR_BLACK, FixedPlantsDefinitions.COLOR_BLACK, FixedPlantsDefinitions.COLOR_DARK_GREEN, 5f);
        mLeaf.setLightingEnable(true);
        appLeaf.setMaterial(mLeaf);

        //modify appearance of leafes to make them visible from both sides (else they are only visible from top)
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        pa.setBackFaceNormalFlip(true);
        appLeaf.setPolygonAttributes(pa);

        //
        // Parse the Lindenmayer System
        //

        for (int i = 0; i < lsSystem.getSystem().length(); i++) {
            //Log.debug("Run number:" + i + "Char: " + lsSystem.getSystem().charAt(i) );

            switch (lsSystem.getSystem().charAt(i)) {
                case FixedPlantsDefinitions.C_TURN_LEFT: //turn turtle left around y axis of turtle's coordinate system (up) by the angle

                    //make rotation matrix
                    m3fRot = rotationMatrix(m_v3fUp, lsSystem.getAngle());
                    //calculate the new directions of the turtles coordinate system related to the scene's coordinate system
                    m3fRot.transform(m_v3fRight);
                    m3fRot.transform(m_v3fHeading);
                    //the direction of m_v3fUp remains the same since we turn around it
                    break;

                case FixedPlantsDefinitions.C_TURN_RIGHT: //turn turtle right around y axis of turtle's coordinate system (up) by the angle

                    m3fRot = rotationMatrix(m_v3fUp, -lsSystem.getAngle());
                    m3fRot.transform(m_v3fRight);
                    m3fRot.transform(m_v3fHeading);
                    break;

                case FixedPlantsDefinitions.C_PITCH_DOWN: //pitch turtle down around x axis of turtle's coordinate system (right) by the angle

                    m3fRot = rotationMatrix(m_v3fRight, -lsSystem.getAngle());
                    m3fRot.transform(m_v3fHeading);
                    m3fRot.transform(m_v3fUp);
                    break;

                case FixedPlantsDefinitions.C_PITCH_UP: //pitch turtle up around x axis of turtle's coordinate system (right) by the angle

                    m3fRot = rotationMatrix(m_v3fRight, lsSystem.getAngle());
                    m3fRot.transform(m_v3fHeading);
                    m3fRot.transform(m_v3fUp);
                    break;

                case FixedPlantsDefinitions.C_ROLL_LEFT: //roll turtle left around z axis of turtle's coordinate system (heading) by the angle

                    m3fRot = rotationMatrix(m_v3fHeading, -lsSystem.getAngle());
                    m3fRot.transform(m_v3fRight);
                    m3fRot.transform(m_v3fUp);
                    break;

                case FixedPlantsDefinitions.C_ROLL_RIGHT: //roll turtle right around z axis of turtle's coordinate system (heading) by the angle

                    m3fRot = rotationMatrix(m_v3fHeading, lsSystem.getAngle());
                    m3fRot.transform(m_v3fRight);
                    m3fRot.transform(m_v3fUp);
                    break;

                case FixedPlantsDefinitions.C_TURN_AROUND: //turn turtle around by 180� around y axis of turtle's coordinate system (up)

                    m3fRot = rotationMatrix(m_v3fUp, (float) Math.PI);
                    m3fRot.transform(m_v3fRight);
                    m3fRot.transform(m_v3fHeading);
                    break;

                case FixedPlantsDefinitions.C_BRANCH: //the turtle has to step forward and bild a cone

                    //make rotation matrix with the turtle's values
                    m3fRotation = buildMatrix(m_v3fRight, m_v3fHeading, m_v3fUp);

                    //correction of the position of the cylinder (default start is in the center)
                    Vector3f v3fTemp = new Vector3f();
                    m3fRotation.transform(new Vector3f(0, m_fLength / 2, 0), v3fTemp);
                    v3fTemp.add(m_v3fPosition);

                    //create transform3d, transformgroup for cylinder
                    t3dScene = new Transform3D(m3fRotation, v3fTemp, 1);
                    tgScene = new TransformGroup(t3dScene);

                    //create truncated cylinder
                    truncCone = new TruncatedCone(m_fThickness, m_fThickness - lsSystem.getDecrement(), m_fLength,
                            TruncatedCone.GENERATE_NORMALS, 10, 1, appCylinder);

                    //calculate new position vector
                    v3fTemp2 = new Vector3f();
                    m3fRotation.transform(new Vector3f(0, m_fLength, 0), v3fTemp2);
                    m_v3fPosition.add(v3fTemp2);

                    //add object to scene graph
                    tgScene.addChild(truncCone);
                    tgMouse.addChild(tgScene);

                    break;

                case FixedPlantsDefinitions.C_STEP: //the turtle has to move forward without drawing

                    //make rotation matrix with the turtle's values
                    m3fRotation = buildMatrix(m_v3fRight, m_v3fHeading, m_v3fUp);

                    //calculate new position vector for the point after the step
                    v3fTemp2 = new Vector3f();
                    m3fRotation.transform(new Vector3f(0, m_fLength, 0), v3fTemp2);
                    m_v3fPosition.add(v3fTemp2);

                    //add the new point to the stack of leaf  points
                    if (m_bLeaf) m_stackLeaf.push(new Point3f(m_v3fPosition));

                    break;

                case FixedPlantsDefinitions.C_BEGIN_LEAF:

                    //indicate that a leaf has to be built
                    m_bLeaf = true;

                    break;

                case FixedPlantsDefinitions.C_END_LEAF: //sign that leaf is finished

                    int iPoints = m_stackLeaf.size();

                    // make array of points out of stack
                    Point3f[] p3fPoints = new Point3f[iPoints];
                    m_stackLeaf.toArray(p3fPoints);

                    //create geometryinfo class of type triangle fan array and add point array
                    GeometryInfo giLeaf = new GeometryInfo(GeometryInfo.TRIANGLE_FAN_ARRAY);
                    giLeaf.setCoordinates(p3fPoints);

                    //make new integer array with one element containing the number of points
                    int[] iArr = {iPoints};
                    giLeaf.setStripCounts(iArr);

                    //create new NormalGenerator to calculate normals
                    NormalGenerator ng = new NormalGenerator();
                    ng.generateNormals(giLeaf);

                    //make triangle strips for faster rendering
                    Stripifier st = new Stripifier();
                    st.stripify(giLeaf);

                    //empty the stack
                    m_stackLeaf.clear();

                    //create new Shape3D object and add the fan array and the leaf appearance
                    Shape3D s3dLeaf = new Shape3D(giLeaf.getGeometryArray(), appLeaf);

                    //add shape to transform group
                    tgMouse.addChild(s3dLeaf);

                    break;

                case FixedPlantsDefinitions.C_DECREMENT_DIAMETER:
                    m_fThickness -= lsSystem.getDecrement(); //decrement diameter of truncated cone
                    break;

                case FixedPlantsDefinitions.C_PUSH_TURTLE:
                    push(); //push all turtle data
                    break;

                case FixedPlantsDefinitions.C_POP_TURTLE:
                    pop(); //pop last pushed turtle data
                    break;

                default:
            }
        }

        //create branch group for the scene
        BranchGroup bgScene = new BranchGroup();

        // its possible to set the background for the scene
        /*Background back = new Background( Def.COLOR_WHITE );
        back.setApplicationBounds(bsRange);
        bgScene.addChild(back);*/

        //create light for scene an add to view so the light comes always from the same direction
        //from the viewers point of view
        DirectionalLight dl = new DirectionalLight(FixedPlantsDefinitions.COLOR_WHITE, new Vector3f(-1.0f, -1.0f, -1.0f));
        dl.setInfluencingBounds(bsRange);
        bgScene.addChild(dl);

        //create an ambient light
        AmbientLight al = new AmbientLight(FixedPlantsDefinitions.COLOR_WHITE);
        al.setInfluencingBounds(bsRange);
        bgScene.addChild(al);

        //add mouse group
        bgScene.addChild(tgMouse);

        //compile scene
        bgScene.compile();

        //create virtual universe
        VirtualUniverse vu = new VirtualUniverse();

        //create locale and add both, view and scene branches
        Locale locale = new Locale(vu);
        locale.addBranchGraph(bgScene);
        locale.addBranchGraph(bgView);
    }

    /**
     * Calculates the rotation matrix for a 3D rotation around the axe going through the point 0,0,0 with the
     * direction described by the vector of the given angle.
     *
     * @param v3fAxe The vector describing the axis going through the origin (0,0,0).
     * @param fAngle The angle describing the rotation value.
     * @return The 3x3 float matrix which is the rotation matrix.
     */
    public Matrix3f rotationMatrix(Vector3f v3fAxe, float fAngle) {
        //store sinus and cosinus of the angle since it it used very often
        float fSinA = (float) Math.sin(fAngle);
        float fCosA = (float) Math.cos(fAngle);

        float fX = v3fAxe.x;
        float fY = v3fAxe.y;
        float fZ = v3fAxe.z;

        return new Matrix3f(fX * fX + (1f - fX * fX) * fCosA, fX * fY * (1f - fCosA) - fZ * fSinA, fX * fZ * (1f - fCosA) + fY * fSinA,
                fX * fY * (1f - fCosA) + fZ * fSinA, fY * fY + (1f - fY * fY) * fCosA, fY * fZ * (1f - fCosA) - fX * fSinA,
                fX * fZ * (1f - fCosA) - fY * fSinA, fY * fZ * (1f - fCosA) + fX * fSinA, fZ * fZ + (1f - fZ * fZ) * fCosA);
    }

    /**
     * Builds a new 3x3 matrix with the given three vectors by inserting the vector values in the matrix columns.
     *
     * @param v3fCol0 The vector to be inserted in the first column.
     * @param v3fCol1 The vector to be inserted in the second column.
     * @param v3fCol2 The vector to be inserted in the third column.
     * @return A newly created 3x3 matrix.
     */
    public Matrix3f buildMatrix(Vector3f v3fCol0, Vector3f v3fCol1, Vector3f v3fCol2) {
        Matrix3f m3fMatrix = new Matrix3f();
        m3fMatrix.setColumn(0, v3fCol0);
        m3fMatrix.setColumn(1, v3fCol1);
        m3fMatrix.setColumn(2, v3fCol2);
        return m3fMatrix;
    }

    /**
     * Pushes all member values of the turtle to the stack for later use.
     */
    private void push() {
        m_stackTurtle.push(new Vector3f(m_v3fHeading));
        m_stackTurtle.push(new Vector3f(m_v3fRight));
        m_stackTurtle.push(new Vector3f(m_v3fUp));
        m_stackTurtle.push(new Vector3f(m_v3fPosition));
        m_stackTurtle.push(new Float(m_fLength));
        m_stackTurtle.push(new Float(m_fThickness));
    }

    /**
     * Pops back the last pushed member variables and writes them to the actual object.
     */
    private void pop() {
        this.m_fThickness = ((Float) m_stackTurtle.pop()).floatValue();
        this.m_fLength = ((Float) m_stackTurtle.pop()).floatValue();
        this.m_v3fPosition = (Vector3f) m_stackTurtle.pop();
        this.m_v3fUp = (Vector3f) m_stackTurtle.pop();
        this.m_v3fRight = (Vector3f) m_stackTurtle.pop();
        this.m_v3fHeading = (Vector3f) m_stackTurtle.pop();
    }

    /**
     * Set a new position for the turtle.
     *
     * @param v3fPosition The new position
     */
    public void setPosition(Vector3f v3fPosition) {
        m_v3fPosition = v3fPosition;
    }

    /**
     * Gets the actual position of the turtle.
     *
     * @return The actual position.
     */
    public Vector3f getPosition() {
        return m_v3fPosition;
    }

}//class Turtle
