package Udpthreadpool;

import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.core.LogEvent;

public class TCPLogServer {
    private static final String LOG_FILE = "server_logs.txt";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        int port = 6000; // Puerto para recibir logs
        System.out.println("TCP Log Server escuchando en puerto " + port);
        System.out.println("Los logs se escribirán en: " + LOG_FILE);
        
        try (ServerSocket serverSocket = new ServerSocket(port);
             PrintWriter fileWriter = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Conexión recibida de: " + clientSocket.getInetAddress());
                    
                    // Crear un thread para manejar la conexión
                    new Thread(() -> handleClient(clientSocket, fileWriter)).start();
                    
                } catch (Exception e) {
                    System.err.println("Error aceptando conexión: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void handleClient(Socket clientSocket, PrintWriter fileWriter) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {
            while (true) {
                try {
                    // Leer el evento de log serializado
                    Object obj = ois.readObject();
                    
                    if (obj instanceof LogEvent) {
                        LogEvent event = (LogEvent) obj;
                        String timestamp = dateFormat.format(new Date(event.getTimeMillis()));
                        String level = event.getLevel().toString();
                        String logger = event.getLoggerName();
                        String message = event.getMessage().getFormattedMessage();
                        
                        // Formatear el log
                        String logLine = String.format("[%s] [%s] %s - %s", 
                            timestamp, level, logger, message);
                        
                        // Imprimir en consola
                        System.out.println(logLine);
                        
                        // Escribir en archivo
                        synchronized (fileWriter) {
                            fileWriter.println(logLine);
                            fileWriter.flush();
                        }
                        
                        // Si hay excepción, imprimirla también
                        if (event.getThrown() != null) {
                            Throwable thrown = event.getThrown();
                            System.out.println("    Exception: " + thrown.getClass().getName() + 
                                ": " + thrown.getMessage());
                            fileWriter.println("    Exception: " + thrown.getClass().getName() + 
                                ": " + thrown.getMessage());
                            fileWriter.flush();
                        }
                    }
                } catch (Exception e) {
                    // Fin del stream o error de lectura
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                // Ignorar
            }
        }
    }
}
