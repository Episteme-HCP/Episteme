package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.server.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * gRPC Service for Device Management.
 */
@GrpcService
public class DeviceServiceImpl extends DeviceServiceGrpc.DeviceServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

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
        LOG.info("Received command {} for device {}", request.getCommand(), request.getDeviceId());

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
