# Deployment Guide: Hugging Face Spaces (MCP Server)

This document explains the industrial deployment process for the Episteme MCP Server on Hugging Face Spaces.

## Overview

The full Episteme repository is over 3.5GB due to large datasets, 3D models, and binaries. Hugging Face Spaces has a **1GB repository limit**. To bypass this, we use a "Lean Deployment" strategy.

## Deployment Strategy: "Lean Build"

We use a specialized deployment script (`deploy_hf.ps1` or `deploy_hf.sh`) that isolates only the modules required for the MCP server:

- **Essential Modules**: `episteme-core`, `episteme-server`, `episteme-natural`, `episteme-social`, `episteme-native`.
- **Infrastructure**: Root `pom.xml`, `docker/Dockerfile.huggingface`.
- **Exclusions**: `episteme-benchmarks`, `episteme-dashboard`, `episteme-resources`, `libs/` (except `mpj.jar`), `launchers/`.

## How to Deploy

### Prerequisites
1. Ensure you have the `hf` remote configured:
   ```bash
   git remote add hf https://huggingface.co/spaces/silveremartin/Episteme
   ```
2. Ensure you have a Hugging Face Token configured in your environment or Git credentials.

### Run Deployment

**On Windows (PowerShell):**
```powershell
.\deploy_hf.ps1
```

**On Linux/macOS (Bash):**
```bash
./deploy_hf.sh
```

### What the script does:
1. **Syncs with GitHub**: Automatically pushes your current branch to `origin/main`.
2. **Creates Orphan Branch**: Creates a temporary `hf-deployment` branch with no history to save space.
3. **Sparse Add**: Only adds the required modules to the index.
4. **Force Push**: Pushes the lean branch to Hugging Face's `main` branch.
5. **Auto-Cleanup**: Returns you to your working branch and deletes the temporary deployment branch.

## Monitoring

Once the push is complete, monitor the build logs on Hugging Face:
[Episteme Space Dashboard](https://huggingface.co/spaces/silveremartin/Episteme)

The Space is configured to use the Docker SDK with the file located at `docker/Dockerfile.huggingface`.
