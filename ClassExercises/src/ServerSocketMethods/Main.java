package ServerSocketMethods;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	public static void main(String[] args) {
		final int PORT = 5050;
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("ServerSocket created.");
			System.out.println("isBound: " + serverSocket.isBound());
			System.out.println("isClosed: " + serverSocket.isClosed());
			System.out.println("Local port: " + serverSocket.getLocalPort());
			System.out.println("Waiting for client connection...");

			Socket clientSocket = serverSocket.accept();
			System.out.println("Client connected!");
			System.out.println("Client address: " + clientSocket.getInetAddress());
			System.out.println("Client port: " + clientSocket.getPort());
			System.out.println("Local socket address: " + clientSocket.getLocalSocketAddress());
			System.out.println("isConnected: " + clientSocket.isConnected());
			System.out.println("isClosed: " + clientSocket.isClosed());

			// Close client socket
			clientSocket.close();
			System.out.println("Client socket closed. isClosed: " + clientSocket.isClosed());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
