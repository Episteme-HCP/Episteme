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
        
        # Ensure measurements are present, otherwise AerSimulator won't return counts
        if not qc.cregs:
            qc.measure_all()
        
        sim = AerSimulator()
        
        # Run and get counts
        transpiled_qc = transpile(qc, sim)
        job = sim.run(transpiled_qc, shots=1024)
        result = job.result()
        
        if not result.success:
            print(f"Error: Simulator failed. Status: {result.status}")
            return

        try:
            # Most robust: get counts of the first (and only) experiment
            counts = result.get_counts(0)
        except Exception:
            try:
                counts = result.get_counts()
            except Exception:
                counts = result.get_counts(transpiled_qc)
        
        if not counts:
             raise ValueError(f"Simulator returned empty counts. Status: {result.status}")

        print(json.dumps(counts))

    except Exception as e:
        print(f"Error executing circuit: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python qiskit_runner.py <qasm_file>")
    else:
        run_circuit(sys.argv[1])
