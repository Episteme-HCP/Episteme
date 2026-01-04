package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import org.jscience.server.proto.*;

import java.util.UUID;

/**
 * Mock implementation of DeviceService for testing.
 * Preserves the original hardcoded mock behavior for unit tests.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MockDeviceServiceImpl extends DeviceServiceGrpc.DeviceServiceImplBase {

    @Override
    public void listDevices(Empty request, StreamObserver<DeviceList> responseObserver) {
        // Mock discovery of devices
        DeviceInfo telescope = DeviceInfo.newBuilder()
                .setDeviceId("dev-scope-01")
                .setName("Main Observatory Telescope")
                .setType("TELESCOPE")
                .setConnected(true)
                .build();

        DeviceInfo sensor = DeviceInfo.newBuilder()
                .setDeviceId("dev-sensor-weather")
                .setName("Weather Station Alpha")
                .setType("SENSOR")
                .setConnected(true)
                .build();

        DeviceList response = DeviceList.newBuilder()
                .addDevices(telescope)
                .addDevices(sensor)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendCommand(DeviceCommand request, StreamObserver<CommandResponse> responseObserver) {
        // Mock execution
        CommandResponse response = CommandResponse.newBuilder()
                .setCommandId(UUID.randomUUID().toString())
                .setSuccess(true)
                .setResultMessage("Command " + request.getCommand() + " executed successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeTelemetry(DeviceIdentifier request, StreamObserver<DeviceData> responseObserver) {
        // Mock streaming data (send 3 updates then finish)
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    DeviceData data = DeviceData.newBuilder()
                            .setDeviceId(request.getDeviceId())
                            .setTimestamp(System.currentTimeMillis())
                            .setDataType("LOG")
                            .putNumericReadings("temperature", 20.0 + Math.random())
                            .build();

                    responseObserver.onNext(data);
                    Thread.sleep(1000);
                }
                responseObserver.onCompleted();
            } catch (Exception e) {
                // ignore
            }
        }).start();
    }
}
