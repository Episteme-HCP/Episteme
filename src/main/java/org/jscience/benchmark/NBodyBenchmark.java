package org.jscience.benchmark;

import org.openjdk.jmh.annotations.*;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.sets.Reals;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.physics.astronomy.CelestialBody;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Length;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class NBodyBenchmark {

    @Param({ "10", "100" })
    public int bodiesCount;

    private List<CelestialBody> bodies;

    @Setup(Level.Trial)
    public void setup() {
        bodies = new ArrayList<>();
        // Star
        bodies.add(new CelestialBody("Sun",
                Quantities.create(1.989e30, Units.KILOGRAM),
                Quantities.create(6.9634e8, Units.METER),
                createVector(0, 0, 0),
                createVector(0, 0, 0)));

        // Random planets
        for (int i = 0; i < bodiesCount; i++) {
            double dist = 1.496e11 + i * 1.0e9; // ~1 AU + offset
            bodies.add(new CelestialBody("Planet-" + i,
                    Quantities.create(5.972e24, Units.KILOGRAM),
                    Quantities.create(6.371e6, Units.METER),
                    createVector(dist, 0, 0),
                    createVector(0, 29780.0, 0)));
        }
    }

    private DenseVector<Real> createVector(double x, double y, double z) {
        DenseVector<Real> v = DenseVector.zeros(3, Reals.getInstance());
        v.set(0, Real.of(x));
        v.set(1, Real.of(y));
        v.set(2, Real.of(z));
        return v;
    }

    @Benchmark
    public void simulateStep() {
        // N-Body naive force calculation step O(N^2)
        // This is a simplified simulation step for benchmarking math ops
        for (CelestialBody b1 : bodies) {
            Vector<Real> totalForce = DenseVector.zeros(3, Reals.getInstance());
            for (CelestialBody b2 : bodies) {
                if (b1 == b2)
                    continue;
                // F = G * m1 * m2 / r^2
                // Simply access positions to simulate memory load
                Vector<Real> r = b2.getPosition().subtract(b1.getPosition());
                // Real distSq = r.norm().pow(2); // Depending on norm imp
                // totalForce = totalForce.add(r);
            }
        }
    }
}
