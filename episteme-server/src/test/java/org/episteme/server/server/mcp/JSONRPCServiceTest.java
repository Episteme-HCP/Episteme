package org.episteme.server.server.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JSONRPCServiceTest {

    private JSONRPCService service;
    private ObjectMapper mapper = new ObjectMapper();

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MCPToolRegistry registry = new MCPToolRegistry();
        registry.init();
        // Use a SyncTaskExecutor for predictable testing
        org.springframework.core.task.SyncTaskExecutor taskExecutor = new org.springframework.core.task.SyncTaskExecutor();
        service = new JSONRPCService(registry, mock(ApplicationContext.class), new SimpleMeterRegistry(), taskExecutor);
        
        // Configure safe data root for testing
        ReflectionTestUtils.setField(service, "dataRoot", tempDir.toString());
    }

    @Test
    void testListTools() throws IOException {
        String request = "{\"jsonrpc\": \"2.0\", \"method\": \"tools/list\", \"id\": 1}";
        String response = service.handle(request);
        JsonNode node = mapper.readTree(response);
        
        assertTrue(node.has("result"));
        assertTrue(node.get("result").get("tools").size() > 0);
    }

    @Test
    void testPathTraversalProtection() throws IOException {
        // Create a file OUTSIDE the tempDir
        Path outsideFile = Files.createTempFile("malicious", ".hdf5");
        
        String request = String.format(
            "{\"jsonrpc\": \"2.0\", \"method\": \"tools/call\", \"params\": {\"name\": \"read_hdf5_data\", \"arguments\": {\"filePath\": \"%s\", \"datasetPath\": \"/data\"}}, \"id\": 1}",
            outsideFile.toAbsolutePath().toString().replace("\\", "\\\\")
        );
        
        String response = service.handle(request);
        JsonNode node = mapper.readTree(response);
        
        assertTrue(node.has("error"), "Should have an error for path traversal");
        assertEquals(-32002, node.get("error").get("code").asInt());
        assertTrue(node.get("error").get("message").asText().contains("Access denied"));
        
        Files.deleteIfExists(outsideFile);
    }

    @Test
    void testValidPathInsideRoot() throws IOException {
        Path insideFile = tempDir.resolve("safe.hdf5");
        Files.createFile(insideFile);
        
        // This will still fail on HDF5 library call because it's an empty file, but it should PASS the security check
        String request = String.format(
            "{\"jsonrpc\": \"2.0\", \"method\": \"tools/call\", \"params\": {\"name\": \"read_hdf5_data\", \"arguments\": {\"filePath\": \"safe.hdf5\", \"datasetPath\": \"/data\"}}, \"id\": 1}",
            insideFile.getFileName().toString()
        );
        
        String response = service.handle(request);
        JsonNode node = mapper.readTree(response);
        
        // Should NOT be code -32002 (Security Exception)
        if (node.has("error")) {
            assertNotEquals(-32002, node.get("error").get("code").asInt(), "Should not be a security error");
        }
    }

    @Test
    void testConvertUnits() throws IOException {
        String request = "{\"jsonrpc\": \"2.0\", \"method\": \"tools/call\", \"params\": {\"name\": \"convert_units\", \"arguments\": {\"value\": 100, \"from\": \"km\", \"to\": \"m\"}}, \"id\": 1}";
        String response = service.handle(request);
        JsonNode node = mapper.readTree(response);
        
        assertFalse(node.has("error"));
        assertTrue(node.get("result").get("content").asText().contains("100000.0"));
    }
}
