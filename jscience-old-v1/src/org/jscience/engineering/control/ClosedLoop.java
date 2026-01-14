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

package org.jscience.engineering.control;

import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ClosedLoop extends BlackBox {
    /**
     * DOCUMENT ME!
     */
    private OpenLoop forwardPath = new OpenLoop(); // forward path boxes

    /**
     * DOCUMENT ME!
     */
    private OpenLoop closedPath = new OpenLoop(); // full closed path boxes

    /**
     * DOCUMENT ME!
     */
    private Vector<BlackBox> feedbackPath = new Vector<BlackBox>(); // feedback path boxes

    /**
     * DOCUMENT ME!
     */
    private int nFeedbackBoxes = 0; // number of boxes in feedback path

    /**
     * DOCUMENT ME!
     */
    private boolean checkPath = false; // true if segment has been called

    /**
     * DOCUMENT ME!
     */
    private boolean checkNoMix = true; // true - no ADC or DAC

    /**
     * DOCUMENT ME!
     */
    private boolean checkConsolidate = false; // true if consolidate has been called

    // Constructor
    /**
     * Creates a new ClosedLoop object.
     */
    public ClosedLoop() {
        this.sNumer = super.sNumer;
        super.name = "Closed Loop";
        super.fixedName = "Closed Loop";
    }

    // Add box to the forward path
    /**
     * DOCUMENT ME!
     *
     * @param box DOCUMENT ME!
     */
    public void addBoxToForwardPath(BlackBox box) {
        this.forwardPath.addBoxToPath(box);
    }

    // Add box to the open path
    /**
     * DOCUMENT ME!
     *
     * @param box DOCUMENT ME!
     */
    public void addBoxToFeedbackPath(BlackBox box) {
        this.feedbackPath.addElement(box);
        this.nFeedbackBoxes++;
    }

    // Consolidate all boxes into appropriate segments and
    /**
     * DOCUMENT ME!
     */
    public void consolidate() {
        // add feedback boxes to forward path boxes
        this.closedPath = this.forwardPath.copy();

        for (int i = 0; i < this.nFeedbackBoxes; i++) {
            this.closedPath.addBoxToPath((BlackBox) this.feedbackPath.elementAt(
                    i));
        }

        // combine forward path boxes
        this.forwardPath.consolidate();

        // combine closed path boxes
        this.closedPath.consolidate();

        // Calculate transfer function
        ComplexPolynomial fpNumer = this.forwardPath.getSnumer();
        ComplexPolynomial fpDenom = this.forwardPath.getSdenom();
        ComplexPolynomial cpNumer = this.closedPath.getSnumer();
        ComplexPolynomial cpDenom = this.closedPath.getSdenom();

        if (fpDenom.equals(cpDenom)) {
            super.sNumer = (ComplexPolynomial) fpNumer.clone();
            this.sDenom = (ComplexPolynomial) ((ComplexPolynomial) (cpNumer.add(fpDenom))).clone();
        } else {
            super.sNumer = (ComplexPolynomial) fpNumer.multiply(cpDenom);
            super.sDenom = (ComplexPolynomial) cpNumer.add(cpDenom.multiply(
                        fpDenom));
        }

        this.checkConsolidate = true;
    }

    // Return number of boxes in the forward path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfBoxesInForwardPath() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.forwardPath.getNumberOfBoxes();
    }

    // Return number of boxes in the closed path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfBoxesInClosedLoop() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.closedPath.getNumberOfBoxes();
    }

    // Return segment Vector for forward path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getForwardPathSegmentsVector() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.forwardPath.getSegmentsVector();
    }

    // Return segment Vector for closed path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getClosedLoopSegmentsVector() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.closedPath.getSegmentsVector();
    }

    // Return number of segments in the forward path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfSegmentsInForwardPath() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.forwardPath.getNumberOfSegments();
    }

    // Return number of segments in the closed path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfSegmentsInClosedLoop() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.closedPath.getNumberOfSegments();
    }

    // Return name of all boxes in forward path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNamesOfBoxesInForwardPath() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.forwardPath.getNamesOfBoxes();
    }

    // Return name of all boxes in closed path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNamesOfBoxesInClosedLoop() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.closedPath.getNamesOfBoxes();
    }

    // Remove all boxes from the path
    /**
     * DOCUMENT ME!
     */
    public void removeAllBoxes() {
        this.forwardPath.removeAllBoxes();
        this.closedPath.removeAllBoxes();
    }
}
