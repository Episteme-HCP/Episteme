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

package org.jscience.device.sensors;

import org.jscience.device.Sensor;
import org.jscience.mathematics.numbers.real.Real; // Standardizing on Real

/**
 * Interface for multimeter devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Multimeter extends Sensor<Real> {
    enum Function {
        DC_VOLTAGE("device.multimeter.func.dcv"),
        AC_VOLTAGE("device.multimeter.func.acv"),
        DC_CURRENT("device.multimeter.func.dca"),
        AC_CURRENT("device.multimeter.func.aca"),
        RESISTANCE("device.multimeter.func.resistance"),
        CAPACITANCE("device.multimeter.func.capacitance"),
        FREQUENCY("device.multimeter.func.frequency"),
        CONTINUITY("device.multimeter.func.continuity"),
        DIODE_TEST("device.multimeter.func.diode"),
        TEMPERATURE("device.multimeter.func.temperature");

        private final String i18nKey;

        Function(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        @Override
        public String toString() {
            return org.jscience.ui.i18n.I18n.getInstance().get(i18nKey);
        }
    }

    Function getFunction();

    void setFunction(Function function);

    // Additional methods for unit support or ranges could go here
}


