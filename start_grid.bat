@echo off

rem --- Native Libraries Setup ---
if exist "C:\Program Files\VideoLAN\VLC" (
    echo [INFO] Adding VLC to PATH...
    set "PATH=C:\Program Files\VideoLAN\VLC;%PATH%"
    set "VLC_PLUGIN_PATH=C:\Program Files\VideoLAN\VLC\plugins"
)
if exist "%~dp0libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0libs;%PATH%"
)
if exist "%~dp0..\libs" (
    echo [INFO] Adding libs/ to PATH...
    set "PATH=%~dp0..\libs;%PATH%"
)

REM Start Episteme Grid with Docker Compose
echo Starting Episteme Grid...
echo.
echo This will start:
echo   - 1x Episteme Server (port 50051)
echo   - 5x Episteme Workers
echo   - Prometheus (port 9090)
echo   - Grafana (port 3000)
echo.

docker-compose up -d --build

echo.
echo Grid is starting! Check status with: docker-compose ps
echo.
echo Grafana Dashboard: http://localhost:3000 (admin/episteme)
echo Prometheus:        http://localhost:9090
echo gRPC Server:       localhost:50051
