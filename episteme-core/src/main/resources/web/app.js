document.addEventListener('DOMContentLoaded', () => {
    const startBtn = document.getElementById('startBtn');
    const stopBtn = document.getElementById('stopBtn');
    const logs = document.getElementById('logs');
    const engineLoad = document.getElementById('engineLoad');
    const dataRate = document.getElementById('dataRate');
    const uptimeLabel = document.getElementById('uptime');

    let chart;
    let ws;
    let startTime = Date.now();

    // Initialize Chart.js
    const ctx = document.getElementById('mainChart').getContext('2d');
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: Array(50).fill(''),
            datasets: [{
                label: 'Scientific Flux',
                data: Array(50).fill(0),
                borderColor: '#4a9eff',
                borderWidth: 2,
                tension: 0.4,
                pointRadius: 0,
                fill: true,
                backgroundColor: 'rgba(74, 158, 255, 0.1)'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)' }
                },
                x: {
                    grid: { display: false }
                }
            },
            plugins: {
                legend: { display: false }
            },
            animation: false
        }
    });

    function addLog(msg, type = 'INFO') {
        const div = document.createElement('div');
        div.textContent = `[${type}] ${msg}`;
        logs.appendChild(div);
        logs.scrollTop = logs.scrollHeight;
    }

    // API interactions
    startBtn.onclick = async () => {
        try {
            const resp = await fetch('/api/simulation/start', { method: 'POST' });
            if (resp.ok) {
                addLog('Simulation started successfully');
            }
        } catch (err) {
            addLog('Failed to start simulation', 'ERROR');
        }
    };

    stopBtn.onclick = async () => {
        try {
            const resp = await fetch('/api/simulation/stop', { method: 'POST' });
            if (resp.ok) {
                addLog('Simulation stopped');
            }
        } catch (err) {
            addLog('Failed to stop simulation', 'ERROR');
        }
    };

    // WebSocket setup
    function connect() {
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        ws = new WebSocket(`${protocol}//${window.location.host}/stream`);

        ws.onopen = () => {
            addLog('Telemetry stream connected');
        };

        ws.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);

                // Update metrics
                if (data.load) engineLoad.textContent = `${data.load.toFixed(1)}%`;
                if (data.rate) dataRate.textContent = data.rate;

                // Update chart if data point exists
                if (data.value !== undefined) {
                    chart.data.datasets[0].data.shift();
                    chart.data.datasets[0].data.push(data.value);
                    chart.update();
                }
            } catch (err) {
                // Not JSON or other error
            }
        };

        ws.onclose = () => {
            addLog('Telemetry stream disconnected. Retrying...', 'WARN');
            setTimeout(connect, 3000);
        };
    }

    // Uptime timer
    setInterval(() => {
        const diff = Date.now() - startTime;
        const h = Math.floor(diff / 3600000).toString().padStart(2, '0');
        const m = Math.floor((diff % 3600000) / 60000).toString().padStart(2, '0');
        const s = Math.floor((diff % 60000) / 1000).toString().padStart(2, '0');
        uptimeLabel.textContent = `${h}:${m}:${s}`;
    }, 1000);

    connect();
});
