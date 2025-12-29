/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry.computational;

/**
 * Molecular geometry predictions using VSEPR theory.
 *
 * VSEPR: Valence Shell Electron Pair Repulsion
 * Geometry depends on steric number (bonding + lone pairs).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MolecularGeometry {

    public enum Geometry {
        LINEAR("Linear", 180.0),
        BENT("Bent", 104.5),
        TRIGONAL_PLANAR("Trigonal planar", 120.0),
        TRIGONAL_PYRAMIDAL("Trigonal pyramidal", 107.0),
        TETRAHEDRAL("Tetrahedral", 109.5),
        SEESAW("Seesaw", 117.0),
        SQUARE_PLANAR("Square planar", 90.0),
        T_SHAPED("T-shaped", 90.0),
        TRIGONAL_BIPYRAMIDAL("Trigonal bipyramidal", 90.0),
        OCTAHEDRAL("Octahedral", 90.0),
        SQUARE_PYRAMIDAL("Square pyramidal", 90.0);

        private final String name;
        private final double idealAngle;

        Geometry(String name, double angle) {
            this.name = name;
            this.idealAngle = angle;
        }

        public String getName() {
            return name;
        }

        public double getIdealAngle() {
            return idealAngle;
        }
    }

    /**
     * Predicts molecular geometry from steric number and lone pairs.
     * 
     * @param bondingPairs Number of bonding electron pairs (atoms bonded)
     * @param lonePairs    Number of lone pairs on central atom
     * @return Predicted geometry
     */
    public static Geometry predict(int bondingPairs, int lonePairs) {
        int stericNumber = bondingPairs + lonePairs;

        switch (stericNumber) {
            case 2:
                return Geometry.LINEAR;
            case 3:
                switch (lonePairs) {
                    case 0:
                        return Geometry.TRIGONAL_PLANAR;
                    case 1:
                        return Geometry.BENT;
                    default:
                        return Geometry.LINEAR;
                }
            case 4:
                switch (lonePairs) {
                    case 0:
                        return Geometry.TETRAHEDRAL;
                    case 1:
                        return Geometry.TRIGONAL_PYRAMIDAL;
                    case 2:
                        return Geometry.BENT;
                    default:
                        return Geometry.LINEAR;
                }
            case 5:
                switch (lonePairs) {
                    case 0:
                        return Geometry.TRIGONAL_BIPYRAMIDAL;
                    case 1:
                        return Geometry.SEESAW;
                    case 2:
                        return Geometry.T_SHAPED;
                    default:
                        return Geometry.LINEAR;
                }
            case 6:
                switch (lonePairs) {
                    case 0:
                        return Geometry.OCTAHEDRAL;
                    case 1:
                        return Geometry.SQUARE_PYRAMIDAL;
                    case 2:
                        return Geometry.SQUARE_PLANAR;
                    default:
                        return Geometry.T_SHAPED;
                }
            default:
                return Geometry.LINEAR;
        }
    }

    /**
     * Predicts hybridization from steric number.
     * 
     * @param stericNumber Total electron domains
     * @return Hybridization string (sp, sp², sp³, etc.)
     */
    public static String hybridization(int stericNumber) {
        switch (stericNumber) {
            case 2:
                return "sp";
            case 3:
                return "sp²";
            case 4:
                return "sp³";
            case 5:
                return "sp³d";
            case 6:
                return "sp³d²";
            case 7:
                return "sp³d³";
            default:
                return "unknown";
        }
    }

    /**
     * Estimates bond angle with lone pair compression.
     * Each lone pair typically reduces angles by ~2.5°.
     * 
     * @param geometry  Base geometry
     * @param lonePairs Number of lone pairs
     * @return Estimated bond angle in degrees
     */
    public static double estimateBondAngle(Geometry geometry, int lonePairs) {
        double baseAngle = geometry.getIdealAngle();
        // Lone pairs compress angles slightly
        return baseAngle - (lonePairs * 2.5);
    }

    /**
     * Determines if molecule is polar based on geometry.
     * Symmetric geometries (linear, trigonal planar, tetrahedral with identical
     * substituents, etc.) are non-polar.
     */
    public static boolean isPolarGeometry(Geometry geometry) {
        switch (geometry) {
            case BENT:
            case TRIGONAL_PYRAMIDAL:
            case SEESAW:
            case T_SHAPED:
            case SQUARE_PYRAMIDAL:
                return true;
            default:
                return false;
        }
    }
}
