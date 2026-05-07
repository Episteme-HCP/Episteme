# Episteme MCP Deployment Guide

This guide explains how to deploy your industrial-grade Episteme MCP server for free and integrate it with modern AI workflows.

## 1. Deployment on Hugging Face Spaces (FREE)

Hugging Face Spaces is the ideal platform for hosting the Episteme MCP server due to its native Docker support and free tier.

### Steps:
1.  **Create a New Space**:
    *   Go to [huggingface.co/new-space](https://huggingface.co/new-space).
    *   Select **Docker** as the Space SDK.
    *   Choose a template (or start blank).
2.  **Upload the Code**:
    *   Push the Episteme repository to the Space's Git remote.
    *   Ensure the `docker/Dockerfile.huggingface` is used (you may need to rename it to `Dockerfile` at the root for HF to pick it up automatically, or specify the path in the Space settings).
3.  **Configure Environment**:
    *   If you want to secure your MCP server, add an Environment Variable: `EPISTEME_MCP_API_KEY`.
4.  **Wait for Build**: Hugging Face will automatically build the image and start the container.
5.  **Access URL**: Your server will be available at `https://<your-username>-<space-name>.hf.space`.

## 2. Integration with N8N

N8N can interact with Episteme to perform complex scientific workflows.

### Connection Steps:
1.  **HTTP Request Node**:
    *   **Method**: POST
    *   **URL**: `https://<your-space>.hf.space/mcp/sse`
    *   **Headers**: 
        *   `Content-Type: application/json`
        *   `X-API-Key: <your-key>` (if configured)
2.  **Payload**: Send standard JSON-RPC 2.0 requests as defined in the Episteme documentation.

## 3. Integration with Google Vertex AI

To use Episteme as a tool for Gemini in Vertex AI:

1.  **Define Extension**: Create an `openapi.yaml` describing the MCP endpoints.
2.  **Upload to Vertex AI**: Register the extension in the Google Cloud Console.
3.  **Call Tools**: Gemini can now call `calculate_matrix`, `solve_expression`, etc., by routing through your Hugging Face Space.

## 4. Industrial Features Included

*   **Observability**: Integrated with Micrometer. Metrics are available at `/actuator/prometheus`.
*   **Performance Logs**: Every tool execution is logged with its duration in ms.
*   **Vector API**: Pre-configured to use `jdk.incubator.vector` for high-performance math on compatible hardware.

---
**Author**: Antigravity AI & Silvere Martin-Michiellot
**Version**: 1.0.0-beta2
