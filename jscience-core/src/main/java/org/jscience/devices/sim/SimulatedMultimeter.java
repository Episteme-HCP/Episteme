package org.jscience.devices.sim;

import org.jscience.devices.sensors.Multimeter;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;

/**
 * Simulated implementation of Multimeter.
 */
public class SimulatedMultimeter extends SimulatedDevice implements Multimeter {

    private Function function = Function.DC_VOLTAGE;
    private Real currentValue = Real.ZERO;

    public SimulatedMultimeter(String name) {
        super(name);
    }

    @Override
    public Function getFunction() {
        return function;
    }

    @Override
    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public Real readValue() throws IOException {
        if (!isConnected())
            throw new IOException("Not connected");
        return currentValue;
    }

    /**
     * Mocks probing a value.
     */
    public void probe(Real value) {
        this.currentValue = value;
    }
}
