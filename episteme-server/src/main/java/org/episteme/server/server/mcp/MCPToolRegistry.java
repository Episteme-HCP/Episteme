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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for executable MCP Tools.
 * Scans for services annotated with @MCPTool and registers them.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Service
public class MCPToolRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(MCPToolRegistry.class);
    private final Map<String, ToolDefinition> tools = new ConcurrentHashMap<>();

    private final org.springframework.context.ApplicationContext context;

    public MCPToolRegistry(org.springframework.context.ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        // Core diagnostic tools
        registerTool("convert_units", "Convert scientific units", 
            "{\"type\": \"object\", \"properties\": {\"value\": {\"type\": \"number\"}, \"from\": {\"type\": \"string\"}, \"to\": {\"type\": \"string\"}}, \"required\": [\"value\", \"from\", \"to\"]}");
        registerTool("get_constant", "Retrieve scientific constants (e.g., PI, SPEED_OF_LIGHT, EARTH_MASS)",
            "{\"type\": \"object\", \"properties\": {\"category\": {\"type\": \"string\", \"enum\": [\"MATH\", \"PHYSICS\", \"EARTH\", \"GEOGRAPHY\", \"HISTORY\"]}, \"name\": {\"type\": \"string\"}}, \"required\": [\"name\"]}");
        
        // Dynamic discovery of AlgorithmProviders
        var providers = context.getBeansOfType(org.episteme.core.technical.algorithm.AlgorithmProvider.class);
        for (var provider : providers.values()) {
            if (provider.isAvailable()) {
                String toolName = provider.getName().toLowerCase().replace(" ", "_").replace("/", "_");
                String description = String.format("Execute scientific algorithm: %s (%s)", provider.getName(), provider.getAlgorithmType());
                // For now, we use a generic schema. In a full implementation, 
                // we'd use reflection or a schema-defining interface.
                String schema = "{\"type\": \"object\", \"properties\": {\"params\": {\"type\": \"object\"}}}";
                registerTool(toolName, description, schema);
            }
        }

        LOG.info("Registered {} dynamic MCP tools from grid algorithms", tools.size());
    }

    public void registerTool(String name, String description, String jsonSchema) {
        tools.put(name, new ToolDefinition(name, description, jsonSchema));
    }

    public ToolDefinition getTool(String name) {
        return tools.get(name);
    }
    
    public Map<String, ToolDefinition> getAllTools() {
        return tools;
    }

    public record ToolDefinition(String name, String description, String inputSchema) {}
}
