package org.jscience.server.gateway;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jscience.server.proto.Empty;
import org.jscience.server.proto.ServerStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.jscience.server.proto.ComputeServiceGrpc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RestGatewayTest {

    @Test
    void testHealth() {
        RestGateway gateway = new RestGateway();
        ResponseEntity<ObjectNode> response = gateway.health();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("healthy", response.getBody().get("status").asText());
    }

    @Test
    void testGetStatus() {
        // Given
        RestGateway gateway = new RestGateway();
        ComputeServiceGrpc.ComputeServiceBlockingStub mockStub = Mockito
                .mock(ComputeServiceGrpc.ComputeServiceBlockingStub.class);
        ReflectionTestUtils.setField(gateway, "computeStub", mockStub);

        ServerStatus status = ServerStatus.newBuilder()
                .setActiveWorkers(5)
                .setQueuedTasks(10)
                .build();

        when(mockStub.getStatus(any(Empty.class))).thenReturn(status);

        // When
        ResponseEntity<?> response = gateway.getStatus();

        // Then
        assertEquals(200, response.getStatusCode().value());
        ObjectNode body = (ObjectNode) response.getBody();
        assertEquals(5, body.get("activeWorkers").asInt());
        assertEquals(10, body.get("queuedTasks").asInt());
    }
}
