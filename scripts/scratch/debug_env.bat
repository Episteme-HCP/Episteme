@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot"
set "VALID_JAVA=0"

if not "%JAVA_HOME%"=="" (
    set "JAVA_HOME=%JAVA_HOME:"=%"
    echo [DEBUG] JAVA_HOME is %JAVA_HOME%
    
    if exist "%JAVA_HOME%\bin\java.exe" (
        echo [DEBUG] Executing java -version...
        "%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr "21 25" >nul && (
            set "VALID_JAVA=1"
            echo [INFO] Compatible version found.
        )
    )
)

echo [DEBUG] VALID_JAVA is %VALID_JAVA%
pause
