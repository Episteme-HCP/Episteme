package org.jscience.physics.kinematics;

import org.jscience.util.IllegalDimensionException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * The ParticleHolder class provides a way to hold set of
 * AbstractClassicleParticles that interact with each others. All particles in
 * the Set must have the same dimension.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */
public class ParticleHolder extends Object {
    /** DOCUMENT ME! */
    private final static int NONE = 0;

    //you can use both at a time
    /** DOCUMENT ME! */
    private final static int GRAVITY = 1;

    /** DOCUMENT ME! */
    private final static int ELECTROSTATIC = 2; //and magnetic

    //may be we could add support for WEAK and STRONG forces kineatics
    //TODO, any equation ?
    /** DOCUMENT ME! */
    private Set particles;

    /** DOCUMENT ME! */
    private int kind;

/**
     * Constructs an Interaction.
     */
    public ParticleHolder() {
        particles = Collections.EMPTY_SET;
        kind = NONE;
    }

/**
     * Creates a new ParticleHolder object.
     *
     * @param particles DOCUMENT ME!
     * @throws IllegalDimensionException DOCUMENT ME!
     * @throws IllegalArgumentException  DOCUMENT ME!
     */
    public ParticleHolder(Set particles) {
        Iterator iterator;
        boolean valid;
        int dimension;

        if (particles != null) {
            iterator = particles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof AbstractClassicalParticle;
            }

            if (valid) {
                valid = true;

                if (particles.size() > 1) {
                    iterator = particles.iterator();
                    dimension = ((AbstractClassicalParticle) iterator.next()).getDimension();

                    while (iterator.hasNext() && valid) {
                        valid = (((AbstractClassicalParticle) iterator.next()).getDimension() == dimension);
                    }
                }

                if (valid) {
                    this.particles = particles;
                    kind = NONE;
                } else {
                    throw new IllegalDimensionException(
                        "All particles must have the same dimension.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Set of particles should contain only Particles.");
            }
        } else {
            throw new IllegalArgumentException(
                "Interaction doesn't accept null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumParticles() {
        return particles.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDimension() {
        if (particles.size() > 0) {
            return ((AbstractClassicalParticle) particles.iterator().next()).getDimension();
        } else {
            return -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getParticles() {
        return particles;
    }

    //particle must be of the same number of dimension
    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void addParticle(AbstractClassicalParticle p) {
        if (p != null) {
            if (getNumParticles() > 0) {
                if (p.getDimension() == getDimension()) {
                    particles.add(p);
                } else {
                    throw new IllegalDimensionException(
                        "All particles must have the same dimension.");
                }
            } else {
                particles.add(p);
            }
        } else {
            throw new IllegalArgumentException("You can't add null particle.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void removeParticle(AbstractClassicalParticle p) {
        particles.remove(p);
    }

    //each element of the set must be a particle and with the same number of dimension
    /**
     * DOCUMENT ME!
     *
     * @param particles DOCUMENT ME!
     */
    public void addParticles(Set particles) {
        Iterator iterator;
        boolean valid;

        if (particles != null) {
            iterator = particles.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof AbstractClassicalParticle;
            }

            if (valid) {
                valid = true;
                iterator = particles.iterator();

                while (iterator.hasNext() && valid) {
                    valid = (((AbstractClassicalParticle) iterator.next()).getDimension() == getDimension());
                }

                if (valid) {
                    this.particles.addAll(particles);
                } else {
                    throw new IllegalDimensionException(
                        "All particles must have the same dimension.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The Set of particles should contain only Particles.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null Set of particles.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param particles DOCUMENT ME!
     */
    public void removeParticles(Set particles) {
        this.particles.removeAll(particles);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getForcesKind() {
        return kind;
    }

    //the fields to compute (invalid values will lead to impredictible results)
    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setForcesKind(int kind) {
        this.kind = kind;
    }

    //call this just to be sure your particles don't hold extraneous useless forces
    /**
     * DOCUMENT ME!
     */
    public void removeAllForces() {
        Iterator iterator;

        iterator = particles.iterator();

        while (iterator.hasNext()) {
            ((AbstractClassicalParticle) iterator.next()).removeAllForces();
        }
    }

    //builds (n-1)*n forces for n particles
    /**
     * DOCUMENT ME!
     */
    public void buildForces() {
        HashSet copySet;
        Iterator iterator1;
        Iterator iterator2;
        AbstractClassicalParticle particle1;
        AbstractClassicalParticle particle2;

        //Gravity gravity;
        //Electrostatic electrostatic;
        copySet = new HashSet(particles);
        iterator1 = particles.iterator();

        while (iterator1.hasNext()) {
            particle1 = (AbstractClassicalParticle) iterator1.next();
            copySet.remove(particle1);
            iterator2 = copySet.iterator();

            while (iterator2.hasNext()) {
                particle2 = (AbstractClassicalParticle) iterator2.next();

                //particle1!=particle2 because of copySet
                //and
                //we make a big save in computation time here by computing the force
                //from P1 to P2 and the force from P2 to P1 only once only changing sign
                //this divides the overall computation by two but we have to find
                //when the computation has already been done
                if ((kind == GRAVITY) || (kind == (ELECTROSTATIC + GRAVITY))) {
                    particle1.addForce(new Gravity(particle1, particle2).createForce());
                    particle2.addForce(new Gravity(particle2, particle2).createForce());

                    //it would be better here if be could do
                    //gravity = new Gravity(particle1, particle2);
                    //particle1.addForce(gravity.createForce());
                    //particle2.addForce(gravity.createForce().clone().negate());
                    //but we have to update a bit the Force abstract class
                }

                if ((kind == ELECTROSTATIC) ||
                        (kind == (ELECTROSTATIC + GRAVITY))) {
                    particle1.addForce(new Electrostatic(particle1, particle2).createForce());
                    particle2.addForce(new Electrostatic(particle2, particle2).createForce());

                    //it would be better here if be could do
                    //electrostatic = new Electrostatic(particle1, particle2);
                    //particle1.addForce(electrostatic.createForce());
                    //particle2.addForce(electrostatic.createForce().clone().negate());
                    //but we have to update a bit the Force abstract class
                }
            }

            copySet.add(particle1);
        }
    }
}
