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

package org.jscience.mathematics.analysis.series;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.numbers.real.Real;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for infinite series implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class InfiniteSeriesTest {

    @Test
    void geometricSeries_convergent() {
        // 1 + 1/2 + 1/4 + ... = 2
        GeometricSeries series = new GeometricSeries(1.0, 0.5);

        assertThat(series.isConvergent()).isTrue();

        Real limit = series.limit();
        assertThat(limit.doubleValue()).isCloseTo(2.0, within(0.0001));

        Real partial10 = series.partialSum(10);
        assertThat(partial10.doubleValue()).isLessThan(2.0);
        assertThat(partial10.doubleValue()).isGreaterThan(1.99);
    }

    @Test
    void geometricSeries_divergent() {
        // r = 1.5 > 1, series diverges
        GeometricSeries series = new GeometricSeries(1.0, 1.5);

        assertThat(series.isConvergent()).isFalse();
        assertThatThrownBy(() -> series.limit())
                .isInstanceOf(ArithmeticException.class);
    }

    @Test
    void harmonicSeries_diverges() {
        HarmonicSeries series = new HarmonicSeries();

        assertThat(series.isConvergent()).isFalse();

        Real h10 = series.partialSum(10);
        assertThat(h10.doubleValue()).isGreaterThan(2.0);

        Real approx = series.approximateSum(100);
        Real actual = series.partialSum(100);
        assertThat(approx.doubleValue()).isCloseTo(actual.doubleValue(), within(0.1));
    }

    @Test
    void powerSeries_exponential() {
        PowerSeries exp = PowerSeries.exponential();

        // e^0 = 1
        Real e0 = exp.at(Real.ZERO).partialSum(10);
        assertThat(e0.doubleValue()).isCloseTo(1.0, within(0.0001));

        // e^1 Ã¢â€°Ë† 2.71828
        Real e1 = exp.at(Real.ONE).partialSum(20);
        assertThat(e1.doubleValue()).isCloseTo(Math.E, within(0.0001));
    }

    @Test
    void powerSeries_sine() {
        PowerSeries sin = PowerSeries.sine();

        // sin(0) = 0
        Real sin0 = sin.at(Real.ZERO).partialSum(10);
        assertThat(sin0.doubleValue()).isCloseTo(0.0, within(0.0001));

        // sin(Ãâ‚¬/2) Ã¢â€°Ë† 1
        Real sinPi2 = sin.at(Real.of(Math.PI / 2)).partialSum(20);
        assertThat(sinPi2.doubleValue()).isCloseTo(1.0, within(0.0001));
    }
}

