package org.jscience.physics.kinematics.force;

import java.util.Iterator;

/**
 * Updates velocity and position data using the 4th-Order Runge-Kutta method.
 * It is slower but more accurate than other techniques such as Euler's Method.
 * The technique requires re-evaluating forces 4 times for a given timestep.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
//this class is rebundled after Prefuse by Jeffrey Heer which under BSD
public class RungeKuttaIntegrator3D implements ForceIntegrator {
    
    /**
     * @see org.jscience.physics.kinematics.force.ForceIntegrator#integrate(org.jscience.physics.kinematics.force.ForceSimulator, long)
     */
    public void integrate(ForceSimulator sim, long timestep) {
        float speedLimit = sim.getSpeedLimit();
        float vx, vy, vz, v, coeff;
        float[][] k, l;
        
        Iterator iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem3D item = (ForceItem3D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            item.plocation[0] = item.location[0];
            item.plocation[1] = item.location[1];
            item.plocation[2] = item.location[2];
            k[0][0] = timestep*item.velocity[0];
            k[0][1] = timestep*item.velocity[1];
            k[0][2] = timestep*item.velocity[2];
            l[0][0] = coeff*item.force[0];
            l[0][1] = coeff*item.force[1];
            l[0][2] = coeff*item.force[2];

            // Set the position to the new predicted position
            item.location[0] += 0.5f*k[0][0];
            item.location[1] += 0.5f*k[0][1];
            item.location[2] += 0.5f*k[0][2];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem3D item = (ForceItem3D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5f*l[0][0];
            vy = item.velocity[1] + .5f*l[0][1];
            vz = item.velocity[2] + .5f*l[0][2];
            v = (float)Math.sqrt(vx*vx+vy*vy);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
                vz = speedLimit * vz / v;
            }
            k[1][0] = timestep*vx;
            k[1][1] = timestep*vy;
            k[1][2] = timestep*vz;
            l[1][0] = coeff*item.force[0];
            l[1][1] = coeff*item.force[1];
            l[1][2] = coeff*item.force[2];

            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5f*k[1][0];
            item.location[1] = item.plocation[1] + 0.5f*k[1][1];
            item.location[2] = item.plocation[2] + 0.5f*k[1][2];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem3D item = (ForceItem3D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            vx = item.velocity[0] + .5f*l[1][0];
            vy = item.velocity[1] + .5f*l[1][1];
            vz = item.velocity[2] + .5f*l[1][2];
            v = (float)Math.sqrt(vx*vx+vy*vy+vz*vz);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
                vz = speedLimit * vz / v;
            }
            k[2][0] = timestep*vx;
            k[2][1] = timestep*vy;
            k[2][2] = timestep*vz;
            l[2][0] = coeff*item.force[0];
            l[2][1] = coeff*item.force[1];
            l[2][2] = coeff*item.force[2];

            // Set the position to the new predicted position
            item.location[0] = item.plocation[0] + 0.5f*k[2][0];
            item.location[1] = item.plocation[1] + 0.5f*k[2][1];
            item.location[2] = item.plocation[2] + 0.5f*k[2][2];
        }
        
        // recalculate forces
        sim.accumulate();
        
        iter = sim.getItems();
        while ( iter.hasNext() ) {
            ForceItem3D item = (ForceItem3D)iter.next();
            coeff = timestep / item.mass;
            k = item.k;
            l = item.l;
            float[] p = item.plocation;
            vx = item.velocity[0] + l[2][0];
            vy = item.velocity[1] + l[2][1];
            vz = item.velocity[2] + l[2][2];
            v = (float)Math.sqrt(vx*vx+vy*vy+vz*vz);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
                vz = speedLimit * vz / v;
            }
            k[3][0] = timestep*vx;
            k[3][1] = timestep*vy;
            k[3][2] = timestep*vz;
            l[3][0] = coeff*item.force[0];
            l[3][1] = coeff*item.force[1];
            l[3][2] = coeff*item.force[2];
            item.location[0] = p[0] + (k[0][0]+k[3][0])/6.0f + (k[1][0]+k[2][0])/3.0f;
            item.location[1] = p[1] + (k[0][1]+k[3][1])/6.0f + (k[1][1]+k[2][1])/3.0f;
            item.location[2] = p[2] + (k[0][2]+k[3][2])/6.0f + (k[1][2]+k[2][2])/3.0f;

            vx = (l[0][0]+l[3][0])/6.0f + (l[1][0]+l[2][0])/3.0f;
            vy = (l[0][1]+l[3][1])/6.0f + (l[1][1]+l[2][1])/3.0f;
            vz = (l[0][2]+l[3][2])/6.0f + (l[1][2]+l[2][2])/3.0f;
            v = (float)Math.sqrt(vx*vx+vy*vy+vz*vz);
            if ( v > speedLimit ) {
                vx = speedLimit * vx / v;
                vy = speedLimit * vy / v;
                vz = speedLimit * vz / v;
            }
            item.velocity[0] += vx;
            item.velocity[1] += vy;
            item.velocity[2] += vz;
        }
    }

} // end of class RungeKuttaIntegrator
