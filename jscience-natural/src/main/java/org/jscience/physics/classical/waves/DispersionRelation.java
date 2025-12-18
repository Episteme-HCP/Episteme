package org.jscience.physics.classical.waves;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Dispersion relations for various wave types.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class DispersionRelation {

    /**
     * Light in vacuum: $\omega = c k$
     */
    public static Real lightInVacuum(Real k) {
        Real c = Real.of(299792458.0);
        return c.multiply(k);
    }

    /**
     * Deep water gravity waves: $\omega = \sqrt{g k}$
     */
    public static Real deepWaterGravity(Real k) {
        Real g = Real.of(9.81);
        return g.multiply(k).sqrt();
    }

    /**
     * Shallow water waves: $\omega = k \sqrt{g h}$
     */
    public static Real shallowWater(Real k, Real depth) {
        Real g = Real.of(9.81);
        return k.multiply(g.multiply(depth).sqrt());
    }

    /**
     * Massive relativistic particle: $E^2 = (pc)^2 + (mc^2)^2$
     * gives $\omega = c\sqrt{k^2 + (mc/\hbar)^2}$
     */
    public static Real massiveRelativistic(Real k, Real mass) {
        Real c = Real.of(299792458.0);
        Real hbar = Real.of(1.054571817e-34);
        Real mc = mass.multiply(c).divide(hbar);
        return c.multiply(k.pow(2).add(mc.pow(2)).sqrt());
    }

    /**
     * Non-relativistic Schrödinger free particle: $\omega = \hbar k^2 / 2m$
     */
    public static Real schrodingerFree(Real k, Real mass) {
        Real hbar = Real.of(1.054571817e-34);
        return hbar.multiply(k.pow(2)).divide(mass.multiply(Real.of(2.0)));
    }

    /**
     * Group velocity: $v_g = d\omega/dk$
     * Approximated numerically as $(\omega(k+\delta) - \omega(k-\delta)) / 2\delta$
     */
    public static Real groupVelocity(java.util.function.Function<Real, Real> dispersion, Real k) {
        Real delta = Real.of(1e-8);
        Real omegaPlus = dispersion.apply(k.add(delta));
        Real omegaMinus = dispersion.apply(k.subtract(delta));
        return omegaPlus.subtract(omegaMinus).divide(delta.multiply(Real.of(2.0)));
    }
}
