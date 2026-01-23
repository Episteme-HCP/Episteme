package org.jscience.server.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service to handle JSON-RPC 2.0 requests for MCP.
 */
@Service
public class JsonRpcService {

    private static final Logger LOG = LoggerFactory.getLogger(JsonRpcService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final McpToolRegistry registry;

    public JsonRpcService(McpToolRegistry registry) {
        this.registry = registry;
    }

    public String handle(String jsonBody) {
        try {
            JsonNode request = mapper.readTree(jsonBody);
            String method = request.get("method").asText();
            JsonNode id = request.get("id");

            if ("tools/list".equals(method)) {
                return listTools(id);
            } else if ("tools/call".equals(method)) {
                return callTool(request.get("params"), id);
            }

            return error(id, -32601, "Method not found");
        } catch (Exception e) {
            LOG.error("Invalid JSON-RPC request", e);
            return error(null, -32700, "Parse error");
        }
    }

    private String listTools(JsonNode id) throws IOException {
        var tools = registry.getAllTools().values();
        // Construct JSON-RPC response manually for simplicity or use DTOs
        var response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", id);
        var result = response.putObject("result");
        var toolsArray = result.putArray("tools");
        
        for (var tool : tools) {
            var t = toolsArray.addObject();
            t.put("name", tool.name());
            t.put("description", tool.description());
            // Schema...
        }
        return mapper.writeValueAsString(response);
    }

    private String callTool(JsonNode params, JsonNode id) {
        // Mock execution
        String name = params.get("name").asText();
        LOG.info("Executing tool: {}", name);
        
        // Return dummy success
        try {
            var response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            response.set("id", id);
            var result = response.putObject("result");
            result.put("content", "Tool execution simulated successfully");
            return mapper.writeValueAsString(response);
        } catch(IOException e) {
            return "{}";
        }
    }

    private String error(JsonNode id, int code, String message) {
        return String.format("{\"jsonrpc\": \"2.0\", \"id\": %s, \"error\": {\"code\": %d, \"message\": \"%s\"}}",
                id, code, message);
    }
}
