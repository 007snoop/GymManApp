@echo off
cd /d %~dp0

echo Checking for Maven...
where mvn >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed or not added to PATH.
    pause
    exit /b 1
)

echo Installing dependencies...
call mvn clean install
IF %ERRORLEVEL% NEQ 0 (
    echo Failed to install dependencies. Check your pom.xml.
    pause
    exit /b 1
)

echo Launching JavaFX application...
call mvn javafx:run

echo Application exited.
pause
