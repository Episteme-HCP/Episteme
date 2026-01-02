package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.server.proto.*;

import java.util.UUID;

/**
 * gRPC Service for Data Lake.
 */
@GrpcService
public class DataServiceImpl extends DataServiceGrpc.DataServiceImplBase {

    @Override
    public void streamGenomeData(GenomeRegionRequest request, StreamObserver<GenomeChunk> responseObserver) {
        try {
            long current = request.getStartBase();
            long end = request.getEndBase();
            while (current < end) {
                long chunkSize = Math.min(100, end - current);
                StringBuilder seq = new StringBuilder();
                for (int i = 0; i < chunkSize; i++) {
                    seq.append("AGCT".charAt((int) (Math.random() * 4)));
                }

                GenomeChunk chunk = GenomeChunk.newBuilder()
                        .setSequence(seq.toString())
                        .setStartBase(current)
                        .build();
                responseObserver.onNext(chunk);
                current += chunkSize;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void streamStarCatalog(SkyRegionRequest request, StreamObserver<StarObject> responseObserver) {
        try {
            // Simulate 10 stars in the region
            for (int i = 0; i < 10; i++) {
                StarObject star = StarObject.newBuilder()
                        .setStarId("Star-" + UUID.randomUUID().toString().substring(0, 8))
                        .setRa(request.getMinRa() + Math.random() * (request.getMaxRa() - request.getMinRa()))
                        .setDec(request.getMinDec() + Math.random() * (request.getMaxDec() - request.getMinDec()))
                        .setMagnitude(Math.random() * 6) // Visible stars
                        .setType("G2V")
                        .build();
                responseObserver.onNext(star);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
