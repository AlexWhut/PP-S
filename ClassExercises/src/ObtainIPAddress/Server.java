package ObtainIPAddress;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5000;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for client...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            int[] ipBytes = new int[4];
            for (int i = 0; i < 4; i++) {
                ipBytes[i] = in.readInt();
            }
            String ipAddress = String.format("%d.%d.%d.%d", ipBytes[0], ipBytes[1], ipBytes[2], ipBytes[3]);
            System.out.println("Received IP Address: " + ipAddress);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
