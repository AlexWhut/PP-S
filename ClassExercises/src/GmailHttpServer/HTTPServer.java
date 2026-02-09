package GmailHttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    private static final int PORT = 8080;
    private static String gmailUsername = "";
    private static String gmailPassword = "";
    
    public static void main(String[] args) {
        // Configurar credenciales de Gmail
        if (args.length >= 2) {
            gmailUsername = args[0];
            gmailPassword = args[1];
            System.out.println("Gmail credentials configured from command line");
        } else {
            System.out.println("WARNING: No Gmail credentials provided.");
            System.out.println("Usage: java GmailHttpServer.HTTPServer <gmail-user> <app-password>");
            System.out.println("Server will start but Gmail access will fail.");
        }
        
        System.out.println("HTTP Server starting on port " + PORT);
        System.out.println("Access: http://localhost:" + PORT + "/gmail");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from: " + clientSocket.getInetAddress());
                
                // Manejar la petición en un thread separado
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {
            
            // Leer la petición HTTP
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);
            
            // Leer headers (aunque no los usemos)
            while (!in.readLine().isEmpty()) {
                // Skip headers
            }
            
            String response;
            
            if (requestLine != null && requestLine.startsWith("GET")) {
                String path = requestLine.split(" ")[1];
                
                if (path.equals("/gmail") || path.equals("/")) {
                    // Acceder a Gmail y obtener mensajes
                    if (gmailUsername.isEmpty() || gmailPassword.isEmpty()) {
                        response = buildErrorResponse("Gmail credentials not configured. " +
                            "Please restart server with: java GmailHttpServer.HTTPServer <email> <app-password>");
                    } else {
                        GmailReader reader = new GmailReader(gmailUsername, gmailPassword);
                        String htmlContent = reader.getUnreadMessages();
                        response = buildHttpResponse(htmlContent);
                    }
                } else {
                    response = buildErrorResponse("Path not found: " + path + 
                        "<br>Try: <a href='/gmail'>/gmail</a>");
                }
            } else {
                response = buildErrorResponse("Invalid HTTP request");
            }
            
            // Enviar respuesta
            out.write(response.getBytes());
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }
    
    private static String buildHttpResponse(String htmlContent) {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 200 OK\r\n");
        response.append("Content-Type: text/html; charset=UTF-8\r\n");
        response.append("Content-Length: ").append(htmlContent.length()).append("\r\n");
        response.append("Connection: close\r\n");
        response.append("\r\n");
        response.append(htmlContent);
        return response.toString();
    }
    
    private static String buildErrorResponse(String message) {
        String html = "<html><head><title>Error</title></head><body>" +
                     "<h1>Error</h1><p>" + message + "</p></body></html>";
        return buildHttpResponse(html);
    }
}
