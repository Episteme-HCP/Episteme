@echo off
REM JScience Spin Valve Simulator Launcher - Windows
REM Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)

setlocal

REM Find Java
if defined JAVA_HOME (
    set JAVA="%JAVA_HOME%\bin\java"
) else (
    set JAVA=java
)

REM Set classpath
set SCRIPT_DIR=%~dp0
set LIB_DIR=%SCRIPT_DIR%..\lib
set CP=%SCRIPT_DIR%..\jscience-featured-apps-1.0-SNAPSHOT.jar

REM Add all JARs in lib directory
for %%i in ("%LIB_DIR%\*.jar") do call :addcp "%%i"

REM JavaFX modules
set JFX_MODULES=javafx.controls,javafx.fxml,javafx.graphics,javafx.swing

REM Launch application
echo Starting JScience Spin Valve Simulator...
%JAVA% --module-path "%LIB_DIR%" --add-modules %JFX_MODULES% -cp "%CP%" org.jscience.apps.physics.spintronics.SpinValveApp %*
goto :eof

:addcp
set CP=%CP%;%~1
goto :eof
