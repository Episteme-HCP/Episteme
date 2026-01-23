package org.jscience.server.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Model Context Protocol (MCP) Endpoint.
 * 
 * Implements the Server-Sent Events (SSE) transport for MCP.
 * Clients connect to /mcp/sse to receive events and POST to /mcp/message to send commands.
 */
@RestController
@RequestMapping("/mcp")
public class McpEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(McpEndpoint.class);
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final JsonRpcService jsonRpcService;

    public McpEndpoint(JsonRpcService jsonRpcService) {
        this.jsonRpcService = jsonRpcService;
    }

    /**
     * Establish an SSE connection.
     * This endpoint pushes the MCP "endpoint" event to the client.
     */
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        LOG.info("New MCP Client connected. Total: {}", emitters.size());

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        // Send the initial endpoint URL as per MCP spec
        try {
             emitter.send(SseEmitter.event().name("endpoint").data("/mcp/message"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }

        return emitter;
    }

    /**
     * Handle client messages (JSON-RPC 2.0).
     */
    @PostMapping(path = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleMessage(@RequestBody String message) {
        LOG.debug("Received MCP message: {}", message);
        return jsonRpcService.handle(message);
    }
}
