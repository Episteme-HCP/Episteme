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

package org.jscience.biology.lsystems.growing;

import org.jscience.biology.lsystems.growing.shape.Branch;
import org.jscience.biology.lsystems.growing.shape.LeafShape;

import java.io.File;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/**
 * This class represents a plant which can be built with a lindenmayer
 * system.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Plant implements Comparable {
    /** The reference of the actually selected plant in the list of the gui */
    private static Plant m_selected;

    /**
     * A stack to store the actual position and values of the rotation
     *
     * @serial
     */
    private static Stack m_stackRot = new Stack();

    /** The name of the plant */
    private String m_strName;

    /** The X and Y position of the plant in the scene. */
    private int m_iX;

    /** The X and Y position of the plant in the scene. */
    private int m_iY;

    /** The lsystem for the plant */
    private LSystem m_lsystem;

    /** The list of objest which are used for the animation */
    private Vector m_vAnimation;

/**
     * Constructor. Makes ney LSystem for his plant and stores placement of
     * plant in the scene.
     *
     * @param fileLsy A file descriptor to the .lsy file for this LSystem.
     * @param iX      The X position in the scene.
     * @param iY      The Y position in the scene.
     * @throws Exception an exception if the file could not be opened.
     */
    public Plant(File fileLsy, int iX, int iY) throws Exception {
        m_iX = iX;
        m_iY = iY;

        m_lsystem = new LSystem(fileLsy);

        m_strName = m_lsystem.getName();

        //Log.debug("Plant created:" + m_strName);
    }

    /**
     * Builds the scene graph from the input file.
     *
     * @return The top transformgroup node of the plant.
     */
    public TransformGroup build() //Appearance appBranch, Appearance appLeaf)
     {
        //Log.log("Building: " + m_strName);
        //m_appBranch = appBranch;
        //m_appLeaf = appLeaf;
        TransformGroup tgPlant = new TransformGroup();

        char chAxiomID = Rule.getElementID(m_lsystem.getAxiom());
        int iAxiomAge = Rule.getElementAge(m_lsystem.getAxiom());

        m_vAnimation = new Vector();

        //call parserule with the axiom to start
        parseRule(tgPlant, chAxiomID, iAxiomAge, 0, m_lsystem.getThickness());

        return tgPlant;
    }

    /**
     * Searches the rule for the specified id and adds the scenegraph
     * subtree.
     *
     * @param tgPrevious The transformgroup where to add the subtree.
     * @param cID The char of the rule which has to be searched.
     * @param fStartAge The start age of the next elements.
     * @param iDepth The recursion depth at this time. Apply rules until iDepth
     *        is equal to the depth of the LSystem.
     * @param fThickness The bottom thicknes of a branch. Top is fThickness
     *        minus the decrement stored in the LSystem.
     */
    public void parseRule(TransformGroup tgPrevious, char cID, float fStartAge,
        int iDepth, float fThickness) {
        //increment depth. look for rules only if idepth is smaller that the lsystems depth
        iDepth++;

        if (m_lsystem.getDepth() < iDepth) {
            return;
        }

        //find all rules with the predecessor ID equal to the char ID. Then add them to the tgPrevious Node
        for (Enumeration enumeration = m_lsystem.getRules().elements();
                enumeration.hasMoreElements();) {
            Rule rule = (Rule) enumeration.nextElement();

            if (rule.getPredecessorID() == cID) {
                // this rule matches and has to be used
                //Log.debug("For char " + cID + " this rule was found: " + rule.toString());
                //new vectors for the rotation
                Vector3f v3fHeading = new Vector3f(0f, 1f, 0f);
                Vector3f v3fRight = new Vector3f(1f, 0f, 0f);
                Vector3f v3fUp = new Vector3f(0f, 0f, 1f);

                Matrix3f m3fRot = null;
                TransformGroup tgLeafRot = null;
                Matrix3f m3fLeafRot = null;
                Point3f p3fLeafPos = null;
                Stack stackLeaf = null;
                boolean bIsFlower = false;

                for (int i = 0; i < rule.getSuccessor().length(); i++) {
                    //Log.debug("Run: " + i + " Char: " + rule.getSuccessor().charAt(i));
                    switch (rule.getSuccessor().charAt(i)) {
                    case GrowingPlantsDefinitions.C_TURN_LEFT: //turn turtle left around y axis of turtle's coordinate system (up) by the angle
                        //make rotation matrix
                        m3fRot = rotationMatrix(v3fUp, m_lsystem.getAngle());
                        //calculate the new directions of the turtles coordinate system related to the scene's coordinate system
                        m3fRot.transform(v3fRight);
                        m3fRot.transform(v3fHeading);

                        //the direction of v3fUp remains the same since we turn around it
                        break;

                    case GrowingPlantsDefinitions.C_TURN_RIGHT: //turn turtle right around y axis of turtle's coordinate system (up) by the angle
                        m3fRot = rotationMatrix(v3fUp, -m_lsystem.getAngle());
                        m3fRot.transform(v3fRight);
                        m3fRot.transform(v3fHeading);

                        break;

                    case GrowingPlantsDefinitions.C_PITCH_DOWN: //pitch turtle down around x axis of turtle's coordinate system (right) by the angle
                        m3fRot = rotationMatrix(v3fRight, -m_lsystem.getAngle());
                        m3fRot.transform(v3fHeading);
                        m3fRot.transform(v3fUp);

                        break;

                    case GrowingPlantsDefinitions.C_PITCH_UP: //pitch turtle up around x axis of turtle's coordinate system (right) by the angle
                        m3fRot = rotationMatrix(v3fRight, m_lsystem.getAngle());
                        m3fRot.transform(v3fHeading);
                        m3fRot.transform(v3fUp);

                        break;

                    case GrowingPlantsDefinitions.C_ROLL_LEFT: //roll turtle left around z axis of turtle's coordinate system (heading) by the angle
                        m3fRot = rotationMatrix(v3fHeading,
                                -m_lsystem.getAngle());
                        m3fRot.transform(v3fRight);
                        m3fRot.transform(v3fUp);

                        break;

                    case GrowingPlantsDefinitions.C_ROLL_RIGHT: //roll turtle right around z axis of turtle's coordinate system (heading) by the angle
                        m3fRot = rotationMatrix(v3fHeading, m_lsystem.getAngle());
                        m3fRot.transform(v3fRight);
                        m3fRot.transform(v3fUp);

                        break;

                    case GrowingPlantsDefinitions.C_TURN_AROUND: //turn turtle around by 180� around y axis of turtle's coordinate system (up)
                        m3fRot = rotationMatrix(v3fUp, (float) Math.PI);
                        m3fRot.transform(v3fRight);
                        m3fRot.transform(v3fHeading);

                        break;

                    case GrowingPlantsDefinitions.C_BEGIN_ELEMENT: //new subtree begins

                        //do not draw branch if its at the end of the recursion (does not look natural)
                        if (m_lsystem.getDepth() == (iDepth + 1)) {
                            break;
                        }

                        //get information about element
                        //Log.debug("Start branch");
                        String strElement = rule.getSuccessor().substring(i);

                        strElement = strElement.substring(0,
                                strElement.indexOf(
                                    GrowingPlantsDefinitions.C_END_ELEMENT) +
                                1);

                        //Log.debug("Element string " + strElement);
                        float fElementAge = Rule.getElementAge(strElement) * m_lsystem.getScale();

                        //Log.debug("StartAge= " + fStartAge + " ElementAge= " + fElementAge);
                        //create the rotation for the branch
                        TransformGroup tgRotation = new TransformGroup();
                        Transform3D t3dRotation = new Transform3D(buildMatrix(
                                    v3fRight, v3fHeading, v3fUp),
                                new Vector3d(), 1);
                        tgRotation.setTransform(t3dRotation);
                        tgPrevious.addChild(tgRotation);

                        //create the scale node for the branch
                        TransformGroup tgScale = new TransformGroup();
                        tgScale.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                        tgScale.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

                        Transform3D t3dScale = new Transform3D();
                        t3dScale.setScale(0.00000000001); //set initial scale value to zero
                        tgScale.setTransform(t3dScale);

                        Branch branch = new Branch(fElementAge, fThickness,
                                fThickness - m_lsystem.getDecrement(),
                                m_lsystem.getBranchAppearance());
                        tgScale.addChild(branch);
                        tgRotation.addChild(tgScale);

                        //create translation for the following subtree
                        TransformGroup tgTranslation = new TransformGroup();
                        tgTranslation.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                        tgTranslation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                        tgRotation.addChild(tgTranslation);

                        //make animation group for later animation of this branch and add it to the list
                        AnimationGroup ag2 = new AnimationGroup(fStartAge,
                                fStartAge + fElementAge, tgScale, tgTranslation);
                        m_vAnimation.add(ag2);

                        //recursively call this function again until max depth is reached
                        parseRule(tgTranslation, rule.getElementID(strElement),
                            fStartAge + rule.getPredecessorAge(), iDepth,
                            fThickness - m_lsystem.getDecrement());

                        break;

                    case GrowingPlantsDefinitions.C_END_ELEMENT:
                        break;

                    case GrowingPlantsDefinitions.C_BEGIN_LEAF:
                        //push rotation information
                        m_stackRot.push(new Vector3f(v3fHeading));
                        m_stackRot.push(new Vector3f(v3fRight));
                        m_stackRot.push(new Vector3f(v3fUp));

                        //create the needed TGs and add the leaf
                        tgLeafRot = new TransformGroup();

                        Transform3D t3dLeafRot = new Transform3D(buildMatrix(
                                    v3fRight, v3fHeading, v3fUp),
                                new Vector3d(), 1);
                        tgLeafRot.setTransform(t3dLeafRot);
                        tgPrevious.addChild(tgLeafRot);

                        //set rotation vectors to the initial values
                        v3fHeading = new Vector3f(0f, 1f, 0f);
                        v3fRight = new Vector3f(1f, 0f, 0f);
                        v3fUp = new Vector3f(0f, 0f, 1f);

                        //make stack for leaf points
                        stackLeaf = new Stack();
                        m3fLeafRot = new Matrix3f();

                        //create initial point and push it to the stack
                        p3fLeafPos = new Point3f(0.0f, 0.0f, 0.0f);
                        stackLeaf.push(p3fLeafPos);

                        break;

                    case GrowingPlantsDefinitions.C_LEAF_EDGE: //edge of leaf
                        //make rotation matrix with the turtle's values
                        m3fLeafRot = buildMatrix(v3fRight, v3fHeading, v3fUp);

                        //calculate new position vector for the point after the step
                        Vector3f v3fTemp = new Vector3f();
                        m3fLeafRot.transform(new Vector3f(0,
                                m_lsystem.getLength(), 0), v3fTemp);

                        //add the new step to the previous position
                        p3fLeafPos.add(v3fTemp);

                        //add the new point to the stack of leaf points
                        stackLeaf.push(new Point3f(p3fLeafPos));

                        break;

                    case GrowingPlantsDefinitions.C_END_LEAF: //sign that leaf is finished

                        int iPoints = stackLeaf.size();

                        // make array of points out of stack
                        Point3f[] p3fLeafPoints = new Point3f[iPoints];
                        stackLeaf.toArray(p3fLeafPoints);

                        //make new leaf with the array of points and leaf or flower appearance
                        Appearance app;

                        if (bIsFlower == true) {
                            app = m_lsystem.getFlowerAppearance();
                        } else {
                            app = m_lsystem.getLeafAppearance();
                        }

                        LeafShape leaf = new LeafShape(p3fLeafPoints, iPoints,
                                app);
                        bIsFlower = false;

                        //make tg for scaling of the leaf
                        TransformGroup tgLeafScale = new TransformGroup();
                        tgLeafScale.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                        tgLeafScale.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

                        Transform3D t3dLeafScale = new Transform3D();
                        t3dLeafScale.setScale(0.00000000001);
                        tgLeafScale.setTransform(t3dLeafScale);
                        tgLeafScale.addChild(leaf);

                        //add scale tg to the previously stored rotation node
                        tgLeafRot.addChild(tgLeafScale);

                        //pop last rotation information
                        v3fUp = (Vector3f) m_stackRot.pop();
                        v3fRight = (Vector3f) m_stackRot.pop();
                        v3fHeading = (Vector3f) m_stackRot.pop();

                        //add the TG for scaling to the list of scalable objects
                        AnimationGroup ag = new AnimationGroup(fStartAge,
                                fStartAge + 2, tgLeafScale);
                        m_vAnimation.add(ag);

                        break;

                    case GrowingPlantsDefinitions.C_PUSH_TURTLE: //push all turtle data
                        m_stackRot.push(new Vector3f(v3fHeading));
                        m_stackRot.push(new Vector3f(v3fRight));
                        m_stackRot.push(new Vector3f(v3fUp));

                        break;

                    case GrowingPlantsDefinitions.C_POP_TURTLE: //pop last pushed turtle data
                        v3fUp = (Vector3f) m_stackRot.pop();
                        v3fRight = (Vector3f) m_stackRot.pop();
                        v3fHeading = (Vector3f) m_stackRot.pop();

                        break;

                    case GrowingPlantsDefinitions.C_FLOWER:
                        bIsFlower = true;

                        break;

                    default:
                    } //switch
                } //for
            } //if
        } //enum
    } //parseRule

    /**
     * Calculates the rotation matrix for a 3D rotation around the axe
     * going through the point 0,0,0 with the direction described by the
     * vector of the given angle.
     *
     * @param v3fAxe The vector describing the axis going through the origin
     *        (0,0,0).
     * @param fAngle The angle describing the rotation value.
     *
     * @return The 3x3 float matrix which is the rotation matrix.
     */
    public Matrix3f rotationMatrix(Vector3f v3fAxe, float fAngle) {
        //store sinus and cosinus of the angle since it it used very often
        float fSinA = (float) Math.sin(fAngle);
        float fCosA = (float) Math.cos(fAngle);

        float fX = v3fAxe.x;
        float fY = v3fAxe.y;
        float fZ = v3fAxe.z;

        return new Matrix3f((fX * fX) + ((1f - (fX * fX)) * fCosA),
            (fX * fY * (1f - fCosA)) - (fZ * fSinA),
            (fX * fZ * (1f - fCosA)) + (fY * fSinA),
            (fX * fY * (1f - fCosA)) + (fZ * fSinA),
            (fY * fY) + ((1f - (fY * fY)) * fCosA),
            (fY * fZ * (1f - fCosA)) - (fX * fSinA),
            (fX * fZ * (1f - fCosA)) - (fY * fSinA),
            (fY * fZ * (1f - fCosA)) + (fX * fSinA),
            (fZ * fZ) + ((1f - (fZ * fZ)) * fCosA));
    }

    /**
     * Builds a new 3x3 matrix with the given three vectors by
     * inserting the vector values in the matrix columns.
     *
     * @param v3fCol0 The vector to be inserted in the first column.
     * @param v3fCol1 The vector to be inserted in the second column.
     * @param v3fCol2 The vector to be inserted in the third column.
     *
     * @return A newly created 3x3 matrix.
     */
    public Matrix3f buildMatrix(Vector3f v3fCol0, Vector3f v3fCol1,
        Vector3f v3fCol2) {
        Matrix3f m3fMatrix = new Matrix3f();
        m3fMatrix.setColumn(0, v3fCol0);
        m3fMatrix.setColumn(1, v3fCol1);
        m3fMatrix.setColumn(2, v3fCol2);

        return m3fMatrix;
    }

    /**
     * Gets the list of AnimtionGroup objects. These are all objects of
     * this plant which have to be scaled or translated.
     *
     * @return The vector containing the objects.
     */
    public Vector getAnimationList() {
        return m_vAnimation;
    }

    /**
     * Gets the X position of this plant.
     *
     * @return The X position.
     */
    public int getX() {
        return m_iX;
    }

    /**
     * Gets the Y position of this plant.
     *
     * @return The Y position.
     */
    public int getY() {
        return m_iY;
    }

    /**
     * Sets the X position of this plant.
     *
     * @param iX The X position.
     */
    public void setX(int iX) {
        m_iX = iX;
    }

    /**
     * Sets the Y position of this plant.
     *
     * @param iY The Y position.
     */
    public void setY(int iY) {
        m_iY = iY;
    }

    /**
     * Returns a string representation of this plant. The name followed
     * by the X and Y position.
     *
     * @return The string of this plant.
     */
    public String toString() {
        return m_strName + "      X: " + m_iX + "  Y: " + m_iY;
    }

    /**
     * Sets this plant instance as the selected one.
     */
    public void setSelected() {
        m_selected = this;
    }

    /**
     * Checks if this plant is selected.
     *
     * @return True if selected else false.
     */
    public boolean isSelected() {
        return (m_selected == this);
    }

    /**
     * Retrieves the selectedd plant.
     *
     * @return The selected plant
     */
    public static Plant getSelected() {
        return m_selected;
    }

    /**
     * Gets the name of the plant.
     *
     * @return The name of the plant.
     */
    public String getName() {
        return m_strName;
    }

    /**
     * Compares a plant to another. This method overrrides the method
     * from the comparable interface. It compared the strings of the names.
     *
     * @param obj Object to compare with. Must be an instance of Plant.
     *
     * @return Returns the return value of the compareto methos from the String
     *         class. The names of the plants are compared.
     */
    public int compareTo(Object obj) {
        if (obj instanceof Plant == false) {
            //Log.log("Wrong type of object. Must be instace of Plant");
            return 0;
        }

        Plant plant = (Plant) obj;

        return m_strName.compareTo(plant.getName());
    }
}
