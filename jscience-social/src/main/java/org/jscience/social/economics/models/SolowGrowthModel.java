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

package org.jscience.social.economics.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.jscience.core.mathematics.numbers.real.Real;

import org.jscience.core.util.UniversalDataModel;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Analytical model of the Solow-Swan long-run economic growth theory. 
 */
public final class SolowGrowthModel implements UniversalDataModel {

    private final String name;
    private final List<GrowthState> history = new ArrayList<>();
    
    // Model parameters
    private Real alpha = Real.of(0.35);
    private Real savingsRate = Real.of(0.2);
    private Real depreciationRate = Real.of(0.05);

    public SolowGrowthModel(String name) {
        this.name = name;
    }

    public void setAlpha(Real alpha) { this.alpha = alpha; }
    public void setSavingsRate(Real s) { this.savingsRate = s; }
    public void setDepreciationRate(Real d) { this.depreciationRate = d; }

    public record GrowthState(
        int year,
        Quantity<?> capital,
        Quantity<?> labor,
        Quantity<?> output,
        Quantity<?> consumption,
        Quantity<?> investment
    ) implements Serializable {
        private static final long serialVersionUID = 2L;
    }

    public void simulate(Real initialK, Real initialL, Real a, Real n, Real g, int periods) {
        Real k = initialK;
        Real l = initialL;
        Real tfp = a;
        Real oneMinusAlpha = Real.ONE.subtract(alpha);

        int startYear = history.isEmpty() ? 0 : history.get(history.size() - 1).year() + 1;

        for (int t = 0; t < periods; t++) {
            Real y = tfp.multiply(k.pow(alpha)).multiply(l.pow(oneMinusAlpha));
            Real i = savingsRate.multiply(y);
            Real c = y.subtract(i);
            
            history.add(new GrowthState(
                startYear + t,
                Quantities.create(k.doubleValue(), Units.ONE), // Units could be more specific
                Quantities.create(l.doubleValue(), Units.ONE),
                Quantities.create(y.doubleValue(), Units.ONE),
                Quantities.create(c.doubleValue(), Units.ONE),
                Quantities.create(i.doubleValue(), Units.ONE)
            ));

            k = k.add(i).subtract(depreciationRate.multiply(k));
            l = l.multiply(Real.ONE.add(n));
            tfp = tfp.multiply(Real.ONE.add(g));
        }
    }

    public List<GrowthState> getHistory() {
        return Collections.unmodifiableList(history);
    }

    @Override
    public String getModelType() {
        return "ECONOMIC_GROWTH_SOLOW";
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("name", name);
        meta.put("capital_share_alpha", alpha.doubleValue());
        meta.put("savings_rate", savingsRate.doubleValue());
        meta.put("depreciation_rate", depreciationRate.doubleValue());
        return meta;
    }

    @Override
    public Map<String, Quantity<?>> getQuantities() {
        Map<String, Quantity<?>> q = new HashMap<>();
        if (!history.isEmpty()) {
            GrowthState last = history.get(history.size() - 1);
            q.put("current_capital", last.capital());
            q.put("current_output", last.output());
            q.put("current_labor", last.labor());
        }
        return q;
    }
}


