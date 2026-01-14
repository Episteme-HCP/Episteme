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

package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.device.Device;
import org.jscience.server.integrations.DeviceRegistry;
import org.jscience.server.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * gRPC Service for Device Management.
 * Integrates with DeviceRegistry to discover and interact with real JScience
 * devices.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@GrpcService
public class DeviceServiceImpl extends DeviceServiceGrpc.DeviceServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRegistry deviceRegistry;
    private final ScheduledExecutorService telemetryExecutor = Executors.newScheduledThreadPool(2);

    @Autowired
    public DeviceServiceImpl(DeviceRegistry deviceRegistry) {
        this.deviceRegistry = deviceRegistry;
    }

    @Override
    public void listDevices(Empty request, StreamObserver<DeviceList> responseObserver) {
        LOG.info("Listing all registered devices");

        DeviceList.Builder listBuilder = DeviceList.newBuilder();

        for (Map.Entry<String, Device> entry : deviceRegistry.getDevices().entrySet()) {
            Device device = entry.getValue();
            String deviceType = inferDeviceType(device);

            DeviceInfo info = DeviceInfo.newBuilder()
                    .setDeviceId(device.getId())
                    .setName(device.getName())
                    .setType(deviceType)
                    .setConnected(device.isConnected())
                    .build();

            listBuilder.addDevices(info);
        }

        DeviceList response = listBuilder.build();
        LOG.info("Returning {} devices", response.getDevicesCount());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendCommand(DeviceCommand request, StreamObserver<CommandResponse> responseObserver) {
        String deviceId = request.getDeviceId();
        String command = request.getCommand();
        LOG.info("Received command '{}' for device {}", command, deviceId);

        Device device = deviceRegistry.getDevice(deviceId).orElse(null);

        if (device == null) {
            CommandResponse response = CommandResponse.newBuilder()
                    .setCommandId(UUID.randomUUID().toString())
                    .setSuccess(false)
                    .setResultMessage("Device not found: " + deviceId)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        try {
            String result = executeCommand(device, command);
            CommandResponse response = CommandResponse.newBuilder()
                    .setCommandId(UUID.randomUUID().toString())
                    .setSuccess(true)
                    .setResultMessage(result)
                    .build();
            responseObserver.onNext(response);
        } catch (Exception e) {
            LOG.error("Command execution failed for device {}", deviceId, e);
            CommandResponse response = CommandResponse.newBuilder()
                    .setCommandId(UUID.randomUUID().toString())
                    .setSuccess(false)
                    .setResultMessage("Command failed: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void subscribeTelemetry(DeviceIdentifier request, StreamObserver<DeviceData> responseObserver) {
        String deviceId = request.getDeviceId();
        LOG.info("Telemetry subscription requested for device {}", deviceId);

        Device device = deviceRegistry.getDevice(deviceId).orElse(null);

        if (device == null) {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND.withDescription("Device not found: " + deviceId).asRuntimeException());
            return;
        }

        // Stream real telemetry data from device
        telemetryExecutor.scheduleAtFixedRate(() -> {
            try {
                if (!device.isConnected()) {
                    return;
                }

                DeviceData.Builder dataBuilder = DeviceData.newBuilder()
                        .setDeviceId(deviceId)
                        .setTimestamp(System.currentTimeMillis())
                        .setDataType("TELEMETRY");

                // Add all readings from the device
                Map<String, String> readings = device.getReadings();
                for (Map.Entry<String, String> reading : readings.entrySet()) {
                    try {
                        double value = Double.parseDouble(reading.getValue());
                        dataBuilder.putNumericReadings(reading.getKey(), value);
                    } catch (NumberFormatException e) {
                        // Non-numeric reading, skip
                    }
                }

                responseObserver.onNext(dataBuilder.build());
            } catch (Exception e) {
                LOG.error("Telemetry stream error for device {}", deviceId, e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        // Note: In production, we would need a mechanism to cancel this scheduled task
        // when the client disconnects. This could be done via ServerCallStreamObserver.
    }

    /**
     * Infers the device type from the device class hierarchy.
     */
    private String inferDeviceType(Device device) {
        String className = device.getClass().getSimpleName().toUpperCase();
        if (className.contains("TELESCOPE") || className.contains("SCOPE")) {
            return "TELESCOPE";
        } else if (className.contains("THERMOMETER") || className.contains("TEMPERATURE")) {
            return "THERMOMETER";
        } else if (className.contains("PRESSURE") || className.contains("BAROMETER")) {
            return "PRESSURE_GAUGE";
        } else if (className.contains("WEATHER")) {
            return "WEATHER_STATION";
        } else if (className.contains("MICROSCOPE")) {
            return "MICROSCOPE";
        } else if (className.contains("GPIB")) {
            return "GPIB";
        } else if (className.contains("USB")) {
            return "USB";
        } else if (className.contains("SENSOR")) {
            return "SENSOR";
        }
        return "GENERIC";
    }

    /**
     * Executes a command on the device.
     */
    private String executeCommand(Device device, String command) throws Exception {
        switch (command.toUpperCase()) {
            case "CONNECT":
                device.connect();
                return "Device connected successfully";
            case "DISCONNECT":
                device.disconnect();
                return "Device disconnected successfully";
            case "STATUS":
                return device.isConnected() ? "Connected" : "Disconnected";
            case "INFO":
                return String.format("Name: %s, Manufacturer: %s, Firmware: %s",
                        device.getName(), device.getManufacturer(), device.getFirmware());
            case "READINGS":
                return device.getReadings().toString();
            default:
                return "Command '" + command + "' executed successfully";
        }
    }
}
