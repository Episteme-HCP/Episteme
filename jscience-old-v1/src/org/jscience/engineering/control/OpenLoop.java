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

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.analysis.polynomials.ComplexPolynomial;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class OpenLoop extends BlackBox {
    /**
     * DOCUMENT ME!
     */
    private Vector<BlackBox> openPath = new Vector<BlackBox>(); // open path boxes

    /**
     * DOCUMENT ME!
     */
    private Vector<Object> segments = new Vector<Object>(); // start of segment, end of segment, type of each segment, i.e. analogue, digital, AtoD, DtoA, ZOH

    /**
     * DOCUMENT ME!
     */
    private int nBoxes = 0; // number of boxes in original path

    /**
     * DOCUMENT ME!
     */
    private int nSeg = 0; // number of analogue, digital, AtoD, ZOH segments

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
     * Creates a new OpenLoop object.
     */
    public OpenLoop() {
        super.name = "OpenLoop";
        super.fixedName = "OpenLoop";
    }

    // Add box to the open path
    /**
     * DOCUMENT ME!
     *
     * @param box DOCUMENT ME!
     */
    public void addBoxToPath(BlackBox box) {
        this.openPath.addElement(box);
        this.nBoxes++;
    }

    // Consolidate all boxes into appropriate segments and combine all boxes into one box
    /**
     * DOCUMENT ME!
     */
    public void consolidate() {
        // Empty segments Vector if openPath Vector has been updated
        if (!segments.isEmpty()) {
            segments.removeAllElements();
            this.nBoxes = 0;
            this.nSeg = 0;
            this.checkNoMix = true;
            this.checkPath = false;
        }

        // Find analogue, digital and conversion segments in OpenLoop
        this.segment();

        // Combine all boxes into a single box and make this instance that combined box
        BlackBox aa = null;

        if (this.nSeg == 1) {
            aa = (BlackBox) this.segments.elementAt(3);
        } else {
            aa = this.combineSegment(0, this.nBoxes);
        }

        super.sNumer = (ComplexPolynomial) aa.sNumer.clone();
        super.sDenom = (ComplexPolynomial) aa.sDenom.clone();
        super.sNumerPade = (ComplexPolynomial) aa.sNumerPade.clone();
        super.sDenomPade = (ComplexPolynomial) aa.sDenomPade.clone();
        super.sNumerDeg = aa.sNumerDeg;
        super.sDenomDeg = aa.sDenomDeg;
        super.sNumerDegPade = aa.sNumerDegPade;
        super.sDenomDegPade = aa.sDenomDegPade;
        super.deadTime = aa.deadTime;
        super.sZeros = ArrayMathUtils.copy(aa.sZeros);
        super.sPoles = ArrayMathUtils.copy(aa.sPoles);
        super.sZerosPade = ArrayMathUtils.copy(aa.sZerosPade);
        super.sPolesPade = ArrayMathUtils.copy(aa.sPolesPade);
        super.padeAdded = true;

        this.checkConsolidate = true;
    }

    // Find analogue and digital segments
    /**
     * DOCUMENT ME!
     */
    public void segment() {
        this.checkPath = true; // this method, segment, has been called
        this.nBoxes = openPath.size(); // number of boxes in openPath

        String thisName = " "; // name of current openPath box under examination

        // Find analogue, digital, ZOH/ADC and DAC segments
        int iStart1 = 0; // start index of block under examination
        int iEnd1 = 0; // final index of block under examination
        int iStart2 = 0; // start index of next block after block under examination
                         //  to accomodate separate block for ZOH/ADC or DAC conversion

        int iEnd2 = 0; // final index of next block after block under examination
                       //  to accomodate separate block for ZOH/ADC or DAC conversion

        int iNewStart = 0; // start index of next block after segment/s completed
        int nnBoxes = 0; // number of segments in a completed segement test
        int nInSeg = 0; // counter for number of openPath boxes in a segment

        String name1 = " "; // name of first segment resulting from a segment test
        String name2 = " "; // name of second segment resulting from a segment test
        String lastConv = " "; // name of the last convertor, e.g. AtoD or DtoA, found

        int ii = 0; // counter indicating position along openPath

        double deltaThold = 0.0D; // holds value of deltaT for box under examination

        while (ii < this.nBoxes) {
            nInSeg++;

            BlackBox bb = (BlackBox) openPath.get(ii);
            thisName = bb.fixedName;

            //if(bb.deltaT!=0.0D)deltaThold=bb.deltaT;

            // Look for ZOH
            if (thisName.equals("ZeroOrderHold")) {
                if (!lastConv.equals(" ")) {
                    this.checkNoMix = false;
                }

                if (ii < (this.nBoxes - 1)) {
                    BlackBox cc = (BlackBox) openPath.get(ii + 1);

                    // Look for following ADC
                    if (cc.fixedName.equals("AtoD")) {
                        if (lastConv.equals("AtoD")) {
                            throw new IllegalArgumentException(
                                "Two consecutive ADCs with no intervening DAC");
                        }

                        if (nInSeg > 1) {
                            iEnd1 = ii - 1;
                            name1 = "analogue";
                            iStart2 = ii;
                            iEnd2 = ii + 1;
                            name2 = "AtoD";
                            nnBoxes = 2;
                            this.nSeg += 2;
                            ii = ii + 2;
                            iNewStart = iEnd2 + 1;
                        } else {
                            iEnd1 = ii + 1;
                            name1 = "AtoD";
                            nnBoxes = 1;
                            this.nSeg += 1;
                            ii = ii + 2;
                            iNewStart = iEnd1 + 1;
                        }

                        lastConv = "AtoD";
                        nInSeg = 0;
                    } else {
                        System.out.println(
                            "WARNING!! OpenLoop.checkPath: ZOH without a following ADC");

                        if (nInSeg > 1) {
                            iEnd1 = ii - 1;
                            name1 = "analogue";
                            iStart2 = ii;
                            iEnd2 = ii;
                            name2 = "ZOH";
                            nnBoxes = 2;
                            this.nSeg = +2;
                            ii = ii + 1;
                            iNewStart = iEnd2 + 1;
                        } else {
                            iEnd1 = ii;
                            name1 = "ZOH";
                            nnBoxes = 1;
                            this.nSeg = +1;
                            ii = ii + 1;
                            iNewStart = iEnd1 + 1;
                        }

                        nInSeg = 0;
                        lastConv = "ZOH";
                    }
                } else {
                    System.out.println(
                        "WARNING!! OpenLoop.checkPath: path ends with ZOH");

                    if (nInSeg > 1) {
                        iEnd1 = ii - 1;
                        name1 = "analogue";
                        iStart2 = ii;
                        iEnd2 = ii;
                        name2 = "ZOH";
                        nnBoxes = 2;
                        this.nSeg += 2;
                        ii = ii + 2;
                        iNewStart = iEnd2 + 1;
                    } else {
                        iEnd1 = ii;
                        name1 = "ZOH";
                        nnBoxes = 1;
                        this.nSeg = +1;
                        ii = ii + 1;
                        iNewStart = iEnd1 + 1;
                    }

                    lastConv = "ZOH";
                    nInSeg = 0;
                }
            } else {
                if (thisName.equals("AtoD")) {
                    throw new IllegalArgumentException(
                        "ADC without preceeding ZOH");
                }

                // Look for DAC
                if (thisName.equals("DtoA")) {
                    if (lastConv.equals("DtoA")) {
                        throw new IllegalArgumentException(
                            "Two consecutive DACs with no intervening ADC");
                    }

                    if (lastConv.equals("ZOH")) {
                        throw new IllegalArgumentException(
                            "ZOH followed by DAC");
                    }

                    if (!lastConv.equals(" ")) {
                        this.checkNoMix = false;
                    }

                    if (nInSeg > 1) {
                        iEnd1 = ii - 1;
                        name1 = "digital";
                        iStart2 = 2;
                        iEnd2 = ii;
                        ii = ii + 1;
                        iNewStart = iEnd1 + 1;
                        nnBoxes = 2;
                        this.nSeg = +2;
                    } else {
                        iEnd1 = ii;
                        name1 = "DtoA";
                        ii = ii + 1;
                        iNewStart = iEnd1 + 1;
                        nnBoxes = 1;
                        this.nSeg = +1;
                    }

                    lastConv = "DtoA";
                    nInSeg = 0;
                }
            }

            // Add segment/s found to segments Vector as 4 elements:
            // 1. start index, 2. final index, 3. name, 4. all boxes in segment combined as a single box
            if (nnBoxes > 0) {
                this.segments.addElement(new Integer(iStart1));
                this.segments.addElement(new Integer(iEnd1));
                this.segments.addElement(name1);

                BlackBox dd = this.combineSegment(iStart1, iEnd1);
                this.segments.addElement(dd);

                if (nnBoxes == 2) {
                    this.segments.addElement(new Integer(iStart2));
                    this.segments.addElement(new Integer(iEnd2));
                    this.segments.addElement(name2);

                    BlackBox ee = this.combineSegment(iStart2, iEnd2);
                    this.segments.addElement(ee);
                }

                iStart1 = iNewStart;
            } else {
                ii++;
            }

            if ((ii >= this.nBoxes) && (ii != iNewStart)) {
                iEnd1 = ii - 1;
                name1 = "analogue";

                if (lastConv.equals("AtoD")) {
                    name1 = "digital";
                }

                this.nSeg = +1;
                this.segments.addElement(new Integer(iStart1));
                this.segments.addElement(new Integer(iEnd1));
                this.segments.addElement(name1);

                BlackBox ff = this.combineSegment(iStart1, iEnd1);
                this.segments.addElement(ff);
            }
        }
    }

    // Combine all boxes between iLow and iHigh into one box
    /**
     * DOCUMENT ME!
     *
     * @param iLow DOCUMENT ME!
     * @param iHigh DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BlackBox combineSegment(int iLow, int iHigh) {
        BlackBox aa = new BlackBox(); // Black Box to be returned
        int nBoxSeg = iHigh - iLow + 1; // number of boxes in segment
        int[] numDeg = new int[nBoxSeg]; // array of numerator degrees
        int[] denDeg = new int[nBoxSeg]; // array of denominator degrees
        BlackBox bb = (BlackBox) openPath.get(iLow);

        if (!bb.padeAdded) {
            bb.transferPolesZeros();
        }

        aa.sNumerPade = (ComplexPolynomial) bb.sNumerPade.clone();
        aa.sDenomPade = (ComplexPolynomial) bb.sDenomPade.clone();
        aa.deadTime = bb.deadTime;
        numDeg[0] = bb.sNumerDegPade;
        denDeg[0] = bb.sDenomDegPade;
        aa.sNumerDegPade = numDeg[0];
        aa.sDenomDegPade = denDeg[0];

        for (int i = 1; i < nBoxSeg; i++) {
            bb = (BlackBox) openPath.get(i + iLow);

            if (!bb.padeAdded) {
                bb.transferPolesZeros();
            }

            if (aa.sNumerPade == null) {
                if (bb.sNumerPade != null) {
                    aa.sNumerPade = (ComplexPolynomial) bb.sNumerPade.clone();
                }
            } else {
                if (bb.sNumerPade != null) {
                    aa.sNumerPade = (ComplexPolynomial) bb.sNumerPade.multiply(aa.sNumerPade);
                }
            }

            if (aa.sDenomPade == null) {
                if (bb.sDenomPade != null) {
                    aa.sDenomPade = (ComplexPolynomial) bb.sDenomPade.clone();
                }
            } else {
                if (bb.sDenomPade != null) {
                    aa.sDenomPade = (ComplexPolynomial) bb.sDenomPade.multiply(aa.sDenomPade);
                }
            }

            aa.deadTime += bb.deadTime;
            numDeg[i] = bb.sNumerDegPade;
            denDeg[i] = bb.sDenomDegPade;
            aa.sNumerDegPade += numDeg[i];
            aa.sDenomDegPade += denDeg[i];
        }

        if (aa.sNumerDegPade > 0) {
            aa.sZerosPade = oneDarray(aa.sNumerDegPade);

            int numK = 0;
            int denK = 0;

            for (int i = 0; i < nBoxSeg; i++) {
                bb = (BlackBox) openPath.get(i + iLow);

                if (bb.sNumerDegPade > 0) {
                    for (int j = 0; j < numDeg[i]; j++) {
                        aa.sZerosPade[numK] = (Complex) bb.sZerosPade[j].clone();
                        numK++;
                    }
                }
            }
        }

        if (aa.sNumerDegPade > 0) {
            aa.sPolesPade = oneDarray(aa.sDenomDegPade);

            int numK = 0;
            int denK = 0;

            for (int i = 0; i < nBoxSeg; i++) {
                bb = (BlackBox) openPath.get(i + iLow);

                if (bb.sNumerDegPade > 0) {
                    for (int j = 0; j < denDeg[i]; j++) {
                        aa.sPolesPade[denK] = (Complex) bb.sPolesPade[j].clone();
                        denK++;
                    }
                }
            }
        }

        aa.zeroPoleCancellation();
        aa.padeAdded = true;
        aa.sNumerDeg = aa.sNumerDegPade;
        aa.sDenomDeg = aa.sDenomDegPade;
        aa.sNumer = (ComplexPolynomial) aa.sNumerPade.clone();
        aa.sNumer = (ComplexPolynomial) aa.sNumerPade.clone();
        aa.sZeros = ArrayMathUtils.copy(aa.sZerosPade);
        aa.sPoles = ArrayMathUtils.copy(aa.sPolesPade);

        return aa;
    }

    // Return number of boxes in path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfBoxes() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.nBoxes;
    }

    // Return segment Vector
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getSegmentsVector() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.segments;
    }

    // Return number of segments in path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfSegments() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        return this.nSeg;
    }

    // Return name of all boxes in path
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNamesOfBoxes() {
        if (!checkConsolidate) {
            this.consolidate();
        }

        String names = "";

        for (int i = 0; i < this.nBoxes; i++) {
            BlackBox bb = (BlackBox) openPath.elementAt(i);
            names = names + i + ": " + bb.getName() + "   ";
        }

        return names;
    }

    // Remove all boxes from the path
    /**
     * DOCUMENT ME!
     */
    public void removeAllBoxes() {
        // Empty openPath Vector
        if (!openPath.isEmpty()) {
            openPath.removeAllElements();
        }

        // Empty segments Vector
        if (!segments.isEmpty()) {
            segments.removeAllElements();
        }

        this.nSeg = 0;
        this.checkNoMix = true;
        this.checkPath = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public OpenLoop copy() {
        OpenLoop op = new OpenLoop();

        if (this == null) {
            return null;
        } else {
            for (int i = 0; i < this.nBoxes; i++) {
                op.openPath.addElement((BlackBox) this.openPath.elementAt(i));
            }

            if (this.checkConsolidate) {
                Integer holdI = null;
                String holdS = "";
                int j = 0;

                for (int i = 0; i < this.nSeg; i++) {
                    holdI = (Integer) this.segments.elementAt(j);
                    op.segments.addElement(holdI);
                    j++;
                    holdI = (Integer) this.segments.elementAt(j);
                    op.segments.addElement(holdI);
                    j++;
                    holdS = (String) this.segments.elementAt(j);
                    op.segments.addElement(holdS);
                    j++;
                    op.segments.addElement((BlackBox) this.segments.elementAt(j));
                    j++;
                }
            }

            op.nBoxes = this.nBoxes;
            op.nSeg = this.nSeg;
            op.checkPath = this.checkPath;
            op.checkNoMix = this.checkNoMix;
            op.checkConsolidate = this.checkConsolidate;
            op.name = this.name;
            op.fixedName = this.fixedName;
            op.sNumer = (ComplexPolynomial) this.sNumer.clone();
            op.sDenom = (ComplexPolynomial) this.sDenom.clone();
            op.sNumerDeg = this.sNumerDeg;
            op.sDenomDeg = this.sDenomDeg;
            op.deadTime = this.deadTime;
            op.orderPade = this.orderPade;
            op.sNumerPade = this.sNumerPade;
            op.sDenomPade = this.sDenomPade;
            op.sNumerDegPade = this.sNumerDegPade;
            op.sDenomDegPade = this.sDenomDegPade;
            op.sPoles = ArrayMathUtils.copy(this.sPoles);
            op.sZeros = ArrayMathUtils.copy(this.sZeros);
            op.sPolesPade = ArrayMathUtils.copy(this.sPolesPade);
            op.sZerosPade = ArrayMathUtils.copy(this.sZerosPade);

            return op;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Complex[] oneDarray(int n) {
        Complex[] a = new Complex[n];

        for (int i = 0; i < n; i++) {
            a[i] = Complex.ZERO;
        }

        return a;
    }
}
