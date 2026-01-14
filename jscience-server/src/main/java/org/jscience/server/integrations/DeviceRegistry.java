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

package org.jscience.server.integrations;

import org.jscience.device.Device;
import org.jscience.ui.MasterControlDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for discovered JScience devices.
 * Uses MasterControlDiscovery from jscience-core to discover Device
 * implementations
 * and maintains active device instances.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Component
public class DeviceRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceRegistry.class);

    /** Active device instances by ID */
    private final Map<String, Device> devices = new ConcurrentHashMap<>();

    /** Device class info by ID for lazy instantiation */
    private final Map<String, MasterControlDiscovery.ClassInfo> deviceClasses = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        discoverDevices();
    }

    /**
     * Discovers all Device implementations using MasterControlDiscovery.
     */
    public void discoverDevices() {
        LOG.info("Discovering JScience devices...");
        List<MasterControlDiscovery.ClassInfo> discoveredDevices = MasterControlDiscovery.getInstance()
                .findClasses("Device");

        for (MasterControlDiscovery.ClassInfo info : discoveredDevices) {
            try {
                Class<?> cls = Class.forName(info.fullName);
                Device device = (Device) cls.getDeclaredConstructor().newInstance();
                String deviceId = device.getId();
                devices.put(deviceId, device);
                deviceClasses.put(deviceId, info);
                LOG.info("Registered device: {} ({})", device.getName(), deviceId);
            } catch (Exception e) {
                LOG.debug("Could not instantiate device class {}: {}", info.fullName, e.getMessage());
                // Store class info for later instantiation if needed
                deviceClasses.put(info.simpleName.toLowerCase(), info);
            }
        }
        LOG.info("Device discovery complete. Found {} devices.", devices.size());
    }

    /**
     * Returns all active device instances.
     */
    public Map<String, Device> getDevices() {
        return devices;
    }

    /**
     * Returns a device by ID.
     */
    public Optional<Device> getDevice(String deviceId) {
        return Optional.ofNullable(devices.get(deviceId));
    }

    /**
     * Registers a device manually.
     */
    public void registerDevice(Device device) {
        devices.put(device.getId(), device);
        LOG.info("Manually registered device: {} ({})", device.getName(), device.getId());
    }

    /**
     * Unregisters a device.
     */
    public void unregisterDevice(String deviceId) {
        Device removed = devices.remove(deviceId);
        if (removed != null) {
            LOG.info("Unregistered device: {}", deviceId);
        }
    }
}
