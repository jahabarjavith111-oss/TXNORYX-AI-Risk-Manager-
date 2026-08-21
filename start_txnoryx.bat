@echo off
title TXNORYX Transaction Intelligence Engine

echo ==========================================
echo  TXNORYX - Starting Application...
echo ==========================================

echo.
echo [Step 1] Killing any existing processes...
taskkill /F /IM node.exe 2>nul
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo [Step 2] Starting Backend (Spring Boot)...
echo.
cd C:\TXNORYX(JAVID)\backend
mvn spring-boot:run ^
  > backend.log 2>&1 &
  set BACKEND_PID=%%
  echo Backend PID: %BACKEND_PID%
  echo Backend starting on http://localhost:8080
  echo.
  timeout /t 10 /nobreak >nul

echo.
echo [Step 3] Starting Frontend (React + Vite)...
cd C:\TXNORYX(JAVID)\frontend
start "" npm run dev > frontend.log 2>&1
set FRONTEND_PID=%%
echo Frontend PID: %FRONTEND_PID%
echo.
echo ==========================================
echo  Application Starting...
echo ==========================================
echo.
echo  Backend:  http://localhost:8080
echo  Frontend: Check the CMD window for Vite's URL
echo  (typically http://localhost:5173, 5174, 5175, etc.)
echo.
echo  Please note the Vite URL shown above in this window.
echo.
echo [Step 4] Waiting for apps to fully start...
timeout /t 15 /nobreak >nul

echo.
echo ==========================================
echo  TXNORYX is now running!
echo ==========================================
echo.
echo  1. Open your browser
echo  2. Go to the Frontend URL from Step 3 above
echo  3. Click the "Dashboard" button in the navbar
echo.
pause