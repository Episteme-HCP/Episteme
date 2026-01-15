package org.jscience.physics.kinematics.force;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * <p>Force function which computes an n-body force such as gravity,
 * anti-gravity, or the results of electric charges. This function implements
 * the the Barnes-Hut algorithm for efficient n-body force simulations,
 * using a quad-tree with aggregated mass values to compute the n-body
 * force in O(N log N) time, where N is the number of ForceItems.</p>
 * 
 * <p>The algorithm used is that of J. Barnes and P. Hut, in their research
 * paper <i>A Hierarchical  O(n log n) force calculation algorithm</i>, Nature, 
 *  v.324, December 1986. For more details on the algorithm, see one of
 *  the following links --
 * <ul>
 *   <li><a href="http://www.cs.berkeley.edu/~demmel/cs267/lecture26/lecture26.html">James Demmel's UC Berkeley lecture notes</a>
 *   <li><a href="http://www.physics.gmu.edu/~large/lr_forces/desc/bh/bhdesc.html">Description of the Barnes-Hut algorithm</a>
 *   <li><a href="http://www.ifa.hawaii.edu/~barnes/treecode/treeguide.html">Joshua Barnes' recent implementation</a>
 * </ul></p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class NBodyForce extends AbstractForce {

    /* 
     * The indexing scheme for octtree child nodes goes row by row.
     * upper layer
     *   0 | 1    0 -> top left,    1 -> top right
     *  -------
     *   2 | 3    2 -> bottom left, 3 -> bottom right
     *   lower layer
     *   4 | 5    0 -> top left,    1 -> top right
     *  -------
     *   6 | 7    2 -> bottom left, 3 -> bottom right
     */

    private static String[] pnames = new String[] { "GravitationalConstant", 
            "Distance", "BarnesHutTheta"  };
    
    public static final float DEFAULT_GRAV_CONSTANT = -1.0f;
    public static final float DEFAULT_MIN_GRAV_CONSTANT = -10f;
    public static final float DEFAULT_MAX_GRAV_CONSTANT = 10f;
    
    public static final float DEFAULT_DISTANCE = -1f;
    public static final float DEFAULT_MIN_DISTANCE = -1f;
    public static final float DEFAULT_MAX_DISTANCE = 500f;
    
    public static final float DEFAULT_THETA = 0.9f;
    public static final float DEFAULT_MIN_THETA = 0.0f;
    public static final float DEFAULT_MAX_THETA = 1.0f;
    
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int MIN_DISTANCE = 1;
    public static final int BARNES_HUT_THETA = 2;
    
    private float xMin, xMax, yMin, yMax, zMin, zMax;
    private OctTreeNodeFactory factory = new OctTreeNodeFactory();
    private OctTreeNode root;
    
    private Random rand = new Random(12345678L); // deterministic randomness

    /**
     * Create a new NBodyForce with default parameters.
     */
    public NBodyForce() {
        this(DEFAULT_GRAV_CONSTANT, DEFAULT_DISTANCE, DEFAULT_THETA);
    }
    
    /**
     * Create a new NBodyForce.
     * @param gravConstant the gravitational constant to use. Nodes will
     * attract each other if this value is positive, and will repel each
     * other if it is negative.
     * @param minDistance the distance within which two particles will
     * interact. If -1, the value is treated as infinite.
     * @param theta the Barnes-Hut parameter theta, which controls when
     * an aggregated mass is used rather than drilling down to individual
     * item mass values.
     */
    public NBodyForce(float gravConstant, float minDistance, float theta) {
        params = new float[] { gravConstant, minDistance, theta };
        minValues = new float[] { DEFAULT_MIN_GRAV_CONSTANT,
            DEFAULT_MIN_DISTANCE, DEFAULT_MIN_THETA };
        maxValues = new float[] { DEFAULT_MAX_GRAV_CONSTANT,
            DEFAULT_MAX_DISTANCE, DEFAULT_MAX_THETA };
        root = factory.getOctTreeNode();
    }

    /**
     * Returns true.
     * @see org.jscience.physics.kinematics.force.Force#isItemForce()
     */
    public boolean isItemForce() {
        return true;
    }
    
    /**
     * @see org.jscience.physics.kinematics.force.AbstractForce#getParameterNames()
     */
    protected String[] getParameterNames() {
        return pnames;
    } 
    
    /**
     * Set the bounds of the region for which to compute the n-body simulation
     * @param xMin the minimum x-coordinate
     * @param yMin the minimum y-coordinate
     * @param zMin the minimum y-coordinate
     * @param xMax the maximum x-coordinate
     * @param yMax the maximum y-coordinate
     * @param zMax the maximum y-coordinate
     */
    private void setBounds(float xMin, float yMin, float zMin, float xMax, float yMax, float zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    /**
     * Clears the octtree of all entries.
     */
    public void clear() {
        clearHelper(root);
        root = factory.getOctTreeNode();
    }
    
    private void clearHelper(OctTreeNode n) {
        for ( int i = 0; i < n.children.length; i++ ) {
            if ( n.children[i] != null )
                clearHelper(n.children[i]);
        }
        factory.reclaim(n);
    }

    /**
     * Initialize the simulation with the provided enclosing simulation. After
     * this call has been made, the simulation can be queried for the 
     * n-body force acting on a given item.
     * @param fsim the enclosing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        clear(); // clear internal state
        
        // compute and squarify bounds of octtree
        float x1 = Float.MAX_VALUE, y1 = Float.MAX_VALUE, z1 = Float.MAX_VALUE;
        float x2 = Float.MIN_VALUE, y2 = Float.MIN_VALUE, z2 = Float.MIN_VALUE;
        Iterator itemIter = fsim.getItems();
        while ( itemIter.hasNext() ) {
            ForceItem item = (ForceItem)itemIter.next();
            float x = item.location[0];
            float y = item.location[1];
            float z = item.location[2];
            if ( x < x1 ) x1 = x;
            if ( y < y1 ) y1 = y;
            if ( z < z1 ) z1 = z;
            if ( x > x2 ) x2 = x;
            if ( y > y2 ) y2 = y;
            if ( z > z2 ) z2 = z;
        }
        float dx = x2-x1, dy = y2-y1, dz = z2-z1;

        // check lines below
       if ( dx > dy ) {
           if ( dz > dx ) {
               x2 = x1 + dz;
               y2 = y1 + dz;
              } else {
               y2 = y1 + dx;
               z2 = z1 + dx;
              }
       } else {
           if ( dz > dy ) {
               x2 = x1 + dz;
               y2 = y1 + dz;
             } else {
               x2 = x1 + dy;
               z2 = z1 + dy;
             }
       }

        setBounds(x1,y1,z1,x2,y2,z2);
        
        // insert items into octtree
        itemIter = fsim.getItems();
        while ( itemIter.hasNext() ) {
            ForceItem item = (ForceItem)itemIter.next();
            insert(item);
        }
        
        // calculate magnitudes and centers of mass
        calcMass(root);
    }

    /**
     * Inserts an item into the octtree.
     * @param item the ForceItem to add.
     * @throws IllegalStateException if the current location of the item is
     *  outside the bounds of the quadtree
     */
    public void insert(ForceItem item) {
        // insert item into the octtrees
        try {
            insert(item, root, xMin, yMin, zMin, xMax, yMax, zMax);
        } catch ( StackOverflowError e ) {
            // TODO: safe to remove?
            e.printStackTrace();
        }
    }

    private void insert(ForceItem p, OctTreeNode n, 
                        float x1, float y1, float z1, float x2, float y2, float z2)
    {
        // try to insert particle p at node n in the quadtree
        // by construction, each leaf will contain either 1 or 0 particles
        if ( n.hasChildren ) {
            // n contains more than 1 particle
            insertHelper(p,n,x1,y1,z1,x2,y2,z2);
        } else if ( n.value != null ) {
            // n contains 1 particle
            if ( isSameLocation(n.value, p) ) {
                insertHelper(p,n,x1,y1,z1,x2,y2,z2);
            } else {
                ForceItem v = n.value; n.value = null;
                insertHelper(v,n,x1,y1,z1,x2,y2,z2);
                insertHelper(p,n,x1,y1,z1,x2,y2,z2);
            }
        } else { 
            // n is empty, so is a leaf
            n.value = p;
        }
    }
    
    private static boolean isSameLocation(ForceItem f1, ForceItem f2) {
        float dx = Math.abs(f1.location[0]-f2.location[0]);
        float dy = Math.abs(f1.location[1]-f2.location[1]);
        float dz = Math.abs(f1.location[2]-f2.location[2]);
        return ( dx < 0.01 && dy < 0.01 && dz < 0.01);
    }
    
    private void insertHelper(ForceItem p, OctTreeNode n, 
                              float x1, float y1, float z1, float x2, float y2, float z2)
    {   
        float x = p.location[0], y = p.location[1], z = p.location[2];
        float splitx = (x1+x2)/2;
        float splity = (y1+y2)/2;
        float splitz = (z1+z2)/2;
        int i = (x>=splitx ? 1 : 0) + (y>=splity ? 2 : 0)+ (z>=splity ? 4 : 0);
        // create new child node, if necessary
        if ( n.children[i] == null ) {
            n.children[i] = factory.getOctTreeNode();
            n.hasChildren = true;
        }
        // update bounds
        if ( i==1 || i==3 || i==5 || i==7 ) x1 = splitx; else x2 = splitx;
        if ( i==2 || i==3 || i==6 || i==7 ) y1 = splity; else y2 = splity;
        if ( i>3 ) z1 = splitz; else z2 = splitz;
         // recurse
        insert(p,n.children[i],x1,y1,z1,x2,y2,z2);        
    }

    private void calcMass(OctTreeNode n) {
        float xcom = 0, ycom = 0, zcom = 0;
        n.mass = 0;
        if ( n.hasChildren ) {
            for ( int i=0; i < n.children.length; i++ ) {
                if ( n.children[i] != null ) {
                    calcMass(n.children[i]);
                    n.mass += n.children[i].mass;
                    xcom += n.children[i].mass * n.children[i].com[0];
                    ycom += n.children[i].mass * n.children[i].com[1];
                    zcom += n.children[i].mass * n.children[i].com[2];
                }
            }
        }
        if ( n.value != null ) {
            n.mass += n.value.mass;
            xcom += n.value.mass * n.value.location[0];
            ycom += n.value.mass * n.value.location[1];
            zcom += n.value.mass * n.value.location[2];
        }
        n.com[0] = xcom / n.mass;
        n.com[1] = ycom / n.mass;
        n.com[2] = ycom / n.mass;
    }

    /**
     * Calculates the force vector acting on the given item.
     * @param item the ForceItem for which to compute the force
     */
    public void getForce(ForceItem item) {
        try {
            forceHelper(item,root,xMin,yMin,zMin,xMax,yMax,zMax);
        } catch ( StackOverflowError e ) {
            // TODO: safe to remove?
            e.printStackTrace();
        }
    }
    
    private void forceHelper(ForceItem item, OctTreeNode n, 
                             float x1, float y1, float z1, float x2, float y2, float z2)
    {
        float dx = n.com[0] - item.location[0];
        float dy = n.com[1] - item.location[1];
        float dz = n.com[2] - item.location[2];
        float r  = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
        boolean same = false;
        if ( r == 0.0f ) {
            // if items are in the exact same place, add some noise
            dx = (rand.nextFloat()-0.5f) / 50.0f;
            dy = (rand.nextFloat()-0.5f) / 50.0f;
            dz = (rand.nextFloat()-0.5f) / 50.0f;
            r  = (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
            same = true;
        }
        boolean minDist = params[MIN_DISTANCE]>0f && r>params[MIN_DISTANCE];
        
        // the Barnes-Hut approximation criteria is if the ratio of the
        // size of the octtree box to the distance between the point and
        // the box's center of mass is beneath some threshold theta.
        if ( (!n.hasChildren && n.value != item) || 
             (!same && (x2-x1)/r < params[BARNES_HUT_THETA]) ) 
        {
            if ( minDist ) return;
            // either only 1 particle or we meet criteria
            // for Barnes-Hut approximation, so calc force
            float v = params[GRAVITATIONAL_CONST]*item.mass*n.mass 
                        / (r*r*r);
            item.force[0] += v*dx;
            item.force[1] += v*dy;
            item.force[2] += v*dz;
        } else if ( n.hasChildren ) {
            // recurse for more accurate calculation
            float splitx = (x1+x2)/2;
            float splity = (y1+y2)/2;
            float splitz = (z1+z2)/2;
            for ( int i=0; i<n.children.length; i++ ) {
                if ( n.children[i] != null ) {
                    forceHelper(item, n.children[i],
                        (i==1||i==3||i==5||i==7?splitx:x1), (i==1||i==3||i==5||i==7?splity:y1), (i>3?splitz:z1),
                        (i==1||i==3||i==5||i==7?splitx:x2), (i==1||i==3||i==5||i==7?splity:y2), (i>3?splitz:z2));
                   }
            }
            if ( minDist ) return;
            if ( n.value != null && n.value != item ) {
                float v = params[GRAVITATIONAL_CONST]*item.mass*n.value.mass
                            / (r*r*r);
                item.force[0] += v*dx;
                item.force[1] += v*dy;
                item.force[2] += v*dz;
            }
        }
    }

    /**
     * Represents a node in the octtree.
     */
    public static final class OctTreeNode {
        public OctTreeNode() {
            com = new float[] {0.0f, 0.0f, 0.0f};
            children = new OctTreeNode[8];
        } //
        boolean hasChildren = false;
        float mass; // total mass held by this node
        float[] com; // center of mass of this node 
        ForceItem value; // ForceItem in this node, null if node has children
        OctTreeNode[] children; // children nodes
    } // end of inner class OctTreeNode

    /**
     * Helper class to minimize number of object creations across multiple
     * uses of the octtree.
     */
    public static final class OctTreeNodeFactory {
        private int maxNodes = 50000;
        private ArrayList nodes = new ArrayList();
        
        public OctTreeNode getOctTreeNode() {
            if ( nodes.size() > 0 ) {
                return (OctTreeNode)nodes.remove(nodes.size()-1);
            } else {
                return new OctTreeNode();
            }
        }
        public void reclaim(OctTreeNode n) {
            n.mass = 0;
            n.com[0] = 0.0f; n.com[1] = 0.0f; n.com[2] = 0.0f;
            n.value = null;
            n.hasChildren = false;
            Arrays.fill(n.children, null);          
            if ( nodes.size() < maxNodes )
                nodes.add(n);
        }
    } // end of inner class OctTreeNodeFactory

} // end of class NBodyForce
