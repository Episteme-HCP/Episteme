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

package org.jscience.server.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service to handle JSON-RPC 2.0 requests for MCP.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Service
public class JSONRpcService {

    private static final Logger LOG = LoggerFactory.getLogger(JSONRpcService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final McpToolRegistry registry;

    public JSONRpcService(McpToolRegistry registry) {
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
