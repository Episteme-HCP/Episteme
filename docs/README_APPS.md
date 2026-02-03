# JScience Killer Apps

Professional-grade scientific applications built with JavaFX and the JScience library.

## 🔬 Pandemic Forecaster

**Simulate global epidemics with real-world data.**

- **Data**: Integrated World Bank demographic data for 200+ countries.
- **Model**: SEIR (Susceptible-Exposed-Infectious-Recovered) with configurable R₀.
- **Features**:
  - Interactive world map selection.
  - Real-time epidemic curve visualization.
  - Sliders for transmission ($\beta$), incubation ($\sigma$), and recovery ($\gamma$) rates.
  - Live R₀ calculation and color-coded risk assessment.
  - Export simulation results to CSV.

## 📉 Market Crash Predictor

**Analyze financial markets and detect crash risks.**

- **Analysis**: Technical indicators (SMA, Bollinger Bands, RSI, Volatility).
- **Features**:
  - Interactive price charts with zoom.
  - Multi-indicator overlays (Upper/Lower Bollinger Bands).
  - Dedicated RSI sub-chart with overbought/oversold alerts.
  - **Risk Assessment Engine**: Real-time "traffic light" risk monitoring (Low/Moderate/High/Extreme).
  - Historical playback simulation.

## ⚛️ Quantum Circuit Simulator

**Design and execute quantum algorithms.**

- **Circuit Builder**: Drag-and-drop interface for 4 qubits and 12 gate steps.
- **Gates**: H, X, Y, Z, S, T, CNOT.
- **Visualization**:
  - **Bloch Sphere**: 3D representation of single-qubit states.
  - **State Vector**: Complex amplitude display ($|\psi\rangle = \alpha|0\rangle + \beta|1\rangle$).
  - **Measurement Probabilities**: Real-time histogram of outcomes.

## 🚀 Planetary Mission Planner

**Design interplanetary transfers across the Solar System.**

- **Data**: Accurate planetary orbital elements (JSON loaded).
- **Features**:
  - **Visualization**: 2D Top-down view of the Solar System with orbital paths.
  - **Trajectory**: Calculates Hohmann-like transfer windows.
  - **Analysis**: Computes $\Delta V$ requirements and flight duration.
  - **Controls**: Interactive launch date and target selection.

## ⚡ Smart Grid Balancer

**Simulate city-scale power distribution.**

- **Simulation**: Real-time power supply vs demand balancing loop (50Hz physics).
- **Features**:
  - **Grid Map**: Visualizes connections between Power Plant, Renewables, and City.
  - **Control Room**: Adjust Coal output, Wind/Solar capacity, and city demand scaling.
  - **Real-time Charts**: Supply/Demand area chart and Frequency monitoring.
  - **Stability Physics**: Frequency deviations based on inertia and power imbalance.
  - **Blackout Simulation**: Tripping mechanism if frequency deviates too far (±2Hz).

## 🚀 How to Run

### Windows (.bat)

Double-click the launchers in the `launchers` directory:

- `run-pandemic.bat`
- `run-market.bat`
- `run-quantum.bat`
- `run-trajectory.bat`
- `run-grid.bat`

### Linux/macOS (.sh)

Run from terminal:

```bash
./launchers/run-pandemic.sh
./launchers/run-market.sh
./launchers/run-quantum.sh
./launchers/run-trajectory.sh
./launchers/run-grid.sh
```
