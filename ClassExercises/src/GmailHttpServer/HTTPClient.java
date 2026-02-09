package GmailHttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final String REQUEST_PATH = "/gmail";
    
    public static void main(String[] args) {
        System.out.println("HTTP Client starting...");
        System.out.println("Connecting to: http://" + SERVER_HOST + ":" + SERVER_PORT + REQUEST_PATH);
        
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             OutputStream out = socket.getOutputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            // Construir petición HTTP GET
            String httpRequest = "GET " + REQUEST_PATH + " HTTP/1.1\r\n" +
                                "Host: " + SERVER_HOST + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n";
            
            // Enviar petición
            out.write(httpRequest.getBytes());
            out.flush();
            System.out.println("Request sent.");
            System.out.println("---");
            
            // Leer respuesta
            System.out.println("Response received:");
            System.out.println("=".repeat(80));
            
            String line;
            boolean isBody = false;
            StringBuilder body = new StringBuilder();
            
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) {
                    // Línea vacía separa headers del body
                    isBody = true;
                    continue;
                }
                
                if (isBody) {
                    body.append(line).append("\n");
                } else {
                    // Imprimir headers
                    System.out.println("Header: " + line);
                }
            }
            
            System.out.println("=".repeat(80));
            System.out.println("\nHTML Body:");
            System.out.println("-".repeat(80));
            System.out.println(body.toString());
            
            // Guardar respuesta en archivo
            String filename = "gmail_response.html";
            try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
                writer.write(body.toString());
                System.out.println("\nResponse saved to: " + filename);
                System.out.println("Open this file in a web browser to view the formatted content.");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
