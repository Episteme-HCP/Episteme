# JScience API Reference (v1.0.0)

## Overview
JScience is divided into three main modules:
- **jscience-core**: The foundation engine containing AI, Math, and HPC primitives.
- **jscience-natural**: Domain-specific libraries for Biology, Physics, Chemistry, and Geology.
- **jscience-social**: Libraries for Economics, Sociology, and Political Science.

---

## Core Engine (`jscience-core`)

### 🤖 Agents & Multi-Agent Systems
**Package:** `org.jscience.core.computing.ai.agents`
The MAS framework supports BDI (Belief-Desire-Intention) agents, swarms, and LLM-backed agents.

- **`Agent`**: The root interface for all autonomous entities.
- **`Behavior`**: Defines a unit of work or logic cycle for an agent.
- **`Environment`**: The shared space where agents perceive and act.
- **`ACLMessage`**: FIPA-compliant Agent Communication Language message.
- **`YellowPages`**: Directory Facilitator for service discovery.

### 🧠 Deep Learning & Tensors
**Package:** `org.jscience.core.mathematics.ml.neural`
A fully differentiable Tensor library with Autograd support (reverse-mode AD).

- **`Tensor<T>`**: N-dimensional array supporting operations on any Field.
- **`Variable<T>`**: A wrapper around Tensor that tracks gradients for backpropagation.
- **`ComputationGraph`**: Manages the forward and backward passes.
- **`Layer`**: Base class for neural layers (`Linear`, `Conv2D`, `LSTM`).
- **`Optimizer`**: Parameter update strategies (`SGD`, `Adam`).

### 👁️ Computer Vision
**Package:** `org.jscience.core.media.vision`
GPU-accelerated image processing pipeline.

- **`ImageOp`**: Functional interface for image transformations.
- **`FastImageOps`**: Collection of specific GPU/Native optimized kernels.
- **`VisionProvider`**: SPI for backend implementations (OpenCL, CUDA, Native).

### 🐝 Swarm Intelligence
**Package:** `org.jscience.core.mathematics.optimization.swarm`
- **`ParticleSwarmOptimization (PSO)`**: Logic for continuous optimization problems.
- **`AntColonyOptimization (ACO)`**: Logic for graph traversal and pathfinding problems.

---

## Natural Sciences (`jscience-natural`)

### 🧬 Biology
**Package:** `org.jscience.natural.biology`
- **`DNA` / `RNA`**: Sequence handling and motifs.
- **`GeneticAlgorithm`**: Evolutionary computation framework.

### ⚛️ Physics & Chemistry
**Package:** `org.jscience.natural.physics`, `org.jscience.natural.chemistry`
- **`PeriodicTable`**: Access to Element properties.
- **`MolecularGraph`**: Graph-based representation of molecules.

---

## Social Sciences (`jscience-social`)

### 📈 Economics
**Package:** `org.jscience.social.economics`
- **`LeontiefModel`**: Input-Output economic modeling.
- **`Market`**: Simulation of supply/demand dynamics.

### 🗳️ Politics
**Package:** `org.jscience.social.politics`
- **`Election`**: Voting systems (Ranked Choice, First-Past-Post).
- **`GerrymanderingDetector`**: Statistical analysis of district maps.

---
*For detailed method signatures, please refer to the auto-generated Javadoc.*
