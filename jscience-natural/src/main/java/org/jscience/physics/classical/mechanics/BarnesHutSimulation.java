package org.jscience.physics.classical.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.Vector;
import org.jscience.physics.PhysicalConstants;

/**
 * Barnes-Hut octree-based N-body simulation O(n log n).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class BarnesHutSimulation {

    private final List<Particle> particles = new ArrayList<>();
    private Real G = PhysicalConstants.G;
    private Real theta = Real.of(0.5);
    private Real softening = Real.of(0.01);
    private OctreeNode root;

    public BarnesHutSimulation() {
    }

    public BarnesHutSimulation(Real G, Real theta) {
        this.G = G;
        this.theta = theta;
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setTheta(Real t) {
        theta = t;
    }

    public void setSoftening(Real s) {
        softening = s;
    }

    public void buildTree() {
        if (particles.isEmpty())
            return;

        // Find bounds
        Particle p0 = particles.get(0);
        Real minX = p0.getX(), minY = p0.getY(), minZ = p0.getZ();
        Real maxX = minX, maxY = minY, maxZ = minZ;

        for (Particle p : particles) {

            Real x = p.getX();
            Real y = p.getY();
            Real z = p.getZ();
            if (x.compareTo(minX) < 0)
                minX = x;
            if (y.compareTo(minY) < 0)
                minY = y;
            if (z.compareTo(minZ) < 0)
                minZ = z;
            if (x.compareTo(maxX) > 0)
                maxX = x;
            if (y.compareTo(maxY) > 0)
                maxY = y;
            if (z.compareTo(maxZ) > 0)
                maxZ = z;
        }

        Real size = Real.ZERO;
        size = size.max(maxX.subtract(minX));
        size = size.max(maxY.subtract(minY));
        size = size.max(maxZ.subtract(minZ));

        Real cx = maxX.add(minX).divide(Real.of(2));
        Real cy = maxY.add(minY).divide(Real.of(2));
        Real cz = maxZ.add(minZ).divide(Real.of(2));

        root = new OctreeNode(cx, cy, cz, size.multiply(Real.of(1.1)));
        for (Particle p : particles)
            root.insert(p);
        root.computeCenterOfMass();
    }

    public void computeForces() {
        buildTree();
        for (Particle p : particles) {
            p.setAcceleration(Real.ZERO, Real.ZERO, Real.ZERO);
            if (root != null)
                computeForce(p, root);
        }
    }

    private void computeForce(Particle p, OctreeNode node) {
        if (node.mass.equals(Real.ZERO) || node.particle == p)
            return;

        Real dx = node.comX.subtract(p.getX());
        Real dy = node.comY.subtract(p.getY());
        Real dz = node.comZ.subtract(p.getZ());

        Real r2 = dx.pow(2).add(dy.pow(2)).add(dz.pow(2)).add(softening.pow(2));
        Real r = r2.sqrt();

        // theta check: s / r < theta
        if (node.isLeaf() || (node.size.divide(r).compareTo(theta) < 0)) {
            Real r3 = r2.multiply(r);
            Real factor = G.multiply(node.mass).divide(r3);

            // acc += factor * d
            // But Vector is likely immutable or we need helper.
            // Let's create vector from d
            // Or use getAcceleration().add(...)
            // However, p.getAcceleration is already a Vector.
            // We need to construct vector (factor*dx, factor*dy, factor*dz)

            // Assuming we use setAcceleration:
            Real ax = p.getAcceleration().get(0).add(factor.multiply(dx));
            Real ay = p.getAcceleration().get(1).add(factor.multiply(dy));
            Real az = (p.getAcceleration().dimension() > 2 ? p.getAcceleration().get(2) : Real.ZERO)
                    .add(factor.multiply(dz));

            p.setAcceleration(ax, ay, az);
        } else {
            for (OctreeNode c : node.children)
                if (c != null)
                    computeForce(p, c);
        }
    }

    public void step(Real dt) {
        for (Particle p : particles) {
            Vector<Real> v = p.getVelocity();
            Vector<Real> a = p.getAcceleration();
            p.setVelocity(v.add(a.multiply(dt.multiply(Real.of(0.5)))));
        }
        for (Particle p : particles)
            p.updatePosition(dt);
        computeForces();
        for (Particle p : particles) {
            Vector<Real> v = p.getVelocity();
            Vector<Real> a = p.getAcceleration();
            p.setVelocity(v.add(a.multiply(dt.multiply(Real.of(0.5)))));
        }
    }

    public void run(Real dt, int steps) {
        computeForces();
        for (int i = 0; i < steps; i++)
            step(dt);
    }

    private static class OctreeNode {
        Real centerX, centerY, centerZ;
        Real size;
        Real mass;
        Real comX, comY, comZ;
        Particle particle;
        OctreeNode[] children = new OctreeNode[8];

        OctreeNode(Real x, Real y, Real z, Real s) {
            centerX = x;
            centerY = y;
            centerZ = z;
            size = s;
            mass = Real.ZERO;
            comX = Real.ZERO;
            comY = Real.ZERO;
            comZ = Real.ZERO;
        }

        boolean isLeaf() {
            for (var ch : children)
                if (ch != null)
                    return false;
            return true;
        }

        int octant(Particle p) {
            int o = 0;
            if (p.getX().compareTo(centerX) > 0)
                o |= 1;
            if (p.getY().compareTo(centerY) > 0)
                o |= 2;
            if (p.getZ().compareTo(centerZ) > 0)
                o |= 4;
            return o;
        }

        void insert(Particle p) {
            if (mass.equals(Real.ZERO) && particle == null) {
                particle = p;
                mass = Real.of(p.getMass().getValue().doubleValue());
                comX = p.getX();
                comY = p.getY();
                comZ = p.getZ();
            } else if (particle != null) {
                Particle e = particle;
                particle = null;
                insertChild(e);
                insertChild(p);
            } else
                insertChild(p);
        }

        void insertChild(Particle p) {
            int o = octant(p);
            if (children[o] == null) {
                Real h = size.divide(Real.of(2));
                Real offset = h.divide(Real.of(2));
                Real nx = centerX.add(((o & 1) != 0) ? offset : offset.negate());
                Real ny = centerY.add(((o & 2) != 0) ? offset : offset.negate());
                Real nz = centerZ.add(((o & 4) != 0) ? offset : offset.negate());
                children[o] = new OctreeNode(nx, ny, nz, h);
            }
            children[o].insert(p);
        }

        void computeCenterOfMass() {
            if (particle != null) {
                mass = Real.of(particle.getMass().getValue().doubleValue());
                comX = particle.getX();
                comY = particle.getY();
                comZ = particle.getZ();
            } else {
                mass = Real.ZERO;
                comX = Real.ZERO;
                comY = Real.ZERO;
                comZ = Real.ZERO;
                for (var ch : children)
                    if (ch != null) {
                        ch.computeCenterOfMass();
                        mass = mass.add(ch.mass);
                        comX = comX.add(ch.mass.multiply(ch.comX));
                        comY = comY.add(ch.mass.multiply(ch.comY));
                        comZ = comZ.add(ch.mass.multiply(ch.comZ));
                    }
                if (mass.compareTo(Real.ZERO) > 0) {
                    comX = comX.divide(mass);
                    comY = comY.divide(mass);
                    comZ = comZ.divide(mass);
                }
            }
        }
    }

}
