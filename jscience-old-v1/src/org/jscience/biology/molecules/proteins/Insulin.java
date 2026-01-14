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

package org.jscience.biology.molecules.proteins;

import org.jscience.biology.Protein;


/**
 * A class representing the human Insulin (precursor) molecule.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//http://us.expasy.org/cgi-bin/niceprot.pl?P01308
//INS_HUMAN
//P01308
//Insulin [Precursor]
//Insulin is a hormone secreted by cells in the pancreas. The major function of this hormone is to allow glucose to enter the body cells in order to be burned as fuel. If the body ceases to produce this hormone the result is a serious disease known as diabetes.
//CAS Number 9004-10-8
public final class Insulin extends Protein {
    //formula should be C257 H383 N65 O77 S6
    /**
     * Creates a new Insulin object.
     */
    public Insulin() {
        super(
            "MALWMRLLPLLALLALWGPDPAAAFVNQHLCGSHLVEALYLVCGERGFFYTPKTRREAEDLQVGQVELGGGPGAGSLQPLALEGSLQKRGIVEQCCTSICSLYQLENYCN");
    }
}
