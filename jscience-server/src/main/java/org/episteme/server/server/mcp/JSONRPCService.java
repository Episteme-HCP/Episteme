/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.server.server.mcp;

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
public class JSONRPCService {

    private static final Logger LOG = LoggerFactory.getLogger(JSONRPCService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final MCPToolRegistry registry;

    public JSONRPCService(MCPToolRegistry registry) {
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
        String name = params.get("name").asText();
        LOG.info("Executing tool: {}", name);
        
        try {
            var response = mapper.createObjectNode();
            response.put("jsonrpc", "2.0");
            response.set("id", id);
            var result = response.putObject("result");
            
            if ("convert_units".equals(name)) {
                return executeConvertUnits(params.get("arguments"), response);
            } else if ("get_constant".equals(name)) {
                return executeGetConstant(params.get("arguments"), response);
            } else if ("get_data_model".equals(name)) {
                return executeGetDataModel(params.get("arguments"), response);
            }
            
            result.put("content", "Unknown tool name: " + name);
            return mapper.writeValueAsString(response);
        } catch(Exception e) {
            LOG.error("Error executing tool", e);
            return error(id, -32000, "Internal error: " + e.getMessage());
        }
    }

    private String executeGetDataModel(JsonNode args, com.fasterxml.jackson.databind.node.ObjectNode response) throws IOException {
        String modelName = args.get("name").asText();
        String type = "UNKNOWN";
        String summary = "Requested data model: " + modelName;
        
        // Mocked response for demo
        var resultNode = response.get("result");
        var content = ((com.fasterxml.jackson.databind.node.ObjectNode)resultNode).putObject("content");
        
        if (modelName.contains("Spatial")) {
            type = "SPATIAL_GEOMETRY";
            summary = "Global Migration Flow Dataset (2025)";
            content.put("model_type", type);
            var md = content.putObject("metadata");
            md.put("name", summary);
            md.put("source", "Synthetic Data");
            var q = content.putObject("quantities");
            q.putObject("locations_count").put("value", 150).put("unit", "ONE");
            q.putObject("total_magnitude").put("value", 1.25e7).put("unit", "ONE");
        } else if (modelName.contains("Portfolio")) {
            type = "FINANCIAL_PORTFOLIO";
            summary = "ESG High-Growth Portfolio";
            content.put("model_type", type);
            var md = content.putObject("metadata");
            md.put("name", summary);
            md.put("risk_profile", "Aggressive");
            var q = content.putObject("quantities");
            q.putObject("assets_count").put("value", 24).put("unit", "ONE");
            q.putObject("total_value").put("value", 4500000.0).put("unit", "USD");
        } else if (modelName.contains("Epidemiology") || modelName.contains("SIR")) {
            type = "EPIDEMIOLOGICAL_SIR";
            summary = "COVID-19 Simulation Run B";
            content.put("model_type", type);
            var md = content.putObject("metadata");
            md.put("name", summary);
            md.put("virus", "SARS-CoV-2");
            var q = content.putObject("quantities");
            q.putObject("susceptible").put("value", 9500000).put("unit", "ONE");
            q.putObject("infected").put("value", 50000).put("unit", "ONE");
            q.putObject("recovered").put("value", 450000).put("unit", "ONE");
            q.putObject("r0").put("value", 2.5).put("unit", "ONE");
        } else {
            content.put("summary", summary);
        }
        
        return mapper.writeValueAsString(response);
    }

    private String executeConvertUnits(JsonNode args, com.fasterxml.jackson.databind.node.ObjectNode response) throws IOException {

        double value = args.get("value").asDouble();
        String from = args.get("from").asText();
        String to = args.get("to").asText();
        
        // Mock implementation for now, in a real app would use Units.valueOf(from) etc.
        double resultValue = value; // placeholder
        
        var resultNode = response.get("result");
        ((com.fasterxml.jackson.databind.node.ObjectNode)resultNode).put("content", 
            String.format("%f %s = %f %s (Simulated)", value, from, resultValue, to));
        return mapper.writeValueAsString(response);
    }

    private String executeGetConstant(JsonNode args, com.fasterxml.jackson.databind.node.ObjectNode response) throws IOException {
        String name = args.get("name").asText().toUpperCase();
        
        String value = "Unknown constant";
        
        // Basic lookup for common constants
        if ("PI".equals(name)) value = "3.141592653589793";
        else if ("E".equals(name)) value = "2.718281828459045";
        else if ("SPEED_OF_LIGHT".equals(name)) value = "299792458 m/s";
        else if ("EARTH_MASS".equals(name)) value = "5.972235e24 kg";
        
        var resultNode = response.get("result");
        ((com.fasterxml.jackson.databind.node.ObjectNode)resultNode).put("content", 
            String.format("Constant %s: %s", name, value));
        return mapper.writeValueAsString(response);
    }


    private String error(JsonNode id, int code, String message) {
        return String.format("{\"jsonrpc\": \"2.0\", \"id\": %s, \"error\": {\"code\": %d, \"message\": \"%s\"}}",
                id, code, message);
    }
}
