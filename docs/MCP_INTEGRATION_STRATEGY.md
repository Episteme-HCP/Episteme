# Episteme MCP Integration Strategy

This document outlines the vision and technical roadmap for making Episteme the computational brain for LLMs via the **Model Context Protocol (MCP)**.

## Vision: The XML Revolution (2025)

The future of science-AI interaction is not just text, but **semantic structure**. We are moving away from LLMs "guessing" math and science toward LLMs "instructing" a scientific kernel.

### Semantic Standards Integration
LLMs will be empowered to read and write science using established XML standards:
1.  **MathML (Content)**: Ensuring high-fidelity mathematical reasoning.
2.  **CML (Chemical Markup Language)**: Reviving the legacy standard for molecular AI.
3.  **SBML (Systems Biology ML)**: Mapping metabolic and regulatory pathways.
4.  **ThermoML**: Standardizing thermodynamic data exchange.
5.  **GML (Geography ML)**: Enabling spatial reasoning for GIS applications.

## Technical Mapping

### 🛠️ Scientific Tools
We map Episteme core libraries to MCP tools:
- **`episteme-core:measure`** → `convert_units`: Bridged via `Units` and `UnitConverter`.
- **`episteme-core:mathematics`** → `calculate_matrix`: Bridged via `LinearAlgebraProvider` and `MatrixService`.
- **`episteme-core:technical`** → Dynamic `AlgorithmProvider` tools: Discovered via Spring SPI and exposed automatically.

### 📡 Resources
Episteme provides "Virtual Scientific Resources" via the `episteme://` URI scheme:
- `episteme://models/*`: Real-time state of active simulations.
- `episteme://data/*`: Access to HDF5 and high-performance datasets.

### 💡 Prompts
Standardized workflows for scientific analysis:
- `analyze_simulation`: Pre-configured prompts for statistical and stability analysis.

## Security Roadmap
1.  **Transport Security**: Currently SSE over HTTP. Future support for WebSockets and TLS is planned.
2.  **Authentication**: Integrating MCP with the existing `AuthService` (JWT/OIDC).
3.  **Governance**: Rate limiting and task quotas for computational-heavy MCP tool calls.

## Implementation Status
- **Handshake**: ✅ Compliant with 2024-11-05 protocol version.
- **Tools**: ✅ Bridged to core libraries.
- **Roadmap**: 🏗️ Active development on XML semantic layers.
