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

/**
 * Intelligent Driver Model (IDM) Traffic Simulation.
 */
public class TrafficSimulator {

    public static class Car {
        public Quantity<Length> position;
        public Quantity<Velocity> velocity;
        public String id;
        
        public Car(String id, Quantity<Length> pos, Quantity<Velocity> vel) {
            this.id = id;
            this.position = pos;
            this.velocity = vel;
        }
    }

    private final List<Car> cars = new ArrayList<>();
    private Quantity<Length> trackLength;
    
    // IDM Parameters
    private Quantity<Velocity> desiredVelocity = Quantities.create(30.0, Units.METER_PER_SECOND);
    private Quantity<Time> timeGap = Quantities.create(1.5, Units.SECOND);
    private Quantity<Length> minGap = Quantities.create(2.0, Units.METER);
    private double delta = 4.0;
    private Quantity<Acceleration> maxAccel = Quantities.create(1.0, Units.METERS_PER_SECOND_SQUARED);
    private Quantity<Acceleration> breakingDecel = Quantities.create(2.0, Units.METERS_PER_SECOND_SQUARED);

    public TrafficSimulator(Quantity<Length> trackLength) {
        this.trackLength = trackLength;
    }

    public void initCars(int count) {
        cars.clear();
        Quantity<Length> spacing = trackLength.divide(count).asType(Length.class);
        for (int i = 0; i < count; i++) {
            Quantity<Length> pos = spacing.multiply(i).asType(Length.class);
            // Random jitter
            pos = pos.add(Quantities.create((Math.random() - 0.5) * 2.0, Units.METER)).asType(Length.class);
            cars.add(new Car("car-" + i, pos, desiredVelocity.multiply(0.8).asType(Velocity.class)));
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

        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Car leadCar = cars.get((i + 1) % n);

            Quantity<Length> dx = leadCar.position.subtract(car.position).asType(Length.class);
            if (dx.getValue().doubleValue() < 0) dx = dx.add(trackLength).asType(Length.class);

            Quantity<Velocity> dv = car.velocity.subtract(leadCar.velocity).asType(Velocity.class);

            double vRatio = car.velocity.divide(desiredVelocity).getValue().doubleValue();
            double term1 = Math.pow(vRatio, delta);

            Quantity<?> moment = car.velocity.multiply(dv);
            Quantity<?> sqrtAB = maxAccel.multiply(breakingDecel).sqrt();
            Quantity<Length> velocityComponent = car.velocity.multiply(timeGap).asType(Length.class);
            Quantity<Length> breakComponent = moment.divide(sqrtAB.multiply(2.0)).asType(Length.class);
            Quantity<Length> sStar = minGap.add(velocityComponent).add(breakComponent).asType(Length.class);

            double sRatio = sStar.divide(dx).getValue().doubleValue();
            double term2 = sRatio * sRatio;

            accels.add((Quantity<Acceleration>) maxAccel.multiply(1.0 - term1 - term2));
        }

        for (int i = 0; i < n; i++) {
            Car car = cars.get(i);
            Quantity<Velocity> velocityChange = accels.get(i).multiply(dt).asType(Velocity.class);
            car.velocity = car.velocity.add(velocityChange);
            if (car.velocity.getValue().doubleValue() < 0) car.velocity = Quantities.create(0.0, Units.METER_PER_SECOND);

            Quantity<Length> distanceChange = car.velocity.multiply(dt).asType(Length.class);
            car.position = car.position.add(distanceChange);
            if (car.position.getValue().doubleValue() > trackLength.getValue().doubleValue()) {
                car.position = car.position.subtract(trackLength).asType(Length.class);
            }
        }
    }

    public List<Car> getCars() { return cars; }
    
    public void setDesiredVelocity(Quantity<Velocity> v) { this.desiredVelocity = v; }
    public void setTimeGap(Quantity<Time> t) { this.timeGap = t; }
    // ... etc
}
