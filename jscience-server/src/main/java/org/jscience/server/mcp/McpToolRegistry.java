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
 * Scans for services annotated with @McpTool and registers them.
 */
@Service
public class McpToolRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(McpToolRegistry.class);
    private final Map<String, ToolDefinition> tools = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // In a real implementation, we would scan the BeanContext
        // For this prototype, we manually register core tools
        registerTool("convert_units", "Convert scientific units", 
            "{\"value\": \"number\", \"from\": \"string\", \"to\": \"string\"}");
        registerTool("balance_reaction", "Balance chemical equation",
            "{\"equation\": \"string\"}");
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
