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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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

    @PostConstruct
    public void init() {
        // In a real implementation, we would scan the BeanContext
        // For this prototype, we manually register core tools
        registerTool("convert_units", "Convert scientific units", 
            "{\"properties\": {\"value\": {\"type\": \"number\"}, \"from\": {\"type\": \"string\"}, \"to\": {\"type\": \"string\"}}, \"required\": [\"value\", \"from\", \"to\"]}");
        registerTool("get_constant", "Retrieve scientific constants (e.g., PI, SPEED_OF_LIGHT, EARTH_MASS)",
            "{\"properties\": {\"category\": {\"type\": \"string\", \"enum\": [\"MATH\", \"PHYSICS\", \"EARTH\", \"GEOGRAPHY\", \"HISTORY\"]}, \"name\": {\"type\": \"string\"}}, \"required\": [\"name\"]}");
        registerTool("balance_reaction", "Balance chemical equation",
            "{\"properties\": {\"equation\": {\"type\": \"string\"}}, \"required\": [\"equation\"]}");
        registerTool("get_data_model", "Retrieve a structured scientific data model by name (e.g., 'Global Migration Spatial Dataset')",
            "{\"properties\": {\"name\": {\"type\": \"string\"}}, \"required\": [\"name\"]}");


        LOG.info("Registered {} MCP tools", tools.size());
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
