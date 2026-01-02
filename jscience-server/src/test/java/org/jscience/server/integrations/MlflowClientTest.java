package org.jscience.server.integrations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MlflowClientTest {

    @Test
    void testInitializationDefaults() {
        // This test assumes no MLflow server is running on localhost:5000
        // It verifies that the client can be instantiated and gracefully handles
        // connection failure
        // by defaulting the experiment ID to "0".

        MlflowClient client = new MlflowClient();
        assertNotNull(client);

        String expId = client.getExperimentId();
        assertNotNull(expId, "Experiment ID should not be null");
        // It will likely be "0" if the server is unreachable
        assertEquals("0", expId, "Expected default experiment ID '0' when server is unreachable");
    }

    @Test
    void testLogArtifact() throws Exception {
        MlflowClient client = new MlflowClient();
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test-artifact", ".txt");

        try {
            // Should not throw exception even if server is offline
            client.logArtifact("run-123", tempFile, null);
            assertTrue(true, "Artifact logging gracefully handled missing server");
        } finally {
            java.nio.file.Files.deleteIfExists(tempFile);
        }
    }
}
