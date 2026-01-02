package org.jscience.server.gateway;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestGatewayTest {

    @Test
    void testConstructorAndConfig() {
        int port = 9090;
        String host = "localhost";
        int grpcPort = 50051;

        RestGateway gateway = new RestGateway(port, host, grpcPort);
        assertNotNull(gateway);
        // Since fields are private and there are no getters, we mainly verify it
        // doesn't crash on init
    }
}
