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

import javax.vecmath.Color3f;


/**
 * All definitions used in the lsys package are stored in this class.
 * Strings defining keywords, signs or other static and not changing data
 * should be defined in this class so it is easier to change a value and no
 * definitions are stored in the code of the program.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class GrowingPlantsDefinitions {
    /** Project name */
    public static final String STR_PROJECT_NAME = "Virtual Plants";

    /** The codeword for the name of the LSystem. Case sensitive ! */
    public static final String STR_NAME = "name";

    /**
     * The codeword for the depth of the LSystem. This is the number of
     * iterations to be performed. Case sensitive !
     */
    public static final String STR_DEPTH = "depth";

    /** The codeword for the angle between branches. Case sensitive ! */
    public static final String STR_ANGLE = "angle";

    /** The codeword for the thickness of a branch. Case sensitive ! */
    public static final String STR_THICKNESS = "thickness";

    /** The codeword for the length of a leaf or flower edge. Case sensitive ! */
    public static final String STR_LENGTH = "length";

    /**
     * The codeword for the decrement value of thickness of branches.
     * Case sensitive !
     */
    public static final String STR_DECREMENT = "decrement";

    /** The codeword for the axiom of the LSystem. Case sensitive ! */
    public static final String STR_AXIOM = "axiom";

    /** The codeword for a rule of the LSystem. Case sensitive ! */
    public static final String STR_RULE = "rule";

    /** The codeword for the color of the flowers. Case sensitive ! */
    public static final String STR_FLOWER = "flower";

    /** The codeword for the color of the leafs. Case sensitive ! */
    public static final String STR_LEAF = "leaf";

    /** The codeword for the color of the branches. Case sensitive ! */
    public static final String STR_BRANCH = "branch";

    /** The codeword for scaling factor. Case sensitive ! */
    public static final String STR_SCALE = "scale";

    /** The codeword for the separator of a codeword and the value. */
    public static final String STR_SEPARATOR = "=";

    /** The codeword for the separator of the predecessor to the successor. */
    public static final String STR_RULESIGN = "->";

    /**
     * This string contains all the valid signs for a rule or axiom. No
     * other signs (except letters) may be used.
     */
    public static final String STR_SIGNS = "+-&^\\/|[]{}(),!0123456789";

    /** The sign to turn left */
    public static final char C_TURN_LEFT = '+';

    /** The sign to turn right */
    public static final char C_TURN_RIGHT = '-';

    /** The sign to pitch down */
    public static final char C_PITCH_DOWN = '&';

    /** The sign to pitch up */
    public static final char C_PITCH_UP = '^';

    /** The sign to roll left */
    public static final char C_ROLL_LEFT = '\\';

    /** The sign to roll right */
    public static final char C_ROLL_RIGHT = '/';

    /** The sign to turn around 180� */
    public static final char C_TURN_AROUND = '|';

    /** The sign to store the actual turtle information */
    public static final char C_PUSH_TURTLE = '[';

    /** The sign to restore the actual turtle information */
    public static final char C_POP_TURTLE = ']';

    /** Begin leaf */
    public static final char C_BEGIN_LEAF = '{';

    /** End leaf */
    public static final char C_END_LEAF = '}';

    /** Begin element */
    public static final char C_BEGIN_ELEMENT = '(';

    /** End element */
    public static final char C_END_ELEMENT = ')';

    /** Element separator */
    public static final char C_ELEMENT_SEPARATOR = ',';

    /** The sign to make a step without drawing */
    public static final char C_LEAF_EDGE = 'f';

    /** The sign to flower appearance */
    public static final char C_FLOWER = '!';

    /** DOCUMENT ME! */
    public static final String STR_BROWN = "brown";

    /** DOCUMENT ME! */
    public static final String STR_GREEN = "green";

    /** DOCUMENT ME! */
    public static final String STR_RED = "red";

    /** DOCUMENT ME! */
    public static final String STR_YELLOW = "yellow";

    /** DOCUMENT ME! */
    public static final String STR_WHITE = "white";

    /** DOCUMENT ME! */
    public static final Color3f COLOR_RED = new Color3f(1, 0, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_BLUE = new Color3f(0, 0, 1);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_GREEN = new Color3f(0, 1, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_WHITE = new Color3f(1, 1, 1);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_BLACK = new Color3f(0, 0, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_SILVER = new Color3f(0.75f, 0.75f, 0.75f);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_BROWN = new Color3f(0.5f, 0.35f, 0.15f);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_LIGHT_BLUE = new Color3f(0, 0.75f, 1);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_DARK_GREEN = new Color3f(0, 0.5f, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_DARK_BROWN = new Color3f(0.05f, 0.04f,
            0.02f);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_DARK_RED = new Color3f(0.5f, 0, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_DARK_YELLOW = new Color3f(0.5f, 0.5f, 0);

    /** DOCUMENT ME! */
    public static final Color3f COLOR_YELLOW = new Color3f(1, 1, 0);
}
