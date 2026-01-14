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

package org.jscience.mathematics.mandelbrot;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.context.MathContext;
import java.math.RoundingMode;

/**
 * High-precision implementation of Mandelbrot set calculation using Real
 * numbers.
 */
public class RealMandelbrotTask extends MandelbrotTask {

    private Real rXMin, rXMax, rYMin, rYMax;
    private int precisionDigits = 100;

    public RealMandelbrotTask() {
        super();
        this.rXMin = Real.ZERO;
        this.rXMax = Real.ZERO;
        this.rYMin = Real.ZERO;
        this.rYMax = Real.ZERO;
    }

    public RealMandelbrotTask(int width, int height, Real xMin, Real xMax, Real yMin, Real yMax, int precision) {
        super(width, height, xMin.doubleValue(), xMax.doubleValue(), yMin.doubleValue(), yMax.doubleValue());
        this.rXMin = xMin;
        this.rXMax = xMax;
        this.rYMin = yMin;
        this.rYMax = yMax;
        this.precisionDigits = precision;
    }

    @Override
    public String getTaskType() {
        return "MANDELBROT_REAL";
    }

    @Override
    public Class<MandelbrotTask> getInputType() {
        // We technically accept MandelbrotTask (superclass) but prefer
        // RealMandelbrotTask
        return MandelbrotTask.class;
    }

    // We reuse MandelbrotTask execute signature
    @Override
    public MandelbrotTask execute(MandelbrotTask input) {
        if (input instanceof RealMandelbrotTask) {
            RealMandelbrotTask realInput = (RealMandelbrotTask) input;
            if (realInput.width > 0) {
                realInput.compute();
                return realInput;
            }
        }
        // Fallback to super execution if input is primitive or local execution
        if (this.width > 0) {
            this.compute();
            return this;
        }
        return super.execute(input);
    }

    @Override
    public void compute() {
        // Set context for high precision if needed, though provider handles core logic
        java.math.MathContext jmc = new java.math.MathContext(precisionDigits, RoundingMode.HALF_UP);
        MathContext mc = MathContext.exact().withJavaMathContext(jmc);

        mc.compute(() -> {
            org.jscience.technical.backend.algorithms.MandelbrotProvider provider = new org.jscience.technical.backend.algorithms.MulticoreMandelbrotProvider();

            this.result = provider.computeReal(rXMin, rXMax, rYMin, rYMax, width, height, maxIterations);
            return null;
        });
    }

    // Getters/Setters for Real params if needed
    public void setRealRegion(Real xMin, Real xMax, Real yMin, Real yMax) {
        this.rXMin = xMin;
        this.rXMax = xMax;
        this.rYMin = yMin;
        this.rYMax = yMax;
        // Update doubles for display preview
        this.xMin = xMin.doubleValue();
        this.xMax = xMax.doubleValue();
        this.yMin = yMin.doubleValue();
        this.yMax = yMax.doubleValue();
    }
}
