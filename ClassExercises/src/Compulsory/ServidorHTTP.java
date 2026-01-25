import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorHTTP {
    public static void main(String[] args) throws Exception {
        final int PORT = 8080;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("HTTP Server started on port " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {

                    String requestLine = in.readLine();
                    System.out.println("Request: " + requestLine);

                    if (requestLine != null && requestLine.startsWith("GET / ")) {
                        String json = "{\"message\": \"Hello from my HTTP Java Server\", \"status\": \"ok\"}";
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: application/json\r\n" +
                                "Content-Length: " + json.length() + "\r\n" +
                                "\r\n" +
                                json;
                        out.print(response);
                    } else {
                        String json = "{\"error\": \"Resource not found\"}";
                        String response = "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Type: application/json\r\n" +
                                "Content-Length: " + json.length() + "\r\n" +
                                "\r\n" +
                                json;
                        out.print(response);
                    }
                    out.flush();
                }
            }
        }
    }
}
