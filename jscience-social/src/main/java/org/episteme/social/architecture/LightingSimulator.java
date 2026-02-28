/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.architecture;

import java.io.Serializable;
import java.util.List;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Units;
import org.episteme.core.measure.quantity.Area;
import org.episteme.core.measure.quantity.Length;

/**
 * Provides mathematical models for lighting simulation and illuminance 
 * calculations in architectural design. It implements the inverse-square law 
 * for point sources and the lumen method for estimating luminaire requirements.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
public final class LightingSimulator {

    private LightingSimulator() {}

    /**
     * Represents a point light source in 3D space.
     * 
     * @param x coordinate
     * @param y coordinate
     * @param z coordinate
     * @param luminousIntensity intensity in candelas (cd)
     */
    public record LightSource(double x, double y, double z, double luminousIntensity) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    /**
     * Calculates the illuminance (Lux) at a specific point on a surface from 
     * a single point source using the inverse-square law.
     * Formula: E = (I * cos(theta)) / d^2
     * 
     * @param source the light source
     * @param pointX X coordinate of measurement point
     * @param pointY Y coordinate of measurement point
     * @param pointZ Z coordinate of measurement point
     * @param normalX surface normal X vector
     * @param normalY surface normal Y vector
     * @param normalZ surface normal Z vector
     * @return illuminance in Lux (lx)
     */
    public static Real illuminanceFromPoint(LightSource source, 
            double pointX, double pointY, double pointZ,
            double normalX, double normalY, double normalZ) {
        
        double dx = source.x() - pointX;
        double dy = source.y() - pointY;
        double dz = source.z() - pointZ;
        double distSq = dx * dx + dy * dy + dz * dz;
        double dist = Math.sqrt(distSq);
        
        if (dist < 0.001) return Real.of(source.luminousIntensity());
        
        // Normalize direction (vector from point to source)
        double dirX = dx / dist, dirY = dy / dist, dirZ = dz / dist;
        
        // Normalize surface normal
        double normLen = Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
        if (normLen == 0) return Real.ZERO;
        
        double nX = normalX / normLen, nY = normalY / normLen, nZ = normalZ / normLen;
        
        // cos(theta) = dot product of light direction and surface normal
        double cosTheta = Math.max(0, dirX * nX + dirY * nY + dirZ * nZ);
        
        double illuminance = (source.luminousIntensity() * cosTheta) / distSq;
        return Real.of(illuminance);
    }

    /**
     * Calculates the total illuminance at a point from multiple light sources.
     * 
     * @param sources list of light sources
     * @param pointX target point X
     * @param pointY target point Y
     * @param pointZ target point Z
     * @param normalX surface normal X
     * @param normalY surface normal Y
     * @param normalZ surface normal Z
     * @return combined illuminance in Lux
     */
    public static Real totalIlluminance(List<LightSource> sources,
            double pointX, double pointY, double pointZ,
            double normalX, double normalY, double normalZ) {
        Real total = Real.ZERO;
        for (LightSource source : sources) {
            total = total.add(illuminanceFromPoint(source, pointX, pointY, pointZ, normalX, normalY, normalZ));
        }
        return total;
    }

    /**
     * Calculates the required luminous intensity (in candelas) to achieve 
     * a target illuminance at a specific distance.
     * 
     * @param targetLux desired light level in Lux
     * @param distance distance to the source
     * @return required intensity in candelas
     */
    public static Real requiredIntensity(Real targetLux, Quantity<Length> distance) {
        double d = distance.to(Units.METER).getValue().doubleValue();
        return targetLux.multiply(Real.of(d * d));
    }

    /**
     * Estimates the number of luminaires needed to achieve a target average 
     * illuminance over an area using the Lumen Method.
     * Formula: N = (E * A) / (n * F * U * M)
     * 
     * @param targetLux average target illuminance
     * @param area total surface area to light
     * @param lumensPerLamp lumens emitted per lamp (Flux)
     * @param lampsPerFixture number of lamps per fixture
     * @param utilizationFactor (U) efficiency of the room 
     * @param maintenanceFactor (M) factor for dirt and aging
     * @return required number of fixtures (rounded up)
     */
    public static int requiredLuminaires(Real targetLux, Quantity<Area> area, 
            double lumensPerLamp, int lampsPerFixture, 
            double utilizationFactor, double maintenanceFactor) {
        
        double E = targetLux.doubleValue();
        double A = area.to(Units.SQUARE_METER).getValue().doubleValue();
        double totalFluxNeeded = E * A;
        
        double fluxPerFixture = lampsPerFixture * lumensPerLamp * utilizationFactor * maintenanceFactor;
        if (fluxPerFixture == 0) return 0;
        
        return (int) Math.ceil(totalFluxNeeded / fluxPerFixture);
    }
}

