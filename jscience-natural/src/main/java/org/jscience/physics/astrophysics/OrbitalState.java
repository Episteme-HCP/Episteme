package org.jscience.physics.astrophysics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Velocity;

public class OrbitalState {

    private final Quantity<Length> x, y, z;
    private final Quantity<Velocity> vx, vy, vz;

    public OrbitalState(Quantity<Length> x, Quantity<Length> y, Quantity<Length> z,
            Quantity<Velocity> vx, Quantity<Velocity> vy, Quantity<Velocity> vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public Quantity<Length> getX() {
        return x;
    }

    public Quantity<Length> getY() {
        return y;
    }

    public Quantity<Length> getZ() {
        return z;
    }

    public Quantity<Velocity> getVx() {
        return vx;
    }

    public Quantity<Velocity> getVy() {
        return vy;
    }

    public Quantity<Velocity> getVz() {
        return vz;
    }

    @Override
    public String toString() {
        return String.format("Pos[%s, %s, %s] Vel[%s, %s, %s]", x, y, z, vx, vy, vz);
    }
}
