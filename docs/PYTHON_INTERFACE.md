# Episteme Python Interface

**Bridging the power of the Episteme Java Grid with the flexibility of the Python scientific ecosystem.**

## Overview

The Episteme Python interface provides a gRPC-based bridge that allows data scientists and researchers to interact with the Episteme Grid infrastructure directly from Python scripts and Jupyter Notebooks.

```python
from episteme import EpistemeClient

# Connect to the distributed Episteme Grid
grid = EpistemeClient(host='grid.episteme.org')

# Submit a heavy computation (e.g., DNA folding or Quantum simulation)
task_id = grid.submit_task('dna_folding', {'sequence': 'ACGT...'})

# Result is processed in Java on the high-performance cluster
result = grid.wait_for_result(task_id)
```

## Why a Python Interface?

1. **Ecosystem Interoperability**: Leverage Python's rich data science libraries (Pandas, NumPy, Matplotlib, Scikit-learn) while Episteme handles the high-performance physics, chemistry, and mathematics computations.
2. **Jupyter Integration**: Episteme is accessible in the standard environment for scientific exploration, facilitating reproducible research.
3. **Distributed Scaling**: Offload computationally intensive tasks from a local Python process to a massive distributed Java grid.
4. **Cross-Language Collaboration**: Teams can work in their preferred language while sharing a unified computational backend.

## Architecture

The interface follows a client-server architecture:

- **Backend (Java)**: The Episteme Grid Server exposes a gRPC service defined by Protobuf schemas.
- **Frontend (Python)**: The `episteme-jupyter` module contains the `episteme.py` client library which encapsulates gRPC calls and provides a Pythonic API.

## Installation

```bash
# Requires grpcio and protobuf
pip install grpcio grpcio-tools
```

## Example: N-Body Simulation

```python
import matplotlib.pyplot as plt
from episteme import EpistemeClient

client = EpistemeClient()
client.login('user', 'pass')

# Submitting an N-Body task with 10k particles
params = {
    'particle_count': 10000,
    'time_step': 0.01,
    'duration': 1.0,
    'integrator': 'RungeKutta4'
}

task_id = client.submit_task('nbody_simulation', params)
result = client.wait_for_result(task_id)

# Visualize result in Matplotlib
positions = result['positions']
plt.scatter(positions[:, 0], positions[:, 1])
plt.show()
```

## Status

The Python interface is currently in **v1.0** (Production Ready), matching the Episteme core release.

---
*Episteme - Making scientific computing natural across languages.*
