# HTTP Client/Server con Acceso a Gmail - Kudos MH Task

## Descripción del Ejercicio

Este proyecto implementa un **HTTP Client** y un **HTTP Server** que, al recibir una petición HTTP GET, accede a una cuenta de Gmail y devuelve los mensajes no leídos en formato HTML.

## Arquitectura del Sistema

```
┌─────────────┐    HTTP GET      ┌──────────────────┐    IMAP/SSL    ┌─────────┐
│ HTTPClient  │ ─────────────►   │  HTTPServer      │ ◄────────────► │  Gmail  │
│             │                   │  (port 8080)     │                │  IMAP   │
│             │ ◄─────────────    │                  │                │         │
└─────────────┘    HTTP 200      │  + GmailReader   │                └─────────┘
                   (HTML)         └──────────────────┘
```

## Componentes del Proyecto

### 1. **HTTPServer.java**
Servidor HTTP que:
- Escucha en puerto **8080**
- Acepta peticiones HTTP GET en la ruta `/gmail`
- Maneja múltiples clientes concurrentemente usando threads
- Integra con GmailReader para obtener mensajes no leídos
- Devuelve respuesta HTTP con contenido HTML

### 2. **HTTPClient.java**
Cliente HTTP que:
- Se conecta al servidor HTTP en `localhost:8080`
- Envía petición HTTP GET a `/gmail`
- Procesa y muestra la respuesta recibida
- Guarda la respuesta HTML en archivo `gmail_response.html`

### 3. **GmailReader.java**
Componente de acceso a Gmail que:
- Usa **JavaMail API** para conectarse a Gmail via IMAP/SSL
- Se autentica con credenciales de Gmail
- Busca y recupera mensajes no leídos
- Formatea los mensajes en HTML con información de:
  - Asunto
  - Remitente
  - Fecha de envío
- Maneja errores y proporciona mensajes de ayuda

## Dependencias

### JavaMail API
- **javax.mail-1.6.2.jar**: Biblioteca para conectarse a servidores de correo (IMAP, SMTP, POP3)
- Ubicación: `lib/javax.mail-1.6.2.jar`
- Descarga desde: Maven Central Repository

## Configuración de Gmail

Para que el proyecto funcione correctamente, necesitas configurar tu cuenta de Gmail:

### Paso 1: Habilitar IMAP en Gmail
1. Accede a Gmail
2. Ve a **Configuración** → **Ver todos los ajustes**
3. Ve a la pestaña **Reenvío y correo POP/IMAP**
4. Selecciona **Habilitar IMAP**
5. Guarda cambios

### Paso 2: Crear una Contraseña de Aplicación
⚠️ **NO uses tu contraseña de Gmail directamente**

Para cuentas con autenticación de 2 factores:
1. Ve a tu cuenta de Google: https://myaccount.google.com/
2. Selecciona **Seguridad**
3. En "Acceso a Google", selecciona **Contraseñas de aplicaciones**
4. Genera una nueva contraseña de aplicación para "Correo"
5. Copia el código de 16 caracteres generado
6. Usa este código como contraseña en el servidor

Para cuentas sin 2FA:
1. Ve a https://myaccount.google.com/security
2. Activa "Acceso de aplicaciones menos seguras" (no recomendado)
3. O mejor: activa 2FA y usa contraseña de aplicación

## Compilación

Desde el directorio raíz del proyecto:

```powershell
javac -d ClassExercises/bin -cp ".;lib/javax.mail-1.6.2.jar" `
  ClassExercises/src/GmailHttpServer/GmailReader.java `
  ClassExercises/src/GmailHttpServer/HTTPServer.java `
  ClassExercises/src/GmailHttpServer/HTTPClient.java
```

## Ejecución

### Paso 1: Iniciar el Servidor HTTP (Terminal 1)

```powershell
cd C:\Users\whutc\Desktop\GitHub\PP-S\ClassExercises\bin
java -cp ".;../../lib/javax.mail-1.6.2.jar" GmailHttpServer.HTTPServer <tu-email@gmail.com> <tu-app-password>
```

**Ejemplo:**
```powershell
java -cp ".;../../lib/javax.mail-1.6.2.jar" GmailHttpServer.HTTPServer user@gmail.com "abcd efgh ijkl mnop"
```

**Salida esperada:**
```
Gmail credentials configured from command line
HTTP Server starting on port 8080
Access: http://localhost:8080/gmail
Client connected from: /127.0.0.1
Request: GET /gmail HTTP/1.1
```

### Paso 2: Ejecutar el Cliente HTTP (Terminal 2)

```powershell
cd C:\Users\whutc\Desktop\GitHub\PP-S\ClassExercises\bin
java -cp ".;../../lib/javax.mail-1.6.2.jar" GmailHttpServer.HTTPClient
```

**Salida esperada:**
```
HTTP Client starting...
Connecting to: http://localhost:8080/gmail
Request sent.
---
Response received:
================================================================================
Header: HTTP/1.1 200 OK
Header: Content-Type: text/html; charset=UTF-8
Header: Content-Length: 1234
Header: Connection: close
================================================================================

HTML Body:
--------------------------------------------------------------------------------
<html><head><title>Gmail Unread Messages</title></head><body>
<h1>Unread Gmail Messages</h1>
<p>Total unread messages: 5</p>
<hr>
...
</body></html>

Response saved to: gmail_response.html
Open this file in a web browser to view the formatted content.
```

### Paso 3: Ver los Resultados

Abre el archivo generado en tu navegador:
```powershell
start gmail_response.html
```

O accede directamente desde el navegador:
```
http://localhost:8080/gmail
```

## Formato de Respuesta HTML

El servidor devuelve una página HTML con:
- **Título**: "Gmail Unread Messages"
- **Contador**: Total de mensajes no leídos
- **Lista de mensajes**: Hasta los últimos 10 mensajes no leídos con:
  - Asunto del mensaje
  - Remitente (From)
  - Fecha de envío

**Ejemplo de respuesta HTML:**

```html
<html>
<head><title>Gmail Unread Messages</title></head>
<body>
  <h1>Unread Gmail Messages</h1>
  <p>Total unread messages: 3</p>
  <hr>
  <div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>
    <h3>Meeting Tomorrow at 10 AM</h3>
    <p><strong>From:</strong> boss@company.com</p>
    <p><strong>Date:</strong> Sun Feb 09 14:30:15 GMT 2026</p>
  </div>
  <div style='border: 1px solid #ccc; padding: 10px; margin: 10px 0;'>
    <h3>Your Package Has Been Delivered</h3>
    <p><strong>From:</strong> noreply@shipping.com</p>
    <p><strong>Date:</strong> Sun Feb 09 09:15:42 GMT 2026</p>
  </div>
</body>
</html>
```

## Protocolo HTTP Implementado

### Petición HTTP GET (Cliente → Servidor)
```http
GET /gmail HTTP/1.1
Host: localhost
Connection: close

```

### Respuesta HTTP 200 OK (Servidor → Cliente)
```http
HTTP/1.1 200 OK
Content-Type: text/html; charset=UTF-8
Content-Length: 1234
Connection: close

<html>...</html>
```

## Manejo de Errores

### Error: Credenciales no configuradas
```html
<h1>Error</h1>
<p>Gmail credentials not configured. Please restart server with: 
   java GmailHttpServer.HTTPServer &lt;email&gt; &lt;app-password&gt;
</p>
```

### Error: Autenticación fallida
```html
<p style='color: red;'><strong>Error:</strong> Authentication failed</p>
<p>Make sure you:</p>
<ul>
  <li>Use an App Password instead of your Gmail password</li>
  <li>Enable IMAP in Gmail settings</li>
  <li>Allow less secure apps or use 2-factor authentication with App Password</li>
</ul>
```

### Error: Ruta no encontrada
```html
<h1>Error</h1>
<p>Path not found: /other
<br>Try: <a href='/gmail'>/gmail</a></p>
```

## Características Técnicas

### Servidor HTTP
- **Puerto**: 8080
- **Protocolo**: HTTP/1.1
- **Métodos soportados**: GET
- **Rutas**: `/` y `/gmail`
- **Concurrencia**: Multi-threaded (un thread por cliente)
- **Content-Type**: text/html; charset=UTF-8

### Conexión Gmail
- **Protocolo**: IMAP sobre SSL/TLS
- **Host**: imap.gmail.com
- **Puerto**: 993
- **Carpeta**: INBOX
- **Modo**: READ_ONLY
- **Filtro**: Mensajes no leídos (Flag.SEEN = false)

### Cliente HTTP
- **Socket TCP**: java.net.Socket
- **Guardar respuesta**: gmail_response.html
- **Encoding**: UTF-8

## Seguridad

⚠️ **Consideraciones de seguridad importantes:**

1. **Nunca** incluyas credenciales en el código fuente
2. Usa **App Passwords** en lugar de tu contraseña real
3. Las credenciales se pasan como argumentos de línea de comandos
4. No compartas tu App Password
5. Revoca las App Passwords cuando no las necesites
6. El servidor NO debe exponerse a Internet sin autenticación adicional

## Troubleshooting

### "Authentication failed"
- Verifica que estés usando una App Password, no tu contraseña de Gmail
- Verifica que IMAP esté habilitado en Gmail
- Asegúrate de no tener espacios extra en el email o password

### "Connection refused"
- El servidor HTTP no está ejecutándose
- Verifica que el puerto 8080 esté libre
- Asegúrate de estar usando `localhost` o `127.0.0.1`

### "Connection timeout"
- Verifica tu conexión a Internet
- Gmail puede estar bloqueando la conexión
- Verifica firewall y antivirus

### Sin mensajes no leídos
- El servidor funciona correctamente pero tu bandeja de entrada no tiene mensajes sin leer
- Marca algunos emails como no leídos para probar

## Testing

Para probar el sistema sin Gmail real, puedes:
1. Comentar la llamada a `GmailReader` en HTTPServer
2. Devolver HTML mock con mensajes de ejemplo
3. Probar sólo la comunicación HTTP Client/Server

## Mejoras Futuras

- [ ] Soporte para parámetros de query (?limit=20, ?folder=Sent)
- [ ] Autenticación HTTP Basic para el servidor
- [ ] Soporte para otras operaciones (marcar como leído, eliminar)
- [ ] Interfaz web interactiva con JavaScript
- [ ] Soporte para adjuntos y contenido HTML de emails
- [ ] Cacheo de mensajes para mejorar rendimiento
- [ ] SSL/TLS en el servidor HTTP
- [ ] Logging con Log4J

## Referencias

- [JavaMail API Documentation](https://javaee.github.io/javamail/)
- [Gmail IMAP Settings](https://support.google.com/mail/answer/7126229)
- [Google App Passwords](https://support.google.com/accounts/answer/185833)
- [HTTP/1.1 RFC 2616](https://www.rfc-editor.org/rfc/rfc2616)

## Autor

Kudos MH Task - HTTP Client/Server con acceso a Gmail  
Ejercicio voluntario de Programación de Servicios y Procesos
