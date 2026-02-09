@echo off
REM Script para ejecutar el HTTP Client

echo ============================================================
echo HTTP Client para Gmail - Conectando al servidor
echo ============================================================
echo.
echo Asegurate de que el servidor HTTP este ejecutandose
echo en otro terminal antes de ejecutar este cliente.
echo.
echo Presiona cualquier tecla para continuar...
pause > nul

cd ClassExercises\bin
java -cp ".;../../lib/javax.mail-1.6.2.jar" GmailHttpServer.HTTPClient

echo.
echo ============================================================
echo Cliente finalizado
echo ============================================================
echo.
echo Si se genero el archivo gmail_response.html, puedes abrirlo
echo con tu navegador web para ver los mensajes formateados.
echo.
pause
