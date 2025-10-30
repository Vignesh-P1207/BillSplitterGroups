@echo off
cls
echo ============================================================
echo       🚀 Bill Splitter - Java GUI + JDBC Project Runner
echo ============================================================

REM Set up paths
set PROJECT_DIR=C:\BillSplitterProject
set SRC=%PROJECT_DIR%\src
set BIN=%PROJECT_DIR%\bin
set LIB=%PROJECT_DIR%\lib\mysql-connector-j-9.4.0.jar

REM Create bin folder if missing
if not exist "%BIN%" mkdir "%BIN%"

echo.
echo [1] Compiling all Java files...
echo.

javac -cp "%LIB%" -d "%BIN%" %SRC%\models\*.java %SRC%\db\*.java %SRC%\gui\*.java %SRC%\Main.java

if %ERRORLEVEL% neq 0 (
    echo ❌ Compilation failed! Check the errors above.
    pause
    exit /b
)

echo.
echo ✅ Compilation successful!
echo.
echo [2] Launching the Bill Splitter app...
echo.

java -cp "%BIN%;%LIB%" Main

echo.
pause
