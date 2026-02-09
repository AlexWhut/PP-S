# UDP ThreadPool con Log4J - Ejercicio Voluntario

## Descripción

Este proyecto implementa un sistema distribuido de logging donde:
- Un **servidor UDP con ThreadPool** procesa peticiones de clientes UDP
- Los logs del servidor UDP se envían a través de **Log4J SocketAppender** (TCP)
- Un **servidor TCP** recibe, deserializa y guarda los logs en archivo

## Arquitectura

```
┌─────────────┐         UDP          ┌──────────────────┐
│ UDPClient   │ ◄──────────────────► │ UDPServer        │
└─────────────┘                      │ (ThreadPool)     │
                                     └──────────────────┘
                                              │
                                              │ TCP (Log4J)
                                              │ Socket
                                              ▼
                                     ┌──────────────────┐
                                     │ TCPLogServer     │
                                     │ - Consola        │
                                     │ - server_logs.txt│
                                     └──────────────────┘
```

## Archivos del Proyecto

- **UDPServerThreadPool.java**: Servidor UDP con pool de threads para manejar múltiples peticiones concurrentemente
- **ManejadorUDP.java**: Runnable que procesa cada petición UDP en un thread separado
- **UDPClient.java**: Cliente UDP que envía mensajes al servidor
- **TCPLogServer.java**: Servidor TCP que recibe, deserializa y guarda logs de Log4J
- **log4j2.xml**: Configuración de Log4J con SocketAppender y ConsoleAppender

## Características

### Log4J SocketAppender
- Envía logs serializados via TCP al servidor de logs
- Puerto configurado: 6000
- Formato: SerializedLayout (objetos LogEvent de Log4J)

### TCPLogServer
- Escucha en puerto 6000
- Deserializa eventos LogEvent de Log4J
- Imprime logs formateados en consola
- Guarda logs en archivo `server_logs.txt` (modo append)
- Maneja múltiples conexiones concurrentemente

### Formato de Logs
```
[2026-02-09 21:23:17] [INFO] Udpthreadpool.ManejadorUDP - Hilo pool-2-thread-1 procesando: Hola servidor UDP
```

## Compilación

Desde el directorio raíz del proyecto (`PP-S`):

```powershell
javac -d ClassExercises/bin -cp ".;lib/log4j-api-2.20.0.jar;lib/log4j-core-2.20.0.jar" `
  ClassExercises/src/Udpthreadpool/UDPClient.java `
  ClassExercises/src/Udpthreadpool/UDPServerThreadPool.java `
  ClassExercises/src/Udpthreadpool/ManejadorUDP.java `
  ClassExercises/src/Udpthreadpool/TCPLogServer.java
```

Copiar el archivo de configuración Log4J:
```powershell
Copy-Item ClassExercises/src/Udpthreadpool/log4j2.xml ClassExercises/bin/
```

## Ejecución

### 1. Iniciar el Servidor TCP de Logs (Terminal 1)
```powershell
cd C:\Users\whutc\Desktop\GitHub\PP-S\ClassExercises\bin
java -cp ".;../../lib/log4j-api-2.20.0.jar;../../lib/log4j-core-2.20.0.jar" Udpthreadpool.TCPLogServer
```

### 2. Iniciar el Servidor UDP (Terminal 2)
```powershell
cd C:\Users\whutc\Desktop\GitHub\PP-S\ClassExercises\bin
java -cp ".;../../lib/log4j-api-2.20.0.jar;../../lib/log4j-core-2.20.0.jar" Udpthreadpool.UDPServerThreadPool
```

### 3. Ejecutar el Cliente UDP (Terminal 3)
```powershell
cd C:\Users\whutc\Desktop\GitHub\PP-S\ClassExercises\bin
java -cp ".;../../lib/log4j-api-2.20.0.jar;../../lib/log4j-core-2.20.0.jar" Udpthreadpool.UDPClient
```

## Puertos Utilizados

- **Puerto UDP 9876**: Servidor UDP para peticiones de clientes
- **Puerto TCP 6000**: Servidor TCP para recepción de logs

## Salida Esperada

### Terminal 1 (TCPLogServer):
```
TCP Log Server escuchando en puerto 6000
Los logs se escribirán en: server_logs.txt
Conexión recibida de: /127.0.0.1
[2026-02-09 21:23:00] [INFO] Udpthreadpool.UDPServerThreadPool - Servidor UDP con ThreadPool en puerto 9876
[2026-02-09 21:23:17] [INFO] Udpthreadpool.UDPClient - Cliente UDP iniciado
[2026-02-09 21:23:17] [INFO] Udpthreadpool.ManejadorUDP - Hilo pool-2-thread-1 procesando: Hola servidor UDP
...
```

### Terminal 2 (UDPServerThreadPool):
```
[21:23:00] [INFO ] UDPServerThreadPool - Servidor UDP con ThreadPool en puerto 9876
```

### Terminal 3 (UDPClient):
```
[21:23:17] [INFO ] UDPClient - Cliente UDP iniciado
[21:23:17] [INFO ] UDPClient - Mensaje enviado al servidor: Hola servidor UDP
Respuesta del servidor: OK -> Hola servidor UDP
```

## Archivo de Logs

Los logs se guardan en: `ClassExercises/bin/server_logs.txt`

Este archivo contiene todos los logs generados por el servidor UDP y los clientes en formato append.

## Dependencias

- **log4j-api-2.20.0.jar**: API de Log4J
- **log4j-core-2.20.0.jar**: Implementación core de Log4J

Ubicación: `lib/` en el directorio raíz del proyecto

## Notas de Seguridad

⚠️ **Advertencia**: SerializedLayout está deprecado por razones de seguridad (vulnerabilidades de deserialización). Para producción, se recomienda usar JsonLayout:

```xml
<Socket name="Socket" host="localhost" port="6000">
  <JsonLayout compact="true" eventEol="true"/>
</Socket>
```

## Autor

Ejercicio voluntario "Kudos" - Logging distribuido con Log4J y sockets
