import json
import sys
import os
import traceback

# Optional: Add local paths or grid-specific imports
# sys.path.append(...)

def handle_exec_qasm(params):
    try:
        try:
            from qiskit_aer import AerSimulator
            backend = AerSimulator()
        except ImportError:
            from qiskit import Aer
            backend_name = params.get("backend", "qasm_simulator")
            backend = Aer.get_backend(backend_name)
    except ImportError:
        return {"status": "ERROR", "message": "Qiskit or Qiskit Aer not installed in the worker environment."}
    
    qasm = params.get("qasm")
    shots = params.get("shots", 1024)
    
    from qiskit import QuantumCircuit, transpile
    qc = QuantumCircuit.from_qasm_str(qasm)
    
    # Transpile the circuit for the backend
    transpiled_qc = transpile(qc, backend)
    job = backend.run(transpiled_qc, shots=shots)
    res = job.result()
    
    return {
        "status": "SUCCESS",
        "counts": res.get_counts(),
        "time": res.time_taken
    }

def main():
    print(json.dumps({"status": "READY", "pid": os.getpid()}))
    sys.stdout.flush()
    
    while True:
        line = sys.stdin.readline()
        if not line:
            break
        
        try:
            request = json.loads(line)
            cmd = request.get("command")
            
            if cmd == "exec_qasm":
                result = handle_exec_qasm(request.get("params", {}))
                print(json.dumps(result))
            elif cmd == "ping":
                print(json.dumps({"status": "PONG"}))
            elif cmd == "exit":
                break
            else:
                print(json.dumps({"status": "ERROR", "message": f"Unknown command: {cmd}"}))
        except Exception as e:
            print(json.dumps({"status": "ERROR", "message": str(e), "trace": traceback.format_exc()}))
            
        sys.stdout.flush()

if __name__ == "__main__":
    main()
