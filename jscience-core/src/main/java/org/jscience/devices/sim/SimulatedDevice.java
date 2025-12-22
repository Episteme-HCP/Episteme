package org.jscience.devices.sim;

import org.jscience.devices.Device;
import java.io.IOException;

/**
 * Abstract base class for simulated devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class SimulatedDevice implements Device {

    private final String name;
    private boolean connected;

    protected SimulatedDevice(String name) {
        this.name = name;
    }

    @Override
    public void connect() throws IOException {
        this.connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void close() throws Exception {
        disconnect();
    }
}
