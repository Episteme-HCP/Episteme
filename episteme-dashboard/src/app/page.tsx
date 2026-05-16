'use client'

import { useState, useEffect } from 'react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts'

interface GridStatus {
    activeWorkers: number
    queuedTasks: number
    completedTasks: number
    failedTasks: number
    systemLoad: number
    avgLatency: number
    jvmMemoryUsage: number
    lastMatrixOpTime: number
}

interface Worker {
    id: string
    hostname: string
    status: 'active' | 'idle' | 'dead'
    tasks: number
    cpu: number
    memory: number
}

interface Task {
    id: string
    name: string
    status: 'running' | 'completed' | 'failed' | 'queued'
    progress: number
    startTime: string
}

export default function Dashboard() {
    const [status, setStatus] = useState<GridStatus>({
        activeWorkers: 5,
        queuedTasks: 47,
        completedTasks: 1234,
        failedTasks: 12,
        systemLoad: 67.5,
        avgLatency: 12.4,
        jvmMemoryUsage: 342,
        lastMatrixOpTime: 4.2
    })

    const [workers, setWorkers] = useState<Worker[]>([
        { id: 'w1', hostname: 'worker-1.local', status: 'active', tasks: 3, cpu: 85, memory: 62 },
        { id: 'w2', hostname: 'worker-2.local', status: 'active', tasks: 2, cpu: 72, memory: 54 },
        { id: 'w3', hostname: 'worker-3.local', status: 'idle', tasks: 0, cpu: 5, memory: 31 },
        { id: 'w4', hostname: 'worker-4.local', status: 'active', tasks: 4, cpu: 91, memory: 78 },
        { id: 'w5', hostname: 'gpu-node-1', status: 'active', tasks: 1, cpu: 45, memory: 88 },
    ])

    const [tasks, setTasks] = useState<Task[]>([
        { id: 't1', name: 'Mandelbrot Render', status: 'running', progress: 67, startTime: '2 min ago' },
        { id: 't2', name: 'N-Body Simulation', status: 'running', progress: 34, startTime: '5 min ago' },
        { id: 't3', name: 'ML Training Job', status: 'queued', progress: 0, startTime: '-- ' },
        { id: 't4', name: 'Data Pipeline', status: 'completed', progress: 100, startTime: '10 min ago' },
        { id: 't5', name: 'Monte Carlo Sim', status: 'failed', progress: 23, startTime: '15 min ago' },
    ])

    const [chartData, setChartData] = useState(
        Array.from({ length: 20 }, (_, i) => ({
            time: `${20 - i}m`,
            tasks: Math.floor(Math.random() * 50) + 20,
            workers: Math.floor(Math.random() * 3) + 3,
        }))
    )

    // Simulate real-time updates
    useEffect(() => {
        const interval = setInterval(() => {
            setStatus(prev => ({
                ...prev,
                queuedTasks: Math.max(0, prev.queuedTasks + Math.floor(Math.random() * 5) - 2),
                completedTasks: prev.completedTasks + Math.floor(Math.random() * 3),
                systemLoad: Math.min(100, Math.max(0, prev.systemLoad + (Math.random() * 10 - 5))),
                avgLatency: 10 + Math.random() * 5,
                jvmMemoryUsage: 300 + Math.random() * 50,
                lastMatrixOpTime: 3 + Math.random() * 2
            }))

            setChartData(prev => {
                const newData = [...prev.slice(1), {
                    time: 'now',
                    tasks: Math.floor(Math.random() * 50) + 20,
                    workers: Math.floor(Math.random() * 3) + 3,
                }]
                return newData
            })
        }, 3000)

        return () => clearInterval(interval)
    }, [])

    const handleSubmitTask = (e: React.FormEvent) => {
        e.preventDefault()
        const newTask: Task = {
            id: `t${Date.now()}`,
            name: 'New Task',
            status: 'queued',
            progress: 0,
            startTime: 'just now'
        }
        setTasks(prev => [newTask, ...prev])
        setStatus(prev => ({ ...prev, queuedTasks: prev.queuedTasks + 1 }))
    }

    return (
        <div className="dashboard">
            <header className="header">
                <div className="title-area">
                    <h1>⚛️ Episteme Kernel</h1>
                    <p className="subtitle">A bare-metal Java 21 scientific computing kernel with native MCP integration</p>
                </div>
                <div className="status">
                    <span className="pulse"></span> LIVE_KERNEL_REPORTS • {new Date().toLocaleTimeString()}
                </div>
            </header>

            {/* Stats Cards */}
            <div className="stats-grid">
                <div className="stat-card technical">
                    <div className="label">AVG LATENCY</div>
                    <div className="value">{status.avgLatency.toFixed(2)} ms</div>
                    <div className="change">P95 Stable</div>
                </div>
                <div className="stat-card technical">
                    <div className="label">JVM MEMORY</div>
                    <div className="value">{status.jvmMemoryUsage.toFixed(0)} MB</div>
                    <div className="change">MaxDirectMemory 512M</div>
                </div>
                <div className="stat-card technical">
                    <div className="label">LAST MATRIX OP</div>
                    <div className="value">{status.lastMatrixOpTime.toFixed(2)} ms</div>
                    <div className="change">SIMD/FFM Accelerated</div>
                </div>
                <div className="stat-card technical">
                    <div className="label">SYSTEM LOAD</div>
                    <div className="value">{status.systemLoad.toFixed(1)}%</div>
                    <div className="change">Non-blocking SSE</div>
                </div>
            </div>

            {/* Panels */}
            <div className="panels-grid">
                {/* Workers Panel */}
                <div className="panel">
                    <div className="panel-header">
                        <h2>🖥️ Workers</h2>
                        <span className="badge">{workers.filter(w => w.status === 'active').length} active</span>
                    </div>
                    <div className="workers-list">
                        {workers.map(worker => (
                            <div key={worker.id} className="worker-item">
                                <div className="worker-info">
                                    <div className={`worker-status ${worker.status}`}></div>
                                    <div>
                                        <div className="worker-name">{worker.hostname}</div>
                                        <div className="worker-tasks">{worker.tasks} tasks • CPU {worker.cpu}%</div>
                                    </div>
                                </div>
                                <div className="worker-tasks">{worker.memory}% mem</div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Tasks Panel */}
                <div className="panel">
                    <div className="panel-header">
                        <h2>📋 Recent Tasks</h2>
                        <span className="badge">{tasks.filter(t => t.status === 'running').length} running</span>
                    </div>
                    <table className="tasks-table">
                        <thead>
                            <tr>
                                <th>Task</th>
                                <th>Status</th>
                                <th>Progress</th>
                                <th>Started</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tasks.map(task => (
                                <tr key={task.id}>
                                    <td>{task.name}</td>
                                    <td><span className={`task-status ${task.status}`}>{task.status}</span></td>
                                    <td>{task.progress}%</td>
                                    <td>{task.startTime}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Chart Panel */}
                <div className="panel">
                    <div className="panel-header">
                        <h2>📊 Task Throughput</h2>
                        <span className="badge">Last 20 minutes</span>
                    </div>
                    <div className="chart-container">
                        <ResponsiveContainer width="100%" height="100%">
                            <AreaChart data={chartData}>
                                <defs>
                                    <linearGradient id="colorTasks" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="#6366f1" stopOpacity={0.3} />
                                        <stop offset="95%" stopColor="#6366f1" stopOpacity={0} />
                                    </linearGradient>
                                </defs>
                                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                                <XAxis dataKey="time" stroke="#94a3b8" fontSize={12} />
                                <YAxis stroke="#94a3b8" fontSize={12} />
                                <Tooltip
                                    contentStyle={{
                                        background: '#1a1a25',
                                        border: '1px solid rgba(255,255,255,0.1)',
                                        borderRadius: '8px'
                                    }}
                                />
                                <Area type="monotone" dataKey="tasks" stroke="#6366f1" fill="url(#colorTasks)" strokeWidth={2} />
                            </AreaChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                {/* Claude Desktop Config Panel */}
                <div className="panel code-panel">
                    <div className="panel-header">
                        <h2>🔌 Proof by Code: Connect to Claude</h2>
                    </div>
                    <div className="code-block">
                        <pre>
{`{
  "mcpServers": {
    "episteme": {
      "command": "npx",
      "args": [
        "-y", 
        "@episteme-hcp/mcp-bridge", 
        "--url", 
        "https://episteme-hcp-episteme.hf.space/mcp/sse"
      ]
    }
  }
}`}
                        </pre>
                        <button className="copy-btn">Copy to mcp_config.json</button>
                    </div>
                    <p className="code-caption">Paste this into your Claude Desktop configuration to enable native scientific tools.</p>
                </div>

                {/* Submit Task Panel */}
                <div className="panel">
                    <div className="panel-header">
                        <h2>🚀 Remote Execution Shell</h2>
                    </div>
                    <form className="submit-form" onSubmit={handleSubmitTask}>
                        <div className="form-group">
                            <label>Method</label>
                            <select defaultValue="compute">
                                <option value="compute">execute_simulation</option>
                                <option value="pipeline">read_hdf5_data</option>
                                <option value="ml">convert_units</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Job Priority</label>
                            <select defaultValue="normal">
                                <option value="critical">CRITICAL</option>
                                <option value="high">HIGH</option>
                                <option value="normal">NORMAL</option>
                                <option value="low">LOW</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Payload (JSON)</label>
                            <textarea rows={4} placeholder='{"simulationType": "FLUID", "parameters": {"viscosity": 0.01}}'></textarea>
                        </div>
                        <button type="submit" className="btn">EXECUTE_RPC</button>
                    </form>
                </div>
            </div>
        </div>
    )
}
