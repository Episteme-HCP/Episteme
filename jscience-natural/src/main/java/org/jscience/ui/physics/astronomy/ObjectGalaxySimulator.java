package org.jscience.ui.physics.astronomy;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;
import org.jscience.mathematics.sets.Reals;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Object-based implementation of Galaxy Physics.
 * Uses Vector<Real> for all positions and velocities.
 * Demonstrates overhead of object creation/math vs primitive doubles.
 */
public class ObjectGalaxySimulator implements GalaxySimulator {

    private List<GalaxyViewer.StarParticle> viewStars;

    // Internal Physics State
    private List<Vector<Real>> positions;
    private List<Vector<Real>> velocities;

    // Core 2 State
    private Vector<Real> g2Pos;
    private Vector<Real> g2Vel;

    @Override
    public void init(List<GalaxyViewer.StarParticle> stars) {
        this.viewStars = stars;
        this.positions = new ArrayList<>(stars.size());
        this.velocities = new ArrayList<>(stars.size());

        for (GalaxyViewer.StarParticle s : stars) {
            positions.add(createVec2(s.x, s.y));
            velocities.add(createVec2(s.vx, s.vy));
        }

        // Init G2 defaults
        g2Pos = createVec2(800, -500);
        g2Vel = createVec2(-2, 2);
    }

    private Vector<Real> createVec2(double x, double y) {
        return DenseVector.of(Arrays.asList(Real.of(x), Real.of(y)), Reals.getInstance());
    }

    @Override
    public void setGalaxy2State(double x, double y, double vx, double vy) {
        this.g2Pos = createVec2(x, y);
        this.g2Vel = createVec2(vx, vy);
    }

    @Override
    public void update(boolean collisionMode, double extG2x, double extG2y) {
        if (collisionMode) {
            // 1. Update Cores
            Real rx = g2Pos.get(0);
            Real ry = g2Pos.get(1);

            // distSq = rx^2 + ry^2
            Real distSq = rx.multiply(rx).add(ry.multiply(ry));

            // force = 10000 / (distSq + 100)
            Real r10k = Real.of(10000.0);
            Real r100 = Real.of(100.0);
            Real force = r10k.divide(distSq.add(r100));

            Real dist = distSq.sqrt();

            // g2v -= force * pos / dist
            // accel = force / dist * pos
            Real factor = force.divide(dist);
            Vector<Real> accel = g2Pos.multiply(factor);

            g2Vel = g2Vel.subtract(accel);
            g2Pos = g2Pos.add(g2Vel);

            Real r500 = Real.of(500.0);
            Real r300 = Real.of(300.0);

            // 2. Perturb Stars
            for (int i = 0; i < positions.size(); i++) {
                Vector<Real> p = positions.get(i);
                Vector<Real> v = velocities.get(i);

                // Attractor 1 (0,0)
                Real p0 = p.get(0);
                Real p1 = p.get(1);
                Real d1Sq = p0.multiply(p0).add(p1.multiply(p1));
                Real d1 = d1Sq.sqrt();

                Real f1 = r500.divide(d1Sq.add(r100));
                v = v.subtract(p.multiply(f1.divide(d1)));

                // Attractor 2 (g2Pos)
                Vector<Real> d2Vec = p.subtract(g2Pos);
                Real d2x = d2Vec.get(0);
                Real d2y = d2Vec.get(1);
                Real d2Sq = d2x.multiply(d2x).add(d2y.multiply(d2y));
                Real d2 = d2Sq.sqrt();

                Real f2 = r300.divide(d2Sq.add(r100));
                v = v.subtract(d2Vec.multiply(f2.divide(d2)));

                p = p.add(v);

                positions.set(i, p);
                velocities.set(i, v);

                GalaxyViewer.StarParticle sp = viewStars.get(i);
                sp.x = p.get(0).doubleValue();
                sp.y = p.get(1).doubleValue();
            }

        } else {
            // Static Rotation
            Real r200 = Real.of(200.0);
            Real r01 = Real.of(0.1);
            Real r005 = Real.of(0.05);

            for (int i = 0; i < positions.size(); i++) {
                Vector<Real> p = positions.get(i);
                GalaxyViewer.StarParticle sp = viewStars.get(i);

                Real x = p.get(0);
                Real y = p.get(1);

                Real r = x.multiply(x).add(y.multiply(y)).sqrt();
                Real ang = y.atan2(x);

                Real speed = Real.of(1.0).divide(r.divide(r200).add(r01)).multiply(r005);
                ang = ang.add(speed);

                x = ang.cos().multiply(r);
                y = ang.sin().multiply(r);

                p = createVec2(x.doubleValue(), y.doubleValue());
                positions.set(i, p);

                sp.x = x.doubleValue();
                sp.y = y.doubleValue();
            }
        }
    }

    // Access methods for G2 Visualization
    public double getG2X() {
        return g2Pos.get(0).doubleValue();
    }

    public double getG2Y() {
        return g2Pos.get(1).doubleValue();
    }

    @Override
    public String getName() {
        return "Scientific (Vector<Real>)";
    }
}
