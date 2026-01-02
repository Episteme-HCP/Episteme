package org.jscience.server.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.jscience.server.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * gRPC Service for Collaboration.
 */
@GrpcService
public class CollaborationServiceImpl extends CollaborationServiceGrpc.CollaborationServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(CollaborationServiceImpl.class);

    // Active Collaboration Sessions
    private final Map<String, List<StreamObserver<SessionEvent>>> collaborationSessions = new ConcurrentHashMap<>();

    @Override
    public void createSession(CreateSessionRequest request, StreamObserver<SessionResponse> responseObserver) {
        String sessionId = UUID.randomUUID().toString();
        collaborationSessions.put(sessionId, new CopyOnWriteArrayList<>());
        LOG.info("Created collaboration session: {} by owner {}", sessionId, request.getOwnerId());

        responseObserver.onNext(SessionResponse.newBuilder().setSessionId(sessionId).build());
        responseObserver.onCompleted();
    }

    @Override
    public void joinSession(SessionRequest request, StreamObserver<SessionEvent> responseObserver) {
        String sessionId = request.getSessionId();
        List<StreamObserver<SessionEvent>> subscribers = collaborationSessions.get(sessionId);

        if (subscribers == null) {
            responseObserver
                    .onError(io.grpc.Status.NOT_FOUND.withDescription("Session not found").asRuntimeException());
            return;
        }

        subscribers.add(responseObserver);
        LOG.info("User {} joined session {}", request.getUserId(), sessionId);
        // Stream stays open
    }

    @Override
    public void publishEvent(SessionEvent request, StreamObserver<PublishAck> responseObserver) {
        String sessionId = request.getSessionId();
        List<StreamObserver<SessionEvent>> subscribers = collaborationSessions.get(sessionId);

        if (subscribers != null) {
            for (StreamObserver<SessionEvent> subscriber : subscribers) {
                try {
                    subscriber.onNext(request);
                } catch (Exception e) {
                    subscribers.remove(subscriber);
                }
            }
        }

        responseObserver.onNext(PublishAck.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
