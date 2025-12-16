package org.jscience.mathematics.geometry;

import org.jscience.mathematics.numbers.real.Real;

public class ReproVector {
    public static void main(String[] args) {
        try {
            System.out.println("ReproVector Start");

            Real r1 = Real.of(1.0);
            Real r2 = Real.of(1.0);
            System.out.println("r1: " + r1);
            System.out.println("r2: " + r2);

            Real sum = r1.add(r2);
            System.out.println("sum: " + sum);

            Real prod = r1.multiply(r2);
            System.out.println("prod: " + prod);

            Vector2D v = new Vector2D(1.0, 1.0);
            System.out.println("Vector v: " + v);

            Real norm = v.norm();
            System.out.println("Vector norm: " + norm);

            Vector2D normalized = v.normalize();
            System.out.println("Normalized: " + normalized);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
