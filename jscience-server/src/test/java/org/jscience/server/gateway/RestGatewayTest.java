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
