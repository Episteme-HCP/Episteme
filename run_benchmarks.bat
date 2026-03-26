@echo off
setlocal

rem --- Argument Parsing ---
set APP_CLASS=org.episteme.benchmarks.ui.Launcher
set EXPORT_FILE=
set GENERATE_PDF=false
set EXTRA_ARGS=

:parse_args
if "%~1"=="" goto end_parse
if "%~1"=="--cli" set APP_CLASS=org.episteme.benchmarks.cli.BenchmarkCLI
if "%~1"=="--shaded" set USE_SHADED_JAR=true
if "%~1"=="-jar" set USE_SHADED_JAR=true
if "%~1"=="--pdf" set GENERATE_PDF=true

set "ARG=%~1"
if "%ARG:~0,14%"=="--export-file=" (
    set "EXPORT_FILE=%ARG:~14%"
)
shift
goto parse_args

:end_parse
if "%GENERATE_PDF%"=="true" (
    if "%EXPORT_FILE%"=="" (
        set "EXPORT_FILE=benchmark-results.json"
        set "EXTRA_ARGS=--export-file=benchmark-results.json"
    )
)

set JAR_PATH=episteme-benchmarks\target\episteme-benchmarks-1.0.0-SNAPSHOT.jar
set LIB_DIR=launchers\lib
set DEPENDENCY_DIR=episteme-benchmarks\target\lib
set MODULE_PATH=episteme-benchmarks\target\classes;episteme-core\target\classes;episteme-natural\target\classes;episteme-social\target\classes;episteme-native\target\classes;episteme-client\target\classes;episteme-server\target\classes

rem --- Add Native JARs to Classpath ---
if defined MPJ_HOME (
    set "MPJ_JAR=%MPJ_HOME%\lib\mpj.jar"
) else (
    rem Fallback if MPJ_HOME not set but folder exists
    if exist "C:\Episteme-Native\MPJ\lib\mpj.jar" set "MPJ_JAR=C:\Episteme-Native\MPJ\lib\mpj.jar"
)


echo ==========================================
echo Running Episteme Benchmarks
echo ==========================================

rem --- Environment Setup ---
if exist "launchers\env_setup.bat" (
    call "launchers\env_setup.bat"
) else (
    echo [WARN] env_setup.bat not found, using legacy path logic.
    set "NATIVE_ROOT=C:\Episteme-Native"
    set "LIBS_DIR=%~dp0libs"
    if not defined EPISTEME_PYTHON (
        set "EPISTEME_PYTHON=C:\Users\silve\AppData\Local\Programs\Python\Python314\python.exe"
    )
    if not defined CUDA_PATH (
        set "CUDA_PATH=C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v13.1"
    )
    set "PATH=%CUDA_PATH%\bin;%CUDA_PATH%\libnvvp;%PATH%"
    if exist "%LIBS_DIR%" set "PATH=%LIBS_DIR%;%PATH%"
)

if defined USE_SHADED_JAR (
    echo [INFO] Flag detected: Forcing use of Shaded JAR...
    if not exist "%JAR_PATH%" (
        echo [ERROR] Shaded JAR not found at %JAR_PATH%
        echo [INFO] Please run 'mvn package -DskipTests' first.
        pause
        exit /b 1
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -Djava.library.path="libs/native" -cp "%JAR_PATH%;%DEPENDENCY_DIR%\*" %APP_CLASS% %*
) else (
    echo [INFO] Running latest compiled classes - Dev Mode. Use --shaded to force JAR.
    if not exist "episteme-benchmarks\target\classes" (
        echo [INFO] Classes not found, building module...
        call mvn compile -pl episteme-benchmarks -am -DskipTests
    )
    if not exist "%DEPENDENCY_DIR%" (
        echo [INFO] Dependencies not found in target, copying...
        call mvn dependency:copy-dependencies -pl episteme-benchmarks -DoutputDirectory=target/lib -DincludeScope=runtime -DskipTests
    )
    java --add-modules jdk.incubator.vector --enable-native-access=ALL-UNNAMED -cp "%MODULE_PATH%;%DEPENDENCY_DIR%\*;%LIB_DIR%\*;%MPJ_JAR%" %APP_CLASS% %* %EXTRA_ARGS%
)

)


echo.
pause
endlocal
