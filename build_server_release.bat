@echo off
echo ==========================================
echo Building Episteme gRPC Server Release
echo ==========================================

REM Check if Maven is available
call mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH.
    pause
    exit /b 1
)

echo [INFO] Cleaning and compiling episteme-server...
call mvn clean package -pl episteme-server -DskipTests

if %errorlevel% neq 0 (
    echo [ERROR] Maven build failed.
    pause
    exit /b 1
)

echo [INFO] Organizing artifacts...
if not exist dist mkdir dist

REM Detect the JAR with the exec classifier
for %%f in (episteme-server\target\*-exec.jar) do (
    echo [INFO] Found executable JAR: %%f
    copy "%%f" dist\episteme-server.jar /Y
)

echo [SUCCESS] Episteme Server is ready in dist\episteme-server.jar
pause
