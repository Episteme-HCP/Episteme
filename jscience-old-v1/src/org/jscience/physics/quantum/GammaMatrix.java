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

package org.jscience.physics.quantum;

import org.jscience.mathematics.algebraic.fields.ComplexField;
import org.jscience.mathematics.algebraic.matrices.ComplexSquareMatrix;
import org.jscience.mathematics.algebraic.numbers.Complex;


/**
 * The GammaMatrix class provides an object for encapsulating the gamma
 * matrices.
 *
 * @author Mark Hale
 * @version 1.2
 */
public final class GammaMatrix extends ComplexSquareMatrix {
    /** DOCUMENT ME! */
    private static final Complex[][] y0_D = {
            {
                ComplexField.ONE, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ONE, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.MINUS_ONE,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.MINUS_ONE
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y1_D = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ONE
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ONE,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.MINUS_ONE, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.MINUS_ONE, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y2_D = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.I
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.MINUS_I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.MINUS_I, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y3_D = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ONE,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.MINUS_ONE
            },
            {
                ComplexField.MINUS_ONE, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ONE, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y5_D = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ONE,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ONE
            },
            {
                ComplexField.ONE, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ONE, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y0_M = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.I
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.MINUS_I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.I, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.MINUS_I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y1_M = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.MINUS_I
            },
            {
                ComplexField.I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.MINUS_I, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y2_M = {
            {
                ComplexField.I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.I, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.MINUS_I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.MINUS_I
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y3_M = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.MINUS_I
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.MINUS_I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.MINUS_I, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.MINUS_I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** DOCUMENT ME! */
    private static final Complex[][] y5_M = {
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.I,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.I
            },
            {
                ComplexField.MINUS_I, ComplexField.ZERO, ComplexField.ZERO,
                ComplexField.ZERO
            },
            {
                ComplexField.ZERO, ComplexField.MINUS_I, ComplexField.ZERO,
                ComplexField.ZERO
            }
        };

    /** Gamma 0 matrix (Dirac representation). */
    public static final GammaMatrix Y0_D = new GammaMatrix(y0_D);

    /** Gamma 1 matrix (Dirac representation). */
    public static final GammaMatrix Y1_D = new GammaMatrix(y1_D);

    /** Gamma 2 matrix (Dirac representation). */
    public static final GammaMatrix Y2_D = new GammaMatrix(y2_D);

    /** Gamma 3 matrix (Dirac representation). */
    public static final GammaMatrix Y3_D = new GammaMatrix(y3_D);

    /** Gamma 5 matrix (Dirac representation). */
    public static final GammaMatrix Y5_D = new GammaMatrix(y5_D);

    /** Gamma 0 matrix (Weyl representation). */
    public static final GammaMatrix Y0_W = Y5_D;

    /** Gamma 1 matrix (Weyl representation). */
    public static final GammaMatrix Y1_W = Y1_D;

    /** Gamma 2 matrix (Weyl representation). */
    public static final GammaMatrix Y2_W = Y2_D;

    /** Gamma 3 matrix (Weyl representation). */
    public static final GammaMatrix Y3_W = Y3_D;

    /** Gamma 5 matrix (Weyl representation). */
    public static final GammaMatrix Y5_W = Y0_D;

    /** Gamma 0 matrix (Majorana representation). */
    public static final GammaMatrix Y0_M = new GammaMatrix(y0_M);

    /** Gamma 1 matrix (Majorana representation). */
    public static final GammaMatrix Y1_M = new GammaMatrix(y1_M);

    /** Gamma 2 matrix (Majorana representation). */
    public static final GammaMatrix Y2_M = new GammaMatrix(y2_M);

    /** Gamma 3 matrix (Majorana representation). */
    public static final GammaMatrix Y3_M = new GammaMatrix(y3_M);

    /** Gamma 5 matrix (Majorana representation). */
    public static final GammaMatrix Y5_M = new GammaMatrix(y5_M);

/**
     * Constructs a gamma matrix.
     *
     * @param gammaArray DOCUMENT ME!
     */
    private GammaMatrix(Complex[][] gammaArray) {
        super(gammaArray);
    }

    /**
     * Returns true if this matrix is unitary.
     *
     * @return DOCUMENT ME!
     */
    public boolean isUnitary() {
        return true;
    }

    /**
     * Returns the determinant.
     *
     * @return DOCUMENT ME!
     */
    public Complex det() {
        return ComplexField.MINUS_ONE;
    }

    /**
     * Returns the trace.
     *
     * @return DOCUMENT ME!
     */
    public Complex trace() {
        return ComplexField.ZERO;
    }
}
