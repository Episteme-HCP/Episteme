package org.jscience.ui;

import org.jscience.device.Device;
import java.io.IOException;

/**
 * Mock device class for testing DashboardDiscovery.
 */
public class TestDevice implements Device {
    @Override
    public void connect() throws IOException {
    }

    @Override
    public void disconnect() throws IOException {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String getName() {
        return "Test Device";
    }

    @Override
    public String getId() {
        return "test-01";
    }

    @Override
    public String getManufacturer() {
        return "Test Inc";
    }

    @Override
    public void close() throws Exception {
    }
}
