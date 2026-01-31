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

package org.jscience.social.architecture.traffic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Acceleration;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.measure.quantity.Time;
import org.jscience.core.measure.quantity.Velocity;

/**
 * Microscopic traffic simulation engine implementing the Intelligent Driver 
 * Model (IDM) for longitudinal car-following and MOBIL for lateral lane-changing 
 * logic.
 *
 * <p>References:</p>
 * <ul>
 *   <li>Treiber, M., Hennecke, A., & Helbing, D. (2000). Congested traffic states in empirical data and microscopic simulation.</li>
 *   <li>Kesting, A., Treiber, M., & Helbing, D. (2007). General Lane-Changing Model MOBIL for Heterogeneous Traffic Flow.</li>
 * </ul>
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TrafficSimulator implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * Represents a single vehicle in the traffic simulation.
     */
    public static class Car implements Serializable {
        private static final long serialVersionUID = 2L;
        
        public Quantity<Length> position;
        public Quantity<Velocity> velocity;
        public int lane;
        public String id;
        
        /**
         * Initializes a new vehicle.
         * 
         * @param id unique identifier
         * @param pos initial position along the track
         * @param vel initial velocity
         * @param lane starting lane index
         */
        public Car(String id, Quantity<Length> pos, Quantity<Velocity> vel, int lane) {
            this.id = Objects.requireNonNull(id, "Car ID cannot be null");
            this.position = Objects.requireNonNull(pos, "Car position cannot be null");
            this.velocity = Objects.requireNonNull(vel, "Car velocity cannot be null");
            this.lane = lane;
        }
    }

    private final List<Car> cars = new ArrayList<>();
    private Quantity<Length> trackLength;
    private int numLanes = 2;
    
    // IDM Parameters (Longitudinal Control)
    private Quantity<Velocity> desiredVelocity = Quantities.create(30.0, Units.METER_PER_SECOND);
    private Quantity<Time> timeGap = Quantities.create(1.5, Units.SECOND);
    private Quantity<Length> minGap = Quantities.create(2.0, Units.METER);
    private double delta = 4.0;
    private Quantity<Acceleration> maxAccel = Quantities.create(1.0, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> breakingDecel = Quantities.create(2.0, Units.METERS_PER_SECOND_SQUARED);

    // MOBIL Parameters (Lateral Control)
    private double politenessFactor = 0.2;
    private Quantity<Acceleration> laneChangeThreshold = Quantities.create(0.1, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> safeDecel = Quantities.create(3.0, Units.METERS_PER_SECOND_SQUARED);

    /**
     * Initializes a circular track simulator.
     * 
     * @param trackLength the total circumference of the track
     * @param numLanes the number of available traffic lanes
     */
    public TrafficSimulator(Quantity<Length> trackLength, int numLanes) {
        this.trackLength = Objects.requireNonNull(trackLength, "Track length cannot be null");
        this.numLanes = Math.max(1, numLanes);
    }

    /**
     * Populates the track with a set of vehicles at random positions and lanes.
     * 
     * @param count the number of cars to spawn
     */
    public void initCars(int count) {
        cars.clear();
        if (count <= 0) return;
        Quantity<Length> spacing = trackLength.divide(count).asType(Length.class);
        for (int i = 0; i < count; i++) {
            Quantity<Length> pos = spacing.multiply(i).asType(Length.class);
            pos = pos.add(Quantities.create((Math.random() - 0.5) * 2.0, Units.METER)).asType(Length.class);
            int lane = (int) (Math.random() * numLanes);
            cars.add(new Car("car-" + i, pos, desiredVelocity.multiply(0.8).asType(Velocity.class), lane));
        }
    }

    /**
     * Introduces a disturbance into the system by significantly slowing down 
     * the lead car, allowing for the observation of shockwave formation.
     */
    public void perturb() {
        if (!cars.isEmpty()) {
            cars.get(0).velocity = cars.get(0).velocity.multiply(0.1).asType(Velocity.class);
        }
    }

    /**
     * Updates the simulation by a fixed time step.
     * 
     * @param dt the time delta for progress
     */

    public void update(Quantity<Time> dt) {
        if (dt == null) return;
        double dtVal = dt.to(Units.SECOND).getValue().doubleValue();
        if (dtVal > 0.1) dt = Quantities.create(0.1, Units.SECOND);

        int n = cars.size();
        List<Quantity<Acceleration>> accels = new ArrayList<>(n);
        List<Integer> targetLanes = new ArrayList<>(n);

        // Step 1: Compute decision logic (accel and lane)
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Car leadCar = findLeader(car, car.lane);
            accels.add(calculateIDM(car, leadCar));
            targetLanes.add(checkLaneChange(car));
        }

        // Step 2: Integrate Euler updates
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            car.lane = targetLanes.get(i);
            
            Quantity<Velocity> dv = accels.get(i).multiply(dt).asType(Velocity.class);
            car.velocity = car.velocity.add(dv);
            if (car.velocity.getValue().doubleValue() < 0) {
                car.velocity = Quantities.create(0, Units.METER_PER_SECOND);
            }
            
            Quantity<Length> ds = car.velocity.multiply(dt).asType(Length.class);
            car.position = car.position.add(ds);
            
            // Periodic boundary conditions (circular track)
            if (car.position.getValue().doubleValue() > trackLength.getValue().doubleValue()) {
                car.position = car.position.subtract(trackLength).asType(Length.class);
            }
        }
    }


    private Quantity<Acceleration> calculateIDM(Car car, Car leader) {
        double vRatio = car.velocity.divide(desiredVelocity).getValue().doubleValue();
        double term1 = Math.pow(vRatio, delta);

        Quantity<Length> dx;
        Quantity<Velocity> dv;

        if (leader != null) {
            dx = leader.position.subtract(car.position).asType(Length.class);
            if (dx.getValue().doubleValue() < 0) {
                dx = dx.add(trackLength).asType(Length.class);
            }
            dv = car.velocity.subtract(leader.velocity).asType(Velocity.class);
        } else {
            dx = Quantities.create(1000.0, Units.METER);
            dv = Quantities.create(0.0, Units.METER_PER_SECOND);
        }

        Quantity<?> sqrtAB = maxAccel.multiply(breakingDecel).sqrt();
        Quantity<Length> sStar = minGap.add(car.velocity.multiply(timeGap).asType(Length.class))
                .add(car.velocity.multiply(dv).divide(sqrtAB.multiply(2.0)).asType(Length.class));

        double sRatio = sStar.divide(dx).getValue().doubleValue();
        double term2 = sRatio * sRatio;

        return (Quantity<Acceleration>) maxAccel.multiply(1.0 - term1 - term2);
    }

    private int checkLaneChange(Car car) {
        if (numLanes < 2) return car.lane;

        int[] options = {car.lane - 1, car.lane + 1};
        for (int otherLane : options) {
            if (otherLane >= 0 && otherLane < numLanes) {
                if (isSafeAndIncentive(car, otherLane)) {
                    return otherLane;
                }
            }
        }
        return car.lane;
    }

    private boolean isSafeAndIncentive(Car car, int otherLane) {
        Car leaderOther = findLeader(car, otherLane);
        Car followerOther = findFollower(car, otherLane);
        
        Quantity<Acceleration> aCurrent = calculateIDM(car, findLeader(car, car.lane));
        Quantity<Acceleration> aNew = calculateIDM(car, leaderOther);

        // Safety criterion
        if (followerOther != null) {
            Quantity<Acceleration> aFollowerNew = calculateIDM(followerOther, car);
            if (aFollowerNew.getValue().doubleValue() < -safeDecel.getValue().doubleValue()) {
                return false;
            }
        }

        // Incentive criterion with politeness
        double selfGain = aNew.subtract(aCurrent).getValue().doubleValue();
        double followerGain = 0;
        if (followerOther != null) {
            Quantity<Acceleration> aFollowerOld = calculateIDM(followerOther, findLeader(followerOther, otherLane));
            Quantity<Acceleration> aFollowerNew = calculateIDM(followerOther, car);
            followerGain = aFollowerNew.subtract(aFollowerOld).getValue().doubleValue();
        }
        
        return (selfGain + politenessFactor * followerGain) > laneChangeThreshold.getValue().doubleValue();
    }

    private Car findLeader(Car car, int lane) {
        Car best = null;
        double minDist = trackLength.getValue().doubleValue(); 
        for (Car other : cars) {
            if (other == car || other.lane != lane) continue;
            
            double dist = other.position.subtract(car.position).getValue().doubleValue();
            if (dist < 0) dist += trackLength.getValue().doubleValue();
            
            if (dist < minDist) {
                minDist = dist;
                best = other;
            }
        }
        return best;
    }

    private Car findFollower(Car car, int lane) {
        Car best = null;
        double minDist = trackLength.getValue().doubleValue();
        for (Car other : cars) {
            if (other == car || other.lane != lane) continue;
            
            double dist = car.position.subtract(other.position).getValue().doubleValue();
            if (dist < 0) dist += trackLength.getValue().doubleValue();
            
            if (dist < minDist) {
                minDist = dist;
                best = other;
            }
        }
        return best;
    }

    public List<Car> getCars() { 
        return cars; 
    }
}

