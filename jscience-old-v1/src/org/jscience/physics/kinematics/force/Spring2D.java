package org.jscience.physics.kinematics.force;

import java.util.ArrayList;

/**
 * Represents a spring in a force simulation.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class Spring2D extends Spring {
    private static SpringFactory s_factory = new SpringFactory();
    
    /**
     * Retrieve the SpringFactory instance, which serves as an object pool
     * for Spring instances.
     * @return the Spring Factory
     */
    public static SpringFactory getFactory() {
        return s_factory;
    }
    
    /**
     * Create a new Spring instance
     * @param fi1 the first ForceItem endpoint
     * @param fi2 the second ForceItem endpoint
     * @param k the spring tension co-efficient
     * @param len the spring's resting length
     */
    public Spring2D(ForceItem2D fi1, ForceItem2D fi2, float k, float len) {
        item1 = fi1;
        item2 = fi2;
        coeff = k;
        length = len;
    }
    
    /** The first ForceItem endpoint */
    public ForceItem2D item1;
    /** The second ForceItem endpoint */
    public ForceItem2D item2;

    /**
     * The SpringFactory is responsible for generating Spring instances
     * and maintaining an object pool of Springs to reduce garbage collection
     * overheads while force simulations are running.
     */
    public static final class SpringFactory {
        private int maxSprings = 10000;
        private ArrayList springs = new ArrayList();
        
        /**
         * Get a Spring instance and set it to the given parameters.
         */
        public Spring2D getSpring(ForceItem2D f1, ForceItem2D f2, float k, float length) {
            if ( springs.size() > 0 ) {
                Spring2D s = (Spring2D)springs.remove(springs.size()-1);
                s.item1 = f1;
                s.item2 = f2;
                s.coeff = k;
                s.length = length;
                return s;
            } else {
                return new Spring2D(f1,f2,k,length);
            }
        }
        /**
         * Reclaim a Spring into the object pool.
         */
        public void reclaim(Spring2D s) {
            s.item1 = null;
            s.item2 = null;
            if ( springs.size() < maxSprings )
                springs.add(s);
        }
    } // end of inner class SpringFactory

} // end of class Spring
