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

package org.episteme.social.sociology;


import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.UniversalDataModel;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.Quantities;
import org.episteme.core.measure.Units;
import java.util.Map;
import java.util.HashMap;

/**
 * Models the diffusion of culture, innovations, or rumors through a population.
 * Provides implementations of standard diffusion models like the Bass Diffusion Model.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class CulturalDiffusionModel implements UniversalDataModel {

    private final String name;
    private Real marketPotential = Real.of(1000000);
    private Real p = Real.of(0.03); // Innovation
    private Real q = Real.of(0.38); // Imitation
    private Real currentAdopters = Real.ZERO;

    public CulturalDiffusionModel(String name) {
        this.name = name;
    }

    public void setParameters(Real p, Real q, Real marketPotential) {
        this.p = p;
        this.q = q;
        this.marketPotential = marketPotential;
    }

    /**
     * Calculates the number of new adopters at time t using the Bass Diffusion Model.
     */
    public Real calculateNewAdopters() {
        if (marketPotential.isZero()) return Real.ZERO;
        
        Real imitationEffect = q.divide(marketPotential).multiply(currentAdopters);
        Real probabilityOfAdoption = p.add(imitationEffect);
        Real remainingPotential = marketPotential.subtract(currentAdopters);
        
        return probabilityOfAdoption.multiply(remainingPotential);
    }

    public void step() {
        Real newAdopters = calculateNewAdopters();
        currentAdopters = currentAdopters.add(newAdopters);
    }

    @Override
    public String getModelType() {
        return "CULTURAL_DIFFUSION_BASS";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("innovation_coefficient", p.doubleValue());
        meta.put("imitation_coefficient", q.doubleValue());
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> qMap = new HashMap<>();
        qMap.put("total_adopters", Quantities.create(currentAdopters.doubleValue(), Units.ONE));
        qMap.put("market_potential", Quantities.create(marketPotential.doubleValue(), Units.ONE));
        qMap.put("adoption_rate", Quantities.create(currentAdopters.divide(marketPotential).doubleValue(), Units.ONE));
        return qMap;
    }
}


