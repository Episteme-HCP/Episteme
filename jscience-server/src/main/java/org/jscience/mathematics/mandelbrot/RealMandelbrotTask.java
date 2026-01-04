package org.jscience.mathematics.mandelbrot;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.context.MathContext;
import java.math.RoundingMode;

/**
 * High-precision implementation of Mandelbrot set calculation using Real numbers.
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
        // We technically accept MandelbrotTask (superclass) but prefer RealMandelbrotTask
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
        // Set context for high precision
        java.math.MathContext jmc = new java.math.MathContext(precisionDigits, RoundingMode.HALF_UP);
        MathContext mc = MathContext.exact().withJavaMathContext(jmc);

        mc.compute(() -> {
            Real dx = rXMax.subtract(rXMin).divide(Real.of(width));
            Real dy = rYMax.subtract(rYMin).divide(Real.of(height));
            Real four = Real.of(4.0);
            Real two = Real.of(2.0);

            for (int px = 0; px < width; px++) {
               Real x0 = rXMin.add(dx.multiply(Real.of(px)));
               for (int py = 0; py < height; py++) {
                   Real y0 = rYMin.add(dy.multiply(Real.of(py)));
                   Real x = Real.ZERO;
                   Real y = Real.ZERO;
                   int iter = 0;

                   while (x.multiply(x).add(y.multiply(y)).compareTo(four) <= 0 && iter < maxIterations) {
                       Real xTemp = x.multiply(x).subtract(y.multiply(y)).add(x0);
                       y = x.multiply(y).multiply(two).add(y0);
                       x = xTemp;
                       iter++;
                   }
                   result[px][py] = iter;
               }
            }
            return null;
        });
    }
    
    // Getters/Setters for Real params if needed
    public void setRealRegion(Real xMin, Real xMax, Real yMin, Real yMax) {
        this.rXMin = xMin; this.rXMax = xMax; this.rYMin = yMin; this.rYMax = yMax;
        // Update doubles for display preview
        this.xMin = xMin.doubleValue(); this.xMax = xMax.doubleValue();
        this.yMin = yMin.doubleValue(); this.yMax = yMax.doubleValue();
    }
}
