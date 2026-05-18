import gradio as gr
import httpx
import json
import os
import pandas as pd
from langchain_openai import ChatOpenAI
from langchain.agents import create_openai_functions_agent
try:
    from langchain.agents import AgentExecutor
except ImportError:
    from langchain_classic.agents import AgentExecutor
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.tools import tool
from dotenv import load_dotenv

load_dotenv()

# Configuration
MCP_SERVER_URL = os.getenv("MCP_SERVER_URL", "http://localhost:8080/mcp/message")
METRICS_URL = os.getenv("METRICS_URL", "http://localhost:8080/mcp/message") # Use JSON-RPC for metrics too

class EpistemeClient:
    def __init__(self, url):
        self.url = url

    def call(self, method, params=None):
        payload = {
            "jsonrpc": "2.0",
            "id": "agent-1",
            "method": method,
            "params": params or {}
        }
        try:
            response = httpx.post(self.url, json=payload, timeout=5.0)
            response.raise_for_status()
            return response.json().get("result", {})
        except Exception as e:
            return {"error": str(e)}

client = EpistemeClient(MCP_SERVER_URL)

# --- Tools ---
@tool
def convert_units(value: float, from_unit: str, to_unit: str):
    """Convert scientific units (e.g., from 'meters' to 'kilometers')."""
    return client.call("tools/call", {"name": "convert_units", "arguments": {"value": value, "from": from_unit, "to": to_unit}})

@tool
def get_constant(name: str):
    """Retrieve scientific constants (e.g., PI, G, SPEED_OF_LIGHT, EARTH_RADIUS)."""
    return client.call("tools/call", {"name": "get_constant", "arguments": {"name": name}})

@tool
def solve_expression(expression: str, guessMin: float, guessMax: float):
    """Find a root for f(x) = 0 using numerical methods (Brent). Useful for trajectories and impact points."""
    return client.call("tools/call", {"name": "solve_expression", "arguments": {"expression": expression, "guessMin": guessMin, "guessMax": guessMax}})

@tool
def execute_simulation(simulationType: str, parameters: dict):
    """Start a scientific simulation task (FLUID, NBODY, SIR, MIGRATION). Returns a taskId."""
    return client.call("tools/call", {"name": "execute_simulation", "arguments": {"simulationType": simulationType, "parameters": parameters}})

@tool
def get_task_status(taskId: str):
    """Check the status and result of a long-running task."""
    return client.call("tools/call", {"name": "get_task_status", "arguments": {"taskId": taskId}})

tools = [convert_units, get_constant, solve_expression, execute_simulation, get_task_status]

# --- Agent ---
llm = ChatOpenAI(model="gpt-4-turbo-preview", temperature=0)
prompt = ChatPromptTemplate.from_messages([
    ("system", "You are Episteme AI, a scientific assistant. Use the provided tools for any math or physics calculation."),
    MessagesPlaceholder(variable_name="chat_history"),
    ("user", "{input}"),
    MessagesPlaceholder(variable_name="agent_scratchpad"),
])
agent = create_openai_functions_agent(llm, tools, prompt)
agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)

# --- UI Functions ---
def get_metrics():
    res = client.call("tools/call", {"name": "get_server_metrics", "arguments": {}})
    content = res.get("content", {})
    if isinstance(content, str):
        try: content = json.loads(content)
        except: return "Error parsing metrics", "N/A", "N/A"
    
    latency = content.get("avg_latency", "0.00")
    memory = content.get("jvm_memory_usage", "0")
    ops = content.get("last_matrix_op_time", "0.00")
    return f"{latency:.2f} ms" if isinstance(latency, float) else f"{latency} ms", \
           f"{memory} MB", \
           f"{ops:.2f} ms" if isinstance(ops, float) else f"{ops} ms"

def chat_fn(message, history):
    chat_history = []
    for h in history:
        chat_history.append(("user", h[0]))
        chat_history.append(("assistant", h[1]))
    response = agent_executor.invoke({"input": message, "chat_history": chat_history})
    return response["output"]

# --- Gradio Interface ---
with gr.Blocks(theme="soft", title="Episteme Scientific Playground") as demo:
    gr.Markdown("# ⚛️ Episteme Scientific Playground")
    gr.Markdown("*Bare-metal Java 21 Kernel + Agentic Orchestration*")
    
    with gr.Tabs():
        with gr.TabItem("🤖 Agentic Assistant"):
            gr.ChatInterface(chat_fn, examples=[
                "Calcule la trajectoire d'un projectile de 10kg lancé à 50m/s avec un angle de 45°.",
                "Quelle est la masse de la Terre et convertis-la en onces ?",
                "Lance une simulation de fluide avec une viscosité de 0.05."
            ])
            
        with gr.TabItem("🖥️ Kernel Monitor"):
            with gr.Row():
                m_lat = gr.Textbox(label="Avg Latency (P95)", value="0.00 ms", interactive=False)
                m_mem = gr.Textbox(label="JVM Off-heap Memory", value="0 MB", interactive=False)
                m_ops = gr.Textbox(label="Last Matrix Op", value="0.00 ms", interactive=False)
            
            btn_refresh = gr.Button("Refresh Metrics")
            btn_refresh.click(get_metrics, inputs=[], outputs=[m_lat, m_mem, m_ops])
            
            gr.Markdown("### 🔌 Claude Desktop Configuration")
            gr.Code(value="""{
  "mcpServers": {
    "episteme": {
      "command": "npx",
      "args": ["-y", "@episteme-hcp/mcp-bridge", "--url", "https://episteme-hcp-episteme.hf.space/mcp/sse"]
    }
  }
}""", language="json")

if __name__ == "__main__":
    demo.launch(server_name="0.0.0.0", server_port=7860)
