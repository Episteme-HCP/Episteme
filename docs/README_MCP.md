# Episteme MCP Integration - Scientific Kernel for AI

This module implements the **Model Context Protocol (MCP)**, allowing Large Language Models (LLMs) to use Episteme as a computational brain.

## ⚠️ Status: Active Development

- **Endpoint**: `/mcp/sse` (Server-Sent Events)
- **Protocol**: JSON-RPC 2.0
- **Port**: 8080 (REST)

## 📡 Usage

Connect your MCP Client (e.g. Claude Desktop, Gemini) to:
```
http://localhost:8080/mcp/sse
```

## 📚 Roadmap: The XML Revolution (2025)

The goal is to implement semantic XML standards to allow AI to "read" science directly.

### To Be Implemented:
1.  **MathML (Content)**: For semantic mathematical expressions.
2.  **CML (Chemical Markup Language)**: For molecular structures (Legacy revival).
3.  **SBML (Systems Biology ML)**: For metabolic pathways.
4.  **ThermoML**: For thermodynamic data.
5.  **GML (Geography ML)**: For advanced GIS.
6.  **AnIML**: For analytical data.

## 🛠️ MCP Tools Available (Prototype)

- `convert_units(value, from, to)`
- `balance_reaction(equation)`
- `calculate_matrix(A, B, op)`

## 🏗️ Architecture

See `MCP_INTEGRATION_STRATEGY.md` for the full design.
