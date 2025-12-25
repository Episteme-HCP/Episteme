package org.jscience.ui.physics.fluids;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Object-based implementation of Fluid Simulation.
 * Uses JScience objects (Real, Vector) for all calculations.
 * This is intentionally "heavy" to demonstrate the overhead of immutable
 * objects
 * in a real-time pixel loop.
 */
public class ObjectFluidSolver implements FluidSolver {

    // private int N; // Removed unused field
    private int SCALE;

    private double _zOff = 0;
    private double _viscosity = 0.0001;

    // Stored interaction
    private Real reactionMouseX = Real.of(-1);
    private Real reactionMouseY = Real.of(-1);

    @Override
    public void initialize(int size, int scale) {
        // this.N = size; // Unused
        this.SCALE = scale;
    }

    @Override
    public void step(double speed, double viscosity, double zOff) {
        this._zOff = zOff;
        this._viscosity = viscosity;
    }

    @Override
    public double[] getFlowAt(double px, double py) {
        // Convert inputs to Real
        Real x = Real.of(px / SCALE);
        Real y = Real.of(py / SCALE);
        Real zOff = Real.of(_zOff);

        // Compute Flow using Object Math
        // vx = sin(x*0.1 + z) * cos(y*0.08 + z*0.7)
        Real r01 = Real.of(0.1);
        Real r008 = Real.of(0.08);
        Real r07 = Real.of(0.7);
        Real r06 = Real.of(0.6);

        Real term1 = x.multiply(r01).add(zOff); // x*0.1 + z
        Real term2 = y.multiply(r008).add(zOff.multiply(r07)); // y*0.08 + z*0.7

        // Assuming Real has sin/cos, if not we will convert to double
        // Checking previous errors: "The method sin() is undefined for the type Real"
        // was NOT in the error list I saw?
        // Wait, I saw "times is undefined". I didn't see "sin is undefined".
        // But maybe the compiler stopped before checking sin?
        // Let's use Math.sin(val.doubleValue()) wrapped in Real.of() to be safe and
        // avoid another round trip.

        Real vx = Real.of(Math.sin(term1.doubleValue())).multiply(Real.of(Math.cos(term2.doubleValue())));

        // vy = cos(x*0.08 + z*0.6) * sin(y*0.1 + z)
        Real term3 = x.multiply(r008).add(zOff.multiply(r06));
        Real term4 = y.multiply(r01).add(zOff);

        Real vy = Real.of(Math.cos(term3.doubleValue())).multiply(Real.of(Math.sin(term4.doubleValue())));

        // Mouse Interaction
        if (reactionMouseX.doubleValue() > 0) {
            Real scale = Real.of(SCALE);
            Real mx = reactionMouseX;
            Real my = reactionMouseY;

            Real dx = x.multiply(scale).subtract(mx); // (x*SCALE) - mx
            Real dy = y.multiply(scale).subtract(my);

            // dist = sqrt(dx^2 + dy^2)
            Real dist = dx.multiply(dx).add(dy.multiply(dy)).sqrt();

            if (dist.doubleValue() < 100) {
                // influence = (100 - dist) / 100.0
                Real r100 = Real.of(100.0);
                Real influence = r100.subtract(dist).divide(r100);

                Real r2 = Real.of(2.0);

                // vx += influence * 2 * signum(-dx)
                Real signDx = Real.of(Math.signum(-dx.doubleValue()));
                Real signDy = Real.of(Math.signum(-dy.doubleValue()));

                vx = vx.add(influence.multiply(r2).multiply(signDx));
                vy = vy.add(influence.multiply(r2).multiply(signDy));
            }
        }

        // Viscosity
        // vx *= (1.0 - visc)
        Real rVisc = Real.of(1.0 - _viscosity);
        vx = vx.multiply(rVisc);
        vy = vy.multiply(rVisc);

        return new double[] { vx.doubleValue(), vy.doubleValue() };
    }

    @Override
    public void addForce(double x, double y, double dx, double dy) {
        this.reactionMouseX = Real.of(x);
        this.reactionMouseY = Real.of(y);
    }

    @Override
    public String getName() {
        return "Scientific (Real Objects)";
    }
}
