import sys
import json
import os

def run_circuit(qasm_file):
    try:
        from qiskit import QuantumCircuit, transpile
        from qiskit_aer import AerSimulator
    except ImportError:
        print("Error: Qiskit or Qiskit Aer not installed. Run 'pip install qiskit qiskit-aer'")
        return

    try:
        if not os.path.exists(qasm_file):
            print(f"Error: File {qasm_file} not found")
            return

        qc = QuantumCircuit.from_qasm_file(qasm_file)
        
        sim = AerSimulator()
        # Compile the circuit for the simulator
        transpiled_qc = transpile(qc, sim)
        
        # Run and get counts
        result = sim.run(transpiled_qc, shots=1024).result()
        counts = result.get_counts(transpiled_qc)
        
        print(json.dumps(counts))

    except Exception as e:
        print(f"Error executing circuit: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python qiskit_runner.py <qasm_file>")
    else:
        run_circuit(sys.argv[1])
