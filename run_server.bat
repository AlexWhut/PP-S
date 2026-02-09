@echo off
REM Script para ejecutar el HTTP Server con credenciales de Gmail
REM 
REM IMPORTANTE: Reemplaza estos valores con tus credenciales reales
REM - EMAIL: Tu dirección de Gmail completa (ejemplo: usuario@gmail.com)
REM - APP_PASSWORD: Tu contraseña de aplicación de Google (16 caracteres sin espacios)
REM
REM Para crear una App Password:
REM 1. Ve a https://myaccount.google.com/security
REM 2. Habilita verificación en 2 pasos si no la tienes
REM 3. Ve a "Contraseñas de aplicaciones"
REM 4. Genera una nueva para "Correo"
REM 5. Copia el código de 16 caracteres

set EMAIL=tu-email@gmail.com
set APP_PASSWORD=abcdefghijklmnop

echo ============================================================
echo HTTP Server para Gmail - Configuracion
echo ============================================================
echo.
echo Email configurado: %EMAIL%
echo App Password: %APP_PASSWORD%
echo.

if "%EMAIL%"=="tu-email@gmail.com" (
    echo ERROR: Debes configurar tu email real en este script
    echo.
    echo Edita este archivo run_server.bat y cambia:
    echo   set EMAIL=tu-email@gmail.com
    echo Por:
    echo   set EMAIL=tuemail@gmail.com
    echo.
    pause
    exit /b 1
)

echo Iniciando servidor HTTP en puerto 8080...
echo.

cd ClassExercises\bin
java -cp ".;../../lib/javax.mail-1.6.2.jar" GmailHttpServer.HTTPServer %EMAIL% %APP_PASSWORD%
