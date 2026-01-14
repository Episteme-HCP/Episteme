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

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

/**
 * This class represents an LSystem as we know it in text form. It can read values from a file and store
 * them in this class.
 * The file must respect some definitions: All codewords listed in the Def class must stand at the beginning
 * of the line and be written in the right case. As separator to the value only the specified symbol may be used.
 * The order of the lines does not matter. Lines which do not begin with a codeword are ignored.
 * White spaces may be used or not. See the design for an exact description on the keywords.
 * <p/>
 * Example:<br>
 * <pre>
 * name = Red
 * depth = 12
 * angle = 22.5
 * thickness = 0.3
 * decrement = 0.025
 * length = 0.8
 * branch = brown
 * leaf = green
 * flower = red
 * scale = 0.3
 * <p/>
 * axiom = (a,0)
 * <p/>
 * rule = (a,0) -> (b,5)
 * rule = (b,1) -> [////(b,4)][&////(c,4)]^////(c,4)
 * rule = (b,0) -> [&&&{-^ff&+ff+&ff^-|-ff+ff+ff}]//////[&&&{-^ff&+ff+&ff^-|-ff+ff+ff}]//////[&&&{-^ff&+ff+&ff^-|-ff+ff+ff}]
 * rule = (c,1) -> (z,2)[&////(d,3)]^////(d,3)
 * rule = (c,2) -> [&&&{-^ff&+ff+&ff^-|-ff+ff+ff}]///////&&&{-^ff&+ff+&ff^-|-ff+ff+ff}
 * rule = (d,2) -> (z,1)[&////(e,2)]^////(e,2)
 * rule = (d,2) -> [&&&{-^f&+f+&f^-|-f+f+f}]/////[&&&{-^f&+f+&f^-|-f+f+f}]/////[&&&{-^f&+f+&f^-|-f+f+f}]
 * rule = (e,3) -> [&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]//[&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]//[&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]//[&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]
 * rule = (z,2) -> //[&&{!-f++f-|-f++f}]//[&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]////[&&{!-f++f-|-f++f}]//[&&&{!-f++f-|-f++f}]//[&&{!-f++f-|-f++f}]
 * </pre>
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class LSystem {
    /**
     * The name of the LSystem
     */
    private String m_strName;

    /**
     * The axiom of the LSystem
     */
    private String m_strAxiom;

    /**
     * The appearance for the leafs
     */
    private Appearance m_appLeaf;

    /**
     * The appearance for the flowers
     */
    private Appearance m_appFlower;

    /**
     * The appearance for the branches
     */
    private Appearance m_appBranch;

    /**
     * A vector containing all the rules for the LSystem
     */
    private Vector m_vRules;

    /**
     * The recursion depth of the LSystem
     */
    private int m_iDepth;

    /**
     * The angle for the turtle to turn in radians.
     */
    private float m_fAngle;

    /**
     * The thickness of the branch on the actual position.
     */
    private float m_fThickness;

    /**
     * The length of the leaf or flower egdes.
     */
    private float m_fLength = 1.0f;

    /**
     * The step to decrement the thickness.
     */
    private float m_fDecrement;

    /**
     * The scaling factor for the plant
     */
    private float m_fScale = 1.0f;

    /**
     * Constructor. Initializes the member variables, reads the values for the LSystem from
     * a file and stores them in the member fields.
     * Note that the values have to be declared in a certain manner defined in this class.
     *
     * @param fileLsy The file to extract and store.
     */
    public LSystem(File fileLsy) throws Exception {
        m_strAxiom = null;
        m_vRules = new Vector();
        m_iDepth = 0;
        m_fAngle = 0.0f;

        if (fileLsy.canRead() == false) { //the file name is not valid
            //Log.log(fileLsy.toString(), Log.NO_FILE, true);

            return;
        }

        FileReader frValues = new FileReader(fileLsy);
        //Log.debug("FileReader created");

        BufferedReader brValues = new BufferedReader(frValues);
        //Log.debug("BufferedReader created");

        String strLine;

        while ((strLine = brValues.readLine()) != null) {
            strLine.trim();
            //Log.debug("Actual line: " + strLine);

            //check what type of string it is
            if (strLine.startsWith(GrowingPlantsDefinitions.STR_NAME)) {
                m_strName = Converter.getStringBehind(strLine, GrowingPlantsDefinitions.STR_SEPARATOR);
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_DEPTH)) {
                m_iDepth = Converter.readInt(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_ANGLE)) {
                m_fAngle = (float) Math.toRadians(Converter.readFloat(Converter.getStringBehind(strLine, GrowingPlantsDefinitions.STR_SEPARATOR)));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_LENGTH)) {
                m_fLength = Converter.readFloat(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_THICKNESS)) {
                m_fThickness = Converter.readFloat(Converter.getStringBehind(strLine, GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_DECREMENT)) {
                m_fDecrement = Converter.readFloat(Converter.getStringBehind(strLine, GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_AXIOM)) {
                m_strAxiom = Converter.getStringBehind(strLine, GrowingPlantsDefinitions.STR_SEPARATOR);
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_RULE)) {
                m_vRules.add(new Rule(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR)));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_FLOWER)) {
                m_appFlower = makeAppearance(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_LEAF)) {
                m_appLeaf = makeAppearance(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_BRANCH)) {
                m_appBranch = makeAppearance(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            } else if (strLine.startsWith(GrowingPlantsDefinitions.STR_SCALE)) {
                m_fScale = Converter.readFloat(Converter.getStringBehind(strLine,
                        GrowingPlantsDefinitions.STR_SEPARATOR));
            }

            /* more values can be added here
            else if ( strLine.startsWith(Def.STR_) )
            {
            }
            else if ( strLine.startsWith(Def.STR_) )
            {
            }*/
        }

        //check if file contains neede values
        if ((m_strAxiom == null) || m_vRules.isEmpty()) {
            //Log.log(fileLsy.toString(), Log.INVALID_FILE, true);
        }

        //set default appearance if not defined in file
        if (m_appBranch == null) {
            m_appBranch = makeAppearance(GrowingPlantsDefinitions.STR_BROWN);
        }

        if (m_appLeaf == null) {
            m_appLeaf = makeAppearance(GrowingPlantsDefinitions.STR_GREEN);
        }

        if (m_appFlower == null) {
            m_appFlower = makeAppearance(GrowingPlantsDefinitions.STR_RED);
        }

        //modify appearance of leafes and flowers to make them visible from both sides (else they are only visible from top)
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        pa.setBackFaceNormalFlip(true);
        m_appLeaf.setPolygonAttributes(pa);
        m_appFlower.setPolygonAttributes(pa);

        brValues.close();
        //Log.debug("BufferedReader closed");

        frValues.close();
        //Log.debug("FileReader closed");
    }

    /**
     * Makes a new appearance object with the string name of a color.
     *
     * @param strColor The color name as string.
     * @return The appearance of the specified color.
     */
    public Appearance makeAppearance(String strColor) {
        Appearance app = new Appearance();
        Material material;

        strColor.trim();

        if (strColor.compareTo(GrowingPlantsDefinitions.STR_BROWN) == 0) {
            material = new Material(GrowingPlantsDefinitions.COLOR_DARK_BROWN, GrowingPlantsDefinitions.COLOR_BLACK,
                    GrowingPlantsDefinitions.COLOR_BROWN, GrowingPlantsDefinitions.COLOR_BROWN, 10f);
        } else if (strColor.compareTo(GrowingPlantsDefinitions.STR_GREEN) == 0) {
            material = new Material(GrowingPlantsDefinitions.COLOR_DARK_GREEN, GrowingPlantsDefinitions.COLOR_BLACK,
                    GrowingPlantsDefinitions.COLOR_BLACK, GrowingPlantsDefinitions.COLOR_DARK_GREEN, 5f);
        } else if (strColor.compareTo(GrowingPlantsDefinitions.STR_WHITE) == 0) {
            material = new Material(GrowingPlantsDefinitions.COLOR_SILVER, GrowingPlantsDefinitions.COLOR_BLACK,
                    GrowingPlantsDefinitions.COLOR_BLACK, GrowingPlantsDefinitions.COLOR_WHITE, 5f);
        } else if (strColor.compareTo(GrowingPlantsDefinitions.STR_RED) == 0) {
            material = new Material(GrowingPlantsDefinitions.COLOR_DARK_RED, GrowingPlantsDefinitions.COLOR_BLACK,
                    GrowingPlantsDefinitions.COLOR_BLACK, GrowingPlantsDefinitions.COLOR_RED, 5f);
        } else if (strColor.compareTo(GrowingPlantsDefinitions.STR_YELLOW) == 0) {
            material = new Material(GrowingPlantsDefinitions.COLOR_DARK_YELLOW, GrowingPlantsDefinitions.COLOR_BLACK,
                    GrowingPlantsDefinitions.COLOR_BLACK, GrowingPlantsDefinitions.COLOR_YELLOW, 5f);
        } else {
            material = new Material();
        }

        material.setLightingEnable(true);
        app.setMaterial(material);

        return app;
    }

    /**
     * Sets the derivation depth for the LSystem.
     *
     * @param iDepth The depth to set.
     */
    public void setDepth(int iDepth) {
        m_iDepth = iDepth;
    }

    /**
     * Gets the derivation depth for the LSystem.
     *
     * @return The depth.
     */
    public int getDepth() {
        return m_iDepth;
    }

    /**
     * Sets the angle for the LSystem to the member field.
     *
     * @param fAngle The new value for the angle.
     */
    public void setAngle(float fAngle) {
        m_fAngle = fAngle;
    }

    /**
     * Gets the angle of the LSystem
     *
     * @return The angle of the system
     */
    public float getAngle() {
        return m_fAngle;
    }

    /**
     * Stores the new axiom for this class.
     *
     * @param strAxiom The new axiom to set.
     */
    public void setAxiom(String strAxiom) {
        m_strAxiom = strAxiom;
    }

    /**
     * Returns the  axiom of this class.
     *
     * @return The axiom.
     */
    public String getAxiom() {
        return m_strAxiom;
    }

    /**
     * Get the thickness of the branch at beginning.
     *
     * @return The thickness of the branch.
     */
    public float getThickness() {
        return m_fThickness * m_fScale;
    }

    /**
     * Gets the length of the branch at beginning.
     *
     * @return The length of the branches.
     */
    public float getLength() {
        return m_fLength * m_fScale;
    }

    /**
     * Gets the value of decrenentation of the thickness of a branch.
     *
     * @return The decrementation value.
     */
    public float getDecrement() {
        return m_fDecrement * m_fScale;
    }

    /**
     * Gets the value of decrenentation of the thickness of a branch.
     *
     * @return The scale value.
     */
    public float getScale() {
        return m_fScale;
    }

    /**
     * Gets the name of the plant.
     *
     * @return The name.
     */
    public String getName() {
        return m_strName;
    }

    /**
     * Gets the name of the plant.
     */
    public void setName(String strName) {
        m_strName = strName;
    }

    /**
     * Gets the rules of the plant.
     *
     * @return The name.
     */
    public Vector getRules() {
        return m_vRules;
    }

    /**
     * Gets the name of the plant.
     */
    public void setRules(Vector vRules) {
        m_vRules = vRules;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Appearance getBranchAppearance() {
        return m_appBranch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Appearance getLeafAppearance() {
        return m_appLeaf;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Appearance getFlowerAppearance() {
        return m_appFlower;
    }
}
