/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.architecture.traffic;

import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Intelligent Driver Model (IDM) car-following and MOBIL lane-changing simulation.
 * 
 * References:
 * - Treiber, M., Hennecke, A., & Helbing, D. (2000). Congested traffic states in empirical data and microscopic simulation.
 * - Kesting, A., Treiber, M., & Helbing, D. (2007). General Lane-Changing Model MOBIL for Heterogeneous Traffic Flow.
 */
public class TrafficSimulator {

    public static class Car {
        public Quantity<Length> position;
        public Quantity<Velocity> velocity;
        public int lane;
        public String id;
        
        public Car(String id, Quantity<Length> pos, Quantity<Velocity> vel, int lane) {
            this.id = Objects.requireNonNull(id, "Car ID cannot be null");
            this.position = Objects.requireNonNull(pos, "Car position cannot be null");
            this.velocity = Objects.requireNonNull(vel, "Car velocity cannot be null");
            this.lane = lane;
        }
    }

    // NOTE: The original instruction included a 'Passenger' constructor snippet
    // inside the Car class, which is syntactically incorrect and refers to
    // fields not present in Car or TrafficSimulator. This appears to be a
    // copy-paste error from a different context (e.g., an elevator simulation).
    // To fulfill the instruction "Use Objects.requireNonNull in Passenger constructor"
    // while maintaining syntactic correctness and relevance to this file,
    // Objects.requireNonNull has been applied to the Car constructor's parameters.
    // If a Passenger class is intended, it should be defined separately.

    private final List<Car> cars = new ArrayList<>();
    private Quantity<Length> trackLength;
    private int numLanes = 2;
    
    // IDM Parameters
    private Quantity<Velocity> desiredVelocity = Quantities.create(30.0, Units.METER_PER_SECOND);
    private Quantity<Time> timeGap = Quantities.create(1.5, Units.SECOND);
    private Quantity<Length> minGap = Quantities.create(2.0, Units.METER);
    private double delta = 4.0;
    private Quantity<Acceleration> maxAccel = Quantities.create(1.0, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> breakingDecel = Quantities.create(2.0, Units.METERS_PER_SECOND_SQUARED);

    // MOBIL Parameters
    private double politenessFactor = 0.2;
    private Quantity<Acceleration> laneChangeThreshold = Quantities.create(0.1, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> safeDecel = Quantities.create(3.0, Units.METERS_PER_SECOND_SQUARED);

    public TrafficSimulator(Quantity<Length> trackLength, int numLanes) {
        this.trackLength = trackLength;
        this.numLanes = numLanes;
    }

    public void initCars(int count) {
        cars.clear();
        Quantity<Length> spacing = trackLength.divide(count).asType(Length.class);
        for (int i = 0; i < count; i++) {
            Quantity<Length> pos = spacing.multiply(i).asType(Length.class);
            pos = pos.add(Quantities.create((Math.random() - 0.5) * 2.0, Units.METER)).asType(Length.class);
            int lane = (int) (Math.random() * numLanes);
            cars.add(new Car("car-" + i, pos, desiredVelocity.multiply(0.8).asType(Velocity.class), lane));
        }
    }

    public void perturb() {
        if (!cars.isEmpty()) {
            cars.get(0).velocity = cars.get(0).velocity.multiply(0.1).asType(Velocity.class);
        }
    }

    public void update(Quantity<Time> dt) {
        if (dt.getValue().doubleValue() > 0.1) dt = Quantities.create(0.1, Units.SECOND);

        int n = cars.size();
        List<Quantity<Acceleration>> accels = new ArrayList<>(n);
        List<Integer> targetLanes = new ArrayList<>(n);

        // Calculate IDM accelerations and potential lane changes (MOBIL)
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Car leadCar = findLeader(car, car.lane);
            accels.add(calculateIDM(car, leadCar));
            
            targetLanes.add(checkLaneChange(car));
        }

        // Apply state updates
        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            car.lane = targetLanes.get(i);
            
            Quantity<Velocity> dv = accels.get(i).multiply(dt).asType(Velocity.class);
            car.velocity = car.velocity.add(dv);
            if (car.velocity.getValue().doubleValue() < 0) car.velocity = Quantities.create(0, Units.METER_PER_SECOND);
            
            Quantity<Length> ds = car.velocity.multiply(dt).asType(Length.class);
            car.position = car.position.add(ds);
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
            if (dx.getValue().doubleValue() < 0) dx = dx.add(trackLength).asType(Length.class);
            dv = car.velocity.subtract(leader.velocity).asType(Velocity.class);
        } else {
            dx = Quantities.create(1000.0, Units.METER); // Large gap for no leader
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

        // Prioritize changing to the left lane if beneficial, then right
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
        
        // Calculate acceleration if car stays in current lane
        Quantity<Acceleration> aCurrent = calculateIDM(car, findLeader(car, car.lane));
        
        // Calculate acceleration if car moves to otherLane
        Quantity<Acceleration> aNew = calculateIDM(car, leaderOther);

        // Safety criterion: Check if the new follower in the target lane would have to decelerate too much
        if (followerOther != null) {
            // Calculate follower's acceleration if car changes lane
            Quantity<Acceleration> aFollowerNew = calculateIDM(followerOther, car);
            if (aFollowerNew.getValue().doubleValue() < -safeDecel.getValue().doubleValue()) {
                return false; // Not safe to change lane
            }
        }

        // Incentive criterion: Check if changing lane provides a sufficient acceleration gain
        double selfGain = aNew.subtract(aCurrent).getValue().doubleValue();
        
        // Politeness: Consider the impact on the follower in the current lane if car leaves
        double followerGain = 0;
        if (followerOther != null) {
            // Calculate follower's acceleration if car stays in its current lane (otherLane)
            Quantity<Acceleration> aFollowerOld = calculateIDM(followerOther, findLeader(followerOther, otherLane));
            // Calculate follower's acceleration if car changes to otherLane (making 'car' its new leader)
            Quantity<Acceleration> aFollowerNew = calculateIDM(followerOther, car);
            followerGain = aFollowerNew.subtract(aFollowerOld).getValue().doubleValue();
        }
        
        return (selfGain + politenessFactor * followerGain) > laneChangeThreshold.getValue().doubleValue();
    }

    private Car findLeader(Car car, int lane) {
        Car best = null;
        double minDist = trackLength.getValue().doubleValue(); // Initialize with max possible distance
        for (Car other : cars) {
            if (other == car || other.lane != lane) continue;
            
            double dist = other.position.subtract(car.position).getValue().doubleValue();
            if (dist < 0) { // Car is ahead in terms of position, but behind on the track (wrapped around)
                dist += trackLength.getValue().doubleValue();
            }
            
            if (dist < minDist) {
                minDist = dist;
                best = other;
            }
        }
        return best;
    }

    private Car findFollower(Car car, int lane) {
        Car best = null;
        double minDist = trackLength.getValue().doubleValue(); // Initialize with max possible distance
        for (Car other : cars) {
            if (other == car || other.lane != lane) continue;
            
            double dist = car.position.subtract(other.position).getValue().doubleValue();
            if (dist < 0) { // Other car is ahead in terms of position, but behind on the track (wrapped around)
                dist += trackLength.getValue().doubleValue();
            }
            
            if (dist < minDist) {
                minDist = dist;
                best = other;
            }
        }
        return best;
    }

    public List<Car> getCars() { return cars; }
}
